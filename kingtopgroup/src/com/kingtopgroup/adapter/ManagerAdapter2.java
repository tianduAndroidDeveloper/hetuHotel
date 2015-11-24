package com.kingtopgroup.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ManagerAdapter2 extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ManagerBean> managerList;
	static int checkedCount = 0;
	public String masserger;
	private int showCount;

	public static void setCheckedCount(int checkedCount) {
		ManagerAdapter2.checkedCount = checkedCount;
	}

	public ManagerAdapter2(Context context, List<ManagerBean> managerList,
			int showCount) {
		this.context = context;
		this.managerList = managerList;
		this.showCount = showCount;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return showCount;
	}

	@Override
	public Object getItem(int arg0) {
		return getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		/*
		 * if (position == 0) { TextView tv = new TextView(context);
		 * tv.setText("推拿师"); tv.setPadding(10, 10, 10, 10);
		 * tv.setTextColor(Color.BLACK); return tv; }
		 */
		if (position == showCount) {
			LinearLayout ll = new LinearLayout(context);
			ll.setPadding(20, 20, 20, 20);
			TextView tv = new TextView(context);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			ll.setLayoutParams(params);
			tv.setBackgroundResource(R.drawable.personal_bg);
			tv.setPadding(10, 10, 10, 10);
			tv.setTextColor(Color.RED);
			tv.setText("没有合适的推拿师?去看看更多");
			ll.addView(tv);
			return ll;
		}
		if (view == null)
			view = View.inflate(context, R.layout.manager_item, null);
		ViewHolder holder = (ViewHolder) view.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.Address = (TextView) view.findViewById(R.id.address);
			holder.distance = (TextView) view.findViewById(R.id.distance);
			holder.logo = (ImageView) view.findViewById(R.id.logo);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.sex = (TextView) view.findViewById(R.id.sex);
			holder.isChecked = (CheckBox) view.findViewById(R.id.isChecked);
			view.setTag(holder);
		}
		final ManagerBean bean = managerList.get(position);
		Log.i("ManagerAdapter2", "position = " + position);
		holder.Address.setText(bean.address);
		holder.name.setText(bean.name);
		String sex;
		if (bean.sex.equals("1")) {
			sex = "男";
		} else {
			sex = "女";
		}
		holder.sex.setText(sex);
		String uri = "http://kingtopgroup.com/upload/store/" + bean.StoreId
				+ "/logo/thumb150_150/" + bean.Logo;
		ImageLoader.getInstance().displayImage(uri.trim(), holder.logo);
		holder.isChecked
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {

						managerList.get(position).isChecked = arg1;
						if (arg1 == true) {
							checkedCount++;
							UserBean.getUSerBean().putMassage(bean);
						} else {
							checkedCount--;
							UserBean.getUSerBean().removeMassage(bean);
						}
					}
				});
		holder.isChecked.setChecked(bean.isChecked);
		return view;

	}

	class ViewHolder {
		TextView name;
		TextView sex;
		ImageView logo;
		TextView Address;
		TextView distance;
		CheckBox isChecked;
	}

	public int getCheckedCount() {
		return checkedCount;
	}

	public String getCheckedIds() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < managerList.size(); i++) {
			ManagerBean bean = managerList.get(i);
			if (bean.isChecked)
				sb.append(bean.StoreId + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
