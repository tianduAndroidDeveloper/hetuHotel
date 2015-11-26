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

public class MyActivty extends FragmentActivity  implements BackHandledInterface{
	private WebView my_webview;
	private SwipeRefreshLayout my_fresh;
	private BackHandledFragment backHandled;
	private CustomProgressDialog progressDialog = null;
	private MainFrameTask mMainFrameTask = null;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.my_webview);
	AgentApplication app=(AgentApplication) getApplication();
	 app.activities.add(this);
	 
	 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		     WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 
	if(SystemConntection.checkNetworkAvailable(MyActivty.this)){
		//返回true说明联网
	my_fresh=(SwipeRefreshLayout) findViewById(R.id.my_fresh);
	my_webview=(WebView) findViewById(R.id.my_webview);
	my_webview.loadUrl("http://tmsgroup.cn/mob/ucenter");
	my_webview.getSettings().setSupportZoom(true);
	my_webview.getSettings().setBuiltInZoomControls(true);
	my_webview.getSettings().setJavaScriptEnabled(true);
	
	 mMainFrameTask = new MainFrameTask(this);
     mMainFrameTask.execute();
	
	// final AlertDialog alertDialog = new AlertDialog.Builder(this).create();  
	 //bar = ProgressDialog.show(meActivty.this, "请稍候，正在加载", "Loading..."); 
	
	my_fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
		@Override
		public void onRefresh() {
			my_webview.loadUrl(my_webview.getUrl());
		}
	});
	
	my_fresh.setColorScheme(R.color.holo_blue_bright,  
				                R.color.holo_green_light, R.color.holo_orange_light,  
				                R.color.holo_red_light);
	
	my_webview.setWebChromeClient(new WebChromeClient(){
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
	
	
	my_webview.setWebViewClient(new WebViewClient(){
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
	
	
	my_webview.setWebViewClient(new WebViewClient(){
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
/*@Override
public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
	if(keyCode == KeyEvent.KEYCODE_BACK && 	my_webview.canGoBack()){
		my_webview.goBack();
		return true;
	}else{
		return false;
	}
}*/



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
		if(my_webview==null){
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
			if(my_webview.canGoBack()){
				my_webview.goBack();
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
    MyActivty mainFrame = null;
     
    public MainFrameTask(MyActivty mainFrame){
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
