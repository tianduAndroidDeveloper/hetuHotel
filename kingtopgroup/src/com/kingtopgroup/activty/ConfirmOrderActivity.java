package com.kingtopgroup.activty;

import java.io.IOException;

import org.apache.http.Header;
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
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ConfirmOrderActivity extends MainActionBarActivity {
	private static final String TAG = "ConfirmOrderActivity";
	ListView lv;
	OrderInfo info;
	String oid;
	MyListViewAdapter adapter;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		titleButton.setText("提交订单");
		init();
	}

	void init() {
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
					JSONObject object = new JSONObject(new String(arg2));
					
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

	void parseToEntity(final JSONObject object) {
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				ObjectMapper om = new ObjectMapper();
				try {
					info = om.readValue(object.toString(), OrderInfo.class);
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
					fillData();
				}
			}
		}).execute();
	}

	void fillData() {
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
		TextView tv_address;
		TextView tv_phone;
		TextView tv_name;
		ImageView img;
		TextView tv_cancel;
		LinearLayout ll;
		TextView tv_ordermoney;
		TextView tv_payway;
		TextView tv_discount;
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
				convertView = View.inflate(ConfirmOrderActivity.this, R.layout.item_order, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv_order_num = (TextView) convertView.findViewById(R.id.tv_num);
				holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_status);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
				holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
				holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_cancel = (TextView) convertView.findViewById(R.id.tv_cancle);
				holder.img = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				holder.tv_ordermoney = (TextView) convertView.findViewById(R.id.tv_ordermoney);
				holder.tv_payway = (TextView) convertView.findViewById(R.id.tv_payway);
				holder.tv_discount = (TextView) convertView.findViewById(R.id.tv_discount);
				convertView.setTag(holder);
			}
			holder.tv_order_num.setText(info.OSN);
			
			View rl = View.inflate(ConfirmOrderActivity.this, R.layout.item_product, null);
			TextView tv_type = (TextView) rl.findViewById(R.id.tv_type);
			TextView tv_people = (TextView) rl.findViewById(R.id.tv_people);
			TextView tv_count = (TextView) rl.findViewById(R.id.tv_count);
			TextView tv_time = (TextView) rl.findViewById(R.id.tv_time);
			TextView tv_sum = (TextView) rl.findViewById(R.id.tv_sum);
			TextView tv_money = (TextView) rl.findViewById(R.id.tv_money);
			ImageView imageView1 = (ImageView) rl.findViewById(R.id.imageView1);

			tv_people.setText("推拿师：" + info.StoreName);
			tv_type.setText("项目：" );
			tv_count.setText("数量：");
			tv_sum.setText("");
			tv_money.setText("￥" + info.OrderAmount);
			
			//ImageLoader.getInstance().displayImage(uri, imageView1);
			holder.ll.addView(rl);

			holder.tv_address.setText("详细地址：" + info.Address);
			holder.tv_phone.setText("联系电话：" + info.Mobile);
			holder.tv_name.setText("联系人：" + info.Consignee);
			holder.tv_discount.setVisibility(View.VISIBLE);
			holder.tv_payway.setVisibility(View.VISIBLE);
			holder.tv_payway.setText("支付方式：" + info.PaySystemName);
			holder.tv_discount.setText("优惠券抵扣：￥" + info.Discount);
			holder.tv_ordermoney.setVisibility(View.GONE);
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
