package com.stevenhu.android.phone.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager;
import cn.androiddevelop.cycleviewpager.lib.CycleViewPager.ImageCycleViewListener;

import com.kingtopgroup.R;
import com.kingtopgroup.activty.manipulationActivty;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ADInfo;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class LunboImageUtil {
	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	
	
	
	@SuppressLint("NewApi")
	public void initialize(Context context,String[] imageUrls,CycleViewPager cycleViewPager) {
		
		configImageLoader(context);
		
		/*ycleViewPager = (CycleViewPager) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);*/
		/*
		 String[] imageUrls = {"http://kingtopgroup.com/mobile/images/banner01.jpg",
					"http://kingtopgroup.com/mobile/images/banner02.jpg",
					"http://kingtopgroup.com/mobile/images/banner03.jpg"};*/
		
		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("鍥剧墖-->" + i );
			infos.add(info);
		}
		
		// 灏嗘渶鍚庝竴涓狪mageView娣诲姞杩涙潵
		views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(context, infos.get(i).getUrl()));
		}
		// 灏嗙涓�涓狪mageView娣诲姞杩涙潵
		views.add(ViewFactory.getImageView(context, infos.get(0).getUrl()));
		
		// 璁剧疆寰幆锛屽湪璋冪敤setData鏂规硶鍓嶈皟鐢�
		cycleViewPager.setCycle(true);

		// 鍦ㄥ姞杞芥暟鎹墠璁剧疆鏄惁寰幆
		cycleViewPager.setData(views, infos, null);
		//璁剧疆杞挱
		cycleViewPager.setWheel(true);

	    // 璁剧疆杞挱鏃堕棿锛岄粯璁�5000ms
		cycleViewPager.setTime(2000);
		//璁剧疆鍦嗙偣鎸囩ず鍥炬爣缁勫眳涓樉绀猴紝榛樿闈犲彸
		cycleViewPager.setIndicatorCenter();
		
		
		
		
	}

	/*private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				Toast.makeText(manipulationActivty.this,
						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
						.show();
			}
			
		}

	};*/

	/**
	 * 閰嶇疆ImageLoder
	 */
	public void configImageLoader(Context context) {
		// 鍒濆鍖朓mageLoader
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 璁剧疆鍥剧墖涓嬭浇鏈熼棿鏄剧ず鐨勫浘鐗�
				.showImageForEmptyUri(R.drawable.icon_empty) // 璁剧疆鍥剧墖Uri涓虹┖鎴栨槸閿欒鐨勬椂鍊欐樉绀虹殑鍥剧墖
				.showImageOnFail(R.drawable.icon_error) // 璁剧疆鍥剧墖鍔犺浇鎴栬В鐮佽繃绋嬩腑鍙戠敓閿欒鏄剧ず鐨勫浘鐗�
				.cacheInMemory(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦瀛樺湪鍐呭瓨涓�
				.cacheOnDisc(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦瀛樺湪SD鍗′腑
				// .displayer(new RoundedBitmapDisplayer(20)) // 璁剧疆鎴愬渾瑙掑浘鐗�
				.build(); // 鍒涘缓閰嶇疆杩囧緱DisplayImageOption瀵硅薄
 
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);		
	}





}
