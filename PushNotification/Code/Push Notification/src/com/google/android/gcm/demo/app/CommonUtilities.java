package com.google.android.gcm.demo.app;

import android.content.Context;

import android.content.Intent;


/**class name : CommonUtilities.java
 * Description : utility class 
 * @author Pooja
 */
public final class CommonUtilities {

   
    //setting the server URL
	static final String SERVER_URL = "http://192.168.1.119:8080/gcm-demo/";

    //setting the sender Id which is the project Id.  
    static final String SENDER_ID = "701087149999"; 

    //Initialising the string "TAG"
    static final String TAG = "GCMDemo";

   //Initialising the string to be displayed while showing the message
    static final String DISPLAY_MESSAGE_ACTION =
            "com.google.android.gcm.demo.app.DISPLAY_MESSAGE";

     static final String EXTRA_MESSAGE = "message";

   
    //function to be called to display the message
     static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        
        //passing the data to the intent
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
