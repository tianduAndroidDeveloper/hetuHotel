package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AgreementActivity extends MainActionBarActivity {
	TextView tv;
	View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		
		titleButton.setText("服务协议");
		
		tv = (TextView) findViewById(R.id.tv);
		progress = findViewById(R.id.progress);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/getserviceprotocol";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject object = new JSONObject(new String(arg2));
					String html = object.getString("Context");
					tv.setText(Html.fromHtml(html));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(AgreementActivity.this, "信息获取失败，请重试！",
						Toast.LENGTH_SHORT).show();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rightButtonClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
}
