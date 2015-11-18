package com.kingtopgroup.activty;

import org.apache.http.Header;

import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivty extends MainActionBarActivity {
	private static final String TAG = "RegisterActivty";
	EditText et_account;
	EditText et_psw;
	EditText et_confirm;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		titleButton.setText("зЂВс");
		init();
	}

	public void back(View v) {
		finish();
	}

	public void register(View v) {
		String account = et_account.getText().toString();
		String psw = et_psw.getText().toString();
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/account/register";
		RequestParams params = new RequestParams();
		params.put("mobile", account);
		params.put("password", psw);
		params.put("ip", "192.168.1.110");
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.i(TAG, new String(arg2));
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.i(TAG, new String(arg2));
				
			}
		});
	}

	void init() {
		et_account = (EditText) findViewById(R.id.et_account);
		et_psw = (EditText) findViewById(R.id.et_psw);
		et_confirm = (EditText) findViewById(R.id.et_confirm);
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
