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
import com.kingtopgroup.adapter.AddressAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AddAddressActivty extends Activity implements OnClickListener{
	
	private TextView add_Address;
	private ListView add_listView;
	Map<String,Object> map=null;
	//List<Map<String,Object>> list=null;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);
		
		
		add_Address=(TextView) findViewById(R.id.add_address);
		add_listView=(ListView) findViewById(R.id.address_listview);
		add_Address.setOnClickListener(this);
		
		getDate();
		
		//add_listView.setAdapter(new SimpleAdapter(this, getDate(), R.layout.address_item, new String[]{"name","phone","detail_address","isDeafult"}, new int[]{R.id.name,R.id.phone,R.id.detail_address}));
	}
	
	
	private void setdate(List<Map<String,Object>> list){
		add_listView.setAdapter(new AddressAdapter(this, list));
	}
	
	public List<Map<String,Object>>getDate(){
		RequestParams params=AsyncHttpCilentUtil.getParams();
				
		params.put("uid", UserBean.getUSerBean().getUid());
		AsyncHttpCilentUtil.getInstance().get(this, ConstanceUtil.get_address_list_url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1,byte[] arg2) {
				if(arg0==200){
					try {
						JSONObject obj;
						String date=new String(arg2);
						obj = new JSONObject(date);
						JSONArray array=obj.getJSONArray("ShipAddressList");
						List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
						for(int i=0;i<array.length();i++){
							String IsDefault=array.getJSONObject(i).getString("IsDefault");
							//if(IsDefault.equals("1")){
					          String phone=array.getJSONObject(i).getString("Mobile");
					          String street=array.getJSONObject(i).getString("Address");
					          String person=array.getJSONObject(i).getString("Consignee");
							   map=new HashMap<String,Object>();
							   map.put("phone", phone);
							   map.put("street", street);
							   map.put("person", person);
							   map.put("isDefault", IsDefault);
					          //service_phone.setText(phone);
							
							//setDate(map);
							   list.add(map);
						}
						setdate(list);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				//super.onSuccess(arg0, arg1);
			}
			
			


			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
		
		
		return null;
	}

	@Override
	public void onClick(View arg0) {
		Intent inten=new Intent(this, AddAddressAddActivty.class);
		startActivity(inten);
	}

}
