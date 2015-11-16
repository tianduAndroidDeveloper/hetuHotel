package com.kingtogroup.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class Utils {
	public static final String TAG = "Utils";

	// 隐藏键盘
	public static void hiddenSoftBorad(Context context) {
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

		}
	}

	// 处理前后颜色不一样的TextView
	public static void changeTextColor(TextView textView) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);

		builder.setSpan(blackSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, 3, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		textView.setText(builder);
	}

	public static void spanTextSize(TextView textView, String split, boolean former, int[] nums) {
		String text = textView.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		String[] price = text.split(split);
		AbsoluteSizeSpan largeSizeSpan = new AbsoluteSizeSpan(nums[0]);
		AbsoluteSizeSpan smallSizeSpan = new AbsoluteSizeSpan(nums[1]);

		if (former) {
			builder.setSpan(largeSizeSpan, 0, price[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(smallSizeSpan, price[0].length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			builder.setSpan(smallSizeSpan, 0, price[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			builder.setSpan(largeSizeSpan, price[0].length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		textView.setText(builder);

	}


	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int px2dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

}