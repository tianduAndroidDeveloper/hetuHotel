package com.kingtopgroup.adapter;

import java.util.List;
import java.util.Map;

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

import com.kingtopgroup.R;

public class ManagerAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Map<String, Object>> managerList;
	int checkedCount = 0;

	public ManagerAdapter(Context context, List<Map<String, Object>> managerList) {
		this.context = context;
		this.managerList = managerList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return managerList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	class people {
		public String name;
		String sex;
		String address;
		boolean isChecked;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.manager_item, null);
			viewHolder.Address = (TextView) view.findViewById(R.id.address);
			viewHolder.distance = (TextView) view.findViewById(R.id.distance);
			viewHolder.logo = (ImageView) view.findViewById(R.id.logo);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.sex = (TextView) view.findViewById(R.id.sex);
			viewHolder.isChecked = (CheckBox) view.findViewById(R.id.isChecked);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// viewHolder.isChecked.setChecked(checked)
		viewHolder.Address.setText((CharSequence) managerList.get(arg0).get(
				"Address"));
		viewHolder.name.setText((CharSequence) managerList.get(arg0)
				.get("name"));
		viewHolder.sex.setText((CharSequence) managerList.get(arg0).get("Sex"));
		
		viewHolder.isChecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				
			}
		});

		String point_x = (String) managerList.get(arg0).get("Point_X");// http://kingtopgroup.com/upload/store/14/logo/thumb100_100/
		String point_y = (String) managerList.get(arg0).get("point_y");// /upload/store/14/logo/thumb100_100/s_1509031745435625758.jpg
		// finalBitmapUtil.getFinalBitmap(context).display(viewHolder.logo,
		// "http://kingtopgroup.com/upload/store/14/logo/thumb100_100/"+(String)
		// managerList.get(arg0).get("Logo"));
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
		return 0;
	}

}
