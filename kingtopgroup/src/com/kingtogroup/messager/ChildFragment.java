package com.kingtogroup.messager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.DiscusBean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChildFragment extends Fragment {
    private static final String TAG = "TestFragment";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    private ListView discus_listview;
    private List<DiscusBean> discusBean;

    public static ChildFragment newInstance(String s) {
        ChildFragment newFragment = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        hello = args != null ? args.getString("hello") : defaultHello;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.discus_layout_tab, container, false);
        discus_listview=(ListView) view.findViewById(R.id.discus_listview);
        try {
			//JSONObject obj=new JSONObject(hello);
            JSONArray array=new JSONArray(hello);
            discusBean=new ArrayList<DiscusBean>();
			for(int i=0;i<array.length();i++){
				DiscusBean bean=new DiscusBean();
				String message=array.getJSONObject(i).getString("message");
				bean.contnet=message;
				String reviewtime=array.getJSONObject(i).getString("reviewtime");
				bean.date=reviewtime;
				String mobile=array.getJSONObject(i).getString("mobile");
				bean.phone=mobile;
				discusBean.add(bean);
			}
			  discus_listview.setAdapter(new DiscusAdapter(getActivity(), discusBean));
		} catch (JSONException e) {
			e.printStackTrace();
		}
      //  TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
       // viewhello.setText(hello);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TestFragment-----onDestroy");
    }
    
    class DiscusAdapter extends BaseAdapter{
    	Context context;
    	LayoutInflater inflater;
    	List<DiscusBean> discusbean;
    	public DiscusAdapter(Context conext,List<DiscusBean> discusbean){
    		this.discusbean=discusbean;
    		this.context=conext;
    		inflater=LayoutInflater.from(conext);
    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return discusbean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return getItem(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if(view==null){
				holder=new ViewHolder();
				view=inflater.inflate(R.layout.discus_item, arg2, false);
				holder.phone=(TextView) view.findViewById(R.id.phone);
				holder.content=(TextView) view.findViewById(R.id.content);
				holder.date=(TextView) view.findViewById(R.id.date);
				view.setTag(holder);
			}else{
				holder=(ViewHolder) view.getTag();
			}
			String phone=null;
			if("".equals(discusbean.get(arg0).phone.trim()) || discusbean.get(arg0).phone.trim()==null){
				phone="";
			}else{
				phone=discusbean.get(arg0).phone.trim();
				String start=phone.substring(0, 3);
				String end=phone.substring(phone.length()-3, phone.length());
				phone=start+"****"+end;
			}
			holder.phone.setText(phone);
			holder.content.setText(discusbean.get(arg0).contnet);
			holder.date.setText(discusbean.get(arg0).date);
			return view;
		}
		
		class ViewHolder{
			TextView phone;
			TextView content;
			TextView date;
		}
    	
    }

}
