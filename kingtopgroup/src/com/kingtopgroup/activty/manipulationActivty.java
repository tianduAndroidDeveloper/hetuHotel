package com.kingtopgroup.activty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;

import com.kingtogroup.view.MyListView;
import com.kingtopgroup.R;
import com.kingtopgroup.adapter.manipulationAdapter;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ADInfo;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.stevenhu.android.phone.utils.ACache;
import com.stevenhu.android.phone.utils.LunboImageUtil;
import com.stevenhu.android.phone.utils.ViewFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class manipulationActivty extends Activity{
	
	private ACache acache;
	private CycleViewPager cycleViewPager;
	private MyListView orderListview;
	private List<Map<String, Object>> list=null;
	private View progress;
	private static final AsyncHttpClient client=new AsyncHttpClient();
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.manipulationlistview);
	
	progress = findViewById(R.id.progress);
	//缓存到Achace 中
	acache=ACache.get(this);
	
	
	//适配listview
	/*if(getDate()==null){
		if(acache.getAsJSONObject("ItemList")!=null){
			  setstatus(JsonObjectToListMap(acache.getAsJSONObject("ItemList")));
		}
	}*/
	try {
		setstatus(JsonObjectToListMap(new JSONObject(getIntent().getStringExtra("json"))));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//getDate();
	//checkedNow();
    
	
	//需要循环的图片
	String[] imageUrls = {"http://kingtopgroup.com/mobile/images/banner01.jpg",
			"http://kingtopgroup.com/mobile/images/banner02.jpg",
			"http://kingtopgroup.com/mobile/images/banner03.jpg"};
	
	
	cycleViewPager = (CycleViewPager) getFragmentManager()
			.findFragmentById(R.id.fragment_cycle_viewpager_content);
	//图片轮播
	LunboImageUtil lb=new LunboImageUtil();
	lb.initialize(this,imageUrls,cycleViewPager);
	
	
		
	}


    
    public List<Map<String, Object>> JsonObjectToListMap (JSONObject obj){
    	list=new ArrayList<Map<String, Object>>(); 	
           JSONArray array;
			try {
				array = obj.getJSONArray("MassagesList");
				  for(int i=0;i<array.length();i++){
			            Map<String,Object> map=new HashMap<String,Object>();
			           	 //项目名称
			           	 String pname=array.getJSONObject(i).getString("pname");
			           	 //时长
			           	 String weight=array.getJSONObject(i).getString("weight");
			           	 //价格
			           	 String marketprice=array.getJSONObject(i).getString("marketprice");
			           	 //一人起订
			           	 String beginnum=array.getJSONObject(i).getString("beginnum");
			           	 //图片
			           	 String showimg=array.getJSONObject(i).getString("showimg");
			           	 //pid
			           	 String pid=array.getJSONObject(i).getString("pid");
			           	 
			           	 String storeid=array.getJSONObject(i).getString("storeid");
			             String CouponId=obj.getString("CouponId");
			           	 
			           UserBean.getUSerBean().setCouponid(CouponId);
			           
			           	    map.put("name", pname);
			           	    map.put("time", weight);
			           	    map.put("marketprice",marketprice);
			           	    map.put("beginnum", beginnum);
			           	    map.put("zuo", R.drawable.zuob);
			           	    map.put("pid", pid);
			           	    map.put("storeid",storeid);
			           	    map.put("order_item_image1","http://kingtopgroup.com/upload/store/5/product/show/thumb190_190/"+showimg);
			           	    list.add(map);
			           	    
				  }
			} catch (JSONException e) {
				e.printStackTrace();
			}
            return list;
    }
    
    public void setstatus(List<Map<String, Object>> list){
    	   orderListview=(MyListView) findViewById(R.id.orderListview);
		   orderListview.setAdapter(new manipulationAdapter(manipulationActivty.this, list));
			 orderListview.setOnItemClickListener(new OnItemClickListener() {
					@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						HashMap<String, Object> obj=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
						String pid=(String) obj.get("pid");
						String storeid=(String) obj.get("storeid");
						String name=(String) obj.get("name");
						String time=(String) obj.get("time");
						String price=(String) obj.get("marketprice");
						String beginnum=(String) obj.get("beginnum");
						String image=(String) obj.get("order_item_image1");
						Intent inten=new Intent(manipulationActivty.this,ServieNumActivty.class);
						Bundle bundle=new Bundle();
						bundle.putString("name", name);
						bundle.putString("time", time);
						bundle.putString("price", price);
						bundle.putString("beginnum", beginnum);
						bundle.putString("image", image);
						bundle.putString("pid", pid);
						       
						UserBean.getUSerBean().setPid(pid);
						UserBean.getUSerBean().setStoreRId(storeid);
						
						inten.putExtras(bundle);
						startActivity(inten);
						}
					});
		  
    }
    

}
