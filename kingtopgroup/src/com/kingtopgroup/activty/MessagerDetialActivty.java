package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.manipulationAdapter;
import com.kingtopgroup.constant.ConstanceUtil;
import com.kingtopgroup.constant.finalBitmapUtil;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;

public class MessagerDetialActivty extends MainActionBarActivity implements OnClickListener {
	private static final String TAG = "MessagerDetialActivty";
	private ListView discus_listview;
	private TextView all_discus, discus_tv, name, sex, address,
			person_introduce, RankTitle,discuss_count;
	private ACache acache;
	String discus_json = null;
	private List<Map<String, Object>> list = null;
	ImageView photo;
	View progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messager_detail);
		titleButton.setText("推拿师详情");
		all_discus = (TextView) findViewById(R.id.all_discus);
		acache = ACache.get(this);
		if (acache.getAsString("discus_json") != null) {
			discus_json = acache.getAsString("discus_json");
		}

		String StoreId = getIntent().getStringExtra("json");
		String count="";
		try {
			JSONObject ob = new JSONObject(StoreId);
			StoreId = ob.getString("storeid");
			count=ob.getString("discusCount");
			discuss_count=(TextView) findViewById(R.id.discuss_count);
			discuss_count.setText(count+"人评价");
			all_discus.setText("全部评价:("+count+")");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

		
		discus_tv = (TextView) findViewById(R.id.discus_tv);
		photo = (ImageView) findViewById(R.id.headPhoto);
		progress = findViewById(R.id.progress);

		all_discus.setOnClickListener(this);
		discus_tv.setOnClickListener(this);

		discus_listview = (ListView) findViewById(R.id.discus_listview);

		RequestParams params = AsyncHttpCilentUtil.getParams();
		params.put("massid", StoreId);
		progress.setVisibility(View.VISIBLE);
		AsyncHttpCilentUtil.getInstance().get(
				ConstanceUtil.get_masserger_detail_list, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if (arg0 == 200) {
							list = new ArrayList<Map<String, Object>>();
							String date = new String(arg2);
							try {
								JSONObject obj = new JSONObject(date);
								JSONArray array = obj.getJSONArray("ItemList");
								// JSONObject obj2=(JSONObject)
								// obj.opt("Massger");
								// JSONObject obj3=(JSONObject)
								// obj.opt("RegionName");
								initData(obj);
								// obj.getString("Massger");
								for (int i = 0; i < array.length(); i++) {
									Map<String, Object> map = new HashMap<String, Object>();
									// 项目名称
									String pname = array.getJSONObject(i)
											.getString("pname");
									// 时长
									String weight = array.getJSONObject(i)
											.getString("weight");
									// 价格
									String marketprice = array.getJSONObject(i)
											.getString("marketprice");
									// 一人起订
									String beginnum = array.getJSONObject(i)
											.getString("beginnum");
									// 图片
									String showimg = array.getJSONObject(i)
											.getString("showimg");
									// pid
									String pid = array.getJSONObject(i)
											.getString("pid");

									String storeid = array.getJSONObject(i)
											.getString("storeid");
									String CouponId = "0";

									UserBean.getUSerBean()
											.setCouponid(CouponId);

									map.put("name", pname);
									map.put("time", weight);
									map.put("marketprice", marketprice);
									map.put("beginnum", beginnum);
									map.put("zuo", R.drawable.zuob);
									map.put("pid", pid);
									map.put("storeid", storeid);
									map.put("order_item_image1",
											"http://kingtopgroup.com/upload/store/5/product/show/thumb190_190/"
													+ showimg);
									list.add(map);
								}

								discus_listview
										.setAdapter(new manipulationAdapter(
												MessagerDetialActivty.this,
												list));
								setDate();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							progress.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						progress.setVisibility(View.GONE);
					}
				});
	}

	public void initData(JSONObject obj) {
		if (obj == null)
			return;

		JSONObject obj2 = (JSONObject) obj.opt("Massger");
		// 姓名
		name = (TextView) findViewById(R.id.name);
		name.setText(obj2.optString("Name").trim() + "  "
				+ (obj2.optInt("Sex") == 0 ? "女" : "男"));

		// 好评度
		String discus_tvs = obj.optString("GoodPercent") + "%好评度>";
		discus_tv.setText(discus_tvs);

		// 个人介绍
		String introduce = obj2.optString("Description");
		person_introduce = (TextView) findViewById(R.id.person_introduce);
		person_introduce.setText("个人介绍 :        " + introduce);

		// 位置
		String RegionName = obj.optString("RegionName");
		address = (TextView) findViewById(R.id.address);
		address.setText(RegionName);

		// 职称
		String RankTitles = obj.optString("RankTitle");
		RankTitle = (TextView) findViewById(R.id.RankTitle);
		RankTitle.setText(RankTitles);
		// storeid
		String store = obj2.optString("StoreId");

		// 照片
		String photos = obj2.optString("Logo");// /upload/store/11/logo/thumb150_150/s_1509031744431521205.jpg// /upload/store/10/logo/thumb150_150/s_1509031744186077167.jpg
		String uri = "http://kingtopgroup.com/upload/store/" + store + "/logo/thumb150_150/" + photos;
		ImageLoader.getInstance().displayImage(uri, (ImageView)findViewById(R.id.headphoto));
		
		//评价数量
		
		//discuss_count.setText(getIntent().getStringExtra("discount_num"));

	}

	private void setDate() {

		discus_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> obj = (HashMap<String, Object>) arg0
						.getItemAtPosition(arg2);
				String messager_name=(String) name.getText();
				
				String pid = (String) obj.get("pid");
				String storeid = (String) obj.get("storeid");
				String name = (String) obj.get("name");
				String time = (String) obj.get("time");
				String price = (String) obj.get("marketprice");
				String beginnum = (String) obj.get("beginnum");
				String image = (String) obj.get("order_item_image1");
				Intent inten = new Intent(MessagerDetialActivty.this,
						ServieNumActivty.class);
				Bundle bundle = new Bundle();
				bundle.putString("messager_name", messager_name);
				bundle.putString("name", name);
				bundle.putString("time", time);
				bundle.putString("price", price);
				bundle.putString("beginnum", beginnum);
				bundle.putString("image", image);
				bundle.putString("pid", pid);
				bundle.putString("messagerDteail", "1");

				UserBean.getUSerBean().setPid(pid);
				UserBean.getUSerBean().setStoreRId(storeid);

				inten.putExtras(bundle);
				startActivity(inten);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.discus_tv:
			Intent intent = new Intent(this, DiscusActivity.class);
			intent.putExtra("json", discus_json);
			startActivity(intent);
			break;

		case R.id.all_discus:
			Intent intents = new Intent(this, DiscusActivity.class);
			intents.putExtra("json", discus_json);
			startActivity(intents);
			break;
		}

	}

	@Override
	public void backButtonClick(View v) {
		finish();
	}

	@Override
	public void titleButtonClick(View v) {
		
	}

	@Override
	public void rightButtonClick(View v) {
		
	}

	@Override
	public Boolean showHeadView() {
		return true;
	}

}
