package com.kingtopgroup.adapter;

import java.util.List;

import android.content.Context;
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

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ManagerBean;

public class ManagerAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<ManagerBean> managerList;
	int checkedCount = 0;
	private static final String TAG = "ManagerAdapter";

	public ManagerAdapter(Context context, List<ManagerBean> managerList) {
		this.context = context;
		this.managerList = managerList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return managerList.size();
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
		/*
		 * ViewHolder viewHolder; if (view == null) { viewHolder = new
		 * ViewHolder(); view = inflater.inflate(R.layout.manager_item, null);
		 * viewHolder.Address = (TextView) view.findViewById(R.id.address);
		 * viewHolder.distance = (TextView) view.findViewById(R.id.distance);
		 * viewHolder.logo = (ImageView) view.findViewById(R.id.logo);
		 * viewHolder.name = (TextView) view.findViewById(R.id.name);
		 * viewHolder.sex = (TextView) view.findViewById(R.id.sex);
		 * viewHolder.isChecked = (CheckBox) view.findViewById(R.id.isChecked);
		 * view.setTag(viewHolder); } else { viewHolder = (ViewHolder)
		 * view.getTag(); } final ManagerBean bean = managerList.get(position);
		 * // viewHolder.isChecked.setChecked(checked) viewHolder.Address
		 * .setText((CharSequence) managerList.get(position).address);
		 * viewHolder.name.setText((CharSequence)
		 * managerList.get(position).name);
		 * viewHolder.sex.setText((CharSequence) managerList.get(position).sex);
		 * viewHolder.isChecked.setOnCheckedChangeListener(new
		 * OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton arg0, boolean
		 * arg1) { Toast.makeText(context, "position = " + position +
		 * ",isChecked = " + arg1, 1).show(); bean.isChecked = arg1; } });
		 * 
		 * viewHolder.isChecked.setChecked(bean.isChecked);
		 * 
		 * // String point_x = (String) managerList.get(arg0).get("Point_X");//
		 * // http://kingtopgroup.com/upload/store/14/logo/thumb100_100/ //
		 * String point_y = (String) managerList.get(arg0).get("point_y");// //
		 * /upload/store/14/logo/thumb100_100/s_1509031745435625758.jpg //
		 * finalBitmapUtil.getFinalBitmap(context).display(viewHolder.logo, //
		 * "http://kingtopgroup.com/upload/store/14/logo/thumb100_100/"+(String)
		 * // managerList.get(arg0).get("Logo")); return view;
		 */

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
		holder.sex.setText(bean.sex);
		holder.isChecked
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						Toast.makeText(context, position + "", 0).show();
						managerList.get(position).isChecked = arg1;
					}
				});
		holder.isChecked.setChecked(bean.isChecked);
		return view;

	}

	class MyCheckBoxChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

		}

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
		return 0;
	}

}
