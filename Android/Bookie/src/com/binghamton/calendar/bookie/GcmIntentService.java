package com.binghamton.calendar.bookie;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.AndroidCalendarUtility;
import com.binghamton.calendar.util.AndroidUtility;
import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Event;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

/**
 * Service to handle push notifications
 */
public class GcmIntentService extends IntentService {
	public static int NOTIFICATION_ID;
    protected static String NOTIFICATION_CLICKED = "notificationClicked";
    private NotificationManager mNotificationManager;
    
    private static int count = 0;

    /**
     * Initialize variables
     */
    public GcmIntentService() {
        super("GcmIntentService");
    }

    /**
     * Parse the Event, add it to the calendar if sync is enabled, and send a notification
     * if the Settings allow it
     */
    @Override
    protected void onHandleIntent(Intent intent) {
    	NOTIFICATION_ID = AccountUtility.getCredentials(this)[0].hashCode();
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	Gson gson = EncryptionUtility.getGson();
            	String message = extras.getString("message");
            	AndroidCalendarUtility.add(this, message);
            	Event event = gson.fromJson(message, Event.class);
            	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            	if (sharedPref.getBoolean(SettingsFragment.PREF_NOTIFICATIONS_WANTED_KEY, false) && (event != null)) {
            		Locale locale = Locale.US;
            		DateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", locale);
            		DateFormat timeFormatter = new SimpleDateFormat("h:mm a", locale);
            		String tmp =  "Added Event On: " + dateFormatter.format(event.getStartDate().getTime()) + " at " + timeFormatter.format(event.getStartTime().getTime());
            		sendNotification(tmp);
            	}
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * Create a Notification to the user that an Event was
     * added to the calendar
     * @param msg The message for the notification
     */
    private void sendNotification(String msg) {
    	count++;
        mNotificationManager = AndroidUtility.getNotificationManager(this);

        Intent intent = new Intent(this, EventActivityA.class);
        intent.putExtra(NOTIFICATION_CLICKED, true);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String newEvent = "New Event";
        if (count > 1) {
        	newEvent += "s";
        	msg = "New events added";
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setAutoCancel(true)
        .setNumber(count)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle(newEvent)
        .setContentText(msg);
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    /**
     * Used to handle multiple notifications. When a notification
     * is clicked, reset the count of Events added.
     */
    protected static void resetNotificationVariables() {
    	count = 0;
    }

}
