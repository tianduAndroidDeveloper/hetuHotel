package com.kingtopgroup.activty;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.adapter.OrderTimeAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class OrderTimeActivty extends MainActionBarActivity implements OrderTimeAdapter.CallBack {
	private GridView order_time_gridview;
	private RadioGroup rg;
	private List<Map<String, Object>> list;
	private View progress;
	private JSONObject mJsonObject;
	private String opid;
	SimpleDateFormat sdf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_time);
		titleButton.setText("选择时间");
		this.opid = getIntent().getStringExtra("opid");
		sdf = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
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

	int dayOfMonth;

	class MyRadioCheckChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
			int dd = Integer.parseInt(sdf2.format(new Date()));
			switch (arg1) {
			case 1:
				dayOfMonth = dd;
				getOtherDays("ValidDay_0");
				break;

			case 2:
				dayOfMonth = dd + 1;
				getOtherDays("ValidDay_1");
				break;

			case 3:
				dayOfMonth = dd + 2;
				getOtherDays("ValidDay_2");
				break;

			case 4:
				dayOfMonth = dd + 3;
				getOtherDays("ValidDay_3");
				break;
			}
		}

	}

	JSONArray array;

	public void setTime() {
		progress.setVisibility(View.VISIBLE);

		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("opid", opid);
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_sesrvice_time, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progress.setVisibility(View.GONE);
				try {
					String date = new String(arg2);
					mJsonObject = new JSONObject(date);

					// acache.put("service_time", obj);
					array = mJsonObject.optJSONArray("ValidDay_0");
					if (array == null)
						return;
					list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < array.length(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						String TimeSection = array.optJSONObject(i).optString("TimeSection");
						String StsId = array.optJSONObject(i).optString("StsId");
						map.put("StsId", StsId);
						map.put("TimeSection", TimeSection.trim());
						list.add(map);

						setAdapter(list, "ValidDay_0");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress.setVisibility(View.GONE);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(OrderTimeActivty.this, "获取时间失败，请重试", Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);
			}

		});

	}

	private void setAdapter(List<Map<String, Object>> list, String day) {
		order_time_gridview = (GridView) findViewById(R.id.order_time_listview);
		String date = sdf.format(new Date()) + dayOfMonth + "日  ";
		if (day.equals("ValidDay_0")) {
			order_time_gridview.setAdapter(new OrderTimeAdapter(this, list, true, this, date));
		} else {
			order_time_gridview.setAdapter(new OrderTimeAdapter(this, list, false, this, date));
		}
	}

	private void getOtherDays(String ValidDay_1) {
		if (mJsonObject != null) {

			JSONArray array;
			try {
				array = mJsonObject.getJSONArray(ValidDay_1);
				if (array == null)
					return;
				list = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < array.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String StsId = array.optJSONObject(i).optString("StsId");
					map.put("StsId", StsId);
					String TimeSection = array.optJSONObject(i).optString("TimeSection");
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

	@Override
	public void callBack(String stid, final String time) {
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("Uid", UserBean.getUSerBean().getUid());
		params.put("Opid", opid);
		params.put("Stsid", stid);

		// 使用默认时区和语言环境获得一个日历
		Calendar cale = Calendar.getInstance();
		// 将Calendar类型转换成Date类型
		java.util.Date tasktime = cale.getTime();
		// 设置日期输出的格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		params.put("ServiceDate", df.format(tasktime));
		params.put("Couponid", 0);
		params.put("Couponmoney", "0");
		progress.setVisibility(View.VISIBLE);
		AsyncHttpCilentUtil.getInstance().post(ConstanceUtil.ser_service_time, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progress.setVisibility(View.GONE);
				String date = new String(arg2);
				try {
					JSONObject obj = new JSONObject(date);
					String ActionMessage = obj.getString("ActionMessage");
					if (ActionMessage.equals("服务时间设置成功，返回Opid")) {
						Intent intent = new Intent(OrderTimeActivty.this, ChioceManagerActivty.class);
						intent.putExtra("opid", opid);
						intent.putExtra("date", time);
						startActivity(intent);
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Utils.showToast(OrderTimeActivty.this, "设置服务时间失败，请重试!");
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				progress.setVisibility(View.GONE);
				Utils.showToast(OrderTimeActivty.this, "设置服务时间失败，请重试!");
			}
		});

	}
}
