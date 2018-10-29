package com.yksj.healthtalk.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.yksj.healthtalk.entity.MyZoneGroupEntity;
import com.yksj.healthtalk.entity.TagEntity;

public class XmlCategoryParserUtils {

	public static LinkedHashMap<String, String> getCategoryDataFromXml(Resources resources, int resid,int classid) {
		XmlResourceParser xmlResourceParser = resources.getXml(resid);
		boolean flag = false;
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		int type;
		try {
			type = xmlResourceParser.getEventType();
			while (type != XmlResourceParser.END_DOCUMENT) {
				if(XmlResourceParser.START_TAG  == xmlResourceParser.getEventType() ){
					//id=0 默认搜索所有大类
					String tagName = xmlResourceParser.getName();
					if(classid == 0) {
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.getAttributeValue(1);
							data.put(name, id);
						}
					} else {
						//id不为0则按id搜索大类里面的小类
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							if(id.equals(classid+"")) {
								flag = true;
							}
						}
						if(tagName.equals("child")&&xmlResourceParser.getDepth()==3&&flag){
							String id1 = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.nextText();
							data.put(name, id1);
						}
					}
				}else if(XmlResourceParser.END_TAG  == xmlResourceParser.getEventType()) {
					String tagName = xmlResourceParser.getName();
					if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2&&flag) {
						flag = false;
						break;
					}
				}
				 if(XmlResourceParser.END_DOCUMENT  == xmlResourceParser.getEventType()){
						break;
					}
				xmlResourceParser.next();
			}
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * 转换map为List<Map>加入标志位
	 * @param data
	 * @return
	 */
	public static List<TagEntity> convertMapToListMap(LinkedHashMap<String, String> data) {
		List<TagEntity> convertedData = new ArrayList<TagEntity>();
		Set<String> set = data.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			String name = iterator.next();
			String id = data.get(name);
			TagEntity tagEntity = new TagEntity(id, name);
			convertedData.add(tagEntity);
		}
		
		return convertedData;
	}
	
	public static List<TagEntity> getCategoryData(Resources resources,int resid,int categoryId) {
		XmlResourceParser xmlResourceParser = resources.getXml(resid);
		List<TagEntity> data = new ArrayList<TagEntity>();
		boolean flag = false;
		int type;
		
		try {
			type = xmlResourceParser.getEventType();
			while (type != XmlResourceParser.END_DOCUMENT) {
				if(XmlResourceParser.START_TAG  == xmlResourceParser.getEventType() ){
					//id=0 默认搜索所有大类
					String tagName = xmlResourceParser.getName();
					if(categoryId == 0) {
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.getAttributeValue(1);
							TagEntity tagEntity = new TagEntity(id, name);
							tagEntity.setUpperId(categoryId+"");
							data.add(tagEntity);
						}
					} else {
						//id不为0则按id搜索大类里面的小类
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							if(id.equals(categoryId+"")) {
								flag = true;
							}
						}
						if(tagName.equals("child")&&xmlResourceParser.getDepth()==3&&flag){
							String id1 = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.nextText();
							TagEntity tagEntity = new TagEntity(id1, name);
							tagEntity.setUpperId(categoryId+"");
							data.add(tagEntity);
						}
					}
				}else if(XmlResourceParser.END_TAG  == xmlResourceParser.getEventType()) {
					String tagName = xmlResourceParser.getName();
					if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2&&flag) {
						flag = false;
						break;
					}
				}
				 if(XmlResourceParser.END_DOCUMENT  == xmlResourceParser.getEventType()){
						break;
					}
				xmlResourceParser.next();
			}
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static List<MyZoneGroupEntity> getCategoryData1(Resources resources,int resid,int categoryId) {
		XmlResourceParser xmlResourceParser = resources.getXml(resid);
		List<MyZoneGroupEntity> data = new ArrayList<MyZoneGroupEntity>();
		boolean flag = false;
		int type;
		try {
			type = xmlResourceParser.getEventType();
			while (type != XmlResourceParser.END_DOCUMENT) {
				if(XmlResourceParser.START_TAG  == xmlResourceParser.getEventType() ){
					//id=0 默认搜索所有大类
					String tagName = xmlResourceParser.getName();
					if(categoryId == 0) {
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.getAttributeValue(1);
							MyZoneGroupEntity entity = new MyZoneGroupEntity();
							entity.setClassId(id);
							entity.setClassName(name);
							data.add(entity);
						}
					} else {
						//id不为0则按id搜索大类里面的小类
						if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2){
							String id = xmlResourceParser.getAttributeValue(0);
							if(id.equals(categoryId+"")) {
								flag = true;
							}
						}
						if(tagName.equals("child")&&xmlResourceParser.getDepth()==3&&flag){
							String id1 = xmlResourceParser.getAttributeValue(0);
							String name=xmlResourceParser.nextText();
							MyZoneGroupEntity entity = new MyZoneGroupEntity();
							entity.setClassId(id1);
							entity.setClassName(name);
							data.add(entity);
						}
					}
				}else if(XmlResourceParser.END_TAG  == xmlResourceParser.getEventType()) {
					String tagName = xmlResourceParser.getName();
					if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2&&flag) {
						flag = false;
						break;
					}
				}
				 if(XmlResourceParser.END_DOCUMENT  == xmlResourceParser.getEventType()){
						break;
					}
				xmlResourceParser.next();
			}
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static TagEntity queryTagEntity(Resources resources,int resid,String categoryId) {
		TagEntity tagEntity = new TagEntity();
		tagEntity.setId(categoryId+"");
		XmlResourceParser xmlResourceParser = resources.getXml(resid);
		boolean flag = false;
		int type;
		try {
			type = xmlResourceParser.getEventType();
			String parentId = "";
			while (type != XmlResourceParser.END_DOCUMENT) {
				if(XmlResourceParser.START_TAG  == xmlResourceParser.getEventType() ){
					String tagName = xmlResourceParser.getName();
					if(tagName.equals("parent")) {
						parentId = xmlResourceParser.getAttributeValue(0);
						String name=xmlResourceParser.getAttributeValue(1);
						if(parentId.equals(categoryId+"")) {
							tagEntity.setName(name);
							tagEntity.setUpperId("0");
						}
					} else if(tagName.equals("child")){
						String id1 = xmlResourceParser.getAttributeValue(0);
						if(id1.equals(categoryId+"")) {
							String name=xmlResourceParser.nextText();
							tagEntity.setName(name);
							tagEntity.setUpperId(parentId);
						}
					}
				} else if(XmlResourceParser.END_TAG  == xmlResourceParser.getEventType()) {
					String tagName = xmlResourceParser.getName();
					if(tagName.equals("parent")&&xmlResourceParser.getDepth()==2&&flag) {
						flag = false;
						break;
					}
				}
				
				 if(XmlResourceParser.END_DOCUMENT  == xmlResourceParser.getEventType()){
						break;
				}
				xmlResourceParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tagEntity;
	}
	
}
