package com.binghamton.calendar.util;

import java.util.ArrayList;
import java.util.Calendar;

import com.binghamton.calendar.bookie.SettingsFragment;
import com.google.gson.Gson;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;

/**
 * Utility used for handling Calendar transactions
 */
public class AndroidCalendarUtility {
	
	//Code based on:
    //http://stackoverflow.com/questions/16242472/retrieve-the-default-calendar-id-in-android
    //http://stackoverflow.com/questions/7859005/how-to-read-and-edit-android-calendar-events-using-the-new-android-4-0-ice-cream
	/**
	 * Gets the AndroidCalendars on the device
	 * @param context The Context to use
	 * @return The list of AndroidCalendars on the device
	 */
	protected static AndroidCalendar[] getAndroidCalendars(Context context) {
    	ArrayList<AndroidCalendar> toReturn = new ArrayList<AndroidCalendar>();

		ContentResolver contentResolover = context.getContentResolver();
	    Uri uri = CalendarContract.Calendars.CONTENT_URI;
	    String[] projection = new String[] {
	           CalendarContract.Calendars._ID,
	           CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
	           CalendarContract.Calendars.CALENDAR_TIME_ZONE,
	    };

	    Cursor calendarCursor = contentResolover.query(uri, projection, null, null, null);
	    if (calendarCursor.moveToFirst()) {
	    	int idCol = calendarCursor.getColumnIndex(projection[0]);
	    	int accNameCol = calendarCursor.getColumnIndex(projection[1]);
	    	int timeZoneCol = calendarCursor.getColumnIndex(projection[2]);
	        do {
	        	String id = calendarCursor.getString(idCol);
	            String accName = calendarCursor.getString(accNameCol);
	            String timeZone = calendarCursor.getString(timeZoneCol);
	            if (!accName.matches(".*@.*\\.(com|edu|org)")) {
	            	continue;
	            }
	            toReturn.add(new AndroidCalendar(id, accName, timeZone));
	        } while(calendarCursor.moveToNext());
	        calendarCursor.close();
	    }
	    return toReturn.toArray(new AndroidCalendar[0]);
	}
	
	/**
	 * Gets the minutes between two Calendars
	 * @param startTime The start time
	 * @param endTime The end time
	 * @return The number of minutes between the two Calendars
	 */
	public static double getMinutes(Calendar startTime, Calendar endTime) {
		if (startTime == null || endTime == null) {
			return 0;
		}
		return ((double)(endTime.getTimeInMillis() - startTime.getTimeInMillis())) / 60000;
	}
	
	/**
	 * Add Event(s) to the Calendar based on the JSON string
	 * @param context The Context to use
	 * @param result The JSON holding the Event(s)
	 * @param checkSync true if sync preference should be checked, false
	 * otherwise
	 */
	public static void add(Context context, String result, boolean checkSync) {
		if (result.startsWith("{")) {
			Event event = EncryptionUtility.decryptEvent(result);
			add(context, event, checkSync);
		}
		else {
			Event[] events = EncryptionUtility.decryptEvents(result);
			add(context, events, checkSync);
		}
	}
	
	/**
	 * Add Event(s) to the Calendar based on the JSON string and
	 * check sync
	 * @param context The Context to use
	 */
	public static void add(Context context, String result) {
		add(context, result, true);
	}
	
	/**
	 * Add an Event to the Calendar and check sync
	 * @param context The Context to use
	 * @param event The Event to add
	 */
	public static void add(Context context, Event event) {
		add(context, new Event[]{event}, true);
	}
	
	/**
	 * Add an Event to the Calendar
	 * @param context The Context to use
	 * @param event The Event to add
	 * @param checkSync true if sync preference should be checked, false
	 * otherwise
	 */
	public static void add(Context context, Event event, boolean checkSync) {
		add(context, new Event[]{event}, checkSync);
	}
	
	/**
	 * Add an array of Events to the Calendar
	 * @param context The Context to use
	 * @param events The Events to add
	 * @param checkSync true if sync preference should be checked, false
	 * otherwise
	 */
	private static void add(Context context, Event[] events, boolean checkSync) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (!checkSync || sharedPref.getBoolean(SettingsFragment.PREF_SYNC_DEVICE_KEY, false)) {
			AndroidCalendar cal = AndroidCalendarUtility.getCalendarFromSettings(context);
			if (cal ==  null) {
				return;
			}
			cal.addEvents(context, events);
		}
	}
	
	/**
	 * Get the calendar chosen in settings
	 * @param context The Context to use
	 * @return The AndroidCalendar in the settings
	 */
	protected static AndroidCalendar getCalendarFromSettings(Context context) {
		Gson gson = EncryptionUtility.getGson();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String json = sharedPref.getString(SettingsFragment.PREF_CALENDAR_KEY, "");
		if (json.isEmpty()) {
			return null;
		}
		return gson.fromJson(json, AndroidCalendar.class);
	}
}
