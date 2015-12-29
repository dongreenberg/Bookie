package com.binghamton.calendar.bookie;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Event;
import com.binghamton.calendar.util.HTTPUtility;
import com.google.gson.Gson;

/**
 * Fragment for a ListView of Events
 */
public class EventListFragment extends ListFragment {
	
	private OnEventSelectedListener mListener;
	private Event[] eventList;
	
	/**
	 * Populate the View and POST to the server to get the logged in user's registered Events
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View ret = super.onCreateView(inflater, container, savedInstanceState);
		String[] credentials = AccountUtility.getCredentials(getActivity());
		new GetEventListTask(getActivity()).execute(credentials[0], credentials[1], HTTPUtility.MY_EVENTS);
		return ret;
	}
	
	/**
	 * Get the user's Events
	 * @return The user's registered Events
	 */
	public Event[] getEvents() {
		return eventList;
	}
	
	/**
	 * Sets the calling activity as the listener for click events
	 */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEventSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEventSelectedListener");
        }
    }
	
    /**
     * Gets the specified Event and calls the method to update the UI
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onEventSelected(eventList[position]);
    }
	
    /**
     * Interface to select Events
     */
	public interface OnEventSelectedListener {
        public void onEventSelected(Event event);
    }
	
	/**
	 * Task used to POST to the server to get Events
	 */
	private final class GetEventListTask extends GetDataTask {
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public GetEventListTask(Context context) {
			super(context);
		}
		
		/**
		 * Populates the list adapter with the retrieved Events
		 * @param result The JSON of encoded Events
		 */
		@Override
		protected void setData(String result) {
			ArrayAdapter<Event> myAdapter = new ArrayAdapter<Event>(getActivity(), android.R.layout.simple_list_item_1);
			Gson gson = EncryptionUtility.getGson();
			eventList = gson.fromJson(result, Event[].class);
			myAdapter.addAll(eventList);
			setListAdapter(myAdapter);
		}
	}
}
