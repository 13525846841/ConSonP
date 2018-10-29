package com.yksj.healthtalk.entity;

public class DynamicMessageListEntity {
	private int r;//
	private int infoId;// 咨询id
	private String infoName;// 咨询标题
	private String publishTime;// 发布时间
	private String infoPicture;// 标题图片
	private String statusTime;// 状态改变时间
	private String infoStaus;// 当前状态（10-已提交，未审核，20-审核通过，30-审核失败，40-已删除）
	private int customerId;// 发布者I
	private int consultationCenterId;// 六一健康id
	private int colorchage;

	public int getColorchage() {
		return colorchage;
	}

	public void setColorchage(int colorchage) {
		this.colorchage = colorchage;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getInfoPicture() {
		return infoPicture;
	}

	public void setInfoPicture(String infoPicture) {
		this.infoPicture = infoPicture;
	}

	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	public String getInfoStaus() {
		return infoStaus;
	}

	public void setInfoStaus(String infoStaus) {
		this.infoStaus = infoStaus;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getConsultationCenterId() {
		return consultationCenterId;
	}

	public void setConsultationCenterId(int consultationCenterId) {
		this.consultationCenterId = consultationCenterId;
	}

	@Override
	public String toString() {
		return "DynamicMessageListEntity [r=" + r + ", infoId=" + infoId + ", infoName=" + infoName + ", publishTime="
				+ publishTime + ", infoPicture=" + infoPicture + ", statusTime=" + statusTime + ", infoStaus="
				+ infoStaus + ", customerId=" + customerId + ", consultationCenterId=" + consultationCenterId + "]";
	}

}
