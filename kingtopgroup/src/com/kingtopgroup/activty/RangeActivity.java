package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class RangeActivity extends MainActionBarActivity {
	WebView wv;
	View progress;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_range);
		titleButton.setText("∑˛ŒÒ∑∂Œß");
		init();
	}

	void init() {
		wv = (WebView) findViewById(R.id.wv);
		progress = findViewById(R.id.progress);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/GetServiceRange";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject object = new JSONObject(new String(arg2));
					String html = object.getString("Context");
					wv.getSettings().setJavaScriptEnabled(true);
					wv.loadDataWithBaseURL("http://kingtopgroup.com/", html,
							"text/html", "utf-8", null);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(RangeActivity.this, "«Î«Û ß∞‹£¨«Î÷ÿ ‘", Toast.LENGTH_LONG).show();
				progress.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {

	}

	@Override
	public void rightButtonClick(View v) {

	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

}
