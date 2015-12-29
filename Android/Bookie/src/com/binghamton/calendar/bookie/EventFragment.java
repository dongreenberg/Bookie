package com.binghamton.calendar.bookie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.AndroidCalendarUtility;
import com.binghamton.calendar.util.Event;

/**
 * Fragment used to display Event details
 */
public class EventFragment extends Fragment {

	/**
	 * Necessary to load the View properly
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_page, container, false);
    }
	
	/**
	 * Populate the fields and UI elements with the Event details
	 * @param event The Event with which to populate the UI
	 */
	public void setEvent(final Event event) {
		Locale locale = Locale.US;
		DateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", locale);
	    DateFormat timeFormatter = new SimpleDateFormat("h:mm a", locale);
		
		TextView titleView = (TextView)(getActivity().findViewById(R.id.event_title_textview));
		titleView.setText(event.getName());
		
		TextView descrView = (TextView)(getActivity().findViewById(R.id.event_description_textview));
		descrView.setText(event.getDescription());
		
		TextView locView = (TextView)(getActivity().findViewById(R.id.event_location_textview));
		locView.setText(event.getLocation());
		
		TextView daysView = (TextView)(getActivity().findViewById(R.id.event_days_textview));
		daysView.setText(event.getDays());
		
		TextView startDateView = (TextView)(getActivity().findViewById(R.id.event_startdate_textview));
		startDateView.setText(dateFormatter.format(event.getStartDate().getTime()));
		
		TextView endDateView = (TextView)(getActivity().findViewById(R.id.event_enddate_textview));
		endDateView.setText(dateFormatter.format(getDate(event.getEndDate())));
		
		TextView timeView = (TextView)(getActivity().findViewById(R.id.event_time_textview));
		timeView.setText(timeFormatter.format(getDate(event.getStartTime())) + " - " + timeFormatter.format(getDate(event.getEndTime())));
		
		final String[] credentials = AccountUtility.getCredentials(getActivity());
		final Button addEventButton = ((Button)getActivity().findViewById(R.id.add_event_button));
		addEventButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new InsertDataTask(getActivity()).execute(credentials[0], credentials[1], "RSVP", Integer.toString(event.getEventNumber()));
				AndroidCalendarUtility.add(getActivity(), event, false);
			}
		});
	}
	
	/**
	 * Gets the Date for a Calendar object
	 * @param cal The Calendar to use
	 * @return the Date for Calendar cal
	 */
	private Date getDate(Calendar cal) {
		return cal.getTime();
	}
}
