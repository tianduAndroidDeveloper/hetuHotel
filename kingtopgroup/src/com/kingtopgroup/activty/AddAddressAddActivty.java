package com.kingtopgroup.activty;

import java.util.Map;

import org.apache.http.Header;

import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.StringUtils;
import com.stevenhu.android.phone.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddAddressAddActivty extends Activity implements OnClickListener {
	private EditText name, phone, detail_address;
	private Button add_button;
	Map<String, Object> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address_add);
		
		name = (EditText) findViewById(R.id.name);
		phone = (EditText) findViewById(R.id.phone);
		detail_address = (EditText) findViewById(R.id.detail_address);
		add_button = (Button) findViewById(R.id.add_button);
		
		add_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// ��ϵ��
		String consignee = name.getText().toString();
		String mobile = phone.getText().toString();
		String address = detail_address.getText().toString();

		if (!StringUtils.isEmpty(consignee)) {
			if (!StringUtils.isEmpty(mobile)) {
				if (!StringUtils.isEmpty(address)) {
					RequestParams params = AsyncHttpCilentUtil.getParams();
					params.put("consignee", consignee);
					params.put("uid", UserBean.getUSerBean().getUid());
					params.put("regionId", "2685");
					params.put("alias", consignee);
					params.put("mobile", mobile);
					params.put("phone", mobile);
					params.put("email", "1135741892@qqq.com");
					params.put("zipcode", "655002");
					params.put("address", address);
					params.put("isDefault", "1");

					AsyncHttpCilentUtil.getInstance().post(this, ConstanceUtil.add_adress_url, params, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

						}

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						}
					});

				} else {
					ToastUtils.show(this, "��ϸ��ַ����Ϊ��");
				}
			} else {
				ToastUtils.show(this, "�ֻ��Ų���Ϊ��");
			}
		} else {
			ToastUtils.show(this, "��ϵ�˲���Ϊ��");
		}
	}

}
