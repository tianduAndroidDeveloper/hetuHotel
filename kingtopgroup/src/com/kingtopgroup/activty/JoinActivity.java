package com.kingtopgroup.activty;

import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kingtogroup.utils.RegxUtils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class JoinActivity extends Activity implements OnClickListener {
	private static final String TAG = "JoinActivity";
	EditText et_name;
	RadioGroup rg_sex;
	EditText et_phone;
	EditText et_experience;
	Button btn_ok;
	ImageView iv;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		init();
	}

	void init() {
		iv = (ImageView) findViewById(R.id.iv);
		et_experience = (EditText) findViewById(R.id.et_experience);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		btn_ok = (Button) findViewById(R.id.btn_ok);

		RadioButton rb = (RadioButton) rg_sex.getChildAt(0);
		rb.setChecked(true);
		btn_ok.setOnClickListener(this);
		String uri = "http://kingtopgroup.com/mobile/images/recruit_01.jpg";
		iv.setTag(uri);
		ImageLoader.getInstance().displayImage(uri, iv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			commit();
			break;

		default:
			break;
		}
	}

	void commit() {
		String name = et_name.getText().toString();
		if (TextUtils.isEmpty(name)) {
			toastMsg("请输入姓名", 0);
			return;
		}
		String phone = et_phone.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			toastMsg("请输入手机号码", 0);
			return;
		} else if (!RegxUtils.isPhone(phone)) {
			toastMsg("请输入正确的手机号码", 0);
			return;
		}
		String experience = et_experience.getText().toString();
		if (TextUtils.isEmpty(experience)) {
			toastMsg("请填写工作经验", 0);
			return;
		}
		int checkedId = rg_sex.getCheckedRadioButtonId();
		String sex = checkedId == R.id.rb_male ? "男" : "女";
		String remark = "";
		String uid = UserBean.getUSerBean().getUid();

		RequestParams params = new RequestParams();
		params.put("CreatDate", new Date().toString());
		params.put("Id", uid);
		params.put("JingYan", experience);
		params.put("Mobile", phone);
		params.put("Name", name);
		params.put("Remark", remark);
		params.put("Sex", sex);

		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/JoinUs";
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.i(TAG, new String(arg2));
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

				Log.i(TAG, new String(arg2));
				toastMsg("请求错误，请重试", 1);
			}
		});

	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
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

}
