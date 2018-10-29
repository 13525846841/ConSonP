package com.yksj.healthtalk.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string 工具类
 *
 * @author Administrator
 */
public class HStringUtil {

    /**
     * 判断字符串是否为null
     * return true 表示为null或者length=0
     * false 表示length>0
     */
    public static boolean isEmpty(String str) {
        if (str == null || TextUtils.isEmpty(str.trim()) || str.equals("null"))
            return true;
        else
            return false;
    }

    /**
     * 判断字符串是否为null
     * return true 表示为null或者length=0
     * false 表示length>0
     */
    public static boolean isEmptyAndZero(String str) {
        if (str == null || TextUtils.isEmpty(str.trim()) || "null".equals(str) || "0".equals(str))
            return true;
        else
            return false;
    }

    /**
     * 对字符串的空处理
     */
    public static String stringFormat(String s) {
        if (isEmpty(s) || null == s || s.equals("null")) {
            return "";
        } else {
            return s;
        }
    }

    //半角转为全角 避免排版混乱
    public static String ToFormatDBC(String input) {
        if (isEmpty(input)) return "";
        input = stringFilter(input);
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    private static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("，", ",")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号   
        String regEx = "[『』]"; // 清除掉特殊字符   
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否是小数
     *
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str).matches();
    }
}
