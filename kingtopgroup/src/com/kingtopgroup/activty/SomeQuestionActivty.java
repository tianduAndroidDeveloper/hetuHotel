package com.kingtopgroup.activty;

import com.kingtopgroup.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

public class SomeQuestionActivty extends Activity{
	private WebView  some_question;
	//private String some="●通过平台下单和到实体店消费有什么区别？点下下手机下单，只需要您约着您觉得合适的时间以及地点，推拿师上门提供健康服务。大大为您节约时间，同时省去了很多的中间环节，我们把这部分费用让利给您，大大降低您的实际费用●任何时间都可以下单吗？您任何时间都可以下单，但是我们提供服务的时间为早上10:00-晚上22:00●我可以选择推拿师吗？可以，我们支持自由选择，也可以由我们为您安排，希望您能有一个愉悦的服务过程●我需要准备什么？原则上来说，我们为您准备了所有东西，您只需要有一个座的地方即可。●如何付费呢？目前的话，我们仅提供在线支付，会员客户支持上门收取现金。  云南笨笨猫信息技术有限公司";
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
		setContentView(R.layout.some_question);
		some_question=(WebView) findViewById(R.id.some_question);
		some_question.loadUrl("file:///android_asset/some_question.htm");
	}

}

