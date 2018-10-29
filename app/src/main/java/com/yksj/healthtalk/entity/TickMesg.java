package com.yksj.healthtalk.entity;

import java.io.Serializable;

import org.json.JSONObject;

public class TickMesg implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	"CUSTOMER_ID": 26285,
//    "ISBUY": "0",
//    "ISBUZY": "1",
//    "ISTALK": "0",
//    "ORDER_CURRENT": 3,
//    "ORDER_ON_OFF": 1,
//    "ORDER_ON_OFF_TIME": "20131212141049",
//    "REPEAT_BATCH": 1581,
//    "REPEAT_FLAG": 1,
//    "SERVICE_HOUR": 5,
//    "SERVICE_ITEM_DESC": "[\"2\",\"4\"]",
//    "SERVICE_ITEM_ID": 3859,
//    "SERVICE_MAX": 3,
//    "SERVICE_PRICE": 9,
//    "SERVICE_TIME_BEGIN": "20131227040200",
//    "SERVICE_TIME_END": "20131227090700",
//    "SERVICE_TYPE_ID": 2,
//    "SERVICE_TYPE_SUB_ID": 21,
//    "WEEK": 5
	String ISBUZY;
	String ISBUY;
	String ISTALK;
	String SERVICE_ITEM_ID	;
	String SERVICE_PRICE;
	String SERVICE_TYPE_SUB;
	String SERVICE_TYPE_SUB_ID;
	String ENJOY_CUSTOMER_ID;
	String EXTEND_FLAG;
	String ORDER_CLASS_ID;
	String ORDER_CREATE_TIME;
	String ORDER_ID;
	String ORDER_UPDATE_TIME;
	String PAY_ID;
	String SERVICE_ALREADY;
	String SERVICE_GOLD;
	String SERVICE_STATUS;
	String SERVICE_TYPE_ID;
	String ADVICE_CONTENT;
	String SERVICE_END;
	String SYSTEMTIME;
	String SERVICE_START;
	String SERVICE_CONTENT;
	
	
	
	
	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}
	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}
	public String getSERVICE_START() {
		return SERVICE_START;
	}
	public void setSERVICE_START(String sERVICE_START) {
		SERVICE_START = sERVICE_START;
	}
	public String getSYSTEMTIME() {
		return SYSTEMTIME;
	}
	public void setSYSTEMTIME(String sYSTEMTIME) {
		SYSTEMTIME = sYSTEMTIME;
	}
	public String getSERVICE_END() {
		return SERVICE_END;
	}
	public void setSERVICE_END(String sERVICE_END) {
		SERVICE_END = sERVICE_END;
	}
	public String getADVICE_CONTENT() {
		return ADVICE_CONTENT;
	}
	public void setADVICE_CONTENT(String aDVICE_CONTENT) {
		ADVICE_CONTENT = aDVICE_CONTENT;
	}
	public String getISBUZY() {
		return ISBUZY;
	}
	public void setISBUZY(String iSBUZY) {
		ISBUZY = iSBUZY;
	}
	public String getISBUY() {
		return ISBUY;
	}
	public void setISBUY(String iSBUY) {
		ISBUY = iSBUY;
	}
	public String getISTALK() {
		return ISTALK;
	}
	public void setISTALK(String iSTALK) {
		ISTALK = iSTALK;
	}
	public String getENJOY_CUSTOMER_ID() {
		return ENJOY_CUSTOMER_ID;
	}
	public void setENJOY_CUSTOMER_ID(String eNJOY_CUSTOMER_ID) {
		ENJOY_CUSTOMER_ID = eNJOY_CUSTOMER_ID;
	}
	public String getEXTEND_FLAG() {
		return EXTEND_FLAG;
	}
	public void setEXTEND_FLAG(String eXTEND_FLAG) {
		EXTEND_FLAG = eXTEND_FLAG;
	}
	public String getORDER_CLASS_ID() {
		return ORDER_CLASS_ID;
	}
	public void setORDER_CLASS_ID(String oRDER_CLASS_ID) {
		ORDER_CLASS_ID = oRDER_CLASS_ID;
	}
	public String getORDER_CREATE_TIME() {
		return ORDER_CREATE_TIME;
	}
	public void setORDER_CREATE_TIME(String oRDER_CREATE_TIME) {
		ORDER_CREATE_TIME = oRDER_CREATE_TIME;
	}
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public String getORDER_UPDATE_TIME() {
		return ORDER_UPDATE_TIME;
	}
	public void setORDER_UPDATE_TIME(String oRDER_UPDATE_TIME) {
		ORDER_UPDATE_TIME = oRDER_UPDATE_TIME;
	}
	public String getPAY_ID() {
		return PAY_ID;
	}
	public void setPAY_ID(String pAY_ID) {
		PAY_ID = pAY_ID;
	}
	public String getSERVICE_ALREADY() {
		return SERVICE_ALREADY;
	}
	public void setSERVICE_ALREADY(String sERVICE_ALREADY) {
		SERVICE_ALREADY = sERVICE_ALREADY;
	}
	public String getSERVICE_GOLD() {
		return SERVICE_GOLD;
	}
	public void setSERVICE_GOLD(String sERVICE_GOLD) {
		SERVICE_GOLD = sERVICE_GOLD;
	}
	public String getSERVICE_STATUS() {
		return SERVICE_STATUS;
	}
	public void setSERVICE_STATUS(String sERVICE_STATUS) {
		SERVICE_STATUS = sERVICE_STATUS;
	}
	public String getSERVICE_TYPE_ID() {
		return SERVICE_TYPE_ID;
	}
	public void setSERVICE_TYPE_ID(String sERVICE_TYPE_ID) {
		SERVICE_TYPE_ID = sERVICE_TYPE_ID;
	}
	public String getSERVICE_ITEM_ID() {
		return SERVICE_ITEM_ID;
	}
	public void setSERVICE_ITEM_ID(String sERVICE_ITEM_ID) {
		SERVICE_ITEM_ID = sERVICE_ITEM_ID;
	}
	public String getSERVICE_PRICE() {
		return SERVICE_PRICE;
	}
	public void setSERVICE_PRICE(String sERVICE_PRICE) {
		SERVICE_PRICE = sERVICE_PRICE;
	}
	public String getSERVICE_TYPE_SUB() {
		return SERVICE_TYPE_SUB;
	}
	public void setSERVICE_TYPE_SUB(String sERVICE_TYPE_SUB) {
		SERVICE_TYPE_SUB = sERVICE_TYPE_SUB;
	}
	public String getSERVICE_TYPE_SUB_ID() {
		return SERVICE_TYPE_SUB_ID;
	}
	public void setSERVICE_TYPE_SUB_ID(String sERVICE_TYPE_SUB_ID) {
		SERVICE_TYPE_SUB_ID = sERVICE_TYPE_SUB_ID;
	}
}
