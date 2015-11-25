package com.kingtopgroup.adapter;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.MainActivity;
import com.kingtopgroup.activty.manipulationActivty;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class manipulationAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private List<Map<String, Object>> ItemInfoList = null;
	private Context context = null;
	private FinalBitmap finalBitMap = null;

	public manipulationAdapter(Context context,
			List<Map<String, Object>> ItemInfoList) {
		finalBitMap = FinalBitmap.create(context);
		this.inflater = LayoutInflater.from(context);
		this.ItemInfoList = ItemInfoList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return ItemInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return ItemInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.orderitem, null);
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.order_item_name = (TextView) convertView
					.findViewById(R.id.order_item_name);
			holder.order_item_image1 = (ImageView) convertView
					.findViewById(R.id.order_item_image1);
			holder.order_item_time = (TextView) convertView
					.findViewById(R.id.time);
			holder.item_id = (LinearLayout) convertView
					.findViewById(R.id.main);
			holder.beginnum = (TextView) convertView
					.findViewById(R.id.order_item_person);
			holder.order_item_price = (TextView) convertView
					.findViewById(R.id.order_item_price);
			convertView.setTag(holder);
		}
		String name = ItemInfoList.get(position).get("name").toString();
		if(name.length()>7){
		holder.item_id.setOrientation(LinearLayout.VERTICAL);
		}else{
			holder.item_id.setOrientation(LinearLayout.HORIZONTAL);
		}

		holder.order_item_name.setId(Integer.parseInt((String) ItemInfoList
				.get(position).get("pid")));
		holder.order_item_name.setText((CharSequence) ItemInfoList
				.get(position).get("name"));
		holder.order_item_time.setText((CharSequence) ItemInfoList
				.get(position).get("time") + "∑÷÷”");
		holder.beginnum.setText("("
				+ (CharSequence) ItemInfoList.get(position).get("beginnum")
				+ "»À∆∂©)");
		holder.order_item_price.setText("£§"
				+ (CharSequence) ItemInfoList.get(position).get("marketprice"));
		ImageLoader.getInstance().displayImage(
				(String) ItemInfoList.get(position).get("order_item_image1"),
				holder.order_item_image1);

		return convertView;
	}

	static class ViewHolder {
		TextView beginnum;
		TextView order_item_name;
		LinearLayout item_id;
		TextView order_item_time;
		ImageView order_item_image1;
		TextView order_item_price;

	}

}
