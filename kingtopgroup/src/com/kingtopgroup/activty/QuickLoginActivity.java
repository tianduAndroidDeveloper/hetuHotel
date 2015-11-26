package com.kingtopgroup.activty;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class QuickLoginActivity extends MainActionBarActivity {
	TextView tv_getcode;
	EditText et_username;
	EditText et_code;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quick_login);
		titleButton.setText("快捷登录");
		init();
	}

	void init() {
		tv_getcode = (TextView) findViewById(R.id.tv_getcode);
		et_username = (EditText) findViewById(R.id.username);
		et_code = (EditText) findViewById(R.id.password);
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	CountDownTimer countDownTimer;

	public void getCode(View v) {
		String mobile = et_username.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			toastMsg("请输入手机号码", 1);
			return;
		}
		final TextView tv_code = (TextView) v;
		tv_code.setBackgroundResource(R.drawable.gray_bg3);
		tv_code.setEnabled(false);
		countDownTimer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long arg0) {
				tv_code.setText("获取验证码(" + arg0 / 1000 + ")");
			}

			@Override
			public void onFinish() {
				tv_code.setText("获取验证码");
				tv_code.setBackgroundResource(R.drawable.red_bg3);

				tv_code.setEnabled(true);
			}
		};
		countDownTimer.start();
		String url = "http://kingtopgroup.com/api/account/GetVerifyMobile?mobile=" + mobile;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}
		});

	}

	public void login(View v) {
		String mobile = et_username.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			toastMsg("请输入手机号码", 1);
			return;
		}
		String code = et_code.getText().toString();
		if (TextUtils.isEmpty(code)) {
			toastMsg("请输入验证码", 1);
			return;
		}

		AsyncHttpClient client = new AsyncHttpClient();
		String post = "http://kingtopgroup.com/api/account/login?mobile="
				+ mobile + "&password=" + code + "";

		client.post(post, null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String date = new String(arg2);
						JSONObject obj = new JSONObject(date);
						String Password = obj.getString("Password");
						String Uid = obj.getString("Uid");
						if (Password.equals("vaild")) {
							Intent intens = new Intent(QuickLoginActivity.this,
									indexActivity.class);
							startActivity(intens);
							UserBean.getUSerBean().setUid(Uid);
						} else {
							toastMsg("用户名或密码错误", 1);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("服务器忙，请稍后再试", 1);

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
