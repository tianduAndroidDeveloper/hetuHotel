package com.kingtopgroup.activty;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtogroup.domain.ShipAddress;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ManageAddressActivity extends MainActionBarActivity implements OnClickListener {
	ListView lv;
	TextView tv_add;
	MyListViewAdapter adapter;
	List<ShipAddress> addresses = new ArrayList<ShipAddress>();
	View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_address);
		
		titleButton.setText("地址管理");
		
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		tv_add = (TextView) findViewById(R.id.tv_add);
		progress = findViewById(R.id.progress);
		
		tv_add.setOnClickListener(this);
		lv.setOnItemClickListener(new MyListViewItemClickListner());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		addresses.clear();
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		UserBean userBean = UserBean.getUSerBean();
		String uid = userBean.getUid();
		if (TextUtils.isEmpty(uid))
			return;
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/ucenter/GetShipAddressList?uid="
				+ uid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					String data = new String(arg2);
					parseToEntity(data);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	void parseToEntity(String data) {
		try {
			JSONObject object = new JSONObject(data);
			final JSONArray array = object.getJSONArray("ShipAddressList");
			new ParserJSON(new ParseListener() {

				@Override
				public Object onParse() {
					for (int i = 0; i < array.length(); i++) {
						try {
							JSONObject obj = array.getJSONObject(i);
							ObjectMapper om = new ObjectMapper();
							ShipAddress shipAddress = om.readValue(
									obj.toString(), ShipAddress.class);
							addresses.add(shipAddress);
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
					return addresses;
				}

				@Override
				public void onComplete(Object parseResult) {
					if (parseResult != null)
						fillData();
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
		progress.setVisibility(View.GONE);
	}
	
	class MyListViewItemClickListner implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(ManageAddressActivity.this, EditAddressActivity.class);
			intent.putExtra("Address", addresses.get(arg2));
			ManageAddressActivity.this.startActivity(intent);
		}
		
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_phone;
		TextView tv_address;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return addresses.size();
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
				convertView = View.inflate(ManageAddressActivity.this,
						R.layout.item_address, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				convertView.setTag(holder);
			}
			ShipAddress address = addresses.get(position);
			holder.tv_address.setText("详细地址：" + address.Address);
			holder.tv_name.setText("姓        名：" + address.Consignee);
			holder.tv_phone.setText("联系电话：" + address.Mobile);

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add:
			Intent intent = new Intent(this, AddAddressActivity.class);
			this.startActivity(intent);
			break;

		default:
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
