package com.binghamton.calendar.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.binghamton.calendar.bookie.SettingsFragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;

/**
 * Class used to represent an Android Device Calendar
 */
public class AndroidCalendar {
	private String id;
	private String name;
	private String timeZone;

	/**
	 * Create an AndroidCalendar, but only let the Utilities have access
	 * @param id The id of the calendar
	 * @param name The name of the calendar
	 * @param timeZone The timezone of the calendar
	 */
	protected AndroidCalendar(String id, String name, String timeZone) {
		this.id = id;
		this.name = name;
		this.timeZone = timeZone;
	}

	/**
	 * Gets the id
	 * @return the id
	 */
	protected String getId() {
		return id;
	}

	/**
	 * Gets the name
	 * @return the name
	 */
	protected String getName() {
		return name;
	}

	/**
	 * Gets the timezone
	 * @return The timezone
	 */
	protected String getTimeZone() {
		return timeZone;
	}

	/**
	 * Add an Event to this calendar
	 * @param context The Context to use
	 * @param event The Event to add
	 */
	protected void addEvent(Context context, Event event) {
		if (context == null || event == null) {
			return; //Silent Failure
		}
		Locale locale = Locale.US;
		DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", locale);

		ContentResolver cr = context.getContentResolver();

		long startMillis = 0;
		Calendar startTime = event.getStartTime();
		Calendar endTime = event.getEndTime();
		
		DayUtility.zeroOut(startTime);
		DayUtility.zeroOut(endTime);
		
		Calendar startDate = event.getStartDate();
		Calendar endDate = event.getEndDate();
		endDate.add(Calendar.DATE, 1); //Include end date on calendar (like length of an array)
		
		double totalMinutes = AndroidCalendarUtility.getMinutes(startTime, endTime);

		String days = event.getDays().toUpperCase(locale);
		
		if (days.length() < 1) {
			return;
		}
		
		String byDay = DayUtility.getByDay(days);
		
		Calendar beginTime = Calendar.getInstance();
		DayUtility.setDay(days, startDate);
		beginTime.set(startDate.get(Calendar.YEAR), 
				startDate.get(Calendar.MONTH),
				startDate.get(Calendar.DAY_OF_MONTH),
				startTime.get(Calendar.HOUR_OF_DAY),
				startTime.get(Calendar.MINUTE));
		startMillis = beginTime.getTimeInMillis();

		// Insert Event
		ContentValues values = new ContentValues();
		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DURATION, "+P" + (int)totalMinutes + "M");
		values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;UNTIL=" + dateFormatter.format(endDate.getTime()) + ";WKST=SU;BYDAY=" + byDay);
		values.put(CalendarContract.Events.TITLE, event.getName());
		if (event.getDescription().length() > 0) {
			values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
		}
		values.put(CalendarContract.Events.EVENT_LOCATION, event.getLocation());
		values.put(CalendarContract.Events.EVENT_TIMEZONE, this.getTimeZone());
		values.put(CalendarContract.Events.CALENDAR_ID, this.getId());
		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

		String eventID = uri.getLastPathSegment();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedPref.getBoolean(SettingsFragment.PREF_REMINDER_WANTED_KEY, false)) {
			String zeroMin = "0";
			String minutesString = sharedPref.getString(SettingsFragment.PREF_MINUTES_KEY, zeroMin);
			if (minutesString.isEmpty()) {
				minutesString = zeroMin;
			}
			int minutes = Integer.parseInt(minutesString);
			if (minutes > 0) {
				ContentValues reminderValues = new ContentValues();
				reminderValues.put(Reminders.MINUTES, minutes);
				reminderValues.put(Reminders.EVENT_ID, eventID);
				reminderValues.put(Reminders.METHOD, Reminders.METHOD_ALERT);
				cr.insert(Reminders.CONTENT_URI, reminderValues);
			}
		}
	}

	/**
	 * Add a list of Events to the Calendar
	 * @param context The Context to use
	 * @param events The list of Events to add
	 */
	protected void addEvents(Context context, Event[] events) {
		if (context != null && events != null) {
			for (int x = 0; x < events.length; x++) {
				addEvent(context, events[x]);
			}
		}
	}
}
