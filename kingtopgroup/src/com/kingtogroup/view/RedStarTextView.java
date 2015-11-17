package com.kingtogroup.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

public class RedStarTextView extends TextView {

	public RedStarTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {

		if (text.length() > 0) {
			Spannable WordtoSpan = new SpannableString(text);

			WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			super.setText(WordtoSpan, type);
		} else {
			super.setText(text, type);
		}
	}

}
