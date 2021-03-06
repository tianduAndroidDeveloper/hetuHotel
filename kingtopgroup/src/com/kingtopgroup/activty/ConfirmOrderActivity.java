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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtogroup.domain.Order;
import com.kingtogroup.domain.OrderInfo;
import com.kingtogroup.domain.Product;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ConfirmOrderActivity extends MainActionBarActivity {
	private static final String TAG = "ConfirmOrderActivity";
	ListView lv;
	OrderInfo info;
	String oid;
	MyListViewAdapter adapter;
	JSONObject object;
	List<Product> products = new ArrayList<Product>();
	TextView tv_total;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		titleButton.setText("提交订单");
		init();
	}

	void init() {
		tv_total = (TextView) findViewById(R.id.tv_total);
		lv = (ListView) findViewById(R.id.lv);
		oid = String.valueOf(getIntent().getIntExtra("oid", 0));
		requestData();
	}

	void requestData() {
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/order/getorderinfo?uid=" + uid + "&oid=" + oid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					object = new JSONObject(new String(arg2));
					parseToEntity(object.optJSONObject("OrderInfo"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}
		});
	}

	void parseToEntity(final JSONObject orderInfo) {
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				ObjectMapper om = new ObjectMapper();
				try {
					info = om.readValue(orderInfo.toString(), OrderInfo.class);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return info;
			}

			@Override
			public void onComplete(Object parseResult) {
				if (parseResult != null) {
					Log.i(TAG, parseResult.toString());
					parseToList(object.optJSONArray("ProductLis"));
				}
			}
		}).execute();
	}

	void parseToList(final JSONArray productList) {
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				ObjectMapper om = new ObjectMapper();
				try {
					for (int i = 0; i < productList.length(); i++) {
						JSONObject productObject = productList.optJSONObject(i);
						Product pro = om.readValue(productObject.toString(), Product.class);
						products.add(pro);
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return products;
			}

			@Override
			public void onComplete(Object parseResult) {
				if (parseResult != null) {
					Log.i(TAG, parseResult.toString());
					fillData();
				}
			}
		}).execute();
	}

	void fillData() {
		double sum = 0;
		for (int i = 0; i < products.size(); i++) {
			Product pro = products.get(i);
			sum += pro.ShopPrice;
		}
		tv_total.setText("合计：￥" + sum);
		if (adapter == null) {
			adapter = new MyListViewAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView tv_order_num;
		TextView tv_order_status;
		TextView tv_payway;
		TextView tv_discount;
		LinearLayout ll;
		CheckBox cb;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null)
				convertView = View.inflate(ConfirmOrderActivity.this, R.layout.item_order3, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv_order_num = (TextView) convertView.findViewById(R.id.tv_num);
				holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_status);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				holder.tv_payway = (TextView) convertView.findViewById(R.id.tv_payway);
				holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
				convertView.setTag(holder);
			}
			holder.tv_order_num.setText(info.OSN);

			for (int i = 0; i < products.size(); i++) {
				Product pro = products.get(i);
				View rl = View.inflate(ConfirmOrderActivity.this, R.layout.item_order3_include, null);
				TextView tv_type = (TextView) rl.findViewById(R.id.tv_type);
				TextView tv_people = (TextView) rl.findViewById(R.id.tv_people);
				TextView tv_count = (TextView) rl.findViewById(R.id.tv_count);
				TextView tv_time = (TextView) rl.findViewById(R.id.tv_time);
				TextView tv_sum = (TextView) rl.findViewById(R.id.tv_sum);
				TextView tv_money = (TextView) rl.findViewById(R.id.tv_money);
				ImageView imageView1 = (ImageView) rl.findViewById(R.id.imageView1);

				tv_people.setText("推拿师：" + pro.MassagerNames);
				tv_type.setText("项目：" + pro.Name);
				tv_count.setText("数量：" + pro.BuyCount);
				tv_sum.setText(pro.Name + "x" + pro.BuyCount);
				tv_money.setText("￥" + pro.ShopPrice);
				tv_time.setText("预约时间：" + pro.ServiceDate.split("T")[0] + " " + pro.ServiceTime);
				String uri = Utils.assembleImageUri(pro.ShowImg, "5");
				ImageLoader.getInstance().displayImage(uri, imageView1);

				holder.ll.addView(rl);
			}
			holder.tv_payway.setText("支付方式：" + info.PaySystemName);
			holder.tv_discount.setText("优惠券抵扣：￥" + info.Discount);
			return convertView;
		}

	}

	public void confirm(View v) {
		Order order = new Order();
		order.osn = info.OSN;
		order.orderamount = info.OrderAmount;
		order.oid = info.Oid;
		order.paysystemname = info.PaySystemName;
		Intent intent = new Intent(this, PayInfoActivity.class);
		intent.putExtra("order", order);
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
