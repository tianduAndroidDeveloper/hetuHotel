package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.domain.CouponEntity;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PreferActivity extends MainActionBarActivity {
	private static final String TAG = "PreferActivity";
	ListView lv;
	MyListViewAdapter adapter;
	List<CouponEntity> coupons = new ArrayList<CouponEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prefer);
		titleButton.setText("”≈ª›»Ø");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		requestData();
	}

	void requestData() {
		UserBean userBead = UserBean.getUSerBean();
		String uid = userBead.getUid();
		if (TextUtils.isEmpty(uid))
			return;

		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/ucenter/GetCouponListByUid?uid="
				+ uid + "&type=" + "0";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					String data = new String(arg2);
					parseToEntity(data);
				} else {
					toastMsg("«Î«Û ß∞‹£¨«Î÷ÿ ‘");
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("«Î«Û ß∞‹£¨«Î÷ÿ ‘");
			}
		});
	}

	void toastMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(PreferActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	void parseToEntity(String data) {
		try {
			JSONObject object = new JSONObject(data);
			JSONArray array = object.getJSONArray("CouponList");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				int couponId = obj.getInt("couponid");
				int useExpireTime = obj.getInt("useexpiretime");
				int money = obj.getInt("money");
				String name = obj.getString("name");
				int state = obj.getInt("state");
				coupons.add(new CouponEntity(couponId, name, money, useExpireTime, state));
			}
			fillData();
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
		TextView tv_deadline;
		TextView tv_type;
		TextView tv_points;
		RelativeLayout rl;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return coupons.size();
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
				convertView = View.inflate(PreferActivity.this,
						R.layout.item_coupon, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv_deadline = (TextView) convertView
						.findViewById(R.id.tv_deadline);
				holder.tv_points = (TextView) convertView
						.findViewById(R.id.tv_points);
				holder.tv_type = (TextView) convertView
						.findViewById(R.id.tv_type);
				holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
				convertView.setTag(holder);
			}
			CouponEntity couponEntity = coupons.get(position);
			Log.i(TAG, couponEntity.toString());
			switch (couponEntity.getState()) {
			case 1:
			case 3:
				holder.rl.setBackgroundResource(R.drawable.coupon);
				break;
			case 2:
				holder.rl.setBackgroundResource(R.drawable.coupon_b);
				break;

			default:
				holder.rl.setBackgroundResource(R.drawable.coupon);
				break;
			}
			holder.tv_deadline.setText("”––ß∆⁄ £”‡"
					+ couponEntity.getUseExpireTime() + "ÃÏ");
			holder.tv_type.setText(couponEntity.getName());
			holder.tv_points.setText(couponEntity.getMoney() + "");

			return convertView;
		}

	}

	@Override
	public void backButtonClick(View v) {
		finish();
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
		return true;
	}
}
