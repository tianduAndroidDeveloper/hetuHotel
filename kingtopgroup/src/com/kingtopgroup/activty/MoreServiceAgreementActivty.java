package com.kingtopgroup.activty;

import com.kingtopgroup.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MoreServiceAgreementActivty extends Activity{
	private WebView more_service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_service);
		more_service=(WebView) findViewById(R.id.more_service);
		//more_service.loadUrl("file///:android_asset/about_king.htm");
		more_service.loadUrl("file:///android_asset/servic_agreenment.htm");
	}
	
}
 