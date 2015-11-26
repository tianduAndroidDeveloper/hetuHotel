package com.kingtopgroup.util.stevenhu.android.phone.bean;

import java.io.Serializable;

public class ManagerBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	public String sex;
	public String address;
	public boolean isChecked = false;
	public String serviceDate;
	public double point_x;
	public double point_y;
	public String Logo;
	public String StoreId;
	@Override
	public String toString() {
		return "ManagerBean [name=" + name + ", sex=" + sex + ", address="
				+ address + ", isChecked=" + isChecked + "]";
	}
	
	
}
