/**
 * 
 */
package com.yksj.healthtalk.entity;

/**
 * @author zheng
 *
 */
public class ConsultationServiceEntity {

	private String consultationId;//会诊id
	private String consultationCenterId;//CONSULTATION_CENTER_ID六一健康id
	private String expertId;//EXPERT_ID
	private String customerNickName;
	private String consultationName;
	private String ConsultationState;
	private String ExpertName;
	private String applytime;
	private String SExpertHeader;
	private String BExpertHeader;
	private String serviceStatusName;//SERVICE_STATUS_NAME	//列表项前标记
	private String serviceOperation;//SERVICE_OPERATION	//列表项后标记
	private String consultationCentent;
	private String isTalk;//完成后是否能聊天
	private String createDoctorIdName;//医生姓名
	private String createDoctorId;//医生Id
	private String patientName;//患者姓名
	private String patientId;//患者Id

	public String getIsTalk() {
		return isTalk;
	}

	public void setIsTalk(String isTalk) {
		this.isTalk = isTalk;
	}

	public String getConsultationId() {
		return consultationId;
	}
	public void setConsultationId(String consultationId) {
		this.consultationId = consultationId;
	}
	public String getConsultationCenterId() {
		return consultationCenterId;
	}
	public void setConsultationCenterId(String consultationCenterId) {
		this.consultationCenterId = consultationCenterId;
	}
	public String getExpertId() {
		return expertId;
	}
	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}
	public String getConsultationName() {
		return consultationName;
	}
	public void setConsultationName(String consultationName) {
		this.consultationName = consultationName;
	}
	public String getConsultationState() {
		return ConsultationState;
	}
	public void setConsultationState(String consultationState) {
		ConsultationState = consultationState;
	}
	public String getExpertName() {
		return ExpertName;
	}
	public void setExpertName(String expertName) {
		ExpertName = expertName;
	}
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
	public String getSExpertHeader() {
		return SExpertHeader;
	}
	public void setSExpertHeader(String sExpertHeader) {
		SExpertHeader = sExpertHeader;
	}
	public String getBExpertHeader() {
		return BExpertHeader;
	}
	public void setBExpertHeader(String bExpertHeader) {
		BExpertHeader = bExpertHeader;
	}
	public String getServiceStatusName() {
		return serviceStatusName;
	}
	public void setServiceStatusName(String serviceStatusName) {
		this.serviceStatusName = serviceStatusName;
	}
	public String getServiceOperation() {
		return serviceOperation;
	}
	public void setServiceOperation(String serviceOperation) {
		this.serviceOperation = serviceOperation;
	}
	public String getConsultationCentent() {
		return consultationCentent;
	}
	public void setConsultationCentent(String consultationCentent) {
		this.consultationCentent = consultationCentent;
	}
	public String getCustomerNickName() {
		return customerNickName;
	}
	public void setCustomerNickName(String customerNickName) {
		this.customerNickName = customerNickName;
	}

	public String getCreateDoctorIdName() {
		return createDoctorIdName;
	}

	public void setCreateDoctorIdName(String createDoctorIdName) {
		this.createDoctorIdName = createDoctorIdName;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getCreateDoctorId() {
		return createDoctorId;
	}

	public void setCreateDoctorId(String createDoctorId) {
		this.createDoctorId = createDoctorId;
	}

	@Override
	public String toString() {
		return "ConsultationServiceEntity{" +
				"consultationId='" + consultationId + '\'' +
				", consultationCenterId='" + consultationCenterId + '\'' +
				", expertId='" + expertId + '\'' +
				", customerNickName='" + customerNickName + '\'' +
				", consultationName='" + consultationName + '\'' +
				", ConsultationState='" + ConsultationState + '\'' +
				", ExpertName='" + ExpertName + '\'' +
				", applytime='" + applytime + '\'' +
				", SExpertHeader='" + SExpertHeader + '\'' +
				", BExpertHeader='" + BExpertHeader + '\'' +
				", serviceStatusName='" + serviceStatusName + '\'' +
				", serviceOperation='" + serviceOperation + '\'' +
				", consultationCentent='" + consultationCentent + '\'' +
				", isTalk='" + isTalk + '\'' +
				", createDoctorIdName='" + createDoctorIdName + '\'' +
				", createDoctorId='" + createDoctorId + '\'' +
				", patientName='" + patientName + '\'' +
				", patientId='" + patientId + '\'' +
				'}';
	}
}
