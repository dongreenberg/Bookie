package com.binghamton.calendar.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to handle computations with Days
 */
public class DayUtility {
	private static Map<Character, Integer> calMap = new HashMap<Character, Integer>();
	private static Map<Character, String> byDayMap = new HashMap<Character, String>();

	private static final char OUR_SUNDAY = 'U';
	private static final char OUR_MONDAY = 'M';
	private static final char OUR_TUESDAY = 'T';
	private static final char OUR_WEDNESDAY = 'W';
	private static final char OUR_THURSDAY = 'R';
	private static final char OUR_FRIDAY = 'F';
	private static final char OUR_SATURDAY = 'S';

	
	static {
		calMap.put(OUR_SUNDAY, Calendar.SUNDAY);
		calMap.put(OUR_MONDAY, Calendar.MONDAY);
		calMap.put(OUR_TUESDAY, Calendar.TUESDAY);
		calMap.put(OUR_WEDNESDAY, Calendar.WEDNESDAY);
		calMap.put(OUR_THURSDAY, Calendar.THURSDAY);
		calMap.put(OUR_FRIDAY, Calendar.FRIDAY);
		calMap.put(OUR_SATURDAY, Calendar.SATURDAY);
		
		byDayMap.put(OUR_SUNDAY, "SU");
		byDayMap.put(OUR_MONDAY, "MO");
		byDayMap.put(OUR_TUESDAY, "TU");
		byDayMap.put(OUR_WEDNESDAY, "WE");
		byDayMap.put(OUR_THURSDAY, "TH");
		byDayMap.put(OUR_FRIDAY, "FR");
		byDayMap.put(OUR_SATURDAY, "SA");
	}
	
	/**
	 * Gets the Calendar day based on the parameter char
	 * @param day The day to get the mapping for
	 * @return The Calendar day mapping
	 */
	private static int getCalendarMapping(char day) {
		if (!calMap.containsKey(day)) {
			return -1;
		}
		return calMap.get(day);
	}
	
	/**
	 * Gets the BYDAY mapping based on the parameter char
	 * @param day The day to get the mapping for
	 * @return The BYDAY mapping
	 */
	private static String getByDayMapping(char day) {
		if (!byDayMap.containsKey(day)) {
			return "ERROR_IN_BY_DAY";
		}
		return byDayMap.get(day);
	}
	
	/**
	 * Zero out the year, month, and day of month
	 * @param cal The Calendar to zero out
	 */
	public static void zeroOut(Calendar cal) {
		cal.set(Calendar.YEAR, 0);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 0);
	}
	
	/**
	 * Set the closest day based on the recurrence of days
	 * Code adapted from:
	 * http://stackoverflow.com/questions/3463756/is-there-a-good-way-to-get-the-date-of-the-coming-wednesday
	 * @param days The recurrence of days
	 * @param cal The calendar to set
	 */
	public static void setDay(String days, Calendar cal) {
		char day = days.charAt(0);
		int dayOfWeek = getCalendarMapping(day);
		if (dayOfWeek == -1) {
			return;
		}
		
        int diff = dayOfWeek - cal.get(Calendar.DAY_OF_WEEK);
        if (diff < 0) {
            diff += 7;
        }
        cal.add(Calendar.DAY_OF_MONTH, diff);
    }
	
	/**
	 * Create the BYDAY String needed to add to a Calendar
	 * @param days The days to map
	 * @return The BYDAY String
	 */
	public static String getByDay(String days) {
		String byDay = getByDayMapping(days.charAt(0));
		for (int y = 1; y < days.length(); y++) {
			byDay = byDay + "," + getByDayMapping(days.charAt(y));
		}
		return byDay;
	}
	
}
