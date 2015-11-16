package com.kingtopgroup.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kingtopgroup.R;

public class MoreActivity extends Activity implements OnClickListener {
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		init();
	}

	void init() {
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);

		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv1:
			intent = new Intent(this, AgreementActivity.class);
			break;
		case R.id.tv3:
			intent = new Intent(this, CommonActivity.class);
			break;
		case R.id.tv4:
			intent = new Intent(this, AboutUSActivity.class);
			break;
		}

		if (intent != null)
			this.startActivity(intent);
	}
}
