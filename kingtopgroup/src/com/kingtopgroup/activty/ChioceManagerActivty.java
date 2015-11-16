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
import com.kingtopgroup.adapter.ManagerAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ChioceManagerActivty extends Activity{
	List<ManagerBean> managerBean;
	private ListView manager_listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chioce_manager);
		
		manager_listview=(ListView) findViewById(R.id.manager_listview);
		
		RequestParams params=AsyncHttpCilentUtil.getParams();
		params.put("uid", UserBean.getUSerBean().getUid());
		params.put("opid", UserBean.getUSerBean().getOpid());
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_manager_list_url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					String date=new String(arg2);
					try {
						JSONObject obj=new JSONObject(date);
						JSONArray array=obj.getJSONArray("MassageList");
						managerBean=new ArrayList<ManagerBean>();
						//ManagerBean manager=new ManagerBean();
						for(int i=0;i<array.length();i++){
							ManagerBean manager=new ManagerBean();
							Map<String,Object> map=new HashMap<String, Object>();
							String name=array.getJSONObject(i).getString("Name");
							manager.address=name;
							//map.put("name", name);
							String Logo=array.getJSONObject(i).getString("Logo");
							//map.put("Logo", Logo);
							String Point_X=array.getJSONObject(i).getString("Point_X");
							//map.put("Point_X", Point_X);
							String Point_Y=array.getJSONObject(i).getString("Point_Y");
							//map.put("Point_Y", Point_Y);
							String Address=array.getJSONObject(i).getString("Address");
							manager.address=Address;
							//map.put("Address", Address);
							String Sex=array.getJSONObject(i).getString("Sex");
							manager.sex=Sex;
							
							managerBean.add(manager);
							//map.put("Sex", Sex);
							//list.add(map);
						}
						
						setAdapter(managerBean);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setAdapter(List<ManagerBean> list){
		manager_listview.setAdapter(new ManagerAdapter(this, list));
	}

}
