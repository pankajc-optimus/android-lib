////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: FacebookRequestListener.java
// 
// Interface for handling facebook request dialog callback.
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * 
 * This is an interface for handling facebook requests dialog callback.
 * 
 * This interface is to be implemented whenever we executing any post request
 * like incase of post to facebook wall.
 * 
 * @author optimus
 * 
 */
public abstract class FacebookDialogListener implements DialogListener {

	/**
	 * This method is executed whenever we get any facebook request error
	 * (Facebook request that could not be fulfilled) error from the facebook
	 * API.
	 */
	public void onFacebookError(FacebookError e, final Object state) {
		// TODO: Action to be decided in future phases.
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * This method is executed whenever we get a any error while processing the
	 * facebook dialog
	 */
	@Override
	public void onError(DialogError e) {
		// TODO: Action to be decided in future phases.
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * This method is called whenever we cancel the facebook dialog
	 */
	@Override
	public void onCancel() {
		// TODO: Action to be decided in future phases.
	}

}