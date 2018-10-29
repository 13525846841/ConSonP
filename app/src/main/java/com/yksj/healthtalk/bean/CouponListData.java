package com.yksj.healthtalk.bean;

import java.util.ArrayList;

/**
 * 优惠券列表数据
 * Created by lmk on 15/9/29.
 */
public class CouponListData {
    public String code;  //返回1表示成功
    public String message; //提示信息
    public ArrayList<CouponBean> result;
}
