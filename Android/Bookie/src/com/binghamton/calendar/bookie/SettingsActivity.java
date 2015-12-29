package com.binghamton.calendar.bookie;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity to hold persistent Settings
 */
public class SettingsActivity extends Activity {
	
	/**
	 * Set up UI
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
	
	
}
