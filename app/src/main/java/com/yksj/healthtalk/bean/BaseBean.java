package com.yksj.healthtalk.bean;

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
public class BaseBean {
    public String code;  //返回1表示成功
    public String message; //提示信息
    public String result;  //结果内容
    public String doctors;//结果内容 by chen on 17/03

    public Love love;

    public static class Love{
        public String love;
        public String tn;
        public String pay_id;
        public String code;
        public String message;

        public String repmap;

        public String doctor_real_name;
        public String order_id;
        public String doctor_id;

        public String role_id;


        public String source;//支付宝需要的字段
        public String sign;
    }


}
