/**
 * @author optimus
 */
package com.component.sortingandgroupingofobjects;

import java.sql.Timestamp;

/**
 * Data class denotes the class on whose array of objects operations like
 * sorting and grouping will be performed
 */
public class Data {

	// Declare the variables
	int id;
	String color;
	String type;
	Timestamp dateSubmitted;
	String description;
	String vendor;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dateSubmitted
	 */
	public Timestamp getDateSubmitted() {
		return dateSubmitted;
	}

	/**
	 * @param dateSubmitted
	 *            the dateSubmitted to set
	 */
	public void setDateSubmitted(Timestamp dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}
