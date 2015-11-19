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
import com.stevenhu.android.phone.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChioceManagerActivty extends Activity {
	private static final String TAG = "ChioceManagerActivty";
	List<ManagerBean> managerBean;
	private ListView manager_listview;
	private Button manager_next_button;
	private TextView orderDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chioce_manager);

		manager_listview = (ListView) findViewById(R.id.manager_listview);

		manager_next_button = (Button) findViewById(R.id.manager_next_button);

		orderDate = (TextView) findViewById(R.id.orderDate);

		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("uid", UserBean.getUSerBean().getUid());
		params.put("opid", UserBean.getUSerBean().getOpid());
		AsyncHttpCilentUtil.getInstance().get(
				ConstanceUtil.get_manager_list_url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (arg0 == 200) {
							String date = new String(arg2);
							try {
								JSONObject obj = new JSONObject(date);
								JSONArray array = obj
										.getJSONArray("MassageList");
								JSONObject psinfo = obj.getJSONObject("PSinfo");
								String ServiceDate = psinfo
										.getString("ServiceDate");
								orderDate.setText(ServiceDate);
								managerBean = new ArrayList<ManagerBean>();
								// ManagerBean manager=new ManagerBean();
								ManagerBean managerAny = null;
								for (int i = 0; i < array.length(); i++) {
									ManagerBean manager = new ManagerBean();
									Map<String, Object> map = new HashMap<String, Object>();
									String name = array.getJSONObject(i)
											.getString("Name");
									manager.name = name;
									String MassagerId = array.getJSONObject(i)
											.getString("Name");
									String Logo = array.getJSONObject(i)
											.getString("Logo");
									manager.Logo = Logo;
									String StoreId = array.getJSONObject(i)
											.getString("StoreId");
									manager.StoreId = StoreId;
									String Point_X = array.getJSONObject(i)
											.getString("Point_X");
									manager.point_x = Point_X;
									String Point_Y = array.getJSONObject(i)
											.getString("Point_Y");
									manager.point_y = Point_Y;
									String Address = array.getJSONObject(i)
											.getString("Address");
									manager.address = Address;
									// manager.serviceDate=ServiceDate;
									String Sex = array.getJSONObject(i)
											.getString("Sex");
									manager.sex = Sex;

									managerBean.add(manager);
									if (name.equals("任意推拿师"))
										managerAny = manager;
								}
								if (managerAny != null) {
									managerBean.remove(managerAny);
									managerBean.add(0, managerAny);
								}

								setAdapter(managerBean);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});

		manager_next_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String count = UserBean.getUSerBean().getBuyCount();
				Integer.parseInt(count);
				String checked=adapter.getCheckedIds();
				String getChecked;
				if(checked.indexOf(",")!=-1){
					getChecked=checked.substring(0,checked.indexOf(","));
				}else{
					getChecked=checked;
				}//
				if (Integer.parseInt(count) < adapter.getCheckedCount()) {
					//Integer.parseInt(count) > adapter.getCheckedCount() && Integer.parseInt(getChecked)==5
					ToastUtils.show(ChioceManagerActivty.this,
							"你選擇推拿師的數量與購買項目的數量不等！");
				} else {
					RequestParams params = AsyncHttpCilentUtil.getParams();
					params.put("Uid", UserBean.getUSerBean().getUid());
					params.put("Opid", UserBean.getUSerBean().getOpid());
					params.put("StoreId", getChecked);
					params.put("MasagerIdList",checked);
					
					  AsyncHttpCilentUtil.getInstance().post(ConstanceUtil.set_manager_list, params,new AsyncHttpResponseHandler() {
					  
					  @Override public void onSuccess(int arg0, Header[] arg1,
					  byte[] arg2) { 
						  String data=new String(arg2);
						  try {
							JSONObject obj=new JSONObject(data);
							String ActionMessage=obj.getString("ActionMessage");
							if(ActionMessage.equals("设置成功")){
								Intent inten = new Intent(ChioceManagerActivty.this,
										SubmitOrderActivty.class);
								startActivity(inten);
							}else{
								//ToastUtils.show(this, "網絡錯誤，請稍後再試");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  
					  
					  }
					  
					  @Override public void onFailure(int arg0, Header[] arg1,
					  byte[] arg2, Throwable arg3) { // TODO Auto-generated
					  
					  } });
				}
			}
		});
	}

	private ManagerAdapter adapter;

	private void setAdapter(List<ManagerBean> list) {
		adapter = new ManagerAdapter(this, list);
		manager_listview.setAdapter(adapter);
	}

}
