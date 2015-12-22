package com.kingtogroup.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;

import com.kingtopgroup.R;
import com.kingtopgroup.util.stevenhu.android.phone.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OrderInfo {

	public int Oid;
	public String OSN = "2015112415355851172685";
	public int Uid = 117;
	public int OrderState = 30;
	public double ProductAmount = 78;
	public double OrderAmount = 78;
	public double SurplusMoney = 78;
	public int ParentId = 0;
	public int IsReview = 0;
	public String AddTime = "2015-11-24T15:35:58";
	public int StoreId = 5;
	public String StoreName = "任意技师";
	public String ShipSN = "";
	public int ShipCoId = 0;
	public String ShipCoName = "";
	public String ShipTime = "1900-01-01T00:00:00";
	public String PaySN = "";
	public String PaySystemName = "微信";
	public String PayFriendName = "";
	public int PayMode = 1;
	public String PayTime = "1900-01-01T00:00:00";
	public int RegionId = 2685;
	public String Consignee = "best";
	public String Mobile = "13524878965";
	public String Phone = "13524878965";
	public String Email = "1135741892@qqq.com";
	public String ZipCode = "655002";
	public String Address = "bitbit";
	public String BestTime = "1970-01-01T00:00:00";
	public double ShipFee = 0;
	public double PayFee = 0;
	public double FullCut = 0;
	public double Discount = 0;
	public int PayCreditCount = 0;
	public double PayCreditMoney = 0;
	public double CouponMoney = 0;
	public int Weight = 45;
	public String BuyerRemark = "0";
	public String IP = "220.165.243.133";
	public List<Product> products = new ArrayList<Product>();
	public boolean isReceive = false;
	public String imgUrl = "";

	@Override
	public String toString() {
		return "OrderInfo [Oid=" + Oid + ", OSN=" + OSN + ", Uid=" + Uid + ", OrderState=" + OrderState + ", ProductAmount=" + ProductAmount + ", OrderAmount=" + OrderAmount + ", SurplusMoney="
				+ SurplusMoney + ", ParentId=" + ParentId + ", IsReview=" + IsReview + ", AddTime=" + AddTime + ", StoreId=" + StoreId + ", StoreName=" + StoreName + ", ShipSN=" + ShipSN
				+ ", ShipCoId=" + ShipCoId + ", ShipCoName=" + ShipCoName + ", ShipTime=" + ShipTime + ", PaySN=" + PaySN + ", PaySystemName=" + PaySystemName + ", PayFriendName=" + PayFriendName
				+ ", PayMode=" + PayMode + ", PayTime=" + PayTime + ", RegionId=" + RegionId + ", Consignee=" + Consignee + ", Mobile=" + Mobile + ", Phone=" + Phone + ", Email=" + Email
				+ ", ZipCode=" + ZipCode + ", Address=" + Address + ", BestTime=" + BestTime + ", ShipFee=" + ShipFee + ", PayFee=" + PayFee + ", FullCut=" + FullCut + ", Discount=" + Discount
				+ ", PayCreditCount=" + PayCreditCount + ", PayCreditMoney=" + PayCreditMoney + ", CouponMoney=" + CouponMoney + ", Weight=" + Weight + ", BuyerRemark=" + BuyerRemark + ", IP=" + IP
				+ ", products=" + products + ", isReceive=" + isReceive + ", imgUrl=" + imgUrl + "]";
	}

}
