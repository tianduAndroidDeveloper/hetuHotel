package com.kingtopgroup.activty;

import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;

import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.constant.finalBitmapUtil;
import com.kingtopgroup.fragment.impl.FirstFram;
import com.kingtopgroup.fragment.impl.ServiceNumFragment;
import com.kingtopgroup.fragment.impl.ServiceYouMustKnowFragment;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.LunboImageUtil;
import com.stevenhu.android.phone.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ServieNumActivty extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "ServieNumActivty";
	private CycleViewPager cycleViewPager;
	private RadioButton service_content, service_scope, you_must_know;
	private TextView product_item_name, product_item_price, product_item_time,
			product_item_begin_num, product_item_need;
	private TextView service_item_content, service_sub_price,
			produt_item_price;
	private String ApplyDescriptio = null;
	private ImageView product_item_image;
	private String XiaDanContext = null;
	private String Description = null;
	private String TuiNaContext = null;
	private ACache acache;
	private WebView service_num_webview;
	JSONObject obj = null;
	// private ProgressBar service_progressbar;
	private Button service_add_button, service_reduce_button,
			service_num_button, service_num_next_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_num);
		acache = ACache.get(this);

		Intent inten = this.getIntent();
		Bundle bundel = inten.getExtras();
		String name = bundel.getString("name");
		String time = bundel.getString("time");
		String pid = bundel.getString("pid");
		String num = bundel.getString("beginnum");
		String image = bundel.getString("image");
		String price = bundel.getString("price");

		product_item_name = (TextView) findViewById(R.id.product_item_name);
		product_item_name.setText(name);
		product_item_price = (TextView) findViewById(R.id.product_item_price);
		product_item_price.setText("￥" + price);
		product_item_time = (TextView) findViewById(R.id.product_item_time);
		product_item_time.setText(time + "分钟");
		product_item_begin_num = (TextView) findViewById(R.id.product_item_num);
		product_item_begin_num.setText("（" + num + "人起订）");
		product_item_image = (ImageView) findViewById(R.id.product_item_image);
		service_sub_price = (TextView) findViewById(R.id.service_sub_price);
		service_sub_price.setText("合计：￥" + price);
		finalBitmapUtil.getFinalBitmap(this).display(product_item_image, image);

		service_content = (RadioButton) findViewById(R.id.service_content);
		service_scope = (RadioButton) findViewById(R.id.service_scope);
		you_must_know = (RadioButton) findViewById(R.id.you_must_know);
		service_item_content = (TextView) findViewById(R.id.service_item_content);
		service_num_webview = (WebView) findViewById(R.id.service_num_text);

		service_add_button = (Button) findViewById(R.id.service_add_button);
		service_reduce_button = (Button) findViewById(R.id.service_reduce_button);
		service_num_button = (Button) findViewById(R.id.service_num_button);
		service_num_next_button = (Button) findViewById(R.id.service_num_next_button);

		produt_item_price = (TextView) findViewById(R.id.product_item_price);

		// 点击数量改变
		service_add_button.setOnClickListener(this);
		service_reduce_button.setOnClickListener(this);
		service_num_next_button.setOnClickListener(this);

		service_content.setOnClickListener(this);
		service_scope.setOnClickListener(this);
		you_must_know.setOnClickListener(this);

		// getDate(pid);
		if (getDate(pid) == null) {
			if (acache.getAsJSONObject("service_num") != null) {
				ObjectToListmap(acache.getAsJSONObject("service_num"));
			}
		}

		// ==========================================进度条=============================================
		// service_progressbar=(ProgressBar)
		// findViewById(R.id.service_progressbar);
		// service_progressbar.setVisibility(View.VISIBLE);
		// service_progressbar.setMax(120);

		// --------------------------------图片循环-----------------------------------------
		// 需要循环的图片
		String[] imageUrls = {
				"http://kingtopgroup.com/mobile/images/banner01.jpg",
				"http://kingtopgroup.com/mobile/images/banner02.jpg",
				"http://kingtopgroup.com/mobile/images/banner03.jpg" };

		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
		// 图片轮播
		LunboImageUtil lb = new LunboImageUtil();
		lb.initialize(this, imageUrls, cycleViewPager);
		acache = ACache.get(this);

		// ----------------------------服务内容-------------------------------------

	}

	public JSONObject getDate(String pid) {

		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("pid", pid);

		AsyncHttpCilentUtil.getInstance().get(this,
				ConstanceUtil.service_item_url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// service_progressbar.setVisibility(View.GONE);
						try {
							String date = new String(arg2);
							obj = new JSONObject(date);
							acache.put("service_num", obj);  
							JSONObject obj1 = (JSONObject) obj
									.get("ProductInfo");
							// 适用范围
							ApplyDescriptio = obj1
									.getString("ApplyDescription");

							// 服务内容
							Description = obj.getString("FuWuContext");

							// 下单须知
							XiaDanContext = obj.getString("XiaDanContext");

							// 足疗
							TuiNaContext = obj.getString("TuiNaContext");

							service_item_content.setText(Html
									.fromHtml(Description));
							service_num_webview.getSettings()
									.setJavaScriptEnabled(true);
							// service_num_webview.setText(Html.fromHtml(TuiNaContext));
							
							service_num_webview.loadDataWithBaseURL(
									"http://kingtopgroup.com", TuiNaContext,
									"text/html", "utf-8", null);
							Log.i(TAG, TuiNaContext);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// super.onSuccess(arg0, arg1);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}

				});
		return obj;
	}

	private void ObjectToListmap(JSONObject obj) {
		JSONObject obj1;
		try {
			obj1 = (JSONObject) obj.get("ProductInfo");
			// 适用范围
			ApplyDescriptio = obj1.getString("ApplyDescription");
			// 服务内容
			Description = obj.getString("FuWuContext");
			// 下单须知
			XiaDanContext = obj.getString("XiaDanContext");
			// 足疗
			TuiNaContext = obj.getString("TuiNaContext");

			service_item_content.setText(Html.fromHtml(Description));
			service_num_webview.getSettings().setJavaScriptEnabled(true);
			// service_num_webview.setText(Html.fromHtml(TuiNaContext));
			service_num_webview.loadDataWithBaseURL("http://kingtopgroup.com",
					TuiNaContext, "text/html", "utf-8", null);
			Log.i(TAG, TuiNaContext);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		String num = service_num_button.getText().toString();
		String price = produt_item_price.getText().toString();
		int index = price.indexOf(".");
		String st = price.substring(1, index);

		switch (arg0.getId()) {
		case R.id.service_scope:// 服务范围
			service_item_content.setText(Html.fromHtml(ApplyDescriptio));
			break;

		case R.id.you_must_know:// 下单须知
			service_item_content.setText(Html.fromHtml(XiaDanContext));
			break;

		case R.id.service_content:// 服务内容
			service_item_content.setText(Html.fromHtml(Description));
			break;

		case R.id.service_add_button:// 数量增加按钮
			Integer nus = Integer.parseInt(num) + 1;
			Integer subprice = (Integer.parseInt(st)) * nus;
			service_num_button.setText(nus + "");
			service_sub_price.setText("合计:￥" + subprice + "");
			break;

		case R.id.service_reduce_button:// 数量减少按钮
			if (num.equals("1")) {
				ToastUtils.show(this, "数量不能少于1");
			} else {
				int nu = Integer.parseInt(num) - 1;
				int sub = Integer.parseInt(st) * nu;
				service_num_button.setText(nu + "");
				service_sub_price.setText("合计:￥" + sub + "");
			}
			break;

		case R.id.service_num_next_button:// 点击下一步
			UserBean user = UserBean.getUSerBean();
			String Uid = user.getUid();

			// Map copyFrom=new ;
			// new JSONObject(copyFrom).toString();

			String pid = user.getPid();
			String StoreRId = user.getStoreRId();
			String couponid = user.getCouponid();
			String BuyCount = service_num_button.getText().toString();
			String ItemIdList = user.getPid();
			// String
			// parans="?Uid="+Uid+"&Pid="+pid+"&BuyCount="+BuyCount+"&StoreRId="+StoreRId+"&couponid="+couponid+"&ItemIdList="+ItemIdList;
			RequestParams params = null;
			params = new RequestParams();
			String parans = "{\"Pid\":" + pid + ",\"Uid\":" + Uid
					+ ",\"BuyCount\":" + BuyCount + ",\"StoreRId\":" + StoreRId
					+ ",\"couponid\":" + couponid + ",\"ItemIdList\":"
					+ ItemIdList + "}";
			// params.put("model", parans);
			params.put("BuyCount", BuyCount);
			UserBean.getUSerBean().setBuyCount(BuyCount);
			params.put("StoreRId", StoreRId);
			params.put("couponid", couponid);
			params.put("Pid", pid);
			params.put("ItemIdList", ItemIdList);
			params.put("Uid", Uid);
			// params.put("Content-Type", "application/json; charset=utf-8");
			AsyncHttpCilentUtil.getInstance().post(
					"http://kingtopgroup.com/api/item/AddItemToCart", params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								try {
									String date = new String(arg2);
									JSONObject obj = new JSONObject(date);
									String ReturnValue = obj
											.getString("ReturnValue");
									if (ReturnValue.equals("0")) {
										// ToastUtils.show(this,
										// "亲，服务器忙，请稍后重试");
									} else {
										// 保存订单号
										UserBean.getUSerBean().setOpid(
												ReturnValue);
										
										Intent intent = new Intent(ServieNumActivty.this, ServiceAddressActivty.class);
										startActivity(intent);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							// super.onSuccess(arg0, arg1);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub

						}

					});
			
			break;
		}
	}

}
