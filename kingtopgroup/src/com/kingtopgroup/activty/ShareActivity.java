package com.kingtopgroup.activty;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ShareActivity extends MainActionBarActivity {
	private static final String TAG = "ShareActivity";
	TextView tv_code;
	TextView tv_count;
	View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		titleButton.setText("我的分享");
		init();
	}

	void init() {
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_count = (TextView) findViewById(R.id.tv_count);
		progress = findViewById(R.id.progress);

		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		String uid = UserBean.getUSerBean().getUid();
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/ucenter/sharecode?uid=" + uid;
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.i(TAG, new String(arg2));
				if (arg0 == 200) {
					try {
						JSONObject object = new JSONObject(new String(arg2));
						String code = object.getString("ShareCode");
						String share_count = object.getString("ShareCount");
						tv_code.setText("您的分享码是：" + code);
						tv_count.setText("您已分享：" + share_count + "人");
						changeTextColor();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}

	void changeTextColor() {
		String text1 = tv_code.getText().toString();
		SpannableStringBuilder builder1 = new SpannableStringBuilder(text1);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);

		builder1.setSpan(blackSpan, 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder1.setSpan(redSpan, 7, text1.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		tv_code.setText(builder1);

		String text2 = tv_count.getText().toString();
		SpannableStringBuilder builder2 = new SpannableStringBuilder(text2);
		builder2.setSpan(new ForegroundColorSpan(Color.RED), 5,
				text2.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_count.setText(builder2);
		
		progress.setVisibility(View.GONE);
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
