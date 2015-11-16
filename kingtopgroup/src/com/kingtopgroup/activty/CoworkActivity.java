package com.kingtopgroup.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kingtopgroup.R;

public class CoworkActivity extends Activity implements OnClickListener{
	TextView tv1;
	TextView tv2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooperate);
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv1:
			intent = new Intent(CoworkActivity.this, FirmWorkActivity.class);
			break;
		case R.id.tv2:
			intent = new Intent(CoworkActivity.this, JoinActivity.class);
			break;
		}
		if(intent != null)
			this.startActivity(intent);
	}
}
