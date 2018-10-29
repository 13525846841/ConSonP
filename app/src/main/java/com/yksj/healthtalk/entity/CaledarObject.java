package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.Calendar;

public class CaledarObject implements Serializable{

	public Calendar date;
	public String flage;
	public  static final String busy ="1";//月满
	public  static final String noBusy ="2";//预约
	public  static final String ilder ="3";//空闲
	public CaledarObject(Calendar date, String flage) {
		super();
		this.date = date;
		this.flage = flage;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getFlage() {
		return flage;
	}
	public void setFlage(String flage) {
		this.flage = flage;
	}
	public CaledarObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
