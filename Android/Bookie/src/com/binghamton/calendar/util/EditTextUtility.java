package com.binghamton.calendar.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.binghamton.calendar.bookie.R;

/**
 * Utility for Android EditText UI element
 */
public class EditTextUtility {
	
	/**
	 * Check if an EditText is empty
	 * @param editText The EditText to check
	 * @return true if empty, false otherwise
	 */
	private static boolean isEmpty(EditText editText) {
		return editText.getText().toString().isEmpty();
	}

	/**
	 * Check if any of the EditTexts in the list are empty.
	 * Create an alert dialog if one is
	 * @param context The Context to use
	 * @param list The list of EditTexts to check
	 * @return true if one is empty, false if none are empty
	 */
	public static boolean hasEmptyFields(Context context, EditText... list) {
		if (list != null) {
			for (int x = 0; x < list.length; x++) {
				if (isEmpty(list[x])) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Text fields cannot be empty.");
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
					builder.show();
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
