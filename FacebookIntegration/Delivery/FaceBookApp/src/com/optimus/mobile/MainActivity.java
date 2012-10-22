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
import java.text.DateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.Util;
import com.optimus.mobile.oauth.facebook.Constants;
import com.optimus.mobile.oauth.facebook.FacebookDialogListener;
import com.optimus.mobile.oauth.facebook.FacebookRequestListener;
import com.optimus.mobile.oauth.facebook.LoginButton;
import com.optimus.mobile.oauth.facebook.SessionEvents;
import com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener;
import com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener;
import com.optimus.mobile.oauth.facebook.SessionStore;
import com.optimus.mobile.oauth.facebook.Utility;

/**
 * This is a sample activity to test oAuth facebook 2.0 library
 * 
 * @author optimus
 * 
 */
public class MainActivity extends Activity {

	// Members
	public static final String APP_ID = "156170661193063";
	private LoginButton mLoginButton;
	private TextView mText;
	private Facebook mFacebook;

	private ImageView mProfilePic;
	private Button mAccessTokenButton, mRequestInformationButton, mPostToWall,
			mDeletePost, mCheckIn, mUploadPhoto, mFqlInterface, mGraphAPI,
			mSearch;
	private EditText mPostId, mLatitude, mLongitude, mQuery, mType;

	public static AsyncFacebookRunner mAsyncRunner;

	final static int FACEBOOK_AUTHORIZE = 0;
	final static int PICK_PHOTO_FROM_GALLERY = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Request to remove the title bar for this activity.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

		mLoginButton = (LoginButton) findViewById(R.id.lbFacebookLogin);

		mAccessTokenButton = (Button) findViewById(R.id.btnAccessToken);
		mRequestInformationButton = (Button) findViewById(R.id.btnRequestInfo);
		mPostToWall = (Button) findViewById(R.id.btnPostToWall);
		mDeletePost = (Button) findViewById(R.id.btnDeleteWallPost);
		mCheckIn = (Button) findViewById(R.id.btnCheckIn);
		mUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);
		mFqlInterface = (Button) findViewById(R.id.btnFqlInterface);
		mGraphAPI = (Button) findViewById(R.id.btnGraphAPI);
		mSearch = (Button) findViewById(R.id.btnSearch);

		mText = (TextView) findViewById(R.id.tvLoginStatus);
		mPostId = (EditText) findViewById(R.id.etWallPostId);
		mLatitude = (EditText) findViewById(R.id.etlatitude);
		mLongitude = (EditText) findViewById(R.id.etLongitude);
		mQuery = (EditText) findViewById(R.id.etQuery);
		mType = (EditText) findViewById(R.id.etType);

		mProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

		// initialize facebook object and facebook request runner
		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());

		mLoginButton.initialize(this,
				this.getResources().getDrawable(R.drawable.login_button), this
						.getResources().getDrawable(R.drawable.logout_button),
				mFacebook);

		// if session is facebook session is still active then show the access
		// token button
		mAccessTokenButton
				.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
						: View.INVISIBLE);
		mAccessTokenButton.setOnClickListener(new OnClickListener() {

			/**
			 * This method is executed whenever access token button is clicked
			 */
			@Override
			public void onClick(View v) {
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(
						DateFormat.SHORT, DateFormat.SHORT);
				Utility.showAlert(
						MainActivity.this,
						MainActivity.this
								.getString(R.string.title_access_token),
						mFacebook
								.getAccessToken()
								.concat("\n\n\n")
								.concat(MainActivity.this
										.getString(R.string.title_expiration_time))
								.concat("\n")
								.concat(dateFormat.format(new Date(mFacebook
										.getAccessExpires()))));
			}
		});

		// if session is facebook session is still active then show the request
		// information button
		mRequestInformationButton
				.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
						: View.INVISIBLE);

		mRequestInformationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utility.requestUserInfo(mAsyncRunner,
						new InformationRequestListener());
			}
		});

		// if session is facebook session is still active then show the post
		// to wall button
		mPostToWall.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mPostToWall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle params = new Bundle();
				params.putString(Constants.PARAMS_FEED_CAPTION, "From Android");
				params.putString(Constants.PARAMS_FEED_DESCRIPTION,
						"Some Message");
				params.putString(Constants.PARAMS_FEED_NAME, "Android");
				params.putString(Constants.PARAMS_FEED_PICTURE,
						"http://www.optimusinfo.com/wp-content/uploads/2012/04/android_developers.png");
				Utility.postToWall(MainActivity.this, mFacebook, params,
						new UpdateStatusListener());

			}
		});

		// if session is facebook session is still active then show the access
		// token button
		mDeletePost.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mDeletePost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPostId.getText().toString().length() != 0) {
					Utility.deleteWallPost(mAsyncRunner, mPostId.getText()
							.toString(), new WallPostDeleteListener());
				} else {
					Utility.showAlert(MainActivity.this, "Error",
							"Post id cannot be blank");
				}
			}
		});

		LocationManager lmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Initialize the criteria for current location manager.
		Criteria currCriteria = new Criteria();
		String towers = lmanager.getBestProvider(currCriteria, false);
		Location cLocation = lmanager.getLastKnownLocation(towers);

		// Set the current location
		if (cLocation != null) {
			mLatitude.setText(String.valueOf((cLocation.getLatitude())));
			mLongitude.setText(String.valueOf((cLocation.getLongitude())));
		}

		// if session is facebook session is still active then show the access
		// token button
		mCheckIn.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mCheckIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utility.getPlaces(mAsyncRunner, mLatitude.getText().toString(),
						mLongitude.getText().toString(), "1000",
						new FetchPlacesListener());
			}
		});

		// if session is facebook session is still active then show the access
		// token button
		mUploadPhoto.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mUploadPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
				startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
			}
		});

		// if session is facebook session is still active then show the access
		// token button
		mFqlInterface.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mFqlInterface.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new FQLInterfaceDialog(MainActivity.this, mAsyncRunner).show();
			}
		});

		// if session is facebook session is still active then show the access
		// token button
		mGraphAPI.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mGraphAPI.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GraphInterfaceDialog(MainActivity.this, mAsyncRunner)
						.show();
			}
		});

		// if session is facebook session is still active then show the access
		// token button
		mSearch.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);
		mSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utility.graphApiSearch(mAsyncRunner, mQuery.getText()
						.toString(), mType.getText().toString(),
						new SearchListener());
			}
		});
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
		switch (requestCode) {
		case FACEBOOK_AUTHORIZE:
			mFacebook.authorizeCallback(requestCode, resultCode, data);
			break;
		case PICK_PHOTO_FROM_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				Uri photoUri = data.getData();
				if (photoUri != null) {
					Utility.uploadPhoto(MainActivity.this, mAsyncRunner,
							photoUri, "Android App Upload",
							new UploadPhotoListener());
				}
			}
			break;
		}

	}

	/**
	 * This is a sample authentication listener when the user request for
	 * authentication with facebook API
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
			mText.setText("Your have logged in!");
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

		}
	}

	public class UploadPhotoListener extends FacebookRequestListener {

		@Override
		public void onComplete(final String response, Object state) {
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					Utility.showAlert(MainActivity.this, "Photo upload",
							response);
				}
			});
		}

	}

	/**
	 * This is a sample interface class to get the facebook user information
	 * 
	 * @author optimus
	 * 
	 */
	public class InformationRequestListener extends FacebookRequestListener {

		@Override
		public void onComplete(final String response, Object state) {
			try {

				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Utility.showAlert(MainActivity.this,
								"Request Information", response);
					}
				});

				final JSONObject json = Util.parseJson(response);
				final String id = json.getString("id");
				final String name = json.getString("name");

				URL img_value = null;
				img_value = new URL("http://graph.facebook.com/" + id
						+ "/picture?type=small");
				final Bitmap mIcon1 = BitmapFactory.decodeStream(img_value
						.openConnection().getInputStream());

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						mProfilePic.setImageBitmap(mIcon1);
						mText.setText(name);
					}
				});

			} catch (JSONException e) {
				// TODO : In future phases
			} catch (FacebookError e) {
				// TODO : In future phases
			} catch (MalformedURLException e) {
				// TODO : In future phases
			} catch (IOException e) {
				// TODO : In future phases
			}
		}
	}

	/*
	 * callback for the feed dialog which updates the profile status
	 */
	public class UpdateStatusListener extends FacebookDialogListener {
		@Override
		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");

			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					mPostId.setText(postId);
				}
			});
		}

		@Override
		public void onFacebookError(FacebookError error) {

		}

		@Override
		public void onCancel() {

		}
	}

	public class WallPostDeleteListener extends FacebookRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					mPostId.setText("");
					Utility.showAlert(MainActivity.this, "Response", response);
				}
			});
		}
	}

	public class FetchPlacesListener extends FacebookRequestListener {
		@Override
		public void onComplete(final String response, Object state) {
			MainActivity.this.runOnUiThread(new Runnable() {

				public void run() {

					try {
						final JSONArray array = new JSONObject(response)
								.getJSONArray("data");
						final String[] jObjectArray = new String[array.length()];
						for (int i = 0; i < array.length(); i++) {
							jObjectArray[i] = array.getJSONObject(i).toString();
						}
						Builder alertBuilder = new Builder(MainActivity.this);
						alertBuilder.setSingleChoiceItems(jObjectArray, 0,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										try {
											checkInLocation(array
													.getJSONObject(which));
										} catch (JSONException e) {
										}
									}
								});
						alertBuilder.create().show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	public class SearchListener extends FacebookRequestListener {
		@Override
		public void onComplete(final String response, Object state) {
			MainActivity.this.runOnUiThread(new Runnable() {

				public void run() {

					try {
						final JSONArray array = new JSONObject(response)
								.getJSONArray("data");
						final String[] jObjectArray = new String[array.length()];
						for (int i = 0; i < array.length(); i++) {
							jObjectArray[i] = array.getJSONObject(i).toString();
						}
						Builder alertBuilder = new Builder(MainActivity.this);
						alertBuilder.setSingleChoiceItems(jObjectArray, 0,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						alertBuilder.create().show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	public class CheckInListener extends FacebookRequestListener {

		@Override
		public void onComplete(final String response, Object state) {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Utility.showAlert(MainActivity.this, "Check In", response);

				}
			});
		}

	}

	/**
	 * 
	 * @param mplace
	 */
	private void checkInLocation(JSONObject place) {

		try {
			final String message = mText.getText().toString().concat(" was at");
			final String name = place.getString("name");
			final String placeID = place.getString("id");
			final JSONObject location = new JSONObject();
			location.put("latitude", mLatitude.getText().toString());
			location.put("longitude", mLongitude.getText().toString());

			new Builder(MainActivity.this)
					.setTitle("Check In")
					.setMessage(message.concat(" ").concat(name))
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Utility.checkInLocation(mAsyncRunner,
											placeID, message, location,
											new CheckInListener());
								}
							}).setNegativeButton("Cancel", null).show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
