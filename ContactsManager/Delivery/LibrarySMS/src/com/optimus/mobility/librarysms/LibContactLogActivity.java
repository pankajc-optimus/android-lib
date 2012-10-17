///**
// * An activity which is executed whenever a particular contact is to be chosen for various attributes like Phone,Email,ImageURI,Name,Id,LookupId.
// * The necessity of starting this activity is due to the fact that the  
// */
//package com.optimus.mobility.librarysms;
//
//import android.app.Activity;
//import android.content.ContentUris;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.CommonDataKinds.Email;
//import android.provider.ContactsContract.CommonDataKinds.Phone;
//import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Data;
//import android.util.Log;
//import android.widget.Toast;
//
//public class LibContactLogActivity extends Activity {
//	private boolean showToasts = true;
//	public LibContactUtil returnObj;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// Initialize LibContactUtil object.
//		returnObj = new LibContactUtil();
//
//		Log.i("Executing Contact Picker Intent", "Success");
//		// Create intent to select content.
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//		// Fire activity for result.
//		startActivityForResult(intent, 1919);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		returnObj.contactIsPresent = true;
//		// Log for testing purposes
//		Log.i("Returning to OnActivityResult", "Success");
//		if (requestCode == LibSMSUtils.PICK_CONTACT) {
//			Uri contactData = data.getData();
//			// ContactsContract Cursor
//			Cursor c = this.managedQuery(contactData, null, null, null, null);
//			setContactLookUp(c);
//			setContactName(c);
//
//			// Contacts Attributes Cursor
//			c = getContentResolver().query(
//					Data.CONTENT_URI,
//					new String[] { Data._ID, Phone.NUMBER, Phone.TYPE,
//							Phone.LABEL, Phone.PHOTO_ID, Phone.DISPLAY_NAME,
//							Email.DATA1 },
//					Data.LOOKUP_KEY + "=?" + " AND " + Data.MIMETYPE + "='"
//							+ Phone.CONTENT_ITEM_TYPE + "'",
//					new String[] { String.valueOf(returnObj.contactLookUpId) },
//					null);
//
//		
//			setContactPhone(c);
//			setContactID(c);
//			setContactImageURI(c);
//			setcontactEmail();
//
//		}
//		// Log for testing purposes
//		Log.i("Finishing Activity", "Success");
//
//		LibSMSUtils.contactObj = returnObj;
//
//		LibContactLogActivity.this.finish();
//	}
//
//	private void setContactID(Cursor c) {
//		if (!c.moveToFirst())
//			return;
//		// Fetching Contact Id.
//		int columnIndexName = c.getColumnIndexOrThrow("_id");
//		String strName = c.getString(columnIndexName);
//		if (strName != null) {
//			returnObj.contactId = Long.parseLong(strName);
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"Contact Id:: " + strName, Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	private void setcontactEmail() {
//		Cursor c = getContentResolver().query(
//				Data.CONTENT_URI,
//				new String[] { Email.DATA1 },
//				Data.LOOKUP_KEY + "=?" + " AND " + Data.MIMETYPE + "='"
//						+ Email.CONTENT_ITEM_TYPE + "'",
//				new String[] { String.valueOf(returnObj.contactLookUpId) },
//				null);
//		if (!c.moveToFirst())
//			return;
//	
//		// Fetching Contact Email.
//		int columnIndexName = c.getColumnIndexOrThrow("data1");
//		String strName = c.getString(columnIndexName);
//		if (strName != null) {
//			returnObj.contactEmail = strName;
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"Email:: " + strName, Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	private void setContactImageURI(Cursor c) {
//		if (!c.moveToFirst())
//			return;
//		// Fetching Contact image URL.
//		int columnIndexName = c.getColumnIndexOrThrow("photo_id");
//		String strName = c.getString(columnIndexName);
//		if (strName != null) {
//			Uri contactUri = ContentUris.withAppendedId(
//					Contacts.CONTENT_LOOKUP_URI, returnObj.contactId);
//			Uri photoUri = Uri.withAppendedPath(contactUri,
//					Contacts.Photo.CONTENT_DIRECTORY);
//			returnObj.contactImage = photoUri;
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"PhotoURL:: " + photoUri.toString(), Toast.LENGTH_SHORT)
//						.show();
//		}
//
//	}
//
//	private void setContactPhone(Cursor c) {
//		if (!c.moveToFirst())
//			return;
//		// Fetching Contact Phone Number.
//		int columnIndexName = c.getColumnIndexOrThrow("data1");
//		String strName = c.getString(columnIndexName);
//		if (strName != null) {
//			returnObj.contactNumber = strName;
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"Phone Number :: " + strName, Toast.LENGTH_SHORT)
//						.show();
//		}
//
//	}
//
//	private void setContactName(Cursor c) {
//		if (!c.moveToFirst())
//			return;
//		// Fetching Contact Name.
//		int columnIndexName = c.getColumnIndexOrThrow("display_name");
//		String strName = c.getString(columnIndexName);
//		if (strName != null) {
//			returnObj.contactName = strName;
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"Display Name :: " + strName, Toast.LENGTH_SHORT)
//						.show();
//		}
//
//	}
//
//	private void setContactLookUp(Cursor c) {
//		if (!c.moveToFirst())
//			return;
//		// Fetching Lookup Key for the Contact.
//		int columnIndexLookup = c.getColumnIndexOrThrow("lookup");
//		String strLookUp = c.getString(columnIndexLookup);
//		if (strLookUp != null) {
//			returnObj.contactLookUpId = strLookUp;
//			if (showToasts)
//				Toast.makeText(LibContactLogActivity.this,
//						"LookUp:: " + strLookUp, Toast.LENGTH_SHORT).show();
//		}
//	}
//}
