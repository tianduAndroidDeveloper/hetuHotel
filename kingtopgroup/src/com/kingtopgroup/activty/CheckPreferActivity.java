package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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

public class CheckPreferActivity extends MainActionBarActivity {
	private static final String TAG = "CheckPreferActivity";
	ListView lv;
	View progress;
	MyListViewAdapter adapter;
	List<CouponEntity> coupons = new ArrayList<CouponEntity>();

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_prefer);
		titleButton.setText("使用优惠券");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		progress = findViewById(R.id.progress);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		UserBean userBead = UserBean.getUSerBean();
		String uid = userBead.getUid();
		if (TextUtils.isEmpty(uid))
			return;

		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/ucenter/GetCouponListByUid?uid="
				+ uid + "&type=" + "3";
		client.get(url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					String data = new String(arg2);
					parseToEntity(data);
				} else {
					toastMsg("请求失败，请重试");
					progress.setVisibility(View.GONE);
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("请求失败，请重试");
				progress.setVisibility(View.GONE);
			}
		});
	}

	void toastMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(CheckPreferActivity.this, msg,
						Toast.LENGTH_SHORT).show();
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
				coupons.add(new CouponEntity(couponId, name, money,
						useExpireTime, state));
			}
			fillData();
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
				convertView = View.inflate(CheckPreferActivity.this,
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
			case 2:
				holder.rl.setBackgroundResource(R.drawable.coupon_b);
				break;
			case 3:
				holder.rl.setBackgroundResource(R.drawable.coupon);
				break;
			}
			holder.tv_deadline.setText("有效期剩余"
					+ couponEntity.getUseExpireTime() + "天");
			holder.tv_type.setText(couponEntity.getName());
			holder.tv_points.setText(couponEntity.getMoney() + "");

			return convertView;
		}

	}

	public void ok(View v) {

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
