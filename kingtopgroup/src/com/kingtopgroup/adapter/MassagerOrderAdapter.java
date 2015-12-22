package com.kingtopgroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingtogroup.domain.OrderInfo;
import com.kingtogroup.domain.Product;
import com.kingtogroup.utils.Utils;
import com.kingtopgroup.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MassagerOrderAdapter extends BaseAdapter {
	List<OrderInfo> mOrders;
	Context mContext;
	boolean mIsReceive;
	ReceiveClickListener mListener;
	String imagePath = "http://kingtopgroup.com";

	public MassagerOrderAdapter(List<OrderInfo> orders, Context context, boolean isReceive, ReceiveClickListener listner) {
		this.mOrders = orders;
		this.mContext = context;
		this.mIsReceive = isReceive;
		this.mListener = listner;
	}

	@Override
	public int getCount() {
		return mOrders.size();
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
			convertView = View.inflate(mContext, R.layout.item_receving, null);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null)
			holder = new ViewHolder(convertView);
		holder.fillData(position);
		return convertView;
	}

	class ViewHolder {
		public ViewHolder(View convertView) {
			tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			tv_upload = (TextView) convertView.findViewById(R.id.tv_upload);
			iv_uploaded = (ImageView) convertView.findViewById(R.id.iv_uploaded);
			ll = (LinearLayout) convertView.findViewById(R.id.ll);

			tv_receive = (TextView) convertView.findViewById(R.id.tv_receive);
			tv_receive.setBackgroundResource(mIsReceive ? R.drawable.red_bg : R.drawable.textview_disable);
			tv_receive.setEnabled(mIsReceive);
			tv_receive.setPadding(Utils.dp2px(mContext, 15), Utils.dp2px(mContext, 5), Utils.dp2px(mContext, 15), Utils.dp2px(mContext, 5));
			tv_receive.setText(mIsReceive ? "接单" : "已接单");
			tv_upload.setVisibility(mIsReceive ? View.GONE : View.VISIBLE);
			iv_uploaded.setVisibility(mIsReceive ? View.GONE : View.VISIBLE);
			convertView.setTag(this);
		}

		TextView tv_num;
		TextView tv_status;
		TextView tv_address;
		TextView tv_name;
		TextView tv_phone;
		TextView tv_receive;
		TextView tv_upload;
		ImageView iv_uploaded;
		LinearLayout ll;

		void fillData(int position) {
			final OrderInfo orderInfo = mOrders.get(position);
			tv_num.setText("订单号：" + orderInfo.OSN);
			tv_name.setText("联系人：" + orderInfo.Consignee);
			tv_phone.setText("联系电话：" + orderInfo.Mobile);
			tv_address.setText("地址：" + orderInfo.Address);
			fillProducts(orderInfo.products);
			tv_receive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mListener.onReceiveClick(orderInfo.Oid);
				}
			});
			tv_upload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mListener.onUploadClick(orderInfo.Oid);
				}
			});
			iv_uploaded.setVisibility(orderInfo.imgUrl.isEmpty() ? View.GONE : View.VISIBLE);
			if (!orderInfo.imgUrl.isEmpty()) {
				String url = imagePath + orderInfo.imgUrl;
				ImageLoader.getInstance().displayImage(url, iv_uploaded);
			}
		}

		void fillProducts(List<Product> products) {
			ll.removeAllViews();
			for (int i = 0; i < products.size(); i++) {
				View v = View.inflate(mContext, R.layout.item_product, null);
				Product product = products.get(i);
				ProductHolder productHolder = new ProductHolder(v);
				productHolder.fillData(product);
				ll.addView(v);
			}
		}
	}

	class ProductHolder {
		public ProductHolder(View v) {
			iv = (ImageView) v.findViewById(R.id.imageView1);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_people = (TextView) v.findViewById(R.id.tv_people);
			tv_count = (TextView) v.findViewById(R.id.tv_count);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_sum = (TextView) v.findViewById(R.id.tv_sum);
			tv_money = (TextView) v.findViewById(R.id.tv_money);
		}

		ImageView iv;
		TextView tv_type;
		TextView tv_people;
		TextView tv_count;
		TextView tv_time;
		TextView tv_sum;
		TextView tv_money;

		void fillData(Product product) {
			String uri = Utils.assembleImageUri(product.ShowImg, "5");
			String time = product.PServiceDate + " " + product.ServiceTime;

			tv_type.setText("项目：" + product.Name);
			tv_people.setText("推拿师：" + product.MassagerNames);
			tv_count.setText("数量：" + product.BuyCount);
			tv_time.setText("预约时间：" + time);
			tv_sum.setText(product.Name + "x" + product.RealCount);
			tv_money.setText("￥" + product.ShopPrice * product.RealCount);

			ImageLoader.getInstance().displayImage(uri, iv);
		}
	}

	public void refresh(List<OrderInfo> orders) {
		this.mOrders = orders;
		this.notifyDataSetChanged();
	}

	public interface ReceiveClickListener {
		void onReceiveClick(int oid);

		void onUploadClick(int oid);
	}

}
