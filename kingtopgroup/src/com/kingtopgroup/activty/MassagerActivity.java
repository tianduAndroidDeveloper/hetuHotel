package com.kingtopgroup.activty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kingtopgroup.R;

public class MassagerActivity extends Activity implements OnClickListener {
	TextView tv_all;
	TextView tv_await;
	TextView tv_already;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massager);
		init();
	}

	void init() {
		tv_all = (TextView) findViewById(R.id.tv_all);
		tv_await = (TextView) findViewById(R.id.tv_await);
		tv_already = (TextView) findViewById(R.id.tv_already);

		tv_all.setOnClickListener(this);
		tv_await.setOnClickListener(this);
		tv_already.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_all:
			Intent intent = new Intent(this, AllMassagerOrderActivity.class);
			this.startActivity(intent);
			break;
		case R.id.tv_await:
			getOrders("待接单", getString(R.string.url_order_confirming));
			break;
		case R.id.tv_already:
			getOrders("已接单", getString(R.string.url_order_confirmed));
			break;
		default:
			break;
		}
	}

	void getOrders(String title, String url) {
		Intent intent = new Intent(this, MassagerOrderActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		this.startActivity(intent);
	}
}
