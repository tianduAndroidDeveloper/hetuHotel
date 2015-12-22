package com.kingtopgroup.activty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtogroup.domain.OrderImage;
import com.kingtogroup.domain.OrderInfo;
import com.kingtogroup.domain.Product;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtopgroup.R;
import com.kingtopgroup.adapter.MassagerOrderAdapter;
import com.kingtopgroup.adapter.MassagerOrderAdapter.ReceiveClickListener;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MassagerOrderActivity extends MainActionBarActivity implements ReceiveClickListener {
	private static final String TAG = "MassagerOrderActivity";
	ListView lv;
	TextView tv_empty;
	String url = "";
	View progress;
	List<OrderInfo> mOrders = new ArrayList<OrderInfo>();
	List<Product> mProducts = new ArrayList<Product>();
	MassagerOrderAdapter adapter;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massagerorders);
		init();
	}

	void init() {
		String title = getIntent().getExtras().getString("title");
		titleButton.setText(title);

		lv = (ListView) findViewById(R.id.lv);
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		progress = findViewById(R.id.progress);
	}

	protected void onResume() {
		super.onResume();
		mOrders.clear();
		mProducts.clear();
		images.clear();
		requestData();
	};

	String imgUrl = "";

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		url = getIntent().getStringExtra("url");
		AsyncHttpClient client = new AsyncHttpClient();
		url += "?massagerId=" + UserBean.getUSerBean().getMassagerId() + "&mobile=" + UserBean.getUSerBean().getAccount();
		Log.i(TAG, url);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String data = new String(arg2);
				if (data != null) {
					JSONArray orderArray = null;
					JSONArray imageArray = null;
					try {
						JSONObject object = new JSONObject(data);
						orderArray = object.optJSONArray("OrderList");
						imageArray = object.optJSONArray("MassagerOrderList");
						imgUrl = object.optString("ImgUrl");
						JSONArray productAraay = object.optJSONArray("OrderProductList");
						if (orderArray.length() > 0)
							parseToOrderList(orderArray, productAraay, imageArray);
						tv_empty.setVisibility(orderArray.length() == 0 ? View.VISIBLE : View.GONE);
						tv_empty.setText("什么都米有");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progress.setVisibility(orderArray != null && orderArray.length() > 0 ? View.VISIBLE : View.GONE);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				tv_empty.setVisibility(View.VISIBLE);
				tv_empty.setText("请求失败，请重试");
				progress.setVisibility(View.GONE);

			}
		});
	}

	List<OrderImage> images = new ArrayList<OrderImage>();

	void parseToOrderList(final JSONArray orderArray, final JSONArray productArray, final JSONArray imgArray) {
		mOrders.clear();
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				ObjectMapper om = new ObjectMapper();
				try {
					for (int i = 0; i < orderArray.length(); i++) {
						JSONObject object = orderArray.optJSONObject(i);
						OrderInfo orderInfo = om.readValue(object.toString(), OrderInfo.class);
						mOrders.add(orderInfo);
					}
					for (int i = 0; i < productArray.length(); i++) {
						JSONObject object = productArray.optJSONObject(i);
						Product product = om.readValue(object.toString(), Product.class);
						mProducts.add(product);
					}
					if (imgArray != null)
						for (int i = 0; i < imgArray.length(); i++) {
							JSONObject obj = imgArray.optJSONObject(i);
							OrderImage image = om.readValue(obj.toString(), OrderImage.class);
							images.add(image);
						}
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return mOrders;
			}

			@Override
			public void onComplete(Object parseResult) {
				if (parseResult != null) {
					getProductLists();
					fillData();
				}
			}
		}).execute();
	}

	void getProductLists() {
		for (int i = 0; i < mOrders.size(); i++) {
			OrderInfo orderInfo = mOrders.get(i);
			int oid = orderInfo.Oid;

			for (int j = 0; j < mProducts.size(); j++) {
				Product product = mProducts.get(j);
				if (product.Oid == oid) {
					orderInfo.products.add(product);
				}
			}

			for (OrderImage image : images) {
				if (image.Oid == oid)
					orderInfo.imgUrl = imgUrl + image.CompeletImage;
			}
		}
	}

	void fillData() {
		if (adapter == null) {
			adapter = new MassagerOrderAdapter(mOrders, this, titleButton.getText().equals("待接单"), this);
			lv.setAdapter(adapter);
		} else {
			adapter.refresh(mOrders);
		}
		progress.setVisibility(View.GONE);
	}

	@Override
	public void onReceiveClick(int oid) {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String url = getString(R.string.url_receive_order);

		RequestParams params = new RequestParams();
		int massagerId = UserBean.getUSerBean().getMassagerId();
		String mobile = UserBean.getUSerBean().getAccount();
		params.put("massagerId", massagerId);
		params.put("mobile", mobile);
		params.put("oid", oid);

		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String data = new String(arg2);
				if (data != null) {
					try {
						JSONObject object = new JSONObject(data);
						int returnValue = object.optInt("ReturnValue");
						String msg = object.optString("ActionMessage");

						if (returnValue == 1) {
							// 接单成功
							requestData();
						} else if (msg.contains("已接单")) {
							Toast.makeText(MassagerOrderActivity.this, "您已接单，等待其他推拿师接单", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(MassagerOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
						}
						progress.setVisibility(View.GONE);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(MassagerOrderActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);

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

	@Override
	public void onUploadClick(int oid) {
		Intent intent = new Intent(this, UploadImageActivity.class);
		intent.putExtra("oid", oid);
		this.startActivity(intent);
	}

}