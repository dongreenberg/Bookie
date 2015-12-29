package com.binghamton.calendar.bookie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.EditTextUtility;
import com.binghamton.calendar.util.EncryptionUtility;
import com.binghamton.calendar.util.HTTPUtility;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Entry point for the app. If already logged in, continue to
 * the MainPageActivity. Otherwise, log in.
 */
public class MainActivity extends Activity {
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    String SENDER_ID = "881424139028";

    GoogleCloudMessaging gcm;

    String regid = "default";
	
	
    /**
     * Initialize UI and Google Cloud Messaging (GCM) push notification
     * services.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Context context = this;
		
		gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(getApplicationContext());
        if (regid.isEmpty()) {
            registerInBackground(getApplicationContext());
        }
        
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		if (AccountUtility.isLoggedIn(context)) {
			AccountUtility.logIn(context);
			finish();
		}
		else {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = sharedPref.edit();
			String[][] cals = EncryptionUtility.getEncryptedCalendars(this);
			if (!EncryptionUtility.isArrayEmpty(cals)) {
				editor.putString(SettingsFragment.PREF_CALENDAR_KEY, cals[0][0]);
				editor.commit();
			}
		}
		
		
		final EditText usernameField = (EditText) findViewById(R.id.app_username_editText);
		final EditText passwordField = (EditText) findViewById(R.id.app_password_editText);
		final TextView createAccountTextView = (TextView) findViewById(R.id.create_account_textView);
		final Button loginButton = (Button) findViewById(R.id.app_login);
		
		createAccountTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CreateBookieAccountActivity.class);
				startActivity(intent);
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!EditTextUtility.hasEmptyFields(context, usernameField, passwordField)) {
					new AppLoginTask(context).execute(usernameField.getText().toString(), passwordField.getText().toString());
				}
			}
		});
	}
	
	/**
	 * Get the device registration ID
	 * @param context The Context to use
	 * @return The device registration ID
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        return "";
	    }

	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * Get the preference file for GCM data
	 * @param context The Context to use
	 * @return the GCM preference file
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    return getSharedPreferences(AccountUtility.REG_PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * Gets the app version
	 * @param context The Context to use
	 * @return The app version
	 */
	private int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Register this device with GCM in the background
	 * @param context The Context to use
	 */
	private void registerInBackground(final Context context) {
	    new AsyncTask<Void, String, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                storeRegistrationId(context, regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        	
	        }
	    }.execute(null, null, null);
	}
		
	/**
	 * Store the registration ID in preferences
	 * @param context The Context to use
	 * @param regId The registration ID to store
	 */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Task used to log in to Bookie (checks username and password
     * combination on the server)
     */
	private final class AppLoginTask extends AsyncTask<String, Void, Boolean> {
		private Context context;
		private ProgressDialog dialog;
		private String[] args;
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public AppLoginTask(Context context) {
			this.context = context;
			this.dialog = new ProgressDialog(this.context);
			args = new String[] {"Username", "Password"};
		}

		/**
		 * Create a progress dialog
		 */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Logging in to Bookie...");
			this.dialog.show();
		}

		/**
		 * POST to the server to check that the credentials entered
		 * are valid
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (int x = 0; x < args.length; x++) {
				nameValuePairs.add(new BasicNameValuePair(args[x], params[x]));
			}
			String result = HTTPUtility.doPost(context, "validCredentials", nameValuePairs);
			boolean isValid = Boolean.parseBoolean(result.toLowerCase(Locale.US));
			if (isValid) {
				AccountUtility.saveCredentials(context, params[0], params[1]);
			}
			return isValid;
		}

		/**
		 * Alert the user to the success/failure of the credential check.
		 * On success, warn the user that multiple devices with sync on for
		 * the same calendar will have the event added multiple times.
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if (result) {
				builder.setMessage("If you are logged on to Bookie on another device and " +
						"sync is on for both, then events will be added to your calendar multiple " +
						"times. Please disable sync on this device (located in Settings) if you " +
						"are logged on to Bookie from another device.");
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AccountUtility.logIn(context);
						dialog.dismiss();
					}
				});
			}
			else {
				builder.setMessage("Username/Password Combination Incorrect")
				.setTitle("Error");
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
			}
			builder.show();
		}
	}
}
