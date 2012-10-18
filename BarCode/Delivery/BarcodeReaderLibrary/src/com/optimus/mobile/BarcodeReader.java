///////////////////////////////////////////////////////////////////////////////
//Application Name : Barcode Reader											 //

// Use			   : Read the Barcode and display its format and content     //
// Created by      : Pooja Srivastava										 //
///////////////////////////////////////////////////////////////////////////////

package com.optimus.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class name     : BarcodeReader.java
 * Description    : Activity class 
 * Responsibility : User Interface
 * 
 * Steps to use the barcode reader : 
 * 1. Launch the application
 * 2. Click on the 'scan'button
 * 2. camera will be initiated and a rectangle with a red horizontal line will be displayed
 * 3. bring the camera near the image of the barcode so that the picture of the barcode lies within the rectangle
 * 4. barcode will be scanned
 * 5. Format and contents of the barcode will be displayed in the textfields.
 * 6. To scan another barcode, repeat the process from step 2.
 * @author Pooja
 * */
public class BarcodeReader extends Activity implements OnClickListener {

	// Members
 
	private Button btn_scan;
	private TextView tvLabel;
	private TextView tvContent;

	public Intent resultIntent;

	// Called when activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_reader);

		// Initialise the UI elements
		tvLabel = (TextView) findViewById(R.id.tv_label);
		tvContent = (TextView) findViewById(R.id.tv_content);
		btn_scan = (Button) findViewById(R.id.btn_scan);

		// setting onclick listener to the button
		btn_scan.setOnClickListener(this);
	}

	// This function handles the onclick event on the button
	
	public void onClick(View view) {

		// calling the scanning function from BarcodeReaderIntentClass class
		BarcodeReaderIntentClass.initiateScan(this);
	}

	// This is the callback function of the activity
	@Override
	public void onActivityResult(int request, int result, Intent i) {

		// parsing the result
		BarcodeIntentResultClass scan = BarcodeReaderIntentClass
				.parseActivityResult(request, result, i);
		if (scan != null) {
			resultIntent = i;
			// setting the values on the text box

			tvLabel.setText(scan.getFormatName());
			tvContent.setText(scan.getContents());
		}
 
	}

	// This function is used to save the instance on orientation change
	@Override
	public void onSaveInstanceState(Bundle state) {
		state.putString("label", tvLabel.getText().toString());
		state.putString("contents", tvContent.getText().toString());
	}

	// This function is used to restore the original state on orientation change
	@Override
	public void onRestoreInstanceState(Bundle state) {
		tvLabel.setText(state.getString("label"));
		tvContent.setText(state.getString("contents"));
	}
}
