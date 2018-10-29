package com.yksj.healthtalk.entity;

public class CommendEntity {
	
	private String messageContent;
	private String sendTime;
	private String customerNickname;
	private String iconUrl;
	private String userid;
	private String sex;//性别
	
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getCustomerNickname() {
		return customerNickname;
	}
	public void setCustomerNickname(String customerNickname) {
		this.customerNickname = customerNickname;
	}

	public class Constant {
		
		public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
		public static final String SEND_TIME = "SEND_TIME";
		public static final String CUSTOMER_NICKNAME = "CUSTOMER_NICKNAME";
		public static final String ICON_URL = "CLIENT_ICON_BACKGROUND";
		
	}
	
}
