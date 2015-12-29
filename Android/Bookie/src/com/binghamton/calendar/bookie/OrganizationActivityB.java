package com.binghamton.calendar.bookie;

import android.os.Bundle;

import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.Organization;
import com.google.gson.Gson;

/**
 * Activity for handset devices to display the Organization detail view
 */
public class OrganizationActivityB extends HasMenuActivity {

	/**
	 * Populate the Organization detail view with the Organization sent as an extra
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization_activity_b);
		OrganizationFragment organizationFragment = (OrganizationFragment) getFragmentManager()
				.findFragmentById(R.id.organization_frag);
		String orgEncrypted = getIntent().getStringExtra(OrganizationActivityA.ORGANIZATION_KEY);
		Gson gson = EncryptionUtility.getGson();
		organizationFragment.updateContent(gson.fromJson(orgEncrypted, Organization.class));
	}
}
