package com.yksj.healthtalk.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.yksj.healthtalk.entity.ChooseTagsEntity;


public class XMLUtils {
	
	public static List<ChooseTagsEntity> parseUserChooseTagsEntity(Resources resources,int resid){
		List<ChooseTagsEntity> list = new ArrayList<ChooseTagsEntity>();
		XmlResourceParser parser = resources.getXml(resid);
		try {
			while(parser.getEventType() != XmlResourceParser.END_DOCUMENT){
				if(parser.getEventType() == XmlResourceParser.START_TAG){
					String name = parser.getName();
					if(name.startsWith("parent")){
						String typeStr = parser.getAttributeValue(2);
						String id = parser.getAttributeValue(0);
						String nameStr = parser.getAttributeValue(1);
						list.add(new ChooseTagsEntity(id, nameStr,typeStr,false));
					}
				}
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(parser != null)parser.close();
		}
		return list;
	}
	
	/**
	 * 获取标签
	 * @param resources
	 * @param resid
	 * @param index 1 是个性标签  2 是相同经历
	 * @return
	 */
	public static List<ChooseTagsEntity> parseUserLablesTags(Resources resources,int resid,int index){
		List<ChooseTagsEntity> list = new ArrayList<ChooseTagsEntity>();
		XmlResourceParser parser = resources.getXml(resid);
		try {
			while(parser.getEventType() != XmlResourceParser.END_DOCUMENT){
				if(parser.getEventType() == XmlResourceParser.START_TAG){
					String name = parser.getName();
					if(name.startsWith("parent")&& parser.getAttributeValue(2).equals(String.valueOf(index))){
						String typeStr = parser.getAttributeValue(2);
						String id = parser.getAttributeValue(0);
						String nameStr = parser.getAttributeValue(1);
						list.add(new ChooseTagsEntity(id, nameStr,typeStr,false));
					}
				}
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(parser != null)parser.close();
		}
		return list;
	}

}
