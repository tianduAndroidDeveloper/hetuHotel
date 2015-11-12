package com.kingtopgroup.fragment.impl;



import com.kingtopgroup.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FirstFram extends Fragment implements OnClickListener{
	private TextView refresh;
	private LayoutInflater inflater;
	private Handler handel;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.first_frame, viewgroup, false);
		//refresh=(TextView) view.findViewById(R.id.refresh);
		//refresh.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View arg0) {
		Toast.makeText(getActivity(), "请检查网络，或重新进入App", Toast.LENGTH_LONG).show();
		//Intent inten=new Intent(getActivity(),sindexActivity.class);
		//startActivity(inten);
		//thread.start();
		
	}
	
	
	/*final Thread thread=new Thread(new Runnable() {
		@Override
		public void run() {
	       Message message=new Message();
	       message.what=firstActivty.REFRESH;
	       handel.sendMessage(message);
		}
	});*/
	
	/*@Override
	public void onAttach(android.app.Activity activity) {
		this.firstactivty=(firstActivty) activity;
		 firstactivty.setHandler(handel);
	};*/
	
	
	/*class GameThread implements Runnable  
	{  
		public void run()  
		{  
			while (!Thread.currentThread().isInterrupted())  
			{  
				try  
				{  
					Thread.sleep(100);  
				}  
				catch (InterruptedException e)  
				{  
					Thread.currentThread().interrupt();  
				}  
				//使用postInvalidate可以直接在线程中更新界面  
				inflater.inflate(R.id.fir, root)
				mGameView.postInvalidate();  
			}  
	 
		}
	}*/
}
