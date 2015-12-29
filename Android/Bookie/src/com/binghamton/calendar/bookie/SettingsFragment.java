package com.binghamton.calendar.bookie;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.binghamton.calendar.util.EncryptionUtility;

/**
 * Holds all of the logic for persistent Settings
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	public static final String PREF_SYNC_DEVICE_KEY = "pref_sync_device";
	public static final String PREF_NOTIFICATIONS_WANTED_KEY = "pref_notifications_wanted";
	public static final String PREF_REMINDER_WANTED_KEY = "pref_reminder_wanted";
	public static final String PREF_MINUTES_KEY = "pref_minutes";
	public static final String PREF_CALENDAR_KEY = "pref_calendar";

	/**
	 * Set up pre-exisiting XML preferences and add new preference to select
	 * a calendar from those registered on device.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);


		Preference minutesPref = findPreference(PREF_MINUTES_KEY);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		minutesPref.setSummary(sharedPref.getString(minutesPref.getKey(), ""));
		
		PreferenceScreen screen = getPreferenceScreen();
		ListPreference listPref = new ListPreference(getActivity());
		listPref.setKey(PREF_CALENDAR_KEY);
		if (!setUpCalendars(listPref)) {
			return;
		}

		String availableCalendars = "Available Calendars";
		listPref.setDialogTitle(availableCalendars); 
		listPref.setTitle(availableCalendars);
		String defaultString = "";
		int defaultIndex = 0;
		String val = sharedPref.getString(listPref.getKey(), defaultString);
		if (val.equals(defaultString)) {
			listPref.setDefaultValue(listPref.getEntryValues()[defaultIndex]);
			listPref.setValueIndex(defaultIndex);
		}
		else {
			listPref.setValue(val);
		}
		listPref.setSummary(listPref.getEntry());
		screen.addPreference(listPref);
	}
	
	/**
	 * Utility method to set up the ListPreference for available Calendars
	 * @param listPref The ListPreference to set up
	 * @return true on success, false otherwise
	 */
	private boolean setUpCalendars(ListPreference listPref) {
		String[][] cals = EncryptionUtility.getEncryptedCalendars(getActivity());
		if (EncryptionUtility.isArrayEmpty(cals)) {
			return false;
		}
		listPref.setEntryValues(cals[0]);
		listPref.setEntries(cals[1]);
		return true;
	}

	/**
	 * Updates the detail view of preferences with changing values
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(PREF_MINUTES_KEY)) {
			Preference minutesPref = findPreference(key);
			minutesPref.setSummary(sharedPreferences.getString(key, ""));
		}
		else if (key.equals(PREF_CALENDAR_KEY)) {
			ListPreference calendarPref = (ListPreference)findPreference(key);
			calendarPref.setSummary(calendarPref.getEntry());
		}
	}

	/**
	 * Persist preferences
	 */
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
		.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Persist preferences
	 */
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
		.unregisterOnSharedPreferenceChangeListener(this);
	}
}
