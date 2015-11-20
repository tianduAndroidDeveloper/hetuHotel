package com.kingtopgroup.activty;

import com.kingtogroup.view.MyListView;
import com.kingtopgroup.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommitActivity extends MainActionBarActivity {
	MyListView lv;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		titleButton.setText("Ã·Ωª∂©µ•");
		init();
	}

	void init() {
		lv = (MyListView) findViewById(R.id.lv);
		lv.setAdapter(new MyListViewAdapter());
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			if(convertView == null)
				convertView = View.inflate(CommitActivity.this, R.layout.item_order2, null);
			return convertView;
		}
		
	}

	@Override
	public void backButtonClick(View v) {
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
		return true;
	}

}
