package com.kingtopgroup.activty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.MeOrderActivity.ViewHolder;

public class ConfirmOrderActivity extends MainActionBarActivity {
	ListView lv;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		titleButton.setText("»∑»œ∂©µ•");
		init();
	}

	void init() {
		lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new MyListViewAdapter());
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
			View v = View.inflate(ConfirmOrderActivity.this, R.layout.item_product, null);
			holder.ll.addView(v);
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
