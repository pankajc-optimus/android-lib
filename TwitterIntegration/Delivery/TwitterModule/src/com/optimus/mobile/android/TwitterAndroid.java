package com.optimus.mobile.android;

import java.util.Date;

import oauth.signpost.OAuth;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class name : TwitterModule This class declares functions for login, logout,
 * post twette, and show tweets. It initiates the first activity where user has
 * 4 options : 1. Login 2. Post tweet 3. show tweet of some person 4. Logout
 * 
 * @author Pooja
 * 
 */
public class TwitterAndroid extends Activity {
	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();

	// Declaring the UI elements
	private Button btn_login;
	private Button btn_logout;
	private Button btn_tweet;
	private Button btn_show_tweets;
	private TextView tv_status;
	private EditText et_message;
	private EditText et_username;

	private String mTweetMessage;
	private String mUsername;

	/**
	 * Runnable : mUpdateTwitterNotification Responsible for generating Toast
	 * notification when tweet message is sent
	 */
	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG)
					.show();
		}
	};

	// Called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_main);

		// Initialising the UI Elements
		tv_status = (TextView) findViewById(R.id.tv_status);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_tweet = (Button) findViewById(R.id.btn_tweet);
		et_message = (EditText) findViewById(R.id.et_message);
		btn_show_tweets = (Button) findViewById(R.id.btn_show);
		et_username = (EditText) findViewById(R.id.et_username);
		btn_logout = (Button) findViewById(R.id.btn_logout);

		// get the default shared preference
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// setting on click listener on the login button
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// if the user is already authenticated then he will be notified
				// that he is already logged in
				if (TwitterUtilities.isAuthenticated(prefs)) {
					Toast.makeText(TwitterAndroid.this,
							"You are already logged in", Toast.LENGTH_SHORT)
							.show();

				} else {
					/**
					 * If the user hasn't authenticated to Twitter yet, he'll be
					 * redirected via a browser to the twitter login page. Once
					 * the user authenticated, he'll authorise the Android
					 * application to send tweets on the users behalf.
					 */
					Intent i = new Intent(getApplicationContext(),
							PrepareRequestTokenActivity.class);

					startActivity(i);
				}

			}
		});
		// setting the onClick listener on the tweet button.This is responsible
		// for posting tweets.
		btn_tweet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				// if user is authenticated then he can post tweets
				if (TwitterUtilities.isAuthenticated(prefs))
					sendTweet();
				/**
				 * If the user hasn't authenticated to Twitter yet, he'll be
				 * notified that he has to login first
				 */
				else {
					Toast.makeText(TwitterAndroid.this, "You need to Login first !!!", Toast.LENGTH_SHORT).show();
				}

			}
		});

		// setting the on click listener on the logout button.On clicking this
		// button, user will be logged out and all his credentials will be
		// cleared.
		btn_logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				// if user is already logged out
				if (!TwitterUtilities.isAuthenticated(prefs))
					Toast.makeText(TwitterAndroid.this,
							"You are already Logged out", Toast.LENGTH_SHORT)
							.show();
				else {

					clearCredentials();

					// login status shows whether you are logged in or not
					updateLoginStatus();
				}
			}
		});

		// setting the onclick listener on the show tweet button.On clicking
		// this button, tweets of the username entered by you will be displayed
		// in the list view
		btn_show_tweets.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// get the username entered by the user in the editText box
				mUsername = et_username.getText().toString();

				// notifying the user in case of null string entered in the
				// username textbox
				if (mUsername.equals(""))
					Toast.makeText(
							TwitterAndroid.this,
							"usernname cannot be empty !!!\nPlease enter a valid username",
							Toast.LENGTH_LONG).show();
				else {

					// if username is not null, then control will be passed
					// to the next activity which is actually responsible
					// for loading the tweets and displaying them in the
					// list view
					Intent intent = new Intent(
							"com.optimus.mobile.android.ShowTweets");

					// passing the username in the next activity.
					intent.putExtra("username", mUsername);
					startActivity(intent);
				}
			}

			//
			/* } */
		});

	}

	/**
	 * sendTweet function responsible to send tweet messages
	 */
	public void sendTweet() {
		Thread thread = new Thread() {
			public void run() {

				try {
					// calling the sendTweet function from TwitterUtilities
					// Parameters passed are the shared preference data and the
					// message from the text box that user has typed in.
					// message from the text box is extracted by the grtTweet()
					// function
					TwitterUtilities.sendTweet(prefs, getTweetMsg());

					// calling the function to post the toast notification that
					// the tweet has been sent
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		thread.start();
	}

	/**
	 * function name : getTweetMsg Reads the message from the edit text box and
	 * returns the text typed by the user
	 * 
	 * @return text message from the edit text box in the string type
	 */
	private String getTweetMsg() {

		mTweetMessage = et_message.getText().toString() + "\n";
		return mTweetMessage + "Tweeting from Android App at "
				+ new Date().toLocaleString();
	}

	/**
	 * Function name : clearCredentials Clears all the user details and lets the
	 * user logout
	 */
	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();

		// removing all the details from the stored details
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}

	public void updateLoginStatus() {
		tv_status.setText("Logged into Twitter : "
				+ TwitterUtilities.isAuthenticated(prefs));
	}

	/**
	 * on resume if the activity
	 */
	@Override
	protected void onResume() {
		super.onResume();
		updateLoginStatus();
	}

}
