package com.binghamton.calendar.bookie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.binghamton.calendar.util.AccountUtility;

/**
 * Activity to be used for extension only, not for direct use.
 * All Activities with the universal menu should extend this
 * Activity instead of Activity
 */
public class HasMenuActivity extends Activity {

	/**
	 * Initialize UI and variables
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Populate the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.has_menu, menu);
		return true;
	}

	/**
	 * Handles each menu item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.logout:
	        	AccountUtility.logOut(this);
	            return true;
	        case R.id.action_settings:
	        	Intent i = new Intent(this, SettingsActivity.class);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
