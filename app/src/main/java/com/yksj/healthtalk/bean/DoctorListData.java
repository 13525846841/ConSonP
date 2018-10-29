package com.yksj.healthtalk.bean;

import java.util.ArrayList;

/**
 * 基本实体
 * Created by lmk on 15/9/18.
 *
 * {
 "code": "1",
 "message": "",
 "result": "",
 }
 */
public class DoctorListData {
    public String code;  //返回1表示成功
    public String message; //提示信息
    public ArrayList<DoctorSimpleBean> result;  //结果内容

}
