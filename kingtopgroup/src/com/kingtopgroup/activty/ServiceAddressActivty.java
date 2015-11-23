package com.kingtopgroup.activty;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aps.ad;
import com.kingtogroup.domain.ShipAddress;
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ning.http.client.AsyncHttpClient;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class ServiceAddressActivty extends MainActionBarActivity implements
		OnClickListener {
	private TextView service_phone, service_address_street,
			service_address_streets_num, service_address_person,
			service_address_mark;
	private LinearLayout add_or_reduce;
	private LinearLayout add_address;
	Map<String, Object> map = null;
	private TextView service_address_name, service_address_phone,
			service_address_address;
	private Button service_address_next_button;
	private RadioButton service_address_for_me, service_address_for_other;
	private ProgressBar pb;
	private String opid,serviceSelf = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_address);
		titleButton.setText("选择地址");
		opid = getIntent().getStringExtra("opid");
		// 为他人预约
		service_address_for_other = (RadioButton) findViewById(R.id.for_other);
		service_address_for_other.setOnClickListener(this);

		// 为自己预约
		service_address_for_me = (RadioButton) findViewById(R.id.for_me);
		service_address_for_me.setOnClickListener(this);
		service_address_for_me.setChecked(true);

		// 下一步按钮
		service_address_next_button = (Button) findViewById(R.id.service_address_next_button);
		service_address_next_button.setOnClickListener(this);

		add_or_reduce = (LinearLayout) findViewById(R.id.add_or_reduce);
		add_or_reduce.setOnClickListener(this);

		// 手机号
		service_phone = (TextView) findViewById(R.id.service_phone);

		// 街道
		service_address_street = (TextView) findViewById(R.id.service_address_street);

		// 门牌号
		service_address_streets_num = (TextView) findViewById(R.id.service_address_door_num);

		// 联系人
		service_address_person = (TextView) findViewById(R.id.service_address_person);

		// 备注
		service_address_mark = (TextView) findViewById(R.id.service_address_mark);

		add_address = (LinearLayout) findViewById(R.id.add_address);

		service_address_name = (TextView) findViewById(R.id.service_address_name);

		service_address_phone = (TextView) findViewById(R.id.service_address_phone);

		service_address_address = (TextView) findViewById(R.id.service_address_address);
		pb = (ProgressBar) findViewById(R.id.progressBar);
		//add_address.setOnClickListener(this);
		getDate();

	}

	private void setDate(Map<String, Object> map) {
		service_phone.setText((CharSequence) map.get("phone"));
		service_address_street.setText((CharSequence) map.get("street"));
		service_address_person.setText((CharSequence) map.get("person"));

		service_address_name.setText((CharSequence) map.get("person"));
		service_address_phone.setText((CharSequence) map.get("phone"));
		service_address_address.setText((CharSequence) map.get("street"));
		
		ShipAddress address = new ShipAddress();
		address.Consignee = (String) map.get("person");
		address.Phone = (String) map.get("phone");
		address.Address = (String) map.get("street");
		
		UserBean.getUSerBean().putAddress(address);

	}

	private Map<String, Object> getDate() {
		// 获取默认地址
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("uid", UserBean.getUSerBean().getUid());
		pb.setVisibility(View.VISIBLE);
		AsyncHttpCilentUtil.getInstance().get(this,
				ConstanceUtil.get_address_list_url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						pb.setVisibility(View.GONE);
						if (arg0 == 200) {
							try {
								String date = new String(arg2);
								JSONObject obj = new JSONObject(date);
								JSONArray array = obj
										.getJSONArray("ShipAddressList");
								for (int i = 0; i < array.length(); i++) {
									String IsDefault = array.getJSONObject(i)
											.getString("IsDefault");
									if (IsDefault.equals("1")) {
										String phone = array.getJSONObject(i)
												.getString("Mobile");
										String street = array.getJSONObject(i)
												.getString("Address");
										String person = array.getJSONObject(i)
												.getString("Consignee");
										map = new HashMap<String, Object>();
										map.put("phone", phone);
										map.put("street", street);
										map.put("person", person);
										// service_phone.setText(phone);
										setDate(map);
									}

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						// super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						pb.setVisibility(View.GONE);
						//Utils.showToast(ServiceAddressActivty.this, "网络请求出错，请重试！");
					}
				});

		return map;
	}

	private Boolean isEmpty() {
		Boolean isnull = false;
		if (!StringUtils.isEmpty(service_phone.getText())) {
			if (!StringUtils.isEmpty(service_address_street.getText())) {
				if (!StringUtils.isEmpty(service_address_streets_num.getText())) {
					if (!StringUtils.isEmpty(service_address_person.getText())) {
						isnull = true;
					} else {
						ToastUtils.show(this, "联系人不能为空");
					}
				} else {
					ToastUtils.show(this, "门牌号不能为空");
				}
			} else {
				ToastUtils.show(this, "街道不能为空");
			}
		} else {
			ToastUtils.show(this, "手机号不能为空");
		}
		return isnull;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.for_other:
			// add_or_reduce.setVisibility(View.GONE);
			serviceSelf = "0";
			break;

		case R.id.for_me:// 隐藏
			serviceSelf = "1";
			//add_or_reduce.setVisibility(View.VISIBLE);
			break;

		case R.id.service_address_next_button:
			if (isEmpty()) {
				doPost();
				/*Intent intent = new Intent(this, OrderTimeActivty.class);
				startActivity(intent);*/
			}
			break;

		/*case R.id.add_address:
			break;*/
		case R.id.add_or_reduce:
			Intent inten = new Intent(this, AddAddressActivty.class);

			startActivityForResult(inten, 0);
			break;

		}

	}
	
	private void doPost(){
		RequestParams rp = new RequestParams();
		rp.add("uid", UserBean.getUSerBean().getUid());
		rp.add("opid", this.opid);
		rp.add("mobile", service_phone.getText().toString());
		rp.add("address", service_address_street.getText().toString());
		rp.add("doorno", service_address_streets_num.getText().toString());
		rp.add("consignee", service_address_person.getText().toString());
		rp.add("remark", service_address_mark.getText().toString());
		rp.add("seviceself", serviceSelf);
		AsyncHttpCilentUtil.getInstance().post(ConstanceUtil.ser_service_address, rp, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				try {
					JSONObject obj = new JSONObject(new String(arg2));
					if(obj.optInt("ReturnValue") > 0){
						Intent intent = new Intent(ServiceAddressActivty.this, OrderTimeActivty.class);
						intent.putExtra("opid", opid);
						startActivity(intent);
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Utils.showToast(ServiceAddressActivty.this, "设置服务地址失败");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Utils.showToast(ServiceAddressActivty.this, "设置服务地址失败");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			ShipAddress address = (ShipAddress) data.getExtras().get("address");
			
			service_address_name.setText(address.Consignee);
			service_address_phone.setText(address.Phone);
			service_address_address.setText(address.Address);
			service_phone.setText(address.Phone);
			service_address_street.setText(address.Address);
			service_address_person.setText(address.Consignee);
			
			UserBean.getUSerBean().putAddress(address);
		}

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
