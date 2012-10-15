package com.optimus.atul.librarysms;

import org.json.JSONArray;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestSMS extends Activity implements OnClickListener {

	LibSMSUtils	smsUtils;
	TextView	tvSMSAvail, tvMMSAvail, tvShowContact;
	Button		buttonGetContact, buttonSendSMS, buttonSendMMS,
			buttonSelectPicture;
	EditText	etMessage, etPhone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lib_sms);
		// Initialize variables and references.
		initVar();

		// Calling check network service availability.(See log for output)
		if (smsUtils.checkSMSServiceAvailability(this)) {
			tvSMSAvail.setText(tvSMSAvail.getText()
					+ this.getString(R.string.labelAvailable));
		} else {
			tvSMSAvail.setText(tvSMSAvail.getText()
					+ this.getString(R.string.labelNotAvailable));
		}

		// Calling check network service availability.(See log for output)
		// if (smsUtils.checkMMSServiceAvailability(this)) {
		// tvMMSAvail.setText(tvMMSAvail.getText()
		// + this.getString(R.string.labelAvailable));
		// } else {
		// tvMMSAvail.setText(tvMMSAvail.getText()
		// + this.getString(R.string.labelNotAvailable));
		// }

	}

	private void initVar() {
		smsUtils = new LibSMSUtils();
		tvSMSAvail = (TextView) findViewById(R.id.tvSMSAvail);
		tvMMSAvail = (TextView) findViewById(R.id.tvMMSAvail);
		tvShowContact = (TextView) findViewById(R.id.tvShowContact);
		buttonGetContact = (Button) findViewById(R.id.buttonGetContact);
		buttonGetContact.setOnClickListener(this);
		buttonSendSMS = (Button) findViewById(R.id.buttonSendSMS);
		buttonSendSMS.setOnClickListener(this);
		etMessage = (EditText) findViewById(R.id.editText1);
		etPhone = (EditText) findViewById(R.id.editText2);
		buttonSendMMS = (Button) findViewById(R.id.buttonSendMMS);
		buttonSendMMS.setOnClickListener(this);
		buttonSelectPicture = (Button) findViewById(R.id.buttonSelectPicture);
		buttonSelectPicture.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.buttonGetContact:

				break;

			case R.id.buttonSendSMS:
				try {
					smsUtils.sendSMS(this, etMessage.getText().toString(),
							etPhone.getText().toString(), null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.buttonSelectPicture:
				startActivity(new Intent("BrowsePicture"));

				break;

			case R.id.buttonSendMMS:
				// smsUtils.sendMMSPicture(BrowsePicture.selectedImagePath,
				// etMessage
				// .getText().toString(), etPhone.getText().toString(), null);

				// smsUtils.deleteConversation(null, null);

				 JSONArray jArray =
				 smsUtils.getListOfConversations(TestSMS.this);
				 Log.d("Json received:: ", jArray.toString());

				// JSONArray jArray = smsUtils.getTextsfromId(TestSMS.this,
				// "2");
				// Log.d("Json received:: ", jArray.toString());

				// boolean isDeleted = smsUtils.deleteSMS(TestSMS.this, "4");
				// Log.d("IsDeleted", "" + isDeleted);

				// JSONArray jArray = smsUtils.getUnreadMessages(TestSMS.this);
				// Log.d("Json received:: ", jArray.toString());

				// JSONArray jArray = smsUtils.getContacts(this);
				// Log.d("Json received:: ", jArray.toString());
				// Log.d("Json length:: ",""+ jArray.length());

				// JSONArray jArray = smsUtils.getSMSLog(this, "1349882081589");
				// Log.d("Json received:: ", jArray.toString());

//				JSONArray jArray = smsUtils.getCallLog(this, null);
//				Log.d("Json received:: ", jArray.toString());
				break;
		}

	}

}
