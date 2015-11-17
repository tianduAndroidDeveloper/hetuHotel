package com.kingtopgroup.activty;

import java.net.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AboutUSActivity extends MainActionBarActivity {
	private static final String TAG = "AboutUSActivity";
	WebView wv;
	View progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		
		titleButton.setText("关于我们");
		
		wv = (WebView) findViewById(R.id.wv);
		progress = findViewById(R.id.progress);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/home/GetAboutUs";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					JSONObject object = new JSONObject(new String(arg2));
					String html = object.getString("Context");
					wv.getSettings().setJavaScriptEnabled(true);
					wv.loadDataWithBaseURL("http://kingtopgroup.com/", html, "text/html", "utf-8", null);
				} catch (JSONException e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(AboutUSActivity.this, "信息获取失败，请重试！",
						Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);
			}
		});
	}

	ImageGetter imgGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;  
            URL url;  
            try {  
                url = new URL(source);  
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片  
            } catch (Exception e) {  
                e.printStackTrace();  
                return null;  
            }  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                    drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		// TODO Auto-generated method stub
		return true;
	}
}
