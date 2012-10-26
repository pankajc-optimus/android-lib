/**
 * Demo code to explain sorting and grouping of objects
 * @author optimus
 */
package com.component.sortingandgroupingofobjects;

// Android Imports
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

// Third party imports
import com.component.guavademo.R;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;

// Java  imports
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * SortingGroupingOfObjects class explains how to use guava library to group
 * objects and also it provides a basic code to use comparators to sort objects
 */
public class SortingGroupingOfObjects extends Activity {
	static Data[] dataObjects;
	static Gson gson;
	static EditText groupname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Declare the array of objects of data class
		dataObjects = new Data[Constants.objectsCount];
		for (int i = 0; i < Constants.objectsCount; i++) {
			dataObjects[i] = new Data();
		}
		gson = new Gson();
		groupname = (EditText) findViewById(R.id.editText1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Initialize the array of objects of class data with the data provided in
	 * class constants
	 * 
	 * @param v
	 *            to execute this method on the click of a corresponding button
	 */
	public static void initializeObjects(View v) {
		try {
			for (int i = 0; i < dataObjects.length; i++) {
				dataObjects[i].setId(Constants.id[i]);
				dataObjects[i].setColor(Constants.color[i]);
				dataObjects[i].setDateSubmitted(getCurrentTimestamp());
				dataObjects[i].setDescription(Constants.description[i]);
				dataObjects[i].setType(Constants.type[i]);
				dataObjects[i].setVendor(Constants.vendor[i]);

			}

			// Parse the array of objects of class data to a string using gson
			// library
			String initializedList = gson.toJson(dataObjects);
			Log.i("Initialized list", initializedList);
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.i("Null pointer exception",
					"Null pointer exception in initializeObjects");
		}
	}

	/**
	 * Returns a timestamp having a delay of 5 seconds from the current
	 * timestamp
	 * 
	 * @return
	 */
	static Timestamp getCurrentTimestamp() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime());
	}

	/**
	 * Sorts the array of objects of class data in an ascending order based upon
	 * the value present in the field id
	 * 
	 * @param v
	 *            to execute this method when the corresponding button is
	 *            clicked
	 */
	public static void sortObjects(View v) {
		try {
			Log.i("Unsorted List:", gson.toJson(dataObjects));

			// Create a comparator which compares the objects based upon the
			// values of field id and sort this array of objects in an ascending
			// order using this comparator
			Collections.sort(Arrays.asList(dataObjects),
					new Comparator<Data>() {
						@Override
						public int compare(Data dateFirstObject,
								Data dateSecondObject) {
							// compare the two objects based upon the value of
							// field Id and
							// return values accordingly to sort objects in an
							// ascending
							// order
							if (dateFirstObject.getId() < dateSecondObject
									.getId()) {
								return 1;
							} else if (dateFirstObject.getId() > dateSecondObject
									.getId()) {
								return -1;
							} else {
								return 0;
							}
						}
					});

			Log.i("Sorted list based upon the latest timestamp",
					gson.toJson(dataObjects));
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.i("Null pointer exception",
					"Null pointer exception in sortObjects");
		}
	}

	/**
	 * Group the objects based upon the value present in the field description.
	 * 
	 * @param v
	 *            to execute this method on the click of corresponding
	 *            button
	 */
	public static void groupObjects(View v) {
		try {

			// Create a multimap derived from Guava library
			Multimap<String, Data> testMap = ArrayListMultimap.create();

			// Initialize the values in this map
			for (int i = 0; i < dataObjects.length; i++) {
				testMap.put(dataObjects[i].getDescription(), dataObjects[i]);
			}

			// Get a corresponding group of objects of class data as per the
			// value of key
			Log.i(groupname.getText().toString(),
					gson.toJson(testMap.get(groupname.getText().toString())));
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.i("Null pointer exception",
					"Null pointer exception in groupm objects");
		}
	}

}
