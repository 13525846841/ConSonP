package com.yksj.healthtalk.entity;

import java.io.Serializable;

import org.json.JSONObject;

import com.yksj.healthtalk.utils.StringFormatUtils;
/**
 * 订单详情
 * @author Administrator
 *
 */
public class DoctorOrderDeatils implements Serializable {
	private String MERCHANT_ID;// 提供服务方商户ID
	private String ORDER_ID;// --服务单ID
	private String SERVICE_CUSTOMER_ID;// 提供服务方客户ID  医生id
	private String ENJOY_CUSTOMER_ID;// --接受服务方客户ID  患者id
	private String ORDER_CLASS_ID;// --服务类型ID，暂写死，挂号服务(10-挂号服务)
	private String SERVICE_GOLD;// --服务收取多美币数量
	private String SERVICE_START;// --服务开始时间
	private String SERVICE_END;// --服务结束时间
	private String SERVICE_STATUS;// --服务状态
	private String SERVICE_FEEDBACK;// --客户反馈服务结果
	private String FEEDBACK_TIME;// --服务反馈时间
	private String NOTE;// --当医生为服务中心服务人员时，记录‘此收入已转入服务中心账户’
	private String ORDER_UPDATE_TIME;// --订单状态变更时间
	private String PAY_OK_TIME;// --支付成功时间/服务开始时间
	private String PAY_TYPE_ID;// --支付类型：--10-支付宝--20-银行类型--30-财富通
	private String PAY_ID;// 支付订单ID(六一健康为支付宝生成的15位随机订单号)
	private String PAY_ACCOUNT;// --支付帐号
	private String ADVICE_CONTENT;// --患者咨询内容
	private String PATIENT_NAME;// --患者姓名
	private String PATIENT_PHONE;// --患者联系方式
	private String SERVICE_PLACE;// --服务地点"
	private String SERVICE_ITEM_DESC;// 医生说明
	private String SERVICE_TYPE;// 医生服务类型名称
	private String ORDER_CREATE_TIME;// --订单生成时间，60分钟后如仍未支付，则作废。
	private String TRANSACTION_ID;// --支付宝交易号
	private String SERVICE_ALREADY;// --医生是否已经为客户服务标记(Y-是，N-否)
	private String TRANSFER_GETTELE;// --退款时收款人联系电话
	private String SERVICE_ITEM_ID;// --医生服务项目ID
	private String SERVICE_TYPE_ID;// --医生服务类型ID
	private String SERVICE_TYPE_SUB_ID;// --医生服务类型细分ID
	private String EXTEND_FLAG;// --是否已经延时服务标记(0-未延时，1-已延时，2-可停止服务)
	private String BACK_REASON;// --是否已经延时服务标记(0-未延时，1-已延时，2-可停止服务)
	private String BACK_ORDER_ID;// --是否已经延时服务标记(0-未延时，1-已延时，2-可停止服务)
	private String BACKPICLIST;//
	private String SYSTEMTIME;//系统时间
	private String SERVICE_TYPE_SUB;//服务类型显示
	private String SERVICE_CONTENT;//所享受服务缩略字

	
	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}

	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}

	public String getSERVICE_TYPE_SUB() {
		return SERVICE_TYPE_SUB;
	}

	public void setSERVICE_TYPE_SUB(String sERVICE_TYPE_SUB) {
		SERVICE_TYPE_SUB = sERVICE_TYPE_SUB;
	}

	public String getSYSTEMTIME() {
		return SYSTEMTIME;
	}

	public void setSYSTEMTIME(String sYSTEMTIME) {
		SYSTEMTIME = sYSTEMTIME;
	}

	public String getBACK_REASON() {
		return BACK_REASON;
	}

	public void setBACK_REASON(String bACK_REASON) {
		BACK_REASON = bACK_REASON;
	}

	public String getBACK_ORDER_ID() {
		return BACK_ORDER_ID;
	}

	public void setBACK_ORDER_ID(String bACK_ORDER_ID) {
		BACK_ORDER_ID = bACK_ORDER_ID;
	}

	public String getBACKPICLIST() {
		return BACKPICLIST;
	}

	public void setBACKPICLIST(String bACKPICLIST) {
		BACKPICLIST = bACKPICLIST;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}

	public String getORDER_ID() {
		return ORDER_ID;
	}

	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}

	public String getSERVICE_CUSTOMER_ID() {
		return SERVICE_CUSTOMER_ID;
	}

	public void setSERVICE_CUSTOMER_ID(String sERVICE_CUSTOMER_ID) {
		SERVICE_CUSTOMER_ID = sERVICE_CUSTOMER_ID;
	}

	public String getENJOY_CUSTOMER_ID() {
		return ENJOY_CUSTOMER_ID;
	}

	public void setENJOY_CUSTOMER_ID(String eNJOY_CUSTOMER_ID) {
		ENJOY_CUSTOMER_ID = eNJOY_CUSTOMER_ID;
	}

	public String getORDER_CLASS_ID() {
		return ORDER_CLASS_ID;
	}

	public void setORDER_CLASS_ID(String oRDER_CLASS_ID) {
		ORDER_CLASS_ID = oRDER_CLASS_ID;
	}

	public String getSERVICE_GOLD() {
		return SERVICE_GOLD;
	}

	public void setSERVICE_GOLD(String sERVICE_GOLD) {
		SERVICE_GOLD = sERVICE_GOLD;
	}

	public String getSERVICE_START() {
		return SERVICE_START;
	}

	public void setSERVICE_START(String sERVICE_START) {
		SERVICE_START = sERVICE_START;
	}

	public String getSERVICE_END() {
		return SERVICE_END;
	}

	public void setSERVICE_END(String sERVICE_END) {
		SERVICE_END = sERVICE_END;
	}

	public String getSERVICE_STATUS() {
		return SERVICE_STATUS;
	}

	public void setSERVICE_STATUS(String sERVICE_STATUS) {
		SERVICE_STATUS = sERVICE_STATUS;
	}

	public String getSERVICE_FEEDBACK() {
		return SERVICE_FEEDBACK;
	}

	public void setSERVICE_FEEDBACK(String sERVICE_FEEDBACK) {
		SERVICE_FEEDBACK = sERVICE_FEEDBACK;
	}

	public String getFEEDBACK_TIME() {
		return FEEDBACK_TIME;
	}

	public void setFEEDBACK_TIME(String fEEDBACK_TIME) {
		FEEDBACK_TIME = fEEDBACK_TIME;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getORDER_UPDATE_TIME() {
		return ORDER_UPDATE_TIME;
	}

	public void setORDER_UPDATE_TIME(String oRDER_UPDATE_TIME) {
		ORDER_UPDATE_TIME = oRDER_UPDATE_TIME;
	}

	public String getPAY_OK_TIME() {
		return PAY_OK_TIME;
	}

	public void setPAY_OK_TIME(String pAY_OK_TIME) {
		PAY_OK_TIME = pAY_OK_TIME;
	}

	public String getPAY_TYPE_ID() {
		return PAY_TYPE_ID;
	}

	public void setPAY_TYPE_ID(String pAY_TYPE_ID) {
		PAY_TYPE_ID = pAY_TYPE_ID;
	}

	public String getPAY_ID() {
		return PAY_ID;
	}

	public void setPAY_ID(String pAY_ID) {
		PAY_ID = pAY_ID;
	}

	public String getPAY_ACCOUNT() {
		return PAY_ACCOUNT;
	}

	public void setPAY_ACCOUNT(String pAY_ACCOUNT) {
		PAY_ACCOUNT = pAY_ACCOUNT;
	}

	public String getADVICE_CONTENT() {
		return ADVICE_CONTENT;
	}

	public void setADVICE_CONTENT(String aDVICE_CONTENT) {
		ADVICE_CONTENT = aDVICE_CONTENT;
	}

	public String getPATIENT_NAME() {
		return PATIENT_NAME;
	}

	public void setPATIENT_NAME(String pATIENT_NAME) {
		PATIENT_NAME = pATIENT_NAME;
	}

	public String getPATIENT_PHONE() {
		return PATIENT_PHONE;
	}

	public void setPATIENT_PHONE(String pATIENT_PHONE) {
		PATIENT_PHONE = pATIENT_PHONE;
	}

	public String getSERVICE_PLACE() {
		return SERVICE_PLACE;
	}

	public void setSERVICE_PLACE(String sERVICE_PLACE) {
		SERVICE_PLACE = sERVICE_PLACE;
	}

	public String getSERVICE_ITEM_DESC() {
		return SERVICE_ITEM_DESC;
	}

	public void setSERVICE_ITEM_DESC(String sERVICE_ITEM_DESC) {
		SERVICE_ITEM_DESC = sERVICE_ITEM_DESC;
	}

	public String getSERVICE_TYPE() {
		return SERVICE_TYPE;
	}

	public void setSERVICE_TYPE(String sERVICE_TYPE) {
		SERVICE_TYPE = sERVICE_TYPE;
	}

	public String getORDER_CREATE_TIME() {
		return ORDER_CREATE_TIME;
	}

	public void setORDER_CREATE_TIME(String oRDER_CREATE_TIME) {
		ORDER_CREATE_TIME = oRDER_CREATE_TIME;
	}

	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}

	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}

	public String getSERVICE_ALREADY() {
		return SERVICE_ALREADY;
	}

	public void setSERVICE_ALREADY(String sERVICE_ALREADY) {
		SERVICE_ALREADY = sERVICE_ALREADY;
	}

	public String getTRANSFER_GETTELE() {
		return TRANSFER_GETTELE;
	}

	public void setTRANSFER_GETTELE(String tRANSFER_GETTELE) {
		TRANSFER_GETTELE = tRANSFER_GETTELE;
	}

	public String getSERVICE_ITEM_ID() {
		return SERVICE_ITEM_ID;
	}

	public void setSERVICE_ITEM_ID(String sERVICE_ITEM_ID) {
		SERVICE_ITEM_ID = sERVICE_ITEM_ID;
	}

	public String getSERVICE_TYPE_ID() {
		return SERVICE_TYPE_ID;
	}

	public void setSERVICE_TYPE_ID(String sERVICE_TYPE_ID) {
		SERVICE_TYPE_ID = sERVICE_TYPE_ID;
	}

	public String getSERVICE_TYPE_SUB_ID() {
		return SERVICE_TYPE_SUB_ID;
	}

	public void setSERVICE_TYPE_SUB_ID(String sERVICE_TYPE_SUB_ID) {
		SERVICE_TYPE_SUB_ID = sERVICE_TYPE_SUB_ID;
	}

	public String getEXTEND_FLAG() {
		return EXTEND_FLAG;
	}

	public void setEXTEND_FLAG(String eXTEND_FLAG) {
		EXTEND_FLAG = eXTEND_FLAG;
	}

	public DoctorOrderDeatils() {
		super();
	}

	public DoctorOrderDeatils(String mERCHANT_ID, String oRDER_ID,
			String sERVICE_CUSTOMER_ID, String eNJOY_CUSTOMER_ID,
			String oRDER_CLASS_ID, String sERVICE_GOLD, String sERVICE_START,
			String sERVICE_END, String sERVICE_STATUS, String sERVICE_FEEDBACK,
			String fEEDBACK_TIME, String nOTE, String oRDER_UPDATE_TIME,
			String pAY_OK_TIME, String pAY_TYPE_ID, String pAY_ID,
			String pAY_ACCOUNT, String aDVICE_CONTENT, String pATIENT_NAME,
			String pATIENT_PHONE, String sERVICE_PLACE,
			String sERVICE_ITEM_DESC, String sERVICE_TYPE,
			String oRDER_CREATE_TIME, String tRANSACTION_ID,
			String sERVICE_ALREADY, String tRANSFER_GETTELE,
			String sERVICE_ITEM_ID, String sERVICE_TYPE_ID,
			String sERVICE_TYPE_SUB_ID, String eXTEND_FLAG, String bACK_REASON,
			String bACK_ORDER_ID, String bACKPICLIST,String SYSTEMTIME,String SERVICE_TYPE_SUB,String SERVICE_CONTENT) {
		super();
		this.MERCHANT_ID = mERCHANT_ID;
		this.ORDER_ID = oRDER_ID;
		this.SERVICE_CUSTOMER_ID = sERVICE_CUSTOMER_ID;
		this.ENJOY_CUSTOMER_ID = eNJOY_CUSTOMER_ID;
		this.ORDER_CLASS_ID = oRDER_CLASS_ID;
		this.SERVICE_GOLD = sERVICE_GOLD;
		this.SERVICE_START = sERVICE_START;
		this.SERVICE_END = sERVICE_END;
		this.SERVICE_STATUS = sERVICE_STATUS;
		this.SERVICE_FEEDBACK = sERVICE_FEEDBACK;
		this.FEEDBACK_TIME = fEEDBACK_TIME;
		this.NOTE = nOTE;
		this.ORDER_UPDATE_TIME = oRDER_UPDATE_TIME;
		this.PAY_OK_TIME = pAY_OK_TIME;
		this.PAY_TYPE_ID = pAY_TYPE_ID;
		this.PAY_ID = pAY_ID;
		this.PAY_ACCOUNT = pAY_ACCOUNT;
		this.ADVICE_CONTENT = aDVICE_CONTENT;
		this.PATIENT_NAME = pATIENT_NAME;
		this.PATIENT_PHONE = pATIENT_PHONE;
		this.SERVICE_PLACE = sERVICE_PLACE;
		this.SERVICE_ITEM_DESC = sERVICE_ITEM_DESC;
		this.SERVICE_TYPE = sERVICE_TYPE;
		this.ORDER_CREATE_TIME = oRDER_CREATE_TIME;
		this.TRANSACTION_ID = tRANSACTION_ID;
		this.SERVICE_ALREADY = sERVICE_ALREADY;
		this.TRANSFER_GETTELE = tRANSFER_GETTELE;
		this.SERVICE_ITEM_ID = sERVICE_ITEM_ID;
		this.SERVICE_TYPE_ID = sERVICE_TYPE_ID;
		this.SERVICE_TYPE_SUB_ID = sERVICE_TYPE_SUB_ID;
		this.EXTEND_FLAG = eXTEND_FLAG;
		this.BACK_REASON = bACK_REASON;
		this.BACK_ORDER_ID = bACK_ORDER_ID;
		this.BACKPICLIST = bACKPICLIST;
		this.SYSTEMTIME=SYSTEMTIME;
		this.SERVICE_TYPE_SUB=SERVICE_TYPE_SUB;
		this.SERVICE_CONTENT=SERVICE_CONTENT;
	}


	public static DoctorOrderDeatils parsToEntity(String content){
		try {
			JSONObject obj=new JSONObject(content);
			DoctorOrderDeatils deatils=new DoctorOrderDeatils(
				StringFormatUtils.ObjectToString(obj.optString("MERCHANT_ID")),
				StringFormatUtils.ObjectToString(obj.optString("ORDER_ID")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_CUSTOMER_ID")),
				StringFormatUtils.ObjectToString(obj.optString("ENJOY_CUSTOMER_ID")),
				StringFormatUtils.ObjectToString(obj.optString("ORDER_CLASS_ID")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_GOLD")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_START")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_END")), 
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_STATUS")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_FEEDBACK")),
				StringFormatUtils.ObjectToString(obj.optString("FEEDBACK_TIME")),
				StringFormatUtils.ObjectToString(obj.optString("NOTE")), 
				StringFormatUtils.ObjectToString(obj.optString("ORDER_UPDATE_TIME")),
				StringFormatUtils.ObjectToString(obj.optString("PAY_OK_TIME")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_START")),
				StringFormatUtils.ObjectToString(obj.optString("PAY_ID")), 
				StringFormatUtils.ObjectToString(obj.optString("PAY_ACCOUNT")),//
				StringFormatUtils.ObjectToString(obj.optString("ADVICE_CONTENT")),
				StringFormatUtils.ObjectToString(obj.optString("PATIENT_NAME")),
				StringFormatUtils.ObjectToString(obj.optString("PATIENT_PHONE")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_PLACE")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_ITEM_DESC")),
			    StringFormatUtils.ObjectToString(obj.optString("SERVICE_TYPE")),
			    StringFormatUtils.ObjectToString(obj.optString("ORDER_CREATE_TIME")),
				StringFormatUtils.ObjectToString(obj.optString("TRANSACTION_ID")) ,
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_ALREADY")),
				StringFormatUtils.ObjectToString(obj.optString("TRANSFER_GETTELE")) ,
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_ITEM_ID")) ,
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_TYPE_ID")) ,
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_TYPE_SUB_ID")),
				StringFormatUtils.ObjectToString(obj.optString("EXTEND_FLAG","0")),
				StringFormatUtils.ObjectToString(obj.optString("BACK_REASON")),
				StringFormatUtils.ObjectToString(obj.optString("BACK_ORDER_ID")),
				StringFormatUtils.ObjectToString(obj.optString("BACKPICLIST")),
				StringFormatUtils.ObjectToString(obj.optString("SYSTEMTIME")),
				StringFormatUtils.ObjectToString(obj.optString("SERVICE_TYPE_SUB")),
			    StringFormatUtils.ObjectToString(obj.optString("SERVICE_CONTENT")));
			return deatils;
		} catch (Exception e) {
			return null;
		}
	}
}
