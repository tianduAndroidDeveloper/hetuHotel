package com.kingtopgroup.util.stevenhu.android.phone.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kingtogroup.domain.ShipAddress;

public class UserBean implements Serializable {
	public static UserBean userBean;

	public static UserBean getUSerBean() {
		if (userBean == null) {
			userBean = new UserBean();
		}
		return userBean;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 用户id
	private String uid;

	// 订单id
	private String oid;

	//
	private String pid;
	// 门店id
	private String StoreRId;

	private String couponid;
	// 订单号
	private String Opid;

	// 購買數量
	private String BuyCount;

	private List<HashMap<String, Object>> serviceItems = new ArrayList<HashMap<String, Object>>();
	private List<ShipAddress> addresses = new ArrayList<ShipAddress>();
	private List<ManagerBean> massages = new ArrayList<ManagerBean>();
	private String ordertime;

	public String getBuyCount() {
		return BuyCount;
	}

	public void setBuyCount(String buyCount) {
		BuyCount = buyCount;
	}

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

	public List<HashMap<String, Object>> getServiceItems() {
		return serviceItems;
	}

	public void setServiceItems(List<HashMap<String, Object>> serviceItems) {
		this.serviceItems = serviceItems;
	}

	public void putServiceItem(HashMap<String, Object> serviceItem) {
		serviceItems.add(serviceItem);
	}

	public List<ShipAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<ShipAddress> addresses) {
		this.addresses = addresses;
	}

	public List<ManagerBean> getMassages() {
		return massages;
	}

	public void setMassages(List<ManagerBean> massages) {
		this.massages = massages;
	}

	public void putMassage(ManagerBean bean) {
		massages.add(bean);
	}

	public void removeMassage(ManagerBean bean) {
		massages.remove(bean);
	}

	public void putAddress(ShipAddress address) {
		addresses.add(address);
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

}
