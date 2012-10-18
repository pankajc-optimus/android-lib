package com.optimus.mobile;

/**Class name : BarcodeIntentResultClass.java
 * Encapsulates the result of a barcode scan
 * @author Pooja
 *
 */


public class BarcodeIntentResultClass {
	private final String contents;
	private final String formatName;

	BarcodeIntentResultClass(String contents, String formatName) {
		this.contents = contents;
		this.formatName = formatName;
	}

	/**
	 * @return raw content of barcode
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @return name of format, like "QR_CODE", "UPC_A".
	 */
	public String getFormatName() {
		return formatName;
	}

}
