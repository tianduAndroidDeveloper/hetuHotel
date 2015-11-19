package com.kingtogroup.location;

public class LastLocation {
	private LocationCallBack callBack;
	private static LastLocation instance;
	private Double latitude,longitude;
	private String sheng,shi,xian;
	private float accuracy,speed;
	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public static LastLocation initInstance(){
		if(instance == null){
			instance = new LastLocation();
		}
		return instance;
	}
	
	public LocationCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(LocationCallBack callBack) {
		this.callBack = callBack;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getSheng() {
		return sheng;
	}
	public void setSheng(String sheng) {
		this.sheng = sheng;
	}
	public String getShi() {
		return shi;
	}
	public void setShi(String shi) {
		this.shi = shi;
	}
	public String getXian() {
		return xian;
	}
	public void setXian(String xian) {
		this.xian = xian;
	}

	public void callBack() {
		if(callBack != null){
			callBack.callBack();
		}
		
	}
	
}
