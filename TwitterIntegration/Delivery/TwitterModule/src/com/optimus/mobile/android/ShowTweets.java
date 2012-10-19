package com.optimus.mobile.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * class name : ShowTweets Responsible for showing Tweets of the person whose
 * username has been entered by the user. Tweets are shown in a list view with
 * the profile image and the actual tweet text.
 * 
 * @author Pooja
 * 
 */
public class ShowTweets extends Activity {
	private String mUsername; // username that has been entered by the user in
								// the text box
	private ListView lv_tweets;// list view where the tweets will be displayed
	private ProgressDialog mProgressDialog;// progress dialog to show the
											// progress of loading of the tweets

	private ArrayList<TweetModel> tweetReader = new ArrayList<TweetModel>();
	// Creating the ArrayList of TweetModel class type for storing the tweet
	// feed data.
	// TweetModel is the model class where tweet text, and profile image of each
	// tweets can be referred.

	/**
	 * Called when the activity is first loaded.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_tweets);

		// getting the username from the previous activity.
		// username has been entered in the edit textbox by the user
		mUsername = getIntent().getExtras().getString("username");

		// generating a notification of the retrieved username from the previous
		// intent
		// This is to check whether the same username has been passed which has
		// been entered by the user in the previous activity
		Toast.makeText(ShowTweets.this, "user name is " + mUsername,
				Toast.LENGTH_SHORT).show();

		// Initialising the list view
		lv_tweets = (ListView) findViewById(R.id.lv_tweets);

		// execute the async class to load the tweet feeds.
		new ShowTweetsAsync().execute();
	}

	/**
	 * class name : ShowTweetsAsync Responsible for loading the tweets
	 * asynchronously
	 * 
	 * @author Pooja
	 * 
	 */
	public class ShowTweetsAsync extends AsyncTask<String, Integer, String> {

		// This function will be executed first.
		protected void onPreExecute() {

			// creating the progress dialog to show the progress
			mProgressDialog = ProgressDialog.show(ShowTweets.this, "twitter",
					"Tweets are loading. Please wait...", true);
		}

		/**
		 * This function will execute in the background It is actually Load the
		 * tweets of the entered user It will load the JSON result and will
		 * extract the tweet text and profile image from the JSON result
		 */
		protected String doInBackground(String... params) {

			StringBuilder builder = new StringBuilder();

			try {
				// create a new HTTPClient
				HttpClient hc = new DefaultHttpClient();

				// This url contains the tweets of the user where screen_name
				// should be equal to the username of the person whose tweets
				// are to be loaded and count specifies the no of tweets to be
				// loaded
				HttpGet get = new HttpGet(
						"http://api.twitter.com/1/statuses/user_timeline.json?screen_name="
								+ mUsername + "&count=30");

				// Executing the request to the above URL
				HttpResponse rp = hc.execute(get);

				// If there occurs no problems while accessing the above URL,
				// then following code is executed
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = rp.getEntity();

					// getting the content of the URL
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line;

					// converting the text of the URL content in the form of
					// string
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					String result = builder.toString();

					// creating a JSONArray of the above string
					JSONArray sessions = new JSONArray(result);

					// downloading feeds
					for (int i = 0; i < sessions.length(); i++) {

						// getting the JSONObject at i th position
						JSONObject session = sessions.getJSONObject(i);

						// creating an instance of TweetModel class so that the
						// entries can be saved.
						TweetModel tweet = new TweetModel();

						// setting the content, profile image of the
						// tweet feed

						// actual content is present under the label 'text'
						tweet.content = session.getString("text");

						// under the label 'user' is a jsonObject which contains
						// the profile image of the user
						String user = session.getString("user");

						// creating another instance of JSONObject to extract
						// the profile image of the user
						JSONObject userInstance = new JSONObject(user);

						// getting the profile image url and setting it in the
						// TweetModel object
						tweet.imageUrl = userInstance
								.getString("profile_image_url_https");

						// extracting the image from the above url.
						try {
							URL url = new URL(tweet.imageUrl);
							Bitmap bmp = BitmapFactory.decodeStream(url
									.openConnection().getInputStream());

							// setting the profile image in the TweetModel
							// obeject
							tweet.profileImage = bmp;

						} catch (Exception e) {
							Log.e("Exception", "Exception" + e);
						}

						// adding the details in the form of TweetModel object
						// to the arraylist tweetReader
						tweetReader.add(tweet);
					}
				}
			}
			// in case of error while retrieving the json data, exception will
			// be thrown.
			catch (Exception e) {
				Log.e("TwitterFeedActivity", "Error loading JSON", e);
			}// end of if block

			// if there occurs any problem while accessing the url, then null
			// will be returned
			return null;

		}

		/**
		 * This function will be executed after the background function has
		 * finished its processing
		 */
		protected void onPostExecute(String result) {

			// progress dialog has to be dismissed now
			mProgressDialog.dismiss();

			// setting the adapter for the display of tweets in the list view
			lv_tweets.setAdapter(new TweetListAdapter(ShowTweets.this,
					R.layout.twitter_feeds, tweetReader));

		}
	}

	/**
	 * class name : TweetListAdapter Responsible for the display of tweets in
	 * the list view
	 * 
	 * @author Pooja
	 * 
	 */
	private class TweetListAdapter extends ArrayAdapter<TweetModel> {

		// Declaring an ArrayList of type TweetModel
		private ArrayList<TweetModel> tweetReader;

		// Initialising the constructor
		public TweetListAdapter(Context context, int textViewResourceId,
				ArrayList<TweetModel> items) {
			super(context, textViewResourceId, items);
			this.tweetReader = items;

		}

		/**
		 * function name : getView Responsible for setting the tweet text in the
		 * text view and profile image in the image view properly in the given
		 * layout
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				// Setting the items in the list to the specified layout
				view = inflater.inflate(R.layout.twitter_feeds, null);

			}

			// get the current position of the object of TweetModel
			TweetModel model = tweetReader.get(position);

			// get the profile image from the object of TweetModel class
			Bitmap profilePicture = model.profileImage;

			// Initialising the imageview of the list iten
			ImageView img = (ImageView) view.findViewById(R.id.img_user);

			// setting the profile image on the image view
			img.setImageBitmap(profilePicture);

			// Initialising the text view which will contain the tweet text
			TextView status = (TextView) view.findViewById(R.id.tv_tweetsfeed);

			// setting the text to the text of the tweet of the current
			// TweetModel Object.
			status.setText(model.content);

			return view;
		}
	}

}
