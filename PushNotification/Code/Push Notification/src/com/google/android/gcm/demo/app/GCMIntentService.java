package com.google.android.gcm.demo.app;

import static com.google.android.gcm.demo.app.CommonUtilities.SENDER_ID;
import static com.google.android.gcm.demo.app.CommonUtilities.displayMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * class name : GCMIntentService.java IntentService responsible for handling GCM
 * messages.
 * 
 * @author Pooja
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	// initialising a constructor where sender Id is passed
	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**function name : onRegistered
	 * @param : context
	 * @param : registration Id
	 * @return: void
	 * called when the device is registered on the server
	 */
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, getString(R.string.gcm_registered));
		ServerUtilities.register(context, registrationId);

	}
    
	/**function name : onUnregistered
	 * @param : context
	 * @param : registration Id
	 * @return: void 
	 * called when the device is unregistered from the server
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	/**function name : onMessage
	 * @param : context
	 * @param : intent
	 * @return: void
	 * called when notification is received on the device
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
	String extra_message = intent.getExtras().getString("message");
		String message =extra_message;
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	} 

	/**function name : onDeletedMessages
	 * @param : context
	 * @param : total
	 * @return: void
	 * called when message is deleted 
	 */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**function name : onError
	 * @param : context
	 * @param :errorId
	 * @return: void
	 * called when an error occurs condition occurs
	 */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	/**function name : onRecoverableError
	 * @param : context
	 * @param : error Id
	 * @return: boolean
	 * called when recoverable error condition occurs
	 */
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	
	/**function name : generateNotification
	 * Issues a notification to inform the user that server has sent a message.
	 * @param context
	 * @param message 
	 */
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_stat_gcm;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, DemoActivity.class);
		// set intent so it does not start a new activity
		
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

}
