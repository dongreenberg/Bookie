package com.binghamton.calendar.bookie;

import android.content.Intent;
import android.os.Bundle;

import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.AndroidUtility;
import com.binghamton.calendar.util.Organization;
import com.google.gson.Gson;

/**
 * Activity to display an Organization List and Organization Detail View, side-by-side for tablets
 * On a handset device, this Activity only displays a list of Organizations
 */
public class OrganizationActivityA extends HasMenuActivity implements OrganizationListFragment.OnOrganizationSelectedListener{
	
	protected static String ORGANIZATION_KEY = "organization";

	/**
	 * Set up the Fragments which make up the Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization_activity);
		
		OrganizationListFragment organizationListFrag = (OrganizationListFragment) AndroidUtility.getFragment(this, R.id.organization_list_frag);
		organizationListFrag.setOrgList(getIntent().getExtras().getBoolean(MainPageActivity.MY_ORGS_CLICKED));
		
		OrganizationFragment organizationFrag = (OrganizationFragment) AndroidUtility.getFragment(this, R.id.organization_details_frag);
		if ((organizationFrag != null) && (organizationListFrag.getOrganizations() != null) && (organizationListFrag.getOrganizations().length > 0)) {
			organizationFrag.updateContent(organizationListFrag.getOrganizations()[0]);
		}
	}

	/**
	 * Populates the Organization detail fragment with the selected Organization from the list
	 * @param org The Organization with which to populate the Organization detail fragment
	 */
	@Override
	public void onOrganizationSelected(Organization org) {
		OrganizationFragment organizationFrag = (OrganizationFragment) AndroidUtility.getFragment(this, R.id.organization_details_frag);
		if (organizationFrag == null) {
			Intent intent = new Intent(this, OrganizationActivityB.class);
			Gson gson = EncryptionUtility.getGson();
			String orgEncrypted = gson.toJson(org, Organization.class);
			intent.putExtra(ORGANIZATION_KEY, orgEncrypted);
			startActivity(intent);
		} else {
			organizationFrag.updateContent(org);
		}
	}
}
