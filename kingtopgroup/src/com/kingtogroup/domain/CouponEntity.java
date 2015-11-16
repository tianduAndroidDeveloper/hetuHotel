package com.kingtogroup.domain;

public class CouponEntity {
	private int couponId;
	private String name;
	private int money;
	private int useExpireTime;
	private int state;

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getUseExpireTime() {
		return useExpireTime;
	}

	public void setUseExpireTime(int useExpireTime) {
		this.useExpireTime = useExpireTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public CouponEntity(int couponId, String name, int money,
			int useExpireTime, int state) {
		super();
		this.couponId = couponId;
		this.name = name;
		this.money = money;
		this.useExpireTime = useExpireTime;
		this.state = state;
	}

	@Override
	public String toString() {
		return "CouponEntity [couponId=" + couponId + ", name=" + name
				+ ", money=" + money + ", useExpireTime=" + useExpireTime
				+ ", state=" + state + "]";
	}

}
