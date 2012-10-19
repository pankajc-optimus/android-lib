package com.optimus.mobile.android;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;

/**class name : TwitterUtilities
 * 
 * Description : Twitter utilities class
 *               Checks whether the user is authenticated or not
 *               and defines function to send tweet message 
 * 
 * @author Pooja
 *
 */
public class TwitterUtilities {
	
	/**function name : isAuthenticated
	 *                 checks whether the user is authenticated or not
	 * 
	 * @param SharedPreferences instance
	 * @return true if authenticated, returns false if not authenticated
	 */
	public static boolean isAuthenticated(SharedPreferences prefs) {

		//Retrieves the token and secret values from the stored values
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		//creating the access token from the token and secret values
		twitter4j.http.AccessToken accesToken = new twitter4j.http.AccessToken(token,
				secret);
		
		Twitter twitter = new TwitterFactory().getInstance();
		//Twitter factory is a factory class for twitter
		
		//setting the oAuth consumer and oAuth access token
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accesToken);

		try {
			//getting the current twitter account settings
			twitter.getAccountSettings();
			return true; //true returned if account seetings are easily returned .It means that user is authenticated
		} catch (TwitterException e) {
			
			//In case of any error or exception, false is returned.It means that user is not authenticated
			return false;
		}
	}

	/**Function name : sendTweet
	 * Description : This function is responsible to post tweet messages of the logged in user
	 * 
	 * @param SharedPreferences instance
	 * @param message that has to be posted
	 * @throws Exception in case of any error in between the process
	 */
	public static void sendTweet(SharedPreferences prefs, String message)
			throws Exception {
		
		//getting the token and secret values from the stored values
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		//creating the access token from the token and secret values
		AccessToken accessToken = new AccessToken(token, secret);
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
		
		//updating the twitter status. It posts the twitter message that the user has enterd.
		twitter.updateStatus(message);
	}
}
