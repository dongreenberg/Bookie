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
import com.binghamton.calendar.util.HTTPUtility;
import com.binghamton.calendar.util.Organization;
import com.google.gson.Gson;

/**
 * Fragment for a ListView of Organizations
 */
public class OrganizationListFragment extends ListFragment {
	
	private Organization[] orgsList;
	private OnOrganizationSelectedListener mListener;

	/**
	 * Populate the View
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View ret = super.onCreateView(inflater, container, savedInstanceState);		
		return ret;
	}
	
	/**
	 * The calling Activity must call this method to set the Organization list
	 * based on the button clicked (My/Browse Organizations). 
	 * @param myOrgs true if Subscribed Organizations wanted, false for unsubscribed
	 */
	public void setOrgList(boolean myOrgs) {
		String toUse = HTTPUtility.BROWSE_ORGS;
		if (myOrgs) {
			toUse = HTTPUtility.MY_ORGS;
		} else {
		}
		String[] credentials = AccountUtility.getCredentials(getActivity());
		new GetOrganizationListTask(getActivity()).execute(credentials[0], credentials[1], toUse);
	}
	
	/**
	 * Sets the calling Activity as the listener for click events
	 */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnOrganizationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnOrganizationSelectedListener");
        }
    }
	
    /**
     * Gets the specified Organization and calls the method to update the UI
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onOrganizationSelected(orgsList[position]);
    }
    
    /**
	 * Get the list of Organizations
	 * @return The list of Organizations
	 */
    public Organization[] getOrganizations() {
    	return orgsList;
    }
	
    /**
     * Interface to select Organizations
     */
	public interface OnOrganizationSelectedListener {
        public void onOrganizationSelected(Organization org);
    }
	
	/**
	 * Task used to POST to the server to get Organizations
	 */
	private final class GetOrganizationListTask extends GetDataTask {
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public GetOrganizationListTask(Context context) {
			super(context);
		}
		
		/**
		 * Populates the list adapter with the retrieved Organizations
		 * @param result The JSON of encoded Organizations
		 */
		@Override
		protected void setData(String result) {
			ArrayAdapter<Organization> myAdapter = new ArrayAdapter<Organization>(getActivity(), android.R.layout.simple_list_item_1);
			Gson gson = EncryptionUtility.getGson();
			orgsList = gson.fromJson(result, Organization[].class);
			myAdapter.addAll(orgsList);
			setListAdapter(myAdapter);
		}
	}
}
