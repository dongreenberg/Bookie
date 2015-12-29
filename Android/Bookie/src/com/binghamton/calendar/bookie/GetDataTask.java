package com.binghamton.calendar.bookie;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.binghamton.calendar.util.HTTPUtility;

/**
 * Abstract Task used to retrieve Data from the server
 */
public abstract class GetDataTask extends AsyncTask<String, Void, String> {
	private Context context;
	private ProgressDialog dialog;
	private String[] args;
	
	/**
	 * Initialize variables
	 * @param context The Context to use
	 */
	public GetDataTask(Context context) {
		this.context = context;
		this.dialog = new ProgressDialog(this.context);
		args = new String[]{"Username", "Password", "QueryType"};
	}

	/**
	 * Get the Context
	 * @return the Context used
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Create a progress dialog
	 */
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage("Retrieving Data...");
		this.dialog.show();
	}

	/**
	 * Do a POST to query the database on the server
	 */
	@Override
	protected String doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (int x = 0; x < args.length; x++) {
			nameValuePairs.add(new BasicNameValuePair(args[x], params[x]));
		}
		String result = HTTPUtility.doPost(context, "queryDatabase", nameValuePairs);
		return result;
	}

	/**
	 * Use an alert dialog to tell the user of the success/failure
	 * of the query. If successful, populate the UI appropriately
	 */
	@Override
	protected final void onPostExecute(String result) {
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		if (!result.equals(HTTPUtility.ERROR)) {
			setData(result);
		}
		else {
			HTTPUtility.showError(context);
		}
	}
	
	/**
	 * Implement to populate the UI accordingly
	 * @param result The JSON with the result of the query
	 */
	protected abstract void setData(String result);
}