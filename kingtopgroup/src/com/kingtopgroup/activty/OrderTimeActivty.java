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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.OrderTimeAdapter;
import com.kingtopgroup.adapter.OrderTommoroTimeAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class OrderTimeActivty extends Activity implements OnClickListener{
	private static final String TAG = "OrderTimeActivty";
	private GridView order_time_gridview;
	private RadioGroup rg;
	private TextView today,tommorr;
	private List<Map<String,Object>> list;
//	private ImageView today_image,tommorr_image,day_after_tomrror,day_after_day_by_day;
	private ACache acache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_time);  
		
		 order_time_gridview=(GridView) findViewById(R.id.order_time_listview);
		//order_time_gridview.setAdapter(new OrderTimeAdapter(this, getDate()));
		
		/*tommorr=(TextView) findViewById(R.id.tomorro);
		tommorr.setOnClickListener(this);
		//今天
		today_image=(ImageView) findViewById(R.id.today_image);
		today_image.setOnClickListener(this);
		//明天
		tommorr_image=(ImageView) findViewById(R.id.tomorro_image);
		tommorr_image.setOnClickListener(this);
		
		//后天
		day_after_tomrror=(ImageView) findViewById(R.id.day_after_tomrror);
		day_after_tomrror.setOnClickListener(this);
		
		//大后天
		day_after_day_by_day=(ImageView) findViewById(R.id.day_after_day_by_day);
		day_after_day_by_day.setOnClickListener(this);*/
		
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new MyRadioCheckChangeListener());
		RadioButton rb = (RadioButton) rg.getChildAt(0);
		rb.setChecked(true);
		
		setTime();
		//设置大后天
		RadioButton rbs = (RadioButton) rg.getChildAt(rg.getChildCount()-1);
		rbs.setText(getWeekOfDay());
		
		
		acache=ACache.get(this);
		
		order_time_gridview.setOnItemClickListener(new MyGridViewItemClickListener());
		
		
		//setTime();
	
		
	}
	
	class MyGridViewItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i(TAG, "onItemClick");
			Intent intent = new Intent(OrderTimeActivty.this, ChioceManagerActivty.class);
			OrderTimeActivty.this.startActivity(intent);
		}
		
	}
	
	
	 String getWeekOfDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
        int weeek=calendar.get(Calendar.DAY_OF_WEEK);  
        int getWeek=weeek+3;
        if(getWeek>7){
        	getWeek=getWeek-7;
        }
          String mWay=String.valueOf(getWeek);
	       if("1".equals(mWay)){  
	            mWay ="周天";  
	        }else if("2".equals(mWay)){  
	            mWay ="周一";  
	        }else if("3".equals(mWay)){  
	            mWay ="周二";  
	        }else if("4".equals(mWay)){  
	            mWay ="周三";  
	        }else if("5".equals(mWay)){  
	            mWay ="周四";  
	        }else if("6".equals(mWay)){  
	            mWay ="周五";  
	        }else if("7".equals(mWay)){  
	            mWay ="周六";  
	        }  
	       return mWay;
	}
	
	class MyRadioCheckChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			int count = rg.getChildCount();
			for(int i = 0; i < count; i++){
				RadioButton rButton = (RadioButton) arg0.getChildAt(i);
				rButton.setTextColor(Color.BLACK);
			
				
				
				/*if(i == 0){
					setTime();
				}else if(i == 1){
					getOtherDays("ValidDay_1");
				}else if(i == 2){
					getOtherDays("ValidDay_2");
				}else if(i == 3){
					getOtherDays("ValidDay_3");
				}*/
			}
			
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
			RadioButton rb = (RadioButton) rg.getChildAt(arg1 - 1);
			rb.setTextColor(Color.WHITE);
		}
		
	}
	
	public void setTime(){
		
		String oid=UserBean.getUSerBean().getOpid();
		RequestParams params=AsyncHttpCilentUtil.getParams();
				params.put("opid", oid);
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_sesrvice_time,params,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String date=new String(arg2);
					JSONObject obj=new JSONObject(date);
					acache.put("service_time", obj);
					JSONArray array=obj.getJSONArray("ValidDay_0");
					list=new ArrayList<Map<String,Object>>();
					for(int i=0;i<array.length();i++){
						Map<String,Object> map=new HashMap<String, Object>();
						String TimeSection=array.getJSONObject(i).getString("TimeSection");
						map.put("TimeSection",TimeSection.trim());
						list.add(map);
					}
					setAdapter(list,"ValidDay_0");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
			}
			
			
			
		});
		
	}
	
	private void setAdapter(List<Map<String,Object>> list,String day){
		order_time_gridview=(GridView) findViewById(R.id.order_time_listview);
		if(day.equals("ValidDay_0")){
			order_time_gridview.setAdapter(new OrderTimeAdapter(this,list));
		}else{
			order_time_gridview.setAdapter(new OrderTommoroTimeAdapter(this,list));
		}
	}
	
	private List<Map<String,Object>> getDate(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<=25;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("time","12:00");
			list.add(map);
		}
		return list;
	}

	@Override
	public void onClick(View arg0) {
      switch (arg0.getId()) {
	/*case R.id.today_image:
		setTime();
		break;

	case R.id.tomorro_image:
		getOtherDays("ValidDay_1");
		break;
		
      case R.id.day_after_tomrror:
    	  getOtherDays("ValidDay_2");
    	  break;
    	  
      case R.id.day_after_day_by_day:
    	  getOtherDays("ValidDay_2");
    	  break;*/
      }
	}
	
	private void getOtherDays(String ValidDay_1){
		if(acache.getAsJSONObject("service_time")!=null){
			JSONObject obj=acache.getAsJSONObject("service_time");
			JSONArray array;
			try {
				array = obj.getJSONArray(ValidDay_1);
				list=new ArrayList<Map<String,Object>>();
				for(int i=0;i<array.length();i++){
					Map<String,Object> map=new HashMap<String, Object>();
					String TimeSection=array.getJSONObject(i).getString("TimeSection");
					map.put("TimeSection",TimeSection.trim());
					list.add(map);
				}
				setAdapter(list,ValidDay_1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			}
			
		}
	}

