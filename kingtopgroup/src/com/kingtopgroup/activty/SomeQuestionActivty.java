package com.kingtopgroup.activty;

import com.kingtopgroup.R;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class SomeQuestionActivty extends Activity {
	private WebView some_question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.some_question);
		some_question = (WebView) findViewById(R.id.some_question);
		some_question.loadUrl("file:///android_asset/some_question.htm");
	}

}
