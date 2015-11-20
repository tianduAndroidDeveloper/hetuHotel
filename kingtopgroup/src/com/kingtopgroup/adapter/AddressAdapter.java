package com.kingtopgroup.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.AddAddressActivity;
import com.kingtopgroup.activty.ServiceAddressActivty;

public class AddressAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<Map<String,Object>> addressList;
	public AddressAdapter(Context context,List<Map<String,Object>> addressList){
		this.context=context;
		this.addressList=addressList;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return addressList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return addressList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder viewholder = null;
		if(view==null){
			viewholder=new ViewHolder();
			view=inflater.inflate(R.layout.address_item, null);
			viewholder.name=(TextView) view.findViewById(R.id.name);
			viewholder.phone=(TextView) view.findViewById(R.id.phone);
			viewholder.add_address=(TextView) view.findViewById(R.id.add_address);
			viewholder.isDefault=(RadioButton) view.findViewById(R.id.isdefault);
			
			view.setTag(viewholder);
		}else{
			viewholder=(ViewHolder) view.getTag();
		}
		viewholder.name.setText((CharSequence) addressList.get(arg0).get("person"));
		viewholder.phone.setText((CharSequence) addressList.get(arg0).get("phone"));
		viewholder.add_address.setText((CharSequence) addressList.get(arg0).get("street"));
		return view;
	}
	
	class ViewHolder{
		TextView name;
		TextView phone;
		TextView add_address;
		RadioButton isDefault;
	}

}
