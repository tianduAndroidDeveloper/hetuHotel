package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginActivty extends Activity implements OnClickListener{
	private EditText username,password;
	private Button loginsubmit;
	
	 private RequestParams params;
	 private SharedPreferences sp;  
	 private CheckBox auto_login;
	 
	 private TextView register_button;
	 
	 private String mobile,passwords;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		 //this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		
		
		//获得实例对象  
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
		username=(EditText) findViewById(R.id.username);
		username.setText("15287811383");
		password=(EditText) findViewById(R.id.password);
		password.setText("123456");
		auto_login = (CheckBox) findViewById(R.id.auto_login);  
		loginsubmit=(Button) findViewById(R.id.loginsubmit);
		register_button=(TextView) findViewById(R.id.register_button);
		register_button.setOnClickListener(this);
		loginsubmit.setOnClickListener(this);
		auto_login.setOnClickListener(this);
		
	
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.loginsubmit:
		   username=(EditText) findViewById(R.id.username);
			password=(EditText) findViewById(R.id.password);
			mobile=username.getText().toString();
			passwords=password.getText().toString();
			 params = new RequestParams();
			 params.put("mobile", mobile);
			 params.put("password", passwords);
			 //params.put("ip", "10.254.215.44");
			 
			 AsyncHttpClient client=new AsyncHttpClient();
			 //"http://kingtopgroup.com/api/account/login?mobile=13888973311&password=HT13888973311"
			 String post="http://kingtopgroup.com/api/account/login?mobile="+mobile+"&password="+passwords+"";
			client.post(post, null, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					if(arg0 ==200){
						 try {
							 //data =JSONObject.parse(arg1); 
							 String date=new String(arg2);
							JSONObject obj = new JSONObject(date);
							String Password=obj.getString("Password");
							String Uid=obj.getString("Uid");
							if(Password.equals("vaild")){
								Intent intens=new Intent(loginActivty.this,indexActivity.class);
								startActivity(intens);
								UserBean.getUSerBean().setUid(Uid);
							}else{
								output("用户名或密码错误");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					output("服务器忙，请稍后再试");
					
				}
			});
			
			break;
          //自动登录
		case R.id.auto_login:
			 if (auto_login.isChecked()) {  
				 setstatus(); 
			 } else {  
				 System.out.println("自动登录没有选中");  
				 sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
			 }  
			break;
			
		case R.id.register_button:
			Intent inten=new Intent(this, RegisterActivty.class);
			startActivity(inten);
			break;
		}
		
	}
	
	public String postLogin(){
		 params = new RequestParams();
		 params.put("mobile", mobile);
		 params.put("password", passwords);
		 AsyncHttpClient client=new AsyncHttpClient();
		client.post("http://kingtopgroup.com/api/account/login", params, new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		return null;
	}

	
	public void output(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	

	public Boolean setstatus(){
		 if(auto_login.isChecked())  
         {  
        //记住用户名、密码、  
			 username.setText(sp.getString("USER_NAME", ""));  
             password.setText(sp.getString("PASSWORD", "")); 
             
            mobile=username.getText().toString();
     		passwords=password.getText().toString();
     		
			 if(mobile!=null && !mobile.equals("") && passwords!=null && !passwords.equals("")){
		          postLogin();
			 }else{
				 Editor editor = sp.edit();  
		          editor.putString("USER_NAME",mobile);  
		          editor.putString("PASSWORD",passwords);  
		          editor.commit();
			 }
		
	}
		 return true;
}
}
