package com.kingtopgroup.activty;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.StringUtils;
import com.stevenhu.android.phone.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceAddressActivty extends Activity implements OnClickListener{
	private TextView service_phone,service_address_street,service_address_streets_num,service_address_person,service_address_mark;
	private LinearLayout add_or_reduce;
	private ImageView add_address;
	Map<String,Object> map=null;
	private TextView service_address_name,service_address_phone,service_address_address;
	private Button service_address_for_me,service_address_for_other,service_address_next_button;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.service_address);
	
	//为他人预约
	service_address_for_other=(Button) findViewById(R.id.for_other);
	service_address_for_other.setOnClickListener(this);
	
	//为自己预约
	service_address_for_me=(Button) findViewById(R.id.for_me);
	service_address_for_me.setOnClickListener(this);
	
	//下一步按钮
	service_address_next_button=(Button) findViewById(R.id.service_address_next_button);
	service_address_next_button.setOnClickListener(this);
	
	add_or_reduce=(LinearLayout) findViewById(R.id.add_or_reduce);
	
	//手机号
	service_phone=(TextView) findViewById(R.id.service_phone);
	
	//街道
	service_address_street=(TextView) findViewById(R.id.service_address_street);
	
	//门牌号
	service_address_streets_num=(TextView) findViewById(R.id.service_address_door_num);
	
	//联系人
	service_address_person=(TextView) findViewById(R.id.service_address_person);
	
	//备注
	service_address_mark=(TextView) findViewById(R.id.service_address_mark);
	
	add_address=(ImageView) findViewById(R.id.add_address);
	
	service_address_name=(TextView) findViewById(R.id.service_address_name);
	
	service_address_phone=(TextView) findViewById(R.id.service_address_phone);
	
	service_address_address=(TextView) findViewById(R.id.service_address_address);
	
	add_address.setOnClickListener(this);
	getDate();
	
}
 
 
 private void setDate(Map<String,Object> map){
			service_phone.setText((CharSequence) map.get("phone"));
			service_address_street.setText((CharSequence) map.get("street"));
			service_address_person.setText((CharSequence) map.get("person"));
			
			service_address_name.setText((CharSequence) map.get("person"));
			service_address_phone.setText((CharSequence) map.get("phone"));
			service_address_address.setText((CharSequence) map.get("street"));
		
 }
 
 
 private Map<String,Object> getDate(){
		//获取默认地址
		RequestParams params=AsyncHttpCilentUtil.getParams();
		params.put("uid", UserBean.getUSerBean().getUid());
		AsyncHttpCilentUtil.getInstance().get(this, ConstanceUtil.get_address_list_url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					try {
						String date=new String(arg2);
						JSONObject obj=new JSONObject(date);
						JSONArray array=obj.getJSONArray("ShipAddressList");
						for(int i=0;i<array.length();i++){
							String IsDefault=array.getJSONObject(i).getString("IsDefault");
							if(IsDefault.equals("1")){
					          String phone=array.getJSONObject(i).getString("Mobile");
					          String street=array.getJSONObject(i).getString("Address");
					          String person=array.getJSONObject(i).getString("Consignee");
							   map=new HashMap<String,Object>();
							   map.put("phone", phone);
							   map.put("street", street);
							   map.put("person", person);
					          //service_phone.setText(phone);
							   setDate(map);
							}
							
						
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				//super.onSuccess(arg0, arg1, arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		return  map;
 }
 
 
 private Boolean isEmpty(){
	 Boolean isnull=false;
	 if(!StringUtils.isEmpty(service_phone.getText())){
		 if(!StringUtils.isEmpty(service_address_street.getText())){
			 if(!StringUtils.isEmpty(service_address_streets_num.getText())){
				 if(!StringUtils.isEmpty(service_address_person.getText())){
					  isnull=true;
				 }else{
					 ToastUtils.show(this, "联系人不能为空");  
				 }
			 }else{
				 ToastUtils.show(this, "门牌号不能为空"); 
			 }
		 }else{
			 ToastUtils.show(this, "街道不能为空");
		 }
	 }else{
		 ToastUtils.show(this, "手机号不能为空");
	 }
	 return isnull;
 }
 

@Override
public void onClick(View arg0) {
	switch (arg0.getId()) {
	case R.id.for_other:
		add_or_reduce.setVisibility(View.GONE);
		break;

	case R.id.for_me://隐藏
		add_or_reduce.setVisibility(View.VISIBLE);
		break;
		
	case R.id.service_address_next_button:
		if(isEmpty()){
			Intent intent=new Intent(this,OrderTimeActivty.class);
			startActivity(intent);
		}
		break;
		
	case R.id.add_address:
		Intent inten=new Intent(this, AddAddressActivty.class);
		startActivity(inten);
		break;
		
	}
	
}
}
