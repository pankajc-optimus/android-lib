/**
 * 
 */
package com.optimus.mobile.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.optimus.mobile.BarcodeReader;
import com.optimus.mobile.R;

/**Test class for BarcodeReader
 * Class name  : ScanTest.java
 * use         : check whether the actual details and expected details of barcode are same.
 * @author Pooja
 * 
 */
public class ScanTest extends ActivityInstrumentationTestCase2<BarcodeReader> {
	
	//setting the expected details
	private static final String CONTENT = "8904064472013";
	private static final String FORMAT = "EAN_13";
	
	private TextView tv_format;
	private TextView tv_content;;
	private BarcodeReader mainActivity;

	//Initialising the constructor
	public ScanTest() {
		super("com.optimus.mobile", BarcodeReader.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		//getting the current activity
		mainActivity = getActivity();
		
		//initialising the UI elements
		tv_format = (TextView) mainActivity.findViewById(R.id.tv_label);
		tv_content = (TextView) mainActivity.findViewById(R.id.tv_content);
	}

	
	/**Test Function
	 * name    : testInitiateScan
	 * Result  : test should pass if actual and expected details are same
	 *  @author Pooja
	 */
	public void testInitiateScan() {

		//calling the click button function
		sendKeys("ENTER");

		//wait while the scanning process is finished
		while (mainActivity.resultIntent == null) {
			Log.i("RESULT", "WAITING FOR RESULT");
		}

		//getting the actual details
		String actual_format = tv_format.getText().toString();
		String actual_content = tv_content.getText().toString();
		
		//comparing the actual details with the expected details
		assertEquals(CONTENT, actual_content);
		assertEquals(FORMAT, actual_format);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
