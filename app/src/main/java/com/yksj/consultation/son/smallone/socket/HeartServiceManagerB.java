package com.yksj.consultation.son.smallone.socket;


import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.smallone.manager.LoginServiceManegerB;
import com.yksj.healthtalk.net.socket.IMManager;
import com.yksj.healthtalk.utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 心跳处理
 *
 * @author jack_tang
 */
public class HeartServiceManagerB extends IMManager {

    /**
     * 单例模式
     */
    private static HeartServiceManagerB inst = new HeartServiceManagerB();
    SocketManagerB mSocketManagerB = SocketManagerB.init();

    public static HeartServiceManagerB instance() {
        return inst;
    }

    @Override
    public void doOnStart() {
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    public void sendHeartMsg() {
        JSONObject params = new JSONObject();
        try {
            params.put("APP_KODE", HTalkApplication.mKey);
            params.put("TERMINALID", SystemUtils.getIMEI(ctx));
            if (LoginServiceManegerB.instance().getLoginId() != null)
                params.put("CUSTOMERID", LoginServiceManegerB.instance().getLoginId());
            mSocketManagerB.sendSocketParams(params, SocketCode.SOCKET_HEART_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
