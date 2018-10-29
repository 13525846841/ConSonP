package com.yksj.healthtalk.entity;

import android.provider.SyncStateContract.Columns;

public class InterestWallImageEntity {

	private String id;// 图片id
	private String imagepath;// 图片路径
	private String customerid;// 上传图片的用户id
	private String customerNickname; // 图片发布用户的名字
	private String picName;// 图片名字
	private String picDesc;// 图片描述
	private String uploadTime;// 上传时间
	private String picUrl;// 图片关联店铺连接
	private String verifyState;// 审核标记 10-未审核 20-已审核 30-审核失败
	private String stateTime;// 审核状态改变时间
	private String clickCount;// 点击次数
	private String collectionCount;// 收藏次数
	private String commentCount;// 评论次数
	private String transmitCount; // 转发次数
	private String shareCount; // 分享次数
	private String groupId; // 关联沙龙id
	private String recordName;// 关联沙龙名字
	private String recordDesc;// 关联沙龙描述
	private String publicCustomerInfo;// 是否公布创建者信息
	private String groupClass;// 沙龙类型
	private String groupLevel;// 沙龙等级
	private String releaseSystemMessage;// 是否将信息发布到信息厅
	private String customerHeadPic;
	private String shop_place; //图片在摄影馆内的位置
	private String classId; //分组id
	private String infoLayId;
	private String ROLE_ID;//是否是医生  777 888是医生
	private String CUSTOMER_SEX;//性别
	private String INFORMATION_LAY;//标签
	private String LIKENUM ;//赞数量
	private String ISLIKE ;//是否赞过 1-赞过  0-没赞过
	private String DISLIKENUM ;// 踩数量 
	private String  ISDISLIKE ;//是否踩过 1-彩果 0-没踩过
	private String  IS_COLLECTION ;//是否收藏
	
	public String getIS_COLLECTION() {
		return IS_COLLECTION;
	}

	public void setIS_COLLECTION(String iS_COLLECTION) {
		IS_COLLECTION = iS_COLLECTION;
	}

	public String getLIKENUM() {
		return LIKENUM;
	}

	public void setLIKENUM(String lIKENUM) {
		LIKENUM = lIKENUM;
	}

	public String getISLIKE() {
		return ISLIKE;
	}

	public void setISLIKE(String iSLIKE) {
		ISLIKE = iSLIKE;
	}

	public String getDISLIKENUM() {
		return DISLIKENUM;
	}

	public void setDISLIKENUM(String dISLIKENUM) {
		DISLIKENUM = dISLIKENUM;
	}

	public String getISDISLIKE() {
		return ISDISLIKE;
	}

	public void setISDISLIKE(String iSDISLIKE) {
		ISDISLIKE = iSDISLIKE;
	}

	public String getINFORMATION_LAY() {
		return INFORMATION_LAY;
	}

	public void setINFORMATION_LAY(String iNFORMATION_LAY) {
		INFORMATION_LAY = iNFORMATION_LAY;
	}

	public String getCUSTOMER_SEX() {
		return CUSTOMER_SEX;
	}

	public void setCUSTOMER_SEX(String cUSTOMER_SEX) {
		CUSTOMER_SEX = cUSTOMER_SEX;
	}

	public String getIsDoctor() {
		return ROLE_ID;
	}

	public void setIsDoctor(String isDoctor) {
		this.ROLE_ID = isDoctor;
	}

	public String getInfoLayId() {
		return infoLayId;
	}

	public void setInfoLayId(String infoLayId) {
		this.infoLayId = infoLayId;
	}

	public String getShop_place() {
		return shop_place;
	}
/**
 * 是否审核成功
 * @return
 */
	public boolean getverifySuccess(){
		return "20".equals(getVerifyState());
	}
	public void setShop_place(String shop_place) {
		this.shop_place = shop_place;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getCustomerHeadPic() {
		return customerHeadPic;
	}

	public void setCustomerHeadPic(String customerHeadPic) {
		this.customerHeadPic = customerHeadPic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getVerifyState() {
		return verifyState;
	}

	public void setVerifyState(String verifyState) {
		this.verifyState = verifyState;
	}

	public String getStateTime() {
		return stateTime;
	}

	public void setStateTime(String stateTime) {
		this.stateTime = stateTime;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(String collectionCount) {
		this.collectionCount = collectionCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getTransmitCount() {
		return transmitCount;
	}

	public void setTransmitCount(String transmitCount) {
		this.transmitCount = transmitCount;
	}

	public String getShareCount() {
		return shareCount;
	}

	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getRecordDesc() {
		return recordDesc;
	}

	public void setRecordDesc(String recordDesc) {
		this.recordDesc = recordDesc;
	}

	public String getPublicCustomerInfo() {
		return publicCustomerInfo;
	}

	public void setPublicCustomerInfo(String publicCustomerInfo) {
		this.publicCustomerInfo = publicCustomerInfo;
	}

	public String getGroupClass() {
		return groupClass;
	}

	public void setGroupClass(String groupClass) {
		this.groupClass = groupClass;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		this.groupLevel = groupLevel;
	}

	public String getReleaseSystemMessage() {
		return releaseSystemMessage;
	}

	public void setReleaseSystemMessage(String releaseSystemMessage) {
		this.releaseSystemMessage = releaseSystemMessage;
	}

	public String getCustomerNickname() {
		return customerNickname;
	}

	public void setCustomerNickname(String customerNickname) {
		this.customerNickname = customerNickname;
	}

	public class Constant implements Columns {
		public static final String ID = "PICTURE_ID";// 图片id
		public static final String IMAGEPATH = "BIG_PICTURE_PATH";// 图片路径
		public static final String CUSTOMERID = "CUSTOMER_ID";// 上传图片的用户id
		public static final String PICNAME = "PICTURE_NAME";// 图片名字
		public static final String PICDESC = "PICTURE_DESC";// 图片描述
		public static final String UPLOADTIME = "UPLOAD_TIME";// 上传时间
		public static final String PICURL = "PICTURE_URL";// 图片关联店铺连接
		public static final String VERIFYSTATE = "VERIFY_STATE";// 审核标记 10-未审核
																// 20-已审核
																// 30-审核失败
		public static final String STATETIME = "STATE_TIME";// 审核状态改变时间
		public static final String CLICKCOUNT = "CLICK_COUNT";// 点击次数
		public static final String COLLECTIONCOUNT = "COLLECTION_COUNT";// 收藏次数
		public static final String COMMENTCOUNT = "COMMENT_COUNT";// 评论次数
		public static final String TRANSMITCOUNT = "FORWARD_COUNT"; // 转发次数
		public static final String SHARECOUNT = "SHARE_COUNT"; // 分享次数
		public static final String GROUPID = "GROUP_ID"; // 关联沙龙id
		public static final String RECORDNAME = "RECORD_NAME";// 关联沙龙名字
		public static final String RECORDDESC = "RECORD_DESC";// 关联沙龙描述
		public static final String PUBLICCUSTOMERINFO = "PUBLIC_CUSTOMER_INFO";// 是否公布创建者信息
		public static final String GROUPCLASS = "GROUP_CLASS";// 沙龙类型
		public static final String GROUPLEVEL = "GROUP_LEVEL";// 沙龙等级
		public static final String RELEASESYSTEMMESSAGE = "RELEASE_SYSTEM_MESSAGE";// 是否将信息发布到信息厅
		public static final String CUSTOMERNICKNAME = "CUSTOMER_NICKNAME";// 是否将信息发布到信息厅
		public static final String IS_COLLECTION = "IS_COLLECTION";// 是否将信息发布到信息厅
	}

}
