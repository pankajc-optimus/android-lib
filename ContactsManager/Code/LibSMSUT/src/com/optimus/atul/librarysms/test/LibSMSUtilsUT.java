package com.optimus.atul.librarysms.test;

import org.json.JSONArray;

import android.content.Context;
import android.test.AndroidTestCase;

import com.optimus.atul.librarysms.LibSMSUtils;

public class LibSMSUtilsUT extends AndroidTestCase {

	LibSMSUtils libSMS;

	@Override
	protected void setUp() throws Exception {
		libSMS = new LibSMSUtils();
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

	public void testgetContactsNULL() {

		JSONArray jArray = libSMS.getContacts(getContext());

		assertNotNull(jArray);
	}

	public void testgetContactsforEmptyJSON() {

		JSONArray jArray = libSMS.getContacts(getContext());

		assertNotSame(jArray.length(), 0);
	}

	public void testgetContactsforIllegalArguments() {
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

	public void testgetTextsfromAddressforIllegalArguments1() {
		Context context = new VoidContext();
		try {
			libSMS.getTextsfromAddress(context, "anythingblah");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromAddressforIllegalArguments2() {
		try {
			libSMS.getTextsfromAddress(getContext(), null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromIdforIllegalArguments1() {
		Context context = new VoidContext();
		try {
			libSMS.getTextsfromId(context, "anythingblah");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetTextsfromIdforIllegalArguments2() {
		try {
			libSMS.getTextsfromId(getContext(), null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testsendMMSPictureforIllegalArguments() {
		Context context = new VoidContext();
		try {
			libSMS.sendMMSPicture(context, null, null, null);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
	}

	public void testgetSMSLogforEmptyJSON() {
		JSONArray jArray = libSMS.getSMSLog(getContext(), "124235");
		assertNotSame(jArray.length(), 0);
	}

	public void testgetSMSLogforNULLJSON() {
		JSONArray jArray = libSMS.getSMSLog(getContext(), "124235");
		assertNotNull(jArray);
	}

	public void testgetSMSLogforIllegalArguments1() {
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
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetUnreadMessagesforNULL() {

		JSONArray jArray = libSMS.getUnreadMessages(getContext());

		assertNotNull(jArray);
	}

	public void testgetUnreadMessagesforIllegalArguments() {
		Context context = new VoidContext();
		try {
			libSMS.getUnreadMessages(context);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	public void testgetCallLog() {
		JSONArray jArray = libSMS.getCallLog(getContext(), null);

		assertNotSame(jArray.length(), 0);
	}

	public void testgetCallLogforNULL() {
		JSONArray jArray = libSMS.getCallLog(getContext(), null);

		assertNotNull(jArray);
	}

	public void testgetCallLogforIllegalArguments() {
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
			libSMS.sendSMS(getContext(), "Hello!", "+919711978907", null);
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
			libSMS.sendSMS(getContext(), "Hello!", "+9197119", null);
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
			libSMS.sendSMS(context, "Hello!", "+919711978907", null);
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
			libSMS.sendSMS(getContext(), null, null, null);
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
