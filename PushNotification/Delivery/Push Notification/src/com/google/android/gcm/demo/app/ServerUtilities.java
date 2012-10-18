package com.google.android.gcm.demo.app;

import static com.google.android.gcm.demo.app.CommonUtilities.SERVER_URL;
import static com.google.android.gcm.demo.app.CommonUtilities.TAG;
import static com.google.android.gcm.demo.app.CommonUtilities.displayMessage;

import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


/**class name : ServerUtilities.java
 * Description : utility class for calling server method calls.
 * @author Pooja
 */
public final class ServerUtilities {

   //initialising the parameters
	private static final int MAX_ATTEMPTS = 5; 
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    
    private static final Random random = new Random();

    /**Function name : register
     * Use : to register the device on the server     * 
     * @param context
     * @param regId
     * @return boolean
     */
    static boolean register(final Context context, final String regId) {
        Log.i(TAG, "registering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
       
        //trying to register for MAX_ATTEMPTS time
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                post(serverUrl, params);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.server_registered);
                
                //displaying the message after registration
                CommonUtilities.displayMessage(context, message);
                return true;
            } catch (IOException e) {
              
               //if unable to register
            	Log.e(TAG, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
               
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        
        //displaying the error message
        CommonUtilities.displayMessage(context, message);
        return false;
    }

    /**Function name : unregister
     * use : to unregister the device from the server 
     * @param context
     * @param regId
     */
    static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            
            //displaying the message after unregistration
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
           
            //if error occurs while unregistration process
        	String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
        	
        	//displaying the error message
            CommonUtilities.displayMessage(context, message);
        }
    }

    /**function name : post
     * @param endpoint
     * @param params
     * @throws IOException
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
        	
        	//in case of invalid URL, exception is thrown
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}
