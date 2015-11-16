package com.kingtogroup.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int oid;
	public String osn;
	public int uid;
	public int orderstate;
	public int orderamount;
	public int surplusmoney;
	public int parentid;
	public int isreview;
	public String addtime;
	public int storeid;
	public int shipcoid;
	public String shipconame;
	public String payfriendname;
	public int paymode;
	public int couponmoney;
	public String paysystemname;
	public String consignee;
	public String mobile;
	public String address;
	public List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
	public boolean checked = false;

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", osn=" + osn + ", uid=" + uid
				+ ", orderstate=" + orderstate + ", orderamount=" + orderamount
				+ ", surplusmoney=" + surplusmoney + ", parentid=" + parentid
				+ ", isreview=" + isreview + ", addtime=" + addtime
				+ ", storeid=" + storeid + ", shipcoid=" + shipcoid
				+ ", shipconame=" + shipconame + ", payfriendname="
				+ payfriendname + ", paymode=" + paymode + ", couponmoney="
				+ couponmoney + ", paysystemname=" + paysystemname
				+ ", consignee=" + consignee + ", mobile=" + mobile
				+ ", address=" + address + "]";
	}
}
