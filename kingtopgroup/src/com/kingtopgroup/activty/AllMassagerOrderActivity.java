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
import com.kingtopgroup.adapter.MassagerOrderAdapter2;
import com.kingtopgroup.adapter.MassagerOrderAdapter2.ReceiveClickListener;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AllMassagerOrderActivity extends MainActionBarActivity implements ReceiveClickListener {
	private static final String TAG = "RecevingActivity";
	ListView lv;
	TextView tv_empty;
	View progress;
	List<OrderInfo> mOrders = new ArrayList<OrderInfo>();
	List<Product> mProducts = new ArrayList<Product>();
	MassagerOrderAdapter2 adapter;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_massagerorders);
		init();
	}

	void init() {
		titleButton.setText("全部订单");
		lv = (ListView) findViewById(R.id.lv);
		tv_empty = (TextView) findViewById(R.id.tv_empty);
		progress = findViewById(R.id.progress);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mOrders.clear();
		mProducts.clear();
		images.clear();
		requestAwaitOrders();
	}

	void requestAwaitOrders() {
		progress.setVisibility(View.VISIBLE);
		String url = getString(R.string.url_order_confirming);
		AsyncHttpClient client = new AsyncHttpClient();
		url += "?massagerId=" + UserBean.getUSerBean().getMassagerId() + "&mobile=" + UserBean.getUSerBean().getAccount();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String data = new String(arg2);
				if (data != null) {
					JSONArray orderArray = null;
					try {
						JSONObject object = new JSONObject(data);
						orderArray = object.optJSONArray("OrderList");
						JSONArray productAraay = object.optJSONArray("OrderProductList");
						if (orderArray.length() > 0)
							parseToOrderList(orderArray, productAraay, true, null);
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

	String imgUrl = "";

	void requestAlreadyOrders() {
		progress.setVisibility(View.VISIBLE);
		String url = getString(R.string.url_order_confirmed);
		AsyncHttpClient client = new AsyncHttpClient();
		url += "?massagerId=" + UserBean.getUSerBean().getMassagerId() + "&mobile=" + UserBean.getUSerBean().getAccount();
		Log.i(TAG, url);
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String data = new String(arg2);
				if (data != null) {
					Log.i(TAG, data);
					JSONArray orderArray = null;
					JSONArray imageArray = null;
					try {
						JSONObject object = new JSONObject(data);
						orderArray = object.optJSONArray("OrderList");
						imgUrl = object.optString("ImgUrl");
						imageArray = object.optJSONArray("MassagerOrderList");
						JSONArray productAraay = object.optJSONArray("OrderProductList");
						if (orderArray.length() > 0)
							parseToOrderList(orderArray, productAraay, false, imageArray);
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

	void parseToOrderList(final JSONArray orderArray, final JSONArray productArray, final boolean isReceive, final JSONArray imgArray) {
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				ObjectMapper om = new ObjectMapper();
				try {
					for (int i = 0; i < orderArray.length(); i++) {
						JSONObject object = orderArray.optJSONObject(i);
						OrderInfo orderInfo = om.readValue(object.toString(), OrderInfo.class);
						orderInfo.isReceive = isReceive;
						mOrders.add(orderInfo);
					}
					for (int i = 0; i < productArray.length(); i++) {
						JSONObject object = productArray.optJSONObject(i);
						Product product = om.readValue(object.toString(), Product.class);
						mProducts.add(product);
					}
					if (imgArray != null)
						for (int i = 0; i < imgArray.length(); i++) {
							JSONObject object = imgArray.optJSONObject(i);
							OrderImage image = om.readValue(object.toString(), OrderImage.class);
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
				if (isReceive) {
					requestAlreadyOrders();
				} else {
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

			for (int j = 0; j < images.size(); j++) {
				OrderImage image = images.get(j);
				if (image.Oid == oid)
					orderInfo.imgUrl = imgUrl + image.CompeletImage;
			}
			Log.i(TAG, orderInfo.toString());
		}

	}

	void fillData() {
		if (adapter == null) {
			adapter = new MassagerOrderAdapter2(mOrders, this, this);
			lv.setAdapter(adapter);
		} else {
			adapter.refresh(mOrders);
		}

		tv_empty.setVisibility(mOrders.size() == 0 ? View.VISIBLE : View.GONE);
		tv_empty.setText("什么都米有");
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
						Toast.makeText(AllMassagerOrderActivity.this, msg, Toast.LENGTH_SHORT).show();
						if (returnValue == 1) {
							// 接单成功
							mOrders.clear();
							requestAwaitOrders();
							requestAlreadyOrders();
						} else {
							progress.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(AllMassagerOrderActivity.this, "请求失败，请重试", Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);

			}
		});
	}

	@Override
	public void onUploadClick(int oid) {
		Intent intent = new Intent(this, UploadImageActivity.class);
		intent.putExtra("oid", oid);
		this.startActivity(intent);
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
