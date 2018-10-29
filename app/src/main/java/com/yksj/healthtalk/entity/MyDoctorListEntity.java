package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class MyDoctorListEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	JSONObject doctorList;
	String name;
	int index;
	int id;
	ImageEntity mImageEntity ;
	List<UrlEntity> entitys;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<UrlEntity> getHeaderPath() {
		return entitys;
	}

	public void setHeaderPath(List<UrlEntity> headerPath) {
		this.entitys = headerPath;
	}

	public ImageEntity getmImageEntity() {
		return mImageEntity;
	}

	public void setmImageEntity(ImageEntity mImageEntity) {
		this.mImageEntity = mImageEntity;
	}

	public JSONObject getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(JSONObject doctorList) {
		this.doctorList = doctorList;
		ImageEntity entity = new ImageEntity();
		entity.num = doctorList.getIntValue("num");
		entity.list = doctorList.getJSONArray("list");
		setmImageEntity(entity);
		List<UrlEntity> list = new ArrayList<UrlEntity>();
		for (int i = 0; i < entity.list.size(); i++) {
			JSONObject object= entity.list.getJSONObject(i);
			UrlEntity urlEntity=new UrlEntity();
			urlEntity.setUrl(object.getString("CLIENT_ICON_BACKGROUND"));
			urlEntity.setSex(object.getString("CUSTOMER_SEX"));
			list.add(urlEntity);
		}
		setHeaderPath(list);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	

	public class ImageEntity implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int num;
		JSONArray list;
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public JSONArray getList() {
			return list;
		}
		public void setList(JSONArray list) {
			this.list = list;
		}
		
		
	}
	
}
