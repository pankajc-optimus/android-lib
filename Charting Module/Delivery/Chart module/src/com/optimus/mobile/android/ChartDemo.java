package com.optimus.mobile.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * class name : ChartDemo This class is responsible for getting data for the
 * charts from the user
 * 
 * @author Pooja
 * 
 */
public class ChartDemo extends Activity implements OnClickListener {

	// Declaring the UI elements

	private EditText et_1_q1;
	private EditText et_1_q2;
	private EditText et_1_q3;
	private EditText et_2_q1;
	private EditText et_2_q2;
	private EditText et_2_q3;

	/**
	 * Declaring the value of the two series : a1: 1st value of 1st series a2:
	 * 2nd value of 1st series a3: 3rd value of 1st series b1: 1st value of 2nd
	 * series b2: 2nd value of 2nd series b3: 3rd value of 2nd series
	 */
	private int a1, a2, a3, b1, b2, b3;
	private Button bar, line, scatter, pie;
	private String btn_pressed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * setting window layout with no title bar
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.chart_demo);

		// defining the UI elements
		et_1_q1 = (EditText) findViewById(R.id.editText1);
		et_1_q2 = (EditText) findViewById(R.id.editText2);
		et_1_q3 = (EditText) findViewById(R.id.editText3);
		et_2_q1 = (EditText) findViewById(R.id.editText4);
		et_2_q2 = (EditText) findViewById(R.id.editText5);
		et_2_q3 = (EditText) findViewById(R.id.editText6);
		bar = (Button) findViewById(R.id.button1);
		bar.setOnClickListener(this);
		line = (Button) findViewById(R.id.button2);
		line.setOnClickListener(this);
		scatter = (Button) findViewById(R.id.button3);
		scatter.setOnClickListener(this);
		pie = (Button) findViewById(R.id.button4);
		pie.setOnClickListener(this);
	}

	/**
	 * on clicking the button, set the value of string btn_pressed to the button
	 * name which has been pressed. Then Retrieve the values of the edit text
	 * boxes and store them in the variables. Store the value of each series in
	 * the respective array of integers Then pass the values of the arrays and
	 * btn_pressed to the next activity which will display the charts
	 * 
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button1) {
			btn_pressed = "bar";
		}
		if (view.getId() == R.id.button2) {
			btn_pressed = "line";
		}
		if (view.getId() == R.id.button3) {
			btn_pressed = "scatter";
		}
		if (view.getId() == R.id.button4) {
			btn_pressed = "pie";
		}
		
		//get the values entered by the user in the text boxes
		a1 = Integer.parseInt(et_1_q1.getText().toString());
		a2 = Integer.parseInt(et_1_q2.getText().toString());
		a3 = Integer.parseInt(et_1_q3.getText().toString());
		b1 = Integer.parseInt(et_2_q1.getText().toString());
		b2 = Integer.parseInt(et_2_q2.getText().toString());
		b3 = Integer.parseInt(et_2_q3.getText().toString());
		int series1[] = new int[3];
		int series2[] = new int[3];
		series1[0] = a1;
		series1[1] = a2;
		series1[2] = a3;
		series2[0] = b1;
		series2[1] = b2;
		series2[2] = b3;
		Intent intent = new Intent("com.optimus.mobile.android.ShowChart");
		
		//passing the values to the intent
		intent.putExtra("year1", series1);
		intent.putExtra("year2", series2);
		intent.putExtra("chart", btn_pressed);

		/**
		 * start the next activity which will display the charts
		 */
		startActivity(intent);

	}

}