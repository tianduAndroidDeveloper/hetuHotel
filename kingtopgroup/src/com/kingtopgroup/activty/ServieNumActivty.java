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

public class ServieNumActivty extends MainActionBarActivity implements
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
	private TextView service_num_webview;
	JSONObject obj = null;
	// private ProgressBar service_progressbar;
	private TextView service_num_button,service_add_button, service_reduce_button, service_num_next_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_num);
		titleButton.setText("ѡ������");
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
		product_item_price.setText("��" + price);
		product_item_time = (TextView) findViewById(R.id.product_item_time);
		product_item_time.setText(time + "����");
		product_item_begin_num = (TextView) findViewById(R.id.product_item_num);
		product_item_begin_num.setText("��" + num + "���𶩣�");
		product_item_image = (ImageView) findViewById(R.id.product_item_image);
		service_sub_price = (TextView) findViewById(R.id.service_sub_price);
		service_sub_price.setText("�ϼƣ���" + price);
		finalBitmapUtil.getFinalBitmap(this).display(product_item_image, image);

		service_content = (RadioButton) findViewById(R.id.service_content);
		service_scope = (RadioButton) findViewById(R.id.service_scope);
		you_must_know = (RadioButton) findViewById(R.id.you_must_know);
		service_item_content = (TextView) findViewById(R.id.service_item_content);
		service_num_webview = (TextView) findViewById(R.id.service_num_text);

		service_add_button = (TextView) findViewById(R.id.service_add_button);
		service_reduce_button = (TextView) findViewById(R.id.service_reduce_button);
		service_num_button = (TextView) findViewById(R.id.service_num_button);
		service_num_next_button = (TextView) findViewById(R.id.service_num_next_button);

		produt_item_price = (TextView) findViewById(R.id.product_item_price);

		// ��������ı�
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

		// ==========================================������=============================================
		// service_progressbar=(ProgressBar)
		// findViewById(R.id.service_progressbar);
		// service_progressbar.setVisibility(View.VISIBLE);
		// service_progressbar.setMax(120);

		// --------------------------------ͼƬѭ��-----------------------------------------
		// ��Ҫѭ����ͼƬ
		String[] imageUrls = {
				"http://kingtopgroup.com/mobile/images/banner01.jpg",
				"http://kingtopgroup.com/mobile/images/banner02.jpg",
				"http://kingtopgroup.com/mobile/images/banner03.jpg" };

		cycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
		// ͼƬ�ֲ�
		LunboImageUtil lb = new LunboImageUtil();
		lb.initialize(this, imageUrls, cycleViewPager);
		acache = ACache.get(this);

		// ----------------------------��������-------------------------------------

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
							// ���÷�Χ
							ApplyDescriptio = obj1
									.getString("ApplyDescription");

							// ��������
							Description = obj.getString("FuWuContext");

							// �µ���֪
							XiaDanContext = obj.getString("XiaDanContext");

							// ����
							TuiNaContext = obj.getString("TuiNaContext");

							service_item_content.setText(Html
									.fromHtml(Description));
							
							 service_num_webview.setText(Html.fromHtml(TuiNaContext));
							
							
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
			// ���÷�Χ
			ApplyDescriptio = obj1.getString("ApplyDescription");
			// ��������
			Description = obj.getString("FuWuContext");
			// �µ���֪
			XiaDanContext = obj.getString("XiaDanContext");
			// ����
			TuiNaContext = obj.getString("TuiNaContext");

			service_item_content.setText(Html.fromHtml(Description));
			 service_num_webview.setText(Html.fromHtml(TuiNaContext));
			Log.i(TAG, TuiNaContext);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		String num = service_num_button.getText().toString();
		String price = produt_item_price.getText().toString() + ".00";
		int index = price.indexOf(".");
		if(index!=-1){
			price = price.substring(1, index);
		}else{
			price = price.substring(1, price.length());
		}
		

		switch (arg0.getId()) {
		case R.id.service_scope:// ����Χ
			service_item_content.setText(Html.fromHtml(ApplyDescriptio));
			break;

		case R.id.you_must_know:// �µ���֪
			service_item_content.setText(Html.fromHtml(XiaDanContext));
			break;

		case R.id.service_content:// ��������
			service_item_content.setText(Html.fromHtml(Description));
			break;

		case R.id.service_add_button:// �������Ӱ�ť
			Integer nus = Integer.parseInt(num) + 1;
			Integer subprice = (Integer.parseInt(price)) * nus;
			service_num_button.setText(nus + "");
			service_sub_price.setText("�ϼ�:��" + subprice + "");
			break;

		case R.id.service_reduce_button:// �������ٰ�ť
			if (num.equals("1")) {
				ToastUtils.show(this, "������������1");
			} else {
				int nu = Integer.parseInt(num) - 1;
				int sub = Integer.parseInt(price) * nu;
				service_num_button.setText(nu + "");
				service_sub_price.setText("�ϼ�:��" + sub + "");
			}
			break;

		case R.id.service_num_next_button:// �����һ��
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
										// "�ף�������æ�����Ժ�����");
									} else {
										// ���涩����
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
