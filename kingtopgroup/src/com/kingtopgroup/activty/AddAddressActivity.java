package com.kingtopgroup.activty;

import org.apache.http.Header;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class AddAddressActivity extends MainActionBarActivity implements
		OnClickListener {
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
		
		titleButton.setText("��ӵ�ַ");

		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String name = et_name.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(this, "��������Ϊ�գ�", Toast.LENGTH_SHORT).show();
				return;
			}
			String phone = et_phone.getText().toString();
			if (TextUtils.isEmpty(phone)) {
				Toast.makeText(this, "��ϵ�绰����Ϊ�գ�", Toast.LENGTH_SHORT).show();
				return;
			}
			String address = et_address.getText().toString();
			if (TextUtils.isEmpty(address)) {
				Toast.makeText(this, "��ϸ��ַ����Ϊ�գ�", Toast.LENGTH_SHORT).show();
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
						toastMsg("��Ϣ����������");
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						toastMsg("��ӳɹ�");
						finish();
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
