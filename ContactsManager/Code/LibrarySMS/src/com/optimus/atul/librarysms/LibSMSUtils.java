/**
 * Optimus Info Android Component Library
 * Name : SMS Library
 * Author : Atul Mittal
 */
package com.optimus.atul.librarysms;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseArray;

/**
 * This library provides various APIs which could be utilized for integrating
 * SMS,CallLogs,Contacts functionality in an Android application.
 * 
 */
public class LibSMSUtils {
	Context context;
	static final int PICK_CONTACT = 1919;
	static LibContactUtil contactObj;

	/**
	 * To check whether the device has access to SMS enabling services.
	 * 
	 * @return Boolean indicating the presence of such service.
	 */
	public boolean checkSMSServiceAvailability(Context argContext) {
		ConnectivityManager connMgr = null;

		try {
			connMgr = (ConnectivityManager) argContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (connMgr == null)
				throw new IllegalArgumentException();
		}

		// Obtain network information of type MOBILE.
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		boolean isPresent = networkInfo.isAvailable();

		Log.i("Mobile Service Provider", "" + isPresent);

		return isPresent;
	}

	// /**
	// * To check whether the device has access to MMS enabling services.
	// *
	// * @return Boolean indicating the presence of such service.
	// */
	// public boolean checkMMSServiceAvailability(Context argContext) {
	//
	// ConnectivityManager connMgr = (ConnectivityManager) argContext
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	//
	// // Obtain network information of type ANY_ACTIVE_DATA.
	// boolean isPresent = connMgr.getActiveNetworkInfo() != null
	// && connMgr.getActiveNetworkInfo().isConnectedOrConnecting();
	//
	// Log.i("Mobile Data Provider", "" + isPresent);
	//
	// return isPresent;
	// }

	// /**
	// * Brings up device contacts list and lets user select a contact.
	// *
	// * Make sure to add PICK_CONTACT Uses-Permission in Android Manifest
	// before
	// * using this method and add a new activity with android:name=
	// * .LibContactLogActivity in your Android Manifest.
	// *
	// * @return LibContactUtil object. Access its static fields to get contact
	// * details. Fields might be null depending on availability.
	// */
	// public LibContactUtil pickContact() {
	// context.startActivity(new Intent(context, LibContactLogActivity.class));
	// return contactObj;
	// }

	/**
	 * Send SMS (Make sure to check network using checkSMSServiceAvailability
	 * before sending a SMS).
	 * 
	 * @param message
	 *            Message to be sent (Will be split if more than 160 characters)
	 * @param recepient
	 *            Recipient number
	 * @param pendingIntent
	 *            if not NULL this <code>PendingIntent</code> is broadcast when
	 *            the message is successfully sent, or failed. The result code
	 *            will be <code>Activity.RESULT_OK</code> for success, or one of
	 *            these errors:<br>
	 *            <code>RESULT_ERROR_GENERIC_FAILURE</code><br>
	 *            <code>RESULT_ERROR_RADIO_OFF</code><br>
	 *            <code>RESULT_ERROR_NULL_PDU</code><br>
	 *            For <code>RESULT_ERROR_GENERIC_FAILURE</code> the sentIntent
	 *            may include the extra "errorCode" containing a radio
	 *            technology specific value, generally only useful for
	 *            troubleshooting.<br>
	 *            The per-application based SMS control checks sentIntent. If
	 *            sentIntent is NULL the caller will be checked against all
	 *            unknown applications, which cause smaller number of SMS to be
	 *            sent in checking period.
	 * @throws Exception
	 */
	public void sendSMS(Context argContext, String message, String recipient,
			Intent pendingIntent) throws Exception {
		SmsManager sms = SmsManager.getDefault();

		if (!checkSMSServiceAvailability(argContext)) {
			Exception ex = new Exception("Service not available");
			throw ex;
		}
		if (message == null || recipient == null)
			throw new IllegalArgumentException();

		// Sending SMS
		if (pendingIntent != null) {
			try {
				sms.sendTextMessage(recipient, null, message, PendingIntent
						.getBroadcast(context, 0, pendingIntent, 0), null);
			} catch (Exception e) {
				throw e;
			}

		} else {
			try {
				sms.sendTextMessage(recipient, null, message, null, null);
			} catch (Exception e) {
				throw e;
			}
			// For sending data sms.
			// sms.sendDataMessage(recipient, null, (short) 15554,
			// message.getBytes(), null, null);
		}

	}

	/**
	 * As of now (10th October,2012) Android does not have any Public APIs which
	 * handles MMS functionality. This method however launches the Android own
	 * Messaging Application with the desired images/text/recipient attached.
	 * 
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param imageUri
	 *            The android.net.Uri of the image to be attached with the
	 *            MMS.Pass null for no image attachment.
	 * @param messageText
	 *            The text part to be attached to the MMS. Pass null for no text
	 *            attachment.
	 * @param recipient
	 *            The destination phone number.Pass null for no recipient
	 *            address attachment.
	 */
	public void sendMMSPicture(Context argContext, Uri imageUri,
			String messageText, String recipient) {

		if (argContext == null) {
			throw new IllegalArgumentException();
		}

		try {
			Intent mmsIntent = new Intent(Intent.ACTION_SENDTO);
			mmsIntent.setType("vnd.android-dir/mms-sms");
			if (imageUri != null)
				mmsIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
			if (messageText != null)
				mmsIntent.putExtra("sms_body", messageText);
			if (recipient != null)
				mmsIntent.putExtra("address", recipient);
			mmsIntent.addCategory(Intent.CATEGORY_DEFAULT);
			argContext.startActivity(mmsIntent);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Delete entire conversation thread. Use with Handler to avoid performance
	 * issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            The context of the activity invoking this method.
	 * @param argAddress
	 *            The Address (Phone Number) with which the conversation is
	 *            held.
	 * @return The length of the text collection of the deleted conversation.
	 */
	public int deleteConversation(Context argContext, String argAddress) {

		if (argAddress == null)
			throw new IllegalArgumentException();

		int i = 0;
		try {
			i = argContext.getContentResolver().delete(
					Uri.parse("content://sms/"), "address=?",
					new String[] { argAddress });
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (i == 0)
				throw new IllegalArgumentException();
		}

		Log.d("Number of Rows Deleted", "" + i);
		return i;

	}

	/**
	 * This method uses the activity context to query the Android content
	 * provider for the list of conversations(SMS/MMS). Use with Handler to
	 * avoid performance issues as querying system resources has latency.
	 * 
	 * @param argsContext
	 *            Context of the activity calling this function.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         ConversationThreadId,ConversationPartnerAddress and the LastText
	 *         with key values "thread_id","address","lasttext" respectively.The
	 *         presence of a key-value pair is subject to the availability of
	 *         the value.
	 */
	public JSONArray getListOfConversations(Context argContext) {

		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		/*
		 * Query the Android content resolver and retrieve the list of all
		 * SMS/MMS conversations.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}
		final String[] projection = new String[] { "address", "thread_id" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, null, null, null);

//		HashMap<Integer, String> threads = new HashMap<Integer, String>();
		SparseArray<String> threads= new SparseArray<String>();
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {
				int columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("thread_id");
				String conversationId = query.getString(columnIndex);
				Log.i("Address", "" + conversationId);
				int threadid = Integer.parseInt(conversationId);
//				if (!threads.containsKey(conversationId))
					threads.put(threadid, address);
			} while (query.moveToNext());
		}
	
		
		// Put the details fetched into a JSONObject --> JSONArray
		try {
			int k = 1;
			String val;
			while ((val=threads.get(k)) != null) {
				JSONObject localObject = new JSONObject();
				localObject.put("address", val);
				localObject.put("thread_id", Integer.toString(k));
			
				// Inserting the local JSONObject into the to-be-returned
				// JSONArray.
				obj.put(localObject);
				k++;
			}
		} catch (JSONException e) {
			Log.e("Exception:: ", "" + e.getMessage());
		}
		// Return the result obtained.
		return obj;
	}

	/**
	 * This method uses the activity context to query the Android content
	 * provider for the list of SMS/MMS in a conversation.Use with Handler to
	 * avoid performance issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param conversationId
	 *            Id of the thread containing the conversation texts.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         MessageTextId,Person,Address,TextMessage,Date,Type with key
	 *         values "messageid","person","address","textmessage","date","type"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 */
	public JSONArray getTextsfromId(Context argContext, String conversationId) {

		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		/*
		 * Query the Android content resolver and retrieve the list of all SMS
		 * in a conversations.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null || conversationId == null)
				throw new IllegalArgumentException();
		}
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "person", "date", "body", "type" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, "thread_id=?",
				new String[] { conversationId }, null);
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {

				int columnIndex = query.getColumnIndexOrThrow("body");
				String lastMessage = query.getString(columnIndex);
				Log.i("Body", "" + lastMessage);

				columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("person");
				String person = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("date");
				String date = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("type");
				String type = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("_id");
				String _id = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {
					localObject.put("type", type);
					localObject.put("date", date);
					localObject.put("textmessage", lastMessage);
					localObject.put("address", address);
					localObject.put("person", person);
					localObject.put("messageid", _id);
					// Inserting the local JSONObject into the to-be-returned
					// JSONArray.
					obj.put(localObject);
				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());
		}
		// Return the result obtained.
		return obj;
	}

	/**
	 * This method uses the activity context to query the Android content
	 * provider for the list of SMS/MMS in a conversation.Use with Handler to
	 * avoid performance issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param argAddress
	 *            Address of the thread containing the conversation texts.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         MessageTextId,Person,Address,TextMessage,Date,Type with key
	 *         values "messageid","person","address","textmessage","date","type"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 */
	public JSONArray getTextsfromAddress(Context argContext, String argAddress) {

		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		/*
		 * Query the Android content resolver and retrieve the list of all SMS
		 * in a conversations.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null || argAddress == null)
				throw new IllegalArgumentException();
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "person", "date", "body", "type" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, "address=?",
				new String[] { argAddress }, null);
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {

				int columnIndex = query.getColumnIndexOrThrow("body");
				String lastMessage = query.getString(columnIndex);
				Log.i("Body", "" + lastMessage);

				columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("person");
				String person = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("date");
				String date = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("type");
				String type = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("_id");
				String _id = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {
					localObject.put("type", type);
					localObject.put("date", date);
					localObject.put("textmessage", lastMessage);
					localObject.put("address", address);
					localObject.put("person", person);
					localObject.put("messageid", _id);
					// Inserting the local JSONObject into the to-be-returned
					// JSONArray.
					obj.put(localObject);
				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());
		}
		// Return the result obtained.
		return obj;
	}

	/**
	 * Delete a particular SMS from its message id. Get message id from the
	 * function LibSMSUtils.getTextsfromThreadId OR
	 * LibSMSUtils.getTextsfromAddress. Use with Handler to avoid performance
	 * issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param argMessageId
	 *            The id of the message to be deleted.
	 * @return boolean value depicting the success/failure of the operation.
	 */
	public boolean deleteSMS(Context argContext, String argMessageId) {
		/*
		 * Query the Android content resolver and delete the message with the
		 * corresponding message id.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null || argMessageId == null)
				throw new IllegalArgumentException();
		}
		Uri uri = Uri.parse("content://sms");
		int i = contentResolver.delete(uri, "_id=?",
				new String[] { argMessageId });

		// Return appropriate result.
		if (i != 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method uses the activity context to query the Android content
	 * provider for the list of unread SMS/MMS in a conversation.Use with
	 * Handler to avoid performance issues as querying system resources has
	 * latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         MessageTextId,Status,Address,TextMessage,Date with key values
	 *         "messageid","status","address","textmessage" and "date"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 */
	public JSONArray getUnreadMessages(Context argContext) {
		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		/*
		 * Query the Android content resolver and retrieve the list of all SMS
		 * in a conversations.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "read", "status" };
		Uri uri = Uri.parse("content://sms");

		Cursor query = contentResolver.query(uri, projection, "read=?",
				new String[] { "0" }, null);

		String[] str = query.getColumnNames();
		for (String s : str) {
			Log.d("ColumnName:: ", s);
		}
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {

				int columnIndex = query.getColumnIndexOrThrow("body");
				String lastMessage = query.getString(columnIndex);
				Log.i("Body", "" + lastMessage);

				columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("read");
				String read = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("date");
				String date = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("status");
				String status = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				columnIndex = query.getColumnIndexOrThrow("_id");
				String _id = query.getString(columnIndex);
				Log.i("Subject", "" + address);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {
					if (Integer.parseInt(read) == 0) {
						localObject.put("status", status);
						localObject.put("date", date);
						localObject.put("textmessage", lastMessage);
						localObject.put("address", address);
						localObject.put("messageid", _id);
						// Inserting the local JSONObject into the
						// to-be-returned
						// JSONArray.
						obj.put(localObject);
					}
				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());

		}
		return obj;
	}

	/**
	 * Returns a JSONArray with all the contact details by querying Android
	 * content providers. Use with Handler to avoid performance issues as
	 * querying system resources has latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         Contact ID,Contact Name, Phone Number, Photo Uri, Contact Email
	 *         with key values "contactid","displayname","phone","photouri" and
	 *         "email" respectively.The presence of a key-value pair is subject
	 *         to the availability of the value.
	 */
	public JSONArray getContacts(Context argContext) {

		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		/*
		 * Query the Android content resolver and get the specific fields
		 * related to contacts.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}

		// For contact name,phone,etc.
		Cursor query = contentResolver.query(Data.CONTENT_URI, new String[] {
				Data._ID, Phone.NUMBER, Phone.DATA, Phone.DATA1, Phone.TYPE,
				Phone.LABEL, Phone.PHOTO_ID, Phone.DISPLAY_NAME, Email.DATA },
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
				null);

		String[] str = query.getColumnNames();
		for (String s : str) {
			Log.d("Col Name", s);
		}
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {
				int columnIndex = query.getColumnIndexOrThrow("_id");
				String _id = query.getString(columnIndex);
				Log.i("id", "" + _id);

				columnIndex = query.getColumnIndexOrThrow("data1");
				String data1 = query.getString(columnIndex);
				Log.i("data1", "" + data1);

				columnIndex = query.getColumnIndexOrThrow("photo_id");
				String photo_id = query.getString(columnIndex);
				Log.i("photo_id", "" + photo_id);

				String photo_uri = getContactImageURI(argContext, photo_id, _id);

				columnIndex = query.getColumnIndexOrThrow("display_name");
				String display_name = query.getString(columnIndex);
				Log.i("Subject", "" + display_name);

				String email = getcontactEmail(argContext, display_name);
				Log.i("email", "" + email);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {

					localObject.put("email", email);
					localObject.put("photouri", photo_uri);
					localObject.put("phone", data1);
					localObject.put("displayname", display_name);
					localObject.put("contactid", _id);

					// Inserting the local JSONObject into the to-be-returned
					// JSONArray.

					obj.put(localObject);

				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());

		}
		return obj;
	}

	/**
	 * Returns Email Address corresponding to the display name provided.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param displayName
	 *            Contact display name.
	 * @return
	 */
	private String getcontactEmail(Context argContext, String displayName) {
		String emailId = null;
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}
		Cursor c = contentResolver.query(Data.CONTENT_URI, new String[] {
				Phone.DISPLAY_NAME, Email.DATA1 }, Phone.DISPLAY_NAME + "=?"
				+ " AND " + Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE
				+ "'", new String[] { displayName }, null);

		if (c.moveToFirst()) {
			// Fetching Contact Email.
			int columnIndexName = c.getColumnIndexOrThrow("data1");
			emailId = c.getString(columnIndexName);
		}

		return emailId;

	}

	/**
	 * Returns Email Address corresponding to the photo_id and contact_id
	 * provided.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param photo_id
	 *            Contact photo id.
	 * @param contact_id
	 *            Contact id.
	 * @return
	 */
	private String getContactImageURI(Context argContext, String photo_id,
			String contact_id) {

		String contactImage = null;

		// Fetching Contact image URL.
		try {
			if (photo_id != null) {
				Uri contactUri = ContentUris
						.withAppendedId(Contacts.CONTENT_LOOKUP_URI,
								Long.parseLong(contact_id));
				Uri photoUri = Uri.withAppendedPath(contactUri,
						Contacts.Photo.CONTENT_DIRECTORY);
				contactImage = photoUri.toString();
			}
		} catch (Exception e) {
			Log.e("Exception Caught", "" + e.getMessage());
		}
		return contactImage;
	}

	/**
	 * Returns SMS log for the duration after the provided TimeStamp value by
	 * querying Android content providers. Use with Handler to avoid performance
	 * issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param timeStamp
	 *            The TimeStamp value to begin from for fetching SMS log.
	 * 
	 * @return JSONArray which consists of JSONObjects.The fields represent Call
	 *         Status,Timestamp of the SMS, SMS Text , Senders Address, SMS Id
	 *         with key values
	 *         "status","date","textmessage","address","messageid"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 */
	public JSONArray getSMSLog(Context argContext, String timeStamp) {
		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();
		/*
		 * Query the Android content resolver and retrieve the list of all SMS
		 * in a conversations.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null || timeStamp == null)
				throw new IllegalArgumentException();
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "read", "status" };

		Uri uri = Uri.parse("content://sms");

		Cursor query = null;
		try {
			query = contentResolver.query(uri, projection, "date>?",
					new String[] { timeStamp }, null);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		String[] str = query.getColumnNames();
		for (String s : str) {
			Log.d("ColumnName:: ", s);
		}
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {

				int columnIndex = query.getColumnIndexOrThrow("body");
				String lastMessage = query.getString(columnIndex);
				Log.i("Body", "" + lastMessage);

				columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				Log.i("address", "" + address);

				columnIndex = query.getColumnIndexOrThrow("read");
				String read = query.getString(columnIndex);
				Log.i("read", "" + address);

				columnIndex = query.getColumnIndexOrThrow("date");
				String date = query.getString(columnIndex);
				Log.i("date", "" + address);

				columnIndex = query.getColumnIndexOrThrow("_id");
				String _id = query.getString(columnIndex);
				Log.i("_id", "" + address);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {
					localObject.put("status", read);
					localObject.put("date", date);
					localObject.put("textmessage", lastMessage);
					localObject.put("address", address);
					localObject.put("messageid", _id);
					// Inserting the local JSONObject into the to-be-returned
					// JSONArray.
					obj.put(localObject);

				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());

		}
		return obj;

	}

	/**
	 * Returns call log details by querying Android content providers. Use with
	 * Handler to avoid performance issues as querying system resources has
	 * latency.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param phoneNumber
	 *            PhoneNumber to fetch corresponding call log details. If null,
	 *            returns complete call log details.
	 * @return JSONArray which consists of JSONObjects.The fields represent Call
	 *         Type(1=Received 2=Outgoing 3=Missed),Timestamp of the
	 *         call,Duration, Caller phone number , Caller phone number
	 *         type,Name of the caller with key values
	 *         "calltype","date","duration","phone","numbertype","name"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 */
	public JSONArray getCallLog(Context argContext, String phoneNumber) {
		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException();
		}

		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.CACHED_NUMBER_TYPE, CallLog.Calls.DATE,
				CallLog.Calls.DURATION, CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE };

		// Check if caller has provided a phoneNumber to fetch corresponding
		// call log details.
		Cursor query = null;
		if (phoneNumber == null) {
			query = contentResolver.query(CallLog.Calls.CONTENT_URI,
					projection, null, null, null);
		} else {

			query = contentResolver.query(CallLog.Calls.CONTENT_URI,
					projection, "number=?", new String[] { phoneNumber }, null);

		}
		String[] columnNames = query.getColumnNames();
		for (String str : columnNames) {
			Log.d("ColumnName", str);
		}

		if (query.moveToFirst()) {
			do {
				int columnIndex = query.getColumnIndexOrThrow("name");
				String name = query.getString(columnIndex);
				Log.i("name", "" + name);

				columnIndex = query.getColumnIndexOrThrow("numbertype");
				String numbertype = query.getString(columnIndex);
				Log.i("numbertype", "" + numbertype);

				columnIndex = query.getColumnIndexOrThrow("date");
				String date = query.getString(columnIndex);
				Log.i("date", "" + date);

				columnIndex = query.getColumnIndexOrThrow("duration");
				String duration = query.getString(columnIndex);
				Log.i("duration", "" + duration);

				columnIndex = query.getColumnIndexOrThrow("number");
				String number = query.getString(columnIndex);
				Log.i("number", "" + number);

				columnIndex = query.getColumnIndexOrThrow("type");
				String type = query.getString(columnIndex);
				Log.i("type", "" + type);

				JSONObject localObject = new JSONObject();
				// Put the details fetched into a JSONObject --> JSONArray
				try {
					localObject.put("calltype", type);
					localObject.put("date", date);
					localObject.put("duration", duration);
					localObject.put("phone", number);
					localObject.put("numbertype", numbertype);
					localObject.put("name", name);
					// Inserting the local JSONObject into the to-be-returned
					// JSONArray.
					obj.put(localObject);
				} catch (JSONException e) {
					Log.e("Exception:: ", "" + e.getMessage());
				}
			} while (query.moveToNext());
		}
		return obj;
	}
}
