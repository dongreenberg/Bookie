package com.binghamton.calendar.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Used to handle JSON serialization/deserialization
 */
public class EncryptionUtility {
	
	/**
	 * Gets a GSON object to handle serialization/deserialization
	 * univerisally in the app
	 * @return The GSON object
	 */
	public static Gson getGson() {
		return new GsonBuilder().disableHtmlEscaping().create();
	}
	
	/**
	 * Get the AndroidCalendars on the device and encrypt
	 * them in JSON for settings purposes
	 * @param context The Context to use
	 * @return The encoded calendars and names in a String[][] array
	 * Index 0 is the encoded calendar String array
	 * Index 1 is the String array of Calendar names
	 * (Parallel arrays)
	 */
	public static String[][] getEncryptedCalendars(Context context) {
		Gson gson = getGson();
		AndroidCalendar[] calendars = AndroidCalendarUtility.getAndroidCalendars(context);
		if ((calendars == null) || (calendars.length == 0)) {
			return new String[0][0];
		}
		String[] calendarsEncoded = new String[calendars.length];
		String[] calendarNames = new String[calendars.length];
		for (int x = 0; x < calendarsEncoded.length; x++) {
			calendarsEncoded[x] = gson.toJson(calendars[x]);
			calendarNames[x] = calendars[x].getName();
		}
		String[][] returnVal = new String[2][calendars.length];
		returnVal[0] = calendarsEncoded;
		returnVal[1] = calendarNames;
		return returnVal;
	}
	
	/**
	 * Checks if a String[][] is empty
	 * @param cals The array to check
	 * @return true if empty, false otherwise
	 */
	public static boolean isArrayEmpty(String[][] cals) {
		if ((cals == null) || (cals.length == 0) || (cals[0] == null) || (cals[0].length == 0)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Deserialize the result String
	 * @param result The String to deserialize
	 * @return The Event[] deserialized from the parameter
	 */
	public static Event[] decryptEvents(String result) {
		Gson gson = EncryptionUtility.getGson();
		return gson.fromJson(result, Event[].class);
	}
	
	/**
	 * Deserialize the result String
	 * @param result The String to deserialize
	 * @return The Event deserialized from the parameter
	 */
	public static Event decryptEvent(String result) {
		Gson gson = EncryptionUtility.getGson();
		return gson.fromJson(result, Event.class);
	}
}
