package com.kingtopgroup.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;

public class PersonalActivity extends Activity implements OnClickListener {
	TextView tv_order;
	TextView tv_prefer;
	TextView tv_member;
	TextView tv_address;
	TextView tv_aboutus;
	TextView tv_agreement;
	TextView tv_share;
	Button btn_call;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		init();
	}

	void init() {
		tv_order = (TextView) findViewById(R.id.tv_order);
		tv_prefer = (TextView) findViewById(R.id.tv_prefer);
		tv_member = (TextView) findViewById(R.id.tv_member);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_aboutus = (TextView) findViewById(R.id.tv_aboutus);
		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		tv_share = (TextView) findViewById(R.id.tv_share);
		btn_call = (Button) findViewById(R.id.btn_call);

		tv_order.setOnClickListener(this);
		tv_prefer.setOnClickListener(this);
		tv_member.setOnClickListener(this);
		tv_address.setOnClickListener(this);
		tv_aboutus.setOnClickListener(this);
		tv_share.setOnClickListener(this);
		tv_agreement.setOnClickListener(this);
		btn_call.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.tv_order:
			intent = new Intent(this, MeOrderActivity.class);
			break;
		case R.id.tv_prefer:
			intent = new Intent(this, PreferActivity.class);
			break;
		case R.id.tv_address:
			intent = new Intent(this, ManageAddressActivity.class);
			break;
		case R.id.tv_agreement:
			intent = new Intent(this, AgreementActivity.class);
			break;
		case R.id.tv_aboutus:
			intent = new Intent(this, AboutUSActivity.class);
			break;
		case R.id.tv_member:
			intent = new Intent(this, MemberCardActivity.class);
			break;
		case R.id.tv_share:
			intent = new Intent(this, ShareActivity.class);
			break;
		case R.id.btn_call:
			Utils.callPhone(this, "400-6589-8875");
			break;
		}
		if (intent != null)
			this.startActivity(intent);
	}

}
