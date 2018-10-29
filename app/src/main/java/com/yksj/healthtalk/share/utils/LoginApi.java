package com.yksj.healthtalk.share.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 15/11/23.
 * Used for third login
 */
public class LoginApi implements Handler.Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;

    private String platform;
    private Context context;
    private Handler handler;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void login(Context context) {
        this.context = context.getApplicationContext();
        if (platform == null) {
            return;
        }

        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj = new Object[]{plat.getName(), res};
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
                t.printStackTrace();
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
    }

    /**
     * 处理操作结果
     */
    public boolean handleMessage(Message msg) {
        EventBus.getDefault().post(new MyEvent("", 13));
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                ToastUtil.showShort("授权取消");
            }
            break;
            case MSG_AUTH_ERROR: {
                // 失败
//                Throwable t = (Throwable) msg.obj;
//                String text = "caught error: " + t.getMessage();
//                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//                t.printStackTrace();
                ToastUtil.showShort("授权失败");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                ToastUtil.showShort("授权成功");
                Object[] objs = (Object[]) msg.obj;
                String plat = (String) objs[0];
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];

                String plats=null;
                if (QQ.NAME.equals(plat)) {
                    plats = "QQ";
                } else if (SinaWeibo.NAME.equals(plat)) {
                    plats = "新浪";
                } else if (Wechat.NAME.equals(plat)) {
                    plats = "微信";
                }
                EventBus.getDefault().post(new String[]{ShareSDK.getPlatform(plat).getDb().getUserId(), plats, "0"});
            }
            break;
        }
        return false;
    }

}