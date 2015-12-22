package com.kingtopgroup.util.stevenhu.android.phone.bean;

import java.io.Serializable;


public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static UserBean userBean;

	public static UserBean getUSerBean() {
		if (userBean == null) {
			userBean = new UserBean();
		}
		return userBean;
	}

	// ”√ªßid
	private String uid;
	private String account;
	private String password;
	private int massagerId;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getMassagerId() {
		return massagerId;
	}

	public void setMassagerId(int massagerId) {
		this.massagerId = massagerId;
	}


}
