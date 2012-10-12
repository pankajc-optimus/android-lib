/**
 * Add appropriate receiver entry in Android Manifest.
 */
package com.optimus.atul.librarysms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.widget.Toast;

public abstract class LibSMSReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras == null)
			return;

		Object[] pdus = (Object[]) extras.get("pdus");

		for (int i = 0; i < pdus.length; i++) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String fromAddress = message.getOriginatingAddress();
			String fromDisplayName = fromAddress;

			Uri uri;
			String[] projection;

			uri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(fromAddress));
			projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };

			Cursor cursor = context.getContentResolver().query(uri, projection,
					null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst())
					fromDisplayName = cursor.getString(0);
				cursor.close();
			}

			Toast toast = Toast.makeText(context,
					"Received SMSss: " + message.getMessageBody(), Toast.LENGTH_LONG);
					toast.show();
			onSMSReceived(message, fromAddress, fromDisplayName);
			break;
		}
	}

	public abstract void onSMSReceived(SmsMessage message, String fromAddress,
			String fromDisplayName);
}
