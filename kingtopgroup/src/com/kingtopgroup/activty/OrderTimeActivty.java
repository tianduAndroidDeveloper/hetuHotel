package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.OrderTimeAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderTimeActivty extends Activity{
	private GridView order_time_gridview;
	private TextView today,tommorr;
	private List<Map<String,Object>> list;
	private ImageView today_image,tommorr_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_time);
		
		//order_time_gridview=(GridView) findViewById(R.id.order_time_listview);
		//order_time_gridview.setAdapter(new OrderTimeAdapter(this, getDate()));
		
		tommorr=(TextView) findViewById(R.id.tomorro);
		tommorr.setOnClickListener(null);
		
		today_image=(ImageView) findViewById(R.id.today_image);
		today_image.setOnClickListener(null);
		
		String oid=UserBean.getUSerBean().getOpid();
		RequestParams params=AsyncHttpCilentUtil.getParams();
				params.put("opid", oid);
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_sesrvice_time,params,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String date=new String(arg2);
					JSONObject obj=new JSONObject(date);
					JSONArray array=obj.getJSONArray("ValidDay_0");
					list=new ArrayList<Map<String,Object>>();
					for(int i=0;i<array.length();i++){
						Map<String,Object> map=new HashMap<String, Object>();
						String TimeSection=array.getJSONObject(i).getString("TimeSection");
						map.put("TimeSection",TimeSection.trim());
						list.add(map);
					}
					setAdapter(list);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// TODO Auto-generated method stub
				//super.onSuccess(arg0, arg1);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
		
	}
	
	private void setAdapter(List<Map<String,Object>> list){
		order_time_gridview=(GridView) findViewById(R.id.order_time_listview);
		order_time_gridview.setAdapter(new OrderTimeAdapter(this,list));
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

}
