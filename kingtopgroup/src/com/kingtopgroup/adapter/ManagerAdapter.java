package com.kingtopgroup.adapter;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.location.DistanceUtils;
import com.kingtogroup.location.LastLocation;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.finalBitmapUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ManagerAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ManagerBean> managerList;
	static int checkedCount = 0;
	public String masserger;
	int showCount;

	public static void setCheckedCount(int checkedCount) {
		ManagerAdapter.checkedCount = checkedCount;
	}

	public ManagerAdapter(Context context, List<ManagerBean> managerList, int showCount) {
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

	/*
	 * class people { public String name; String sex; String address; boolean
	 * isChecked; }
	 */

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
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
		holder.Address.setText(bean.address);
		holder.name.setText(bean.name);
		String sex;
		if (bean.sex.equals("1")) {
			sex = "ÄÐ";
		} else {
			sex = "Å®";
		}
		try {
			double lat = bean.point_x;
			double lgn = bean.point_y;
			double distance = DistanceUtils.GetDistance(lat, lgn, LastLocation.initInstance().getLatitude(), LastLocation.initInstance().getLongitude());

			holder.distance.setText(distance + "¹«Àï");
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.sex.setText(sex);// http://kingtopgroup.com/upload/store/10/logo/thumb100_100/s_1509031744186077167.jpg
		String uri = "http://kingtopgroup.com/upload/store/" + bean.StoreId + "/logo/thumb150_150/" + bean.Logo;
		ImageLoader.getInstance().displayImage(uri, holder.logo);
		holder.isChecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				managerList.get(position).isChecked = arg1;
				if (arg1 == true) {
					checkedCount++;
				} else {
					checkedCount--;
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
		if (sb.length() != 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public boolean isAnyChecked() {
		ManagerBean bean = managerList.get(0);
		return bean.isChecked;
	}
}
