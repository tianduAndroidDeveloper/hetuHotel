package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kingtogroup.utils.RegxUtils;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class AddAddressActivity extends MainActionBarActivity implements
		OnClickListener {
	private static final String TAG = "AddAddressActivity";
	EditText et_name;
	EditText et_phone;
	EditText et_address;
	Button btn_ok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addaddress);
		init();
	}

	void init() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_address = (EditText) findViewById(R.id.et_address);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_ok = (Button) findViewById(R.id.btn_ok);

		titleButton.setText("添加地址");

		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String name = et_name.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			String phone = et_phone.getText().toString();
			if (TextUtils.isEmpty(phone)) {
				Toast.makeText(this, "联系电话不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!RegxUtils.isPhone(phone)) {
				Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
				return;
			}
			String address = et_address.getText().toString();
			if (TextUtils.isEmpty(address)) {
				Toast.makeText(this, "详细地址不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}

			requestAdd(name, phone, address);

			break;

		default:
			break;
		}
	}

	void requestAdd(String name, String phone, String address) {
		UserBean userBean = UserBean.getUSerBean();
		String uid = userBean.getUid();
		if (TextUtils.isEmpty(uid))
			return;
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("consignee", name);
		params.put("uid", UserBean.getUSerBean().getUid());
		params.put("regionId", "2685");
		params.put("alias", name);
		params.put("mobile", phone);
		params.put("phone", phone);
		params.put("email", "1135741892@qqq.com");
		params.put("zipcode", "655002");
		params.put("address", address);
		params.put("isDefault", "1");

		AsyncHttpCilentUtil.getInstance().post(this,
				ConstanceUtil.add_adress_url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						toastMsg("信息有误，请重试");
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.i(TAG, new String(arg2));
						if (arg0 == 200) {
							try {
								JSONObject obj = new JSONObject(
										new String(arg2));
								int returnValue = obj.getInt("ReturnValue");
								String msg = obj.getString("ActionMessage");
								toastMsg(msg);
								if (returnValue != -1) {
									finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}
				});
	}

	void toastMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(AddAddressActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

}
