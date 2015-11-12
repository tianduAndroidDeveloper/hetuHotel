package com.kingtopgroup.util.stevenhu.android.phone.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
	public static UserBean userBean;
	
	public static UserBean getUSerBean(){
		if(userBean==null){
		userBean=new UserBean();
		}
		return userBean;
	}
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	//用户id
	private String uid;
	
	//订单id
	private String oid;
	
	//
	private String pid;
	//门店id
	private String StoreRId;
	
	private String couponid;
	//订单号
	private String Opid;


	public String getOpid() {
		return Opid;
	}


	public void setOpid(String opid) {
		Opid = opid;
	}


	public String getCouponid() {
		return couponid;
	}


	public void setCouponid(String couponid) {
		this.couponid = couponid;
	}


	public String getStoreRId() {
		return StoreRId;
	}


	public void setStoreRId(String storeRId) {
		StoreRId = storeRId;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getOid() {
		return oid;
	}


	public void setOid(String oid) {
		this.oid = oid;
	}



}
