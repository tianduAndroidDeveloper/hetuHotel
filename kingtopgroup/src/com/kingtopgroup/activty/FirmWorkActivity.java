package com.kingtopgroup.activty;

import java.util.Date;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kingtogroup.utils.RegxUtils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FirmWorkActivity extends MainActionBarActivity {
	ImageView iv;
	EditText et_firm;
	EditText et_address;
	EditText et_name;
	EditText et_phone;
	EditText et_email;
	EditText et_how;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firmcowork);
		titleButton.setText("企业合作体验");
		init();
	}

	void init() {
		iv = (ImageView) findViewById(R.id.iv);
		et_firm = (EditText) findViewById(R.id.et_firm);
		et_address = (EditText) findViewById(R.id.et_address);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_email = (EditText) findViewById(R.id.et_email);
		et_how = (EditText) findViewById(R.id.et_how);

		String uri = "http://kingtopgroup.com/mobile/images/dfdss_01.jpg";
		iv.setTag(uri);
		ImageLoader.getInstance().displayImage(uri, iv);
	}

	public void commit(View v) {
		String firm = et_firm.getText().toString();
		if (TextUtils.isEmpty(firm)) {
			toastMsg("请填写公司的名称", 0);
			return;
		}

		String address = et_address.getText().toString();
		if (TextUtils.isEmpty(address)) {
			toastMsg("请填写公司的地址", 0);
			return;
		}

		String name = et_name.getText().toString();
		if (TextUtils.isEmpty(name)) {
			toastMsg("请填写您的姓名", 0);
			return;
		}

		String phone = et_phone.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			toastMsg("请填写联系电话", 0);
			return;
		} else if (!RegxUtils.isPhone(phone)) {
			toastMsg("请填写正确的手机号码", 0);
			return;
		}

		String email = et_email.getText().toString();
		if (TextUtils.isEmpty(email)) {
			toastMsg("请填写邮箱", 0);
			return;
		} else if (!RegxUtils.isEmail(email)) {
			toastMsg("请填写正确的邮箱", 0);
			return;
		}

		String how = et_how.getText().toString();
		if (TextUtils.isEmpty(how)) {
			toastMsg("请填写您是如何知道点下下的", 0);
			return;
		}

		String uid = UserBean.getUSerBean().getUid();
		RequestParams params = new RequestParams();
		params.put("CreatDate", new Date().toString());
		params.put("Id", uid);
		params.put("CompanyAddress", address);
		params.put("Mobile", phone);
		params.put("CompanyName", firm);
		params.put("ContectName", name);
		params.put("Description", how);
		params.put("Remark", "");
		params.put("Email", email);

		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/QiYeHeZou";
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject object = new JSONObject(new String(arg2));
						int returnValue = object.getInt("ReturnValue");
						if (returnValue == 0) {
							displayDialog();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("请求错误，请重试", 1);
			}
		});
	}

	Dialog dialog;

	void displayDialog() {
		Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_join, null);
		builder.setView(view);
		dialog = builder.show();

		Button btn = (Button) view.findViewById(R.id.btn_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}
	
	public void back(View v){
		finish();
	}

	@Override
	public void backButtonClick(View v) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return true;
	}

}
