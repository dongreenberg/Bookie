package com.binghamton.calendar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.binghamton.calendar.bookie.InsertDataTask;
import com.binghamton.calendar.bookie.MainActivity;
import com.binghamton.calendar.bookie.MainPageActivity;

/**
 * Holds variables/methods used across Activities dealing with
 * account management
 */
public class AccountUtility {
	public static final String USER_INFO_PREFS_NAME = "UserInfo";
	public static final String REG_PREFS_NAME = "RegistrationInfo";
	public static final String IS_LOGGED_IN = "isLoggedIn";
	public static final String USERNAME_PREF = "username";
	public static final String PASSWORD_PREF = "password";
	
	/**
	 * Save username and password to internal storage
	 * @param context The Context to use
	 * @param username the username
	 * @param password the password
	 */
	public static void saveCredentials(Context context, String username, String password) {
		SharedPreferences.Editor editor = getEditor(context);
		editor.putString(USERNAME_PREF, username);
		editor.putString(PASSWORD_PREF, password);
		editor.commit();
	}
	
	/**
	 * Get the username/password of the logged in user
	 * @param context The Context to use
	 * @return An array with index 0 as the username and index
	 * 1 as the password
	 */
	public static String[] getCredentials(Context context) {
		SharedPreferences sharedPref = getLogInSettings(context);
		String username = sharedPref.getString(AccountUtility.USERNAME_PREF, "");
		String password = sharedPref.getString(AccountUtility.PASSWORD_PREF, "");
		return new String[]{username, password};
	}
	
	/**
	 * Determines if the user is logged in
	 * @param context The Context to use
	 * @return true if logged in, false otherwise
	 */
	public static boolean isLoggedIn(Context context) {
		SharedPreferences settings = getLogInSettings(context);
		return settings.getBoolean(IS_LOGGED_IN, false);
	}
	
	/**
	 * Log in to Bookie
	 * @param context The Context to use
	 */
	public static void logIn(Context context) {
		if (!isLoggedIn(context)) {
			SharedPreferences.Editor editor = getEditor(context);
			editor.putBoolean(IS_LOGGED_IN, true);
			editor.commit();
			
			SharedPreferences regPref = context.getSharedPreferences(AccountUtility.REG_PREFS_NAME, Context.MODE_PRIVATE);
			final String[] credentials = AccountUtility.getCredentials(context);
			String mID = regPref.getString(MainActivity.PROPERTY_REG_ID, "");
			if (!mID.isEmpty()) {
				new InsertDataTask(context).execute(credentials[0], credentials[1], "AndroidDevice", mID);
			}

		}
		
		startNextIntent(context);
	}
	
	/**
	 * Log out of Bookie
	 * @param context The Context to use
	 */
	public static void logOut(Activity context) {
		SharedPreferences.Editor editor = getEditor(context);
		editor.clear();
		editor.commit();
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor prefEditor = sharedPref.edit();
		prefEditor.clear();
		prefEditor.commit();
		
    	Intent i = new Intent(context, MainActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    	context.startActivity(i);
    	context.finish();
	}
	
	/**
	 * Get the editor for log in settings preferences
	 * @param context The Context to use
	 * @return The editor for log in settings preferences
	 */
	private static SharedPreferences.Editor getEditor(Context context) {
		SharedPreferences settings = getLogInSettings(context);
		return settings.edit();
	}
	
	/**
	 * Get the log in settings
	 * @param context The Context to use
	 * @return The log in settings
	 */
	private static SharedPreferences getLogInSettings(Context context) {
		return context.getSharedPreferences(USER_INFO_PREFS_NAME, 0);
	}
	
	/**
	 * Start the MainPageActivity on successful login
	 * @param context The Context to use
	 */
	private static void startNextIntent(Context context) {
		Intent intent = new Intent(context, MainPageActivity.class);
	    context.startActivity(intent);
	}
}
