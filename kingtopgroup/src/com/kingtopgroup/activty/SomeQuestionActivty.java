package com.kingtopgroup.activty;

import com.kingtopgroup.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

public class SomeQuestionActivty extends Activity{
	private WebView  some_question;
	//private String some="��ͨ��ƽ̨�µ��͵�ʵ���������ʲô���𣿵������ֻ��µ���ֻ��Ҫ��Լ�������ú��ʵ�ʱ���Լ��ص㣬����ʦ�����ṩ�������񡣴��Ϊ����Լʱ�䣬ͬʱʡȥ�˺ܶ���м价�ڣ����ǰ��ⲿ�ַ���������������󽵵�����ʵ�ʷ��á��κ�ʱ�䶼�����µ������κ�ʱ�䶼�����µ������������ṩ�����ʱ��Ϊ����10:00-����22:00���ҿ���ѡ������ʦ�𣿿��ԣ�����֧������ѡ��Ҳ����������Ϊ�����ţ�ϣ��������һ�����õķ�����̡�����Ҫ׼��ʲô��ԭ������˵������Ϊ��׼�������ж�������ֻ��Ҫ��һ�����ĵط����ɡ�����θ����أ�Ŀǰ�Ļ������ǽ��ṩ����֧������Ա�ͻ�֧��������ȡ�ֽ�  ���ϱ���è��Ϣ�������޹�˾";
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
		setContentView(R.layout.some_question);
		some_question=(WebView) findViewById(R.id.some_question);
		some_question.loadUrl("file:///android_asset/some_question.htm");
	}

}

