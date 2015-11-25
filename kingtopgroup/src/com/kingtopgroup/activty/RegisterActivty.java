package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivty extends MainActionBarActivity {
	private static final String TAG = "RegisterActivty";
	EditText et_account;
	EditText et_psw;
	EditText et_confirm;
	EditText et_code;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		titleButton.setText("注册");
		init();
	}

	public void back(View v) {
		finish();
	}

	public void register(View v) {
		String account = et_account.getText().toString();
		String psw = et_psw.getText().toString();
		String confirm = et_confirm.getText().toString();
		String code = et_code.getText().toString();
		if (TextUtils.isEmpty(account)) {
			toastMsg("请输入手机号码", 0);
			return;
		}
		if (TextUtils.isEmpty(psw)) {
			toastMsg("请输入密码", 0);
			return;
		}
		if (confirm.isEmpty()) {
			toastMsg("请确认密码", 0);
			return;
		}
		if (!confirm.equals(psw)) {
			toastMsg("两次输入的密码不一致", 0);
			return;
		}
		if (code.isEmpty()) {
			toastMsg("请输入验证码", 0);
			return;
		}
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/account/register?mobile=" + account + "&password=" + psw + "&ip=" + "127.1.1.0" + "&verifyCode=" + code;
		client.post(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject obj = new JSONObject(new String(arg2));
						String msg = obj.getString("Message");
						toastMsg(msg, 1);
						String uid = String.valueOf(obj.optInt("Uid"));
						if (!uid.equals("0")) {
							UserBean.getUSerBean().setUid(uid);
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				toastMsg("请求失败，请重试", 1);

			}
		});
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	CountDownTimer countDownTimer;

	public void getCode(View v) {
		String mobile = et_account.getText().toString();
		if (TextUtils.isEmpty(mobile)) {
			toastMsg("请输入手机号码", 1);
			return;
		}
		final TextView tv_code = (TextView) v;
		tv_code.setBackgroundResource(R.drawable.textview_disable);
		tv_code.setEnabled(false);
		countDownTimer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long arg0) {
				tv_code.setText("获取验证码(" + arg0 / 1000 + ")");
			}

			@Override
			public void onFinish() {
				tv_code.setText("获取验证码");
				tv_code.setBackgroundResource(R.drawable.textview_red_bg);

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

	void init() {
		et_account = (EditText) findViewById(R.id.et_account);
		et_psw = (EditText) findViewById(R.id.et_psw);
		et_confirm = (EditText) findViewById(R.id.et_confirm);
		et_code = (EditText) findViewById(R.id.et_code);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
		}
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
