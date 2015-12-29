package com.binghamton.calendar.bookie;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.binghamton.calendar.util.AccountUtility;
import com.binghamton.calendar.util.EditTextUtility;
import com.binghamton.calendar.util.HTTPUtility;

/**
 * Activity used to create a Bookie account
 */
public class CreateBookieAccountActivity extends Activity {

	/**
	 * Retrieves the data from the appropriate fields and sends
	 * data to the server to create an account
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_bookie_account);
		final Context context = this;
		
		final EditText usernameField = (EditText) findViewById(R.id.app_create_username);
		final EditText firstnameField = (EditText) findViewById(R.id.firstname_editText);
		final EditText lastnameField = (EditText) findViewById(R.id.lastname_editText);
		final EditText bmailField = (EditText) findViewById(R.id.bmail_editText);
		final EditText passwordField = (EditText) findViewById(R.id.app_create_password);
		final EditText confirmPasswordField = (EditText) findViewById(R.id.app_confirm_password);
		
		final Button createAccountButton = (Button) findViewById(R.id.create_account_button);
		
		createAccountButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!EditTextUtility.hasEmptyFields(context, usernameField, firstnameField, lastnameField, bmailField, passwordField, confirmPasswordField)) {
					if (passwordField.getText().toString().equals(confirmPasswordField.getText().toString())) {
						new AppCreateAccountTask(context).execute(usernameField.getText().toString(), passwordField.getText().toString(), firstnameField.getText().toString(), lastnameField.getText().toString(), bmailField.getText().toString());
					}
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setMessage("\"Password\" Does Not Match \"Confirm Password.\"");
						builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
						builder.show();
					}
				}
			}
		});
	}
	
	
	/**
	 * Task used to create a background thread which will POST
	 * to the server to create an account
	 */
	private final class AppCreateAccountTask extends AsyncTask<String, Void, Boolean> {
		private Context context;
		private ProgressDialog dialog;
		private String[] args;
		
		/**
		 * Initialize variables
		 * @param context The Context to use
		 */
		public AppCreateAccountTask(Context context) {
			this.context = context;
			this.dialog = new ProgressDialog(this.context);
			args = new String[]{"Username", "Password", "Firstname", "Lastname", "Bmail"};
		}


		/**
		 * Create a progress dialog to limit user interaction
		 * during the transaction
		 */
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Creating Your Bookie Account...");
			this.dialog.show();
		}

		/**
		 * POST to the server and on success save the credentials (username/password)
		 * to internal storage
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (int x = 0; x < args.length; x++) {
				nameValuePairs.add(new BasicNameValuePair(args[x], params[x]));
			}
			String result = HTTPUtility.doPost(context, "createAccount", nameValuePairs);
			boolean isValid = Boolean.parseBoolean(result.toLowerCase(Locale.US));
			if (isValid) {
				AccountUtility.saveCredentials(context, params[0], params[1]);
			}
			return isValid;
		}

		/**
		 * Display a message on the success/failure of the account creation.
		 * If successful, log in immediately. Otherwise, enter new credentials.
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if (result) {
				builder.setMessage("Account Created Successfully");
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AccountUtility.logIn(context);
						dialog.dismiss();
					}
				});
			}
			else {
				builder.setMessage("Username Already Exists. Please Choose Another.")
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
