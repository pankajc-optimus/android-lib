////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: SessionEvents.java
// 
// Class which maintains the facebook login session state
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import java.util.LinkedList;

/**
 * This class maintains the facebook login session state
 * 
 * @author optimus
 */
public class SessionEvents {

	// Members
	private static LinkedList<AuthListener> mAuthListeners = new LinkedList<AuthListener>();
	private static LinkedList<LogoutListener> mLogoutListeners = new LinkedList<LogoutListener>();

	/**
	 * Associate the given listener with this Facebook object. The listener's
	 * callback interface will be invoked when authentication events occur.
	 * 
	 * @param listener
	 *            The callback object for notifying the application when auth
	 *            events happen.
	 */
	public static void addAuthListener(AuthListener listener) {
		mAuthListeners.add(listener);
	}

	/**
	 * Remove the given listener from the list of those that will be notified
	 * when authentication events occur.
	 * 
	 * @param listener
	 *            The callback object for notifying the application when auth
	 *            events happen.
	 */
	public static void removeAuthListener(AuthListener listener) {
		mAuthListeners.remove(listener);
	}

	/**
	 * Associate the given listener with this Facebook object. The listener's
	 * callback interface will be invoked when logout occurs.
	 * 
	 * @param listener
	 *            The callback object for notifying the application when log out
	 *            starts and finishes.
	 */
	public static void addLogoutListener(LogoutListener listener) {
		mLogoutListeners.add(listener);
	}

	/**
	 * Remove the given listener from the list of those that will be notified
	 * when logout occurs.
	 * 
	 * @param listener
	 *            The callback object for notifying the application when log out
	 *            starts and finishes.
	 */
	public static void removeLogoutListener(LogoutListener listener) {
		mLogoutListeners.remove(listener);
	}

	/**
	 * This method is executed when the facebook login is successful
	 */
	public static void onLoginSuccess() {
		for (AuthListener listener : mAuthListeners) {
			listener.onAuthSucceed();
		}
	}

	/**
	 * This method is executed when error occurs in facebook request
	 * 
	 * @param error
	 *            - error description
	 */
	public static void onLoginError(String error) {
		for (AuthListener listener : mAuthListeners) {
			listener.onAuthFail(error);
		}
	}

	/**
	 * This method is executed when the logout request is initiated
	 */
	public static void onLogoutBegin() {
		for (LogoutListener l : mLogoutListeners) {
			l.onLogoutBegin();
		}
	}

	/**
	 * This method is executed when the logout request completes
	 */
	public static void onLogoutFinish() {
		for (LogoutListener l : mLogoutListeners) {
			l.onLogoutFinish();
		}
	}

	/**
	 * Callback interface for authorization events.
	 * 
	 */
	public static interface AuthListener {

		/**
		 * Called when a auth flow completes successfully and a valid OAuth
		 * Token was received.
		 * 
		 * Executed by the thread that initiated the authentication.
		 * 
		 * API requests can now be made.
		 */
		public void onAuthSucceed();

		/**
		 * Called when a login completes unsuccessfully with an error.
		 * 
		 * Executed by the thread that initiated the authentication.
		 */
		public void onAuthFail(String error);
	}

	/**
	 * Callback interface for logout events.
	 * 
	 */
	public static interface LogoutListener {
		/**
		 * Called when logout begins, before session is invalidated. Last chance
		 * to make an API call.
		 * 
		 * Executed by the thread that initiated the logout.
		 */
		public void onLogoutBegin();

		/**
		 * Called when the session information has been cleared. UI should be
		 * updated to reflect logged-out state.
		 * 
		 * Executed by the thread that initiated the logout.
		 */
		public void onLogoutFinish();
	}

}
