package com.binghamton.calendar.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Utility for handling Android specific tasks
 */
public class AndroidUtility {
	
	/**
	 * Get a Fragment
	 * @param activity The Activity using the Fragment
	 * @param resId The ID of the Fragment
	 * @return The Fragment
	 */
	public static Fragment getFragment(Activity activity, int resId) {
		return activity.getFragmentManager().findFragmentById(resId);
	}
	
	/**
	 * Get the NotificationManager for the device
	 * @param context The Context to use
	 * @return The NotificationManager
	 */
	public static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
}
