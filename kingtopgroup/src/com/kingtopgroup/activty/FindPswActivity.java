package com.kingtopgroup.activty;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.utils.RegxUtils;
import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FindPswActivity extends MainActionBarActivity {
	private static final String TAG = "FindPswActivity";
	EditText et_phone;
	EditText et_psw;
	EditText et_confirm;
	EditText et_code;
	TextView tv_code;
	View progress;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpsw);
		titleButton.setText("忘记密码");
		init();
	}

	void init() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_psw = (EditText) findViewById(R.id.et_psw);
		et_confirm = (EditText) findViewById(R.id.et_confirm);
		et_code = (EditText) findViewById(R.id.et_code);
		tv_code = (TextView) findViewById(R.id.tv_code);
		progress = findViewById(R.id.progress);
	}

	public void getCode(View v) {
		tv_code.setBackgroundResource(R.drawable.textview_disable);
		tv_code.setEnabled(false);
		requestCode();
		CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long arg0) {
				tv_code.setText("获取验证码(" + arg0 / 1000 + ")");
			}

			@Override
			public void onFinish() {
				tv_code.setText("获取验证码");
				tv_code.setBackgroundResource(R.drawable.red_bg);
				tv_code.setEnabled(true);
			}
		};
		countDownTimer.start();
	}

	void requestCode() {
		String phone = et_phone.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			toastMsg("请输入手机号码", 1);
			return;
		}
		String url = getString(R.string.url_getcode) + phone;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				toastMsg("获取验证码失败，请重试", 1);
			}
		});
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	public void find(View v) {
		String phone = et_phone.getText().toString();
		String code = et_code.getText().toString();
		String psw = et_psw.getText().toString();
		String confirm = et_confirm.getText().toString();
		if (phone.isEmpty()) {
			toastMsg("请输入手机号码", 1);
			return;
		}
		if (!RegxUtils.isPhone(phone)) {
			toastMsg("请输入正确的手机号码", 1);
			return;
		}
		if (code.isEmpty()) {
			toastMsg("请输入验证码", 1);
			return;
		}
		if (psw.isEmpty()) {
			toastMsg("请输入密码", 1);
			return;
		}
		if (confirm.isEmpty()) {
			toastMsg("请确认密码", 1);
			return;
		}
		if (!confirm.equals(psw)) {
			toastMsg("两次输入的密码不一致", 1);
			return;
		}
		progress.setVisibility(View.VISIBLE);
		// mobile=&verifyCode=&newpwd=&confirmpwd=
		String url = getString(R.string.url_findpsw) + "?mobile=" + phone + "&verifycode=" + code + "&newpwd=" + psw + "&confirmpwd=" + confirm;
		Log.i(TAG, url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject object = new JSONObject(new String(arg2));
					String msg = object.optString("ActionMessage");
					int returnValue = object.optInt("ReturnValue");
					toastMsg(msg, 1);
					if (returnValue == 1)
						finish();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				toastMsg("请求失败，请重试", 1);
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
