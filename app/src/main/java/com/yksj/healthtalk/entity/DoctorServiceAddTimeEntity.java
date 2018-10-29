package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.yksj.healthtalk.utils.StringFormatUtils;

public class DoctorServiceAddTimeEntity implements Serializable,Parcelable,Comparator<DoctorServiceAddTimeEntity>{
	
	private String ISBUZY="0";//闲忙状态
	private String MERCHANT_ID;//提供服务方商户ID
	private String CUSTOMER_ID;//--医生客户ID
	private String SERVICE_ITEM_ID;//--医生服务项目ID
	private String SERVICE_ITEM_NAME;//--医生服务项目名称(自定义服务预留)
	private String SERVICE_TYPE_ID;//--医生服务类型ID
	private String SERVICE_TYPE_SUB_ID;//-医生服务类型细分ID
	private String ORDER_ON_OFF;// --开启标记: 1-开；0-关；默认关！在已被购买的情况下，可以关闭，但已有挂号的仍然服务订单执行！
	private String ORDER_ON_OFF_TIME;//20131127094946,-开启服务时间YYYYMMDDHH24MISS
	private String SERVICE_MAX;//服务最大名额，
	private String SERVICE_PRICE;//服务价格
	private String SERVICE_PLACE;//--服务地点
	private String SERVICE_ITEM_DESC;//--服务内容, --医生服务项目说明
	private String SERVICE_HOUR;//医生服务项目服务时间总和(小时)
	private String SERVICE_TIME_BEGIN;//-服务时间段开始时间HH24MISS
	private String SERVICE_TIME_END;//服务时间段结束时间HH24MISS
	private String ORDER_CURRENT;//-当前挂号人数
	private String REPEAT_FLAG;//--预约服务时段是否重复标记(1-是，0-否)
	private String REPEAT_BATCH;//预约服务时段设置批次号 --(主要为重复时间段使用，一个时间段周二、三、五重复，则生成三笔数据，批次号一致)
	private String WEEK;//-时间段对应的星期几(1、2、3、4、5、6、7)"
	private String ISSPECIAL;//是否特殊收费
	private String ISSLECT;//是否选择服务内容
	private boolean isSeclect=false;//用于批量删除  是否选中
	private String REPEATDATES;//如果是自定的日期 就表示选择的自定义时间
	
	
	public boolean isSeclect() {
		return isSeclect;
	}

	public void setSeclect(boolean isSeclect) {
		this.isSeclect = isSeclect;
	}

	public String getISSLECT() {
		return ISSLECT;
	}

	public void setISSLECT(String iSSLECT) {
		ISSLECT = iSSLECT;
	}

	public String getISSPECIAL() {
		return ISSPECIAL;
	}

	public void setISSPECIAL(String iSSPECIAL) {
		ISSPECIAL = iSSPECIAL;
	}

	public String getISBUZY() {
		return ISBUZY;
	}

	public void setISBUZY(String iSBUZY) {
		ISBUZY = iSBUZY;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}

	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}

	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}

	public String getSERVICE_ITEM_ID() {
		return SERVICE_ITEM_ID;
	}

	public void setSERVICE_ITEM_ID(String sERVICE_ITEM_ID) {
		SERVICE_ITEM_ID = sERVICE_ITEM_ID;
	}

	public String getSERVICE_ITEM_NAME() {
		return SERVICE_ITEM_NAME;
	}

	public void setSERVICE_ITEM_NAME(String sERVICE_ITEM_NAME) {
		SERVICE_ITEM_NAME = sERVICE_ITEM_NAME;
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

	public String getORDER_ON_OFF() {
		return ORDER_ON_OFF;
	}

	public void setORDER_ON_OFF(String oRDER_ON_OFF) {
		ORDER_ON_OFF = oRDER_ON_OFF;
	}

	public String getORDER_ON_OFF_TIME() {
		return ORDER_ON_OFF_TIME;
	}

	public void setORDER_ON_OFF_TIME(String oRDER_ON_OFF_TIME) {
		ORDER_ON_OFF_TIME = oRDER_ON_OFF_TIME;
	}

	public String getSERVICE_MAX() {
		return SERVICE_MAX;
	}

	public void setSERVICE_MAX(String sERVICE_MAX) {
		SERVICE_MAX = sERVICE_MAX;
	}

	public String getSERVICE_PRICE() {
		return SERVICE_PRICE;
	}

	public void setSERVICE_PRICE(String sERVICE_PRICE) {
		SERVICE_PRICE = sERVICE_PRICE;
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

	public String getSERVICE_HOUR() {
		return SERVICE_HOUR;
	}

	public void setSERVICE_HOUR(String sERVICE_HOUR) {
		SERVICE_HOUR = sERVICE_HOUR;
	}

	public String getSERVICE_TIME_BEGIN() {
		return SERVICE_TIME_BEGIN;
	}
	public void setSERVICE_TIME_BEGIN(String sERVICE_TIME_BEGIN) {
		SERVICE_TIME_BEGIN = sERVICE_TIME_BEGIN;
	}

	public String getSERVICE_TIME_END() {
		return SERVICE_TIME_END;
	}

	public void setSERVICE_TIME_END(String sERVICE_TIME_END) {
		SERVICE_TIME_END = sERVICE_TIME_END;
	}

	public String getORDER_CURRENT() {
		return ORDER_CURRENT;
	}

	public void setORDER_CURRENT(String oRDER_CURRENT) {
		ORDER_CURRENT = oRDER_CURRENT;
	}

	public String getREPEAT_FLAG() {
		return REPEAT_FLAG;
	}

	public void setREPEAT_FLAG(String rEPEAT_FLAG) {
		REPEAT_FLAG = rEPEAT_FLAG;
	}

	public String getREPEAT_BATCH() {
		return REPEAT_BATCH;
	}

	public void setREPEAT_BATCH(String rEPEAT_BATCH) {
		REPEAT_BATCH = rEPEAT_BATCH;
	}

	public String getWEEK() {
		return WEEK;
	}

	public void setWEEK(String wEEK) {
		WEEK = wEEK;
	}

	public DoctorServiceAddTimeEntity(String iSBUZY, String mERCHANT_ID,
			String cUSTOMER_ID, String sERVICE_ITEM_ID,
			String sERVICE_ITEM_NAME, String sERVICE_TYPE_ID,
			String sERVICE_TYPE_SUB_ID, String oRDER_ON_OFF,
			String oRDER_ON_OFF_TIME, String sERVICE_MAX, String sERVICE_PRICE,
			String sERVICE_PLACE, String sERVICE_ITEM_DESC,
			String sERVICE_HOUR, String sERVICE_TIME_BEGIN,
			String sERVICE_TIME_END, String oRDER_CURRENT, String rEPEAT_FLAG,
			String rEPEAT_BATCH, String wEEK,String ISSPECIAL,String ISSLECT) {
		super();
		this.ISBUZY = iSBUZY;
		this.MERCHANT_ID = mERCHANT_ID;
		this.CUSTOMER_ID = cUSTOMER_ID;
		this.SERVICE_ITEM_ID = sERVICE_ITEM_ID;
		this.SERVICE_ITEM_NAME = sERVICE_ITEM_NAME;
		this.SERVICE_TYPE_ID = sERVICE_TYPE_ID;
		this.SERVICE_TYPE_SUB_ID = sERVICE_TYPE_SUB_ID;
		this.ORDER_ON_OFF = oRDER_ON_OFF;
		this.ORDER_ON_OFF_TIME = oRDER_ON_OFF_TIME;
		this.SERVICE_MAX = sERVICE_MAX;
		this.SERVICE_PRICE = sERVICE_PRICE;
		this.SERVICE_PLACE = sERVICE_PLACE;
		this.SERVICE_ITEM_DESC = sERVICE_ITEM_DESC;
		this.SERVICE_HOUR = sERVICE_HOUR;
		this.SERVICE_TIME_BEGIN = sERVICE_TIME_BEGIN;
		this.SERVICE_TIME_END = sERVICE_TIME_END;
		this.ORDER_CURRENT = oRDER_CURRENT;
		this.REPEAT_FLAG = rEPEAT_FLAG;
		this.REPEAT_BATCH = rEPEAT_BATCH;
		this.WEEK = wEEK;
		this.ISSPECIAL=ISSPECIAL;
		this.ISSLECT=ISSLECT;
	}

	public DoctorServiceAddTimeEntity() {
	}

	public static List<DoctorServiceAddTimeEntity> parToList(String content){
		List<DoctorServiceAddTimeEntity> entities=new ArrayList<DoctorServiceAddTimeEntity>();
		try {
			JSONArray array=new JSONArray(content);
			int length=array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object=(JSONObject) array.get(i);
				DoctorServiceAddTimeEntity entity=new DoctorServiceAddTimeEntity(
						StringFormatUtils.ObjectToString(object.optString("ISBUZY","0")),
						StringFormatUtils.ObjectToString(object.optString("MERCHANT_ID","")),
						StringFormatUtils.ObjectToString(object.optString("CUSTOMER_ID","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_ITEM_ID","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_ITEM_NAME","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_TYPE_ID","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_TYPE_SUB_ID","")),
						StringFormatUtils.ObjectToString(object.optString("ORDER_ON_OFF","")),
						StringFormatUtils.ObjectToString(object.optString("ORDER_ON_OFF_TIME","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_MAX","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_PRICE","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_PLACE","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_ITEM_DESC","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_HOUR","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_TIME_BEGIN","")),
						StringFormatUtils.ObjectToString(object.optString("SERVICE_TIME_END","")),
						StringFormatUtils.ObjectToString(object.optString("ORDER_CURRENT","")),
						StringFormatUtils.ObjectToString(object.optString("REPEAT_FLAG","")),
						StringFormatUtils.ObjectToString(object.optString("REPEAT_BATCH","")),
						StringFormatUtils.ObjectToString(object.optString("WEEK","")),
						StringFormatUtils.ObjectToString(object.optString("ISSPECIAL","0")),//
						StringFormatUtils.ObjectToString(object.optString("ISSLECT","0"))
				);
					entities.add(entity);
			}
			return entities;
		} catch (Exception e) {
			return null;
		}
	}
	

	@Override 
	public int describeContents() { 
	return 0; 
	} 

	@Override 
	public void writeToParcel(Parcel dest, int flags) { 
	// TODO Auto-generated method stub 
		dest.writeString(this.ISBUZY); 
		dest.writeString(this.MERCHANT_ID); 
		dest.writeString(this.CUSTOMER_ID); 
		dest.writeString(this.SERVICE_ITEM_ID); 
		dest.writeString(this.SERVICE_ITEM_NAME); 
		dest.writeString(this.SERVICE_TYPE_ID); 
		dest.writeString(this.SERVICE_TYPE_SUB_ID); 
		dest.writeString(this.ORDER_ON_OFF); 
		dest.writeString(this.ORDER_ON_OFF_TIME); 
		dest.writeString(this.SERVICE_MAX); 
		dest.writeString(this.SERVICE_PRICE); 
		dest.writeString(this.SERVICE_PLACE); 
		dest.writeString(this.SERVICE_ITEM_DESC); 
		dest.writeString(this.SERVICE_HOUR); 
		dest.writeString(this.SERVICE_TIME_BEGIN); 
		dest.writeString(this.SERVICE_TIME_END); 
		dest.writeString(this.ORDER_CURRENT); 
		dest.writeString(this.REPEAT_FLAG); 
		dest.writeString(this.REPEAT_BATCH); 
		dest.writeString(this.WEEK); 
		dest.writeString(this.ISSPECIAL);
		dest.writeString(this.ISSLECT);
	} 

	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口  
	public static final Parcelable.Creator<DoctorServiceAddTimeEntity> CREATOR = new Creator<DoctorServiceAddTimeEntity>() { 
	@Override 
	public DoctorServiceAddTimeEntity[] newArray(int size) {//直接返回对象的长度就可以了
		return new DoctorServiceAddTimeEntity[size]; 
	}; 


	@Override 
	public DoctorServiceAddTimeEntity createFromParcel(Parcel source) { 
	return new DoctorServiceAddTimeEntity(source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),
			source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString(),source.readString()); 
	} };


	@Override
	public int compare(DoctorServiceAddTimeEntity arg0,DoctorServiceAddTimeEntity arg1) {
		try {
			return Long.valueOf(arg0.getSERVICE_TIME_BEGIN() )- Long.valueOf(arg1.getSERVICE_TIME_BEGIN() ) > 0 ? 1:-1;
		} catch (Exception e) {
			return -1;
		}
	}
}
