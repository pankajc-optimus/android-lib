package com.optimus.mobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.FacebookError;
import com.optimus.mobile.oauth.facebook.Constants;
import com.optimus.mobile.oauth.facebook.FacebookRequestListener;
import com.optimus.mobile.oauth.facebook.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GraphInterfaceDialog extends Dialog {

	private EditText mObjectId;
	private TextView mGraphOutput;
	private Button mSubmitButton;
	private Activity activity;
	private Handler mHandler;
	private ProgressDialog dialog;

	private AsyncFacebookRunner mAsyncRunner;

	public GraphInterfaceDialog(Activity activity, AsyncFacebookRunner runner) {
		super(activity);
		this.activity = activity;
		setTitle(R.string.title_graphAPi);
		mAsyncRunner = runner;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHandler = new Handler();

		setContentView(R.layout.graph_api);
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.FILL_PARENT;
		params.height = LayoutParams.FILL_PARENT;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

		mObjectId = (EditText) findViewById(R.id.etGraphObject);
		mObjectId.setText(Constants.REQUEST_ME);
		mGraphOutput = (TextView) findViewById(R.id.tvOutput);
		mSubmitButton = (Button) findViewById(R.id.btnExecuteQuery);

		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(mObjectId.getWindowToken(), 0);
				dialog = ProgressDialog.show(
						GraphInterfaceDialog.this.activity, "",
						"Loading Graph Response..", true, true);
				Utility.executeGraphAPIRequest(mAsyncRunner, mObjectId
						.getText().toString(), "1", new GraphRequestListener());
			}
		});
	}

	public class GraphRequestListener extends FacebookRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			dialog.dismiss();
			/*
			 * Output can be a JSONArray or a JSONObject. Try JSONArray and if
			 * there's a JSONException, parse to JSONObject
			 */
			try {
				JSONArray json = new JSONArray(response);
				setText(json.toString(2));
			} catch (JSONException e) {
				try {
					/*
					 * JSONObject probably indicates there was some error
					 * Display that error, but for end user you should parse the
					 * error and show appropriate message
					 */
					JSONObject json = new JSONObject(response);
					setText(json.toString(2));
				} catch (JSONException e1) {
					setText(e1.getMessage());
				}
			}
		}

		public void onFacebookError(FacebookError error) {
			dialog.dismiss();
			setText(error.getMessage());
		}
	}

	public void setText(final String txt) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mGraphOutput.setText(txt);
				mGraphOutput.setVisibility(View.VISIBLE);
			}
		});
	}
}
