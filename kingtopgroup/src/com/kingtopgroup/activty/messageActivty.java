package com.kingtopgroup.activty;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtogroup.location.DistanceUtils;
import com.kingtogroup.location.LastLocation;
import com.kingtogroup.messager.DiscusActivity;
import com.kingtopgroup.R;
import com.makeramen.RoundedImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class messageActivty extends Activity{
	private ListView mListView;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagelistview);
		mContext = this;
		mListView = (ListView) findViewById(R.id.listView);
		Adapter adapter = new Adapter(mContext,getIntent().getStringExtra("json"));
		mListView.setAdapter(adapter);
	}

}

class Adapter extends BaseAdapter{
	JSONArray dataList;
	Context mContext;
	public Adapter(Context context,String json){
		this.mContext = context;
		try {
			dataList = new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		
		return dataList==null?0:dataList.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.optJSONObject(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.messager_info_item, null);
			holder = new ViewHolder();
			holder.initwidget(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.initData(dataList.optJSONObject(position));
		return convertView;
	}
	
	class ViewHolder{
		TextView nameSex,metter,discus,location,introduce,level;
		View discusLayout;
		RoundedImageView headPhoto;
		FinalBitmap finalBitMap=FinalBitmap.create(mContext);
		public void initwidget(View view){
			this.nameSex = (TextView) view.findViewById(R.id.nameSex);
			this.metter = (TextView) view.findViewById(R.id.metter);
			this.discus = (TextView) view.findViewById(R.id.discusCount);
			this.location = (TextView) view.findViewById(R.id.location);
			this.introduce = (TextView) view.findViewById(R.id.introduce);
			this.level = (TextView) view.findViewById(R.id.level);
			this.headPhoto = (RoundedImageView) view.findViewById(R.id.headPhoto);
			this.discusLayout = view.findViewById(R.id.discusLayout);
			this.discusLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					JSONObject obj = (JSONObject) v.getTag();
					Intent intent = new Intent(mContext,DiscusActivity.class);
					intent.putExtra("json", obj.toString());
					mContext.startActivity(intent);
				}
			});
		}
		
		public void initData(JSONObject obj){
			if(obj == null)
				return;
			this.nameSex.setText(obj.optString("name").trim() + "  " + (obj.optInt("sex")==0?"Å®":"ÄÐ"));
			try{
			double lat = obj.optDouble("point_x", 0);
			double lgn = obj.optDouble("point_y", 0);
			
			double distance = DistanceUtils.GetDistance(lat, lgn, LastLocation.initInstance().getLatitude(), LastLocation.initInstance().getLongitude());
			this.metter.setText(String.valueOf(distance));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			this.discus.setText(obj.optString("announcement").trim());
			this.location.setText(obj.optString("regionName").trim());
			this.introduce.setText(obj.optString("description").trim());
			this.level.setText(obj.optString("title").trim());
			
			finalBitMap.display(this.headPhoto, mContext.getString(R.string.url_imgHost) + obj.optString("logo").trim(),BitmapFactory.decodeResource(mContext.getResources(), R.drawable.photos));
			this.discusLayout.setTag(obj);
		}
	}
	
}
