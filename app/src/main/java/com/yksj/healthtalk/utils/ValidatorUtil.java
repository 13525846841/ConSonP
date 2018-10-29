package com.yksj.healthtalk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 验证类
 * @author origin
 *
 */
public class ValidatorUtil {
	//手机号码验证
	public static boolean checkMobile(String value) {
		if(!HStringUtil.isEmpty(value) && value.length() ==11 && value.startsWith("1"))return true;
		return false;
	}
	
    /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) {   
        return str.length()>7;  
    } 
    
    /** 
     * 验证输入的邮箱格式是否符合 
     * @param email 
     * @return 是否合法   true  合法
     */  
    public static boolean emailFormat(String email)  
    {  
    	if(!HStringUtil.isEmpty(email) && email.contains("@") && email.contains("."))
    	 return  true;
    	 return false;
        
    } 
    /**
     * 邮箱正则表达
     * @param email
     * @return
     */
    public static boolean emailRegular(String email){
    	 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
    	 Pattern regex = Pattern.compile(check);    
    	 Matcher matcher = regex.matcher(email);    
    	 boolean isMatched = matcher.matches();  
    	 return isMatched;
    }
    /**
     * 手机号码正则
     * @param phone
     * @return
     */
    public static boolean PhoneRegular(String phone){
    	String check = "^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\d{8}$";  
    	 Pattern regex = Pattern.compile(check);  
    	 Matcher matcher = regex.matcher(phone);  
    	 boolean isMatched = matcher.matches();
    	 return isMatched;
    }

	/**
	 * 姓名正则
	 * @param name
	 * @return
	 */
	public static boolean nameRegular(String name){
//		String check = "^[\\w\\u4E00-\\u9FA5\\uF900-\\uFA2D]*$";
		String check = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(name);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
}
