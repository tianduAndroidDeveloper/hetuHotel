package com.kingtopgroup.activty;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.Util;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.domain.Order;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayInfoActivity extends MainActionBarActivity {
	private static final String TAG = "PayInfoActivity";
	TextView tv_money;
	TextView tv_way;
	TextView tv_num;
	Order order;
	IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payinfo);
		api = WXAPIFactory.createWXAPI(this, "wx1bf532a71f3f6550");
		api.registerApp("wx1bf532a71f3f6550");
		titleButton.setText("支付信息");
		init();
	}

	byte[] buf;

	public void pay(final View v) {
		v.setEnabled(false);
		String url = "http://kingtopgroup.com/api/appwxpay/getpaypackage?" + "orderid=" + order.oid + "&uid=" + UserBean.getUSerBean().getUid() + "&mobilesystem=android";
		Log.i(TAG, url);
		AsyncHttpClient client = new AsyncHttpClient();

		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					Log.i(TAG, new String(arg2));
					JSONObject object = new JSONObject(new String(arg2));
					assembleParams(object);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				v.setEnabled(true);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.i(TAG, "onFailure");
				Log.i(TAG, new String(arg2));
				v.setEnabled(true);

			}
		});
	}

	void assembleParams(JSONObject object) {
		PayReq req = new PayReq();
		req.appId = object.optString("appid");
		req.partnerId = object.optString("partnerid");
		req.prepayId = object.optString("prepayid");
		req.nonceStr = object.optString("noncestr");
		req.timeStamp = object.optString("timestamp");
		req.packageValue = object.optString("package");
		req.sign = object.optString("sign");
		req.extData = "app data"; // optional

		Toast.makeText(PayInfoActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
	}

	public void pay2(View v) {
		final String url = "http://kingtopgroup.com/api/appwxpay/getpaypackage";
		// final String url =
		// "http://kingtopgroup.com/mob/weixinpay/WeiXinPayNotify";
		final Button payBtn = (Button) v;
		payBtn.setEnabled(false);
		Toast.makeText(this, "获取订单中...", Toast.LENGTH_SHORT).show();
		try {
			new Thread() {
				public void run() {
					buf = Util.httpGet(url);
				};
			}.start();
			if (buf != null && buf.length > 0) {
				String content = new String(buf);
				Log.e("get server pay params:", content);
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode")) {
					Log.i(TAG, json.toString());
					PayReq req = new PayReq();
					req.appId = Constants.APP_ID; // 测试用appId
					// req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
					Toast.makeText(PayInfoActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
				} else {
					Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
					Toast.makeText(PayInfoActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("PAY_GET", "服务器请求错误");
				Toast.makeText(PayInfoActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常：" + e.getMessage());
			Toast.makeText(PayInfoActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		payBtn.setEnabled(true);

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
