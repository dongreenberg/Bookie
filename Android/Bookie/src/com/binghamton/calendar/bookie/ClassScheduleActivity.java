package com.binghamton.calendar.bookie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.AndroidCalendarUtility;
import com.binghamton.calendar.util.EditTextUtility;
import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.HTTPUtility;
import com.binghamton.calendar.util.Term;
import com.google.gson.Gson;

/**
 * Activity to import a class schedule from BUBrain into the
 * device calendar.
 */
public class ClassScheduleActivity extends HasMenuActivity {

	/**
	 * Retrieves the data from the appropriate fields and requests
	 * data from the server
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_schedule);
		final Context context = this;
		String[] credentials = AccountUtility.getCredentials(context);
		
		new GetTermsTask(context).execute(credentials[0], credentials[1], HTTPUtility.GET_TERMS);
		
		final EditText usernameField = (EditText) findViewById(R.id.username);
		final EditText passwordField = (EditText) findViewById(R.id.password);
		final Button go = (Button) findViewById(R.id.bubrain_go);
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Spinner spinner = (Spinner) findViewById(R.id.semester_spinner);
				Object sel = spinner.getSelectedItem();
				if (!EditTextUtility.hasEmptyFields(context, usernameField, passwordField) && (sel !=  null)) {
					new DownloadScheduleTask(context).execute(usernameField.getText().toString(), passwordField.getText().toString(), ((Term)sel).getTermID());
				}
			}
		});
	}

	/**
	 * Creates a background thread to HTTP POST to the server for the
	 * selected schedule
	 */
	private final class DownloadScheduleTask extends AsyncTask<String, Void, Boolean> {
		private Context context;
		private ProgressDialog dialog;
		private String[] args;
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public DownloadScheduleTask(Context context) {
			this.context = context;
			this.dialog = new ProgressDialog(this.context);
			args = new String[]{"Username", "Password", "Term"};
		}


		/**
		 * Creates a progress dialog to prohibit user
		 * interaction during the transaction
		 */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Your Schedule...");
			this.dialog.show();
		}

		/**
		 * POSTs to the server and adds to the calendar upon success
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (int x = 0; x < args.length; x++) {
				System.out.println(params[x]);
				nameValuePairs.add(new BasicNameValuePair(args[x], params[x]));
			}
			String result = HTTPUtility.doPost(context, "queryBUBrain", nameValuePairs);
			if (result.equals(HTTPUtility.ERROR)) {
				return false;
			}
			AndroidCalendarUtility.add(context, result, false);
			return true;
		}

		/**
		 * Alerts the user of the success or failure of the
		 * transaction
		 */
		protected void onPostExecute(Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (result) {
				HTTPUtility.showSuccess(context);
			}
			else {
				HTTPUtility.showError(context);
			}
		}

	}

	/**
	 * Task used to get the terms (semesters) available from the database
	 * on the server
	 */
	private final class GetTermsTask extends GetDataTask {
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public GetTermsTask(Context context) {
			super(context);
		}

		/**
		 * Populate the spinner with the data retrieved from the server
		 * @param result the JSON result to deserialize and populate the Spinner with.
		 */
		@Override
		protected void setData(String result) {
			Spinner spinner = (Spinner) findViewById(R.id.semester_spinner);
			ArrayAdapter<Term> adapter = new ArrayAdapter<Term>(this.getContext(), android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Gson gson  = EncryptionUtility.getGson();
			Term[] termArr = gson.fromJson(result, Term[].class);
			Arrays.sort(termArr);
			adapter.addAll(termArr);
			spinner.setAdapter(adapter);
		}
	}
}
