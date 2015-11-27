package com.kingtopgroup.activty;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtogroup.domain.ShipAddress;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddAddressActivty extends MainActionBarActivity implements OnClickListener {

	private TextView add_Address;
	private ListView add_listView;
	Map<String, Object> map = null;
	private List<Map<String, Object>> addressList;
	private LinearLayout ll_add;
	private View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);

		titleButton.setText("—°‘Òµÿ÷∑");

		ll_add = (LinearLayout) findViewById(R.id.ll_add);
		add_Address = (TextView) findViewById(R.id.add_address);
		add_listView = (ListView) findViewById(R.id.address_listview);
		progress = findViewById(R.id.progress);

		add_Address.setOnClickListener(this);
		ll_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddAddressActivty.this, AddAddressActivity.class);
				AddAddressActivty.this.startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		getDate();
	}

	private void setdate(List<Map<String, Object>> list) {
		add_listView.setAdapter(new AddressAdapter());
	}

	class AddressAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return addressList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return addressList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewholder = null;
			if (view == null) {
				viewholder = new ViewHolder();
				view = View.inflate(AddAddressActivty.this, R.layout.address_item, null);
				viewholder.name = (TextView) view.findViewById(R.id.name);
				viewholder.phone = (TextView) view.findViewById(R.id.phone);
				viewholder.add_address = (TextView) view.findViewById(R.id.add_address);
				viewholder.isDefault = (RadioButton) view.findViewById(R.id.isdefault);

				view.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) view.getTag();
			}
			final String name = (String) addressList.get(position).get("person");
			viewholder.name.setText(name);
			final String phone = (String) addressList.get(position).get("phone");
			viewholder.phone.setText(phone);
			final String street = (String) addressList.get(position).get("street");
			viewholder.add_address.setText(street);
			viewholder.isDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					ShipAddress address = new ShipAddress();
					address.Consignee = name;
					address.Phone = phone;
					address.Address = street;
					Intent intent = new Intent();
					intent.putExtra("address", address);
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
			return view;
		}
	}

	class ViewHolder {
		TextView name;
		TextView phone;
		TextView add_address;
		RadioButton isDefault;
	}

	public List<Map<String, Object>> getDate() {
		progress.setVisibility(View.VISIBLE);
		RequestParams params = AsyncHttpCilentUtil.getParams();

		params.put("uid", UserBean.getUSerBean().getUid());
		AsyncHttpCilentUtil.getInstance().get(this, ConstanceUtil.get_address_list_url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						if (arg2 != null) {
							JSONObject obj = new JSONObject(new String(arg2));
							JSONArray array = obj.getJSONArray("ShipAddressList");
							addressList = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								String IsDefault = array.getJSONObject(i).getString("IsDefault");
								String phone = array.getJSONObject(i).getString("Mobile");
								String street = array.getJSONObject(i).getString("Address");
								String person = array.getJSONObject(i).getString("Consignee");
								map = new HashMap<String, Object>();
								map.put("phone", phone);
								map.put("street", street);
								map.put("person", person);
								map.put("isDefault", IsDefault);

								addressList.add(map);
							}
							setdate(addressList);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progress.setVisibility(View.GONE);

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(AddAddressActivty.this, "«Î«Û ß∞‹£¨«Î÷ÿ ‘", Toast.LENGTH_SHORT).show();
				progress.setVisibility(View.GONE);
			}

		});

		return null;
	}

	@Override
	public void onClick(View arg0) {
		Intent inten = new Intent(this, AddAddressAddActivty.class);
		startActivity(inten);
	}

	@Override
	public void backButtonClick(View v) {
		// TODO Auto-generated method stub
		this.finish();
	}

	@Override
	public void titleButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rightButtonClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean showHeadView() {
		// TODO Auto-generated method stub
		return true;
	}

}
