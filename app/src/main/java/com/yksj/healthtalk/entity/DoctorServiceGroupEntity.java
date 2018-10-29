package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorServiceGroupEntity implements Serializable{
/**
 *    "SERVICE_ITEM_ID": 362,
        "SPECIAL_GROUP_ID": 101,
        "SPECIAL_GROUP": "咯露露",
        "GROUP_TYPE": null,
        "CREATE_TIME": "20131122165141",
        "SPECIAL_PRICE": 55
 */
	private String SERVICE_ITEM_ID;
	private String SPECIAL_GROUP_ID;
	private String SPECIAL_GROUP;
//	private String GROUP_TYPE;
//	private String CREATE_TIME;
	private String SPECIAL_PRICE;
	public String getSERVICE_ITEM_ID() {
		return SERVICE_ITEM_ID;
	}
	public void setSERVICE_ITEM_ID(String sERVICE_ITEM_ID) {
		SERVICE_ITEM_ID = sERVICE_ITEM_ID;
	}
	public String getSPECIAL_GROUP_ID() {
		return SPECIAL_GROUP_ID;
	}
	public void setSPECIAL_GROUP_ID(String sPECIAL_GROUP_ID) {
		SPECIAL_GROUP_ID = sPECIAL_GROUP_ID;
	}
	public String getSPECIAL_GROUP() {
		return SPECIAL_GROUP;
	}
	public void setSPECIAL_GROUP(String sPECIAL_GROUP) {
		SPECIAL_GROUP = sPECIAL_GROUP;
	}
/*	public String getGROUP_TYPE() {
		return GROUP_TYPE;
	}
	public void setGROUP_TYPE(String gROUP_TYPE) {
		GROUP_TYPE = gROUP_TYPE;
	}*/
/*	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}*/
	public String getSPECIAL_PRICE() {
		return SPECIAL_PRICE;
	}
	public void setSPECIAL_PRICE(String sPECIAL_PRICE) {
		SPECIAL_PRICE = sPECIAL_PRICE;
	}
	
	public static List<DoctorServiceGroupEntity> parseToList(String content){
		List<DoctorServiceGroupEntity> entities = null;
		try {
			entities=new ArrayList<DoctorServiceGroupEntity>();
			JSONArray array=new JSONArray(content);
			for (int i = 0; i < array.length(); i++) {
			DoctorServiceGroupEntity entity=new DoctorServiceGroupEntity();
			JSONObject object=(JSONObject) array.get(i);
//			entity.setCREATE_TIME(object.getString("CREATE_TIME"));
//			entity.setGROUP_TYPE(object.getString("GROUP_TYPE"));
			entity.setSERVICE_ITEM_ID(object.getString("SERVICE_ITEM_ID"));
			entity.setSPECIAL_GROUP(object.getString("SPECIAL_GROUP"));
			entity.setSPECIAL_GROUP_ID(object.getString("SPECIAL_GROUP_ID"));
			entity.setSPECIAL_PRICE(object.getString("SPECIAL_PRICE"));
			entities.add(entity);
			}
		} catch (JSONException e) {
			return null;
		}
		return entities;
	}
}
