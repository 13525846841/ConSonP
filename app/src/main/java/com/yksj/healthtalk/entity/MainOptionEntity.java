package com.yksj.healthtalk.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 首页图标封装实体
 * @author jack_tang
 *
 */
public class MainOptionEntity implements Serializable{
	public String text;
	public int id;
	public MainOptionEntity(String text, int id) {
		super();
		this.text = text;
		this.id = id;
	}
	public MainOptionEntity() {
		super();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public static JSONObject formatJson(MainOptionEntity str){
		JSONObject object=new JSONObject();
		try {
			object.put("Text", str.text);
			object.put("Id", str.id);
		} catch (JSONException e) {
			return null;
		}
	
		return object;
	}
	
}
