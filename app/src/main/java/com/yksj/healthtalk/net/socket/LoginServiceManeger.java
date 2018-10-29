package com.yksj.healthtalk.net.socket;

import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.BaiduLocationManager;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.avchat.login.LogoutHelper;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.DialogUtils;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ThreadManager;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 登陆,退出,
 *
 * @author jack_tang
 */
public class LoginServiceManeger extends IMManager implements BaiduLocationManager.LocationListenerCallBack {

    public boolean isVisitor = true;//默认是游客
    public LoginEvent loginState = LoginEvent.NONE;//登录状态-1未登陆,0登录中,1登录成功,2资料加载完成
    SocketManager manager = SocketManager.init();
    private final String imei = "";
    private String userName;
    private String password;
    private String userId;
    private String platformName;
    private String loginType;
    /**
     * 单例模式
     */
    private static LoginServiceManeger inst = new LoginServiceManeger();
    private CustomerInfoEntity mLoginEntity;

    public static LoginServiceManeger instance() {
        return inst;
    }

    /**
     * /**
     * 接口名称：192.168.16.45:8899/DuoMeiHealth/    server_code  1001
     * 入参json：
     * USERID		第三方授权id
     * PLATFORM_NAME	第三方平台名称
     * loginType	登录方式0正常登录 /1第三方登录
     * ACCOUNT		账号或手机
     * PASSWORD	密码
     * IS_AFTER_MINIMIZATION_LOGIN  是否在最小化后登录 1-是 0-不是
     * IPADDRESS	ip地址
     * CLIENTONLY
     * IDFA		（积分墙用）
     */
    public void login(String userName, String password, String userId, String platformName, String loginType) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.platformName = platformName;
        this.loginType = loginType;
        login();
    }


    public void login() {
        String IS_AFTER_MINIMIZATION_LOGIN = loginState == LoginEvent.LOGIN_OK ? "1" : "0";
        loginState = LoginEvent.LOGINING;
        if (manager.isConnected()) {
            SocketParams params = new SocketParams();
            params.put("ACCOUNT", userName);
            params.put("PASSWORD", password);
            params.put("USERID", userId);
            params.put("PLATFORM_NAME", platformName);
            params.put("FLAG", loginType);
            params.put("client_type", HTalkApplication.CLIENT_TYPE);
            params.put("CLIENTONLY", SystemUtils.getDeviceId(ctx));
            params.put("IS_AFTER_MINIMIZATION_LOGIN", IS_AFTER_MINIMIZATION_LOGIN);
            SocketManager.sendSocketParams(params, SmartControlClient.LOGIN_CODE);
        } else {
            manager.connect();
        }
    }


    /**
     * 退出
     */
    public synchronized void loginOut() {
        mLoginEntity = null;
        isVisitor = true;//设为游客
        SharePreUtils.updateLoginState(false);
        HTalkApplication.clearAll();
        loginState = LoginEvent.NONE;
        if (manager.isConnected()) {
            manager.disConnect();
        }
    }


    public synchronized void setLoginInfo(CustomerInfoEntity info) {
        if (info != null)
            this.mLoginEntity = info;
    }

    public CustomerInfoEntity getLoginEntity() {
        return mLoginEntity;
    }


    /**
     * jo.put("code", code);//成功 失败
     * jo.put("message", mesg);//提示消息
     * jo.put("server_params", result);//
     */
    public synchronized void setLoginInfo(final JSONObject jo) {
        if ("2".equals(jo.optString("code"))) {//异地登陆
            System.out.println(jo.optString("message"));
            manager.disConnect();
            ThreadManager.getInstance().createShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    CoreService.actionLoginError(ctx, jo);
                    DialogUtils.showLoginOutDialog2(ctx,ctx.getResources().getString(R.string.login_agin));
                }
            });
            LogoutHelper.logout();
        } else if (loginState == LoginEvent.LOGINING) {//  客户资料
            if (1 == jo.optInt("code")) {//登陆成功 解析客户资料
                loginState = LoginEvent.LOGIN_OK;
                //保存登录用户信息
                String parame = jo.optString("server_params");
                SharePreUtils.saveLoginUserInfo(parame);
                CustomerInfoEntity entity = DataParseUtil.jsonToCustomerInfo2(parame);
                if (entity != null) {
                    this.mLoginEntity = entity;
                    HttpRestClient.addHttpHeader("username", mLoginEntity.getUsername());
                    HttpRestClient.addHttpHeader("password", this.password);
                    HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
                }
                isVisitor = false;
                CoreService.actionLoginSuccess(ctx);
                onLocationClick();
            } else {//账号密码错误
                CoreService.actionLoginError(ctx, jo);
            }
        }
    }



    /**
     * 用户登录成功后定位他的地理位置
     *
     * @param v
     */
    BaiduLocationManager mBaiduManager;

    private void onLocationClick() {
        if (mBaiduManager == null) {
//			BaiduLocationManager.init(getActivity());
            mBaiduManager = BaiduLocationManager.getInstance(ctx);
            mBaiduManager.setCallBack(this);

        }
        mBaiduManager.startLocation();
    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void reset() {

    }

    public LoginEvent getLoginState() {
        return loginState;
    }

    public void setLoginState(LoginEvent loginState) {
        this.loginState = loginState;
    }

//    public String getLoginUserId() {
//        if (instance() != null) {
//            if (mLoginEntity != null) {
//                return mLoginEntity.getId();
//            } else {
//                reLogin();
////                login();
//                return getLoginUserId();
//            }
//        } else {
//            return "";
//        }
//
//    }

    public String getLoginUserId() {
        if (mLoginEntity != null) {
            return mLoginEntity.getId();
        } else {
            return "";
        }


    }

    private synchronized void reLogin() {
        String[] cache = SharePreUtils.fatchUserLoginCache();
        this.login(cache[0], cache[1], "", "", "0");
    }

    @Override
    public void locationListenerCallBack(double longitude, double latitude) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE", "updateCustomerBasic"));
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("CLIENTONLY", SystemUtils.getDeviceId(ctx)));
        pairs.add(new BasicNameValuePair("IDFA", ""));
        pairs.add(new BasicNameValuePair("LNG", longitude + ""));
        pairs.add(new BasicNameValuePair("LAT", latitude + ""));
        HttpRestClient.OKHttpOrderTip(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if ("1".equals(object.optString("code"))) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, this);
    }

    @Override
    public void locationListenerCallBackFaile() {

    }


    //false 未登录  true 已登陆
    public static boolean isLogined() {
        return !isNoLogin();
    }


    public static boolean isNoLogin() {
        if (LoginServiceManeger.instance().getLoginEntity() == null || HStringUtil.isEmpty(LoginServiceManeger.instance().getLoginEntity().PHONE_NUMBER))
            return true;
        return false;
    }
}
