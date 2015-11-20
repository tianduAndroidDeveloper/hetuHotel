package com.kingtopgroup.activty;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.OrderTimeAdapter;
import com.kingtopgroup.adapter.OrderTommoroTimeAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class OrderTimeActivty extends MainActionBarActivity {
	private GridView order_time_gridview;
	private RadioGroup rg;
	private List<Map<String, Object>> list;
	private View progress;
	private ACache acache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_time);
		titleButton.setText("选择时间");
		init();
	}

	void init() {
		order_time_gridview = (GridView) findViewById(R.id.order_time_listview);
		progress = findViewById(R.id.progress);
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new MyRadioCheckChangeListener());
		RadioButton rb = (RadioButton) rg.getChildAt(0);
		rb.setChecked(true);

		setTime();
		RadioButton rbs = (RadioButton) rg.getChildAt(rg.getChildCount() - 1);
		rbs.setText(getWeekOfDay());

		acache = ACache.get(this);

		order_time_gridview
				.setOnItemClickListener(new MyGridViewItemClickListener());
	}

	class MyGridViewItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(OrderTimeActivty.this,
					ChioceManagerActivty.class);
			LinearLayout convertView = (LinearLayout) arg1;
			Button btn = (Button) convertView.getChildAt(0);
			UserBean.getUSerBean().setOrdertime(btn.getText().toString());
			OrderTimeActivty.this.startActivity(intent);
		}

	}

	String getWeekOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int weeek = calendar.get(Calendar.DAY_OF_WEEK);
		int getWeek = weeek + 3;
		if (getWeek > 7) {
			getWeek = getWeek - 7;
		}
		String mWay = String.valueOf(getWeek);
		if ("1".equals(mWay)) {
			mWay = "周天";
		} else if ("2".equals(mWay)) {
			mWay = "周一";
		} else if ("3".equals(mWay)) {
			mWay = "周二";
		} else if ("4".equals(mWay)) {
			mWay = "周三";
		} else if ("5".equals(mWay)) {
			mWay = "周四";
		} else if ("6".equals(mWay)) {
			mWay = "周五";
		} else if ("7".equals(mWay)) {
			mWay = "周六";
		}
		return mWay;
	}

	class MyRadioCheckChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {

			switch (arg1) {
			case 1:
				setTime();
				break;

			case 2:
				getOtherDays("ValidDay_1");
				break;

			case 3:
				getOtherDays("ValidDay_2");
				break;

			case 4:
				getOtherDays("ValidDay_3");
				break;
			}
		}

	}

	JSONArray array;

	public void setTime() {
		progress.setVisibility(View.VISIBLE);
		String oid = UserBean.getUSerBean().getOpid();
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("opid", oid);
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_sesrvice_time,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						try {
							String date = new String(arg2);
							JSONObject obj = new JSONObject(date);
							acache.put("service_time", obj);
							array = obj.getJSONArray("ValidDay_0");
							list = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								String TimeSection = array.getJSONObject(i)
										.getString("TimeSection");
								String StsId = array.getJSONObject(i)
										.getString("StsId");
								map.put("StsId", StsId);
								map.put("TimeSection", TimeSection.trim());
								list.add(map);
							}
							setAdapter(list, "ValidDay_0");
							progress.setVisibility(View.GONE);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(OrderTimeActivty.this, "获取时间失败，请重试", Toast.LENGTH_SHORT).show();
						progress.setVisibility(View.GONE);
					}

				});

	}

	private void setAdapter(List<Map<String, Object>> list, String day) {
		order_time_gridview = (GridView) findViewById(R.id.order_time_listview);
		if (day.equals("ValidDay_0")) {
			order_time_gridview.setAdapter(new OrderTimeAdapter(this, list));
		} else {
			order_time_gridview.setAdapter(new OrderTommoroTimeAdapter(this,
					list));
		}
	}

	private void getOtherDays(String ValidDay_1) {
		if (acache.getAsJSONObject("service_time") != null) {
			JSONObject obj = acache.getAsJSONObject("service_time");
			JSONArray array;
			try {
				array = obj.getJSONArray(ValidDay_1);
				list = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < array.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String TimeSection = array.getJSONObject(i).getString(
							"TimeSection");
					map.put("TimeSection", TimeSection.trim());
					list.add(map);
				}
				setAdapter(list, ValidDay_1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

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
