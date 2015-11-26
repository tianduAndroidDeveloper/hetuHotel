package com.kingtopgroup.activty;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;

import com.kingtopgroup.R;
import com.kingtopgroup.adapter.manipulationAdapter;
import com.stevenhu.android.phone.utils.LunboImageUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class manipulationActivty extends Activity {
	private static final String TAG = "manipulationActivty";
	private CycleViewPager cycleViewPager;
	private ListView orderListview;
	private List<Map<String, Object>> list = null;
	JSONArray attributeArray;
	// 需要循环的图片
	String[] imageUrls = { "http://kingtopgroup.com/mobile/images/banner01.jpg", "http://kingtopgroup.com/mobile/images/banner02.jpg", "http://kingtopgroup.com/mobile/images/banner03.jpg" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manipulationlistview);

		orderListview = (ListView) findViewById(R.id.orderListview);
		if (getIntent().getBooleanExtra("lubo", false)) {
			View pv = LayoutInflater.from(this).inflate(R.layout.photos_lubo, null);

			cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
			orderListview.addHeaderView(pv);
			// 图片轮播
			LunboImageUtil lb = new LunboImageUtil();
			lb.initialize(this, imageUrls, cycleViewPager);
		}
		try {
			attributeArray = new JSONArray(getIntent().getStringExtra("attributes"));
			Log.i(TAG, attributeArray.toString());
			setstatus(JsonObjectToListMap(new JSONArray(getIntent().getStringExtra("json"))));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	JSONArray array;

	public void setstatus(List<Map<String, Object>> list) {

		orderListview.setAdapter(new manipulationAdapter(manipulationActivty.this, list));
		orderListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				HashMap<String, Object> obj = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				String pid = (String) obj.get("pid");
				// String storeid = (String) obj.get("storeid");
				String name = (String) obj.get("name");
				String time = (String) obj.get("time");
				String price = (String) obj.get("marketprice");
				String beginnum = (String) obj.get("beginnum");
				String image = (String) obj.get("order_item_image1");
				Intent inten = new Intent(manipulationActivty.this, ServieNumActivty.class);
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putString("time", time);
				bundle.putString("price", price);
				bundle.putString("beginnum", beginnum);
				bundle.putString("image", image);
				bundle.putString("pid", pid);

				inten.putExtra("zuliao", !getIntent().getBooleanExtra("lubo", false));
				inten.putExtras(bundle);
				startActivity(inten);
			}
		});

	}

	// 复写方法
	public List<Map<String, Object>> JsonObjectToListMap(JSONArray objs) {
		list = new ArrayList<Map<String, Object>>();
		if (objs == null || objs.length() < 1)
			return list;
		try {

			for (int i = 0; i < objs.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String pname = objs.getJSONObject(i).getString("pname");
				String weight = objs.getJSONObject(i).getString("weight");
				String marketprice = objs.getJSONObject(i).getString("marketprice");
				String beginnum = objs.getJSONObject(i).getString("beginnum");
				String showimg = objs.getJSONObject(i).getString("showimg");
				String pid = objs.getJSONObject(i).getString("pid");
				String storeid = objs.getJSONObject(i).getString("storeid");

				map.put("name", pname);
				map.put("time", weight);
				map.put("marketprice", marketprice);
				map.put("beginnum", beginnum);
				map.put("zuo", R.drawable.zuob);
				map.put("pid", pid);
				map.put("storeid", storeid);
				map.put("order_item_image1", "http://kingtopgroup.com/upload/store/5/product/show/thumb190_190/" + showimg);
				for (int j = 0; j < attributeArray.length(); j++) {
					JSONObject attributeObject = attributeArray.optJSONObject(j);
					Log.i(TAG, "attrPid = " + attributeObject.optString("pid"));
					if (attributeObject.optString("pid").equals(pid)) {
						map.put("attrvalue", attributeObject.optString("attrvalue"));
					}
				}
				list.add(map);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
