package com.kingtopgroup.activty;

import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.location.LastLocation;
import com.kingtogroup.location.LocationCallBack;
import com.kingtogroup.location.LocationService;
import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class orderActivity extends TabActivity implements OnCheckedChangeListener {

	private RadioGroup radiogroup;
	private TextView location;
	private TabHost tabhost;
	private TabWidget tabs;
	private View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderlistview);
		location = (TextView) findViewById(R.id.order);
		progress = findViewById(R.id.progress);
		initLocation();

		tabs = (TabWidget) findViewById(android.R.id.tabs);
		tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		tabhost.setFocusable(true);

		radiogroup = (RadioGroup) findViewById(R.id.main_radio);
		radiogroup.setOnCheckedChangeListener(this);
		RadioButton radioButton = (RadioButton) radiogroup.getChildAt(0);
		radioButton.setChecked(true);

		getDate();

	}

	private void initLocation() {
		LastLocation.initInstance().setCallBack(new LocationCallBack() {

			@Override
			public void callBack() {
				location.setText(LastLocation.initInstance().getShi());

			}
		});
		startService(new Intent(this, LocationService.class));

	}

	String extraMassageAttribute(JSONArray ItemAttributeList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < ItemAttributeList.length(); i++) {
			JSONObject object = ItemAttributeList.optJSONObject(i);
			if (object.optString("attrgroupname").contains("推拿姿势"))
				array.put(object);
		}
		return array.toString();
	}

	private void initTabs(JSONObject object) {
		Intent intent1 = new Intent();
		// 推拿
		TabHost.TabSpec tabSpec0 = tabhost.newTabSpec("推拿");
		tabSpec0.setIndicator("推拿");
		intent1.setClass(this, manipulationActivty.class);
		intent1.putExtra("json", object.optJSONArray("MassagesList").toString());
		intent1.putExtra("attributes", extraMassageAttribute(object.optJSONArray("ItemAttributeList")));
		intent1.putExtra("lubo", true);
		// object.optJSONArray("MassagesList").toString());
		tabSpec0.setContent(intent1);
		tabhost.addTab(tabSpec0);

		// 足疗
		Intent intent2 = new Intent();
		TabHost.TabSpec tabSpec5 = tabhost.newTabSpec("足疗");
		tabSpec5.setIndicator("足疗");
		intent2.setClass(this, manipulationActivty.class);
		intent2.putExtra("json", object.optJSONArray("PedicuresList").toString());
		tabSpec5.setContent(intent2);
		tabhost.addTab(tabSpec5);

		// 推拿师
		TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("订制");
		tabSpec2.setIndicator("订制");
		tabSpec2.setContent(new Intent(orderActivity.this, OrderForCustomerActivty.class));
		tabhost.addTab(tabSpec2);
		tabhost.setCurrentTab(0);

		// 订制
		Intent intent4 = new Intent();
		TabHost.TabSpec tabSpec4 = tabhost.newTabSpec("推拿师");
		tabSpec4.setIndicator("推拿师");
		intent4.setClass(this, messageActivty.class);
		intent4.putExtra("json", object.optJSONArray("MassagersList").toString());
		intent4.putExtra("reviews", object.optJSONObject("ReviewCount").toString());
		tabSpec4.setContent(intent4);
		tabhost.addTab(tabSpec4);
	}

	public void getDate() {
		progress.setVisibility(View.VISIBLE);
		// 服务器端传来数据

		new AsyncHttpClient().get(getResources().getString(R.string.url_getItemList), null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						String date = new String(arg2);
						JSONObject jObject = new JSONObject(date);
						initTabs(jObject);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(orderActivity.this, "出现异常，请重试", Toast.LENGTH_LONG).show();
					}
					progress.setVisibility(View.GONE);
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				progress.setVisibility(View.GONE);
				Toast.makeText(orderActivity.this, "请求失败，请重试", Toast.LENGTH_LONG).show();
			}
		});

	}

	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		// 推拿
		case R.id.manipulation:
			tabhost.setCurrentTab(0);
			break;

		case R.id.pedicure:
			tabhost.setCurrentTab(1);
			break;

		case R.id.message:
			tabhost.setCurrentTab(3);
			break;

		case R.id.orders:
			tabhost.setCurrentTab(2);
			break;
		}

	}

}
