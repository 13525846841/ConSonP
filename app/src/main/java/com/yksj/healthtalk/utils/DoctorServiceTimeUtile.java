package com.yksj.healthtalk.utils;

public class DoctorServiceTimeUtile {
	//String str="20131127151850";
	//获取年 2013
	public static String getYear(String str){
		return str.substring(0, 4);
	}
	//获取时间 15:18
	public static String getTime(String str){
		return str.substring(8, 10)+":"+str.substring(10,12);
	}
	//获取日期1127
	public static String getDate(String str){
		return str.substring(4, 8);
	}
	//获取时间 15:18
	public static String getTimeObje(String str){
		return str.substring(8, 12);
	}
}
