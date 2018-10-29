package com.yksj.healthtalk.entity;

/**
 * 话题收入详细信息
 * @author Administrator
 *
 */
public class SalonIncomeDetailEntity {
	
//    {
//        "changeNum": "0",
//        "changeTime": "20120316174111",
//        "changeType": "",
//        "customerId": "9507",
//        "goldAfter": "",
//        "goldBefore": "",
//        "typeDetail": "102160"
//    },
	
	private String changeNum;//金额
	private String changeTime;//时间
	private String changeType;
	private String customerId;
	private String goldAfter;
	private String goldBefore;
	private String typeDetail;//详细情况
//	102110 话题日票赚取
//	102120	 话题月票赚取
//	102130	 共享视频赚取
//	102140	 共享图片赚取
//	102150	 共享文件赚取
//	102160	 共享音频赚取
	public String getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(String changeNum) {
		this.changeNum = changeNum;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getGoldAfter() {
		return goldAfter;
	}
	public void setGoldAfter(String goldAfter) {
		this.goldAfter = goldAfter;
	}
	public String getGoldBefore() {
		return goldBefore;
	}
	public void setGoldBefore(String goldBefore) {
		this.goldBefore = goldBefore;
	}
	public String getTypeDetail() {
		return typeDetail;
	}
	public void setTypeDetail(String typeDetail) {
		this.typeDetail = typeDetail;
	}
	

}
