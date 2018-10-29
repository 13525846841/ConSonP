package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
/**患者实体
 * @author Administrator
 *
 */
public class DoctorPatient implements Serializable {
	private String backGold;
	private String bigIconBackground;
	private String clientIconBackground;
	private String customerAccounts;
	private String customerGold;
	private String customerGroupRel;
	private String customerId;
	private String customerLocus;
	private String customerNickname;
	private String customerSex;
	private String distance;
	private String doctorHospital;
	private String doctorOffice;
	private String doctorSpecially;
	private String doctorTitle;
	private String dwellingPlace;
	private String flag;
	private String hasExtensionInfomaiton;
	private String infoVersion;
	private String orderId;
	private String orderInfo;
	private String orderOnOff;
    private String personalNarrate;
	private String phoneNum;
	private String phonefriendName;
	private String relationType;
	private String remarksName;
	private String reqCount;
	private String roleId;
	private String serviceEnd;
	private String serviceFlag;
	private String servicePrice;
	private String servicePriceDefine;
	private String serviceStart;
	private String serviceStatus;
	private String serviceStatusCode;
	private String serviceTypeName;
	private String ticketFlag;
	private String serviceTypeId;
	
	public String getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public String getBackGold() {
		return backGold;
	}
	public void setBackGold(String backGold) {
		this.backGold = backGold;
	}
	public String getBigIconBackground() {
		return bigIconBackground;
	}
	public void setBigIconBackground(String bigIconBackground) {
		this.bigIconBackground = bigIconBackground;
	}
	public String getClientIconBackground() {
		return clientIconBackground;
	}
	public void setClientIconBackground(String clientIconBackground) {
		this.clientIconBackground = clientIconBackground;
	}
	public String getCustomerAccounts() {
		return customerAccounts;
	}
	public void setCustomerAccounts(String customerAccounts) {
		this.customerAccounts = customerAccounts;
	}
	public String getCustomerGold() {
		return customerGold;
	}
	public void setCustomerGold(String customerGold) {
		this.customerGold = customerGold;
	}
	public String getCustomerGroupRel() {
		return customerGroupRel;
	}
	public void setCustomerGroupRel(String customerGroupRel) {
		this.customerGroupRel = customerGroupRel;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerLocus() {
		return customerLocus;
	}
	public void setCustomerLocus(String customerLocus) {
		this.customerLocus = customerLocus;
	}
	public String getCustomerNickname() {
		if(remarksName!=null&&(!"".equals(remarksName))){
			return	remarksName;
		}else
		return customerNickname;
	}
	public void setCustomerNickname(String customerNickname) {
		this.customerNickname = customerNickname;
	}
	public String getCustomerSex() {
		return customerSex;
	}
	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDoctorHospital() {
		return doctorHospital;
	}
	public void setDoctorHospital(String doctorHospital) {
		this.doctorHospital = doctorHospital;
	}
	public String getDoctorOffice() {
		return doctorOffice;
	}
	public void setDoctorOffice(String doctorOffice) {
		this.doctorOffice = doctorOffice;
	}
	public String getDoctorSpecially() {
		return doctorSpecially;
	}
	public void setDoctorSpecially(String doctorSpecially) {
		this.doctorSpecially = doctorSpecially;
	}
	public String getDoctorTitle() {
		return doctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
	}
	public String getDwellingPlace() {
		return dwellingPlace;
	}
	public void setDwellingPlace(String dwellingPlace) {
		this.dwellingPlace = dwellingPlace;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getHasExtensionInfomaiton() {
		return hasExtensionInfomaiton;
	}
	public void setHasExtensionInfomaiton(String hasExtensionInfomaiton) {
		this.hasExtensionInfomaiton = hasExtensionInfomaiton;
	}
	public String getInfoVersion() {
		return infoVersion;
	}
	public void setInfoVersion(String infoVersion) {
		this.infoVersion = infoVersion;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}
	public String getOrderOnOff() {
		return orderOnOff;
	}
	public void setOrderOnOff(String orderOnOff) {
		this.orderOnOff = orderOnOff;
	}
	public String getPersonalNarrate() {
		return personalNarrate;
	}
	public void setPersonalNarrate(String personalNarrate) {
		this.personalNarrate = personalNarrate;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPhonefriendName() {
		return phonefriendName;
	}
	public void setPhonefriendName(String phonefriendName) {
		this.phonefriendName = phonefriendName;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getRemarksName() {
		return remarksName;
	}
	public void setRemarksName(String remarksName) {
		this.remarksName = remarksName;
	}
	public String getReqCount() {
		return reqCount;
	}
	public void setReqCount(String reqCount) {
		this.reqCount = reqCount;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getServiceEnd() {
		return serviceEnd;
	}
	public void setServiceEnd(String serviceEnd) {
		this.serviceEnd = serviceEnd;
	}
	public String getServiceFlag() {
		return serviceFlag;
	}
	public void setServiceFlag(String serviceFlag) {
		this.serviceFlag = serviceFlag;
	}
	public String getServicePrice() {
		return servicePrice;
	}
	public void setServicePrice(String servicePrice) {
		this.servicePrice = servicePrice;
	}
	public String getServicePriceDefine() {
		return servicePriceDefine;
	}
	public void setServicePriceDefine(String servicePriceDefine) {
		this.servicePriceDefine = servicePriceDefine;
	}
	public String getServiceStart() {
		return serviceStart;
	}
	public void setServiceStart(String serviceStart) {
		this.serviceStart = serviceStart;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getServiceStatusCode() {
		return serviceStatusCode;
	}
	public void setServiceStatusCode(String serviceStatusCode) {
		this.serviceStatusCode = serviceStatusCode;
	}
	public String getServiceTypeName() {
		return serviceTypeName;
	}
	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}
	public String getTicketFlag() {
		return ticketFlag;
	}
	public void setTicketFlag(String ticketFlag) {
		this.ticketFlag = ticketFlag;
	}
	
	public DoctorPatient(String backGold, String bigIconBackground,
			String clientIconBackground, String customerAccounts,
			String customerGold, String customerGroupRel, String customerId,
			String customerLocus, String customerNickname, String customerSex,
			String distance, String doctorHospital, String doctorOffice,
			String doctorSpecially, String doctorTitle, String dwellingPlace,
			String flag, String hasExtensionInfomaiton, String infoVersion,
			String orderId, String orderInfo, String orderOnOff,
			String personalNarrate, String phoneNum, String phonefriendName,
			String relationType, String remarksName, String reqCount,
			String roleId, String serviceEnd, String serviceFlag,
			String servicePrice, String servicePriceDefine,
			String serviceStart, String serviceStatus,
			String serviceStatusCode, String serviceTypeName, String ticketFlag,String serviceTypeId) {
		super();
		this.backGold = backGold;
		this.bigIconBackground = bigIconBackground;
		this.clientIconBackground = clientIconBackground;
		this.customerAccounts = customerAccounts;
		this.customerGold = customerGold;
		this.customerGroupRel = customerGroupRel;
		this.customerId = customerId;
		this.customerLocus = customerLocus;
		this.customerNickname = customerNickname;
		this.customerSex = customerSex;
		this.distance = distance;
		this.doctorHospital = doctorHospital;
		this.doctorOffice = doctorOffice;
		this.doctorSpecially = doctorSpecially;
		this.doctorTitle = doctorTitle;
		this.dwellingPlace = dwellingPlace;
		this.flag = flag;
		this.hasExtensionInfomaiton = hasExtensionInfomaiton;
		this.infoVersion = infoVersion;
		this.orderId = orderId;
		this.orderInfo = orderInfo;
		this.orderOnOff = orderOnOff;
		this.personalNarrate = personalNarrate;
		this.phoneNum = phoneNum;
		this.phonefriendName = phonefriendName;
		this.relationType = relationType;
		this.remarksName = remarksName;
		this.reqCount = reqCount;
		this.roleId = roleId;
		this.serviceEnd = serviceEnd;
		this.serviceFlag = serviceFlag;
		this.servicePrice = servicePrice;
		this.servicePriceDefine = servicePriceDefine;
		this.serviceStart = serviceStart;
		this.serviceStatus = serviceStatus;
		this.serviceStatusCode = serviceStatusCode;
		this.serviceTypeName = serviceTypeName;
		this.ticketFlag = ticketFlag;
		this.serviceTypeId=serviceTypeId;
	}
	
	public DoctorPatient() {
		super();
	}
	public static List<DoctorPatient> parseToList(String content){
		List<DoctorPatient> mlist = new ArrayList<DoctorPatient>();
		try {
			JSONArray array=new JSONArray(content);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object=(JSONObject) array.get(i);
				DoctorPatient patient=new DoctorPatient(object.optString("backGold"),
				object.optString("bigIconBackground"),object.optString("clientIconBackground"),object.optString("customerAccounts"),
				object.optString("customerGold"),object.optString("customerGroupRel"),object.optString("customerId"),
				object.optString("customerLocus"),object.optString("customerNickname"),object.optString("customerSex"),
				object.optString("distance"),object.optString("doctorHospital"),object.optString("doctorOffice"),
				object.optString("doctorSpecially"),object.optString("doctorTitle"),object.optString("dwellingPlace"),
				object.optString("flag"),object.optString("hasExtensionInfomaiton"),object.optString("infoVersion"),
				object.optString("orderId"),object.optString("orderInfo"),object.optString("orderOnOff"),object.optString("personalNarrate"),object.optString("phoneNum")
				,object.optString("phonefriendName"),object.optString("relationType"),object.optString("remarksName"),object.optString("reqCount")
				,object.optString("roleId"),object.optString("serviceEnd"),object.optString("serviceFlag")
				,object.optString("servicePrice"),object.optString("servicePriceDefine"),object.optString("serviceStart"),object.optString("serviceStatus")
				,object.optString("serviceStatusCode"),object.optString("serviceTypeName"),object.optString("ticketFlag"),object.optString("serviceTypeId")
				);
				mlist.add(patient);
			}
			return mlist;
		} catch (Exception e) {
			return null;
		}
		
	}
	

}
