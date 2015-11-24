package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.adapter.ManagerAdapter2;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.ToastUtils;

public class ChioceManagerActivty extends MainActionBarActivity {
	private static final String TAG = "ChioceManagerActivty";
	List<ManagerBean> managerBean;
	private ListView manager_listview;
	private TextView orderDate;
	private View headerView;
	private View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chioce_manager);
		titleButton.setText("选择推拿师");

		progress = findViewById(R.id.progress);

		progress.setVisibility(View.VISIBLE);
		manager_listview = (ListView) findViewById(R.id.manager_listview);
		headerView = View.inflate(this, R.layout.header_massager, null);
		manager_listview.addHeaderView(headerView);
		addFooter();
		orderDate = (TextView) headerView.findViewById(R.id.orderDate);
		orderDate.setText(getIntent().getStringExtra("date"));
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("uid", UserBean.getUSerBean().getUid());
		params.put("opid", UserBean.getUSerBean().getOpid());
		AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_manager_list_url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					String date = new String(arg2);
					Log.i(TAG, date);
					try {
						JSONObject obj = new JSONObject(date);
						JSONArray array = obj.getJSONArray("MassageList");
						JSONObject psinfo = obj.getJSONObject("PSinfo");
						String ServiceDate = psinfo.getString("ServiceDate");
						managerBean = new ArrayList<ManagerBean>();
						// ManagerBean manager=new ManagerBean();
						ManagerBean managerAny = null;
						for (int i = 0; i < array.length(); i++) {
							ManagerBean manager = new ManagerBean();
							Map<String, Object> map = new HashMap<String, Object>();
							String name = array.getJSONObject(i).getString("Name");
							manager.name = name;
							String MassagerId = array.getJSONObject(i).getString("Name");
							String Logo = array.getJSONObject(i).getString("Logo");
							manager.Logo = Logo;
							String StoreId = array.getJSONObject(i).getString("StoreId");
							manager.StoreId = StoreId;
							String Point_X = array.getJSONObject(i).getString("Point_X");
							manager.point_x = Point_X;
							String Point_Y = array.getJSONObject(i).getString("Point_Y");
							manager.point_y = Point_Y;
							String Address = array.getJSONObject(i).getString("Address");
							manager.address = Address;
							// manager.serviceDate=ServiceDate;
							String Sex = array.getJSONObject(i).getString("Sex");
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

	void addFooter() {
		int dp20 = Utils.dp2px(this, 20);
		int dp10 = Utils.dp2px(this, 10);
		LinearLayout ll = new LinearLayout(this);
		ll.setGravity(Gravity.CENTER);
		ll.setPadding(dp20, dp20, dp20, dp20);
		ll.setBackgroundColor(getResources().getColor(R.color.gray_light));
		TextView tv = new TextView(this);
		tv.setBackgroundResource(R.drawable.personal_bg);
		tv.setPadding(dp10, dp10, dp10, dp10);
		tv.setTextColor(Color.RED);
		tv.setText("没有合适的推拿师?去看看更多");
		ll.addView(tv);
		manager_listview.addFooterView(ll);
		Button btn = new Button(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		btn.setBackgroundResource(R.drawable.red_bg);
		btn.setTextColor(Color.WHITE);
		btn.setText("下一步");
		btn.setLayoutParams(params);
		manager_listview.addFooterView(btn);

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				more();
			}
		});

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (adapter.getCheckedCount() == 0) {
					Toast.makeText(ChioceManagerActivty.this, "您还没有选择推拿师哦", Toast.LENGTH_SHORT).show();
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
				}//
				if (Integer.parseInt(count) < adapter.getCheckedCount()) {
					// Integer.parseInt(count) > adapter.getCheckedCount() &&
					// Integer.parseInt(getChecked)==5
					ToastUtils.show(ChioceManagerActivty.this, "你选择推拿师的数量与购买项目的数量不等！");
				} else {
					RequestParams params = AsyncHttpCilentUtil.getParams();
					params.put("Uid", UserBean.getUSerBean().getUid());
					params.put("Opid", UserBean.getUSerBean().getOpid());
					params.put("MassagerId", getChecked);
					params.put("MasagerIdList", checked);

					AsyncHttpCilentUtil.getInstance().post(ConstanceUtil.set_manager_list, params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							String data = new String(arg2);
							Log.i(TAG, data);
							try {
								JSONObject obj = new JSONObject(data);
								String ActionMessage = obj.getString("ActionMessage");
								int returnValue = obj.optInt("ReturnValue");
								if (returnValue != 0) {
									UserBean.getUSerBean().setOpid(String.valueOf(returnValue));
									Intent inten = new Intent(ChioceManagerActivty.this, CommitActivity.class);
									inten.putExtra("opid", returnValue);
									startActivity(inten);
								} else {
									Toast.makeText(ChioceManagerActivty.this, ActionMessage, Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

						}
					});
				}
			}
		});
	}


	private ManagerAdapter2 adapter;

	private void setAdapter(List<ManagerBean> list) {
		int showCount = list.size() > 5 ? 5 : list.size();
		adapter = new ManagerAdapter2(this, list, showCount);
		manager_listview.setAdapter(adapter);
		progress.setVisibility(View.GONE);
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

	public void more() {
		Intent intent = new Intent(this, MoreMassagers.class);
		this.startActivity(intent);
	}

}
