package com.yksj.consultation.son.smallone.manager;


import android.text.TextUtils;

import com.yksj.consultation.son.smallone.event.MesgEvent;
import com.yksj.consultation.son.smallone.event.MessagesEvent;
import com.yksj.consultation.son.smallone.service.CoreServiceB;
import com.yksj.consultation.son.smallone.socket.SocketCode;
import com.yksj.consultation.son.smallone.socket.SocketManagerB;
import com.yksj.consultation.son.smallone.view.RingPlayerB;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.socket.IMManager;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 消息处理
 *
 * @author jack_tang
 */
public class MessageServiceManagerB extends IMManager {

    /**
     * 单例模式
     */
    private static MessageServiceManagerB inst = new MessageServiceManagerB();
    SocketManagerB mSocketManagerB = SocketManagerB.init();
    private String TAG = MessageServiceManagerB.this.getClass().toString();
    public static final String KEY_CODE = "server_code";
    public static final String KEY_PARAME = "server_parame";

    public static MessageServiceManagerB instance() {
        return inst;
    }

    @Override
    public void doOnStart() {
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    public void receiverMsg(final JSONObject jo, int code) {
        switch (code) {
            case SocketCode.RECEIVE_MSG_CODE://接受对方消息
                /**
                 *  {
                 "sms_req_content": "fghjjjjv",
                 "sid": "686237",
                 "customerId": "225305",//发送方
                 "serverId": "686237",
                 "sms_target_id": "226512",//接收方
                 "timeStamp": "1445326626675",
                 "server_code": 9019
                 */
                MessageEntity en = MessageEntity.parseImMesgToEntity(jo);
                onUpdateMesgCllection(en);
                MesgEvent mesgEvent = new MesgEvent(en, code);
                EventBus.getDefault().post(mesgEvent);
                RingPlayerB.playPressSound(ctx);
                break;
            case SocketCode.SEND_MSG_CODE://接受自己发送的消息
                break;
            case SocketCode.SERVICE_SWITCH_RESPONSE://客服要求切换机器人小壹
//                serviceChangeAutoDoctor(jo);
                if ("21".equals(jo.optString("type"))) {
                    serviceChangeAutoDoctor(jo);
                }
                break;
        }
    }


    /**
     * 消息实体,消息类型
     *
     * @param entity
     * @param msgtype
     */
    public void sendMsg(MessageEntity entity, int msgtype) {
        try {
            JSONObject params = new JSONObject();
            String mesgId = entity.getId();
            String tagertId = entity.getReceiverId();//接受者
            String customerId = entity.getSenderId();//发送方
            boolean isGroupMessage = false;
            String content = entity.getContent();
            params.put("serverId", mesgId);//消息返回状态id,与前台id进行对应
            params.put("sms_req_content", content);
            params.put("customerId", customerId);
            params.put("isGroupMessage", isGroupMessage ? 1 : 0);
            params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
            params.put("sms_target_id", tagertId);
            params.put("type", msgtype);
            params.put("duration", String.valueOf(0));
            //ValidFlag      --1-客户输入，2-客服输入的
            params.put("ValidFlag", 1);
            if (!TextUtils.isEmpty(entity.getSERVICE_TYPE()))
                params.put("SERVICE_TYPE", entity.getSERVICE_TYPE());
            else
                params.put("SERVICE_TYPE", "9999");
            if (SocketManagerB.mNbc.isOpen())
                SocketManagerB.sendSocketParams(params, SocketCode.SEND_MSG_CODE);
            else {//如果断开,就去登陆
                CoreServiceB.login(ctx);
                //   KLog.d("断开,就去登陆");
            }

        } catch (JSONException e) {
            //LogUtil.d(TAG, "=========消息解析失败,未发送");
        }
    }


    /**
     * 将消息存起来
     */
    private void onUpdateMesgCllection(MessageEntity entity) {
//        HApplication.getAppData().messageNoRead.add(entity);
    }


    /**
     * @"queryLixianxiaoxi",@"Type",customerId,@"CUSTOMER_ID",@"",@"serverid
     */
    public void loadOffLineMsg(String serverid) {
//        // KLog.d("======加载离线消息");
//        RequestParams params = new RequestParams();
//        params.put("Type", "queryLixianxiaoxiAll");
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("serverid", serverid);
//        HttpRestClient.doHttpLOADEDOFFLINEMESSAGE(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                //    KLog.d("======离线消息加载成功");
//                //    KLog.json(response.toString());
//                if (response.optInt("code", -1) == 1) {
//                    if (response.optJSONArray("result").length() != 0) {
//                        HApplication.getAppData().saveOffMsg(MessageEntity.parseOnOffline(response.optJSONArray("result")));
//                    }
//                    EventBus.getDefault().post(HApplication.getAppData());
//                    deletMsg();
//
//                } else {
//
//                    EventBus.getDefault().post(HApplication.getAppData());
//                    //ToastUtil.showShort(response.optString("message"));加载历史
//                }
//            }
//        });
    }

    /**
     * 删除离线消息
     */
    public void deletMsg() {

//        List<String> ids = HApplication.getAppData().getLeaveMsg();
//
//        for (int i = 0; i < ids.size(); i++) {
//            RequestParams params = new RequestParams();
//            params.put("Type", "deleteLixian");
//            params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//            params.put("offid", String.valueOf(Integer.MAX_VALUE));
//            params.put("sms_target_id", ids.get(i));
//            //  KLog.d("执行第一次删除离线消息i, 对方id==" + ids.get(i));
//            HttpRestClient.doHttpLOADEDOFFLINEMESSAGE(params, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, JSONObject response) {
//                    super.onSuccess(statusCode, response);
//                    //   KLog.d("第一次离线消息删除返回");
//                    //  KLog.json(response.toString());
//                }
//            });
//        }
    }

    public void orderReciviMsg(JSONObject j, int code) {
//        RingPlayer.playPressSound(ctx);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("VALID_FLAG", "2");
//
//            jsonObject.put("Customerid", j.optString("Customerid"));
//            jsonObject.put("vSendCode", "");
//            jsonObject.put("PARA_NOTE_TIME", "");
//            jsonObject.put("offline_id", "");
//
//            JSONArray array = new JSONArray();
//            JSONObject json1 = new JSONObject();
//
//            JSONObject server_params = j.optJSONObject("server_params");
//            json1.put("identifier", "detailForm");
//            json1.put("cont", "去填写");
//            json1.put("type", "3");
//            json1.put("ORDER_ID", j.optString("ORDER_ID"));
//            json1.put("REGISTER_NAME", HStringUtil.stringFormat(server_params.optString("REGISTER_NAME")));
//            json1.put("REGISTER_AGE", HStringUtil.stringFormat(server_params.optString("REGISTER_AGE")));
//            json1.put("REGISTER_PHONE", HStringUtil.stringFormat(server_params.optString("REGISTER_PHONE")));
//            json1.put("REGISTER_NUMBER", HStringUtil.stringFormat(server_params.optString("REGISTER_NUMBER")));
//            json1.put("REGISTER_SEX", HStringUtil.stringFormat(server_params.optString("REGISTER_SEX")));
//            array.put(json1);
//
//            JSONObject json2 = new JSONObject();
//            json2.put("cont", "我需要您的个人信息来帮助您挂号");
//            json2.put("btns", array);
//            json2.put("btn_align", 0);
//            json2.put("btn_type", 0);
//            jsonObject.put("retlist", json2);
//
//            jsonObject.put("retJBTZGN", "");
//            jsonObject.put("ltalkname", "");
//            jsonObject.put("manflag", "");
//            jsonObject.put("retDiseaseName", "");
//            jsonObject.put("retCode", "");
//            jsonObject.put("retserial", "");
//            jsonObject.put("vpage", "");
//            jsonObject.put("modify_Content", "");
//            jsonObject.put("retClass", "");
//            jsonObject.put("zjbb_jump", "");
//            jsonObject.put("customer_info", "");
//            jsonObject.put("push_code", code);
////            HApplication.getAppData().saveToAutoContent(jsonObject);
//            MesgEvent mesgEvent = new MesgEvent(jsonObject, code);
//            EventBus.getDefault().post(mesgEvent);
//        } catch (JSONException e) {
//        }
    }


    /**
     * {
     * "message": "查询成功",
     * "result": [
     * {
     * "content": "{\"ORDER_ID\":10122,\"CREATE_TIME\":\"20151118132521\",\"SERVICE_ID\":883797,\"CUSTOMER_ID\":979612,\"REGISTER_ID\":null,\"REGISTER_SYSTEM\":\"114\",\"REGISTER_NAME\":null,\"REGISTER_SEX\":null,\"REGISTER_AGE\":null,\"REGISTER_NUMBER\":null,\"REGISTER_PHONE\":null,\"REGISTER_HOSPITAL\":\"北大人民医院\",\"REGISTER_OFFICE\":\"内科\",\"REGISTER_DOCTOR\":null,\"REGISTER_DATE\":\"2015.11.21 下午\",\"REGISTER_PRICE\":null,\"ACTUAL_PRICE\":0,\"PAY_OK_TIME\":null,\"PAY_TYPE\":0,\"PAY_ID\":\"2151118001132406\",\"PAY_ACCOUNT\":null,\"TRANSACTION_ID\":null,\"ORDER_STATUS\":10,\"STATUS_TIME\":\"20151118132453\",\"REGISTER_TYPE\":\"普通门诊\"}",
     * "isGroupMessage": 0,
     * "duration": 0,
     * "customerId": "883797",
     * "timeStamp": "1447824379539",
     * "dataHolder": "",
     * "fileName": "",
     * "keyWords": [
     * <p/>
     * ],
     * "targetCustomerId": "979612",
     * "self": true,
     * "groupid": "0",
     * "serverId": "15965",
     * "type": 5
     * }
     * ],
     * "code": "1"
     * }
     *
     * @param j
     */
//    public void offLineMsgPushOrder(JSONObject j) {
//        RingPlayer.playPressSound(ctx);
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("VALID_FLAG", "2");
//
//            jsonObject.put("Customerid", j.optString("customerId"));
//            jsonObject.put("vSendCode", "");
//            jsonObject.put("PARA_NOTE_TIME", "");
//            jsonObject.put("offline_id", "");
//
//            JSONArray array = new JSONArray();
//            JSONObject json1 = new JSONObject();
//
//            JSONObject server_params = j.optJSONObject("content");
//            json1.put("identifier", "detailForm");
//            json1.put("cont", "去填写");
//            json1.put("type", "3");
//            json1.put("ORDER_ID", j.optString("ORDER_ID"));
//            json1.put("REGISTER_NAME", HStringUtil.stringFormat(server_params.optString("REGISTER_NAME")));
//            json1.put("REGISTER_AGE", HStringUtil.stringFormat(server_params.optString("REGISTER_AGE")));
//            json1.put("REGISTER_PHONE", HStringUtil.stringFormat(server_params.optString("REGISTER_PHONE")));
//            json1.put("REGISTER_NUMBER", HStringUtil.stringFormat(server_params.optString("REGISTER_NUMBER")));
//            json1.put("REGISTER_SEX", HStringUtil.stringFormat(server_params.optString("REGISTER_SEX")));
//            array.put(json1);
//
//            JSONObject json2 = new JSONObject();
//            json2.put("cont", "我需要您的个人信息来帮助您挂号");
//            json2.put("btns", array);
//            json2.put("btn_align", 0);
//            json2.put("btn_type", 0);
//            jsonObject.put("retlist", json2);
//
//            jsonObject.put("retJBTZGN", "");
//            jsonObject.put("ltalkname", "");
//            jsonObject.put("manflag", "");
//            jsonObject.put("retDiseaseName", "");
//            jsonObject.put("retCode", "");
//            jsonObject.put("retserial", "");
//            jsonObject.put("vpage", "");
//            jsonObject.put("modify_Content", "");
//            jsonObject.put("retClass", "");
//            jsonObject.put("zjbb_jump", "");
//            jsonObject.put("customer_info", "");
//            MesgEvent mesgEvent = new MesgEvent(jsonObject, SocketCode.SERVICE_PUSH_ORDER);
//            EventBus.getDefault().post(mesgEvent);
//        } catch (JSONException e) {
//        }
//    }

    /**
     * {
     * "ValidFlag": 2,
     * "sms_req_content": "touteng",
     * "sid": "723346",
     * "customerId": "225305",
     * "server_code": "9017",
     * "timeStamp": "1448863575389",
     * "sms_target_id": "228053",
     * "serverId": "723346",
     * "SERVICE_TYPE": "99999",
     * "type": 21,
     * "timeout": "5"
     * }
     */
    private void serviceChangeAutoDoctor(JSONObject jsonObject) {
//        HApplication.getAppData().saveToAutoContent(jsonObject);
        EventBus.getDefault().post(new MessagesEvent(jsonObject.optString("sms_req_content"), MessagesEvent.Event.SERVICE_SWITCH_RESPONSE));
    }

}
