package com.kingtopgroup.adapter;

import java.text.ChoiceFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
		// TODO Auto-generated method stub
		return nameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return nameList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
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
			viewHolder.buttoTime.setBackgroundColor(conveView.getResources()
					.getColor(R.color.red));
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

		// String times=(String) nameList.get(arg0).get("TimeSection");

		int checked = equationTime - getTime;

		if (checked > 0) {
			// viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.gray2));
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_gb_pop);
			viewHolder.buttoTime.setTextColor(Color.BLACK);
		} else if ((getTime - equationTime) > 60) {
			// viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.red));
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_select_pop);
			viewHolder.buttoTime.setTextColor(Color.WHITE);
		} else if ((getTime - equationTime) < 60) {
			// viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.gray2));
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_gb_pop);
			viewHolder.buttoTime.setTextColor(Color.BLACK);
		} else if ((getTime - equationTime) == 60) {
			// viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.red));
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_select_pop);
			viewHolder.buttoTime.setTextColor(Color.WHITE);
		}
		viewHolder.buttoTime.setText((CharSequence) nameList.get(arg0).get(
				"TimeSection"));
		viewHolder.buttoTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "onClick");
				Intent intent = new Intent(context, ChioceManagerActivty.class);
				context.startActivity(intent);
			}
		});
		return conveView;
	}

	class ViewHolder {
		Button buttoTime;

	}

}
