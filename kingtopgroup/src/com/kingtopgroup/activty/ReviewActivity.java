package com.kingtopgroup.activty;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ReviewActivity extends MainActionBarActivity {
	private static final String TAG = "ReviewActivity";
	
	String uid;
	String oid;
	String recordId;
	RatingBar rb1;
	RatingBar rb2;
	RatingBar rb3;
	EditText et_review;
	LinearLayout items;
	ArrayList<RadioButton> rbs ;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		titleButton.setText("评价");
		init();
	}

	void init() {
		items = (LinearLayout) findViewById(R.id.line1);
		
		et_review = (EditText) findViewById(R.id.et_review);
		rb1 = (RatingBar) findViewById(R.id.rb_star1);
		rb2 = (RatingBar) findViewById(R.id.rb_star2);
		rb3 = (RatingBar) findViewById(R.id.rb_star3);
		requestData();
	}

	void requestData() {
		AsyncHttpClient client = new AsyncHttpClient();
		uid = UserBean.getUSerBean().getUid();
		oid = getIntent().getStringExtra("oid");
		String url = "http://kingtopgroup.com/api/ucenter/ReviewOrder?uid=" + uid + "&oid=" + oid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				try {
					JSONObject object = new JSONObject(new String(arg2));
					JSONObject orderInfo = object.optJSONObject("OrderInfo");
					//double money = orderInfo.optDouble("OrderAmount");
					//tv_money.setText("￥" + String.valueOf(money));
					JSONArray array = object.optJSONArray("OrderProductList");
					if(array != null){
						initProductList(array);
					}
					/*for (int i = 0; i < array.length(); i++) {
						JSONObject product = array.optJSONObject(i);
						recordId = product.optString("RecordId");
					}*/
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}
		});
	}

	protected void initProductList(JSONArray array) {
		rbs = new ArrayList<RadioButton>();
		for(int i=0;i<array.length();i++){
			JSONObject obj = array.optJSONObject(i);
			if(obj==null)
				continue;
			View v = LayoutInflater.from(this).inflate(R.layout.activity_review_item, null);
			RadioButton rb = (RadioButton) v.findViewById(R.id.checkbox);
			rb.setTag(obj);
			rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						for(RadioButton rb:rbs){
							if(rb!=buttonView){
								rb.setChecked(false);
							}
						}
					}
					
				}
			});
			rbs.add(rb);
			final ImageView iv = (ImageView) v.findViewById(R.id.iv);
			TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
			TextView tv_money = (TextView) v.findViewById(R.id.tv_money);
			if(i==0){
				rb.setChecked(true);
				
			}
			ImageLoader.getInstance().displayImage(getString(R.string.url_imgHost)+obj.optString("ShowImg").trim(), iv
				);
			tv_type.setText(obj.optString("Name").trim());
			tv_money.setText(obj.optString("ShopPrice"));
			items.addView(v);
		}
		
	}

	public void commit(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		int star1 = (int)rb1.getRating();
		int star2 = (int)rb2.getRating();
		int star3 = (int)rb3.getRating();
		String message = et_review.getText().toString();
		if(rbs==null){
			Utils.showToast(this, "没有选择商品，请重试!");
			return;
		}
		for(RadioButton rb:rbs){
			if(rb.isChecked()){
				try{
				recordId = ((JSONObject)rb.getTag()).optString("RecordId");
				}catch(Exception e){
					e.printStackTrace();
				}
				break;
			}
		}
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("oid", oid);// 订单id
		params.put("recordId", recordId);// 订单商品记录id
		params.put("star", star1);// 星星
		params.put("message", message);// 评价内容
		params.put("descriptionStar", star2);// 项目服务星星
		params.put("serviceStar", star3);// 推拿师服务星星
		String url = "http://kingtopgroup.com/api/ucenter/AddReviewOrder";
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject object = new JSONObject(new String(arg2));
						String msg = object.optString("ActionMessage");
						int returnValue = object.optInt("ReturnValue");
						Toast.makeText(ReviewActivity.this, msg, Toast.LENGTH_SHORT).show();
						//if (returnValue != 0)
						setResult(200);
							finish();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.i(TAG, new String(arg2));

			}
		});
	}

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

		return true;
	}

}
