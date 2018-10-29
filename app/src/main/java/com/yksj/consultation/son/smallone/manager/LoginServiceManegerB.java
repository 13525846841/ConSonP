package com.yksj.consultation.son.smallone.manager;


import com.yksj.consultation.comm.SOConfigs;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.smallone.bean.User;
import com.yksj.consultation.son.smallone.event.LoginEventB;
import com.yksj.consultation.son.smallone.service.CoreServiceB;
import com.yksj.consultation.son.smallone.socket.SocketCode;
import com.yksj.consultation.son.smallone.socket.SocketManagerB;
import com.yksj.healthtalk.net.http.HttpRestClientB;
import com.yksj.healthtalk.net.socket.IMManager;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ThreadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


/**
 * 小壹
 *
 * @author jack_tang
 */
public class LoginServiceManegerB extends IMManager {

    private final String TAG = "LoginServiceManegerB";
    public LoginEventB.Event loginState = LoginEventB.Event.NONE;//登录状态-1未登陆,0登录中,1登录成功,2资料加载完成
    SocketManagerB manager = SocketManagerB.init();
    private String userName;
    private String password;
    public String mactiviArray;//活动介绍内容
    /**
     * 单例模式
     */
    private static LoginServiceManegerB inst = new LoginServiceManegerB();
    private User mLoginEntity;

    public static LoginServiceManegerB instance() {
        return inst;
    }

    /**
     * /**
     * 接口名称：192.168.16.45:8899/DuoMeiHealth/    server_code  1001
     * 入参json：
     * ACCOUNT		账号或手机
     * PASSWORD	密码
     * IS_AFTER_MINIMIZATION_LOGIN  是否在最小化后登录 1-是 0-不是
     * IPADDRESS	ip地址
     * CLIENTONLY
     * IDFA		（积分墙用）
     */
    public synchronized void login(String userName, String password) {
        this.userName = userName;
        this.password = password;
        login();
    }

    public synchronized void loginEmpty() {
        this.userName = "";
        this.password = "";

        login();
    }


    public void clearAccountPswd() {
        this.userName = "";
        this.password = "";
    }


    private JSONObject initConfig(JSONObject object) {
        try {
            JSONObject json1 = new JSONObject();
            json1.put("ProjectVersion", SOConfigs.VERSION_SYS);
            json1.put("OSName", "Android");
            json1.put("OSSYstemVersion", android.os.Build.VERSION.RELEASE);
            json1.put("OSDeviceName", android.os.Build.MODEL);
//            String str=ctx.getResources().getConfiguration().locale.toString();
            String str= Locale.getDefault().getCountry();
            json1.put("OSLocale", str);
            object.put("client_info", json1);
        } catch (JSONException e) {
            return null;
        }
        return object;
    }

    public synchronized void login() {
//        if (!"".equals(password) && password.length() <= 12) {
//            this.password = MD5Utils.getMD5(password);
//        }
        String state;
        if (loginState == LoginEventB.Event.LOGIN_OK) {
            state = "0";
        } else {
            state = "1";
        }
        loginState = LoginEventB.Event.LOGINING;
        if (manager.isConnected()) {
            try {
                JSONObject params = new JSONObject();
                params.put("phone", userName);
                params.put("customerPassword", password);
                params.put("terminalidtype", "1");
                params.put("isClient", "1");
                params.put("MERCHANT_ID", SOConfigs.MERCHANT_ID);
                params.put("TERMINALID", HTalkApplication.getDeviceNumber());
                params.put("isReconnect", state);
                SocketManagerB.sendSocketParams(initConfig(params), SocketCode.LOGIN_CODE);
            } catch (JSONException e) {
            }
        } else {
            manager.connect();
        }
    }

    /**
     * 退出
     */
    public synchronized void loginOut() {
        loginState = LoginEventB.Event.NONE;
        if (manager.isConnected()) {
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    manager.disConnect();
                }
            });
        }
    }

    /**
     * 登出
     */
    public synchronized void loginOutAccount() {
        mLoginEntity.PHONE_NUMBER = "";
        mLoginEntity.CUSTOMER_PASSWORD = "";
    }

    public User getLoginEntity() {
        return mLoginEntity;
    }

    public String getLoginId() {
        return mLoginEntity == null ? null : mLoginEntity.CUSTOMER_ID;
    }

    /**
     * jo.put("code", code);//成功 失败
     * jo.put("message", mesg);//提示消息
     * jo.put("server_params", result);//
     */
    public void setLoginInfo(JSONObject jo) {
        if (0 == jo.optInt("code")) {//异地登陆
//            SharePreUtils.saveUserStatues(false, ctx);
            //   KLog.d("==登陆失败");
            CoreServiceB.actionLoginError(ctx, jo);
        } else if (loginState == LoginEventB.Event.LOGINING) {//  客户资料
            if (1 == jo.optInt("code")) {//登陆成功 解析客户资料
                //    KLog.d("======登陆成功");
                loginState = LoginEventB.Event.LOGIN_OK;
                JSONObject infoJson = jo.optJSONObject("server_params");
                mactiviArray = infoJson.optString("huodong");
                //   KLog.json(infoJson.toString());
                mLoginEntity = User.parseFormat(infoJson);
                CoreServiceB.actionTalkTimeOut(ctx, mLoginEntity.INTERVAL_TIME);
//                SharePreUtils.saveUserStatues(!TextUtils.isEmpty(userName), ctx);
                // MessageServiceManager.instance().loadOffLineMsg();//加载离线消息
                CoreServiceB.actionLoginSuccess(ctx);
                HttpRestClientB.addHttpHeader("username", mLoginEntity.PHONE_NUMBER);
                HttpRestClientB.addHttpHeader("password", mLoginEntity.CUSTOMER_PASSWORD);
                HttpRestClientB.addHttpHeader("os_type", "1");
                HttpRestClientB.addHttpHeader("CUSTOMER_ID", mLoginEntity.CUSTOMER_ID);
//                getTagFromHttp();
                ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        sendChangeDeviceNumber();
                    }
                });
            }

        }
    }


    @Override
    public void doOnStart() {

    }

    @Override
    public void reset() {

    }

    public LoginEventB.Event getLoginState() {
        return loginState;
    }

    public void setLoginState(LoginEventB.Event loginState) {
        this.loginState = loginState;
    }


//    private void getLocation(String[] location) {
//        KLog.d("======获取天气信息");
//        RequestParams params = new RequestParams();
//        params.put("Type", "updateClientCode");
//        params.put("LNG", location[0]);
//        params.put("LAT", location[1]);
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        HttpRestClient.doHttpcreateOrder(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
////                KLog.json(response.toString());
//                if (response.optInt("code") == 1) {
//                    MyEvent event = new MyEvent(response.toString(), 119);
//                    EventBus.getDefault().post(event);
//                }
//            }
//        });
//    }

    //false 未登录  true 已登陆
    public static boolean isLogined() {
        return !isNoLogin();
    }


    public static boolean isNoLogin() {
        if (LoginServiceManegerB.instance().getLoginEntity() == null || HStringUtil.isEmpty(LoginServiceManegerB.instance().getLoginEntity().PHONE_NUMBER))
            return true;
        return false;
    }

    public void setUser(User user) {
        mLoginEntity = user;
    }

    public void setPassword(String password) {
//        SharePreUtils.saveAcctorPaswd(ctx, new String[]{userName, ""});
        this.password = password;
    }

    public void setUserName(String userName) {
//        SharePreUtils.saveAcctorPaswd(ctx, new String[]{"", ""});
        this.userName = userName;
    }

    /**
     * 登陆成功之后发送(改变串号)
     */
    public void sendChangeDeviceNumber() {
        JSONObject params = new JSONObject();
        try {
            params.put("CUSTOMER_ID", mLoginEntity.CUSTOMER_ID);
            params.put("server_params", SystemUtils.getDeviceId(ctx));
            SocketManagerB.sendSocketParams(params, SocketCode.LOGINED_SEND);
        } catch (JSONException e) {
            //   KLog.d("串号发送失败");
        }
    }

//    public void getTagFromHttp() {
//        //http://220.194.46.204/dmys/xyMenu?Type=initMenu&CUSTOMER_ID=
//        RequestParams params = new RequestParams();
//        params.put("Type", "initMenu");
//        params.put("CUSTOMER_ID", getLoginId());
//        HttpRestClient.doHttpXYMENU(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(final JSONObject response) {
//                super.onSuccess(response);
//                if (response.optInt("code") == 1) {
//
//                    ThreadManager.getInstance().createLongPool().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            TagIndexEntity.parseToList(response.optString("result"));
//                            EventBus.getDefault().post(new TagEvent());
//                        }
//                    });
//
//                } else {
//                    ToastUtil.showShort(response.optString("message"));
//                }
//            }
//        });
//    }
}
