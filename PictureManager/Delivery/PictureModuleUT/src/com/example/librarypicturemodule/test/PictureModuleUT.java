/**
 * Unit test cases for picture library
 * @author Optimus
 */
package com.example.librarypicturemodule.test;

// Android Imports
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.test.AndroidTestCase;

// Third party imports
import com.example.picturemodule.PictureModule;

// Java imports
import java.io.File;

/**
 * PictureModuleUT contains unit test cases for picture library
 */
public class PictureModuleUT extends AndroidTestCase {
	Context mTestAppContext;
	String filePath;

	/**
	 * (Initialize the variables needed before running a test case)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mTestAppContext = getContext().createPackageContext(
				"com.example.picturemodule.test",
				Context.CONTEXT_IGNORE_SECURITY);
		filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				.toString()
				+ File.separator + "Pictures";
	}

	/**
	 * (Do all the clean up work after each test case has finish its execution)
	 * 
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		File file = new File(filePath + File.separator + "Chi.jpg");
		if (file.exists()) {
			file.delete();
		}
		file = new File(filePath + File.separator + "g.jpg");
		if (file.exists()) {
			file.delete();
		}
		file = new File(filePath + File.separator + "m.jpg");
		if (file.exists()) {
			file.delete();
		}
		file = new File(filePath + File.separator + "new.jpg");
		if(file.exists()){
			file.delete();
		}
	}

	/**
	 * Unit test case which tests whether rotateImage method of Picture Library
	 * does proper handling on a null input
	 */
	public void testRotateImageNullInputProperHandling() {
		// Prepare the input
		Bitmap rotatedBitmap = PictureModule.rotateImage(90, null);

		// Compare the expected result with actual and throw an exception
		assertEquals(null, rotatedBitmap);

	}

	/**
	 * Unit test case which tests whether the image is compressed correctly when
	 * compressImage method of Picture Library has provided a positive input
	 */
	public void testCompressImagePositiveInputProperCompression() {
		// Prepare the input
		Utilities.copyAssets(mTestAppContext, filePath, "g.jpg");
		Bitmap sourceBitmap = PictureModule.compressImage(4, filePath
				+ File.separator + "g.jpg");
		PictureModule.saveImage(sourceBitmap, filePath + File.separator
				+ "g.jpg");

		// Prepare the actual result
		File f = new File(filePath + File.separator + "g.jpg");
		int actualSize = (int) (f.length() / 1024);

		// Prepare the expected result
		int expectedSize = 12;

		// Compare the expected result with actual and throw an exception
		assertEquals(expectedSize, actualSize);
	}

	/**
	 * Unit test case which tests CompressImage method of Picture Library for
	 * OutOfBoubndary condition.
	 */
	public void testCompressImageOutOfBoundaryInputProperHandling() {
		// Prepare the input
		Utilities.copyAssets(mTestAppContext, filePath, "m.jpg");
		Bitmap sourceBitmap = PictureModule.compressImage(2, filePath
				+ File.separator + "m.jpg");
		PictureModule.saveImage(sourceBitmap, filePath + File.separator
				+ "m.jpg");

		// Prepare the actual result
		File f = new File(filePath + File.separator + "m.jpg");
		int actualSize = (int) (f.length() / (1024 * 1024));

		// Prepare the expected result
		int expectedSize = 0;

		// Compare the expected result with actual and throw an exception
		assertEquals(expectedSize, actualSize);
	}

	/**
	 * Unit test case which checks whether compressImage method of Picture
	 * Library does proper handling on null input for file path
	 */
	public void testCompressImageNullInputForPathProperHandling() {
		// Prepare the input
		Bitmap sourceBitmap = PictureModule.compressImage(2, null);
		Utilities.copyAssets(mTestAppContext, filePath, "g.jpg");

		// Compare the expected result with actual and throw an exception
		assertEquals(null, sourceBitmap);
	}

	/**
	 * Unit test case which checks whether saveImage method of Picture Library
	 * saves file correctly on positive input
	 */
	public void testSaveImagePositiveInputSavesFile() {
		// Prepare the input
		Utilities.copyAssets(mTestAppContext, filePath, "Chi.jpg");
		Bitmap sourceBitmap = PictureModule.compressImage(1, filePath
				+ File.separator + "Chi.jpg");
		PictureModule.saveImage(sourceBitmap, filePath + File.separator
				+ "new.jpg");

		// Prepare the actual result
		File file = new File(filePath + File.separator + "new.jpg");
		boolean actualResult = false;
		if (file.exists()) {
			actualResult = true;
		}

		// Compare the expected result with actual and throw an exception
		assertEquals(true, actualResult);

	}

	/**
	 * Unit test case which checks whether saveImage method of Picture Library
	 * does proper handling on null input for bitmap
	 */
	public void testSaveImageNullInputForBitmapProperHandling() {
		// Prepare the input and actual result
		Boolean actualResult = PictureModule.saveImage(null, filePath
				+ File.separator + "m.jpg");

		// Compare the expected result with actual and throw an exception
		assertEquals(null, actualResult);

	}

	/**
	 * Unit test case which checks whether saveImage method of Picture Library
	 * does proper handling on null input for file path
	 */
	public void testSaveImageNullInputForFilePathProperHandling() {
		// Prepare the input
		Utilities.copyAssets(mTestAppContext, filePath, "g.jpg");
		Bitmap sourceBitmap = PictureModule.compressImage(4, filePath
				+ File.separator + "g.jpg");

		// Prepare the actual result
		Boolean actualResult = PictureModule.saveImage(sourceBitmap, null);

		// Compare the expected result with actual and throw an exception
		assertEquals(null, actualResult);

	}
}
