package com.optimus.mobile.android;

/**
 * Class name : Constants
 * It defines the constant values that will be required in the application
 * 
 * @author Pooja
 *
 */
public class Constants {

	public static final String CONSUMER_KEY = "9wKJ9ho0MSj9t9l0IrTfQ"; //obtained from the twitter create new application page
	public static final String CONSUMER_SECRET= "6utM6iRWHVaEp2ueaYzCc28qQldpVewfeCvpBEmI7lE";//obtained from the twitter create new application page
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";//url from where request token is retrieved
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";//url from where access token can be retrieved
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";//url for authorisation of our application.This url also contains the login fields
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter"; //callback scheme defined
	public static final String	OAUTH_CALLBACK_HOST		= "callback";//callback host defined
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST; //callback url defined

}

