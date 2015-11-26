package com.kingtopgroup.activty;

import java.util.ArrayList;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingtogroup.messager.ChildFragment;
import com.kingtogroup.messager.CustomFragmentPagerAdapter;
import com.kingtopgroup.R;
import com.kingtopgroup.constant.ConstanceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stevenhu.android.phone.utils.AsyncHttpCilentUtil;


public class DiscusActivity extends MainActionBarActivity {
	    private String json;
	    private ViewPager mPager;
	    private ArrayList<Fragment> fragmentsList;
	    private ImageView ivBottomLine;
	    private TextView tvTab1, tvTab2, tvTab3, tvTab4;

	    private int currIndex = 0;
	    private int position_one;
	    private int position_two;
	    private int position_three;
	    private Resources resources;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.discus_layout);
	        titleButton.setText("评论");
	        resources = getResources();
	       this.json = getIntent().getStringExtra("json");
	      
	       
	        initTextView();
	        getDate();
	       // initViewPager();
	       
	       
	    }

	    private void initTextView() {
	    	ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
	        tvTab1 = (TextView) findViewById(R.id.tv_tab_1);
	        tvTab2 = (TextView) findViewById(R.id.tv_tab_2);
	        tvTab3 = (TextView) findViewById(R.id.tv_tab_3);
	        tvTab4 = (TextView) findViewById(R.id.tv_tab_4);

	        tvTab1.setOnClickListener(new MyOnClickListener(0));
	        tvTab2.setOnClickListener(new MyOnClickListener(1));
	        tvTab3.setOnClickListener(new MyOnClickListener(2));
	        tvTab4.setOnClickListener(new MyOnClickListener(3));
	    }

	    private void initViewPager(String date) {
	        mPager = (ViewPager) findViewById(R.id.vPager);
	        fragmentsList = new ArrayList<Fragment>();
	        //LayoutInflater mInflater = getLayoutInflater();
	        //View activityView = mInflater.inflate(R.layout.discus_layout_tab, null);
	        
	        
	        JSONObject obj;
	        try {
	        	obj = new JSONObject(date);
	        	//全部
	        	String ReviewTotalList=obj.getString("ReviewTotalList");
	        	JSONArray array=new JSONArray(ReviewTotalList);
	        	tvTab1.setText("全部("+array.length()+")");
	        	Fragment ReviewTotalListfragment = ChildFragment.newInstance(ReviewTotalList);

	        	//好评
	        	String ReviewGoodList=obj.getString("ReviewGoodList");
	        	JSONArray array2=new JSONArray(ReviewGoodList);
	        	tvTab2.setText("好评("+array2.length()+")");
	        	Fragment ReviewGoodListfragment= ChildFragment.newInstance(ReviewGoodList);
	        	
	        	//中评
	        	String ReviewMiddleList=obj.getString("ReviewMiddleList");
	        	JSONArray array3=new JSONArray(ReviewMiddleList);
	        	tvTab3.setText("中评("+array3.length()+")");
	        	Fragment ReviewMiddleListfragment=ChildFragment.newInstance(ReviewMiddleList);
	        	
	        	//差评
	        	String ReviewBadList=obj.getString("ReviewBadList");
	        	JSONArray array4=new JSONArray(ReviewBadList);
	        	tvTab4.setText("差评("+array4.length()+")");
	        	Fragment ReviewBadListFragment=ChildFragment.newInstance(ReviewBadList);

	        	fragmentsList.add(ReviewTotalListfragment);
	        	fragmentsList.add(ReviewGoodListfragment);
	        	fragmentsList.add(ReviewMiddleListfragment);
	        	fragmentsList.add(ReviewBadListFragment);

	        	mPager.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
	        	mPager.setCurrentItem(0);
	        	mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	        } catch (JSONException e) {
	        	// TODO Auto-generated catch block
	        	e.printStackTrace();
	        }
	       
	      
	    }
	    
	    private void getDate(){
	    	try {
	    		JSONObject obj=new JSONObject(json);
				String storeid=obj.getString("storeid");
				RequestParams params=AsyncHttpCilentUtil.getParams();
				params.put("massagerid", storeid);
				AsyncHttpCilentUtil.getInstance().get(ConstanceUtil.get_masserger_list_url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						if(arg0==200){
							String date =new String(arg2);
							//try {
								//JSONObject obj=new JSONObject(date);
								//String ReviewTotalList=obj.getString("ReviewTotalList");
								initViewPager(date);
							//} catch (JSONException e) {
							//	e.printStackTrace();
							//}
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						
					}
				});
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	   
	    
	    }

	   

	    public class MyOnClickListener implements View.OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            mPager.setCurrentItem(index);
	        }
	    };

	    public class MyOnPageChangeListener implements OnPageChangeListener {

	        @Override
	        public void onPageSelected(int arg0) {
	            Animation animation = null;
	            int width = ivBottomLine.getWidth();
	            position_one = width;
	            position_two = 2*width;
	            position_three = 3*width;
	            switch (arg0) {
	            case 0:
	                if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, 0, 0, 0);
	                    tvTab2.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 2) {
	                    animation = new TranslateAnimation(position_two, 0, 0, 0);
	                    tvTab3.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 3) {
	                    animation = new TranslateAnimation(position_three, 0, 0, 0);
	                    tvTab4.setTextColor(resources.getColor(android.R.color.black));
	                }
	                tvTab1.setTextColor(resources.getColor(R.color.red_light));
	                break;
	            case 1:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(0, position_one, 0, 0);
	                    tvTab1.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 2) {
	                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
	                    tvTab3.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 3) {
	                    animation = new TranslateAnimation(position_three, position_one, 0, 0);
	                    tvTab4.setTextColor(resources.getColor(android.R.color.black));
	                }
	                tvTab2.setTextColor(resources.getColor(R.color.red_light));
	                break;
	            case 2:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(0, position_two, 0, 0);
	                    tvTab1.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
	                    tvTab2.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 3) {
	                    animation = new TranslateAnimation(position_three, position_two, 0, 0);
	                    tvTab4.setTextColor(resources.getColor(android.R.color.black));
	                }
	                tvTab3.setTextColor(resources.getColor(R.color.red_light));
	                break;
	            case 3:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(0, position_three, 0, 0);
	                    tvTab1.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 1) {
	                    animation = new TranslateAnimation(position_one, position_three, 0, 0);
	                    tvTab2.setTextColor(resources.getColor(android.R.color.black));
	                } else if (currIndex == 2) {
	                    animation = new TranslateAnimation(position_two, position_three, 0, 0);
	                    tvTab3.setTextColor(resources.getColor(android.R.color.black));
	                }
	                tvTab4.setTextColor(resources.getColor(R.color.red_light));
	                break;
	            }
	            currIndex = arg0;
	            animation.setFillAfter(true);
	            animation.setDuration(300);
	            ivBottomLine.startAnimation(animation);
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    }

	@Override
	public void backButtonClick(View v) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return true;
	}

}
