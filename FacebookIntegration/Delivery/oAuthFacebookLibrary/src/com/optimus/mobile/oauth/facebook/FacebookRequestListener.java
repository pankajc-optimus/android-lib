////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: FacebookRequestListener.java
// 
// Interface for handling facebook request callback.
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * 
 * This is an interface for handling facebook requests callback.
 * 
 * This interface is to be implemented whenever we are creating any request for
 * the facebook API.
 * 
 * @author optimus
 * 
 */
public abstract class FacebookRequestListener implements RequestListener {

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
	 * This method is executed whenever we get a file not found exception while
	 * any facebook request is in progress
	 */
	public void onFileNotFoundException(FileNotFoundException e,
			final Object state) {
		// TODO: Action to be decided in future phases.
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * This method is executed whenever we get an Input/Output exception while
	 * facebook request is in progress
	 */	
	public void onIOException(IOException e, final Object state) {
		// TODO: Action to be decided in future phases.
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	/**
	 * This method is executed whenever we get any exception in the URl of the
	 * facebook request
	 */
	public void onMalformedURLException(MalformedURLException e,
			final Object state) {
		// TODO: Action to be decided in future phases.
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

}
