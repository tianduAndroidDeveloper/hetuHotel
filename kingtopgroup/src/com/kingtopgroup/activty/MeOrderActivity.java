package com.kingtopgroup.activty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtogroup.domain.Order;
import com.kingtogroup.domain.OrderProduct;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MeOrderActivity extends MainActionBarActivity implements
		OnClickListener {
	private static final String TAG = "MeOrderActivity";
	ListView lv;
	View progress;
	Button btn_pay;
	List<Order> orders = new ArrayList<Order>();
	MyListViewAdapter adapter;
	JSONObject object;
	TextView tv_total;
	int checkedPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		titleButton.setText("我的订单");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		tv_total = (TextView) findViewById(R.id.tv_total);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		progress = findViewById(R.id.progress);

		lv.setAdapter(new MyListViewAdapter());
		btn_pay.setOnClickListener(this);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/ucenter/GetOrderList?uid="
				+ uid + "&page=" + 1 + "&orderState=0";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						object = new JSONObject(new String(arg2));
						JSONArray array = object.getJSONArray("OrderList");
						parseToEntity(array);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.i(TAG, new String(arg2));
				toastMsg("请求失败，请重试！", 1);
			}
		});
	}

	void parseToEntity(final JSONArray array) {
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				try {
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Order order = mapper.readValue(obj.toString(),
								Order.class);
						orders.add(order);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return orders;
			}

			@Override
			public void onComplete(Object parseResult) {
				if (parseResult != null) {
					parseProduct();
				}
			}
		}).execute();
	}

	void parseProduct() {
		try {
			final JSONArray array = object.getJSONArray("OrderProductList");
			new ParserJSON(new ParseListener() {

				@Override
				public Object onParse() {
					for (int i = 0; i < array.length(); i++) {
						try {
							JSONObject obj = array.getJSONObject(i);
							ObjectMapper om = new ObjectMapper();
							OrderProduct product = om.readValue(obj.toString(),
									OrderProduct.class);
							for (int j = 0; j < orders.size(); j++) {
								Order order = orders.get(j);
								if (order.oid == product.Oid)
									order.orderProduct = product;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (JsonParseException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					return orders;
				}

				@Override
				public void onComplete(Object parseResult) {
					if (parseResult != null) {
						Log.i(TAG, orders.toString());
						fillData();
					}

				}
			}).execute();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	void fillData() {
		if (adapter == null) {
			adapter = new MyListViewAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		if (checkedPosition != -1) {
			Order order = orders.get(checkedPosition);
			OrderProduct product = order.orderProduct;
			tv_total.setText("合计：￥" + product.ShopPrice);
		} else {
			tv_total.setText("合计：￥0");
		}
		progress.setVisibility(View.GONE);
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	class ViewHolder {
		TextView tv_order_num;
		TextView tv_order_status;
		TextView tv_type;
		TextView tv_people;
		TextView tv_count;
		TextView tv_time;
		TextView tv_sum;
		TextView tv_money;
		TextView tv_address;
		TextView tv_phone;
		TextView tv_name;
		ImageView img;
		TextView tv_cancel;
		CheckBox cb;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return orders.size();
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
		public View getView(final int position, View convertView, ViewGroup arg2) {
			if (convertView == null)
				convertView = View.inflate(MeOrderActivity.this,
						R.layout.item_order, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv_order_num = (TextView) convertView
						.findViewById(R.id.tv_num);
				holder.tv_order_status = (TextView) convertView
						.findViewById(R.id.tv_status);
				holder.tv_type = (TextView) convertView
						.findViewById(R.id.tv_type);
				holder.tv_people = (TextView) convertView
						.findViewById(R.id.tv_people);
				holder.tv_count = (TextView) convertView
						.findViewById(R.id.tv_count);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_sum = (TextView) convertView
						.findViewById(R.id.tv_sum);
				holder.tv_money = (TextView) convertView
						.findViewById(R.id.tv_money);
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_cancel = (TextView) convertView
						.findViewById(R.id.tv_cancle);
				holder.img = (ImageView) convertView
						.findViewById(R.id.imageView1);
				holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
				convertView.setTag(holder);
			}

			final Order order = orders.get(position);
			final OrderProduct product = order.orderProduct;
			holder.tv_name.setText("联系人：" + order.consignee);
			holder.tv_phone.setText("电话：" + order.mobile);
			holder.tv_address.setText("地址：" + order.address);
			holder.tv_order_num.setText("订单号：" + order.osn);

			holder.tv_people.setText("推拿师：" + product.MassagerNames);
			holder.tv_time.setText("预约时间：" + product.PServiceDate + " "
					+ product.ServiceTime);
			holder.tv_type.setText("项目：" + product.Name);
			holder.tv_sum.setText(product.Name + "x" + product.RealCount);
			holder.tv_money.setText("￥" + product.ShopPrice);

			holder.cb.setChecked(order.checked);

			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0,
						boolean isChecked) {
					if (checkedPosition != -1) {
						if (checkedPosition != position) {
							orders.get(checkedPosition).checked = false;
							checkedPosition = position;
						} else {
							order.checked = false;
							checkedPosition = -1;
						}
					} else {
						checkedPosition = position;
					}
					order.checked = isChecked;
					fillData();

				}
			});

			String state = "订单状态：";
			switch (order.orderstate) {
			case 10:
				state += "已提交";
				break;
			case 30:
				state += "等待付款";
				break;
			case 50:
				state += "待接单";
				break;
			case 70:
				state += "已接单";
				break;
			case 90:
				state += "服务中";
				break;
			case 110:
				state += "已发货";
				break;
			case 140:
				state += "已完成";
				break;
			case 160:
				state += "已退款";
				break;
			case 180:
				state += "锁定";
				break;
			case 200:
				state += "取消";
				break;

			default:
				break;
			}

			holder.tv_cancel
					.setVisibility(state.contains("等待付款") ? View.VISIBLE
							: View.GONE);
			holder.cb.setVisibility(state.contains("等待付款") ? View.VISIBLE
					: View.GONE);
			holder.tv_order_status.setText(state);

			changeTextColor(holder.tv_type, 3);
			changeTextColor(holder.tv_people, 4);
			changeTextColor(holder.tv_count, 3);
			changeTextColor(holder.tv_time, 5);

			changeTextColor(holder.tv_name, 4);
			changeTextColor(holder.tv_address, 3);
			changeTextColor(holder.tv_phone, 3);
			return convertView;
		}

	}

	void changeTextColor(TextView textView, int count) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);

		builder.setSpan(blackSpan, 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(graySpan, count, text.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		textView.setText(builder);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			if (checkedPosition == -1) {
				toastMsg("您还没有选择任何订单哦", 1);
				return;
			}
			Intent intent = new Intent(MeOrderActivity.this,
					PayInfoActivity.class);
			intent.putExtra("order", orders.get(checkedPosition));
			this.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
