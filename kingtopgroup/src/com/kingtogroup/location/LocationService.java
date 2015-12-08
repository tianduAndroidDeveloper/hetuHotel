package com.kingtogroup.location;

import com.amap.api.location.AMapLocation;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service implements AMapLocationListener{

	private LocationManagerProxy mLocationManagerProxy;
		
	private static LocationService instance;
	public static LocationService initInstance(){
		if(instance == null){
			instance = new LocationService();
		}
		return instance;
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mLocationManagerProxy != null){
			mLocationManagerProxy.removeUpdates(this);
			mLocationManagerProxy.destroy();
		}
		mLocationManagerProxy = null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		this.instance = this;
		init();
		System.out.println("定位服务启动");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
	    if(location != null && location.getAMapException().getErrorCode() == 0){
	        
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}		
			System.out.println("定位成功");
				LastLocation entity = LastLocation.initInstance();
				entity.setLatitude(geoLat);
				entity.setLongitude(geoLng);
				entity.setSheng(location.getProvince());
				entity.setShi(location.getCity());
				entity.setXian(location.getDistrict());
				entity.setAccuracy(location.getAccuracy());
				entity.setSpeed(location.getSpeed());
				entity.callBack();
				uploadLocation(entity);
				stopLocation();
			
			//mTextView.setText("高德定位\n" + str);
		}
	

}

	/*
	 * 初始化定位
     */
    private void init() {      
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法     
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60*1000, 15, this);
        mLocationManagerProxy.setGpsEnable(false);
    }
    
    private void stopLocation(){
    	stopSelf();
    }

    private void uploadLocation(LastLocation entity){
    	RequestParams rp = new RequestParams();
    	rp.add("longitude", String.valueOf(entity.getLongitude()));
    	rp.add("latitude", String.valueOf(entity.getLatitude()));
    	rp.add("accuracy", String.valueOf(entity.getAccuracy()));
    	rp.add("speed", String.valueOf(entity.getSpeed()));
    	new AsyncHttpClient().post("", rp, null);
    }

}
