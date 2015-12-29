package com.binghamton.calendar.bookie;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * BroadcastReceiver for Push Notifications
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	/**
	 * Starts the Service to handle push notifications when one is received
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
	}

}
