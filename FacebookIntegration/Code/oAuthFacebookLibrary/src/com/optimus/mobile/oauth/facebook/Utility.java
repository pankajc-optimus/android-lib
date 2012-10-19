////////////////////////////////////////////////////////////////////
// oAuth Facebook Android Library
// File: Utility.java
// 
// This class provides the methods for various actions to be performed through
// the facebook API
////////////////////////////////////////////////////////////////////
package com.optimus.mobile.oauth.facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FbDialog;

/**
 * This class provides the methods for various actions to be performed through
 * the facebook API
 * 
 * @author optimus
 */
public class Utility {

	/**
	 * This method calls an asynchronous task to get user information using
	 * facebook Graph API. <br/>
	 * In this method the asynchronous runner create a facebook request on a
	 * separate a thread and the FacebookRequestListener object returns the
	 * output of that request back to user.
	 * 
	 * Make a request to the Facebook Graph API with the given string parameters
	 * using an HTTP GET (default method).
	 * 
	 * <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler. * Will fetch data from
	 * <b>http://graph.facebook.com/me </b> for current authenticated user
	 * 
	 * @see http://developers.facebook.com/docs/api
	 * 
	 * @param asyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener <br/>
	 *            This listener will give the callback result to the application
	 */
	public static void requestUserInfo(AsyncFacebookRunner asyncRunner,
			FacebookRequestListener listener) {
		// asynchronous facebook request
		asyncRunner.request(Constants.REQUEST_ME, listener);
	}

	/**
	 * This method uses the dialog base facebook request to post a custom feed
	 * to the facebook wall. <br/>
	 * The elements to be put in the bundle can be checked from
	 * (https://developers.facebook.com/docs/reference/dialogs/feed/). <br/>
	 * This method converts the Bundle object into facebook Dialog Base URI and
	 * shows it to user in a com.facebook.android.FbDialog.FbDialog object.
	 * 
	 * 
	 * @see https://developers.facebook.com/docs/reference/dialogs/feed/
	 * 
	 * 
	 * @param current
	 *            - context of current activity
	 * @param objFb
	 *            - com.facebook.android.Facebook Object
	 * @param params
	 *            - Bundle object , A sample bundle implementation can be <br/>
	 * <br/>
	 *            Bundle params = new Bundle(); <br/>
	 *            params.putString(Constants.PARAMS_FEED_CAPTION,
	 *            "From Android"); <br/>
	 *            params.putString(Constants.PARAMS_FEED_DESCRIPTION,
	 *            "Some Message"); <br/>
	 *            params.putString(Constants.PARAMS_FEED_NAME, "Android"); <br/>
	 *            params.putString(Constants.PARAMS_FEED_PICTURE,
	 *            "http://www.optimusinfo.com/wp-content/uploads/2012/04/android_developers.png"
	 *            ); <br/>
	 *            This bundle object will be converted to url using function
	 *            com.optimus.mobile.oauth.facebook.Utility.encodeUrl
	 * 
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener
	 */
	public static void postToWall(Context current, Facebook objFb,
			Bundle params, FacebookDialogListener listener) {
		String dialogUrl = Constants.URL_BASE_DIALOG + Constants.ACTION_FEED;
		params.putString(Constants.PARAMS_FEED_DISPLAY,
				Constants.VALUES_FEED_TOUCH);
		params.putString(Constants.PARAMS_FEED_REDIRECT_URI,
				Constants.URI_REDIRECT);
		params.putString(Constants.PARAMS_FEED_APP_ID, objFb.getAppId());

		if (objFb.isSessionValid()) {
			params.putString(Constants.PARAMS_TOKEN, objFb.getAccessToken());
		}

		String url = dialogUrl + "?" + encodeUrl(params);

		if (current.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			// TODO : In future phases
			showAlert(current, "Error",
					"Application requires permission to access the Internet");
		} else {
			new FbDialog(current, url, listener).show();
		}
	}

	/**
	 * This method deletes the wall post using the facebook post id. <br/>
	 * Make a request to the Facebook Graph API with the given HTTP method and
	 * string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @See http://developers.facebook.com/docs/api
	 * 
	 * 
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param postId
	 *            - postId
	 * @param listener
	 *            object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void deleteWallPost(AsyncFacebookRunner mAsyncRunner,
			String postId, FacebookRequestListener listener) {
		mAsyncRunner.request(postId, new Bundle(), Constants.METHOD_DELETE,
				listener, null);
	}

	/**
	 * This method gives the near by location in the forma of json array.<br/>
	 * 
	 * In this method the asynchronous runner create a facebook request on a
	 * separate a thread and the FacebookRequestListener object returns the
	 * output of that request back to user. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @See https://developers.facebook.com/docs/reference/api/
	 * 
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * 
	 * @param latitude
	 *            - latitude value in decimals i.e. not a multiple of 1E6
	 * @param longitude
	 *            - longitude value in decimals i.e. not a multiple of 1E6
	 * @param distanceProximity
	 *            - near by distance proximity
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void getPlaces(AsyncFacebookRunner mAsyncRunner,
			String latitude, String longitude, String distanceProximity,
			FacebookRequestListener listener) {
		Bundle params = new Bundle();
		params.putString(Constants.PARAMS_TYPE, Constants.VALUES_PLACE);
		params.putString(Constants.PARAMS_CENTER, latitude + "," + longitude);
		params.putString(Constants.PARAMS_DISTANCE, distanceProximity);

		mAsyncRunner.request(Constants.PARAMS_SEARCH, params, listener);
	}

	/**
	 * This method checks in a particular location with a place
	 * recognizable(place id) by facebook and message.<br/>
	 * This method makes a request to the Facebook Graph API with the given HTTP
	 * method and string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @see http://developers.facebook.com/docs/api
	 * 
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param placeId
	 *            - placeId returned the facebook API response in
	 *            com.optimus.mobile.oauth.facebook.Utility.getPlaces call .
	 * @param message
	 *            - custom message
	 * @param coordinates
	 *            - a JSON object with two elements latitude and longitude with
	 *            their values
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void checkInLocation(AsyncFacebookRunner mAsyncRunner,
			String placeId, String message, JSONObject coordinates,
			FacebookRequestListener listener) {
		Bundle params = new Bundle();
		params.putString(Constants.PARAMS_PLACE, placeId);
		params.putString(Constants.PARAMS_MESSAGE, message);
		params.putString(Constants.PARAMS_COORDINATES, coordinates.toString());
		mAsyncRunner.request(Constants.REQUEST_CHECKIN, params,
				Constants.METHOD_POST, listener, null);
	}

	/**
	 * This method uploads a photo to facebook profile. This method compresses
	 * the image to a minimum size of 720 pixels if image is larger than this
	 * size. <br/>
	 * This method makes a request to the Facebook Graph API with the given HTTP
	 * method and string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @see http://developers.facebook.com/docs/api
	 * 
	 * @param current
	 *            - context of current running activity
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param photoUri
	 *            - Uri of photo selected/captured
	 * @param Caption
	 *            - photo caption
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void uploadPhoto(Context current,
			AsyncFacebookRunner mAsyncRunner, Uri photoUri, String Caption,
			FacebookRequestListener listener) {
		Bundle params = new Bundle();
		try {
			params.putByteArray(Constants.PARAMS_PHOTO,
					scaleImage(current, photoUri));
		} catch (IOException e) {
			// TODO To be done in future phases
			e.printStackTrace();
		}
		params.putString(Constants.PARAMS_CAPTION, Caption);
		mAsyncRunner.request(Constants.REQUEST_UPLOAD_PHOTO, params,
				Constants.METHOD_POST, listener, null);
	}

	/**
	 * This method provides an interface to execute FQL queries.<br/>
	 * This method makes a request to the Facebook Graph API with the given HTTP
	 * method and string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @see https://developers.facebook.com/docs/reference/fql/
	 * 
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param Query
	 *            - FQL query string
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void executeFqlQuery(AsyncFacebookRunner mAsyncRunner,
			String query, FacebookRequestListener listener) {
		Bundle params = new Bundle();
		params.putString(Constants.PARAMS_METHOD, Constants.VALUES_FQL_QUERY);
		params.putString(Constants.PARAMS_QUERY, query);
		mAsyncRunner.request(null, params, listener);
	}

	/**
	 * This method provides an interface for Graph API calls.<br/>
	 * This method works similar to Graph API Explorer (See
	 * https://developers.facebook
	 * .com/tools/explorer/?method=GET&path=1115266870%3Ffields%3Did%2Cname) <br/>
	 * This method makes a request to the Facebook Graph API with the given HTTP
	 * method and string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @see https://developers.facebook.com/docs/reference/api/
	 * @see https 
	 *      ://developers.facebook.com/tools/explorer/?method=GET&path=1115266870
	 *      %3Ffields%3Did%2Cname
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param Query
	 *            - root string / object id
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void executeGraphAPIRequest(AsyncFacebookRunner mAsyncRunner,
			String objectId, String metadata, FacebookRequestListener listener) {
		Bundle params = new Bundle();
		params.putString(Constants.PARAMS_METADATA, metadata);
		mAsyncRunner.request(objectId, params, listener);
	}

	/**
	 * This method provides an interface to execute the Search Graph API.<br/>
	 * This method works similar to Graph API Explorer (See
	 * https://developers.facebook
	 * .com/tools/explorer/?method=GET&path=1115266870%3Ffields%3Did%2Cname) <br/>
	 * This method makes a request to the Facebook Graph API with the given HTTP
	 * method and string parameters. <br/>
	 * Note that this method is asynchronous and the callback will be invoked in
	 * a background thread; operations that affect the UI will need to be posted
	 * to the UI thread or an appropriate handler.
	 * 
	 * @see https://developers.facebook.com/docs/reference/api/
	 * 
	 * @param mAsyncRunner
	 *            - object of com.facebook.android.AsyncFacebookRunner
	 * @param query
	 *            - query string (For e.g coffee)
	 * @param type
	 *            - query type (for eg place)
	 * @param listener
	 *            - object of
	 *            com.optimus.mobile.oauth.facebook.FacebookRequestListener<br/>
	 *            This listener will give the callback result to the application
	 */
	public static void graphApiSearch(AsyncFacebookRunner mAsyncRunner,
			String query, String type, FacebookRequestListener listener) {
		Bundle params = new Bundle();
		params.putString(Constants.PARAMS_SEARCH_Q, query);
		params.putString(Constants.PARAMS_TYPE, type);
		mAsyncRunner.request("search", params, listener);
	}

	/**
	 * Display a simple alert dialog with the given text and title.
	 * 
	 * @param context
	 *            - current activity context
	 * @param title
	 *            Alert dialog title
	 * @param text
	 *            Alert dialog message
	 */
	public static void showAlert(Context context, String title, String text) {
		Builder alertBuilder = new Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		alertBuilder.create().show();
	}

	/**
	 * This method converts a bundle object into Url string.<br/>
	 * 
	 * @param parameters
	 *            - Bundle object
	 * @return - url string
	 */
	public static String encodeUrl(Bundle parameters) {
		if (parameters == null) {
			return "";
		}
		StringBuilder objSb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			Object parameter = parameters.get(key);
			if (!(parameter instanceof String)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				objSb.append("&");
			}
			objSb.append(URLEncoder.encode(key) + "="
					+ URLEncoder.encode(parameters.getString(key)));
		}
		return objSb.toString();
	}

	/**
	 * This method returns the image in byte array form from the Uri passed to
	 * it.
	 * 
	 * @param context
	 *            - current activity context
	 * @param photoUri
	 *            - Uri of photo
	 * @return - image representation in ByteArray form
	 * @throws IOException
	 */
	public static byte[] scaleImage(Context context, Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > Constants.MAX_IMAGE_DIMENSION
				|| rotatedHeight > Constants.MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) Constants.MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) Constants.MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();
		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		String type = context.getContentResolver().getType(photoUri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (type.equals("image/png")) {
			srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		return bMapArray;
	}

	/**
	 * This method returns the orientation of image.
	 * 
	 * @param context
	 *            - current activity context
	 * @param photoUri
	 *            - photo Uri
	 * @return integer value of orientation
	 */
	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

}
