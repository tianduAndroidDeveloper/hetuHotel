package com.kingtogroup.domain;

import java.io.Serializable;

public class OrderProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int RecordId;
	public int Oid;
	public String StoreName;
	public String Logo;
	public int Uid;
	public int Sid;
	public int Pid;
	public String PSN;
	public int CateId;
	public String Remark;
	public String MassagerNames;
	public String PServiceDate;
	public String ServiceAddress;
	public String ServiceTime;
	public int BrandId;
	public int StoreId;
	public int StoreCid;
	public int StoreSTid;
	public String Name;
	public String Massagers;
	public String ShowImg;
	public int DiscountPrice;
	public int ShopPrice;
	public int CostPrice;
	public int MarketPrice;
	public int Weight;
	public int IsReview;
	public int RealCount;
	public int BuyCount;
	public int SendCount;

	@Override
	public String toString() {
		return "OrderProduct [RecordId=" + RecordId + ", Oid=" + Oid
				+ ", StoreName=" + StoreName + ", Logo=" + Logo + ", Uid="
				+ Uid + ", Sid=" + Sid + ", Pid=" + Pid + ", PSN=" + PSN
				+ ", CateId=" + CateId + ", Remark=" + Remark
				+ ", MassagerNames=" + MassagerNames + ", PServiceDate="
				+ PServiceDate + ", ServiceAddress=" + ServiceAddress
				+ ", ServiceTime=" + ServiceTime + ", BrandId=" + BrandId
				+ ", StoreId=" + StoreId + ", StoreCid=" + StoreCid
				+ ", StoreSTid=" + StoreSTid + ", Name=" + Name
				+ ", Massagers=" + Massagers + ", ShowImg=" + ShowImg
				+ ", DiscountPrice=" + DiscountPrice + ", ShopPrice="
				+ ShopPrice + ", CostPrice=" + CostPrice + ", MarketPrice="
				+ MarketPrice + ", Weight=" + Weight + ", IsReview=" + IsReview
				+ ", RealCount=" + RealCount + ", BuyCount=" + BuyCount
				+ ", SendCount=" + SendCount + ", Type=" + Type
				+ ", PayCredits=" + PayCredits + ", CouponTypeId="
				+ CouponTypeId + ", ExtCode1=" + ExtCode1 + ", ExtCode2="
				+ ExtCode2 + ", ExtCode3=" + ExtCode3 + ", ExtCode4="
				+ ExtCode4 + ", ExtCode5=" + ExtCode5 + ", AddTime=" + AddTime
				+ ", Woods=" + Woods + ", Water=" + Water + ", Stsid=" + Stsid
				+ ", Storerid=" + Storerid + ", ServiceDate=" + ServiceDate
				+ ", Consignee=" + Consignee + ", Mobile=" + Mobile
				+ ", Address=" + Address + ", Regionid=" + Regionid
				+ ", ServiceSelf=" + ServiceSelf + ", Price_Grade1="
				+ Price_Grade1 + ", Price_Grade2=" + Price_Grade2
				+ ", Price_Grade3=" + Price_Grade3 + ", Price_Grade4="
				+ Price_Grade4 + ", TiCheng_Grade1=" + TiCheng_Grade1
				+ ", TiCheng_Grade2=" + TiCheng_Grade2 + ", TiCheng_Grade3="
				+ TiCheng_Grade3 + ", TiCheng_Grade4=" + TiCheng_Grade4 + "]";
	}

	public int Type;
	public int PayCredits;
	public int CouponTypeId;
	public int ExtCode1;
	public int ExtCode2;
	public int ExtCode3;
	public int ExtCode4;
	public int ExtCode5;
	public String AddTime;
	public int Woods;
	public int Water;
	public int Stsid;
	public int Storerid;
	public String ServiceDate;
	public String Consignee;
	public String Mobile;
	public String Address;
	public int Regionid;
	public int ServiceSelf;
	public int Price_Grade1;
	public int Price_Grade2;
	public int Price_Grade3;
	public int Price_Grade4;
	public int TiCheng_Grade1;
	public int TiCheng_Grade2;
	public int TiCheng_Grade3;
	public int TiCheng_Grade4;
}
