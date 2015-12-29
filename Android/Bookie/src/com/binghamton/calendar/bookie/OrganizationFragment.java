package com.binghamton.calendar.bookie;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Event;
import com.binghamton.calendar.util.Organization;
import com.google.gson.Gson;

/**
 * Fragment used to display Organization details
 */
public class OrganizationFragment extends Fragment {

	/**
	 * Necessary to load the View properly
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization_page, container, false);
    }
	
	/**
	 * Populate the fields and UI elements with the Organization details
	 * @param org The Organization with which to populate the UI
	 */
	public void updateContent(final Organization org) {
		TextView organizationTextView = ((TextView)getActivity().findViewById(R.id.organization_title_textview));
		organizationTextView.setText(org.getName());
		
		TextView organizationDescriptionTextView = ((TextView)getActivity().findViewById(R.id.organization_description_textview));
		organizationDescriptionTextView.setText(org.getDescription());
		
		TextView organizationContactTextView = ((TextView)getActivity().findViewById(R.id.organization_contact_textview));
		organizationContactTextView.setText("Contact: " + org.getContact());
		
		ArrayAdapter<Event> myAdapter = new ArrayAdapter<Event>(getActivity(), android.R.layout.simple_list_item_1);
		myAdapter.addAll(org.getEventList());
		
		ListView eventsListView = (ListView)getActivity().findViewById(R.id.events_listview);
		eventsListView.setAdapter(myAdapter);
		eventsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), EventActivityB.class);
				Gson gson = EncryptionUtility.getGson();
				String encodedEvent = gson.toJson(org.getEventList()[arg2], Event.class);
				intent.putExtra(EventActivityA.EVENT_KEY, encodedEvent);
				startActivity(intent);
			}
		});
		
		final String[] credentials = AccountUtility.getCredentials(getActivity());
		final Button subsButton = ((Button)getActivity().findViewById(R.id.subscription_button));
		boolean subscribed = org.isSubscribed();
		subsButton.setText(subscribed ? "Unsubscribe" : "Subscribe");
		subsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean subscribed = org.isSubscribed();
				subsButton.setText(subscribed ? "Subscribe" : "Unsubscribe");
				new InsertDataTask(getActivity()).execute(credentials[0], credentials[1], (subscribed ? "Unsubs" : "Subs"), Integer.toString(org.getOrgNumber()));
				org.setSubscribed(!org.isSubscribed());
			}
		});
	}
}