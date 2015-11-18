package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;

import com.kingtogroup.location.LastLocation;
import com.kingtogroup.location.LocationCallBack;
import com.kingtogroup.location.LocationService;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ADInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stevenhu.android.phone.utils.ViewFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class orderActivity extends TabActivity implements
		OnCheckedChangeListener {

	private ImageView imgaeview;
	private RadioGroup radiogroup;
	private RadioButton manipulation, message, orders, pedicure, location;
	private TabHost tabhost;
	private TabWidget tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderlistview);
		getDate();
		manipulation = (RadioButton) findViewById(R.id.manipulation);
		pedicure = (RadioButton) findViewById(R.id.pedicure);
		message = (RadioButton) findViewById(R.id.message);
		orders = (RadioButton) findViewById(R.id.orders);
		location = (RadioButton) findViewById(R.id.order);
		initLocation();

		tabs = (TabWidget) findViewById(android.R.id.tabs);
		tabhost = (TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		tabhost.setFocusable(true);

		radiogroup = (RadioGroup) findViewById(R.id.main_radio);
		radiogroup.setOnCheckedChangeListener(this);
		RadioButton radioButton = (RadioButton) radiogroup.getChildAt(0);
		radioButton.setChecked(true);

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

	private void initTabs(JSONObject object) {
		Intent intent = new Intent();
		// 推拿
		TabHost.TabSpec tabSpec0 = tabhost.newTabSpec("推拿");
		tabSpec0.setIndicator("推拿");
		intent.setClass(this, manipulationActivty.class);
		intent.putExtra("json", object.optJSONArray("MassagesList").toString());
		tabSpec0.setContent(intent);
		tabhost.addTab(tabSpec0);

		// 足疗
		TabHost.TabSpec tabSpec5 = tabhost.newTabSpec("足疗");
		tabSpec5.setIndicator("足疗");
		tabSpec5.setContent(new Intent(orderActivity.this,
				pedicureActivty.class));
		tabhost.addTab(tabSpec5);

		// 推拿师
		TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("订制");
		tabSpec2.setIndicator("订制");
		tabSpec2.setContent(new Intent(orderActivity.this,
				OrderForCustomerActivty.class));
		tabhost.addTab(tabSpec2);
		tabhost.setCurrentTab(0);

		// 订制
		TabHost.TabSpec tabSpec4 = tabhost.newTabSpec("推拿师");
		tabSpec4.setIndicator("推拿师");
		tabSpec4.setContent(new Intent(orderActivity.this, messageActivty.class));
		tabhost.addTab(tabSpec4);
	}

	public void getDate() {
		// 服务器端传来数据

		new AsyncHttpClient().get(
				getResources().getString(R.string.url_getItemList), null,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (arg0 == 200) {
							try {
								String date = new String(arg2);
								JSONObject jObject = new JSONObject(date);
								initTabs(jObject);
							} catch (Exception e) {
								e.printStackTrace();
								// checkedNow();
								Toast.makeText(orderActivity.this, "ddd",
										Toast.LENGTH_LONG).show();
							}
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

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
			tabhost.setCurrentTab(2);
			break;

		case R.id.orders:
			tabhost.setCurrentTab(3);
			break;
		}

	}

}
