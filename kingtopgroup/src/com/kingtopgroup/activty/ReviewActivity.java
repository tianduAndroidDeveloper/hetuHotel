package com.kingtopgroup.activty;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReviewActivity extends MainActionBarActivity {
	private static final String TAG = "ReviewActivity";
	TextView tv_money;
	String uid;
	String oid;
	String recordId;
	RatingBar rb1;
	RatingBar rb2;
	RatingBar rb3;
	EditText et_review;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		titleButton.setText("评价");
		init();
	}

	void init() {
		tv_money = (TextView) findViewById(R.id.tv_money);
		et_review = (EditText) findViewById(R.id.et_review);
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
					double money = orderInfo.optDouble("OrderAmount");
					tv_money.setText("￥" + String.valueOf(money));
					JSONArray array = object.optJSONArray("OrderProductList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject product = array.optJSONObject(i);
						recordId = product.optString("RecordId");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}
		});
	}

	public void commit(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		int star1 = rb1.getNumStars();
		int star2 = rb2.getNumStars();
		int star3 = rb3.getNumStars();
		String message = et_review.getText().toString();
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
						if (returnValue != 0)
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
