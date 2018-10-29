package com.yksj.healthtalk.entity;

import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.yksj.healthtalk.utils.HStringUtil;

import org.universalimageloader.core.download.ImageDownloader;

import java.io.Serializable;

public class CustomerInfoEntity extends BaseInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    public CustomerInfoEntity() {

    }

    private String age;// 年龄
    private String sex = "0";// 性别 0--全部 1--男 2--女
    private String username;// 用户名
    private String doctorunitAdd = "";// 所属医疗机构地址
    private String doctorunit = "";// 所属医疗机构
    private String email = "";// 绑定email
    private String customerlocus = "";//
    private String dwellingplace = "";// 详细住址 所在地
    private String accomplishedname = "";// 专长名称 擅长方向
    private String metier = ""; // 职业
    private String xueli = ""; // 学历
    private String hunyin = ""; // 婚姻
    private String poneNumber = "";// 手机号
    // private List<String> feature; // 特征
    private String birthday = ""; // 出生日期
    private String lableJson = ""; // 个性标签(存储的是json格式)
    private int roldid; // 判断是不是V 666第一次审核 777再次审核 888审核成功
    private int money = 0; // 多美币
    private String phoneName = ""; // 通讯录中个的名称
    private String json = "";// 获得个人资料的json数据
    private int AttentionFriend;// 关系  0 没有关系 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者
    private int attentionFriendFlag;//**********废弃
    //废弃   1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户
    // 6加医生 7取消医生 8我的合作者 9取消我的合作者
    private String latitude = ""; // 经度
    private String longitude = ""; // 纬度
    private String flag; // 请求标记
    private String ticketFlag;// 标记该用户是否购买了日票,月票 "0"没有购买 "1"日票 "2"月票
    private int minAge;
    private int maxAge;
    private int sameExperienceCode;
    private String interestCode = "";// 个性标签编码
    private String interestName = "";// 个性编码名
    private int areaCode;// 地区编码
    private String areaName = "";// 地区名字
    private String sameExperience = "";// 相同经历
    private String cultureLevel = "";// 学历
    private double distance;// 距离
    private int errorCode;
    private String gameLevel = "";// 角色等级
    private String onlineState = "";
    private int type; // 0--找朋友 1--找医生 2--相同经历
    private int backGold; // 加黑名单的时候退费的金额
    private int customerGroupRel;// 聊天界面:是否在群的黑名单
    private String infoVersion = "";// 资料的等级
    private String serviceEnd;// 挂号结束时间
    private String phonefriendName;// 匹配的电话薄名称
    private String payId;// 订单ID
    /**
     * 医师资料
     */
    private String realname; // 真实姓名
    private String doctorEmail; // 邮箱
    private String telePhone; // 固话
    private String mobilePhone; // 手机
    private String hospital; // 所在医院
    private String special; // 专长
    private String doctorMessage; // 医生公告

    private String doctorWorkaddress;// 工作地点
    private String doctorWorkaddressCode;// 工作地点编码
    private String doctorTitle;// 职称编号 主治医师
    private String doctorTitleName;// 职称名称 主治医师
    // private String auditMark; // 审核标记 888审核通过 777 再次审核 666 初次审核中

    private String officeCode1;
    private String officeCode2;

    private String officeName1;
    private String officeName2;
    /*
	 * private String office; // 科室 private String office2; // 科室2
	 *
	 * private String doctorOfficeCode;// 科室编码 private String
	 * doctorOfficeName;// 科室编码
	 */

    private String doctorBigPicture;// 医师大头像
    private String doctorClientPicture;// 医师小头像

    private int orderOnOff;// 挂号标记 1-开；0-关

    private String servicePrice;// 挂号价格
    private String orderId;// 支付宝账号
    private String serviceTypeId;// 服务类型的ID SERVICE_TYPE_ID
    // (1为普通服务，2为预约时段服务,3为预约面访服务)
    private String serviceTypeName;// 服务类型的名称
    private String serviceTypeSubId;// 子服务类型ID
    private String serviceItemId;
    private int customerLevel;// 普通客户等级
    private int doctorLevel;// 医生身份等级
    private String doctorPosition;// 医生资质类型
    private String academicJob;//ACADEMIC_JOB
    private String resumeContent;//RESUME_CONTENT
    private String talkOnOff;//私聊开关ME_CONTENT
    private String avToken;//网易云视频token


    /**
     * 微博绑定
     *
     * @return
     */
    private String setBindSinaState;// 绑定微博的状态
    private String setBindSessionCod;// 微博绑定的token
    private String microBlogUID;// 微博的UID
    private String serviceStatus;// 挂号服务的状态描述
    private String serviceTime;// 挂号开始的时间
    private String serviceStatusCode;// 挂号服务的状态码
    private String MicroBlogCode;// 微博账号
    private String microBlogTimeOut;// 过期时间

    /**
     * 3.0医生添加的属性 doctorCertificate医师证照片存储地址，
     * transferName转账途径名称(开户行名称、支付宝、财富通等)， transferCode转账帐号(开户行卡号、支付宝帐号、财富通帐号等)，
     * transferAddr转账途径地址(开户行地址)， transferGetName收款人开户名(如果选择银行转帐，此字段必填)，
     * transferGetTele收款人联系电话
     *
     * @return
     */
    private String doctorCertificate;// 医师证照片存储地址
    private String transferName;// 转账途径名称(开户行名称、支付宝、财富通等)
    private String transferCode;// 转账帐号(开户行卡号、支付宝帐号、财富通帐号等)
    private String transferGetName;// 收款人开户名(如果选择银行转帐，此字段必填)
    private String transferGetTele;// 收款人联系电话
    private String transferAddr;// 转账途径地址(开户行地址)，
    private String serviceTypes;// 挂号服务类型
    private String orderServiceTypes;// 医生订制服务类型
    private String qrCode;// 二维码
    private String qrCodeIcon;// 二维码图片
    private String registerCount;// 挂号次数,这个在历史挂号中显示
    private String remarkName = "";// 备注名字
    private String praiseCount = "";//点赞数量
    private String praiseFlag = "";//自己是否对对方点过赞   赞标记  0-没赞过
    private String lastMessage;
    private String lastMessageTime;
    private String lastMessageType;
    private String messageTime;//医生公告发布时间
    private String consultationId;//会诊id
    private String centerName;//会诊id
    private String relType;//会诊关注关系 REL_TYPE    Y-是好友，B-黑名单,C-患者,D-我的医生  “”没有关系
    private String sortLetters;
    private String introduction;//简介
    private String hospitalCode; // 所在医院编码
    private String objectType;
    private String isSetPwd;//是否设置了钱包密码
    private AVChatData avData;//网易云客户信息
    private String sixOneAccoutn;//六一账号


    public AVChatData getAvData() {
        return avData;
    }

    public void setAvData(AVChatData avData) {
        this.avData = avData;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getVerifyFlag() {
        return verifyFlag;
    }

    public void setVerifyFlag(String verifyFlag) {
        this.verifyFlag = verifyFlag;
    }

    public String getBandingState() {
        return bandingState;
    }

    public void setBandingState(String bandingState) {
        this.bandingState = bandingState;
    }

    public String getDiseaseDesc() {
        return diseaseDesc;
    }

    public void setDiseaseDesc(String diseaseDesc) {
        this.diseaseDesc = diseaseDesc;
    }

    public String getIsSetPwd() {
        return isSetPwd;
    }

    public void setIsSetPwd(String isSetPwd) {
        this.isSetPwd = isSetPwd;
    }

    private String verifyFlag;//验证标记
    private String bandingState;//绑定状态
    private String diseaseDesc;//绑定状态


    public String PHONE_NUMBER;

    /**
     * 0 没有关系 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者
     *
     * @return
     */
    public int getAttentionNumber() {
        if (AttentionFriend == 0) { //
            return 0;//未关注
        } else if (AttentionFriend == 2) {
            return 2;//黑名单
        } else {
            return 1;//已关注
        }
    }

    public String getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }


    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(String praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getOrderServiceTypes() {
        return orderServiceTypes;
    }

    public void setOrderServiceTypes(String orderServiceTypes) {
        this.orderServiceTypes = orderServiceTypes;
    }

    public String getOfficeCode1() {
        return officeCode1;
    }

    public void setOfficeCode1(String officeCode1) {
        this.officeCode1 = officeCode1;
    }

    public String getOfficeCode2() {
        return officeCode2;
    }

    public void setOfficeCode2(String officeCode2) {
        this.officeCode2 = officeCode2;
    }

    public String getOfficeName1() {
        return officeName1;
    }

    public void setOfficeName1(String officeName1) {
        this.officeName1 = officeName1;
    }

    public String getOfficeName2() {
        return officeName2;
    }

    public void setOfficeName2(String officeName2) {
        this.officeName2 = officeName2;
    }

    public int getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(int customerLevel) {
        this.customerLevel = customerLevel;
    }

    public int getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(int doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getServiceTypeSubId() {
        return serviceTypeSubId;
    }

    public void setServiceTypeSubId(String serviceTypeSubId) {
        this.serviceTypeSubId = serviceTypeSubId;
    }

    public String getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }


    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getPhonefriendName() {
        return phonefriendName;
    }

    public void setPhonefriendName(String phonefriendName) {
        this.phonefriendName = phonefriendName;
    }

    public String getDoctorWorkaddress() {
        return doctorWorkaddress;
    }

    public String getDoctorWorkaddressCode() {
        return doctorWorkaddressCode;
    }

    public void setDoctorWorkaddressCode(String doctorWorkaddressCode) {
        this.doctorWorkaddressCode = doctorWorkaddressCode;
    }

    public String getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(String serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public void setDoctorWorkaddress(String doctorWorkaddress) {
        this.doctorWorkaddress = doctorWorkaddress;
    }

    /*
     * public String getOffice2() { return office2; }
     *
     * public void setOffice2(String office2) { this.office2 = office2.trim(); }
     */
    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(String registerCount) {
        this.registerCount = registerCount;
    }

    public String getQrCodeIcon() {
        return qrCodeIcon;
    }

    public void setQrCodeIcon(String qrCodeIcon) {
        this.qrCodeIcon = qrCodeIcon;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTransferAddr() {
        return transferAddr;
    }

    public void setTransferAddr(String transferAddr) {
        this.transferAddr = transferAddr;
    }

    public String getDoctorCertificate() {
        return doctorCertificate;
    }

    public void setDoctorCertificate(String doctorCertificate) {
        this.doctorCertificate = doctorCertificate;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public String getTransferGetName() {
        return transferGetName;
    }

    public void setTransferGetName(String transferGetName) {
        this.transferGetName = transferGetName;
    }

    public String getTransferGetTele() {
        return transferGetTele;
    }

    public void setTransferGetTele(String transferGetTele) {
        this.transferGetTele = transferGetTele;
    }

    public String getMicroBlogCode() {
        return MicroBlogCode;
    }

    public void setMicroBlogCode(String microBlogCode) {
        MicroBlogCode = microBlogCode;
    }

    public String getServiceEnd() {
        return serviceEnd;
    }

    public void setServiceEnd(String serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    /**
     * 是否显示v标记
     *
     * @return
     */
    public boolean isShowDoctorV() {
        if (roldid == 777 || roldid == 888) {
            return true;
        }
        return false;
    }

    public static boolean isShowDoctorV(int role) {
        if (role == 777 || role == 888) {
            return true;
        }
        return false;
    }

    public String getInfoVersion() {
        return infoVersion;
    }

    public void setInfoVersion(String infoVersion) {
        this.infoVersion = infoVersion;
    }

    public int getCustomerGroupRel() {
        return customerGroupRel;
    }

    public void setCustomerGroupRel(int customerGroupRel) {
        this.customerGroupRel = customerGroupRel;
    }

    public int getBackGold() {
        return backGold;
    }

    public void setBackGold(int backGold) {
        this.backGold = backGold;
    }

    public String getServiceStatusCode() {
        return serviceStatusCode;
    }

    public void setServiceStatusCode(String serviceStatusCode) {
        this.serviceStatusCode = serviceStatusCode;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getOrderOnOff() {
        return orderOnOff;
    }

    public void setOrderOnOff(int orderOnOff) {
        this.orderOnOff = orderOnOff;
    }

//	public int getAttentionFriendFlag() {
//		return attentionFriendFlag;
//	}
//
//	private void setAttentionFriendFlag(int attentionFriendFlag) {
//		this.attentionFriendFlag = attentionFriendFlag;
//	}

    public String getDoctorTitleName() {
        return doctorTitleName;
    }

    public void setDoctorTitleName(String doctorTitleName) {
        this.doctorTitleName = doctorTitleName;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

	/*
	 * public String getDoctorOfficeName() { return doctorOfficeName; }
	 *
	 * public void setDoctorOfficeName(String doctorOfficeName) {
	 * this.doctorOfficeName = doctorOfficeName; }
	 */

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTicketFlag() {
        return ticketFlag;
    }

    public void setTicketFlag(String ticketFlag) {
        this.ticketFlag = ticketFlag;
    }

    public String getSetBindSinaState() {
        return setBindSinaState;
    }

    public void setSetBindSinaState(String setBindSinaState) {
        this.setBindSinaState = setBindSinaState;
    }

    public String getSetBindSessionCod() {
        return setBindSessionCod;
    }

    public void setSetBindSessionCod(String setBindSessionCod) {
        this.setBindSessionCod = setBindSessionCod;
    }

    public String getMicroBlogUID() {
        return microBlogUID;
    }

    public void setMicroBlogUID(String microBlogUID) {
        this.microBlogUID = microBlogUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(String gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDoctorBigPicture() {
        return doctorBigPicture;
    }

    public void setDoctorBigPicture(String doctorPicture) {
        this.doctorBigPicture = doctorPicture;
    }

    public String getDoctorClientPicture() {
        return doctorClientPicture;
    }

    public void setDoctorClientPicture(String doctorPicture) {
        this.doctorClientPicture = doctorPicture;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCultureLevel() {
        return cultureLevel.trim();
    }

    public void setCultureLevel(String cultureLevel) {
        this.cultureLevel = cultureLevel;
    }

    public String getSameExperience() {
        return sameExperience;
    }

    public void setSameExperience(String sameExperience) {
        this.sameExperience = sameExperience;
    }

	/*
	 * public int getDoctorOfficeCode() { return doctorOfficeCode; }
	 *
	 * public void setDoctorOfficeCode(int doctorOfficeCode) {
	 * this.doctorOfficeCode = doctorOfficeCode; }
	 */

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public String getInterestCode() {
        return interestCode;
    }

    public void setInterestCode(String interestCode) {
        this.interestCode = interestCode;
    }

    public int getSameExperienceCode() {
        return sameExperienceCode;
    }

    public void setSameExperienceCode(int sameExperienceCode) {
        this.sameExperienceCode = sameExperienceCode;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isFristAudit() {
        if (roldid == 666)
            return true;
        else
            return false;
    }

    public boolean isAudit() {
        if (roldid == 777)
            return true;
        else
            return false;
    }

    public String getMicroBlogTimeOut() {
        return microBlogTimeOut;
    }

    public void setMicroBlogTimeOut(String microBlogTimeOut) {
        this.microBlogTimeOut = microBlogTimeOut;
    }

    public boolean isShow() {// 是否是医生角色判断
        if (roldid == 888)
            return true;
        else
            return false;
    }

    public boolean isDoctor() {// 是否是医生
        if (isShow() || isAudit())
            return true;
        else
            return false;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special.trim();
    }

	/*
	 * public String getOffice() { return office; }
	 *
	 * public void setOffice(String office) { this.office = office.trim(); }
	 */

    /**
     * 关系状态码
     *
     * @return
     */
    public int getIsAttentionFriend() {
        return AttentionFriend;
    }

    /**
     * 是否关注  0 没有关系  2 黑名单    其它都是关注状态
     *
     * @return true 已有关系
     * false 没有关系
     */
    public boolean getIsAttention() {
        if (AttentionFriend != 0 && AttentionFriend != 2) return true;
        return false;
    }

    public void setIsAttentionFriend(int AttentionFriend) {
        this.AttentionFriend = AttentionFriend;
    }

    public String getXueli() {
        return cultureLevel;
    }

    public void setXueli(String xueli) {
        this.cultureLevel = xueli.trim();
    }

    public String getHunyin() {
        return hunyin;
    }

    public void setHunyin(String hunyin) {
        this.hunyin = hunyin.trim();
    }

    public String getMetier() {
        return metier;
    }

    public void setMetier(String metier) {
        this.metier = metier.trim();
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
    public String getCusMessag() {
        return super.getCusMessag();
    }

    @Override
    public void setCusMessag(String cusMessage) {
        super.setCusMessag(cusMessage);
    }

    @Override
    public String getName() {
        // return this.remarkName;
        if (this.remarkName.equals("")) {
            return super.getName();
        } else {
            return this.remarkName;
        }
    }

    public String getDoctorName() {
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
        // TODO Auto-generated method stub
        super.setBigHeadIcon(bigHeadIcon);
    }

    @Override
    public String getNormalHeadIcon() {
        // TODO Auto-generated method stub
        return super.getNormalHeadIcon();
    }

    @Override
    public void setNormalHeadIcon(String normalHeadIcon) {
		/*
		 * if (normalHeadIcon != null &&
		 * normalHeadIcon.equals("assets/customerIcons/系统默认头像女.png")) {
		 * normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS +
		 * ":///customerIcons/default_head_female.png"; } else if
		 * (normalHeadIcon != null &&
		 * normalHeadIcon.equals("assets/customerIcons/系统默认头像男.png")) {
		 * normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS +
		 * ":///customerIcons/default_head_mankind.png"; } else if
		 * (normalHeadIcon != null &&
		 * normalHeadIcon.equals("assets/customerIcons/s_zcmale.png")) {
		 * normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS +
		 * ":///customerIcons/default_head_mankind.png"; } else if
		 * (normalHeadIcon != null && normalHeadIcon
		 * .equals("assets/customerIcons/default_women.png")) { normalHeadIcon =
		 * ImageDownloader.PROTOCOL_FILE_ASSETS +
		 * ":///customerIcons/default_head_female.png"; }
		 */
        super.setNormalHeadIcon(normalHeadIcon);
    }

    public static String parseHeaderUrl(String normalHeadIcon) {
        if (normalHeadIcon != null
                && normalHeadIcon.equals("assets/customerIcons/系统默认头像女.png")) {
            normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS
                    + ":///customerIcons/default_head_female.png";
        } else if (normalHeadIcon != null
                && normalHeadIcon.equals("assets/customerIcons/系统默认头像男.png")) {
            normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS
                    + ":///customerIcons/default_head_mankind.png";
        } else if (normalHeadIcon != null
                && normalHeadIcon.equals("assets/customerIcons/s_zcmale.png")) {
            normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS
                    + ":///customerIcons/default_head_mankind.png";
        } else if (normalHeadIcon != null
                && normalHeadIcon
                .equals("assets/customerIcons/default_women.png")) {
            normalHeadIcon = ImageDownloader.PROTOCOL_FILE_ASSETS
                    + ":///customerIcons/default_head_female.png";
        }
        return normalHeadIcon;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    /**
     * 解析性别
     *
     * @param sex
     * @return
     */
    public static int parseSex(String sex) {
        if ("W".equalsIgnoreCase(sex)) {
            return 2;
        } else if ("M".equalsIgnoreCase(sex)) {
            return 1;
        } else if ("X".equalsIgnoreCase(sex)) {
            return 0;
        } else {
            int value = 0;
            try {
                value = Integer.valueOf(sex);
            } catch (NumberFormatException e) {
            }
            return value;
        }
    }

    public void setSex(String sex) {
        if ("W".equalsIgnoreCase(sex)) {
            sex = "2";
        } else if ("M".equalsIgnoreCase(sex)) {
            sex = "1";
        } else if ("X".equalsIgnoreCase(sex)) {
            sex = "0";
        } else {
            sex = sex;
        }
        this.sex = sex;
    }

    /**
     * 获取性别的文字
     *
     * @return
     */
    public String getSexText() {
        if (HStringUtil.isEmpty(sex)) {
            return "未知";
        } else if (sex.equals("2")) {
            return "女";
        } else if (sex.equals("1")) {
            return "男";
        } else if (sex.equals("0")) {
            return "未知";
        } else {
            return "未知";
        }

    }


    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDoctorunitAdd() {
        return doctorunitAdd;
    }

    public void setDoctorunitAdd(String doctorunitAdd) {
        this.doctorunitAdd = doctorunitAdd;
    }

    public String getDoctorunit() {
        return doctorunit;
    }

    public void setDoctorunit(String doctorunit) {
        this.doctorunit = doctorunit;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle.trim();
    }

    public String getCustomerlocus() {
        return customerlocus;
    }

    public void setCustomerlocus(String customerlocus) {
        this.customerlocus = customerlocus;
    }

    public String getDwellingplace() {
        return dwellingplace;
    }

    public void setDwellingplace(String dwellingplace) {
        this.dwellingplace = dwellingplace.trim();
    }

    public String getAccomplishedname() {
        return accomplishedname;
    }

    public void setAccomplishedname(String accomplishedname) {
        this.accomplishedname = accomplishedname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday.trim();
    }

    public String getPoneNumber() {
        return poneNumber;
    }

    public void setPoneNumber(String poneNumber) {
        this.poneNumber = poneNumber;
    }

    public String getLableJson() {
        return lableJson;
    }

    public void setLableJson(String attention) {
        this.lableJson = attention.trim();
    }

    public int getRoldid() {
        return roldid;
    }

    /**
     * 设置审核状态
     *
     * @param roldid
     */
    public void setRoldid(int roldid) {
        this.roldid = roldid;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }




    public String getAcademicJob() {
        return academicJob;
    }

    public void setAcademicJob(String academicJob) {
        this.academicJob = academicJob;
    }

    public String getResumeContent() {
        return resumeContent;
    }

    public void setResumeContent(String resumeContent) {
        this.resumeContent = resumeContent;
    }

    public String getDoctorMessage() {
        return doctorMessage;
    }

    public void setDoctorMessage(String doctorMessage) {
        this.doctorMessage = doctorMessage;
    }

    public String getDoctorPosition() {
        return doctorPosition;
    }

    public void setDoctorPosition(String doctorPosition) {
        this.doctorPosition = doctorPosition;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getTalkOnOff() {
        return talkOnOff;
    }

    public void setTalkOnOff(String takeOnOff) {
        this.talkOnOff = takeOnOff;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getAvToken() {
        return avToken;
    }

    public void setAvToken(String avToken) {
        this.avToken = avToken;
    }

    public String getSixOneAccoutn() {
        return sixOneAccoutn;
    }

    public void setSixOneAccoutn(String sixOneAccoutn) {
        this.sixOneAccoutn = sixOneAccoutn;
    }
}
