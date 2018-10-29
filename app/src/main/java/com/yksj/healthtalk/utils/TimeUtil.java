package com.yksj.healthtalk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.TextUtils;
import android.text.format.Time;

public class TimeUtil {

    static SimpleDateFormat mDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyyMMddhhmmss");
    static SimpleDateFormat mDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    public static int getChatTime(long time) {
        //上一次时间
        Time lastTime = new Time();
        lastTime.set(time);
        //今天
        Time todayTime = new Time();
        todayTime.setToNow();
//		DateUtils.isToday(when)
        //今天
        if ((lastTime.year == todayTime.year) && (lastTime.month == todayTime.month) && (lastTime.monthDay == todayTime.monthDay)) {
            return 0;
            //年,月相等
        } else if ((lastTime.year == todayTime.year) && (lastTime.month == todayTime.month)) {
            //计算相差天数
            return todayTime.monthDay - lastTime.monthDay;
        }
        return -1;
    }

    static SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日 HH时mm分ss秒");

    public static String getTimeStr() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static long getChatTime(String time) {
        try {
            return mDateFormat2.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String getFormatTime(String time) {
        Date date = null;
        try {
            date = mDateFormat2.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return mDateFormat1.format(date);
        } else {
            return time;
        }
    }

    public static String getFormatTime1(String time) {
        Date date = null;
        try {
            date = mDateFormat2.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return mDateFormat3.format(date);
        } else {
            return time;
        }
    }

    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
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

    public static String getTimeStr8(String time) {
        return format("yyyyMMddhhmmss", "yyyy-MM-dd", time);
    }

    public static String getChatMessageDate(Date date) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return mDateFormat.format(date).toString();
    }

    public static String getChatMessageData(long time) {
        return mDateFormat1.format(new Date(time));
    }

    /**
     * 根据毫秒数获取时间
     *
     * @param time
     * @return
     */
    public static String getDateByMilliseconds(String time) {
        return mDateFormat1.format(new Date(time));
    }

    /**
     * 根据时间格式获取时间毫秒
     *
     * @param time
     * @return
     */
    public static long getChatMessageData(String time) {
        try {
            return mDateFormat1.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getBirthday(String time) {
        return format("yyyyMMdd", "yyyy年MM月dd日", time);
    }

    private static String format(String format, String style, String time) {
        String timeFormate = "";
        try {
            Date date = new SimpleDateFormat(format).parse(time);
            timeFormate = new SimpleDateFormat(style).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeFormate;
    }

    public static String pareTime(String time) {
        if (time == null) time = String.valueOf(System.currentTimeMillis());
        double dtime = Double.valueOf(time);
        time = String.valueOf((long) dtime);
        return time;
    }

    public static long getTime(String time) {
        long timeSec = 0;
        try {
            if (time != null) timeSec = Long.valueOf(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return timeSec;
    }

    public static Date getRealTime(String str) {
//		if(object instanceof Date) {
//			return ((Date)object).getTime();
//		}
//		if(object instanceof String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//			try {
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    }

    /**
     * 获得当前月  格式如 2013-3
     *
     * @return
     */
    public static String getMoneyTime() {
        String yue;
        int y, m, d, h, mi, s;
        Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH) + 1;
        if (m < 10) {
            yue = "0" + m;
        } else {
            yue = m + "";
        }
        return (y + "-" + yue).toString();
    }

    /**
     * 获取日期字符串。
     * <p>
     * <pre>
     *  日期字符串格式： yyyy-MM-dd
     *  其中：
     *      yyyy   表示4位年。
     *      MM     表示2位月。
     *      dd     表示2位日。
     * </pre>
     *
     * @param date 需要转化的日期。
     * @return String "yyyy-MM-dd"格式的日期字符串。
     */
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(new Date());
    }

    public static String getNowDateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        return formatter.format(new Date());
    }

    /**
     * 返回十四位的日期
     *
     * @return
     */
    public static String getNowDataForFourteen() {
        String date = mDateFormat2.format(new Date());
        return date;
    }

    //比较
    public static boolean getTimeByString(String time, String systemtime) {
        return parTimeTo14(time) > Long.valueOf(systemtime);
    }


    private static long parTimeTo14(String time) {
        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = "";
        Date date;
        try {
            if (!"".equals(time)) {
                date = ss.parse(time);
                str = s.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(str);
    }

    //2013-12-13 17:00-18:00。   开始结束时间
    public static String getDemo(String str, String str1) {
        return getYear(str) + "-" + str.subSequence(4, 6) + "-" + str.subSequence(6, 8) + " " + getTime2(str) + "-" + getTime2(str1);
    }

    public static String getFormatDate(String str) {
        return getYear(str) + "-" + str.subSequence(4, 6) + "-" + str.subSequence(6, 8) + " " + getTime2(str);
    }

    public static String getYear(String str) {
        return str.substring(0, 4);
    }

    //17:00-18:00
    public static String getTime2(String str) {
        return str.substring(8, 10) + ":" + str.substring(10, 12);
    }

    //将14位转化为 2013-12-28 17:39
    public static String format(String time) {
        String timeFormate = "";
        try {
            Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
            timeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (ParseException e) {
            return "";
        }
        return timeFormate.substring(0, timeFormate.length() - 3);
    }

    //将14位转化为 毫秒
    public static Long formatMillion(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(time).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//毫秒
        return millionSeconds;
    }

    public static String formatTime(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = sdf.parse(string);
            sdf.applyPattern("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getNowDayTime() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    /**
     * 20141012   转化为  2014年10月12日
     *
     * @return
     */
    public static String getFormatDate2(String calendar) {
        return calendar.substring(0, 4) + "年" + calendar.substring(4, 6) + "月" + calendar.substring(6, 8) + "日";
    }

    public static String changeTimeForInterestWall(String time) {
        String reStr = "";
        SimpleDateFormat reSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(Long.valueOf(time));
        Date nowDate = new Date();
        long between = (nowDate.getTime() - date.getTime()) / 1000;
        long day = between / (24 * 3600);//天
        long hour = between % (24 * 3600) / 3600;//小时
        long minute = between % 3600 / 60;//分钟
        if (day >= 1) {
            reStr = reSdf.format(date);
        } else if (hour != 0) {
            reStr = hour + "小时前";
        } else if (minute != 0) {
            reStr = minute + "分钟前";
        } else {
            reStr = "刚刚";
        }
        return reStr;
    }

    //小壹
    public static String getMesgTime(String time) {
        if (TextUtils.isEmpty(time)) return "";
        return dateFormat("yyyyMMddHHmmss", "yyyy-MM-dd HH:mm", time);
    }

    /**
     * 日期转换
     *
     * @param inputDate
     * @param outputDate
     * @param date
     * @return
     */
    public static String dateFormat(String inputDate, String outputDate, String date) {
        if (HStringUtil.isEmpty(date)) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(inputDate);
        SimpleDateFormat sdf2 = new SimpleDateFormat(outputDate);

        String sss = null;
        try {
            sss = sdf2.format(sdf.parse(date));
        } catch (ParseException e) {
            return "";
        }
        return sss;
    }


    /**
     * 返回当前日期时间字符串<br>
     * 默认格式:yyyymmddhhmmss
     *
     * @return String 返回当前字符串型日期时间
     */
    public static String getCurrentTimeAsNumber() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return f.format(date);
    }

    /**
     * 将时间戳转化为 yyyyMMddHHmmss
     *
     * @param begin
     * @return
     */
    public static String formatTimeLong(String begin) {
        if (TextUtils.isEmpty(begin)) return null;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        return f.format(new Date(Long.parseLong(begin)));
    }
}
