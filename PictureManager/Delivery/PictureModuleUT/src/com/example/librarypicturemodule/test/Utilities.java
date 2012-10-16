/**
 * Contains various methods that will be used for local use by the test cases
 * @author optimus
 */
package com.example.librarypicturemodule.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Contains various methods that will be used for local use by the test cases
 */
public class Utilities {

	/**
	 * Copies the file with filename passed as an argument present in assets to
	 * the specified location
	 * 
	 * @param currentContext
	 *            context of the activity where assets are present
	 * 
	 * @param filePath
	 *            specified path where the file has to be saved
	 */
	public static void copyAssets(Context currentContext, String filePath,
			String fileName) {

		AssetManager assetManager = currentContext.getAssets();
		String[] files = null;

		try {
			files = assetManager.list("");
			// Search for the file in assets having the file name passed as an
			// argument
			for (String filename : files) {
				if (filename.equals(fileName)) {

					// If file with the given filename is present then copy it
					// to the given location passed as an argument
					InputStream in = null;
					OutputStream out = null;

					in = assetManager.open(filename);
					out = new FileOutputStream(filePath + File.separator
							+ filename);
					copyFile(in, out);

					// Clean up work
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Copy the data present in input stream to a output stream
	 * 
	 * @param in
	 *            InputStream from where the data has to be read in
	 * @param out
	 *            OutputStream where data has to be written
	 * @throws IOException
	 */
	public static void copyFile(InputStream in, OutputStream out)
			throws IOException {

		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}

	}

}
