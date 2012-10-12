package com.optimus.mobile;

import android.app.AlertDialog;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**Class name     : BarcodeReaderIntentClass.java
 * Description    : utility class which helps ease integration with Barcode Scanner
 * Responsibility : scan the bar code and parse its result * 
 * @author Pooja
 *
 */

public class BarcodeReaderIntentClass {
	public static final int REQUEST_CODE = 0; 

	public static final String DEFAULT_TITLE = "Install Barcode Scanner?";
	public static final String DEFAULT_MESSAGE = "This application requires Barcode Scanner. Would you like to install it?";
	public static final String DEFAULT_YES = "Yes";
	public static final String DEFAULT_NO = "No";

	// Supported Barcode formats
	public static final String PRODUCT_CODE_TYPES = "UPC_A,UPC_E,EAN_8,EAN_13";
	public static final String ONE_D_CODE_TYPES = PRODUCT_CODE_TYPES
			+ ",CODE_39,CODE_93,CODE_128";
	public static final String QR_CODE_TYPES = "QR_CODE";
	public static final String ALL_CODE_TYPES = null;

	private BarcodeReaderIntentClass() {

	}

	public static AlertDialog initiateScan(Activity activity) {
		return initiateScan(activity, DEFAULT_TITLE, DEFAULT_MESSAGE,
				DEFAULT_YES, DEFAULT_NO);
	}

	/**
	 * Scans
	 * @param activity
	 * @param stringTitle
	 * @param stringMessage
	 * @param stringButtonYes
	 * @param stringButtonNo
	 * @return
	 */
	public static AlertDialog initiateScan(Activity activity, int stringTitle,
			int stringMessage, int stringButtonYes, int stringButtonNo) {
		return initiateScan(activity, activity.getString(stringTitle),
				activity.getString(stringMessage),
				activity.getString(stringButtonYes),
				activity.getString(stringButtonNo));
	}

	public static AlertDialog initiateScan(Activity activity,
			CharSequence stringTitle, CharSequence stringMessage,
			CharSequence stringButtonYes, CharSequence stringButtonNo) {

		return initiateScan(activity, stringTitle, stringMessage,
				stringButtonYes, stringButtonNo, ALL_CODE_TYPES);
	}

	/**
	 * Invokes scanning.
	 * 
	 * @param stringTitle
	 *            title of dialog prompting user to download Barcode Scanner
	 * @param stringMessage
	 *            text of dialog prompting user to download Barcode Scanner
	 * @param stringButtonYes
	 *            text of button user clicks when agreeing to download Barcode
	 *            Scanner (e.g. "Yes")
	 * @param stringButtonNo
	 *            text of button user clicks when declining to download Barcode
	 *            Scanner (e.g. "No")
	 * @param stringDesiredBarcodeFormats
	 *            a comma separated list of codes you would like to scan for.
	 * @return an {@link AlertDialog} if the user was prompted to download the
	 *         app, null otherwise
	 * @throws InterruptedException
	 *             if timeout expires before a scan completes
	 */

	public static AlertDialog initiateScan(Activity activity,
			CharSequence stringTitle, CharSequence stringMessage,
			CharSequence stringButtonYes, CharSequence stringButtonNo,
			CharSequence stringDesiredBarcodeFormats) {
		Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);

		// check which types of codes to scan for
		if (stringDesiredBarcodeFormats != null) {
			// set the desired barcode types
			intentScan.putExtra("SCAN_FORMATS", stringDesiredBarcodeFormats);
		}

		try {
			activity.startActivityForResult(intentScan, REQUEST_CODE);
			return null;
		} catch (ActivityNotFoundException e) {
			return showDownloadDialog(activity, stringTitle, stringMessage,
					stringButtonYes, stringButtonNo);
		}
	}

	
	/**
	 * Function name   : showDownloadDialog
	 * responsibility  : to display the download dialog box for barcode reader
	 * @param activity
	 * @param stringTitle
	 * @param stringMessage
	 * @param stringButtonYes
	 * @param stringButtonNo
	 * @return         : DialogBox
	 */
	private static AlertDialog showDownloadDialog(final Activity activity,
			CharSequence stringTitle, CharSequence stringMessage,
			CharSequence stringButtonYes, CharSequence stringButtonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
		downloadDialog.setTitle(stringTitle);
		downloadDialog.setMessage(stringMessage);
		downloadDialog.setPositiveButton(stringButtonYes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						Uri uri = Uri
								.parse("market://search?q=pname:com.google.zxing.client.android");
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						activity.startActivity(intent);
					}
				});
		downloadDialog.setNegativeButton(stringButtonNo,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				});
		return downloadDialog.show();
	}

	
	/**Function name  : parseActivityResult
	 * Responsibility : parse the result after scanning the bar code
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 * @return        : BarcodeIntentResultClass object containing content and format of the barcode
	 */
	public static BarcodeIntentResultClass parseActivityResult(int requestCode,
			int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
				return new BarcodeIntentResultClass(contents, formatName);
			} else {
				return new BarcodeIntentResultClass(null, null);
			}
		}
		return null;
	}



}
