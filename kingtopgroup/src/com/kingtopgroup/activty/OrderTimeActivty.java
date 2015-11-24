package com.kingtopgroup.activty;

import java.text.SimpleDateFormat;
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
import android.util.Log;
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

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.adapter.OrderTimeAdapter;
import com.kingtopgroup.adapter.OrderTommoroTimeAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;


public class OrderTimeActivty extends MainActionBarActivity implements OrderTimeAdapter.CallBack{

	private static final String TAG = "OrderTimeActivty";

	private GridView order_time_gridview;
	private RadioGroup rg;
	private List<Map<String, Object>> list;
	private View progress;
	//private ACache acache;
	private JSONObject mJsonObject;
	private String opid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_time);
		titleButton.setText("ѡ��ʱ��");
		this.opid = getIntent().getStringExtra("opid");
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

		//acache = ACache.get(this);

		//order_time_gridview
				//.setOnItemClickListener(new MyGridViewItemClickListener());
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
			mWay = "����";
		} else if ("2".equals(mWay)) {
			mWay = "��һ";
		} else if ("3".equals(mWay)) {
			mWay = "�ܶ�";
		} else if ("4".equals(mWay)) {
			mWay = "����";
		} else if ("5".equals(mWay)) {
			mWay = "����";
		} else if ("6".equals(mWay)) {
			mWay = "����";
		} else if ("7".equals(mWay)) {
			mWay = "����";
		}
		return mWay;
	}

	class MyRadioCheckChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			System.out.println("checked="+arg1);
			switch (arg1) {
			case 1:
				//setTime();
				getOtherDays("ValidDay_0");
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
		
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("opid", opid);
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_sesrvice_time,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						progress.setVisibility(View.GONE);
						try {
							String date = new String(arg2);
							mJsonObject = new JSONObject(date);
							
							//acache.put("service_time", obj);
							array = mJsonObject.optJSONArray("ValidDay_0");
							if(array == null){
								return ;
							}
							list = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								String TimeSection = array.optJSONObject(i)
										.optString("TimeSection");
								String StsId = array.optJSONObject(i)
										.optString("StsId");
								map.put("StsId", StsId);
								map.put("TimeSection", TimeSection.trim());
								list.add(map);
							}
							setAdapter(list, "ValidDay_0");

						} catch (JSONException e) {
							e.printStackTrace();
						}
						progress.setVisibility(View.GONE);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(OrderTimeActivty.this, "��ȡʱ��ʧ�ܣ�������",
								Toast.LENGTH_SHORT).show();
						progress.setVisibility(View.GONE);
					}

				});

	}

	private void setAdapter(List<Map<String, Object>> list, String day) {
		order_time_gridview = (GridView) findViewById(R.id.order_time_listview);
		if (day.equals("ValidDay_0")) {
			order_time_gridview.setAdapter(new OrderTimeAdapter(this, list,true,this));
		} else {
			//order_time_gridview.setAdapter(new OrderTommoroTimeAdapter(this,
					//list));
			order_time_gridview.setAdapter(new OrderTimeAdapter(this, list,false,this));
		}
	}

	private void getOtherDays(String ValidDay_1) {
		if (mJsonObject != null) {
			
			JSONArray array;
			try {
				array = mJsonObject.getJSONArray(ValidDay_1);
				if(array == null)
					return;
				list = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < array.length(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String StsId = array.optJSONObject(i)
							.optString("StsId");
					map.put("StsId", StsId);
					String TimeSection = array.optJSONObject(i).optString(
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

	@Override
	public void callBack(String stid) {
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("Uid", UserBean.getUSerBean().getUid());
		params.put("Opid", opid);
		params.put("Stsid", stid);

		// ʹ��Ĭ��ʱ�������Ի������һ������
		Calendar cale = Calendar.getInstance();
		// ��Calendar����ת����Date����
		java.util.Date tasktime = cale.getTime();
		// ������������ĸ�ʽ
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		params.put("ServiceDate", df.format(tasktime));
		params.put("Couponid", UserBean.getUSerBean().getCouponid());
		params.put("Couponmoney", "0");
		progress.setVisibility(View.VISIBLE);
		AsyncHttpCilentUtil.getInstance().post(
				ConstanceUtil.ser_service_time, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1,
							byte[] arg2) {
						progress.setVisibility(View.GONE);
						String date = new String(arg2);
						try {
							JSONObject obj = new JSONObject(date);
							String ActionMessage = obj
									.getString("ActionMessage");
							if (ActionMessage.equals("����ʱ�����óɹ�������Opid")) {
								Intent intent = new Intent(OrderTimeActivty.this,
										ChioceManagerActivty.class);
								intent.putExtra("opid", opid);
								startActivity(intent);
								return;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Utils.showToast(OrderTimeActivty.this, "���÷���ʱ��ʧ�ܣ�������!");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1,
							byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						progress.setVisibility(View.GONE);
						Utils.showToast(OrderTimeActivty.this, "���÷���ʱ��ʧ�ܣ�������!");
					}
				});
		
	}
}
