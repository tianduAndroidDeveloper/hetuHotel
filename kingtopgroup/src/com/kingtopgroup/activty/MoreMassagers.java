package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.ManagerAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.ToastUtils;

public class MoreMassagers extends MainActionBarActivity {
	private static final String TAG = "MoreMassagers";
	ListView lv;
	List<ManagerBean> massageList;
	View progress;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_massage);
		titleButton.setText("��������ʦ");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		progress = findViewById(R.id.progress);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
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
							Log.i(TAG, date);
							try {
								JSONObject obj = new JSONObject(date);
								JSONArray array = obj
										.getJSONArray("MassageList");
								JSONObject psinfo = obj.getJSONObject("PSinfo");
								String ServiceDate = psinfo
										.getString("ServiceDate");
								massageList = new ArrayList<ManagerBean>();
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

									massageList.add(manager);
									if (name.equals("��������ʦ"))
										managerAny = manager;
								}
								if (managerAny != null) {
									massageList.remove(managerAny);
									massageList.add(0, managerAny);
								}

								fillData();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							progress.setVisibility(View.GONE);

						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(MoreMassagers.this, "����ʧ�ܣ������ԣ�",
								Toast.LENGTH_SHORT).show();
						progress.setVisibility(View.GONE);

					}
				});
	}

	ManagerAdapter adapter;

	void fillData() {
		adapter = new ManagerAdapter(this, massageList, massageList.size());
		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.red_bg);
		btn.setTextColor(Color.WHITE);
		btn.setText("��һ��");
		lv.addFooterView(btn);
		lv.setAdapter(adapter);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (adapter.getCheckedCount() == 0) {
					Toast.makeText(MoreMassagers.this, "����û��ѡ������ʦŶ",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String count = UserBean.getUSerBean().getBuyCount();
				Integer.parseInt(count);
				String checked = adapter.getCheckedIds();
				String getChecked;
				if (checked.indexOf(",") != -1) {
					getChecked = checked.substring(0, checked.indexOf(","));
				} else {
					getChecked = checked;
				}
				if (Integer.parseInt(count) < adapter.getCheckedCount()) {
					ToastUtils.show(MoreMassagers.this, "��ѡ������ʦ�������빺����Ŀ���������ȣ�");
				} else {
					RequestParams params = AsyncHttpCilentUtil.getParams();
					params.put("Uid", UserBean.getUSerBean().getUid());
					params.put("Opid", UserBean.getUSerBean().getOpid());
					params.put("StoreId", getChecked);
					params.put("MasagerIdList", checked);
					AsyncHttpCilentUtil.getInstance().post(
							ConstanceUtil.set_manager_list, params,
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									String data = new String(arg2);
									try {
										JSONObject obj = new JSONObject(data);
										String ActionMessage = obj
												.getString("ActionMessage");
										if (ActionMessage.equals("���óɹ�")) {
											Intent inten = new Intent(
													MoreMassagers.this,
													CommitActivity.class);
											startActivity(inten);
										} else {
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {

								}
							});
				}

			}
		});
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

}