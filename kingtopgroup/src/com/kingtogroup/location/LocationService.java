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
		System.out.println("��λ��������");
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
			System.out.println("��λ�ɹ�");
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
			
			//mTextView.setText("�ߵ¶�λ\n" + str);
		}
	

}

	/*
	 * ��ʼ����λ
     */
    private void init() {      
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
        //ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
        //�ڶ�λ�������ں��ʵ��������ڵ���destroy()����     
        //����������ʱ��Ϊ-1����λֻ��һ��
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
