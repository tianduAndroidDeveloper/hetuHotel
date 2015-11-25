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
import com.kingtogroup.domain.MassageEntity;
import com.kingtogroup.domain.ServiceEntity;
import com.kingtogroup.location.DistanceUtils;
import com.kingtogroup.location.LastLocation;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommitActivity extends MainActionBarActivity implements OnClickListener {
	private static final String TAG = "CommitActivity";
	ListView lv;
	View progress;
	List<ServiceEntity> services = new ArrayList<ServiceEntity>();
	List<MassageEntity> massages = new ArrayList<MassageEntity>();
	MyListViewAdapter adapter;
	TextView tv_commit;
	TextView tv_total;
	String opid;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		titleButton.setText("确认订单");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		progress = findViewById(R.id.progress);
		tv_commit = (TextView) findViewById(R.id.tv_commit);
		tv_total = (TextView) findViewById(R.id.tv_total);
		opid = String.valueOf(getIntent().getIntExtra("opid", -1));
		tv_commit.setOnClickListener(this);
		checkCardBalance();
		addHeader();
		requestData();
	}

	void addHeader() {
		View v = View.inflate(this, R.layout.header_commit, null);
		final CheckBox cb_shipcard = (CheckBox) v.findViewById(R.id.cb);
		lv.addHeaderView(v);
		TextView tv_prefer = (TextView) v.findViewById(R.id.tv_prefer);
		tv_prefer.setOnClickListener(this);
		cb_shipcard.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (balance < sum) {
					toastMsg("您的会员卡余额不足，请充值", 1);
					cb_shipcard.setChecked(false);
				}
			}
		});
	}

	double balance = -1;

	double checkCardBalance() {
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/ucenter/ShipCardList?uid=" + uid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject memberObject = new JSONObject(new String(arg2));
						JSONObject account = memberObject.optJSONObject("Account");
						balance = account.optDouble("CurrentAmount");
					} catch (JSONException e) {
						toastMsg("会员卡余额查询失败，请联系客服", 1);
						e.printStackTrace();
					}
					progress.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				toastMsg("会员卡余额查询失败，请重试", 1);
				balance = -1;
				progress.setVisibility(View.GONE);
			}
		});
		return balance;
	}

	JSONObject object;

	void requestData() {
		if (opid.equals("-1"))
			return;
		progress.setVisibility(View.VISIBLE);
		AsyncHttpClient client = new AsyncHttpClient();
		String uid = UserBean.getUSerBean().getUid();
		String url = "http://kingtopgroup.com/api/order/confirmorder?uid=" + uid + "&opid=" + opid;
		client.post(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						object = new JSONObject(new String(arg2));
						JSONArray array = object.optJSONArray("ServiceInfoList");
						parseToEntity(array);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
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
						ServiceEntity service = mapper.readValue(obj.toString(), ServiceEntity.class);
						services.add(service);
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
				return services;
			}

			@Override
			public void onComplete(Object parseResult) {
				if (parseResult != null) {
					parseMassages();
				}
			}
		}).execute();
	}

	void parseMassages() {
		JSONArray array = object.optJSONArray("MassagerInfoList");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.optJSONObject(i);
			MassageEntity massageEntity = new MassageEntity();
			massageEntity.Description = object.optString("Description");
			massageEntity.Logo = object.optString("Logo");
			massageEntity.Name = object.optString("Name");
			massageEntity.Point_X = object.optDouble("Point_X");
			massageEntity.Point_Y = object.optDouble("Point_Y");
			massages.add(massageEntity);
		}
		fillData();

	}

	double sum = 0;

	void fillData() {
		progress.setVisibility(View.GONE);
		if (adapter == null) {
			adapter = new MyListViewAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		for (int i = 0; i < services.size(); i++) {
			ServiceEntity service = services.get(i);
			sum += service.ShopPrice;
		}
		tv_total.setText("合计：￥" + sum);
	}

	void toastMsg(String msg, int time) {
		Toast.makeText(this, msg, time).show();
	}

	class ViewHolder {
		ImageView iv_service;
		TextView tv_type;
		TextView tv_count;
		TextView tv_time;
		TextView tv_sum;
		TextView tv_money;
		LinearLayout ll_massagers;
		TextView tv_address;
		TextView tv_phone;
		TextView tv_name;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return services.size();
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
				convertView = View.inflate(CommitActivity.this, R.layout.item_service, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null)
				holder = initHolder(convertView);
			ServiceEntity service = services.get(position);
			String uri = Utils.assembleImageUri(service.Logo, "5");
			Log.i(TAG, uri);
			ImageLoader.getInstance().displayImage(uri.trim(), holder.iv_service);
			holder.tv_type.setText("项目：" + service.Name);
			holder.tv_count.setText("数量：" + service.BuyCount);
			String[] date = service.ServiceDate.split("T");
			holder.tv_time.setText("预约时间：" + date[0] + " " + service.TimeSection);
			holder.tv_phone.setText("联系电话：" + service.Mobile);
			holder.tv_sum.setText(service.Name + "x" + service.BuyCount);
			holder.tv_name.setText("联系人：" + service.Consignee);
			holder.tv_address.setText("通讯地址：" + service.Address);
			holder.tv_money.setText("￥" + service.ShopPrice);
			holder.ll_massagers.removeAllViews();
			initMassageData(holder.ll_massagers, service);
			spanTextColor(holder);
			return convertView;
		}
	}

	void initMassageData(LinearLayout ll, ServiceEntity service) {
		for (int i = 0; i < massages.size(); i++) {
			MassageEntity massageEntity = massages.get(i);
			View v = View.inflate(this, R.layout.item_massager, null);
			TextView tv1 = (TextView) v.findViewById(R.id.tv1);
			TextView tv2 = (TextView) v.findViewById(R.id.tv2);
			TextView tv3 = (TextView) v.findViewById(R.id.tv3);
			ImageView iv2 = (ImageView) v.findViewById(R.id.imageView2);
			try {
				double distance = DistanceUtils.GetDistance(massageEntity.Point_X, massageEntity.Point_X, LastLocation.initInstance().getLatitude(), LastLocation.initInstance().getLongitude());
				tv2.setText(String.valueOf(distance) + "公里");
			} catch (Exception e) {
				e.printStackTrace();
			}
			tv1.setText(massageEntity.Name);
			String uri = "http://kingtopgroup.com/upload/store/" + service.Storeid + "/logo/thumb150_150/" + service.ShowImg;
			Log.i(TAG, uri);
			ImageLoader.getInstance().displayImage(uri.trim(), iv2);
			ll.addView(v);

		}
	}

	void spanTextColor(ViewHolder holder) {
		changeTextColor(holder.tv_type, 3);
		changeTextColor(holder.tv_count, 3);
		changeTextColor(holder.tv_time, 5);
		changeTextColor(holder.tv_name, 4);
		changeTextColor(holder.tv_address, 5);
		changeTextColor(holder.tv_phone, 5);
	}

	ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
		holder.iv_service = (ImageView) convertView.findViewById(R.id.imageView1);
		holder.ll_massagers = (LinearLayout) convertView.findViewById(R.id.ll);
		holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
		holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
		holder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
		holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
		holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
		holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		convertView.findViewById(R.id.tv_people).setVisibility(View.GONE);
		convertView.setTag(holder);
		return holder;
	}

	void changeTextColor(TextView textView, int count) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);

		builder.setSpan(blackSpan, 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(graySpan, count, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		textView.setText(builder);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_prefer:
			Intent intent = new Intent(CommitActivity.this, CheckPreferActivity.class);
			CommitActivity.this.startActivity(intent);
			break;
		case R.id.tv_commit:
			confirmOrder();
			break;

		default:
			break;
		}
	}

	void confirmOrder() {
		AsyncHttpClient client = new AsyncHttpClient();
		// uid,orderProductKeyList以“,”分开,payCreditCount=1,coupList=""以“,”分开
		String uid = UserBean.getUSerBean().getUid();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < services.size(); i++) {
			ServiceEntity service = services.get(i);
			sb.append(service.Opid + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		String url = "http://kingtopgroup.com/api/order/SubmitOrder?uid=" + uid + "&orderProductKeyList=" + sb.toString() + "&payCreditCount=1&coupList=0";
		Log.i(TAG, url);
		client.post(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject orderObject = new JSONObject(new String(arg2));
						Log.i(TAG, orderObject.toString());
						int returnValue = orderObject.optInt("ReturnValue");
						String msg = orderObject.optString("ActionMessage");
						if (returnValue != 0) {
							Intent intent = new Intent(CommitActivity.this, ConfirmOrderActivity.class);
							intent.putExtra("oid", returnValue);
							CommitActivity.this.startActivity(intent);
						} else {
							toastMsg(msg, 1);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Log.i(TAG, new String(arg2));

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

}
