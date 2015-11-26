package com.kingtopgroup.activty;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
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
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeOrderActivity extends MainActionBarActivity implements OnClickListener {
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 111 && arg1 == 200) {
			//requestData();
		}
	}

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
		titleButton.setText("�ҵĶ���");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		tv_total = (TextView) findViewById(R.id.tv_total);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		progress = findViewById(R.id.progress);

		lv.setAdapter(new MyListViewAdapter());
		btn_pay.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		requestData();
	}

	void requestData() {
		orders.clear();
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/ucenter/GetOrderList?uid=" + uid + "&page=" + 1 + "&orderState=0";
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
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				toastMsg("����ʧ�ܣ������ԣ�", 1);
			}
		});
	}

	void parseToEntity(final JSONArray array) {
		orders.clear();
		new ParserJSON(new ParseListener() {

			@Override
			public Object onParse() {
				try {
					ObjectMapper mapper = new ObjectMapper();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Order order = mapper.readValue(obj.toString(), Order.class);
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
							OrderProduct product = om.readValue(obj.toString(), OrderProduct.class);
							for (int j = 0; j < orders.size(); j++) {
								Order order = orders.get(j);
								if (order.oid == product.Oid)
									order.orderProducts.add(product);
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
						fillData();
					}

				}
			}).execute();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	void fillData() {
		progress.setVisibility(View.VISIBLE);
		tv_total.setText("�ϼƣ���0");
		boolean flag = false;
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			if (order.checked) {
				tv_total.setText("�ϼƣ���" + order.orderamount);
				checkedPosition = i;
				flag = true;
				break;
			}
		}
		if (!flag) {
			checkedPosition = -1;
		}
		if (adapter == null) {
			adapter = new MyListViewAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		progress.setVisibility(View.GONE);
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
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
		TextView tv_review;
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
				convertView = View.inflate(MeOrderActivity.this, R.layout.item_order, null);
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
				holder.tv_review = (TextView) convertView.findViewById(R.id.tv_review);
				convertView.setTag(holder);
			}

			final Order order = orders.get(position);
			holder.tv_name.setText("��ϵ�ˣ�" + order.consignee);
			holder.tv_phone.setText("�绰��" + order.mobile);
			holder.tv_address.setText("��ַ��" + order.address);
			holder.tv_order_num.setText("�����ţ�" + order.osn);
			holder.tv_ordermoney.setText("Ӧ������" + order.orderamount);
			String state = "����״̬��";
			switch (order.orderstate) {
			case 10:
				state += "���ύ";
				break;
			case 30:
				state += "�ȴ�����";
				break;
			case 50:
				state += "���ӵ�";
				break;
			case 70:
				state += "�ѽӵ�";
				break;
			case 90:
				state += "������";
				break;
			case 110:
				state += "�ѷ���";
				break;
			case 140:
				state += "�����";
				break;
			case 160:
				state += "���˿�";
				break;
			case 180:
				state += "����";
				break;
			case 200:
				state += "ȡ��";
				break;

			default:
				break;
			}

			holder.tv_cancel.setVisibility(state.contains("�ȴ�����") ? View.VISIBLE : View.GONE);
			holder.tv_review.setVisibility(state.contains("�����") ? View.VISIBLE : View.GONE);
			holder.cb.setVisibility(state.contains("�ȴ�����") ? View.VISIBLE : View.GONE);
			holder.tv_order_status.setText(state);
			holder.tv_review.setText(order.isreview == 0 ? "ȥ����" : "��������");
			holder.tv_review.setEnabled(order.isreview == 0);
			holder.tv_review.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MeOrderActivity.this, ReviewActivity.class);
					intent.putExtra("oid", String.valueOf(order.oid));
					MeOrderActivity.this.startActivityForResult(intent, 111);
				}
			});

			holder.tv_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					progress.setVisibility(View.VISIBLE);
					// ȡ������
					AsyncHttpClient client = new AsyncHttpClient();
					String uid = UserBean.getUSerBean().getUid();
					String url = "http://kingtopgroup.com/api/order/cancelorder?uid=" + uid + "&oid=" + order.oid;
					client.get(url, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							if (arg0 == 200) {
								try {
									JSONObject json = new JSONObject(new String(arg2));
									int returnValue = json.optInt("ReturnValue");
									String msg = json.optString("ActionMessage");
									toastMsg(msg, 1);
									if (returnValue == 1)
										requestData();

								} catch (JSONException e) {
									e.printStackTrace();
								}
								progress.setVisibility(View.GONE);

							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							toastMsg("����ʧ�ܣ�������", 1);
							progress.setVisibility(View.GONE);

						}
					});
				}
			});
			List<OrderProduct> products = order.orderProducts;
			holder.ll.removeAllViews();
			for (int i = 0; i < products.size(); i++) {
				OrderProduct product = products.get(i);
				View rl = View.inflate(MeOrderActivity.this, R.layout.item_product, null);
				TextView tv_type = (TextView) rl.findViewById(R.id.tv_type);
				TextView tv_people = (TextView) rl.findViewById(R.id.tv_people);
				TextView tv_count = (TextView) rl.findViewById(R.id.tv_count);
				TextView tv_time = (TextView) rl.findViewById(R.id.tv_time);
				TextView tv_sum = (TextView) rl.findViewById(R.id.tv_sum);
				TextView tv_money = (TextView) rl.findViewById(R.id.tv_money);
				ImageView imageView1 = (ImageView) rl.findViewById(R.id.imageView1);

				tv_people.setText("����ʦ��" + product.MassagerNames);
				String time = product.PServiceDate + " " + product.ServiceTime;
				tv_time.setText("ԤԼʱ�䣺" + time);
				tv_type.setText("��Ŀ��" + product.Name);
				tv_count.setText("������" + product.BuyCount);
				tv_sum.setText(product.Name + "x" + product.RealCount);
				tv_money.setText("��" + product.ShopPrice * product.RealCount);
				String uri = Utils.assembleImageUri(product.ShowImg, "5");
				Log.i(TAG, uri);
				ImageLoader.getInstance().displayImage(uri, imageView1);

				changeTextColor(tv_type, 3);
				changeTextColor(tv_people, 4);
				changeTextColor(tv_count, 3);
				changeTextColor(tv_time, 5);

				holder.ll.addView(rl);

				if (product.BrandId != 28 && state.contains("�ȴ�����")) {
					boolean flag1 = false;
					if (TextUtils.isEmpty(product.ServiceTime))
						flag1 = true;
					String date = product.PServiceDate + product.ServiceTime;
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy��MM��dd��", Locale.CHINA);
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy��MM��dd��HH:mm", Locale.CHINA);
					Date d;
					try {
						d = flag1 ? sdf1.parse(date) : sdf2.parse(date);
						boolean flag2 = d.before(new Date());
						if (flag2) {
							holder.tv_order_status.setText("����״̬��ԤԼʱ���ѹ�");
							holder.cb.setVisibility(View.GONE);
							holder.tv_cancel.setVisibility(View.GONE);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			holder.cb.setChecked(order.checked);

			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
					if (isChecked) {
						for (int i = 0; i < orders.size(); i++) {
							Order order = orders.get(i);
							if (order.checked) {
								order.checked = false;
								break;
							}
						}
					}

					order.checked = isChecked;
					fillData();
				}
			});

			changeTextColor(holder.tv_name, 4);
			changeTextColor(holder.tv_address, 3);
			changeTextColor(holder.tv_phone, 3);
			return convertView;
		}
	}

	void changeTextColor(TextView textView, int count) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		// ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ
		ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);

		builder.setSpan(blackSpan, 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(graySpan, count, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

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
				toastMsg("����û��ѡ���κζ���Ŷ", 1);
				return;
			}
			Intent intent = new Intent(MeOrderActivity.this, PayInfoActivity.class);
			intent.putExtra("order", orders.get(checkedPosition));
			this.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
