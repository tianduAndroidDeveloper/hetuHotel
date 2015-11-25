package com.kingtopgroup.activty;

import java.util.List;


import com.kimgtopgroup.application.AgentApplication;
import com.kingtopgroup.R;
import com.kingtopgroup.fragment.impl.BackHandledFragment;
import com.kingtopgroup.fragment.impl.FirstFram;
import com.kingtopgroup.fragment.inter.BackHandledInterface;
import com.stevenhu.android.phone.utils.CustomProgressDialog;
import com.stevenhu.android.phone.utils.SystemConntection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AddKingTopGroupActivty extends FragmentActivity implements BackHandledInterface{
	private WebView cooper_webview;
	private SwipeRefreshLayout my_fresh;
	private BackHandledFragment backHandled;
	private CustomProgressDialog progressDialog = null;
	private MainFrameTask mMainFrameTask = null;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.cooper_webview);
	AgentApplication app=(AgentApplication) getApplication();
	 app.activities.add(this);
	 
	 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		     WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 
	if(SystemConntection.checkNetworkAvailable(AddKingTopGroupActivty.this)){
		//返回true说明联网
	my_fresh=(SwipeRefreshLayout) findViewById(R.id.cooper_fresh);
	cooper_webview=(WebView) findViewById(R.id.cooper_webview);
	cooper_webview.loadUrl("http://tmsgroup.cn/mob/home/JoinUs");
	cooper_webview.getSettings().setSupportZoom(true);
	cooper_webview.getSettings().setBuiltInZoomControls(true);
	cooper_webview.getSettings().setJavaScriptEnabled(true);
	
	 mMainFrameTask = new MainFrameTask(this);
     mMainFrameTask.execute();
	
	
     my_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			cooper_webview.loadUrl(cooper_webview.getUrl());
		}
	});
	
	my_fresh.setColorScheme(R.color.holo_blue_bright,  
				                R.color.holo_green_light, R.color.holo_orange_light,  
				                R.color.holo_red_light);
	
	cooper_webview.setWebChromeClient(new WebChromeClient(){
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if(newProgress==100){
				my_fresh.setRefreshing(false);
			}else{
				if(!my_fresh.isRefreshing()){
					my_fresh.setRefreshing(true);
				}
			}
			super.onProgressChanged(view, newProgress);
		}
	});
	
	
	cooper_webview.setWebViewClient(new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			   view.loadUrl(url);
               return true;
		}
		
	/*	public void onPageFinished(WebView view, String url) {  
            // Log.i(TAG, "Finished loading URL: " +url);  
			stopProgressDialog();
         } */
	});
	
	
	cooper_webview.setWebViewClient(new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//view.loadUrl("http://116.55.230.207:8008/touch/cart");
			return false;
		}
		   public void onPageFinished(WebView view, String url) {  
              // Log.i(TAG, "Finished loading URL: " +url);  
			   stopProgressDialog();
           } 
	});
	
	
	
	}else{
		//返回false说明没有联网
		//first_fram=(FrameLayout) findViewById(R.id.first_frame);
		FirstFram firstframe=new FirstFram();
		getSupportFragmentManager().beginTransaction().add(R.id.my_frame, firstframe).commit();
	}
}

@Override
protected void onDestroy() {
	 stopProgressDialog();
     
     if (mMainFrameTask != null && !mMainFrameTask.isCancelled()){
         mMainFrameTask.cancel(true);
     }
	super.onDestroy();
}
@Override
public void selectFragmen(BackHandledFragment backHandledFragment) {
       this.backHandled=backHandledFragment;	
}

@Override
public void onBackPressed() {
	if(backHandled==null || !backHandled.onBackPressed()){
		if(cooper_webview==null){
			if(getSupportFragmentManager().getBackStackEntryCount()==0){
				AgentApplication app = (AgentApplication) getApplication();
				List<Activity> activities = app.activities;
				for(Activity act:activities){
					act.finish();//显式结束
				}
			}else{
				getSupportFragmentManager().popBackStack(); 
			}
		}else{
			if(cooper_webview.canGoBack()){
				cooper_webview.goBack();
			}else if(getSupportFragmentManager().getBackStackEntryCount()==0){
				AgentApplication app = (AgentApplication) getApplication();
				List<Activity> activities = app.activities;
				for(Activity act:activities){
					act.finish();//显式结束
				}
			}else {
				getSupportFragmentManager().popBackStack(); 
			}
		}
	}
		}




private void startProgressDialog(){
    if (progressDialog == null){
        progressDialog = CustomProgressDialog.createDialog(this);
        progressDialog.setMessage("正在加载中...");
    }
     
    progressDialog.show();
}
 
private void stopProgressDialog(){
    if (progressDialog != null){
        progressDialog.dismiss();
        progressDialog = null;
    }
}




public class MainFrameTask extends AsyncTask<Integer, String, Integer>{
    AddKingTopGroupActivty mainFrame = null;
     
    public MainFrameTask(AddKingTopGroupActivty mainFrame){
        this.mainFrame = mainFrame;
    }
     
    @Override
    protected void onCancelled() {
        stopProgressDialog();
        super.onCancelled();
    }

    @Override
    protected Integer doInBackground(Integer... params) {
    	 try {
             Thread.sleep(50 * 1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         return null;
    }
         
    @Override
    protected void onPreExecute() {
        startProgressDialog();
    }

    @Override
    protected void onPostExecute(Integer result) {
        stopProgressDialog();
    }
         
}
}
