package com.kimgtopgroup.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class AgentApplication extends Application {
	public List<Activity> activities = new ArrayList<Activity>();  
	  
	   public void addActivity(Activity activity) {  
	        activities.add(activity);  
	   }  
	  
	    @Override  
	    public void onTerminate() {  
	        for (Activity activity : activities) {  
	            activity.finish();  
	       }  
	        //onDestroy(); 
	       System.exit(0);  
	       super.onTerminate();  
	    }  
	    

}
