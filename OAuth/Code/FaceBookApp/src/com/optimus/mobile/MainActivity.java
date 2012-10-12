////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: MainActivity.java
// 
// Test Activity to use oAuth Facebook Library.
////////////////////////////////////////////////////////////////////
package com.optimus.mobile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.optimus.mobile.oauth.facebook.FacebookRequestListener;
import com.optimus.mobile.oauth.facebook.LoginButton;
import com.optimus.mobile.oauth.facebook.SessionEvents;
import com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener;
import com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener;
import com.optimus.mobile.oauth.facebook.SessionStore;

/**
 * This is a sample activity to test oAuth facebook 2.0 library
 * 
 * @author optimus.support
 * 
 */
public class MainActivity extends Activity {

	// Members
	public static final String APP_ID = "156170661193063";
	private LoginButton mLoginButton;
	private TextView mText;
	private Button mRequestButton;
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running this example: see Example.java");
		}
		setContentView(R.layout.activity_main);
		initialize();
	}

	/**
	 * This method initializes the UI and facebook elements
	 */
	private void initialize() {

		mLoginButton = (LoginButton) findViewById(R.id.login);
		mText = (TextView) MainActivity.this.findViewById(R.id.txt);
		mRequestButton = (Button) findViewById(R.id.requestButton);

		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.initialize(this, mFacebook);

		mRequestButton.setOnClickListener(new OnClickListener() {
			/*
			 * This method is executed whenever the request button is clicked.
			 * 
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {
				mAsyncRunner.request("me", new SampleRequestListener());
			}
		});
		// if session is facebook session is still active then show the mRequest
		// button
		mRequestButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
	}

	/*
	 * This method is executed when the facebook app is installed and user
	 * returns after logging in using facebook app. (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);

	}

	/**
	 * This is a sample authentication listener when the user request for
	 * authentication with facebook api
	 * 
	 * @author optimus
	 * 
	 */
	public class SampleAuthListener implements AuthListener {

		/*
		 * This method is executed when the authentication is successful
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener#
		 * onAuthSucceed()
		 */
		public void onAuthSucceed() {
			mText.setText("You have logged in! ");
			mRequestButton.setVisibility(View.VISIBLE);

		}

		/*
		 * This method is executed when the authentication fails (non-Javadoc)
		 * 
		 * @see
		 * com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener#onAuthFail
		 * (java.lang.String)
		 */
		public void onAuthFail(String error) {
			mText.setText("Login Failed: " + error);
		}
	}

	/**
	 * This is a sample class for the log out request listener
	 * 
	 * @author optimus.support
	 * 
	 */
	public class SampleLogoutListener implements LogoutListener {
		/*
		 * This method is executed when the logout request is initiated
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener#
		 * onLogoutBegin()
		 */
		public void onLogoutBegin() {
			mText.setText("Logging out...");
		}

		/*
		 * This method is executed when the logout request is complete
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener#
		 * onLogoutFinish()
		 */
		public void onLogoutFinish() {
			mText.setText("You have logged out! ");
			mRequestButton.setVisibility(View.INVISIBLE);

		}
	}

	/**
	 * This is a sample class for any facebook request
	 * 
	 * @author optimus.support
	 * 
	 */
	public class SampleRequestListener extends FacebookRequestListener {

		/*
		 * This method is executed when the facebook request is
		 * complete(non-Javadoc)
		 * 
		 * @see
		 * com.facebook.android.AsyncFacebookRunner.RequestListener#onComplete
		 * (java.lang.String, java.lang.Object)
		 */
		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.i("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String name = json.getString("bio");
				final String id = json.getString("id");

				URL img_value = null;
				img_value = new URL("http://graph.facebook.com/" + id
						+ "/picture?type=large");
				final Bitmap mIcon1 = BitmapFactory.decodeStream(img_value
						.openConnection().getInputStream());

				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						ImageView user_picture = (ImageView) findViewById(R.id.ivProfilEpIC);
						user_picture.setImageBitmap(mIcon1);
					}
				});
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						mText.setText(name);
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
