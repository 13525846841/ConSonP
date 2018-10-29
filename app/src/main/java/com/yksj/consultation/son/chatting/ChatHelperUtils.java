package com.yksj.consultation.son.chatting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.BuyNumFramentDialog;
import com.yksj.consultation.comm.RegisterFramentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyConstant;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.SalonHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jack_tang on 15/7/27.
 * 聊天帮助类
 */
public class ChatHelperUtils {

    /**
     * 点击聊天
     * entity  对方实体(name id 必须有)
     */
    public static void chatFromPerson(final FragmentActivity activity,
                                      final CustomerInfoEntity entity) {
        if (entity != null && SmartFoxClient.helperId.equals(entity.getId())) {//系统通知
            Intent intent = new Intent();
            intent.setClass(activity, ChatActivity.class);
            intent.putExtra(ChatActivity.KEY_PARAME, entity);
            activity.startActivity(intent);
            return;
        }
        Intent intent = new Intent(activity, ChatActivity.class);

        intent.putExtra(ChatActivity.KEY_PARAME, entity);
        activity.startActivity(intent);
//        if (activity instanceof DoctorCommonServiceActivity
//                || activity instanceof DoctorTimeServiceActivity
//                || activity instanceof PayActivity
//                || activity instanceof ServiceAddInfoActivity) {
//            activity.finish();
//        }
//        HttpRestClient.doHttpInitChat(SmartFoxClient.getLoginUserId(), entity.getId(), new JsonsfHttpResponseHandler(activity) {
//            public void onSuccess(int statusCode, com.alibaba.fastjson.JSONObject response) {
//                super.onSuccess(statusCode, response);
//                if (!activity.isFinishing()) {
//                    String content;
//                    if ((content = JsonParseUtils.filterErrorMessage(response)) != null) {
//                        SingleBtnFragmentDialog.showDefault(activity.getSupportFragmentManager(), content);
//                    } else {
//                        String sendCode = response.getString("send_code");
//                        Intent intent = new Intent(activity, ChatActivity.class);
//                        if (!TextUtils.isEmpty(sendCode)) {
//                            intent.putExtra("NOTE", sendCode);
//                        }
//                        intent.putExtra(ChatActivity.KEY_PARAME, entity);
//                        activity.startActivity(intent);
//                        if (activity instanceof DoctorCommonServiceActivity
//                                || activity instanceof DoctorTimeServiceActivity
//                                || activity instanceof DoctorInterviewServiceActivity
//                                || activity instanceof PayActivity
//                                || activity instanceof ServiceAddInfoActivity) {
//                            activity.finish();
//                        }
//                    }
//                }
//            }
//        });
    }

    ;


    /**
     * 话题点击没列
     *
     * @param context
     * @param listener        需要实现的监听
     * @param fragmentManager
     * @param entity
     * @param isReturnInfo    是否返回资料(重新加载资料返回)
     * @return
     */
    public static void chatFromGroup(final Context context,
                                     final SalonSelectPaymentOptionActivity.OnBuyTicketHandlerListener listener,
                                     final FragmentManager fragmentManager,
                                     final GroupInfoEntity entity, final boolean isReturnInfo) {
        HttpRestClient.doHttpJoinGroupChating(SmartFoxClient.getLoginUserId(), entity.getId(),
                isReturnInfo ? "1" : "0", entity.getIsBL(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                        GroupInfoEntity infoEntity = null;
                        try {
                            String code = response.getString("CODE");

                            if (response.has("GROUPMESSAGE") && isReturnInfo) {
                                String str = response.getString("GROUPMESSAGE");
                                infoEntity = SalonHttpUtil.jsonAnalysisSalonEntity(str).get(0);
                            }

                            if ("-1".equals(code)) {// 收取门票
                                int dayMoney = 0;// 日票
                                int monthMoney = 0;// 月票
                                //已经抢到名额
                                if (response.has("PAY_ID")) {
                                    String titleName = context.getString(R.string.tishi);
                                    RegisterFramentDialog.showLodingDialog(fragmentManager, response.getString("MESSAGE"), titleName, response.getString("PAY_ID"), entity);
                                    return;
                                }
                                //开始抢名额
                                JSONArray array = response
                                        .getJSONArray("TICKET");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    int charge = jsonObject.getInt("CHARGING_STANDARDS");
                                    // int defineType
                                    // =jsonObject.getInt("DEFINE_TYPE");
                                    int ticketType = jsonObject.getInt("TICKET_TYPE");
                                    if (ticketType == 1) {// 日票
                                        dayMoney = charge;
                                    } else if (ticketType == 2) {// 月票
                                        monthMoney = charge;
                                    }
                                }
                                if (infoEntity != null) {
                                    BuyNumFramentDialog.showLodingDialog(fragmentManager, infoEntity,
                                            infoEntity.getName(), dayMoney, monthMoney);
                                } else {
                                    BuyNumFramentDialog.showLodingDialog(
                                            fragmentManager, entity,
                                            entity.getName(), dayMoney, monthMoney);
                                }
                            } else if ("-2".equals(code)) {
                                SingleBtnFragmentDialog.showDefault(fragmentManager, response.getString("MESSAGE"));
                            } else if ("0".equals(code)) {//{"CODE":"0","MESSAGE":"会诊结束，该会话已关闭"}
                                SingleBtnFragmentDialog.showDefault(fragmentManager, response.getString("MESSAGE"));
                            } else {// 不收门票
                                if (infoEntity != null) {
                                    listener.onTicketHandler("1", infoEntity);
                                } else {
                                    listener.onTicketHandler("1", entity);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        LodingFragmentDialog.dismiss(fragmentManager);
                        super.onFinish();
                    }

                    @Override
                    public void onStart() {
                        LodingFragmentDialog.showLodingDialog(fragmentManager, context.getResources());
                        super.onStart();
                    }
                });
    }

    /**
     * 点击聊天
     * entity  对方实体(name id 必须有)
     */
    public static void chatFromPerson(final FragmentActivity activity,
                                      String id, String name) {
        Intent intent = new Intent();
        intent.setClass(activity, ChatActivity.class);
        intent.putExtra(MyConstant.SERVICE_CHAT, id);
        intent.putExtra(MyConstant.SERVICE_CHAT_NAME, name);
        activity.startActivity(intent);

    }

    /**
     * 点击聊天
     * entity  对方实体(name id 必须有)
     */
    public static void chatFromPerson(final FragmentActivity activity,
                                      String id, String name, String orderId,String objectType) {
        Intent intent = new Intent();
        intent.setClass(activity, ChatActivity.class);
        intent.putExtra(MyConstant.SERVICE_CHAT, id);
        intent.putExtra(MyConstant.SERVICE_CHAT_NAME, name);
        intent.putExtra(ChatActivity.ORDER_ID, orderId);
        intent.putExtra(ChatActivity.OBJECT_TYPE, objectType);
        activity.startActivity(intent);

    }
    /**
     * 点击聊天
     * entity  对方实体(name id 必须有)
     */
    public static void chatFromPerson(final Activity activity,
                                      String id, String name, String orderId,String objectType) {
        Intent intent = new Intent();
        intent.setClass(activity, ChatActivity.class);
        intent.putExtra(MyConstant.SERVICE_CHAT, id);
        intent.putExtra(MyConstant.SERVICE_CHAT_NAME, name);
        intent.putExtra(ChatActivity.ORDER_ID, orderId);
        intent.putExtra(ChatActivity.OBJECT_TYPE, objectType);
        activity.startActivity(intent);

    }
}
