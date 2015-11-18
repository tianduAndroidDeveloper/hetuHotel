package com.kingtogroup.messager;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.MainActionBarActivity;


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
	        titleButton.setText("∆¿¬€“≥");
	        resources = getResources();
	       this.json = getIntent().getStringExtra("json");
	        initTextView();
	        initViewPager();
	       
	       
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

	    private void initViewPager() {
	        mPager = (ViewPager) findViewById(R.id.vPager);
	        fragmentsList = new ArrayList<Fragment>();
	        LayoutInflater mInflater = getLayoutInflater();
	        View activityView = mInflater.inflate(R.layout.discus_layout_tab, null);

	        Fragment activityfragment = ChildFragment.newInstance("Hello tab1.");
	        Fragment groupFragment = ChildFragment.newInstance("Hello tab2.");
	        Fragment friendsFragment=ChildFragment.newInstance("Hello tab3.");
	        Fragment chatFragment=ChildFragment.newInstance("Hello tab4.");

	        fragmentsList.add(activityfragment);
	        fragmentsList.add(groupFragment);
	        fragmentsList.add(friendsFragment);
	        fragmentsList.add(chatFragment);
	        
	        mPager.setAdapter(new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
	        mPager.setCurrentItem(0);
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
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
