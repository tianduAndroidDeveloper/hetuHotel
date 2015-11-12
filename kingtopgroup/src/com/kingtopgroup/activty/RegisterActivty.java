package com.kingtopgroup.activty;

import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivty extends Activity implements OnClickListener{
	private EditText username,password;
	private Button register_submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		register_submit=(Button) findViewById(R.id.register_submit);
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
		register_submit.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View arg0) {
		String name=username.getText().toString();
		String passwords=password.getText().toString();
		if(name!=null && !name.equals("") && !passwords.equals("") && passwords!=null){
			String ip="10.12.253.6";
			String getParams=AsyncHttpCilentUtil.postClien(this,ConstanceUtil.register_url+"?mobile="+name+"&password="+passwords+"&ip="+ip, null);
			if(getParams!=null){
				Intent intent=new Intent(this, indexActivity.class);
				startActivity(intent);
			}
	}else{
		ToastUtils.show(this, "用户名或密码不能为空");
	}
}
}
