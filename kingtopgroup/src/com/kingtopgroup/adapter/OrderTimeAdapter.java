package com.kingtopgroup.adapter;

import java.util.List;
import java.util.Map;

import com.kingtopgroup.R;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

public class OrderTimeAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private LinearLayout.LayoutParams params;
	private List<Map<String,Object>> nameList;
	
	public OrderTimeAdapter(Context context,List<Map<String,Object>> nameList){
		this.context=context;
		this.nameList=nameList;
		inflater=LayoutInflater.from(context);
		params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
		ViewHolder viewHolder=null ;
		if(conveView==null){
			conveView=inflater.inflate(R.layout.oreder_time_item, null);
			viewHolder=new ViewHolder();
			viewHolder.buttoTime=(Button) conveView.findViewById(R.id.order_time_time);
			viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.red));
			conveView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) conveView.getTag();
		}
		
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour;  
		int minute=t.minute;
    /* if(PnameList.get(arg0).get("TimeSection")>minute){
			
		}*/
		
		viewHolder.buttoTime.setText((CharSequence) nameList.get(arg0).get("TimeSection")); 
		viewHolder.buttoTime.setBackgroundColor(conveView.getResources().getColor(R.color.red));
		
		
		
		return conveView;
	}
	
	class ViewHolder{
		Button buttoTime;
		
	}

}
