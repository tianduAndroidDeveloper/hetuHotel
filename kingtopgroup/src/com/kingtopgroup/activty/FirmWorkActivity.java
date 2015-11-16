package com.kingtopgroup.activty;

import com.kingtopgroup.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

public class FirmWorkActivity extends MainActionBarActivity {
	
	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firmcowork);
		titleButton.setText("企业合作");
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
