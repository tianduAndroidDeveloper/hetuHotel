package com.kingtogroup.domain;

public class ShipAddress {
	public int ProvinceId;
	public String ProvinceName;
	public int CityId;
	public String CityName;
	public int CountyId;
	public String CountyName;
	public int SAId;
	public int Uid;
	public int RegionId;
	public int IsDefault;
	public String Alias;
	public String Consignee;
	public String Mobile;
	public String Phone;
	public String Email;
	public String ZipCode;
	public String Address;

	@Override
	public String toString() {
		return "ShipAddress [ProvinceId=" + ProvinceId + ", ProvinceName="
				+ ProvinceName + ", CityId=" + CityId + ", CityName="
				+ CityName + ", CountyId=" + CountyId + ", CountyName="
				+ CountyName + ", SAId=" + SAId + ", Uid=" + Uid
				+ ", RegionId=" + RegionId + ", IsDefault=" + IsDefault
				+ ", Alias=" + Alias + ", Consignee=" + Consignee + ", Mobile="
				+ Mobile + ", Phone=" + Phone + ", Email=" + Email
				+ ", ZipCode=" + ZipCode + ", Address=" + Address + "]";
	}

}
