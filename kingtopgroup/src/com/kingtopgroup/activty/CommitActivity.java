package com.kingtopgroup.activty;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingtogroup.domain.ShipAddress;
import com.kingtogroup.view.MyListView;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommitActivity extends MainActionBarActivity implements
		OnClickListener {
	MyListView lv;
	List<HashMap<String, Object>> serviceItems;
	List<ShipAddress> addresses;
	List<ManagerBean> massages;
	TextView tv_pay;
	TextView tv_prefer;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		titleButton.setText("提交订单");
		init();
	}

	void init() {
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_prefer = (TextView) findViewById(R.id.tv_prefer);
		lv = (MyListView) findViewById(R.id.lv);

		serviceItems = UserBean.getUSerBean().getServiceItems();
		addresses = UserBean.getUSerBean().getAddresses();
		massages = UserBean.getUSerBean().getMassages();

		lv.setAdapter(new MyListViewAdapter());
		tv_pay.setOnClickListener(this);
		tv_prefer.setOnClickListener(this);
	}

	class ViewHolder {
		TextView tv_type;
		TextView tv_count;
		TextView tv_time;
		TextView tv_sum;
		TextView tv_money;
		TextView tv_address;
		TextView tv_phone;
		TextView tv_contactor;
		TextView tv_name;
		TextView tv_location;
		TextView tv_distance;
		ImageView imageView1;
		ImageView imageView2;
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
				convertView = View.inflate(CommitActivity.this,
						R.layout.item_order2, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.imageView1 = (ImageView) convertView
						.findViewById(R.id.imageView1);
				holder.tv_type = (TextView) convertView
						.findViewById(R.id.tv_type);
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
				holder.tv_contactor = (TextView) convertView
						.findViewById(R.id.tv_contactor);
				holder.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.tv_location);
				holder.tv_distance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				holder.imageView2 = (ImageView) convertView
						.findViewById(R.id.imageView2);
				convertView.setTag(holder);
			}
			HashMap<String, Object> serviceItem = serviceItems.get(position);
			String name = (String) serviceItem.get("name");
			String time = (String) serviceItem.get("time");
			holder.tv_type.setText("项目：" + name + " " + time + "分钟");
			String count = (String) serviceItem.get("beginnum");
			holder.tv_count.setText("项目数量：" + count);
			holder.tv_sum.setText(name + "x" + count);
			holder.tv_money.setText("￥" + serviceItem.get("marketprice"));
			String uri = (String) serviceItem.get("order_item_image1");
			ImageLoader.getInstance().displayImage(uri, holder.imageView1);

			ShipAddress address = addresses.get(position);
			holder.tv_address.setText("详细地址：" + address.Address);
			holder.tv_phone.setText("联系电话：" + address.Phone);
			holder.tv_contactor.setText("联系人：" + address.Consignee);

			ManagerBean bean = massages.get(position);
			holder.tv_name.setText(bean.name + " " + bean.sex);
			holder.tv_location.setText("位置：" + bean.address);
			String uri2 = "http://kingtopgroup.com/upload/store/"
					+ bean.StoreId + "/logo/thumb150_150/" + bean.Logo;
			ImageLoader.getInstance().displayImage(uri2, holder.imageView2);
			return convertView;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_pay:
			Intent intent = new Intent(this, ConfirmOrderActivity.class);
			this.startActivity(intent);
			break;
		case R.id.tv_prefer:
			Intent intent2 = new Intent(this, CheckPreferActivity.class);
			this.startActivity(intent2);
			break;
		default:
			break;
		}
	}

}
