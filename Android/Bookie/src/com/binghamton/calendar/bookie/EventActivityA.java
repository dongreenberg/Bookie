package com.binghamton.calendar.bookie;

import android.content.Intent;
import android.os.Bundle;

import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Event;
import com.binghamton.calendar.util.AndroidUtility;
import com.google.gson.Gson;

/**
 * Activity to display an Event List and Event Detail View, side-by-side for tablets
 * On a handset device, this Activity only displays a list of Events
 */
public class EventActivityA extends HasMenuActivity implements EventListFragment.OnEventSelectedListener{

	private EventFragment eventFrag;
	private EventListFragment eventListFrag;
	
	protected static String EVENT_KEY = "event";
	
	/**
	 * Set up the Fragments which make up the Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_activity);
		
		if (getIntent().getExtras() != null) {
			boolean notificationClicked = getIntent().getExtras().getBoolean(GcmIntentService.NOTIFICATION_CLICKED);
			if (notificationClicked) {
				GcmIntentService.resetNotificationVariables();
			}
		}

		
		eventFrag = (EventFragment) AndroidUtility.getFragment(this, R.id.event_details_frag);
		eventListFrag = (EventListFragment) AndroidUtility.getFragment(this, R.id.event_list_frag);
		if ((eventFrag != null) && (eventListFrag.getEvents() != null) && (eventListFrag.getEvents().length > 0)) {
			eventFrag.setEvent(eventListFrag.getEvents()[0]);
		}
	}

	/**
	 * Populates the Event detail fragment with the selected Event from the list
	 * @param event The Event with which to populate the Event detail fragment
	 */
	@Override
	public void onEventSelected(Event event) {
		EventFragment eventFrag = (EventFragment) getFragmentManager()
				.findFragmentById(R.id.event_details_frag);
		if (eventFrag == null) {
			Intent intent = new Intent(this, EventActivityB.class);
			Gson gson = EncryptionUtility.getGson();
			String encodedEvent = gson.toJson(event, Event.class);
			intent.putExtra(EVENT_KEY, encodedEvent);
			startActivity(intent);
		} else {
			eventFrag.setEvent(event);
		}
	}
}
