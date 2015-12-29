package com.binghamton.calendar.bookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.binghamton.calendar.util.HTTPUtility;

/**
 * Task used to insert data into the database on the server
 */
public class InsertDataTask extends AsyncTask<String, Void, Boolean> {
	private Context context;
	private ProgressDialog dialog;
	private String[] args;
	
	/**
	 * Initialize variables
	 * @param context The Context to use
	 */
	public InsertDataTask(Context context) {
		this.context = context;
		this.dialog = new ProgressDialog(this.context);
		args = new String[]{"Username", "Password", "QueryType", "Entity"};
	}
	
	/**
	 * Create a progress dialog
	 */
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Sending Data...");
		this.dialog.show();
	}

	/**
	 * POST to the server with the data to insert
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (int x = 0; x < args.length; x++) {
			nameValuePairs.add(new BasicNameValuePair(args[x], params[x]));
		}
		String result = HTTPUtility.doPost(context, "insertDatabase", nameValuePairs);
		boolean isValid = Boolean.parseBoolean(result.toLowerCase(Locale.US));
		return isValid;
	}

	/**
	 * Show an alert dialog with an error if unsuccessful,
	 * otherwise continue normally.
	 */
	@Override
	protected final void onPostExecute(Boolean result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		if (!result) {
			HTTPUtility.showError(context);
		}
	}

}
