package com.yksj.healthtalk.utils;

import android.content.Context;

import com.pinyin4android.PinyinUtil;




/**
 * 中文转化为拼音
 * @author dhh
 *
 */
public class PingYinUtil {
	    /**
	    * 将字符串中的中文转化为拼音,其他字符不变
	    * @param inputString
	    * @return
	    */
	    public static String getPingYin(Context context,String inputString) {
	    	String temp = PinyinUtil.toPinyin(context, inputString);
	    	if(temp.equals(""))temp = inputString;
	        return temp.trim().substring(0, 1).toUpperCase();
	    }
	    
}
