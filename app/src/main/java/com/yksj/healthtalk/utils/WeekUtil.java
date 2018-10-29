package com.yksj.healthtalk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 计算某一天是星期几
 *
 */
public class WeekUtil {
	private static final Calendar calendar = Calendar.getInstance(java.util.Locale.CHINA);
	private static final String[] weekdays = {"周日","周一","周二","周三","周四","周五","周六",};
	private static final SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.CHINA);

      
      public static String getweekday(String ymd) throws Exception{
    	  Date date = adf.parse(ymd);
    	  calendar.setTime(date);
    	  int weekday = calendar.get(Calendar.DAY_OF_WEEK)-1;
    	  return  weekdays[weekday];
    	 }

      
      public String getDate(){
    	  SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");   
          Date date = new Date(System.currentTimeMillis());
          String str = formatter.format(date);
		  return str;
    	  
      }
      
    
}
