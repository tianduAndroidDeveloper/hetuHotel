package cn.androiddevelop.cycleviewpager.lib;

import java.util.ArrayList;
import java.util.List;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.ADInfo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * 瀹炵幇鍙惊鐜紝鍙疆鎾殑viewpager
 */
@SuppressLint("NewApi")
public class CycleViewPager extends Fragment implements OnPageChangeListener {
	
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	private ImageView[] indicators;
	private FrameLayout viewPagerFragmentLayout;
	private LinearLayout indicatorLayout; // 鎸囩ず鍣�
	private BaseViewPager viewPager;
	private BaseViewPager parentViewPager;
	private ViewPagerAdapter adapter;
	private CycleViewPagerHandler handler;
	private int time = 5000; // 榛樿杞挱鏃堕棿
	private int currentPosition = 0; // 杞挱褰撳墠浣嶇疆
	private boolean isScrolling = false; // 婊氬姩妗嗘槸鍚︽粴鍔ㄧ潃
	private boolean isCycle = false; // 鏄惁寰幆
	private boolean isWheel = false; // 鏄惁杞挱
	private long releaseTime = 0; // 鎵嬫寚鏉惧紑銆侀〉闈笉婊氬姩鏃堕棿锛岄槻姝㈡墜鏈烘澗寮�鍚庣煭鏃堕棿杩涜鍒囨崲
	private int WHEEL = 100; // 杞姩
	private int WHEEL_WAIT = 101; // 绛夊緟
	private ImageCycleViewListener mImageCycleViewListener;
	private List<ADInfo> infos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_cycle_viewpager_contet, null);

		viewPager = (BaseViewPager) view.findViewById(R.id.viewPager);
		indicatorLayout = (LinearLayout) view
				.findViewById(R.id.layout_viewpager_indicator);

		viewPagerFragmentLayout = (FrameLayout) view
				.findViewById(R.id.layout_viewager_content);

		handler = new CycleViewPagerHandler(getActivity()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == WHEEL && imageViews.size() != 0) {
					if (!isScrolling) {
						int max = imageViews.size() + 1;
						int position = (currentPosition + 1) % imageViews.size();
						viewPager.setCurrentItem(position, true);
						if (position == max) { // 鏈�鍚庝竴椤垫椂鍥炲埌绗竴椤�
							viewPager.setCurrentItem(1, false);
						}
					}

					releaseTime = System.currentTimeMillis();
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, time);
					return;
				}
				if (msg.what == WHEEL_WAIT && imageViews.size() != 0) {
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, time);
				}
			}
		};

		return view;
	}

	public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener) {
		setData(views, list, listener, 0);
	}

	/**
	 * 鍒濆鍖杤iewpager
	 * 
	 * @param views
	 *            瑕佹樉绀虹殑views
	 * @param showPosition
	 *            榛樿鏄剧ず浣嶇疆
	 */
	public void setData(List<ImageView> views, List<ADInfo> list, ImageCycleViewListener listener, int showPosition) {
		mImageCycleViewListener = listener;
		infos = list;
		this.imageViews.clear();

		if (views.size() == 0) {
			viewPagerFragmentLayout.setVisibility(View.GONE);
			return;
		}

		for (ImageView item : views) {
			this.imageViews.add(item);
		}

		int ivSize = views.size();

		// 璁剧疆鎸囩ず鍣�
		indicators = new ImageView[ivSize];
		if (isCycle)
			indicators = new ImageView[ivSize - 2];
		indicatorLayout.removeAllViews();
		for (int i = 0; i < indicators.length; i++) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.view_cycle_viewpager_indicator, null);
			indicators[i] = (ImageView) view.findViewById(R.id.image_indicator);
			indicatorLayout.addView(view);
		}

		adapter = new ViewPagerAdapter();

		// 榛樿鎸囧悜绗竴椤癸紝涓嬫柟viewPager.setCurrentItem灏嗚Е鍙戦噸鏂拌绠楁寚绀哄櫒鎸囧悜
		setIndicator(0);

		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(adapter);
		if (showPosition < 0 || showPosition >= views.size())
			showPosition = 0;
		if (isCycle) {
			showPosition = showPosition + 1;
		}
		viewPager.setCurrentItem(showPosition);

	}

	/**
	 * 璁剧疆鎸囩ず鍣ㄥ眳涓紝榛樿鎸囩ず鍣ㄥ湪鍙虫柟
	 */
	public void setIndicatorCenter() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		indicatorLayout.setLayoutParams(params);
	}

	/**
	 * 鏄惁寰幆锛岄粯璁や笉寮�鍚紝寮�鍚墠锛岃灏唙iews鐨勬渶鍓嶉潰涓庢渶鍚庨潰鍚勫姞鍏ヤ竴涓鍥撅紝鐢ㄤ簬寰幆
	 * 
	 * @param isCycle
	 *            鏄惁寰幆
	 */
	public void setCycle(boolean isCycle) {
		this.isCycle = isCycle;
	}

	/**
	 * 鏄惁澶勪簬寰幆鐘舵��
	 * 
	 * @return
	 */
	public boolean isCycle() {
		return isCycle;
	}

	/**
	 * 璁剧疆鏄惁杞挱锛岄粯璁や笉杞挱,杞挱涓�瀹氭槸寰幆鐨�
	 * 
	 * @param isWheel
	 */
	public void setWheel(boolean isWheel) {
		this.isWheel = isWheel;
		isCycle = true;
		if (isWheel) {
			handler.postDelayed(runnable, time);
		}
	}

	/**
	 * 鏄惁澶勪簬杞挱鐘舵��
	 * 
	 * @return
	 */
	public boolean isWheel() {
		return isWheel;
	}

	final Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (getActivity() != null && !getActivity().isFinishing()
					&& isWheel) {
				long now = System.currentTimeMillis();
				// 妫�娴嬩笂涓�娆℃粦鍔ㄦ椂闂翠笌鏈涔嬮棿鏄惁鏈夎Е鍑�(鎵嬫粦鍔�)鎿嶄綔锛屾湁鐨勮瘽绛夊緟涓嬫杞挱
				if (now - releaseTime > time - 500) {
					handler.sendEmptyMessage(WHEEL);
				} else {
					handler.sendEmptyMessage(WHEEL_WAIT);
				}
			}
		}
	};

	/**
	 * 閲婃斁鎸囩ず鍣ㄩ珮搴︼紝鍙兘鐢变簬涔嬪墠鎸囩ず鍣ㄨ闄愬埗浜嗛珮搴︼紝姝ゅ閲婃斁
	 */
	public void releaseHeight() {
		getView().getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
		refreshData();
	}

	/**
	 * 璁剧疆杞挱鏆傚仠鏃堕棿锛屽嵆娌″灏戠鍒囨崲鍒颁笅涓�寮犺鍥�.榛樿5000ms
	 * 
	 * @param time
	 *            姣涓哄崟浣�
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * 鍒锋柊鏁版嵁锛屽綋澶栭儴瑙嗗浘鏇存柊鍚庯紝閫氱煡鍒锋柊鏁版嵁
	 */
	public void refreshData() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	/**
	 * 闅愯棌CycleViewPager
	 */
	public void hide() {
		viewPagerFragmentLayout.setVisibility(View.GONE);
	}

	/**
	 * 杩斿洖鍐呯疆鐨剉iewpager
	 * 
	 * @return viewPager
	 */
	public BaseViewPager getViewPager() {
		return viewPager;
	}

	/**
	 * 椤甸潰閫傞厤鍣� 杩斿洖瀵瑰簲鐨剉iew
	 * 
	 * @author Yuedong Li
	 * 
	 */
	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			ImageView v = imageViews.get(position);
			if (mImageCycleViewListener != null) {
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mImageCycleViewListener.onImageClick(infos.get(currentPosition - 1), currentPosition, v);
					}
				});
			}
			container.addView(v);
			return v;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (arg0 == 1) { // viewPager鍦ㄦ粴鍔�
			isScrolling = true;
			return;
		} else if (arg0 == 0) { // viewPager婊氬姩缁撴潫
			if (parentViewPager != null)
				parentViewPager.setScrollable(true);

			releaseTime = System.currentTimeMillis();

			viewPager.setCurrentItem(currentPosition, false);
			
		}
		isScrolling = false;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		int max = imageViews.size() - 1;
		int position = arg0;
		currentPosition = arg0;
		if (isCycle) {
			if (arg0 == 0) {
				currentPosition = max - 1;
			} else if (arg0 == max) {
				currentPosition = 1;
			}
			position = currentPosition - 1;
		}
		setIndicator(position);
	}

	/**
	 * 璁剧疆viewpager鏄惁鍙互婊氬姩
	 * 
	 * @param enable
	 */
	public void setScrollable(boolean enable) {
		viewPager.setScrollable(enable);
	}

	/**
	 * 杩斿洖褰撳墠浣嶇疆,寰幆鏃堕渶瑕佹敞鎰忚繑鍥炵殑position鍖呭惈涔嬪墠鍦╲iews鏈�鍓嶆柟涓庢渶鍚庢柟鍔犲叆鐨勮鍥撅紝鍗冲綋鍓嶉〉闈㈣瘯鍥惧湪views闆嗗悎鐨勪綅缃�
	 * 
	 * @return
	 */
	public int getCurrentPostion() {
		return currentPosition;
	}

	/**
	 * 璁剧疆鎸囩ず鍣�
	 * 
	 * @param selectedPosition
	 *            榛樿鎸囩ず鍣ㄤ綅缃�
	 */
	private void setIndicator(int selectedPosition) {
		for (int i = 0; i < indicators.length; i++) {
			indicators[i]
					.setBackgroundResource(R.drawable.icon_point);
		}
		if (indicators.length > selectedPosition)
			indicators[selectedPosition]
					.setBackgroundResource(R.drawable.icon_point_pre);
	}

	/**
	 * 濡傛灉褰撳墠椤甸潰宓屽鍦ㄥ彟涓�涓獀iewPager涓紝涓轰簡鍦ㄨ繘琛屾粴鍔ㄦ椂闃绘柇鐖禫iewPager婊氬姩锛屽彲浠� 闃绘鐖禫iewPager婊戝姩浜嬩欢
	 * 鐖禫iewPager闇�瑕佸疄鐜癙arentViewPager涓殑setScrollable鏂规硶
	 */
	public void disableParentViewPagerTouchEvent(BaseViewPager parentViewPager) {
		if (parentViewPager != null)
			parentViewPager.setScrollable(false);
	}

	
	/**
	 * 杞挱鎺т欢鐨勭洃鍚簨浠�
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 鍗曞嚮鍥剧墖浜嬩欢
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(ADInfo info, int postion, View imageView);
	}
}