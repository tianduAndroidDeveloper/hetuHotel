package com.kingtopgroup.activty;

import com.kingtopgroup.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutKingTopGroup extends Activity {
	private WebView about_king_top_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutkingtopgroup);
		about_king_top_group = (WebView) findViewById(R.id.about_kingtopgroup);
		about_king_top_group.loadUrl("file:///android_asset/about_king.htm");
	}
}
