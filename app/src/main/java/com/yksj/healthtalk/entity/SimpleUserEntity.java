package com.yksj.healthtalk.entity;

public class SimpleUserEntity {
	
	private String cusid;
	private String user_icon_address;
	private String cusName;
	/**
	 * 是否被选择  Y  N
	 */
	private String isBeChoose;
	
	public String getIsBeChoose() {
		return isBeChoose;
	}
	public void setIsBeChoose(String isBeChoose) {
		this.isBeChoose = isBeChoose;
	}
	public String getCusid() {
		return cusid;
	}
	public void setCusid(String cusid) {
		this.cusid = cusid;
	}
	public String getUser_icon_address() {
		return user_icon_address;
	}
	public void setUser_icon_address(String user_icon_address) {
		this.user_icon_address = user_icon_address;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
}
