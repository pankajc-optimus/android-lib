////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: LoginButton.java
// 
// Custom Image Button for facebook login.
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener;
import com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener;

/**
 * This is a custom image button for logging into facebook
 * 
 * @author optimus.support
 * 
 */
public class LoginButton extends ImageButton {

	// Members
	private Facebook mObjFacebook;
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;

	/**
	 * Constructor for login button
	 * 
	 * @param context
	 *            - context of current activity
	 */
	public LoginButton(Context context) {
		super(context);
	}

	/**
	 * Constructor for login button
	 * 
	 * @param context
	 *            - context of current activity
	 * @param attrs
	 *            - button attributes
	 */
	public LoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor for login button
	 * 
	 * @param context
	 *            - context of current activity
	 * @param attrs
	 *            - button attributes
	 * @param defStyle
	 *            - image button style
	 */
	public LoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Constructor for login button
	 * 
	 * @param activity
	 *            - current activity
	 * @param fb
	 *            - facebook object
	 */
	public void initialize(final Activity activity, final Facebook fb) {
		initialize(activity, fb, new String[] {});
	}

	/**
	 * This method initializes the login button
	 * 
	 * @param activity
	 *            - current activity
	 * @param fb
	 *            - Facebook object
	 * @param permissions
	 *            - set of permissions for login button
	 */
	public void initialize(final Activity activity, final Facebook fb,
			final String[] permissions) {
		// initialize the members
		mActivity = activity;
		mObjFacebook = fb;
		mPermissions = permissions;
		mHandler = new Handler();
		// initialize the button properties
		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);
		setImageResource(fb.isSessionValid() ? R.drawable.logout_button
				: R.drawable.login_button);
		drawableStateChanged();
		// initialize the sessions states
		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		setOnClickListener(new ButtonOnClickListener());
	}

	/**
	 * This is a class/interface/listener for the on click events for this
	 * button
	 * 
	 * @author optimus
	 * 
	 */
	private final class ButtonOnClickListener implements OnClickListener {

		/**
		 * This method is executed whenever button is clicked (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(View arg0) {
			// if user has already logged in to facebook account
			if (mObjFacebook.isSessionValid()) {

				SessionEvents.onLogoutBegin();
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
						mObjFacebook);
				asyncRunner.logout(getContext(), new LogoutRequestListener());
			}
			// if user has not logged in to facebook account
			else {
				mObjFacebook.authorize(mActivity, mPermissions,
						new LoginDialogListener());
			}
		}
	}

	/**
	 * This is a class/interface/listener for login dialog events
	 * 
	 * @author optimus
	 * 
	 */
	private final class LoginDialogListener implements DialogListener {
		/**
		 * This method is executed when the user has completed logging into
		 * facebook (non-Javadoc)
		 * 
		 * @see com.facebook.android.Facebook.DialogListener#onComplete(android.os.Bundle)
		 */
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		/**
		 * This method is executed when the any error occurs in the facebook API
		 * (non-Javadoc)
		 * 
		 * @see com.facebook.android.Facebook.DialogListener#onFacebookError(com.facebook.android.FacebookError)
		 */
		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		/**
		 * This method is executed when any error occurs in the facebook dialog
		 * (non-Javadoc)
		 * 
		 * @see com.facebook.android.Facebook.DialogListener#onError(com.facebook.android.DialogError)
		 */
		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		/**
		 * This method is executed when the user cancels the facebook login call
		 * (non-Javadoc)
		 * 
		 * @see com.facebook.android.Facebook.DialogListener#onCancel()
		 */
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	/**
	 * This is a class/interface/listener for logout request
	 * 
	 * @author optimus
	 * 
	 */
	private class LogoutRequestListener extends FacebookRequestListener {
		/**
		 * This method is executed when the log out is successfully completed
		 * (non-Javadoc)
		 * 
		 * @see com.facebook.android.AsyncFacebookRunner.RequestListener#onComplete(java.lang.String,
		 *      java.lang.Object)
		 */
		public void onComplete(String response, final Object state) {
			// callback should be run in the original thread,
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}

		/**
		 * This method is executed when there is some error in the facebook API
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.FacebookRequestListener#onFacebookError(com.facebook.android.FacebookError,
		 *      java.lang.Object)
		 */
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO : Action to be implemented in future phases

		}
	}

	/**
	 * This is a class/interface/listener for the user facebook login session
	 * 
	 * @author optimus
	 * 
	 */
	private class SessionListener implements AuthListener, LogoutListener {

		/**
		 * This method is executed when the authentication is successful
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener#onAuthSucceed()
		 */
		public void onAuthSucceed() {
			// change the button image resource
			setImageResource(R.drawable.logout_button);
			SessionStore.save(mObjFacebook, getContext());
		}

		/**
		 * This method is executed when the authentication fails (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.AuthListener#onAuthFail(java.lang.String)
		 */
		public void onAuthFail(String error) {
			// TODO : Action to be implemented in future phases
		}

		/**
		 * This method is executed when the user button has been clicked for
		 * logout request (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener#onLogoutBegin()
		 */
		public void onLogoutBegin() {
			// TODO : Action to be implemented in future phases
		}

		/**
		 * This method is executed when the logout request is successful
		 * (non-Javadoc)
		 * 
		 * @see com.optimus.mobile.oauth.facebook.SessionEvents.LogoutListener#onLogoutFinish()
		 */
		public void onLogoutFinish() {
			// clear the context and change the image button background resource
			SessionStore.clear(getContext());
			setImageResource(R.drawable.login_button);
		}
	}

}
