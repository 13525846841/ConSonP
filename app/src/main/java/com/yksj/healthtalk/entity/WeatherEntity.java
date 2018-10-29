package com.yksj.healthtalk.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 地区+编码实体
 * @author Administrator
 *
 */
public class WeatherEntity implements Parcelable{

	private String AREA_CODE;
	private String AREA_NAME;
	private String UPPER_AREA_CODE;
	private String USED_FLAG;
	private String SUB_AREAS;
	public String getAREA_CODE() {
		return AREA_CODE;
	}
	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}
	public String getAREA_NAME() {
		return AREA_NAME;
	}
	public void setAREA_NAME(String aREA_NAME) {
		AREA_NAME = aREA_NAME;
	}
	public String getUPPER_AREA_CODE() {
		return UPPER_AREA_CODE;
	}
	public void setUPPER_AREA_CODE(String uPPER_AREA_CODE) {
		UPPER_AREA_CODE = uPPER_AREA_CODE;
	}
	public String getUSED_FLAG() {
		return USED_FLAG;
	}
	public void setUSED_FLAG(String uSED_FLAG) {
		USED_FLAG = uSED_FLAG;
	}
	public String getSUB_AREAS() {
		return SUB_AREAS;
	}
	public void setSUB_AREAS(String sUB_AREAS) {
		SUB_AREAS = sUB_AREAS;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(AREA_CODE);
		dest.writeString(AREA_NAME);
		dest.writeString(SUB_AREAS);
		dest.writeString(UPPER_AREA_CODE);
		dest.writeString(USED_FLAG);
		
	}
	
	public static final Parcelable.Creator<WeatherEntity> CREATOR=new Creator<WeatherEntity>() {
		
		@Override
		public WeatherEntity[] newArray(int size) {
			return new WeatherEntity[size];
		}
		
		@Override
		public WeatherEntity createFromParcel(Parcel source) {
			WeatherEntity mEntity=new WeatherEntity();
			mEntity.AREA_CODE=source.readString();
			mEntity.AREA_NAME=source.readString();
			mEntity.SUB_AREAS=source.readString();
			mEntity.UPPER_AREA_CODE=source.readString();
			mEntity.USED_FLAG=source.readString();
			return mEntity;
		}
	};
	public static Map<WeatherEntity,List<WeatherEntity>> parseJsonToInfo(String content) {
		Map<WeatherEntity,List<WeatherEntity>> map=new LinkedHashMap<WeatherEntity, List<WeatherEntity>>();
		try {
			JSONArray json=new JSONArray(content);
			for (int i = 0; i < json.length(); i++) {
				JSONObject object = (JSONObject) json.get(i);
				WeatherEntity entitys=new WeatherEntity();
				entitys.setAREA_CODE(object.getString("AREA_CODE"));
				entitys.setAREA_NAME(object.getString("AREA_NAME"));
				entitys.setUPPER_AREA_CODE(object.getString("UPPER_AREA_CODE"));
				entitys.setUSED_FLAG(object.getString("USED_FLAG"));
				JSONArray jsonArray=(JSONArray)object.get("SUB_AREAS");
				List list=new ArrayList<WeatherEntity>();
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject o = (JSONObject) jsonArray.get(j);
					WeatherEntity entity=new WeatherEntity();
					entity.setAREA_CODE(o.getString("AREA_CODE"));
					entity.setAREA_NAME(o.getString("AREA_NAME"));
					entity.setUPPER_AREA_CODE(o.getString("UPPER_AREA_CODE"));
					entity.setUSED_FLAG(o.getString("USED_FLAG"));
					list.add(entity);
				}
				map.put(entitys, list);
			}
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
			return map;
		}
		
	}
	
}
