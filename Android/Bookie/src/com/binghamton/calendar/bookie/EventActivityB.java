package com.binghamton.calendar.bookie;

import android.os.Bundle;

import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Event;
import com.google.gson.Gson;

/**
 * Activity for handset devices to display the Event detail view
 */
public class EventActivityB extends HasMenuActivity {

	/**
	 * Populate the Event detail view with the Event sent as an extra
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_activity_b);
		EventFragment eventFragment = (EventFragment) getFragmentManager()
				.findFragmentById(R.id.event_frag);
		Gson gson = EncryptionUtility.getGson();
		Event event = gson.fromJson(getIntent().getStringExtra(EventActivityA.EVENT_KEY), Event.class);
		eventFragment.setEvent(event);
	}
}
