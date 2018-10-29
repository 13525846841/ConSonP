package com.yksj.healthtalk.entity;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 群信息
 */
public class GroupInfoEntity extends BaseInfoEntity implements Parcelable{
	

	public GroupInfoEntity() {
	}
	private String recordDesc="";//群组主题
	private String groupHeadPortraitID;//群组头像ID
	private String groupHeadPortraitName;//群组头像名称
	private String createCustomerID;//创建客户ID
	private String createTime="";//创建时间
	private String limitNumber="";
	private String note="";//备注
	private String infoLayName="";//信息层面名称
	private boolean publicCustInfo = false; //创建者信息是否公开
	private boolean salon = false; //是否医生沙龙
	private String onLineNumber = ""; //在线人数
	private String personNumber = ""; //群成员人数
	private boolean  inceptMessage = true;//是否接受信息
	private boolean isShowPersonNumber = true;
	private boolean charge;//是否收费
	private String groupLevel = "0";
	private String groupClass;//话题类型
	private boolean isSalonAttention; //是否关注
	private Boolean isReleaseSystemMessage;//是否将消息发布到消息厅(1-发布，0-不发布)
	private String flag="";//翻页标记
	private String flagPlacing="";//硬性排序标记
	private int type;//广告 名称 类型
	private String merchantId = "";//商户Id "" 表示是普通沙龙 
	private int pagesize ; 
	private int pagenum;
	private String orderInfo;//是否要再次支付
	private String ticketMsg;//门票信息,医生端开通门票才有
	private String lastMsg ;//最后一条消息
	private String lastMsgTime ;//最后一条消息时间
	private String lastMsgType ;//最后一条消息类型
	private String isBL ;//是否是病历：（1是病历，0是会诊）
	private String objectType;

	public String getIsBL() {
		return isBL;
	}

	public void setIsBL(String isBL) {
		this.isBL = isBL;
	}

	public String getFlagPlacing() {
		return flagPlacing;
	}

	public String getLastMsgTime() {
		return lastMsgTime;
	}

	public String getLastMsgType() {
		return lastMsgType;
	}

	public void setLastMsgType(String lastMsgType) {
		this.lastMsgType = lastMsgType;
	}

	public void setLastMsgTime(String lastMsgTime) {
		this.lastMsgTime = lastMsgTime;
	}

	public void setFlagPlacing(String flagPlacing) {
		this.flagPlacing = flagPlacing;
	}
	private String upperId;
	private String  infoId;
	private boolean yesornoShow;//是否显示在线人数
	
	
	public String getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	public String getTicketMsg() {
		return ticketMsg;
	}

	public void setTicketMsg(String ticketMsg) {
		this.ticketMsg = ticketMsg;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	

	public boolean isYesornoShow() {
		return yesornoShow;
	}

	public void setYesornoShow(boolean yesornoShow) {
		this.yesornoShow = yesornoShow;
	}

	public String getUpperId() {
		return upperId;
	}

	public Boolean getIsReleaseSystemMessage() {
		return isReleaseSystemMessage;
	}

	public void setIsReleaseSystemMessage(Boolean isReleaseSystemMessage) {
		this.isReleaseSystemMessage = isReleaseSystemMessage;
	}

//	public String getGroupflag() {
//		return groupflag;
//	}
//
//	public void setGroupflag(String groupflag) {
//		this.groupflag = groupflag;
//	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isSalonAttention() {
		return isSalonAttention;
	}

	public void setSalonAttention(boolean isSalonAttention) {
		this.isSalonAttention = isSalonAttention;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public boolean isCharge() {
		return charge;
	}

	public void setCharge(boolean charge) {
		this.charge = charge;
	}

	public void setGroupLevel(String groupLevel) {
		if(!"".equals(groupLevel)){
			this.groupLevel = groupLevel;
		}
	}
	
	//"yesornoShow":false
	
	public String getOnLineNumber() {
		return onLineNumber;
	}

	public void setOnLineNumber(String onLineNumber) {
		this.onLineNumber = onLineNumber;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public boolean isInceptMessage() {
		return inceptMessage;
	}

	public void setInceptMessage(boolean inceptMessage) {
		this.inceptMessage = inceptMessage;
	}

	public String getGroupClass() {
		return groupClass;
	}

	public void setGroupClass(String groupClass) {
		this.groupClass = groupClass;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public boolean isShowPersonNumber() {
		return isShowPersonNumber;
	}

	public void setShowPersonNumber(boolean isShowPersonNumber) {
		this.isShowPersonNumber = isShowPersonNumber;
	}

	public String getLimitNumber() {
		return limitNumber;
	}
	
	public void setLimitNumber(String limitNumber) {
		this.limitNumber = limitNumber;
	}
	
	public String getRecordDesc() {
		return recordDesc;
	}
	public void setRecordDesc(String recordDesc) {
		this.recordDesc = recordDesc;
	}
	@Override
	public String getCusMessag() {
		
		return super.getCusMessag();
	}
	@Override
	public void setCusMessag(String cusMessage) {
		
		super.setCusMessag(cusMessage);
	}
	@Override
	public String getId() {
		
		return super.getId();
	}
	@Override
	public void setId(String id) {
		
		super.setId(id);
	}
	@Override
	public String getName() {
		
		return super.getName();
	}
	@Override
	public void setName(String name) {
		
		super.setName(name);
	}
	@Override
	public String getBigHeadIcon() {
		
		return super.getBigHeadIcon();
	}
	@Override
	public void setBigHeadIcon(String bigHeadIcon) {
		
		super.setBigHeadIcon(bigHeadIcon);
	}
	@Override
	public String getNormalHeadIcon() {
		
		return super.getNormalHeadIcon();
	}
	@Override
	public void setNormalHeadIcon(String normalHeadIcon) {
		
		super.setNormalHeadIcon(normalHeadIcon);
	}
	@Override
	public int getIsConnection() {
		
		return super.getIsConnection();
	}
	@Override
	public void setIsConnection(int isConnection) {
		
		super.setIsConnection(isConnection);
	}
	@Override
	public String getDescription() {
		
		return super.getDescription();
	}
	@Override
	public void setDescription(String description) {
		
		super.setDescription(description);
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		
		return super.clone();
	}
	@Override
	public boolean equals(Object o) {
		
		return super.equals(o);
	}
	@Override
	protected void finalize() throws Throwable {
		
		super.finalize();
	}
	@Override
	public int hashCode() {
		
		return super.hashCode();
	}
	@Override
	public String toString() {
		
		return super.toString();
	}
	public String getGroupHeadPortraitID() {
		return groupHeadPortraitID;
	}
	public void setGroupHeadPortraitID(String groupHeadPortraitID) {
		this.groupHeadPortraitID = groupHeadPortraitID;
	}
	public String getGroupHeadPortraitName() {
		return groupHeadPortraitName;
	}
	public void setGroupHeadPortraitName(String groupHeadPortraitName) {
		this.groupHeadPortraitName = groupHeadPortraitName;
	}
	public String getCreateCustomerID() {
		return createCustomerID;
	}
	public void setCreateCustomerID(String createCustomerID) {
		this.createCustomerID = createCustomerID;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
//	public String getInfoLayid() {
//		return infoLayid;
//	}
//	public void setInfoLayid(String infoLayid) {
//		this.infoLayid = infoLayid;
//	}
	public String getInfoLayName() {
		return infoLayName;
	}
	public void setInfoLayName(String infoLayName) {
		this.infoLayName = infoLayName.trim();
	}

	public boolean isPublicCustInfo() {
		return publicCustInfo;
	}

	public void setPublicCustInfo(boolean publicCustInfo) {
		this.publicCustInfo = publicCustInfo;
	}

	public boolean isSalon() {
		return salon;
	}

	public void setSalon(boolean salon) {
		this.salon = salon;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectType() {
		return objectType;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.bigHeadIcon);
		dest.writeString(this.normalHeadIcon);
		dest.writeInt(this.isConnection);
		dest.writeString(this.messgeNumber);
		
		dest.writeString(this.recordDesc);
		dest.writeString(this.groupHeadPortraitID);
		dest.writeString(this.groupHeadPortraitName);
		dest.writeString(this.createCustomerID);
		dest.writeString(this.createTime);
		dest.writeString(this.limitNumber);
		dest.writeString(this.note);
//		dest.writeString(this.infoLayid);
		dest.writeString(this.infoLayName);
		dest.writeValue(this.publicCustInfo);
		dest.writeValue(this.salon);
		dest.writeString(this.onLineNumber);
		dest.writeString(this.personNumber);
		dest.writeValue(this.inceptMessage);
		dest.writeValue(this.isShowPersonNumber);
		dest.writeString(this.groupLevel);
		dest.writeValue(this.charge);
		dest.writeValue(this.isSalonAttention);
		dest.writeString(this.upperId);
		dest.writeString(this.infoId);
		dest.writeValue(this.isReleaseSystemMessage);
		dest.writeString(this.merchantId);
		dest.writeString(this.ticketMsg);
		dest.writeString(this.lastMsg);
		dest.writeString(this.lastMsgTime);
		dest.writeString(this.lastMsgType);
		dest.writeString(this.groupClass);
		dest.writeString(this.isBL);
		dest.writeString(this.objectType);
	}
	
	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
		public static final Parcelable.Creator<GroupInfoEntity> CREATOR = new Creator<GroupInfoEntity>() {

			@Override
			public GroupInfoEntity[] newArray(int size) {
				
				return new GroupInfoEntity[size];
			}
//			public String id;// 当前用户
//			public String name;
//			public String bigHeadIcon;
//			public String normalHeadIcon;
//			public int isConnection;
//			public String messgeNumber;// 未读消息数量
			
			@Override
			public GroupInfoEntity createFromParcel(Parcel source) {
				GroupInfoEntity entity = new GroupInfoEntity();
				entity.id = source.readString();
				entity.name = source.readString();
				entity.bigHeadIcon = source.readString();
				entity.normalHeadIcon = source.readString();
				entity.isConnection = source.readInt();
				entity.messgeNumber = source.readString();
				
				entity.recordDesc = source.readString();
				entity.groupHeadPortraitID = source.readString();
				entity.groupHeadPortraitName = source.readString();
				entity.createCustomerID = source.readString();
				entity.createTime = source.readString();
				entity.limitNumber = source.readString();
				entity.note = source.readString();
//				entity.infoLayid = source.readString();
				entity.infoLayName = source.readString();
				entity.publicCustInfo = (Boolean) source.readValue(null);
				entity.salon = (Boolean) source.readValue(null);
				entity.onLineNumber = source.readString();
				entity.personNumber = source.readString();
				entity.inceptMessage = (Boolean) source.readValue(null);
				entity.isShowPersonNumber = (Boolean) source.readValue(null);
				entity.groupLevel = source.readString();
				entity.charge = (Boolean) source.readValue(null);
				entity.isSalonAttention = (Boolean) source.readValue(null);
				entity.upperId=source.readString();
				entity.infoId = source.readString();
				entity.isReleaseSystemMessage = (Boolean) source.readValue(null);
				entity.merchantId = source.readString();
				entity.ticketMsg=source.readString();
				entity.lastMsg=source.readString();
				entity.lastMsgTime=source.readString();
				entity.lastMsgType=source.readString();
				entity.groupClass=source.readString();
				entity.isBL=source.readString();
				entity.objectType=source.readString();
				return entity;
			}
		};

}
