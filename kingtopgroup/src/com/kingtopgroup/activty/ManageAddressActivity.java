package com.kingtopgroup.activty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtogroup.domain.ShipAddress;
import com.kingtogroup.utils.ParserJSON;
import com.kingtogroup.utils.ParserJSON.ParseListener;
import com.kingtogroup.view.MyListView;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ManageAddressActivity extends Activity {
	private static final String TAG = "ManageAddressActivity";
	MyListView lv;
	MyListViewAdapter adapter;
	List<ShipAddress> addresses = new ArrayList<ShipAddress>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_address);
		init();
	}

	void init() {
		lv = (MyListView) findViewById(R.id.lv);
		requestData();
	}

	void requestData() {
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
					Log.i(TAG, data);
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					return addresses;
				}

				@Override
				public void onComplete(Object parseResult) {
					if(parseResult != null)
						fillData();
				}
			}).execute();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
			if(convertView == null)
				convertView = View.inflate(ManageAddressActivity.this, R.layout.item_address, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder == null){
				holder = new ViewHolder();
				holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
				convertView.setTag(holder);
			}
			ShipAddress address = addresses.get(position);
			holder.tv_address.setText("详细地址：" + address.Address);
			holder.tv_name.setText("姓  名：" + address.Mobile);
			holder.tv_phone.setText("联系电话：" + address.Phone);
			
			return convertView;
		}

	}
}
