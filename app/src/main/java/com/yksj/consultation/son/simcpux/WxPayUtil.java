package com.yksj.consultation.son.simcpux;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yksj.consultation.son.smallone.TyJsonObject;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONObject;

public class WxPayUtil {
    private IWXAPI api;
    private Activity con;
    private JSONObject mJson;

    public WxPayUtil(Activity con, JSONObject object) {
        this.con = con;
        api = WXAPIFactory.createWXAPI(con, null);
        api.registerApp(Constants.APP_ID);
        this.mJson = object;
    }

    public void pay() {

        if (!api.isWXAppInstalled()) {
            ToastUtil.showShort("没有安装微信");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtil.showShort("当前版本不支持支付功能");
            return;
        }
        // Toast.makeText(con, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            TyJsonObject json = new TyJsonObject(mJson.toString());
            if (null != json) {
                PayReq req = new PayReq();
                req.appId = json.getString("appid");
                req.partnerId = json.getString("partnerid");
                req.prepayId = json.getString("prepayid");
                req.nonceStr = json.getString("noncestr");
                req.timeStamp = json.getString("timestamp");
                req.packageValue = json.getString("package");
                req.sign = json.getString("sign");
                req.extData = "app data"; // optional
                Toast.makeText(con, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            } else {
                Toast.makeText(con, "支付失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            //Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(con, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

