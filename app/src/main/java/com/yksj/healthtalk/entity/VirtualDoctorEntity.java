package com.yksj.healthtalk.entity;

/**
 * 虚拟医生聊天实体类
 * @author zhao yuan
 *
 */
public class VirtualDoctorEntity {
	/**
	 * 时间
	 */
	public static final int TYPE_TIME = 0;
	
	/**
	 * 纯文字消息
	 */
	public static final int  TXT_TYPE = 3;
	/**
	 * 数字键盘消息
	 */
	public static final int NUMBER_TYPE = 1;
	/**
	 * 关联消息
	 */
	public static final int LINK_TYPE = 2;
	
	private int manflag;//用于是否调出人体
	
	private int retsex;//性别
	
	//消息id
	private long id  = -1;
	
	//发送者id
	private String senderId = "-1";
	
	private String receiverId;
	
	//选中状态
	private boolean isSelected = false;
	
	//日期
	private String date;
	
	//消息内容
	private String content;

	//消息类型
	private int type;
	
	//消息编码
	private String messageCode;

	private String messageClass;
	
	//0or1
	private int retFeatur;

	//用于区分话题还是服务1话题2服务
	private String functionId;
	
	//区别消息发送与接收
	private boolean isSendFlag = false;
	
	private String linkDialog;
	
	private String ltalkName;
	
	//服务关联id
	private String serviceLinkId;
	
	//服务关联名称
	private String serviceLinkName;
	
	//消息是否已经读取0未读取 1读取
	private int readTag = 1;
	
	private String largeinfoId;
	
	private String talkname;
	
	//发送状态
	private int sendState = 0;

	//症状编码
	private String symptomCode;
	
	
	public String getSymptomCode() {
		return symptomCode;
	}

	public void setSymptomCode(String symptomCode) {
		this.symptomCode = symptomCode;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public int getManflag() {
		return manflag;
	}

	public void setManflag(int manflag) {
		this.manflag = manflag;
	}

	public int getRetsex() {
		return retsex;
	}

	public void setRetsex(int retsex) {
		this.retsex = retsex;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	public int getReadTag() {
		return readTag;
	}

	public void setReadTag(int readTag) {
		this.readTag = readTag;
	}

	public String getLtalkName() {
		return ltalkName;
	}

	public void setLtalkName(String ltalkName) {
		this.ltalkName = ltalkName;
	}
	
	public String getLargeinfoId() {
		return largeinfoId;
	}

	public void setLargeinfoId(String largeinfoId) {
		this.largeinfoId = largeinfoId;
	}

	public String getTalkname() {
		return talkname;
	}

	public void setTalkname(String talkname) {
		this.talkname = talkname;
	}
	
	public String getServiceLinkId() {
		return serviceLinkId;
	}

	public void setServiceLinkId(String serviceLinkId) {
		this.serviceLinkId = serviceLinkId;
	}

	public String getServiceLinkName() {
		return serviceLinkName;
	}

	public void setServiceLinkName(String serviceLinkName) {
		this.serviceLinkName = serviceLinkName;
	}

	public int getRetFeatur() {
		return retFeatur;
	}

	public void setRetFeatur(int retFeatur) {
		this.retFeatur = retFeatur;
	}


	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getLinkDialog() {
		return linkDialog;
	}

	public void setLinkDialog(String linkDialog) {
		this.linkDialog = linkDialog;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageClass() {
		return messageClass;
	}

	public void setMessageClass(String messageClass) {
		this.messageClass = messageClass;
	}

	public boolean isSendFlag() {
		return isSendFlag;
	}

	public void setSendFlag(boolean isSendFlag) {
		this.isSendFlag = isSendFlag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
