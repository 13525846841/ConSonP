package com.yksj.healthtalk.entity;


/**
 * 基本信息
 */
public class BaseInfoEntity {
//	public volatile WeakReference<ViewHolder> viewHolder;//软引用
	public String id;// 当前用户
	public String name;
	public String bigHeadIcon;//大头像
	public String normalHeadIcon;//小头像
	public int isConnection;
	public String messgeNumber;// 未读消息数量
	public String description = "";//个人说明
	public String inital = ""; // 首字母
	// 一句话说明
	public String cusMessage;
	public Object mViewHold;

	public BaseInfoEntity(String id, String name, String bigHeadIcon,
			String normalHeadIcon, int isConnection, String messgeNumber,
			String description, String inital, String cusMessage) {
		super();
		this.id = id;
		this.name = name;
		this.bigHeadIcon = bigHeadIcon;
		this.normalHeadIcon = normalHeadIcon;
		this.isConnection = isConnection;
		this.messgeNumber = messgeNumber;
		this.description = description;
		this.inital = inital;
		this.cusMessage = cusMessage;
	}

	
	public BaseInfoEntity() {
	}

	public String getNickName(){
		return name;
	}
	public String getCusMessag() {
		return cusMessage;
	}

	public void setCusMessag(String cusMessage) {
		this.cusMessage = cusMessage.trim();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBigHeadIcon() {
		return bigHeadIcon;
	}

	public void setBigHeadIcon(String bigHeadIcon) {
		this.bigHeadIcon = bigHeadIcon;
	}

	public String getNormalHeadIcon() {
		return normalHeadIcon;
	}

	public void setNormalHeadIcon(String normalHeadIcon) {
		this.normalHeadIcon = normalHeadIcon;
	}

	public int getIsConnection() {
		return isConnection;
	}

	public void setIsConnection(int isConnection) {
		this.isConnection = isConnection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

	public String getInital() {
		return inital;
	}

	public void setInital(String inital) {
		this.inital = inital;
	}

	public String getMessgeNumber() {
		return messgeNumber;
	}

	public void setMessgeNumber(String messgeNumber) {
		this.messgeNumber = messgeNumber;
	}




}
