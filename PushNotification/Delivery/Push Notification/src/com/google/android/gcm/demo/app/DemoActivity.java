/**Application name : Push notification
 * Device receives a notification whenever a message is received from the server
 * Created by : Pooja Srivastava
 * 
 */

package com.google.android.gcm.demo.app;

import static com.google.android.gcm.demo.app.CommonUtilities.DISPLAY_MESSAGE_ACTION;

import static com.google.android.gcm.demo.app.CommonUtilities.EXTRA_MESSAGE;
import static com.google.android.gcm.demo.app.CommonUtilities.SENDER_ID;
import static com.google.android.gcm.demo.app.CommonUtilities.SERVER_URL;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Class name : DemoActivity.java Description : Activity class Displays notification
 * in the text field
 * 
 * @author Pooja
 */

public class DemoActivity extends Activity {

	TextView mDisplay;
	AsyncTask<Void, Void, Void> mRegisterTask;

	// called when activity is first started
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// verify if the server url and sender id is not null
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		setContentView(R.layout.main);
		mDisplay = (TextView) findViewById(R.id.display);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// getting the registration ID from the server
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {

			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
		} else {

			// If Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.

				mDisplay.append(getString(R.string.already_registered) + "\n");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						boolean registered = ServerUtilities.register(context,
								regId);
						// Calling the register function from ServerUtilities
						// class

						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	// menu creation with two options:
	// clear and exit
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	// selecting an item from the menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// on selecting clear option, text is set to null
		case R.id.options_clear:
			mDisplay.setText(null);
			return true;

	   // on selecting the exit option, activity is finished
		case R.id.options_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		//when the activity is destroyed, device is unregistered
		unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}

	//function to check whether an object reference is not null.
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	//function to broadcast the message
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		
		//function is called on receiving the message
		@Override
		public void onReceive(Context context, Intent intent) {
			
			//extracting the message from the intent
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			mDisplay.append(newMessage + "\n");
		}
	};

}