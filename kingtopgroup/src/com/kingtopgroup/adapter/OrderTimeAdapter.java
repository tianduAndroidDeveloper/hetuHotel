package com.kingtopgroup.adapter;

import java.text.ChoiceFormat;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.SyncStateContract.Constants;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.ChioceManagerActivty;
import com.kingtopgroup.activty.OrderTimeActivty;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class OrderTimeAdapter extends BaseAdapter {
	private static final String TAG = "OrderTimeAdapter";
	private Context context;
	private LayoutInflater inflater;
	private LinearLayout.LayoutParams params;
	private List<Map<String, Object>> nameList;

	public OrderTimeAdapter(Context context, List<Map<String, Object>> nameList) {
		this.context = context;
		this.nameList = nameList;
		inflater = LayoutInflater.from(context);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public int getCount() {
		return nameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return nameList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View conveView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (conveView == null) {
			conveView = inflater.inflate(R.layout.oreder_time_item, null);
			viewHolder = new ViewHolder();
			viewHolder.buttoTime = (Button) conveView
					.findViewById(R.id.order_time_time);
			/*viewHolder.buttoTime.setBackgroundColor(conveView.getResources()
					.getColor(R.color.red));*/
			conveView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) conveView.getTag();
		}

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
		t.setToNow(); // 取得系统时间。
		/*
		 * int year = t.year; int month = t.month; int date = t.monthDay;
		 */
		int hour = t.hour;
		int minute = t.minute;
		int equationTime = hour * 60 + minute;

		String times = (String) nameList.get(arg0).get("TimeSection");
		int index = times.indexOf(":");
		String st = times.substring(0, index);
		String endTime = times.substring(index + 1, times.length());

		int getHour = Integer.parseInt(st);
		int getminter = Integer.parseInt(endTime);

		int getTime = getHour * 60 + getminter;
		int checked = equationTime - getTime;

		if (checked > 0) {
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_gb_pop);
			viewHolder.buttoTime.setTextColor(Color.BLACK);
		} else if ((getTime - equationTime) > 60) {
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_select_pop);
			viewHolder.buttoTime.setTextColor(Color.WHITE);
		} else if ((getTime - equationTime) < 60) {
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_gb_pop);
			viewHolder.buttoTime.setTextColor(Color.BLACK);
		} else if ((getTime - equationTime) == 60) {
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_select_pop);
			viewHolder.buttoTime.setTextColor(Color.WHITE);
		}
		viewHolder.buttoTime.setText((CharSequence) nameList.get(arg0).get(
				"TimeSection"));
		viewHolder.buttoTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				RequestParams params=AsyncHttpCilentUtil.getParams();
				params.put("Uid", UserBean.getUSerBean().getUid());
				params.put("Opid",UserBean.getUSerBean().getOpid());
				//params.put("Stsid", nameList.get(arg0.get).get(""));
				//params.put("ServiceDate", value);
				params.put("Couponid",UserBean.getUSerBean().getCouponid());
				params.put("Couponmoney",0);
				AsyncHttpCilentUtil.getInstance().post(ConstanceUtil.ser_service_time, params,new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String date =new String(arg2);
						try {
							JSONObject obj=new JSONObject(date);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Intent intent = new Intent(context, ChioceManagerActivty.class);
						context.startActivity(intent);
						
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						
					}
				});
				
				
				
			}
		});
		return conveView;
	}
	class ViewHolder {
		Button buttoTime;
	}

}
