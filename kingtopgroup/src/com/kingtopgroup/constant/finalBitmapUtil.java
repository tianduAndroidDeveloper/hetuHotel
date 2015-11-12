package com.kingtopgroup.constant;

import android.content.Context;

import com.kingtopgroup.R;

import net.tsz.afinal.FinalBitmap;

public class finalBitmapUtil {
	 public static  FinalBitmap fb;
	  public  static FinalBitmap getFinalBitmap(Context context){
		  if(fb==null){
			  fb=FinalBitmap.create(context);
			  fb.configLoadingImage(R.drawable.error);//  设置图片正在加载的时候显示的图片
		  }
	    return fb;
	  }

}
