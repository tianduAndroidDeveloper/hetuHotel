package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CommonActivity extends MainActionBarActivity {
	TextView tv;
	View progress;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		titleButton.setText("常见问题");
		init();
	}

	void init() {
		tv = (TextView) findViewById(R.id.tv);
		progress = findViewById(R.id.progress);
	}
	
	void requestData() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/GetServiceChangJianWenTi";
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
				Toast.makeText(CommonActivity.this, "信息获取失败，请重试！",
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
