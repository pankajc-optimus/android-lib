/**
 * Library for performing operations on a image
 * @author Optimus
 */
package com.example.picturemodule;

// Android imports
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

// Java imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * LibraryPictureModule class is a library which contains methods for various
 * operations on a image
 */
public class PictureModule extends Activity {

	/**
	 * Rotates the image to the degrees passed as an argument and returns the
	 * bitmap of this rotated image
	 * 
	 * @param degrees
	 *            The degrees to which the image has to be rotated
	 * 
	 * @param filePath
	 *            The path of the image file which has to rotated
	 * 
	 * @return bitmap of the rotated image
	 */
	public static Bitmap rotateImage(int degrees, Bitmap sourceBitmap) {
		try {
			if (sourceBitmap != null && degrees != 0) {
				// Create a pre rotated matrix and rotate the bitmap using it
				Matrix matrix = new Matrix();
				matrix.preRotate(degrees);
				Bitmap resizedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
						sourceBitmap.getWidth(), sourceBitmap.getHeight(),
						matrix, true);
				return resizedBitmap;
			}
			return null;
		} catch (NullPointerException e) {
			// Handling for null pointer exception
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			// Handling for outofmemory exception
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Saves the bitmap as a image file to a path passed as an argument
	 * 
	 * @param image
	 *            The bitmap to be saved
	 * @param filePath
	 *            The path where this bitmap has to saved
	 * @return true if the image has been saved successfully otherwise returns
	 *         null
	 */
	public static Boolean saveImage(Bitmap image, String filePath) {
		try {
			// Create a fileoutput stream and save bitmap to it.
			FileOutputStream out = new FileOutputStream(filePath);
			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
			return true;
		} catch (NullPointerException e) {
			// Handling for null pointer exception
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// Handling for file not found exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Compress the image to the scale passed as an argument
	 * 
	 * @param scale
	 *            The scale to which the compression has to be done
	 * @param filePath
	 *            The path of the file which has to be compressed
	 * @return bitmap of the compressed image otherwise it returns false
	 */
	public static Bitmap compressImage(int scale, String filePath) {
		try {
			// Create a bitmap scaled down using a scale value passed as an
			// argument
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(filePath),
					null, o);
		} catch (FileNotFoundException e) {
			// Handling for filenotfound exception
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			// Handling for nullpointer exception
			e.printStackTrace();
			return null;
		} catch (NumberFormatException e) {
			// Handling for numberformatexception
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			try {
				// Handling for outofmemory exception
				File f = new File(filePath);

				// In the case of outofmemory exception we need to scale down
				// the
				// image below 1mb.
				Double fileSize = (double) f.length() / (double) 1048576;
				if (fileSize > 1) {
					// If the size of image is greater then 1mb set a particular
					// scale value
					// to scale down the image below 1mb
					scale = (int) Math.ceil(fileSize);
				} else {
					scale = 1;
				}

				// Create and return scaled bitmap
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				try {
					return BitmapFactory.decodeStream(new FileInputStream(
							filePath), null, o2);
				} catch (FileNotFoundException e1) {
					// Handling for filenotfound exception
					e1.printStackTrace();
					return null;
				}
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				Log.i("Exception Message",
						"Your device does not have free 1 MB native memory");
				return null;
			}
		}
	}
}