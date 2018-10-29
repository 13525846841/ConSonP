package com.yksj.healthtalk.bean;

/**
 * 优惠券的bean
 * Created by lmk on 15/9/29.
 *
 *"R": 1,
 "ID": 101,
 "STATUS": 0,
 "VALUE": 5,
 "AREA": "",
 "GET_TIME": "20150923000000",
 "BEGIN": "20150923000000",
 "END": "20151020000000",
 "USED_TIME": null,
 "STATUSNAME": "有效期至2015-10-20 00:00"
 *
 */
public class CouponBean {

    public int R;
    public int ID;
    public int STATUS;//0未使用  1已使用 2已过期
    public int VALUE;
    public String AREA;
    public String GET_TIME;
    public String BEGIN;
    public String END;
    public String USED_TIME;
    public String STATUSNAME;


}
