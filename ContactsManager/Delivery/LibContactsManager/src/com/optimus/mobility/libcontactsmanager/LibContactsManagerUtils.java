/**
 * Optimus Info Android Component Library
 * Name : SMS Library
 * Author : Atul Mittal
 */
package com.optimus.mobility.libcontactsmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * This library provides various APIs which could be utilized for integrating
 * SMS,CallLogs,Contacts functionality in an Android application.
 * 
 */
public class LibContactsManagerUtils {

	private final String	exceptionContext			= "Unable to fetch System Service for the given context";
	private final String	exceptionInvalidParamenters	= "Invalid input parameters";
	private final String	exceptionJSONParse			= "Unable to parse into JSON";
	private boolean			debugMode					= false;

	/**
	 * This library provides various APIs which could be utilized for
	 * integrating SMS,CallLogs,Contacts functionality in an Android
	 * application.
	 * 
	 * @param debugMode
	 *            To initialize the object with debugging on log enabled.
	 */
	public LibContactsManagerUtils(boolean argDebugMode) {
		if (argDebugMode)
			debugMode = true;
	}

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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (connMgr == null)
				throw new IllegalArgumentException(exceptionContext);
		}

		// Obtain network information of type MOBILE.
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		boolean isPresent = networkInfo.isAvailable();

		if (debugMode)
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
	// if(debugMode)Log.i("Mobile Data Provider", "" + isPresent);
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
	 * @param deliveryIntent
	 *            if not NULL this <code>Delivery Intent</code> is broadcast
	 *            when the message is successfully delivered. The result code
	 *            will be <code>Activity.RESULT_OK</code> for success, or one of
	 *            these errors:<br>
	 *            <code>RESULT_ERROR_GENERIC_FAILURE</code><br>
	 *            <code>RESULT_ERROR_RADIO_OFF</code><br>
	 *            <code>RESULT_ERROR_NULL_PDU</code><br>
	 *            For <code>RESULT_ERROR_GENERIC_FAILURE</code>
	 * @throws Exception
	 */
	public void sendSMS(Context argContext, String message, String recipient,
			PendingIntent pendingIntent, PendingIntent deliveryIntent)
			throws Exception {
		SmsManager sms = SmsManager.getDefault();

		// Incompatibility with Samsung
		// if (!checkSMSServiceAvailability(argContext)) {
		// throw new IllegalArgumentException(exceptionContext);
		// }
		if (message == null || recipient == null)
			throw new IllegalArgumentException(exceptionInvalidParamenters);

		// Sending SMS
		sms.sendTextMessage(recipient, null, message, pendingIntent,
				deliveryIntent);

	}

	// /**
	// * As of now (10th October,2012) Android does not have any Public APIs
	// which
	// * handles MMS functionality. This method however launches the Android own
	// * Messaging Application with the desired images/text/recipient attached.
	// *
	// *
	// * @param argContext
	// * Context of the activity calling this function.
	// * @param imageUri
	// * The android.net.Uri of the image to be attached with the
	// * MMS.Pass null for no image attachment.
	// * @param messageText
	// * The text part to be attached to the MMS. Pass null for no text
	// * attachment.
	// * @param recipient
	// * The destination phone number.Pass null for no recipient
	// * address attachment.
	// */
	// public void sendMMSPicture(Context argContext, Uri imageUri,
	// String messageText, String recipient) {
	//
	// if (argContext == null) {
	// throw new IllegalArgumentException();
	// }
	//
	// try {
	// Intent mmsIntent = new Intent(Intent.ACTION_SENDTO);
	// mmsIntent.setType("vnd.android-dir/mms-sms");
	// if (imageUri != null)
	// mmsIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
	// if (messageText != null)
	// mmsIntent.putExtra("sms_body", messageText);
	// if (recipient != null)
	// mmsIntent.putExtra("address", recipient);
	// mmsIntent.addCategory(Intent.CATEGORY_DEFAULT);
	// argContext.startActivity(mmsIntent);
	// } catch (Exception e) {
	// throw new IllegalArgumentException();
	// }
	//
	// }

	/**
	 * Deletes all texts associated with a conversation thread id
	 * 
	 * @param argContext
	 *            The context of the activity invoking this method.
	 * @param conversationId
	 *            The conversation thread id.
	 * @return Boolean stating if any thread texts were deleted.
	 * @throws JSONException
	 */
	public boolean deleteConversation(Context argContext, int conversationId)
			throws JSONException {

		boolean deletedText = false;
		// Fetching all the texts associated with a particular thread id.
		JSONArray jarray = getTextsfromId(argContext,
				Integer.toString(conversationId));
		List<JSONObject> jobjList = new ArrayList<JSONObject>();

		// Generating list of all the text attributes obtained.
		for (int i = 0; i < jarray.length(); i++) {
			jobjList.add(jarray.getJSONObject(i));
		}

		// Using the sms_id of all the texts to delete them.
		for (int i = 0; i < jobjList.size(); i++) {
			int smsID = jobjList.get(i).getInt("_id");
			deleteSMS(argContext, smsID);
			deletedText = true;
		}

		return deletedText;

	}

	/**
	 * Delete all SMS from a given phone number. Use with Handler to avoid
	 * performance issues as querying system resources has latency.
	 * 
	 * @param argContext
	 *            The context of the activity invoking this method.
	 * @param argAddress
	 *            The Address (Phone Number)
	 * @return If any SMS were deleted.
	 */
	public boolean deleteSMS(Context argContext, String argAddress) {

		boolean returnVal = false;

		if (argAddress == null)
			throw new IllegalArgumentException(exceptionInvalidParamenters);

		int count = 0;
		try {
			count = argContext.getContentResolver().delete(
					Uri.parse("content://sms/"), "address=?",
					new String[] { argAddress });
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (count > 0)
				returnVal = true;
		}

		return returnVal;

	}

	/**
	 * This method uses the activity context to query the Android content
	 * provider for the list of conversations(SMS/MMS). Use with Handler to
	 * avoid performance issues as querying system resources has latency.
	 * 
	 * @param argsContext
	 *            Context of the activity calling this function.
	 * @return JSONArray which consists of JSONObjects.The fields represent
	 *         ConversationThreadId and ConversationPartnerAddress with key
	 *         values "thread_id" and "address" respectively.The presence of a
	 *         key-value pair is subject to the availability of the value.
	 */
	@SuppressLint("UseSparseArrays")
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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}
		final String[] projection = new String[] { "address", "thread_id" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, null, null, null);

		HashMap<Integer, String> hashSMSThreads = new HashMap<Integer, String>();
		// SparseArray<String> threads = new SparseArray<String>();
		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {
				int columnIndex = query.getColumnIndexOrThrow("address");
				String address = query.getString(columnIndex);
				if (debugMode)
					Log.i("address", "" + address);

				columnIndex = query.getColumnIndexOrThrow("thread_id");
				String conversationId = query.getString(columnIndex);
				if (debugMode)
					Log.i("id", "" + conversationId + hashSMSThreads.size());
				int threadid = Integer.parseInt(conversationId);
				// if (!threads.containsKey(conversationId))
				hashSMSThreads.put(threadid, address);
			} while (query.moveToNext());
		}

		// Put the details fetched into a JSONObject --> JSONArray
		try {
			Iterator<Entry<Integer, String>> iterator = hashSMSThreads
					.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) iterator
						.next();
				JSONObject localObject = new JSONObject();
				localObject.put("address", pair.getValue());
				localObject.put("thread_id", pair.getKey());

				// Inserting the local JSONObject into the to-be-returned
				// JSONArray.
				obj.put(localObject);
			}
		} catch (JSONException e) {
			if (debugMode)
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
	 *         MessageTextId,Senders address,TextMessage,Date,Type(
	 *         MESSAGE_TYPE_ALL = 0, MESSAGE_TYPE_INBOX = 1, MESSAGE_TYPE_SENT =
	 *         2, MESSAGE_TYPE_DRAFT = 3, MESSAGE_TYPE_OUTBOX = 4,
	 *         MESSAGE_TYPE_FAILED = 5,MESSAGE_TYPE_QUEUED = 6) with key values
	 *         "messageid","address","textmessage","date","type"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 * @throws JSONException
	 */
	public JSONArray getTextsfromId(Context argContext, String conversationId)
			throws JSONException {

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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null || conversationId == null)
				throw new IllegalArgumentException(exceptionInvalidParamenters);
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "type" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, "thread_id=?",
				new String[] { conversationId }, null);
		try {
			putCursorToJArray(query, projection, obj);
		} catch (JSONException e1) {
			throw new JSONException(exceptionJSONParse);
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
	 *         MessageTextId,TextMessage,Date,Type( MESSAGE_TYPE_ALL = 0,
	 *         MESSAGE_TYPE_INBOX = 1, MESSAGE_TYPE_SENT = 2, MESSAGE_TYPE_DRAFT
	 *         = 3, MESSAGE_TYPE_OUTBOX = 4, MESSAGE_TYPE_FAILED =
	 *         5,MESSAGE_TYPE_QUEUED = 6) with key values
	 *         "messageid","address","textmessage","date","type"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 * @throws JSONException
	 */
	public JSONArray getTextsfromAddress(Context argContext, String argAddress)
			throws JSONException {

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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null || argAddress == null)
				throw new IllegalArgumentException(exceptionInvalidParamenters);
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "type" };
		Uri uri = Uri.parse("content://sms");
		Cursor query = contentResolver.query(uri, projection, "address=?",
				new String[] { argAddress }, null);
		try {
			putCursorToJArray(query, projection, obj);
		} catch (JSONException e1) {
			throw new JSONException(exceptionJSONParse);
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
	public boolean deleteSMS(Context argContext, int argMessageId) {
		/*
		 * Query the Android content resolver and delete the message with the
		 * corresponding message id.
		 */
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}
		Uri uri = Uri.parse("content://sms");
		int count = contentResolver.delete(uri, "_id=?",
				new String[] { Integer.toString(argMessageId) });

		// Return appropriate result.
		if (count != 1) {
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
	 *         MessageTextId,Status,Senders Address,TextMessage,Date with key
	 *         values "messageid","status"(STATUS_NONE = -1;STATUS_COMPLETE =
	 *         0;STATUS_PENDING = 32;STATUS_FAILED =
	 *         64;),"address","textmessage" and "date" respectively.The presence
	 *         of a key-value pair is subject to the availability of the value.
	 * @throws JSONException
	 */
	public JSONArray getUnreadMessages(Context argContext) throws JSONException {
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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "read", "status" };
		Uri uri = Uri.parse("content://sms");

		Cursor query = contentResolver.query(uri, projection, "read=?",
				new String[] { "0" }, null);

		String[] str = query.getColumnNames();
		for (String s : str) {
			if (debugMode)
				Log.d("ColumnName:: ", s);
		}
		try {
			putCursorToJArray(query, projection, obj);
		} catch (JSONException e1) {
			throw new JSONException(exceptionJSONParse);
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
	 * @throws JSONException
	 */
	public JSONArray getContacts(Context argContext) throws JSONException {

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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}

		final String[] projection = new String[] { Data.CONTACT_ID,
				Phone.NUMBER, Phone.DATA, Phone.DATA1, Phone.TYPE, Phone.LABEL,
				Phone.PHOTO_ID, Phone.DISPLAY_NAME, Email.DATA };

		// For contact name,phone,etc.
		Cursor query = contentResolver.query(Data.CONTENT_URI, projection,
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
				null);

		/*
		 * Begin an iteration and extract information from the returned query
		 * result.
		 */
		if (query.moveToFirst()) {
			do {
				int columnIndex = query.getColumnIndexOrThrow("contact_id");
				String _id = query.getString(columnIndex);
				if (debugMode)
					Log.i("id", "" + _id);

				columnIndex = query.getColumnIndexOrThrow("data1");
				String data1 = query.getString(columnIndex);
				if (debugMode)
					Log.i("data1", "" + data1);

				columnIndex = query.getColumnIndexOrThrow("photo_id");
				String photo_id = query.getString(columnIndex);
				if (debugMode)
					Log.i("photo_id", "" + photo_id);

				String photo_uri = getContactImageURI(argContext, photo_id, _id);

				columnIndex = query.getColumnIndexOrThrow("display_name");
				String display_name = query.getString(columnIndex);
				if (debugMode)
					Log.i("Subject", "" + display_name);

				String email = getcontactEmail(argContext, _id);
				if (debugMode)
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
					if (debugMode)
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
	 * @param contactID
	 *            Contact id corresponding to which the e-mail is to be
	 *            searched.
	 * @return
	 */
	private String getcontactEmail(Context argContext, String contactID) {
		String emailId = null;
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}
		Cursor c = contentResolver.query(Data.CONTENT_URI, new String[] {
				Data._ID, Email.DATA1 }, Email.CONTACT_ID + "=?" + " AND "
				+ Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE + "'",
				new String[] { contactID }, null);

		if (c.moveToFirst()) {
			// Fetching Contact Email.
			int columnIndexName = c.getColumnIndexOrThrow("data1");
			emailId = c.getString(columnIndexName);
			Log.i("email", emailId);
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
			if (debugMode)
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
	 * @return JSONArray which consists of JSONObjects.The fields represent SMS
	 *         Status,Timestamp of the SMS(13 digit SQL Timestamp value), SMS
	 *         Text , Senders Address, SMS Id with key values
	 *         "status"(0=unread,1=read),"date","textmessage","address"
	 *         ,"messageid" respectively.The presence of a key-value pair is
	 *         subject to the availability of the value.
	 * @throws JSONException
	 */
	public JSONArray getSMSLog(Context argContext, String timeStamp)
			throws JSONException {
		// Converting timeStamp(if in milliseconds) to timeStamp in seconds.
		StringBuffer timeString = new StringBuffer(timeStamp);

		if (!(timeString.length() == 13)) {
			throw new IllegalArgumentException(exceptionInvalidParamenters);
		}

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
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null || timeStamp == null)
				throw new IllegalArgumentException(exceptionInvalidParamenters);
		}
		final String[] projection = new String[] { "_id", "thread_id",
				"address", "date", "body", "read", "status" };

		Uri uri = Uri.parse("content://sms");

		Cursor query = null;
		try {
			query = contentResolver.query(uri, projection, "date>?",
					new String[] { timeStamp }, null);
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		}
		String[] str = query.getColumnNames();
		for (String s : str) {
			if (debugMode)
				Log.d("ColumnName:: ", s);
		}
		try {
			putCursorToJArray(query, projection, obj);
		} catch (JSONException e1) {
			throw new JSONException(exceptionJSONParse);
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
	 * @return JSONArray which consists of JSONObjects.The fields represent <br>
	 *         <h3>1.Call Type</h3>(1=Received 2=Outgoing 3=Missed),<br>
	 *         <h3>2.Timestamp of the call</h3><br>
	 *         <h3>3.Duration</h3><br>
	 *         <h3>4.Caller phone number</h3><br>
	 *         <h3>5.Caller phone number type</h3>(1=Home, 2=Mobile,3= Work, 4=
	 *         Workfax, 5=homefax, 6=pager,7=Other<br>
	 *         8=Custom, 9=Callback, 10=Car, 11=Company Main, 12=ISDN, 13=Main,
	 *         14=Other Fax, 15= Radio<br>
	 *         16=Telex, 17=TTY TDD, 18=WorkMobile, 19=WorkPager, 20=Assistant,
	 *         21=MMS)<br>
	 *         <h3>6.Name of the caller</h3><br>
	 *         with key values
	 *         "calltype","date","duration","phone","numbertype","name"
	 *         respectively.The presence of a key-value pair is subject to the
	 *         availability of the value.
	 * @throws JSONException
	 */
	public JSONArray getCallLog(Context argContext, String phoneNumber)
			throws JSONException {

		// Creating a new JSONArray for storing the result.
		JSONArray obj = new JSONArray();

		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
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

			if (debugMode)
				Log.d("ColumnName", str);
		}

		try {
			putCursorToJArray(query, projection, obj);
		} catch (JSONException e1) {
			throw new JSONException(exceptionJSONParse);
		}
		return obj;
	}

	/**
	 * Creates a JSONArray by extracting data from the Cursor according to the
	 * column strings provided.
	 * 
	 * @param query
	 *            Data Cursor
	 * @param columnNames
	 *            Column Names
	 * @param jObj
	 *            JSONArray to be populated
	 * @return Boolean indicating the success of this operation
	 * @throws JSONException
	 */
	private boolean putCursorToJArray(Cursor query, String[] columnNames,
			JSONArray jObj) throws JSONException {

		if (query.moveToFirst()) {
			do {
				JSONObject localObject = new JSONObject();
				for (int i = 0; i < columnNames.length; i++) {
					int columnIndex = query
							.getColumnIndexOrThrow(columnNames[i]);
					String value = query.getString(columnIndex);
					localObject.put(columnNames[i], value);
					if (debugMode)
						Log.i(columnNames[i], "" + value);
				}
				jObj.put(localObject);
			} while (query.moveToNext());
		}

		return false;
	}

	/**
	 * Adds a Call log entry into system database.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param duration
	 *            Duration of the call log to be entered.
	 * @param contactPhone
	 *            Contact phone number of the call log to be entered.
	 * @param timeStamp
	 *            SQL Timestamp (13-digits) indicating the time of call.(If
	 *            null, registers the call time as epoch time)
	 * @param callType
	 *            Type of call (1=Received 2=Outgoing 3=Missed) (If null,
	 *            registers as Received call)
	 * @param statusNew
	 *            Whether the call is reviewed (Values: 0,1) (If null, registers
	 *            as 0)
	 * @return The Uri of the newly created database row
	 */
	public Uri addCallLog(Context argContext, String duration,
			String contactPhone, String timeStamp, String callType,
			String statusNew) {

		// Check input parameters

		// argContext
		if (argContext == null) {
			throw new IllegalArgumentException(exceptionInvalidParamenters
					+ ":: argContext");
		}

		// Call Duration
		try {
			Integer.parseInt(duration);
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionInvalidParamenters
					+ ":: duration");
		}

		// Phone Number
		if (contactPhone == null)
			throw new IllegalArgumentException(exceptionInvalidParamenters
					+ ":: contactPhone");

		// Call Duration
		if (statusNew != null) {
			try {
				Integer.parseInt(duration);
			} catch (Exception e) {
				throw new IllegalArgumentException(exceptionInvalidParamenters
						+ ":: statusNew");
			}
		}
		// Initializing content resolver
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}

		// Building content values to be populated in system database
		ContentValues newValues = new ContentValues();
		newValues.put(CallLog.Calls.DATE, timeStamp);
		newValues.put(CallLog.Calls.DURATION, duration);
		newValues.put(CallLog.Calls.NUMBER, contactPhone);
		newValues.put(CallLog.Calls.TYPE, callType);
		newValues.put(CallLog.Calls.NEW, statusNew);

		// Inserting into database
		Uri query = contentResolver
				.insert(CallLog.Calls.CONTENT_URI, newValues);

		if (debugMode)
			Log.d("Content Uri", "" + ContentUris.parseId(query));

		return query;
	}

	/**
	 * Adds a SMS entry into system database.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param argAddress
	 *            Phone number
	 * @param argTimestamp
	 *            Timestamp of the SMS
	 * @param argBody
	 *            SMS Text body
	 * @param argRead
	 *            Read status (1=Read,0=Unread)
	 * @return The Uri of the newly created database row
	 */
	public Uri addSMSLog(Context argContext, String argAddress,
			String argTimestamp, String argBody, String argRead) {
		// Check input parameters

		// argContext
		if (argContext == null || argAddress == null || argTimestamp == null
				|| argBody == null || argRead == null) {
			throw new IllegalArgumentException(exceptionInvalidParamenters);
		}

		// Read status
		try {
			Integer.parseInt(argRead);
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionInvalidParamenters
					+ ":: statusNew");
		}

		// Initializing content resolver
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}

		Uri uriQuery = Uri.parse("content://sms");

		// Building content values to be populated in system database
		ContentValues newValues = new ContentValues();
		newValues.put("person", "Bill Gates");
		newValues.put("address", argAddress);
		newValues.put("date", argTimestamp);
		newValues.put("body", argBody);
		newValues.put("read", argRead);

		// Inserting into database
		Uri query = contentResolver.insert(uriQuery, newValues);

		if (debugMode)
			Log.d("Content Uri", "" + ContentUris.parseId(query));

		return query;

	}

	/**
	 * Adds a contact into system database.
	 * 
	 * @param argContext
	 *            Context of the activity calling this function.
	 * @param DisplayName
	 *            Display name of the contact
	 * @param MobileNumber
	 *            Mobile number of the contact
	 * @param HomeNumber
	 *            Home number of the contact
	 * @param WorkNumber
	 *            Work number of the contact
	 * @param emailID
	 *            Email id of the contact
	 * @return Boolean indicating the success/failure of this operation
	 */
	public boolean addContact(Context argContext, String DisplayName,
			String MobileNumber, String HomeNumber, String WorkNumber,
			String emailID) {
		// Check input parameters

		// argContext
		if (argContext == null) {
			throw new IllegalArgumentException(exceptionInvalidParamenters
					+ ":: argContext");
		}

		boolean funcResult = false;
		// Initializing content resolver
		ContentResolver contentResolver = null;
		try {
			contentResolver = argContext.getContentResolver();
		} catch (Exception e) {
			throw new IllegalArgumentException(exceptionContext);
		} finally {
			if (contentResolver == null)
				throw new IllegalArgumentException(exceptionContext);
		}

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.withValue(RawContacts.AGGREGATION_MODE,
						RawContacts.AGGREGATION_MODE_DISABLED)

				.build());
		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
							DisplayName).build());
			if (!funcResult)
				funcResult = true;
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
			if (!funcResult)
				funcResult = true;
		}

		// ------------------------------------------------------ Home Numbers
		if (HomeNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							HomeNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
					.build());
			if (!funcResult)
				funcResult = true;
		}

		// ------------------------------------------------------ Work Numbers
		if (WorkNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							WorkNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
					.build());
			if (!funcResult)
				funcResult = true;
		}

		// ------------------------------------------------------ Email
		if (emailID != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA,
							emailID)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
							ContactsContract.CommonDataKinds.Email.TYPE_WORK)
					.build());
			if (!funcResult)
				funcResult = true;
		}

		// Asking the Contact provider to create a new contact
		try {
			ContentProviderResult[] result = contentResolver.applyBatch(
					ContactsContract.AUTHORITY, ops);
			for (int i = 0; i < result.length; i++) {
				Log.d("Resolver Result", "" + result[i].toString());

			}
		} catch (Exception e) {

		}
		return funcResult;
	}

}
