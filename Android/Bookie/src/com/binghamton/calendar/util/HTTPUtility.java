package com.binghamton.calendar.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.binghamton.calendar.bookie.MainPageActivity;
import com.binghamton.calendar.bookie.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Utility for handling HTTP requests 
 */
public class HTTPUtility {
	public static final String IP = "http://192.168.1.105/";
	public static final String PHP = ".php";
	
	public static final String MY_ORGS = "myOrgs";
	public static final String BROWSE_ORGS = "browseOrgs";
	public static final String MY_EVENTS = "myEvents";
	public static final String GET_TERMS = "terms";
	public static final String ERROR = "Error";
	
	/**
	 * Carry out a POST
	 * @param context the Context to use
	 * @param phpPage The PHP page to POST to
	 * @param nameValuePairs The variables to send
	 * @return The result of the POST
	 */
	public static String doPost(Context context, String phpPage, List<NameValuePair> nameValuePairs) {
		String mURL = IP + phpPage + PHP;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mURL);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String result = EntityUtils.toString(response.getEntity());
			return result;

		} catch (ClientProtocolException e) {
			return ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	/**
	 * Show an error dialog
	 * @param context The Context to use
	 */
	public static void showError(final Context context) {
		showDialog(context, true);
	}
	
	/**
	 * Show a success dialog
	 * @param context The Context to use
	 */
	public static void showSuccess(final Context context) {
		showDialog(context, false);
	}
	
	/**
	 * Show a Dialog on the screen
	 * @param context The Context to use
	 * @param isError true if for an error, false otherwise
	 */
	private static void showDialog(final Context context, boolean isError) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(isError? "Error Contacting Server." : "Data Successfully Transferred.")
		.setTitle(isError ? "Error" : "Success");
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(context, MainPageActivity.class);
		    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    	context.startActivity(i);
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
