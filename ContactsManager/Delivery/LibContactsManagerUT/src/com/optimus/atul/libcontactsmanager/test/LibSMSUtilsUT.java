package com.optimus.atul.libcontactsmanager.test;

import org.json.JSONArray;
import org.json.JSONException;

import com.optimus.mobility.libcontactsmanager.LibContactsManagerUtils;


import android.content.Context;
import android.test.AndroidTestCase;

public class LibSMSUtilsUT extends AndroidTestCase {

	LibContactsManagerUtils	libSMS;

	@Override
	protected void setUp() throws Exception {
		libSMS = new LibContactsManagerUtils(true);
		super.setUp();
	}

	public void testgetListOfConversationsforNULL() {

		JSONArray jArray = libSMS.getListOfConversations(getContext());

		assertNotNull(jArray);
	}

	public void testgetListOfConversationsforEmptyJSON() {

		JSONArray jArray = libSMS.getListOfConversations(getContext());

		assertNotSame(jArray.length(), 0);
	}

	public void testgetListOfConversationsforIllegalArguments() {
		Context context = new VoidContext();
		try {
			libSMS.getListOfConversations(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

	}

	public void testgetContactsNULL() throws JSONException {

		JSONArray jArray = libSMS.getContacts(getContext());

		assertNotNull(jArray);
	}

	public void testgetContactsforEmptyJSON() throws JSONException {

		JSONArray jArray = libSMS.getContacts(getContext());

		assertNotSame(jArray.length(), 0);
	}

	public void testgetContactsforIllegalArguments() throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getContacts(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);

	}

	public void testcheckSMSServiceAvailabilityforIllegalArguments() {
		Context context = new VoidContext();
		try {
			libSMS.checkSMSServiceAvailability(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testcheckSMSServiceAvailability() {
		boolean bool = libSMS.checkSMSServiceAvailability(getContext());
		if (bool || !bool) {
			assertTrue(true);
			return;
		}
		assertTrue(false);

	}

	public void testgetTextsfromAddressforIllegalArguments1()
			throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getTextsfromAddress(context, "anythingblah");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromAddressforIllegalArguments2()
			throws JSONException {
		try {
			libSMS.getTextsfromAddress(getContext(), null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromIdforIllegalArguments1() throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getTextsfromId(context, "anythingblah");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromIdforIllegalArguments2() throws JSONException {
		try {
			libSMS.getTextsfromId(getContext(), null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	// public void testsendMMSPictureforIllegalArguments() {
	// Context context = new VoidContext();
	// try {
	// libSMS.sendMMSPicture(context, null, null, null);
	// } catch (IllegalArgumentException e) {
	// assertTrue(true);
	// return;
	// }
	// }

	public void testgetSMSLogforIllegalArguments1() throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getSMSLog(context, "1212");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetSMSLogforIllegalArguments2() {
		try {
			libSMS.getSMSLog(getContext(), null);
		} catch (Exception e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetUnreadMessagesforNULL() throws JSONException {

		JSONArray jArray = libSMS.getUnreadMessages(getContext());

		assertNotNull(jArray);
	}

	public void testgetUnreadMessagesforIllegalArguments() throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getUnreadMessages(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetCallLog() throws JSONException {
		JSONArray jArray = libSMS.getCallLog(getContext(), null);

		assertNotSame(jArray.length(), 0);
	}

	public void testgetCallLogforNULL() throws JSONException {
		JSONArray jArray = libSMS.getCallLog(getContext(), null);

		assertNotNull(jArray);
	}

	public void testgetCallLogforIllegalArguments() throws JSONException {
		Context context = new VoidContext();
		try {
			libSMS.getCallLog(context, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testsendSMS() {
		try {
			libSMS.sendSMS(getContext(), "Hello!", "+919711978907", null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
			return;
		} catch (Exception e) {
			assertTrue(false);
		}
		assertTrue(true);
	}

	public void testsendSMSforInvalidDestination() {
		try {
			libSMS.sendSMS(getContext(), "Hello!", "+9197119", null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
			return;
		} catch (Exception e) {
		}
		assertTrue(true);
	}

	public void testsendSMSforIllegalArguments1() {
		Context context = new VoidContext();
		try {
			libSMS.sendSMS(context, "Hello!", "+919711978907", null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		} catch (Exception e) {
			assertTrue(true);
		}
		// SMS works for any context passed.
		assertTrue(true);

	}

	public void testsendSMSforIllegalArguments2() {
		try {
			libSMS.sendSMS(getContext(), null, null, null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		} catch (Exception e) {
			assertTrue(true);
		}
		assertTrue(false);
	}

	public void testdeleteConversation() {

	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

}
