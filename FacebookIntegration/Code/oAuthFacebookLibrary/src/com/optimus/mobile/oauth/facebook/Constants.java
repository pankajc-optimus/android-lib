////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: Constants.java
// 
//This class contains all the constants which are to be used in the library or application
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

/**
 * This class contains all the constants which are to be used in the library or
 * application
 * 
 * @author optimus
 * 
 */
public class Constants {

	// All the constants related to post to wall API request
	public static final String PARAMS_FEED_APP_ID = "app_id";
	public static final String PARAMS_FEED_REDIRECT_URI = "redirect_uri";
	public static final String PARAMS_FEED_DISPLAY = "display";
	public static final String PARAMS_FEED_FROM = "from";
	public static final String PARAMS_FEED_TO = "to";
	public static final String PARAMS_FEED_LINK = "link";
	public static final String PARAMS_FEED_PICTURE = "picture";
	public static final String PARAMS_FEED_SOURCE = "source";
	public static final String PARAMS_FEED_NAME = "name";
	public static final String PARAMS_FEED_CAPTION = "caption";
	public static final String PARAMS_FEED_DESCRIPTION = "description";
	public static final String PARAMS_FEED_PROPERTIES = "properties";
	public static final String PARAMS_FEED_ACTIONS = "actions";
	public static final String PARAMS_FEED_REF = "ref";
	public static final String VALUES_FEED_TOUCH = "touch";
	public static final String VALUES_USER_AGENT = "user_agent";
	public static final String ACTION_FEED = "feed";

	// All the constants related to parameters to be passed in facebook API
	// requests
	public static final String PARAMS_TOKEN = "access_token";
	public static final String PARAMS_TYPE = "type";
	public static final String PARAMS_CLIENT_ID = "client_id";
	public static final String PARAMS_CENTER = "center";
	public static final String PARAMS_DISTANCE = "distance";
	public static final String PARAMS_SEARCH = "search";
	public static final String PARAMS_PHOTO = "photo";
	public static final String PARAMS_CAPTION = "caption";
	public static final String PARAMS_METHOD = "method";
	public static final String PARAMS_QUERY = "query";
	public static final String PARAMS_METADATA = "metadata";
	public static final String PARAMS_SEARCH_Q = "q";
	public static final String PARAMS_PLACE = "place";
	public static final String PARAMS_MESSAGE = "message";
	public static final String PARAMS_COORDINATES = "coordinates";

	public static final String VALUES_PLACE = "place";
	public static final String VALUES_FQL_QUERY = "fql.query";

	public static final String REQUEST_ME = "me";
	public static final String REQUEST_CHECKIN = "me/checkins";
	public static final String REQUEST_UPLOAD_PHOTO = "me/photos";

	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_POST = "POST";

	public static final String URL_BASE_DIALOG = "https://m.facebook.com/dialog/";
	public static final String URL_BASE_GRAPH = "https://graph.facebook.com/";
	public static final String URL_BASE_RESTAPI = "https://api.facebook.com/restserver.php";

	public static final String URI_REDIRECT = "fbconnect://success";

	// Maximum size of image to be posted on facebook
	public static int MAX_IMAGE_DIMENSION = 720;

}
