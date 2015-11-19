package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kingtogroup.domain.MemberCard;
import com.kingtogroup.view.MyListView;
import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MemberCardActivity extends MainActionBarActivity {
	TextView tv_money;
	ListView lv;
	View progress;
	MyListViewAdapter adapter;
	List<MemberCard> cards = new ArrayList<MemberCard>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_membercard);
		
		titleButton.setText("会员卡");
		
		init();
	}

	void init() {
		tv_money = (TextView) findViewById(R.id.tv_money);
		lv = (ListView) findViewById(R.id.lv);
		progress = findViewById(R.id.progress);
		
		spanTextSize(tv_money);
		requestData();
	}

	void requestData() {
		progress.setVisibility(View.VISIBLE);
		String uid = UserBean.getUSerBean().getUid();
		if (TextUtils.isEmpty(uid))
			return;
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "http://kingtopgroup.com/api/ucenter/ShipCardList?uid="
				+ uid;
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					try {
						JSONObject object = new JSONObject(new String(arg2));
						JSONArray array = object.getJSONArray("ShipCardList");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							int pId = obj.getInt("pid");
							int price = obj.getInt("shopprice");
							int weight = obj.getInt("weight");
							cards.add(new MemberCard(pId, price, weight));
						}
						fillData();
						JSONObject account = object.getJSONObject("Account");
						tv_money.setText(account.getInt("CurrentAmount") + "元");
						spanTextSize(tv_money);
					} catch (JSONException e) {
						progress.setVisibility(View.GONE);
						toastMsg("啊哦，出现错误了，重新试一次吧^^");
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toastMsg("请求失败，请重试");
				progress.setVisibility(View.GONE);
			}
		});
	}

	void toastMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	void fillData() {
		if (adapter == null) {
			adapter = new MyListViewAdapter();
			lv.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		progress.setVisibility(View.GONE);
	}

	class ViewHolder {
		TextView tv1;
		TextView tv2;
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cards.size();
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
				convertView = View.inflate(MemberCardActivity.this,
						R.layout.item_card, null);
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
				convertView.setTag(holder);
			}
			MemberCard card = cards.get(position);
			holder.tv1.setText("储值￥ " + card.price);
			holder.tv2.setText("送￥ " + card.weight);
			return convertView;
		}

	}

	public static void spanTextSize(TextView textView) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		builder.setSpan(new AbsoluteSizeSpan(20), text.length() - 1,
				text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);

	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rightButtonClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}
}
