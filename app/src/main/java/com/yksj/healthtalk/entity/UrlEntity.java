package com.yksj.healthtalk.entity;

import java.io.Serializable;

public class UrlEntity implements Serializable{

	private String url;
	private String sex;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
