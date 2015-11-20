package com.kingtopgroup.activty;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
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

import com.kingtogroup.domain.ShipAddress;
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ConfirmOrderActivity extends MainActionBarActivity {
	ListView lv;
	List<HashMap<String, Object>> serviceItems;
	List<ShipAddress> addresses;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		titleButton.setText("ȷ�϶���");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new MyListViewAdapter());
		
		serviceItems = UserBean.getUSerBean().getServiceItems();
		addresses = UserBean.getUSerBean().getAddresses();
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
				holder.tv_order_num = (TextView) convertView
						.findViewById(R.id.tv_num);
				holder.tv_order_status = (TextView) convertView
						.findViewById(R.id.tv_status);
				holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
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
				holder.tv_ordermoney = (TextView) convertView
						.findViewById(R.id.tv_ordermoney);
				convertView.setTag(holder);
			}
			HashMap<String, Object> serviceItem = serviceItems.get(position);
			View rl = View.inflate(ConfirmOrderActivity.this, R.layout.item_product, null);
			TextView tv_type = (TextView) rl.findViewById(R.id.tv_type);
			TextView tv_people = (TextView) rl.findViewById(R.id.tv_people);
			TextView tv_count = (TextView) rl.findViewById(R.id.tv_count);
			TextView tv_time = (TextView) rl.findViewById(R.id.tv_time);
			TextView tv_sum = (TextView) rl.findViewById(R.id.tv_sum);
			TextView tv_money = (TextView) rl.findViewById(R.id.tv_money);
			ImageView imageView1 = (ImageView) rl.findViewById(R.id.imageView1);

			tv_people.setVisibility(View.GONE);
			tv_type.setText("��Ŀ��" + serviceItem.get("name"));
			tv_count.setText("������" + serviceItem.get("beginnum"));
			tv_sum.setText(serviceItem.get("name") + "x" + serviceItem.get("beginnum"));
			int count = Integer.parseInt((String)serviceItem.get("beginnum"));
			int price = Integer.parseInt((String)serviceItem.get("marketprice"));
			tv_money.setText("��" + count * price);
			String uri = (String) serviceItem.get("order_item_image1");
			ImageLoader.getInstance().displayImage(uri, imageView1);
			holder.ll.addView(rl);
			
			ShipAddress address = addresses.get(position);
			holder.tv_address.setText("��ϸ��ַ��" + address.Address);
			holder.tv_phone.setText("��ϵ�绰��" + address.Phone);
			holder.tv_name.setText("��ϵ�ˣ�" + address.Consignee);
			
			holder.tv_ordermoney.setText("��" + count * price);
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

}
