package com.yksj.healthtalk.entity;


/**
 *服务内容
 * @author Administrator
 *
 */
public class ServiceContent {
	
//	{
//        "SERVICE_CONTENT": "24小时内预约咨询",
//        "SERVICE_CONTENT_ID": 1,
//        "SERVICE_DESC": "暂无",
//        "SERVICE_SEQ": 1
//    }
	
	String SERVICE_CONTENT;
	String SERVICE_CONTENT_ID;
	String SERVICE_DESC;
	String SERVICE_SEQ;
	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}
	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}
	public String getSERVICE_CONTENT_ID() {
		return SERVICE_CONTENT_ID;
	}
	public void setSERVICE_CONTENT_ID(String sERVICE_CONTENT_ID) {
		SERVICE_CONTENT_ID = sERVICE_CONTENT_ID;
	}
	public String getSERVICE_DESC() {
		return SERVICE_DESC;
	}
	public void setSERVICE_DESC(String sERVICE_DESC) {
		SERVICE_DESC = sERVICE_DESC;
	}
	public String getSERVICE_SEQ() {
		return SERVICE_SEQ;
	}
	public void setSERVICE_SEQ(String sERVICE_SEQ) {
		SERVICE_SEQ = sERVICE_SEQ;
	}
	
	

}
