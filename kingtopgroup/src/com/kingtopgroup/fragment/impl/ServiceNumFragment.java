package com.kingtopgroup.fragment.impl;

import com.kingtopgroup.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ServiceNumFragment extends Fragment{
	private WebView service_num;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.my_webview, container, false);
		
		service_num=(WebView) view.findViewById(R.id.my_webview);
		//more_service.loadUrl("file///:android_asset/about_king.htm");
		service_num.loadUrl("file:///android_asset/service_scope.htm");
		return view;
	}

}
