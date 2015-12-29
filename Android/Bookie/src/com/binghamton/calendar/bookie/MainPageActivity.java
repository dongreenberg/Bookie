package com.binghamton.calendar.bookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The MainPage once logged in. Has buttons to get subscribed
 * organizations, all other organizations, and registered events.
 * There is also a button to get one's class schedule
 */
public class MainPageActivity extends HasMenuActivity {
	protected static String MY_ORGS_CLICKED = "my_orgs_clicked";

	/**
	 * Initialize the UI with the proper listeners to start the right
	 * Activities on button clicks
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		final Context context = this;

		final Button myOrgsButton = (Button)findViewById(R.id.my_orgs_button);
		myOrgsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrganizationActivityA.class);
				intent.putExtra(MainPageActivity.MY_ORGS_CLICKED, true);
				startActivity(intent);
			}
		});
		
		final Button browseOrgsButton = (Button)findViewById(R.id.browse_orgs_button);
		browseOrgsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, OrganizationActivityA.class);
				intent.putExtra(MainPageActivity.MY_ORGS_CLICKED, false);
				startActivity(intent);
			}
		});
		
		final Button myEventsButton = (Button)findViewById(R.id.my_events_button);
		myEventsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, EventActivityA.class));
			}
		});
		
		final Button importScheduleButton = (Button)findViewById(R.id.import_schedule_button);
		importScheduleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, ClassScheduleActivity.class));
			}
		});
	}
}
