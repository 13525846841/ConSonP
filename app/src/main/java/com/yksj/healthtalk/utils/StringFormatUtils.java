package com.yksj.healthtalk.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.yksj.healthtalk.entity.ShopListItemEntity;
import com.yksj.healthtalk.entity.TagEntity;

/**
 * 对所有的字符串进行格式转换
 * @author Administrator
 *
 */
public class StringFormatUtils {
	static SimpleDateFormat mDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DecimalFormat mDecimalFormat2 = new DecimalFormat("0.0");
/*	static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH时mm分ss秒");*/
	
	public static String getTimeStr(){
		return String.valueOf(System.currentTimeMillis());
	}
	
	public static String getTimeStr(String time) {
		int length = time.length();
		if (length == 14) {
			return format("yyyyMMddhhmmss", "yyyy-MM-dd HH:mm:ss", time);
		}
		if (length == 8) {
			return format("yyyyMMdd", "yyyy-MM-dd", time);
		}
		return time;
	}
	
	
	public static String getChatMessageDate(Date date){
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return mDateFormat.format(date).toString();
	}
	
	public static String getChatMessageData(long time){
		return mDateFormat1.format(new Date(time));
	}
	
	/**
	 * 根据毫秒数获取时间
	 * @param time
	 * @return
	 */
	public static String getDateByMilliseconds(String time){
		return mDateFormat1.format(new Date(time));
	}
	
	/**
	 * 根据时间格式获取时间毫秒
	 * @param time
	 * @return
	 */
	public static long getChatMessageData(String time){
		try {
			return mDateFormat1.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getBirthday(String time){
		return format("yyyyMMdd", "yyyy年MM月dd日", time);
	}
	
	private static String format(String format,String style,String time){
		String timeFormate = "";
		try {
			Date date = new SimpleDateFormat(format).parse(time);
			timeFormate = new SimpleDateFormat(style).format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeFormate;
	}
	
	public static String pareTime(String time){
		if(time == null)time = String.valueOf(System.currentTimeMillis());
		double dtime = Double.valueOf(time);
		time = String.valueOf((long)dtime);
		return time;
	}
	
	public static long getTime(String time){
		long timeSec = 0;
		try{
			if(time != null)timeSec = Long.valueOf(time);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		return timeSec;
	}
	/**
	 * 将集合转换成字符串
	 * @param list
	 * @return
	 */
	public static String ListToString(List<TagEntity> list){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getName());
		}
		return sb.toString();
	}
	
	/**
	 * 将数组转为ArrayList[]
	 */
	public static ArrayList<TagEntity> ArrayToArrayList(String[] arr){
		List<TagEntity> list = new ArrayList<TagEntity>();
        for(int i=0;i<arr.length;i++){
//            list.add(arr[i]);
        	}
        return null;
	}
	
	public static boolean isEmail(String str){
		if(str == null)return false;
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isPhoneNumber(String str) {
		if(str == null)return false;
//		str = str.substring(str.length() - 11);
		String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.find();
    }
	
	public static String ObjectToString(Object o){
		if(null != o && !"null".equals(o.toString()))
			return o.toString();
		return "";
	}
	
	
	
	/**
	 * 商品信息
	 * @param entity
	 * @param i 购买
	 * @return
	 * @example {goods:[{goods_id:123,goods_price:12.8,goods_number:2}]}
	 */
	public static String getGoodsInfoStr(ShopListItemEntity  entity){
		StringBuilder builder = new StringBuilder();
		builder.append("{goods:[{goods_id:").append(entity.getGOODS_ID()).append(",goods_price:")
		.append(!TextUtils.isEmpty(entity.getCURRENT_PRICE())?entity.getCURRENT_PRICE():0).append(",goods_number:").append(1).append("}]}");
	return builder.toString();
	}
	
	/**
	 * 删除商品信息
	 * @param entity
	 * @param i 购买
	 * @return
	 * @example {goods:[{goods_id:123,goods_price:12.8,goods_number:2}]}
	 */
	public static String getDeleteGoodsInfoStr(ShopListItemEntity  entity){
			StringBuilder builder = new StringBuilder();
			builder.append("{goods:[{goods_id:").append(entity.getGOODS_ID()).append(",goods_price:")
			.append(entity.getGOODS_GOLD()).append(",goods_number:").append(0).append("}]}");
		return builder.toString();
	}

	public static boolean isIDCardNumber(String idNumStr)
	{
//		Pattern idNumPattern15 = Pattern.compile("^[1-9]d{7}((0[1-9])|(1[0-2]))(([0[1-9]|1d|2d])|3[0-1])d{2}([0-9]|x|X){1}$");
//		Pattern idNumPattern18 = Pattern.compile("^[1-9]d{5}[1-9]d{3}((0[1-9]))|((1[0-2]))(([0[1-9]|1d|2d])|3[0-1])d{3}([0-9]|x|X){1}$");
//		if (idNumPattern15.matcher(str).matches() || idNumPattern18.matcher(str).matches())
//		{
//			return true;
//		}else
//			return false;

		//定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9X])|(\\d{17}[0-9X])");
		//通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(idNumStr);
		//判断用户输入是否为身份证号
		return idNumMatcher.matches();
	}

	public static boolean isPhoneNum(String phone){
		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0-9])||(18[0,0-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

//	public static boolean isPhoneNumT(String phone){
//		//Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		if (phone.length()!=11){
//			return true;
//		}else {
//			return false;
//		}
//	}
}
