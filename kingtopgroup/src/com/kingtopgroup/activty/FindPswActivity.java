package com.kingtopgroup.activty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kingtopgroup.R;

public class FindPswActivity extends MainActionBarActivity {
	EditText et_phone;
	EditText et_psw;
	EditText et_confirm;
	EditText et_code;
	TextView tv_code;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpsw);
		titleButton.setText("找回密码");
		init();
	}

	void init() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_psw = (EditText) findViewById(R.id.et_psw);
		et_confirm = (EditText) findViewById(R.id.et_confirm);
		et_code = (EditText) findViewById(R.id.et_code);
		tv_code = (TextView) findViewById(R.id.tv_code);
	}

	public void getCode(View v) {
		tv_code.setBackgroundResource(R.drawable.textview_disable);
		tv_code.setEnabled(false);
		CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
			
			@Override
			public void onTick(long arg0) {
				tv_code.setText("获取验证码(" + arg0/1000 + ")");
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
