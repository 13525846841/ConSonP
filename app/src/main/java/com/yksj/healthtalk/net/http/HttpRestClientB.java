package com.yksj.healthtalk.net.http;

import android.content.Context;

import java.util.Map;

/**
 * 所有执行http请求操作调用此类
 *
 * @author zhao
 */

public class HttpRestClientB {

    private static HttpUrlsB mHttpUrlsB;
    public static AsyncHttpClient mAsyncHttpClient = new AsyncHttpClient(true);
    public static String mDeviceId;//设备包名
    public static String mKey = "58781ed63f807a8f5d157f2180d95e82";

    public static Map<String, String> getDefaultHeaders() {
        return mAsyncHttpClient.getHeaders();
    }

    public static void getDeviceId(Context context) {
//		mDeviceId = SystemUtils.getIMEI(context);
        mDeviceId = context.getApplicationInfo().packageName;
    }


    public static HttpUrlsB getmHttpUrlsB() {
        return mHttpUrlsB;
    }

    public static void setmHttpUrlsB(HttpUrlsB mHttpUrlsB) {
        HttpRestClientB.mHttpUrlsB = mHttpUrlsB;
    }

    public static void addHttpHeader(String key, String value) {
        if (mAsyncHttpClient != null) {
            mAsyncHttpClient.addHeader(key, value);
        }
    }

    /**
     * 虚拟医生
     *
     * @param handler
     */
    public static void doHttpVirtualDoctor(org.json.JSONObject jsonObject,
                                           AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("server_parame", jsonObject.toString());
        params.put("appId", mDeviceId);//
        params.put("key", mKey);
        mAsyncHttpClient.post(mHttpUrlsB.URL_MMS_SERVLET300, params, handler);
    }

    /**
     * 六一健康六一博士
     *
     * @param handler
     */
    public static void doHttpSODoctor(org.json.JSONObject jsonObject, String id, String content,
                                      AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("customer_id", id);
        params.put("merchant_id", "6");
        params.put("batch_code", "1");
        params.put("Type", "talk");
        params.put("site_id", "4");
        params.put("content", content);
        mAsyncHttpClient.post(mHttpUrlsB.URL_MMS_SERVLET61, params, handler);
    }
    /**
     * 六一健康六一博士
     *
     * @param handler
     */
    public static void doHttpSOWel(AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("MERCHANT_ID", "6");
        params.put("Type", "XiaoYi_chat_his_pb");
        mAsyncHttpClient.post(mHttpUrlsB.URL_MMS_SERVLET61, params, handler);
    }

    /**
     * 人体图症状内容查询
     *
     * @param codeId 症状编码id
     */
    public static void doHttpQuerySymptomContent(String codeId,
                                                 AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("TYPE", "2");
        params.put("SITUATIONCODES", codeId);
        params.put("appId", mDeviceId);
        params.put("key", mKey);
        mAsyncHttpClient.get(mHttpUrlsB.URL_QUERYSITUATION, params, handler);
    }

    /**
     * 虚拟人体查询
     *
     * @param verion
     */
    public static void doHttpQuerySymptom(String verion,
                                          AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("TYPE", "1");
        requestParams.put("VERSION", verion);
        requestParams.put("appId", mDeviceId);
        requestParams.put("key", mKey);
        mAsyncHttpClient.get(mHttpUrlsB.URL_QUERYSITUATION, requestParams, handler);
    }

    /**
     * 下载更新
     */
    public static void doHttpUpdate(AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("version", HApplication.getAppVersionName());
        requestParams.put("appId", mDeviceId);
        requestParams.put("key", mKey);
        mAsyncHttpClient.get(mHttpUrlsB.URL_DOWN_LOAD_URL, requestParams, handler);
    }

    public static void doHttpJUMPRECORD(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.JUMPRECORD, params, handler);

    }

    /**
     * 保持长连接
     */
    public static void doHttpkeepConnect(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.KEEPCONNECTION, params, handler);
    }

    public static void doHttpcusSuggestion(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.CUSSUGGESTION, params, handler);
    }

    public static void doHttpSENDCODE(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.SENDCODE, params, handler);
    }

    public static void doHttpREGIST(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.REGIST, params, handler);
    }

    public static void doHttpLOGIN(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.LOGIN, params, handler);
    }

    public static void doHttpUPDATECUSMESG(RequestParams params, JsonHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.UPDATECUSMESG, params, handler);
    }

    public static void doHttpSYSSETTING(RequestParams params, JsonHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.SYSSETTING, params, handler);
    }

    public static void doHttpUPDATEPHONENUM(RequestParams params, JsonHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.UPDATEPHONENUM, params, handler);
    }

    public static void doHttpUPDATEPSW(RequestParams params, JsonHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.UPDATEPSW, params, handler);

    }

    public static void doHttpCusevaluationDisease(RequestParams params, JsonHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.CUSEVALUATIONDISEASE, params, handler);
    }

    public static void doHttpXYWENZHENHIS(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.XYWENZHENHIS, params, handler);

    }

    public static void doHttpMESGHISTORY(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.MESGHISTORY, params, handler);
    }

    public static void doHttpINTELLIGENCEBALL(AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.INTELLIGENCEBALL, handler);
    }

    public static void doHttpFOGETPSW(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.FORGETPSW, params, handler);
    }

    public static void doHttpNEARBYHOSPITAL2(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.NEARBYHOSPITAL2, params, handler);
    }

    public static void doHttpUPBITMAP(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.NEARBYHOSPITAL2, params, handler);
    }


    //http://api.map.baidu.com/staticimage?markers=116.376785,39.95285&width=300&height=200&zoom=16&markerStyles=-1,http://api.map.baidu.com/images/marker_red.png,-1,23,25
    public static String getGoogleMapUrl(String lt, String ln) {
        return "http://api.map.baidu.com/staticimage?markers=" + lt + "," + ln + "&width=400&height=200&zoom=16&markerStyles=-1,http://api.map.baidu.com/images/marker_red.png,-1,23,25";
    }

    /**
     * 断开连接
     *
     * @param params
     * @param handler
     */
    public static void doHttpDISCONNECTSERVER(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.DISCONNECTSERVER, params, handler);
    }

    /**
     * 奖品 初始化
     *
     * @param params
     * @param handler
     */
    public static void doHttpINICHOUJIANG(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.INICHOUJIANG, params, handler);
    }

    public static void doHttpSHARECHOUJIANGREC(RequestParams params) {
        mAsyncHttpClient.get(mHttpUrlsB.SHARECHOUJIANGREC, params, null);
    }

    public static void doHttpLOADEDOFFLINEMESSAGE(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.LOADEDOFFLINEMESSAGE, params, handler);
    }

    public static void doHttpcreateOrder(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.CREATEORDER, params, handler);
    }

    public static void doHttpPAYMENTMESG(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.PAYMENTMESG, params, handler);
    }

    public static void doHttpXYMENU(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.XYMENU, params, handler);
    }

    public static void doHttpXYVIP(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.XYVIP, params, handler);
    }

    public static void doHttpFamilyList(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.FAMILYLIST, params, handler);
    }

    public static void doHttpProducts(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.PRODUCTS, params, handler);
    }

    public static void doHttpBindCard(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.BINDCARD, params, handler);
    }

    public static void doHttpBindCard2(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.xyMenu, params, handler);
    }

    public static void doHttpUpdateInfo(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.UPDATEINFO, params, handler);
    }

    public static void doHttpChange(RequestParams params, AsyncHttpResponseHandler handler) {
        mAsyncHttpClient.get(mHttpUrlsB.CHANGETALKER, params, handler);
    }

}




