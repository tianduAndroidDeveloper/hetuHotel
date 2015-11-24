package com.kingtopgroup.adapter;

import java.text.ChoiceFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.SyncStateContract.Constants;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.ChioceManagerActivty;
import com.kingtopgroup.activty.OrderTimeActivty;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class OrderTimeAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private LinearLayout.LayoutParams params;
	private List<Map<String, Object>> nameList;
	private boolean isToDay = false;
	private CallBack mCallBack;
	String date;

	public OrderTimeAdapter(Context context, List<Map<String, Object>> nameList, boolean isToDay, CallBack callBack, String date) {
		this.context = context;
		this.nameList = nameList;
		this.isToDay = isToDay;
		this.mCallBack = callBack;
		this.date = date;
		inflater = LayoutInflater.from(context);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public int getCount() {
		return nameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return nameList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View conveView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (conveView == null) {
			conveView = inflater.inflate(R.layout.oreder_time_item, null);
			viewHolder = new ViewHolder();
			viewHolder.buttoTime = (Button) conveView.findViewById(R.id.order_time_time);
			conveView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) conveView.getTag();
		}

		if (isToDay) {
			try {

				String[] sections = (String.valueOf(nameList.get(arg0).get("TimeSection"))).split(":");
				int minute = Integer.parseInt(sections[1]) - 30;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
				Date d2 = sdf.parse(sections[0] + ":" + minute);
				String section2 = sdf.format(new Date());
				Date d1 = sdf.parse(section2);
				viewHolder.buttoTime.setBackgroundResource(d1.before(d2) ? R.drawable.shape_select_pop : R.drawable.shape_gb_pop);
				viewHolder.buttoTime.setTextColor(d1.before(d2) ? Color.WHITE : Color.BLACK);
				viewHolder.buttoTime.setEnabled(d1.before(d2));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		} else {
			viewHolder.buttoTime.setBackgroundResource(R.drawable.shape_select_pop);
			viewHolder.buttoTime.setTextColor(Color.WHITE);
			viewHolder.buttoTime.setEnabled(true);
		}

		viewHolder.buttoTime.setText((CharSequence) nameList.get(arg0).get("TimeSection"));
		viewHolder.buttoTime.setTag((CharSequence) nameList.get(arg0).get("TimeSection"));
		viewHolder.buttoTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ChioceManagerActivty.class);
				intent.putExtra("date", date + " " + arg0.getTag());
				context.startActivity(intent);
			}
		});
		
		return conveView;
	}

	class ViewHolder {
		Button buttoTime;
	}

	public interface CallBack {
		void callBack(String stid);
	}

}
