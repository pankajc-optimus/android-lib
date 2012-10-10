////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: SessionStore.java
// 
// Class which stores the session state variables
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import com.facebook.android.Facebook;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStore {
	// key constants
	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-session";

	/**
	 * This method saves the facebook session variables
	 * 
	 * @param session
	 *            - facebook object
	 * @param context
	 *            - current activity context
	 * @return - save status
	 */
	public static boolean save(Facebook session, Context context) {
		// initiate the shared preferences editor
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putString(TOKEN, session.getAccessToken());
		editor.putLong(EXPIRES, session.getAccessExpires());
		return editor.commit();
	}

	/**
	 * This method restore the last facebook session state
	 * 
	 * @param session
	 *            - facebook object
	 * @param context
	 *            - current activity context
	 * @return - restore status
	 */
	public static boolean restore(Facebook session, Context context) {
		// initiate the shared preferences object
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		session.setAccessToken(savedSession.getString(TOKEN, null));
		session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
		return session.isSessionValid();
	}

	/**
	 * This method clears all the shared preferences saved states
	 * 
	 * @param context
	 *            - current activity context
	 */
	public static void clear(Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
	}

}
