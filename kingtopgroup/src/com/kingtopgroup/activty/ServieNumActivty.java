package com.kingtopgroup.activty;


import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;

import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;
import com.stevenhu.android.phone.utils.LunboImageUtil;
import com.stevenhu.android.phone.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class ServieNumActivty extends MainActionBarActivity implements OnClickListener {
	private static final String TAG = "ServieNumActivty";
	private CycleViewPager cycleViewPager;
	private RadioButton service_content, service_scope, you_must_know;
	private TextView product_item_name, product_item_price, product_item_time, product_item_begin_num;
	private ProgressBar pb;
	private TextView service_item_content, service_sub_price, produt_item_price;
	private String ApplyDescriptio = null;
	private ImageView product_item_image;
	private String XiaDanContext = null;
	private String Description = null;
	private String TuiNaContext = null;
	private TextView service_num_webview, messager_name;
	private LinearLayout main, extend;
	private RadioButton water_other, wood_other;
	JSONObject obj = null;
	String num;
	String pid;

	LinearLayout manipulations;
	// private ProgressBar service_progressbar;
	private TextView service_num_button, service_add_button, service_reduce_button, service_num_next_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_num);
		titleButton.setText("选择数量");
		// acache = ACache.get(this);
		pb = (ProgressBar) findViewById(R.id.progressBar);
		main = (LinearLayout) findViewById(R.id.main);
		Intent inten = this.getIntent();
		Bundle bundel = inten.getExtras();
		String name = bundel.getString("name");
		String time = bundel.getString("time");
		pid = bundel.getString("pid");
		num = bundel.getString("beginnum");

		String image = bundel.getString("image");
		String price = bundel.getString("price");
		String messagerDteail = bundel.getString("messagerDteail");
		String messager_names = bundel.getString("messager_name");
		if ("".equals(messagerDteail) || messagerDteail == null) {

		} else {
			manipulations = (LinearLayout) findViewById(R.id.manipulations);
			manipulations.setVisibility(View.GONE);
			messager_name = (TextView) findViewById(R.id.messager_name);
			messager_name.setText("推拿师:" + messager_names);
		}
		main = (LinearLayout) findViewById(R.id.main);
		if (name.length() > 7) {
			main.setOrientation(LinearLayout.VERTICAL);
		} else {
			main.setOrientation(LinearLayout.HORIZONTAL);
		}
		extend = (LinearLayout) findViewById(R.id.extend);
		if (getIntent().getBooleanExtra("zuliao", false)) {
			View v = LayoutInflater.from(this).inflate(R.layout.zuliao_middle, null);
			water_other = (RadioButton) v.findViewById(R.id.water_other);
			wood_other = (RadioButton) v.findViewById(R.id.wood_other);
			water_other.setChecked(true);
			wood_other.setChecked(true);
			extend.addView(v);
		}
		product_item_name = (TextView) findViewById(R.id.product_item_name);

		main.setOrientation(name.length() > 7 ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
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
		ImageLoader.getInstance().displayImage(image, product_item_image);
		//finalBitmapUtil.getFinalBitmap(this).display(product_item_image, image);

		service_content = (RadioButton) findViewById(R.id.service_content);
		service_scope = (RadioButton) findViewById(R.id.service_scope);
		you_must_know = (RadioButton) findViewById(R.id.you_must_know);
		service_item_content = (TextView) findViewById(R.id.service_item_content);
		service_num_webview = (TextView) findViewById(R.id.service_num_text);

		service_add_button = (TextView) findViewById(R.id.service_add_button);
		service_reduce_button = (TextView) findViewById(R.id.service_reduce_button);
		service_num_button = (TextView) findViewById(R.id.service_num_button);
		service_num_button.setText(num);
		service_num_next_button = (TextView) findViewById(R.id.service_num_next_button);

		produt_item_price = (TextView) findViewById(R.id.product_item_price);

		// 点击数量改变
		service_add_button.setOnClickListener(this);
		service_reduce_button.setOnClickListener(this);
		service_num_next_button.setOnClickListener(this);

		service_content.setOnClickListener(this);
		service_scope.setOnClickListener(this);
		you_must_know.setOnClickListener(this);

		getDate(pid);

		cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
		String[] imageUrls = { "http://kingtopgroup.com/mobile/images/banner01.jpg", "http://kingtopgroup.com/mobile/images/banner02.jpg", "http://kingtopgroup.com/mobile/images/banner03.jpg" };

		// 图片轮播
		LunboImageUtil lb = new LunboImageUtil();
		lb.initialize(this, imageUrls, cycleViewPager);

	}

	public JSONObject getDate(String pid) {
		pb.setVisibility(View.VISIBLE);
		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("pid", pid);
		AsyncHttpCilentUtil.getInstance().get(this, ConstanceUtil.service_item_url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String date = new String(arg2);
					Log.i(TAG, date);
					obj = new JSONObject(date);
					JSONObject obj1 = (JSONObject) obj.get("ProductInfo");
					ApplyDescriptio = obj1.getString("ApplyDescription");
					Description = obj.getString("FuWuContext");
					XiaDanContext = obj.getString("XiaDanContext");
					TuiNaContext = obj.getString("TuiNaContext");
					service_item_content.setText(Html.fromHtml(Description));
					service_num_webview.setText(Html.fromHtml(TuiNaContext));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				pb.setVisibility(View.GONE);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				pb.setVisibility(View.GONE);
				Utils.showToast(ServieNumActivty.this, "获取项目信息失败，请重试!");
			}

		});
		return obj;
	}


	@Override
	public void onClick(View arg0) {
		String count = service_num_button.getText().toString();
		String price = produt_item_price.getText().toString();// + ".00";
		price = price.substring(1, price.length());

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
			Integer nus = Integer.parseInt(count) + 1;
			float subprice = (Float.parseFloat(price)) * nus;
			service_num_button.setText(nus + "");
			service_sub_price.setText("合计:￥" + Utils.stringToFloat(subprice) + "");
			break;

		case R.id.service_reduce_button:// 数量减少按钮
			if (count.equals(num)) {
				ToastUtils.show(this, num + "人起订");
			} else {
				int nu = Integer.parseInt(count) - 1;
				float sub = Float.parseFloat(price) * nu;
				service_num_button.setText(nu + "");
				service_sub_price.setText("合计:￥" + Utils.stringToFloat(sub) + "");
			}
			break;

		case R.id.service_num_next_button:// 点击下一步
			UserBean user = UserBean.getUSerBean();
			String Uid = user.getUid();
			String BuyCount = service_num_button.getText().toString();
			String ItemIdList = pid;
			RequestParams params = null;
			params = new RequestParams();
			params.put("BuyCount", BuyCount);
			params.put("StoreRId", "0");
			params.put("couponid", "0");
			params.put("Pid", pid);
			params.put("ItemIdList", ItemIdList);
			params.put("Uid", Uid);
			pb.setVisibility(View.VISIBLE);
			AsyncHttpCilentUtil.getInstance().post("http://kingtopgroup.com/api/item/AddItemToCart", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					pb.setVisibility(View.GONE);
					if (arg0 == 200) {
						try {
							String date = new String(arg2);
							JSONObject obj = new JSONObject(date);
							int ReturnValue = obj.optInt("ReturnValue");

							if (ReturnValue == 0) {
								Utils.showToast(ServieNumActivty.this, "亲，服务器忙，请稍后重试");
							} else {
								// 保存订单号
								Intent intent = new Intent(ServieNumActivty.this, ServiceAddressActivty.class);
								intent.putExtra("opid", ReturnValue);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					pb.setVisibility(View.GONE);
					Utils.showToast(ServieNumActivty.this, "亲，服务器忙，请稍后重试");
				}

			});

			break;
		}
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
