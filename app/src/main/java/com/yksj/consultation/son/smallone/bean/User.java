package com.yksj.consultation.son.smallone.bean;


import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by jack_tang on 15/9/1.
 */
public class User implements Serializable {
    /**
     * "result": {
     * "TERMINAL_ID": "846fb0e4-37ae-4b50-a118-c17b115bd06b",
     * "CUSTOMER_ID": "225209",
     * "NOTE1": "20",
     * "NOTE2": null,
     * "RECORD_TIME": "20150831181454",
     * "PHONE_NUMBER": "18310118001",
     * "CUSTOMER_PASSWORD": "e10adc3949ba59abbe56e057f20f883e",
     * "CUSTOMER_NAME": null,
     * "CUSTOMER_AGE": null,
     * "CUSTOMER_SEX": null,
     * "CUSTOMER_ID_NUMBER": null,
     * "config": null
     * }
     */
    public String TERMINAL_ID;
    public String CUSTOMER_ID;
    public String NOTE1;
    public String NOTE2;
    public String RECORD_TIME;
    public String PHONE_NUMBER;
    public String CUSTOMER_PASSWORD;
    public String CUSTOMER_NAME;
    public String CUSTOMER_AGE;
    public String CUSTOMER_SEX;
    public String CUSTOMER_ID_NUMBER;
    public String config;
    public String lgtime;
    public String MYMAXTIME;
    public String INTERVAL_ID;
    public String SERVICEMAXTIME;
    public String INTERVAL_TIME;
    public String INTERVAL_DESC;//前端默认时间间隔，回复超时N分钟，切回小壹
    public String USED_FALG;
    public String ip;
    public int isVIP = 0;//0表示不是  1表示是VIP

    public int PUSH_FLAG;
    public int LOCATION_FLAG;
    public int HEALTHY_FLAG;
    public int SPORT_FLAG;
    public int NOTE;
    private JSONArray merchantArr;//绑定的商户信息

    public JSONArray getMerchantArr() {
        return merchantArr;
    }

    public void setMerchantArr(JSONArray merchantArr) {
        this.merchantArr = merchantArr;
    }

    public int getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(int isVIP) {
        this.isVIP = isVIP;
    }

    /**
     * 无账号密码登陆
     * "code": "1",
     * "message": "登录成功",
     * "server_params": {
     * "lgtime": 1447290988284,
     * "TERMINALID": "384fb524-c2b2-42a8-824f-dd68cbfc9118",
     * "list": [
     * {
     * "INTERVAL_ID": 10001,
     * "INTERVAL_TIME": 2,
     * "INTERVAL_DESC": "前端默认时间间隔，回复超时N分钟，切回小壹",
     * "USED_FALG": 1
     * }
     * ],
     * "CUSTOMER_ID": "921035",
     * "MYMAXTIME": "",
     * "SERVICEMAXTIME": "",
     * "idfa": "",
     * "ip": "111.206.125.40"
     * },
     * "server_code": "10001"
     * }
     * <p/>
     * 手机号码登陆
     * <p/>
     * ║ {
     * ║     "code": "1",
     * ║     "message": "登录成功",
     * ║     "server_params": {
     * ║         "TERMINAL_ID": "4E24487A-6B33-43B8-833C-83985A0A72B0",
     * ║         "CUSTOMER_ID": "225239",
     * ║         "NOTE1": "20",
     * ║         "NOTE2": null,
     * ║         "RECORD_TIME": "20150908125657",
     * ║         "PHONE_NUMBER": "15810262908",
     * ║         "CUSTOMER_PASSWORD": "e10adc3949ba59abbe56e057f20f883e",
     * ║         "CLIENT_CODE": null,
     * ║         "LNG": null,
     * ║         "LAT": null,
     * ║         "IDFA": null,
     * ║         "IP": null,
     * ║         "TERMINAL_TYPE": null,
     * ║         "CUSTOMER_NAME": null,
     * ║         "CUSTOMER_AGE": null,
     * ║         "CUSTOMER_SEX": null,
     * ║         "CUSTOMER_ID_NUMBER": null,
     * ║         "PREGNANT_WEEK": 0,
     * ║         "INVALID_WEIXIN_ID": null,
     * ║         "WEIXIN_ID": null,
     * ║         "WEIXIN_PUBLIC_ID": null,
     * ║         "VERSION": null,
     * ║         "VERSION_TIME": null,
     * ║         "config": null,
     * ║         "lgtime": 1449140287283,
     * ║         "SERVICEMAXTIME": "",
     * ║         "MYMAXTIME": "",
     * ║         "list": [
     * ║             {
     * ║                 "INTERVAL_ID": 10001,
     * ║                 "INTERVAL_TIME": 5,
     * ║                 "INTERVAL_DESC": "前端默认时间间隔，回复超时N分钟，切回小壹",
     * ║                 "USED_FALG": 1
     * ║             }
     * ║         ],
     * ║         "ip": "221.204.170.33"
     * ║     },
     * ║     "server_code": "10001"
     * ║ }
     *
     * @param json
     * @return
     */
    public static User parseFormat(JSONObject json) {
        User user = new User();
        try {
            user.lgtime = HStringUtil.stringFormat(json.optString("lgtime"));
            user.MYMAXTIME = HStringUtil.stringFormat(json.optString("MYMAXTIME"));
            user.SERVICEMAXTIME = HStringUtil.stringFormat(json.optString("SERVICEMAXTIME"));
            user.ip = HStringUtil.stringFormat(json.optString("ip"));
            user.isVIP = json.optInt("isVIP", 0);
            JSONArray array = json.optJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.optJSONObject(i);
                if (jsonObject.optInt("INTERVAL_ID", -1) == 10001) {
                    user.INTERVAL_ID = HStringUtil.stringFormat(jsonObject.optString("INTERVAL_ID"));
                    user.INTERVAL_TIME = HStringUtil.stringFormat(jsonObject.optString("INTERVAL_TIME"));
                    user.INTERVAL_DESC = HStringUtil.stringFormat(jsonObject.optString("INTERVAL_DESC"));
                    user.USED_FALG = HStringUtil.stringFormat(jsonObject.optString("USED_FALG"));
                }
            }
            user.merchantArr = json.optJSONArray("centerMesg");//解析已经绑定的商户信息
            user.TERMINAL_ID = HStringUtil.stringFormat(json.optString("TERMINAL_ID"));
            user.CUSTOMER_ID = HStringUtil.stringFormat(json.optString("CUSTOMER_ID"));
            user.NOTE1 = HStringUtil.stringFormat(json.optString("NOTE1"));
            user.NOTE2 = HStringUtil.stringFormat(json.optString("NOTE2"));
            user.RECORD_TIME = HStringUtil.stringFormat(json.optString("RECORD_TIME"));
            user.PHONE_NUMBER = HStringUtil.stringFormat(json.optString("PHONE_NUMBER"));
            user.CUSTOMER_PASSWORD = HStringUtil.stringFormat(json.optString("CUSTOMER_PASSWORD"));
            user.CUSTOMER_NAME = HStringUtil.stringFormat(json.optString("CUSTOMER_NAME"));
            user.CUSTOMER_AGE = HStringUtil.stringFormat(json.optString("CUSTOMER_AGE"));
            user.CUSTOMER_SEX = HStringUtil.stringFormat(json.optString("CUSTOMER_SEX"));
            user.CUSTOMER_ID_NUMBER = HStringUtil.stringFormat(json.optString("CUSTOMER_ID_NUMBER"));
            user.config = HStringUtil.stringFormat(json.optString("config"));
            if (!HStringUtil.isEmpty(user.config)) {
                JSONObject js = new JSONObject(user.config);
                user.PUSH_FLAG = js.optInt("PUSH_FLAG", 0);
                user.LOCATION_FLAG = js.optInt("LOCATION_FLAG", 0);
                user.HEALTHY_FLAG = js.optInt("HEALTHY_FLAG", 0);
                user.SPORT_FLAG = js.optInt("SPORT_FLAG", 0);
                user.NOTE = js.optInt("NOTE", 0);
            }
            user.isKF = HStringUtil.stringFormat(json.optString("havSer"));
        } catch (Exception e) {
            return null;
        }
        return user;
    }


    public String isKF;
}
