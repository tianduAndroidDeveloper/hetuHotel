package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kingtogroup.domain.ShipAddress;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class EditAddressActivity extends MainActionBarActivity implements OnClickListener {
	private static final String TAG = "EditAddressActivity";
	EditText et_address;
	EditText et_name;
	EditText et_phone;
	Button btn_save;
	Button btn_delete;
	ShipAddress address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_address);
		
		titleButton.setText("修改地址");
		
		init();
	}

	void init() {
		address = (ShipAddress) getIntent().getExtras().getSerializable(
				"Address");

		et_address = (EditText) findViewById(R.id.et_address);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_delete = (Button) findViewById(R.id.btn_delete);

		et_address.setText(address.Address);
		et_name.setText(address.Consignee);
		et_phone.setText(address.Mobile);
		btn_delete.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			requestSave();
			break;
		case R.id.btn_delete:
			requestDelete();
			break;
		default:
			break;
		}
	}

	void requestSave() {
		String name = et_name.getText().toString();
		String phone = et_phone.getText().toString();
		String detail = et_address.getText().toString();
		if (TextUtils.isEmpty(name)) {
			toastMsg("请输入姓名");
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			toastMsg("请输入联系电话");
			return;
		}
		if (TextUtils.isEmpty(detail)) {
			toastMsg("请输入详细地址");
			return;
		}
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("consignee", name);
		params.put("uid", UserBean.getUSerBean().getUid());
		params.put("regionId", "2685");
		params.put("alias", name);
		params.put("mobile", phone);
		params.put("phone", phone);
		params.put("email", "1135741892@qqq.com");
		params.put("zipcode", "655002");
		params.put("address", detail);
		params.put("isDefault", "1");
		params.put("said", address.SAId);
		String url = "http://kingtopgroup.com/api/ucenter/EditShipAddress";
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.i(TAG, new String(arg2));
				try {
					JSONObject object = new JSONObject(new String(arg2));
					int returnValue = object.getInt("ReturnValue");
					if (returnValue == 1) {
						toastMsg("修改成功");
						finish();
					} else if (returnValue == -1) {
						toastMsg("信息有误，请重试");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("信息有误，请重试！");
			}
		});
	}

	void requestDelete() {
		UserBean userBean = UserBean.getUSerBean();
		String uid = userBean.getUid();
		if (TextUtils.isEmpty(uid))
			return;
		AsyncHttpClient client = new AsyncHttpClient();

		String url = "http://kingtopgroup.com/api/ucenter/DelShipAddress?uid="
				+ uid + "&said=" + address.SAId;

		client.post(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject object = new JSONObject(new String(arg2));
					int returnValue = object.getInt("ReturnValue");
					if (returnValue == 1) {
						toastMsg("删除成功！");
						finish();
					} else {
						toastMsg("信息有误，请重试！");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("信息有误，请重试！");
			}
		});
	}

	void toastMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(EditAddressActivity.this, msg,
						Toast.LENGTH_SHORT).show();
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
