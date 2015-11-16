package com.stevenhu.android.phone.utils;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;





import android.content.Context;


public class AsyncHttpCilentUtil {
	private static AsyncHttpClient client;
	private static RequestParams params;
	private static String url=null;
	public static String getParams=null;
	

	public static AsyncHttpClient getInstance() {
		if (client == null) {
			client = new AsyncHttpClient();
			//PersistentCookieStore myCookieStore = new PersistentCookieStore(paramContext);
			//client.setCookieStore(myCookieStore);
		}
		return client;
	}
	
	/* static
	 	    {
		        client.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s
	 	    }*/
	
	public static RequestParams getParams(){
		if(params==null){
			params=new RequestParams();
		}
		return params;
	}
	
	public static String postClien(Context context,String url,RequestParams params) {
		getInstance().post(context, url, params, new AsyncHttpResponseHandler(){
			/*@Override
			public void onSuccess(int arg0, String arg1) {
				if(arg0==200){
					getParams=arg1;
				}
				super.onSuccess(arg0, arg1);
			}*/
			
		/*	@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
			}
*/
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					String date=new String(arg2);
					getParams=date;
				}
				
			}
		});
		
		return getParams;

	}
	
	public static void getAsynClient(Context context,String url,RequestParams params){
		getInstance().get(context,url, params, new AsyncHttpResponseHandler(){

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
	}
	
	public static String getClient(Context context,String url,RequestParams params){
		getInstance().get(context,url, params, new AsyncHttpResponseHandler(){
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if(arg0==200){
					String date=new String(arg2);
					getParams=date;
				}
				
			}
		
		});
		
		
		return getParams;
	}
	
	

}
