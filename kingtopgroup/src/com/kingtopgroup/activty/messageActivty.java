package com.kingtopgroup.activty;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kingtogroup.location.DistanceUtils;
import com.kingtogroup.location.LastLocation;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class messageActivty extends Activity {
	private static final String TAG = "messageActivty";
	private ListView mListView;
	private Context mContext;
	private ACache acache;
	TextView discusCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagelistview);
		mContext = this;
		mListView = (ListView) findViewById(R.id.listView);
		Adapter adapter = new Adapter(mContext, getIntent().getStringExtra(
				"json"));
		mListView.setAdapter(adapter);
	}

}

class Adapter extends BaseAdapter {
	JSONArray dataList;
	Context mContext;

	public Adapter(Context context, String json) {
		this.mContext = context;
		try {
			dataList = new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {

		return dataList == null ? 0 : dataList.length();
	}

	@Override
	public Object getItem(int position) {
		return dataList.optJSONObject(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.messager_info_item, null);
			holder = new ViewHolder();
			holder.initwidget(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.initData(dataList.optJSONObject(position));
		return convertView;
	}

	class ViewHolder {
		TextView nameSex, metter, discus, location, introduce, level,discusCount;
		View discusLayout;
		ImageView headPhoto;
		FinalBitmap finalBitMap = FinalBitmap.create(mContext);

		public void initwidget(View view) {
			this.nameSex = (TextView) view.findViewById(R.id.nameSex);
			this.metter = (TextView) view.findViewById(R.id.metter);
			this.discus = (TextView) view.findViewById(R.id.discusCount);
			this.location = (TextView) view.findViewById(R.id.location);
			this.introduce = (TextView) view.findViewById(R.id.introduce);
			this.level = (TextView) view.findViewById(R.id.level);
			this.headPhoto = (ImageView) view
					.findViewById(R.id.headPhoto);
			this.discusLayout = view.findViewById(R.id.discusLayout);
			
			this.discusCount=(TextView) view.findViewById(R.id.discusCount);

			this.discusLayout.setOnClickListener(new OnClickListener() {
				private ACache acache;

				@Override
				public void onClick(View v) {
					JSONObject obj = (JSONObject) v.getTag();
					Intent intent = new Intent(mContext, DiscusActivity.class);
					intent.putExtra("json", obj.toString());
					acache = ACache.get(mContext);
					acache.put("discus_json", obj.toString());
					mContext.startActivity(intent);
				}
			});

			// 点击头像
			this.headPhoto.setOnClickListener(new OnClickListener() {
				private ACache acache;
				@Override
				public void onClick(View v) {
					JSONObject obj = (JSONObject) v.getTag();
					Intent inten = new Intent(mContext,
							MessagerDetialActivty.class);
					inten.putExtra("json", obj.toString());
					acache = ACache.get(mContext);
					acache.put("discus_json", obj.toString());
					//inten.putExtra("discount_num",v.getTag(12).toString());
					mContext.startActivity(inten);

				}
			});

		}

		public void initData(final JSONObject obj) {
			
			if (obj == null)
				return;
			this.nameSex.setText(obj.optString("name").trim() + "  "
					+ (obj.optInt("sex") == 0 ? "女" : "男"));
			try {
				double lat = obj.optDouble("point_x", 0);
				double lgn = obj.optDouble("point_y", 0);

				double distance = DistanceUtils.GetDistance(lat, lgn,
						LastLocation.initInstance().getLatitude(), LastLocation
								.initInstance().getLongitude());
				this.metter.setText(String.valueOf(distance));
			} catch (Exception e) {
				e.printStackTrace();
			}
             
			this.discus.setText(obj.optString("announcement").trim());
			this.location.setText(obj.optString("regionName").trim());
			this.introduce.setText(obj.optString("description").trim());
			this.level.setText(obj.optString("title").trim());
			String uri = "http://kingtopgroup.com/upload/store/" + obj.optString("storeid")
					+ "/logo/thumb150_150/" + obj.optString("logo");
			Log.i("messageActivty", uri);
			ImageLoader.getInstance().displayImage(uri.trim(), this.headPhoto);
			
			//获取评价数
			String storeid=obj.optString("storeid");
			
			
			RequestParams params=AsyncHttpCilentUtil.getParams();
			params.put("massagerid", storeid);
			AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_masserger_list_url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					if(arg0==200){
						String date =new String(arg2);
						JSONObject obj2;
						try {
							obj2 = new JSONObject(date);
							String ReviewTotalList=obj2.getString("ReviewTotalList");
				        	JSONArray array=new JSONArray(ReviewTotalList);
				        	discusCount.setText(array.length()+"");
				        	obj.put("discusCount", array.length()+"");
				        	//headPhoto.setTag(12, array.length());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						//try {
							//JSONObject obj=new JSONObject(date);
							//String ReviewTotalList=obj.getString("ReviewTotalList");
							//initViewPager(date);
						//} catch (JSONException e) {
						//	e.printStackTrace();
						//}
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					
				}
			});
			
		/*} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	
		//}
    
   // }
			
			this.discusLayout.setTag(obj);
			this.headPhoto.setTag(obj);
			

		}
	}

}
