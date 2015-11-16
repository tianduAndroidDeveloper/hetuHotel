package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ADInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stevenhu.android.phone.utils.ViewFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class orderActivity extends TabActivity implements OnCheckedChangeListener{
	
	
	private ImageView imgaeview;
	private RadioGroup radiogroup;
	private RadioButton manipulation,message,orders,pedicure;
	private TabHost tabhost;
	private TabWidget tabs;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderlistview);
	
		
		
		manipulation=(RadioButton) findViewById(R.id.manipulation);
		pedicure=(RadioButton) findViewById(R.id.pedicure);
		message=(RadioButton) findViewById(R.id.message);
		orders=(RadioButton) findViewById(R.id.orders);
		
		
		
		    tabs=(TabWidget) findViewById(android.R.id.tabs);
	        tabhost=(TabHost) findViewById(android.R.id.tabhost);
	        tabhost.setup();
	        tabhost.setFocusable(true);  
	        
	        
	         //推拿
	       TabHost.TabSpec tabSpec0= tabhost.newTabSpec("推拿");
	        tabSpec0.setIndicator("推拿");
	        tabSpec0.setContent(new Intent(orderActivity.this, manipulationActivty.class));
	        tabhost.addTab(tabSpec0);
	        
	        
	        //足疗
	        TabHost.TabSpec tabSpec5= tabhost.newTabSpec("足疗");
	        tabSpec5.setIndicator("足疗");
	        tabSpec5.setContent(new Intent(orderActivity.this, pedicureActivty.class));
	        tabhost.addTab(tabSpec5);
	       
		
	     
	
		  //推拿师
	        TabHost.TabSpec tabSpec2= tabhost.newTabSpec("订制");
	        tabSpec2.setIndicator("订制");
	        tabSpec2.setContent(new Intent(orderActivity.this, OrderForCustomerActivty.class));
	        tabhost.addTab(tabSpec2);
	        tabhost.setCurrentTab(0); 
		
	       
	       //订制
	        TabHost.TabSpec tabSpec4= tabhost.newTabSpec("推拿师");
	        tabSpec4.setIndicator("推拿师");
	        tabSpec4.setContent(new Intent(orderActivity.this, messageActivty.class));
	        tabhost.addTab(tabSpec4);
		
	       radiogroup=(RadioGroup) findViewById(R.id.main_radio);
	       radiogroup.setOnCheckedChangeListener(this);
	       RadioButton radioButton = (RadioButton) radiogroup.getChildAt(0);
	       radioButton.setChecked(true);
	     		
		
		
		
	}
	
	
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		//推拿
		case R.id.manipulation:
			 tabhost.setCurrentTab(0);
			break;
		
		case R.id.pedicure:
			 tabhost.setCurrentTab(1);
			break;

		case R.id.message:
			 tabhost.setCurrentTab(2);
			break;
			
		case R.id.orders:
			 tabhost.setCurrentTab(3);
			break;
		}
		
	}
	
	

	

}
