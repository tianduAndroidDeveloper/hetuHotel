package com.kingtopgroup.activty;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.domain.Order;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PayInfoActivity extends MainActionBarActivity {
	TextView tv_money;
	TextView tv_way;
	TextView tv_num;
	Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payinfo);
		titleButton.setText("支付信息");
		init();
	}

	void pay(View v) {
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/order/ApiPayNotify";
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("orderIdList", order.oid);
		params.put("trade_no", order.osn);
		params.put("tradeMoney", order.orderamount);
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject obj = new JSONObject(new String(arg2));
						String msg = obj.optString("ActionMessage");
						Toast.makeText(PayInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(PayInfoActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();

			}
		});
	}

	void init() {
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_way = (TextView) findViewById(R.id.tv_way);
		tv_num = (TextView) findViewById(R.id.tv_num);

		order = (Order) getIntent().getExtras().get("order");
		tv_money.setText("应付金额：￥" + order.orderamount);
		tv_num.setText("订单号：" + order.osn);
		spanText();
	}

	void spanText() {
		String text1 = tv_num.getText().toString();
		SpannableStringBuilder builder1 = new SpannableStringBuilder(text1);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);

		builder1.setSpan(blackSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder1.setSpan(graySpan, 4, text1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		tv_num.setText(builder1);

		String text2 = tv_way.getText().toString();
		SpannableStringBuilder builder2 = new SpannableStringBuilder(text2);
		builder2.setSpan(blackSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder2.setSpan(graySpan, 5, text2.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_way.setText(builder2);

		String text3 = tv_money.getText().toString();
		SpannableStringBuilder builder3 = new SpannableStringBuilder(text3);
		builder3.setSpan(blackSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder3.setSpan(redSpan, 5, text3.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv_money.setText(builder3);
	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
}
