package com.yksj.consultation.son.consultation.avchat.login;


import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.common.NimUIKit;

/**
 * 注销帮助类 网易云
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
//        removeLoginState();
        NIMClient.getService(AuthService.class).logout();
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        DemoCache.clear();
        LoginSyncDataStatusObserver.getInstance().reset();
    }

}
