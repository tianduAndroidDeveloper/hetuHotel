package com.kingtopgroup.fragment.impl;


import com.kingtopgroup.fragment.inter.BackHandledInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class BackHandledFragment extends Fragment{
	protected  BackHandledInterface backHandled;
	
	public abstract Boolean onBackPressed();
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			if(!(getActivity() instanceof BackHandledInterface)){
				throw new ClassCastException("please implement BackHandledInterface");
			}else{
				this.backHandled=(BackHandledInterface) getActivity();
			}
		}

		@Override
		public void onStart() {
			super.onStart();
			backHandled.selectFragmen(this);
		}
}
