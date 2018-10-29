//package com.yksj.consultation.son.smallone;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.dmsj.newask.Views.RippleLayout;
//import com.dmsj.newask.adapter.ChatInfoAdapter;
//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.InitListener;
//import com.iflytek.cloud.RecognizerListener;
//import com.iflytek.cloud.RecognizerResult;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechRecognizer;
//import com.iflytek.cloud.SpeechUtility;
//import com.squareup.picasso.Picasso;
//import com.yksj.consultation.comm.BaseFragmentActivity;
//import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
//import com.yksj.consultation.comm.LodingFragmentDialogB;
//import com.yksj.consultation.comm.SOConfigs;
//import com.yksj.consultation.son.R;
//import com.yksj.consultation.son.RingPlayer;
//import com.yksj.consultation.son.app.AppData;
//import com.yksj.consultation.son.app.HTalkApplication;
//import com.yksj.consultation.son.chatting.HumanBodyActivity;
//import com.yksj.consultation.son.consultation.bean.MyEvent;
//import com.yksj.consultation.son.consultation.main.AtyConsultMain;
//import com.yksj.consultation.son.smallone.bean.TagMenuEntity;
//import com.yksj.consultation.son.smallone.common.ApkInstaller;
//import com.yksj.consultation.son.smallone.event.LoginEventB;
//import com.yksj.consultation.son.smallone.event.MesgEvent;
//import com.yksj.consultation.son.smallone.event.MessagesEvent;
//import com.yksj.consultation.son.smallone.event.TalkEvent;
//import com.yksj.consultation.son.smallone.manager.LoginServiceManegerB;
//import com.yksj.consultation.son.smallone.manager.MessageServiceManagerB;
//import com.yksj.consultation.son.smallone.service.CoreServiceB;
//import com.yksj.consultation.son.smallone.socket.SocketCode;
//import com.yksj.consultation.son.smallone.ui.CommonWebUIActivity;
//import com.yksj.consultation.son.smallone.ui.DoctorChatMyinformation;
//import com.yksj.consultation.son.smallone.ui.TagPageFragment;
//import com.yksj.healthtalk.entity.MessageEntity;
//import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
//import com.yksj.healthtalk.net.http.HttpRestClientB;
//import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
//import com.yksj.healthtalk.net.http.RequestParams;
//import com.yksj.healthtalk.utils.Bimp;
//import com.yksj.healthtalk.utils.FileUtils;
//import com.yksj.healthtalk.utils.HStringUtil;
//import com.yksj.healthtalk.utils.JsonParser;
//import com.yksj.healthtalk.utils.PopUtil;
//import com.yksj.healthtalk.utils.ScreenUtil;
//import com.yksj.healthtalk.utils.SharePreUtils;
//import com.yksj.healthtalk.utils.SystemUtils;
//import com.yksj.healthtalk.utils.TimeUtil;
//import com.yksj.healthtalk.utils.ToastUtil;
//import com.yksj.healthtalk.utils.UnitUtil;
//import com.yksj.healthtalk.utils.WeakHandler;
//
//import org.handmark.pulltorefresh.library.PullToRefreshBase;
//import org.handmark.pulltorefresh.library.PullToRefreshListView;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * Created by HEKL on 16/5/11.
// * Used for
// */
//
//public class ChatActivityB extends BaseFragmentActivity implements View.OnClickListener {
//    public static final int TITLE_SETTINT = 1, TITLE_CHANGE = 2, LOGIN_OUT = 3, VOICE_TEXT = 4, TAKE_PHOTO = 5;
//    private final int RESULT_LOAD_IMAGE = 1;//拍照
//    private final int RESULT_LOAD_CAMERA = 10;//拍照
//    private final int CUT_PHOTO_REQUEST_CODE = 2;//剪裁
//    public static final int BIND_OK = 3;//成功绑定VIP后返回
//    private static final int JUMP_CUSTOMERINFO = 1001;//跳转页面
//    private static final long JUMP_TIMER = 2;//跳转间隔时间
//    private boolean IS_SPEECH = false;
//    public static ChatActivityB chatActivity;
//    private boolean IS_SHOW_B = false;
//    private PopupWindow mPop = null;
//    private View popView = null;
//    private LayoutInflater mInflater = null;
//    private ImageView chat_send_project, chat_send_img, chat_send_voice, chat_send_text, chart_change, chat_send_camera;
//    private EditText chat_input_text;
//    private PullToRefreshListView chat_info_lv;
//    private ListView mListView;
//    public JSONObject mActioncacheJson;//点击的那条返回的参数
//    JSONObject mJsonObject;//
//    String mRetserial = "";
//    public ChatInfoAdapter mChatAapter;
//    private boolean isLoginedAction = true;//是否已经尝试登录过
//    String mUserId = "";//发送者id
//    //  String mReciverId = "226512";//接收者id
////    String mReciverId = "225305";//接收者id
//    private String V_send_code = "0";
//    private TextView chat_speech_tv;
//    private WindowManager.LayoutParams windowLp;
//    private JSONArray merchantArr = null;//我绑定的商户
//    private Intent intent;//带有当前商户信息
//    private String merchant_name;
//    public static String merchant_head = "";
//    private String merchant_id = SOConfigs.MERCHANT_ID;
//    private String activate_code;
//    private LinearLayout chat_bottom_project;
//    private JSONArray fastSendArr = null;//展开项数据
//    private final String sendBitmapType = "1";
//    HashMap<String, String> mapID = new HashMap<String, String>();
//    WeakHandler wHandler = new WeakHandler();
//
//    ImageView chat_add;
//    LinearLayout linearLayoutChatPoject;
//    TextView rip_text;
//
//    class MJsonHttpResponseHandler extends JsonHttpResponseHandler {
//
//        final MessageEntity mEntity;
//
//        public MJsonHttpResponseHandler(MessageEntity entity) {
//            this.mEntity = entity;
//        }
//
//        public void onSuccess(int statusCode, JSONObject response) {//成功
//            if ("-1".equals(response.optString("kefuflag")) || "-1".equals(response.optString("kefuFlag"))) {
//                HTalkApplication.getApplication().mTalkServiceType = "";
//                HTalkApplication.getApplication().setMTalkServiceId("");
//            } else if (response.toString().contains("result")) {
//                JSONObject jsb = response.optJSONObject("result");
//                if (!"-1".equals(jsb.optString("kefuflag"))) {
//
//                    HTalkApplication.getApplication().mTalkServiceType = jsb.optString("retflag");
//                    HTalkApplication.getApplication().setMTalkServiceId(response.optString("redflag"));
//                    mapID.put(merchant_id, response.optString("redflag"));
//                    setTalkID(jsb.optString("redflag"));
//                }
//            } else {
//                HTalkApplication.getApplication().mTalkServiceType = response.optString("retflag");
//                HTalkApplication.getApplication().setMTalkServiceId(response.optString("redflag"));
//                mapID.put(merchant_id, response.optString("redflag"));
//                setTalkID(response.optString("redflag"));
//            }
//
//            if (!HStringUtil.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {
//                //CoreServiceB.actionTalkTimeOut(ChatActivityB.this, LoginServiceManeger.instance().getLoginEntity().INTERVAL_TIME);
//            }
//            mEntity.setSendState(MessageEntity.STATE_OK);
//            if (!isFinishing()) {
//                if (statusCode == 200 && response != null) onMessageReceive(response);
//            }
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            mListView.setSelection(mChatAapter.getCount());
//        }
//    }
//
//    class MJsonHttpResponseHandler61 extends JsonHttpResponseHandler {
//
//        final MessageEntity mEntity;
//
//        public MJsonHttpResponseHandler61(MessageEntity entity) {
//            this.mEntity = entity;
//        }
//
//        public void onSuccess(int statusCode, JSONObject response) {//成功
//                if (response != null) {
//                    final MessageEntity messageEntity = new MessageEntity();
//                    messageEntity.setSendState(MessageEntity.STATE_OK);
////                    messageEntity.setSenderId(mReciverId);
//                    messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//                    messageEntity.setReceiverId(mUserId);
//                    messageEntity.setType(MessageEntity.TYPE_TEXT);
//                    messageEntity.setSendFlag(false);
//                    String mContents=response.optString("content");
//                    mContents = mContents.replace("<\\/br>", "<br/>");
//                    mContents = mContents.replace("\\n", "<br/>");
//                    messageEntity.setContent(mContents);
//                    //  messageEntity.setContent(HStringUtil.chatRadom());
//                    messageEntity.setId("-1");
////                messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    messageEntity.setDate(response.optLong("message_time"));
//                    messageEntity.setMerchant_head(merchant_head);
//                    if (!TextUtils.isEmpty(messageEntity.getContent())) {
//                        //  mList.add(messageEntity);
////                        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
////                                .map(new Func1<Long, Object>() {
////                                    @Override
////                                    public Object call(Long aLong) {
////                                        mChatAapter.addData(messageEntity);
////                                        mListView.setSelection(mChatAapter.getCount());
////                                        return  null;
////                                    }
////                                }).subscribe();
//                        wHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mChatAapter.addData(messageEntity);
//                                mListView.setSelection(mChatAapter.getCount());
//                            }
//                        }, 1000);
//                    }
//            }
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            mListView.setSelection(mChatAapter.getCount());
//        }
//    }
//
//    /**
//     * 发送的数据有三种形式：文字、图片和语音
//     * 目前只有文字方式
//     */
//    public void onMessageSend(Object obj) {
//        String content = null;
//        if (obj instanceof String) {//文字
//            content = (String) obj;
//        } else if (obj instanceof Bitmap) {//图片
//            // TODO: 2016-3-29
//        } else if (obj instanceof File) {//语音
//
//        }
//        if (false) {//socket
//            //if (!TextUtils.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {//socket
//            MessageEntity msgEntity = new MessageEntity();
//            msgEntity.setId(String.valueOf(System.currentTimeMillis()));
//            msgEntity.setSenderId(LoginServiceManegerB.instance().getLoginId());
//            //msgEntity.setReceiverId(mReciverId);
////          msgEntity.setReceiverId("226512");
//            msgEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//            msgEntity.setType(MessageEntity.TYPE_TEXT);
//            msgEntity.setSendFlag(true);
//            msgEntity.setContent(content);
//            msgEntity.setSendState(MessageEntity.STATE_PROCESING);
//            msgEntity.setSERVICE_TYPE(HTalkApplication.getApplication().mTalkServiceType);
//            msgEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//            mChatAapter.onDataChange(msgEntity);
//            mListView.setSelection(mChatAapter.getCount());
//            EventBus.getDefault().post(msgEntity);
//        } else {//http
//            if ("".equals(content) && "".equals(mRetserial)) return;
//
//            try {
//                mJsonObject.put("Customerid", LoginServiceManegerB.instance().getLoginId());
//                if (content.length() == 0) {
//                    MessageEntity messageEntity = new MessageEntity();
//                    String retCode = mJsonObject.getString("retCode");
//                    if ("ZJBB001".equals(retCode) || "ZJBB0012".equals(retCode) || "ZJBB021".equals(retCode) || "ZJBB011".equals(retCode)) {
//                        mJsonObject.put("smsTEXT", "我没有其他症状了");
//                        messageEntity.setContent("我没有其他症状了");
//                    } else if ("ZJBB002".equals(retCode)) {
//                        mJsonObject.put("smsTEXT", "这个问题我清楚了");
//                        messageEntity.setContent("这个问题我清楚了");
//                    }
//                    JSONObject jsonObject = new JSONObject(mJsonObject.toString());
//                    messageEntity.setSendState(MessageEntity.STATE_PROCESING);
//                    messageEntity.setSenderId(mUserId);
//                    messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
////                  messageEntity.setReceiverId(mReciverId);
//                    messageEntity.setType(MessageEntity.TYPE_TEXT);
//                    messageEntity.setSendFlag(true);
//                    if (!"".equals(mRetserial)) {
//                        mJsonObject.put("smsTEXT", mRetserial + " " + "777");
//                        return;
//                    }
//                    JSONArray array = new JSONArray();
//                    array.put(jsonObject);
//                    array.put(mJsonObject);
//                    mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                    messageEntity.setContentJsonArray(array);
//                    messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    mChatAapter.onDataChange(messageEntity);
//                    mJsonObject.put("original_smstext", messageEntity.getContent());
//                    mJsonObject.put("Customerid", LoginServiceManegerB.instance().getLoginId());
//                    mJsonObject.put("merchantID", merchant_id);//商户ID
////                    HttpRestClientB.doHttpVirtualDoctor(mJsonObject, new MJsonHttpResponseHandler(messageEntity));
//                    String mContents=messageEntity.getContent();
//                    mContents = mContents.replace("<\\/br>", "<br/>");
//                    mContents = mContents.replace("\\n", "<br/>");
//                    HttpRestClientB.doHttpSODoctor(null, LoginServiceManegerB.instance().getLoginId(), mContents, new MJsonHttpResponseHandler61(messageEntity));
////                    HttpRestClientB.doHttpVirtualDoctor(mJsonObject, new MJsonHttpResponseHandler(messageEntity));
//                } else {
//                    mJsonObject.put("smsTEXT", content);
//                    JSONObject jsonObject = new JSONObject(mJsonObject.toString());
//                    mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                    if (!"".equals(mRetserial)) {
//                        mJsonObject.put("smsTEXT", mRetserial + " " + content);
//                    }
//                    JSONArray array = new JSONArray();
//                    array.put(jsonObject);
//                    array.put(mJsonObject);
//                    MessageEntity messageEntity = new MessageEntity();
//                    messageEntity.setSendState(MessageEntity.STATE_PROCESING);
//                    messageEntity.setSenderId(mUserId);
////                  messageEntity.setReceiverId(mReciverId);
//                    messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//                    messageEntity.setType(MessageEntity.TYPE_TEXT);
//                    messageEntity.setSendFlag(true);
//                    messageEntity.setMerchantId(merchant_id);//商户ID
//                    messageEntity.setContentJsonArray(array);
//                    messageEntity.setContent(content);
//                    messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    //SERVICE_TO_AUTO_CONTENT
//                    if (555 != mJsonObject.optInt("SERVICE_SWITCH_RESPONSE"))//表示转发给小壹机器人的内容,不需要切换
//                        mChatAapter.onDataChange(messageEntity);
//                    mJsonObject.put("original_smstext", messageEntity.getContent());
//                    //servicecustomerid
//                    mJsonObject.put("servicecustomerid", LoginServiceManegerB.instance().getLoginId());
//                    mJsonObject.put("merchantID", merchant_id);//商户ID
//                    mJsonObject.put("Customerid", LoginServiceManegerB.instance().getLoginId());//商户ID
////                    mChatInputFragment.caidanID = "";
////                    HttpRestClientB.doHttpVirtualDoctor(mJsonObject, new MJsonHttpResponseHandler(messageEntity));
//                    String mContents=messageEntity.getContent();
//                    mContents = mContents.replace("<\\/br>", "<br/>");
//                    mContents = mContents.replace("\\n", "<br/>");
//                    HttpRestClientB.doHttpSODoctor(null, LoginServiceManegerB.instance().getLoginId(), mContents, new MJsonHttpResponseHandler61(messageEntity));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 发送的数据有三种形式：文字、图片和语音
//     * 目前只有文字方式
//     */
//    public void onMessageSend(String content, String id) {
//        if (false) {//socket
////        if (!TextUtils.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {//socket
//            MessageEntity msgEntity = new MessageEntity();
//            msgEntity.setId(String.valueOf(System.currentTimeMillis()));
//            msgEntity.setSenderId(LoginServiceManegerB.instance().getLoginId());
//            // msgEntity.setReceiverId(mReciverId);
////            msgEntity.setReceiverId("226512");
//            msgEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//            msgEntity.setType(MessageEntity.TYPE_TEXT);
//            msgEntity.setSendFlag(true);
//            msgEntity.setContent(content);
//            msgEntity.setSendState(MessageEntity.STATE_PROCESING);
//            msgEntity.setSERVICE_TYPE(HTalkApplication.getApplication().mTalkServiceType);
//            msgEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//            mChatAapter.onDataChange(msgEntity);
//            mListView.setSelection(mChatAapter.getCount());
//            EventBus.getDefault().post(msgEntity);
//            return;
//        }
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(mJsonObject.toString());
//            mJsonObject.put("smsTEXT", content);
//
//            mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//            if (!"".equals(mRetserial)) {
//                mJsonObject.put("smsTEXT", mRetserial + " " + content);
//            }
//            JSONArray array = new JSONArray();
//            array.put(jsonObject);
//            array.put(mJsonObject);
//            MessageEntity messageEntity = new MessageEntity();
//            messageEntity.setSendState(MessageEntity.STATE_PROCESING);
//            messageEntity.setSenderId(mUserId);
////                    messageEntity.setReceiverId(mReciverId);
//            messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//            messageEntity.setType(MessageEntity.TYPE_TEXT);
//            messageEntity.setSendFlag(true);
//            messageEntity.setMerchantId(merchant_id);//商户ID
//            messageEntity.setContentJsonArray(array);
//            messageEntity.setContent(content);
//            messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//            //SERVICE_TO_AUTO_CONTENT
//            if (555 != mJsonObject.optInt("SERVICE_SWITCH_RESPONSE"))//表示转发给小壹机器人的内容,不需要切换
//                mChatAapter.onDataChange(messageEntity);
//            mJsonObject.put("original_smstext", messageEntity.getContent());
//            //servicecustomerid
//            mJsonObject.put("servicecustomerid", LoginServiceManegerB.instance().getLoginId());
//            mJsonObject.put("merchantID", merchant_id);//商户ID
//            mJsonObject.put("caidanID", id);//商户ID
////          mChatInputFragment.caidanID = "";
//            HttpRestClientB.doHttpVirtualDoctor(mJsonObject, new MJsonHttpResponseHandler(messageEntity));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param content 文字
//     * @param json    发送数据
//     */
//    public void sendMessage(JSONObject json, String content) {
//        try {
//            mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//            JSONObject jsonObject = new JSONObject(json.toString());
//            JSONArray array = new JSONArray();
//            array.put(jsonObject);
//            array.put(json);
//            MessageEntity messageEntity = new MessageEntity();
//            messageEntity.setSendState(MessageEntity.STATE_PROCESING);
//            messageEntity.setSenderId(mUserId);
////            messageEntity.setReceiverId(mReciverId);
//            messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//            messageEntity.setType(MessageEntity.TYPE_TEXT);
//            messageEntity.setSendFlag(true);
//            messageEntity.setContentJsonArray(array);
//            messageEntity.setContent(content);
//            messageEntity.setMerchantId(merchant_id);
//            messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//            mChatAapter.onDataChange(messageEntity);
//            json.put("original_smstext", content);
//            json.put("merchantID", merchant_id);
////            HttpRestClientB.doHttpVirtualDoctor(json, new MJsonHttpResponseHandler(messageEntity));
//            String mContents=messageEntity.getContent();
//            mContents = mContents.replace("<\\/br>", "<br/>");
//            mContents = mContents.replace("\\n", "<br/>");
//            HttpRestClientB.doHttpSODoctor(null, LoginServiceManegerB.instance().getLoginId(), mContents, new MJsonHttpResponseHandler61(messageEntity));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 消息接收处理
//     *
//     * @param jsonObject
//     */
//    private void onMessageReceive(JSONObject jsonObject) {
//        try {
//            jsonObject.put("receive_time", TimeUtil.getCurrentTimeAsNumber());
//            mActioncacheJson = jsonObject;
//            //直接跳转导医护士
//            if (3 == jsonObject.optInt("retflag")) return;
//            mJsonObject.put("zjbb_jump", jsonObject.getString("zjbb_jump"));
//            mJsonObject.put("smsfeature", jsonObject.getString("retfeature"));
//            mJsonObject.put("smsClass", jsonObject.getString("retClass"));
//            mJsonObject.put("smsCode", jsonObject.getString("retCode"));
//            mJsonObject.put("vpage", jsonObject.getString("vpage"));
//            mJsonObject.put("smsserial", mRetserial = jsonObject.getString("retserial"));
//            mJsonObject.put("redflag", jsonObject.getString("redflag"));
//            mJsonObject.put("singlemultiple", jsonObject.getString("singlemultiple"));//1单选,2多选
//            mJsonObject.put("Modify_Content", jsonObject.getString("modify_Content"));//1单选,2多选
////            mChatInputFragment.mChoiceType = jsonObject.getString("singlemultiple");
//
//            int manflag = jsonObject.getInt("manflag");
//            int retsex = jsonObject.getInt("retsex");//3男童 4女童
//            String redflag = jsonObject.getString("redflag");//2年龄键盘 1可变颜色 0不可变
//            //跳转到药品列表
////            if (manflag == 4) {
//////                jumpApp();
////
////            } else
//
//            if (manflag == 1 || manflag == 3) {//跳转到人体视图
//                //retserial 回复内容  [{"cont":"3","type":"1"}]
////                Message message = mHandler.obtainMessage();
////                message.arg1 = retsex;
////                String retlist = new JSONObject(jsonObject.getString("retlist")).getString("cont");//症状编码
////                message.obj = retlist;//症状编码
////                message.what = 1;
////                mHandler.sendMessageDelayed(message, 2000);
//            } else if (manflag == 100) {//活动预留
//                String web = jsonObject.getString("retlist");
//                Intent intent = new Intent(ChatActivityB.this, CommonWebUIActivity.class);
//                intent.putExtra(CommonWebUIActivity.URL, web);
//                intent.putExtra(CommonWebUIActivity.TITLE_NAME, jsonObject.optString("functionname"));
//                startActivity(intent);
//            } else if (manflag == 4) {
//
//            } else {
//
//                if (!"".equals(jsonObject.getString("retgretting"))) {
//                    String str = jsonObject.getString("retgretting");
//                    mJsonObject.put("smsTEXT", str);
//                    mJsonObject.put("redflag", "0");
//                    JSONObject jsonObject2 = new JSONObject(mJsonObject.toString());
//                    JSONArray array = new JSONArray();
//                    array.put(jsonObject2);
//                    array.put(jsonObject);
//                    onNewMesgHandler(array);
//                } else if (!"".equals(jsonObject.getString("retlist"))) {
//                    parseToRelist(jsonObject);
//                }
//
//                if (1 == jsonObject.optInt("customer_info", -1)) {//mJsonObject.put("Modify_flag","0");
//                    mRetserial = jsonObject.optString("retserial");
//                    //jsonObject.getString("modify_Content")
//                    Message msg = Message.obtain();
//                    msg.what = JUMP_CUSTOMERINFO;
//                    msg.obj = jsonObject.toString();
//                    long timer = jsonObject.optLong("functionparm", JUMP_TIMER);
//                    mHandler.sendMessageDelayed(msg, timer * 1000);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 消息处理
//     */
//    final Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 1://跳转到人体视图
//                    if (!isFinishing()) {
//                        onStartBoadyActivity(msg.arg1, (String) msg.obj);
//                    }
//                    break;
//                case 2://接收消息
//                    if (!isFinishing()) {
//                        MessageEntity messageEntity = new MessageEntity();
//                        messageEntity.setSendState(MessageEntity.STATE_OK);
//                        //   messageEntity.setSenderId(mReciverId);
//                        messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//                        messageEntity.setReceiverId(mUserId);
//                        messageEntity.setType(MessageEntity.TYPE_TEXT);
//                        messageEntity.setSendFlag(false);
//                        messageEntity.setContentJsonArray((JSONArray) msg.obj);
//                        messageEntity.setMerchant_head(merchant_head);
//                        messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                        mChatAapter.onDataChange(messageEntity);
//                        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                    }
//                    break;
//                case 3:
//                    mChatAapter.onDataChange((MessageEntity) msg.obj);//第一次发送消息
//                    break;
//                case 4:
//                    mChatAapter.onDataChange((MessageEntity) msg.obj);
//                    mListView.setSelection(mChatAapter.getCount());
//                    break;
//                case 5:
////                    ToastUtil.showShort(ChatActivityB.this, msg.obj.toString());
//                    addliftMessage(msg.obj.toString());
//                    break;
//                case 6:
//                    HTalkApplication.getApplication().setMTalkServiceId(msg.obj.toString());
//                    mapID.put(merchant_id, msg.obj.toString());
//                    if (TextUtils.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {
//                        chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_people));
//                        chart_change.setTag(false);
//                    } else {
//                        chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_ms));
//                        chart_change.setTag(true);
//
//                    }
//                    break;
//                case 7:
//                    addliftMessage(msg.obj.toString());
//                    break;
//                case JUMP_CUSTOMERINFO:
//                    try {
//                        Intent intent = new Intent(ChatActivityB.this, DoctorChatMyinformation.class);
//                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
//                        intent.putExtra("modify_Content", jsonObject.optString("modify_Content"));
//                        intent.putExtra("titleName", jsonObject.optString("ltalkname"));
//                        startActivityForResult(intent, 200);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_chat);
//        windowLp = getWindow().getAttributes();
//        intent = getIntent();
//        chatActivity = this;
//        initView();
//        initVoiceData();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        this.intent = intent;
//        initView();
//        initVoiceData();
//        if (mPop != null && mPop.isShowing()) {
//            mPop.dismiss();
//        }
//    }
//
//    private boolean isDownAlert = true;
//
//    protected void onStart() {
//        super.onStart();
//        if (!EventBus.getDefault().isRegistered(this)) {
//
//            EventBus.getDefault().register(this);
//        }
//        //KLog.d("注册一个EventBus");
////        initLogin();
//        try {
//            if (isDownAlert) {
//                // TODO: 2016-3-29 检查更新
//                isDownAlert = false;
////                AppUpdateManager manager = new AppUpdateManager(this, true);
////                manager.checkeUpdate();
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
////            initHttp();//adapter写好后在进行初始化
//        } catch (Exception e) {
//        }
//    }
//
//    private void initView() {
//        initTitle();
//        titleLeftBtn.setVisibility(View.VISIBLE);
//        titleLeftBtn.setOnClickListener(this);
//        String str = getApplication().getResources().getString(R.string.app_name);
//        setTitle(str);
////        setLeftSet(this);
//        setRightChange(this);
//        mInflater = LayoutInflater.from(ChatActivityB.this);
//        chat_info_lv = (PullToRefreshListView) findViewById(R.id.chat_info_lv);
//
//        mListView = chat_info_lv.getRefreshableView();
//        mChatAapter = new ChatInfoAdapter(this);
//        mListView.setAdapter(mChatAapter);
//        mListView.setSelector(new BitmapDrawable());
//        mChatAapter.setListView(mListView);
//        chat_send_project = (ImageView) findViewById(R.id.chat_send_project);
//        chat_send_project.setClickable(false);
//        chat_send_img = (ImageView) findViewById(R.id.chat_send_img);
//        chat_send_img.setOnClickListener(this);
//        chat_send_camera = (ImageView) findViewById(R.id.chat_send_camera);
//        chat_send_camera.setOnClickListener(this);
//        chat_send_voice = (ImageView) findViewById(R.id.chat_send_voice);
//        chat_send_voice.setOnClickListener(this);
//        chat_send_text = (ImageView) findViewById(R.id.chat_send_text);
//        chat_send_text.setOnClickListener(this);
//        chat_input_text = (EditText) findViewById(R.id.chat_input_text);
//        chat_input_text.setOnClickListener(this);
//        linearLayoutChatPoject = (LinearLayout) findViewById(R.id.chat_project);
//        chat_add = (ImageView) findViewById(R.id.chat_add);
//        chat_add.setOnClickListener(this);
//
//        chart_change = (ImageView) findViewById(R.id.chat_send_change);
//        chart_change.setOnClickListener(this);
//        chart_change.setTag(false);
//        chat_bottom_project = (LinearLayout) findViewById(R.id.mlinearlayout);
//        chat_speech_tv = (TextView) findViewById(R.id.chat_speech_tv);
//        chat_speech_tv.setClickable(true);
//        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
//        chat_info_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
//                onLoadHistoryMesgs();
//            }
//        });
//        // title_change.setVisibility(View.GONE);
//
//
//        title_change.setSelected(SharePreUtils.getSoundsBoolean(this));
//        mTalkRipView = findViewById(R.id.sound_vv);
//        mTalkBtn = (RippleLayout) mTalkRipView.findViewById(R.id.ripple_layout);
//        if ("1".equals(intent.getStringExtra("IsKF"))) {
//            chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_ms));
//            chart_change.setTag(true);
//            LoginServiceManegerB.instance().getLoginEntity().isKF = "-1";
//        } else if ("-1".equals(intent.getStringExtra("IsKF"))) {
//        } else {
//            chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_people));
//            chart_change.setTag(false);
//        }
//        initData();
//        listenerSoftInput();
//        chat_input_text.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().equals("") && chat_add.getVisibility() == View.GONE) {
////                    chat_add.setVisibility(View.VISIBLE);
////                    chat_send_text.setVisibility(View.VISIBLE);
//                } else if (!s.toString().equals("") && chat_add.getVisibility() == View.VISIBLE) {
////                    chat_add.setVisibility(View.GONE);
////                    chat_send_text.setVisibility(View.VISIBLE);
//                }
//                if (s.toString().length() > 100) {
//                    ToastUtil.showShort(ChatActivityB.this, "您输入的文字内容过长");
//                    chat_input_text.setText(s.toString().substring(0, 100));
//                    chat_input_text.setSelection(chat_input_text.getText().toString().length());
//                }
//            }
//        });
//
//    }
//
//    private void initData() {
//        try {
//            mJsonObject = new JSONObject();
//            mJsonObject.put("Customerid", mUserId);
//            mJsonObject.put("terminalid", SystemUtils.getIMEI(getApplicationContext()));
//            mJsonObject.put("smsrail", "");
//            mJsonObject.put("smsType", "CLIENT");
//            mJsonObject.put("smsCode", "");
//            mJsonObject.put("retCode", "");
//            mJsonObject.put("smsClass", "");
//            mJsonObject.put("retClass", "");
//            mJsonObject.put("smsfeature", "1");
//            mJsonObject.put("smsTEXT", "");
//            mJsonObject.put("vpage", "");
//            mJsonObject.put("singlemultiple", "0");
//            mJsonObject.put("zjbb_jump", "");
//            mJsonObject.put("Modify_Content", "");
//            mJsonObject.put("Modify_flag", "0");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        initMerchantInfo(intent.getStringExtra("MERCHANT_NAME"), intent.getStringExtra("MERCHANT_ID"), intent.getStringExtra("ACTIVATE_CODE"), intent.getStringExtra("ICON_ADDRESS"));
//        mListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        SystemUtils.hideSoftBord(ChatActivityB.this, chat_input_text);
//                        hideMoreProject();
//                }
//                return false;
//            }
//        });
//
//        // TODO: 2016-3-30 发送语音功能
//
//        chat_speech_tv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//
//                        mIatResults.clear();
//                        if (mIat.isListening())
//                            mIat.cancel();
//                        if (mIat.startListening(mRecognizerListener) == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
//                            //未安装则跳转到提示安装页面
//                            new ApkInstaller(ChatActivityB.this).install();
//                            chat_speech_tv.setTag(false);
//                        } else {
//                            mTalkBtn.startRippleAnimation();
//                            if (rip_text == null) {
//                                rip_text = (TextView) mTalkRipView.findViewById(R.id.rip_text);
//                            }
//                            rip_text.setTag(false);
//                            rip_text.setBackgroundColor(Color.TRANSPARENT);
//                            mTalkRipView.setVisibility(View.VISIBLE);
//
//                            chat_speech_tv.setText("松开结束");
//                            chat_speech_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_speech_select));
//                            chat_speech_tv.setTag(true);
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (!(boolean) chat_speech_tv.getTag()) {
//                            break;
//                        }
//                        if (rip_text == null) {
//                            rip_text = (TextView) mTalkRipView.findViewById(R.id.rip_text);
//                            rip_text.setTag(false);
//                            rip_text.setBackgroundColor(Color.TRANSPARENT);
//                        }
//
//                        if (event.getY() < -200) {
//
//                            chat_speech_tv.setText("取消发送");
//                            mTalkRipView.setVisibility(View.VISIBLE);
//                            if (!(boolean) rip_text.getTag()) {
//                                rip_text.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_sound_red));
//                                rip_text.setTag(true);
//                            }
//                        } else {
////                            mediaRecord.changeCancelState(false);
//                            chat_speech_tv.setText("松开发送");
//                            if ((boolean) rip_text.getTag()) {
//                                rip_text.setBackgroundColor(Color.TRANSPARENT);
//                                rip_text.setTag(false);
//                            }
//                        }
//                        if (event.getY() < -200) {
//                            //LogUtil.d("----------------------", event.getY() + "------true");
//                        } else {
//                            //LogUtil.d("----------------------", event.getY() + "------false");
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (!(boolean) chat_speech_tv.getTag()) {
//                            break;
//                        }
//                        chat_speech_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_speech_unselect));
//                        if (event.getY() < -200) {
//                            mIatResults.clear();
//                            mIat.cancel();
//                            chat_speech_tv.setText("按住说话");
//                            ToastUtil.showShort(ChatActivityB.this, "已取消语音发送");
//                            //LogUtil.d("--------------------up--", "------cancle_fasong");
//                        } else {
//                            new android.os.Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //LogUtil.d("--------------------up--", "------fasong");
//                                    RingPlayer.playPressSound(ChatActivityB.this);
//                                    mIat.stopListening();
//                                    chat_speech_tv.setText("按住说话");
//                                }
//                            }, 500);
//                            mLodingFragmentDialog = LodingFragmentDialogB.showLodingDialog(getSupportFragmentManager(), "");
//
//                        }
//                        mTalkRipView.setVisibility(View.GONE);
//                        break;
//                }
//                return false;
//            }
//        });
//    }
//
//    /**
//     * 加载展开项的快捷发送数据
//     */
////    private void requestFastSend() {
////        RequestParams params = new RequestParams();
////        params.put("Type", "queryAI_Client_Quick_List");
////        params.put("MERCHANT_ID", merchant_id);
////        params.put("CUSTOMER_ID", LoginServiceManeger.instance().getLoginUserId());
////        HttpRestClient.doHttpBindCard(params, new JsonHttpResponseHandler(null) {
////            @Override
////            public void onSuccess(JSONObject response) {
////                super.onSuccess(response);
////                if (1 == response.optInt("code")) {
////                    chat_send_project.setClickable(true);
////                    chat_send_project.setOnClickListener(ChatActivityB.this);
////                    fastSendArr = response.optJSONArray("result");
////                    if (fastSendArr != null && fastSendArr.length() > 0) {
////                        chat_bottom_project.setAdapter(new FastSendAdapter(ChatActivityB.this, fastSendArr));//设置底部快捷发送adapter
////                        chat_bottom_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
////                            @Override
////                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                                JSONObject obj = fastSendArr.optJSONObject(position);//当点击底部快捷发送条目，获取到此条目obj
////                                onMessageSend(obj.optString("QUICK_CONTENT"), obj.optString("QUICK_CODE"));
////                                chat_bottom_project.setVisibility(View.GONE);
////                                chat_send_project.setImageDrawable(getResources().getDrawable(R.drawable.chat_bottom_project));
////                            }
////                        });
////                        speedyAction();
////                    } else {
////                        chat_bottom_project.setAdapter(null);//没有快捷像可以换一种展示方式
////                    }
////                }
////            }
////        });
////    }
//
//
//    TagPageFragment mPageFragment;
//
//    /**
//     * 加载展开项的快捷发送展开数据
//     */
//    private void requestFastSend2() {
//        mPageFragment = (TagPageFragment) getSupportFragmentManager().findFragmentById(R.id.tag_id);
//        mPageFragment.setItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                try {
////                    HTalkApplication.getApplication().setMTalkServiceId("");
//                TagMenuEntity tag = (TagMenuEntity) view.getTag();
////                    JSONArray str = new JSONArray(tag.guide_content);
//////                    caidanID = tag.menu_code;
////                    TyJsonObject j = new TyJsonObject(str.optString(0));
//                onMessageSend(tag.guide_content, tag.menu_code);
//                //      sendHttpjilu(tag, j.optString("GUIDE_CONTENT"));
//                //      mPlanCheck.setChecked(false);
////            tag.is_custom = "1";
////            LiteOrmDBUtil.insert(tag);
//                //      CustomTag.saveDB(tag);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//            }
//        });
//
//        RequestParams params = new RequestParams();
//        params.put("Type", "queryTiyanCaidan");
//        params.put("MERCHANT_ID", merchant_id);
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        HttpRestClientB.doHttpBindCard2(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
////                if (1 == response.optInt("code")) {
////                    chat_send_project.setClickable(true);
////                    chat_send_project.setOnClickListener(ChatActivityB.this);
//////                    IS_SHOW_B = true;
////                    //   showBottomProject();
////                    final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.group);
////                    radioGroup.removeAllViews();
//////        List<TagIndexEntity> lists = LiteOrmDBUtil.getLiteOrm().query(TagIndexEntity.class);
////                    List<TagIndexEntity> lists = null;
////                    try {
////                        lists = TagIndexEntity.parseToList(response.getJSONArray("result"));
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    for (int i = 0; i < lists.size(); i++) {
////                        final TagIndexEntity tagindex = lists.get(i);
////                        RadioButton btn = (RadioButton) LayoutInflater.from(ChatActivityB.this).inflate(R.layout.check_fragment_radio_btn_item, null);
////                        btn.setText(tagindex.class_name);
////                        btn.setTag(tagindex);
////                        radioGroup.addView(btn);
////                    }
////
////                    if (lists.size() == 1) {
////                        radioGroup.setVisibility(View.GONE);
////                    } else {
////                        radioGroup.setVisibility(View.VISIBLE);
////                    }
////
////                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////                        @Override
////                        public void onCheckedChanged(RadioGroup group, int checkedId) {
////                            RadioButton btn = (RadioButton) radioGroup.findViewById(checkedId);
////                            TagIndexEntity tag = (TagIndexEntity) btn.getTag();
////                            List<TagMenuEntity> data = tag.data;
////                            mPageFragment.setData(data);
////                        }
////                    });
////
////                    if (radioGroup.getChildCount() > 0) {
////                        RadioButton b = (RadioButton) radioGroup.getChildAt(0);
////                        b.setChecked(true);
////                    }
////                }
//            }
//        });
//    }
//
//    /**
//     * 初始化当前绑定的商户信息
//     *
//     * @param merchant_name
//     * @param merchant_id
//     * @param activate_code
//     */
//    private void initMerchantInfo(String merchant_name, String merchant_id, String activate_code, String merchant_head) {
//        mChatAapter.removeAll();
//        HTalkApplication.getApplication().setMTalkServiceId("");
//        this.merchant_name = merchant_name;
//        this.merchant_id = merchant_id;
//        this.activate_code = activate_code;
//        this.merchant_head = merchant_head;
////        if (!TextUtils.isEmpty(ChatActivityB.merchant_head)) {
//
////        Picasso.with(this).load(WelcomeActivityB.headdrawid).placeholder(R.drawable.base_messon).error(R.drawable.base_messon).into(title_setting);
////        }
////        setTitle(merchant_name);
////        setTitle(getResources().getString(R.string.app_name));
//        setTitle(getResources().getString(R.string.xiaoyi_name));
////        requestMyMerchant();//多商户
////        MessageServiceManager.instance().loadOffLineMsg("");//有客服时加载离线
//        //adapter写好后在进行初始化
//    }
//
//    /**
//     * 加载我已经绑定的商户
//     */
//    private void requestMyMerchant() {
////        RequestParams params = new RequestParams();
////        params.put("Type", "queryCenter_Merchant_DefineMesg");
////        params.put("CUSTOMER_ID", LoginServiceManeger.instance().getLoginUserId());
////        HttpRestClient.doHttpBindCard(params, new JsonHttpResponseHandler() {
////            @Override
////            public void onSuccess(JSONObject response) {
////                super.onSuccess(response);
////                if (1 == response.optInt("code")) {
////                    try {
////                        merchantArr = response.getJSONArray("result");
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        });
//    }
//
//    /**
//     * 天气查询结果
//     */
//    public void onEventMainThread(MyEvent v) {
//        if (v.code == 119) {
//            try {
//                mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                JSONObject jsonObject = new JSONObject(v.what);
//                JSONArray array = new JSONArray(jsonObject.optString("result"));
//                if (1 == jsonObject.optInt("code") && array.length() != 0) {
//                    for (int i = 0; i < array.length(); i++) {
//                        String content = array.get(i).toString();
//                        mChatAapter.sendMsgToService(content);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (v.code == 1) {//登陆成功
////            KLog.d("======登陆成功");
//        } else if (v.code == 0) {//登陆失败
//            //KLog.d("======登陆失败");
////            initLogin();
//        }
//    }
//
//    /**
//     * 当掉线时请求登陆
//     *
//     * @param event
//     */
//    public void onEventBackgroundThread(LoginEventB event) {
//        switch (event.getEvent()) {
//            case ACTION_LOGIN:
////                if (SharePreUtils.feachLoginStatus(this)) {//获取账号密码
////                    String[] str = SharePreUtils.getAcctorPaswd(this);
////                    if (str != null)
////                        LoginServiceManeger.instance().login(str[0], str[1]);
////                    else
////                        LoginServiceManeger.instance().loginEmpty();
////                } else {//用空字符串登录
////                    LoginServiceManeger.instance().loginEmpty();
////                }
//                break;
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void onEventMainThread(final TalkEvent log) {
//        switch (log.getWhat()) {
//            case TalkEvent.LONGTIME:
//                setTalkID("");
//                break;
//            case TalkEvent.STOPAN:
//                mChatAapter.stopAnimation();
//                break;
//
//            case TalkEvent.UPTALK:
//                mListView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mListView.smoothScrollToPositionFromTop(log.getArg1() + 1, -log.getArg2());
//                    }
//                }, 200);
//                break;
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public void onEventMainThread(LoginEventB log) {
//        switch (log.getEvent()) {
//            case ACTION_LOGIN_OK:
//                try {
////                    LiteOrmDBUtil.createDb(this,LoginServiceManeger.instance().getLoginUserId());
//                    initHttp();
//                    //  requestFastSend();
//                    requestFastSend2();//快速回复
//                    //  requestMyMerchant();
//                    //MessageServiceManager.instance().loadOffLineMsg("");
//                    JSONArray array = new JSONArray(LoginServiceManegerB.instance().mactiviArray);
//                    if (array.length() > 0) {
//                        TyJsonObject json = new TyJsonObject(array.optString(0));
//                        if (SharePreUtils.saveActiv(this, mUserId, json.optString("ANDROID_ICON3"))) {
//                            //第一次进入的介绍页 暂时去掉
//                            //   LanchFragmentDialog.showCustmDialog(getSupportFragmentManager(), LoginServiceManeger.instance().mactiviArray);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//            case ACTION_LOGIN_FAILUES:
//                //KLog.d("======登陆失败");
//                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), log.getMesge(), "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                    @Override
//                    public void onDismiss(DialogFragment fragment) {
//                        LoginEventB event = new LoginEventB(LoginEventB.Event.ACTION_LOGIN);
//                        EventBus.getDefault().post(event);
//                    }
//
//                    @Override
//                    public void onClick(DialogFragment fragment, View v) {
//                        startActivity(new Intent(ChatActivityB.this, AtyConsultMain.class));
//                    }
//                });
//
//                break;
//            case ACTION_LOGIN_OTHER_PLACE:
//                break;
//            case LOGIN_OUT_SUCCESS:
//
////                startActivity(new Intent(ChatActivityB.this, LoginRegisterActivity.class));
////                finish();
//                break;
//        }
//    }
//
//    /**
//     * 消息处理
//     */
//    public void onEventMainThread(MessagesEvent event) {
//        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        switch (event.getEvent()) {
//            case SERVICE_TO_AUTO_CONTENT:
//                try {
//                    mRetserial = "";
//                    mJsonObject.put("phonetype", "555");
//                    onMessageSend(event.getString());
//                    mJsonObject.put("phonetype", "");
//                    HTalkApplication.getAppData().clearToAutoContent();
//                    deletMsg();
//                    setTalkID("");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case SERVICE_SWITCH_RESPONSE:
//                try {
//                    mJsonObject.put("phonetype", "555");
//                    mJsonObject.put("SERVICE_SWITCH_RESPONSE", "555");
//                    onMessageSend(event.getString());
//                    mJsonObject.put("SERVICE_SWITCH_RESPONSE", "");
//                    HTalkApplication.getAppData().clearToAutoContent();
//                    deletMsg();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case RECEIVE_PUSH_SHOP_MESG:
//                if (event.getMsg() != null) {
//                    mChatAapter.addData(event.getMsg());
//                    HTalkApplication.getAppData().clearToAutoContent();
//                    deletMsg();
//                }
//                break;
//        }
//    }
//
//    /**
//     * 每次登陆之后  都要重新配置数据
//     */
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void initHttp() {
//        //KLog.d("====mUserId = " + mUserId + "========LoginId = " + LoginServiceManeger.instance().getLoginUserId());
//        V_send_code = "0";
//        mChatAapter.removeAll();
////        onLoadHistoryMesgs();
//        onLoadWelcome();
//        // KLog.d("====开始加载第一次历史 ");
//
//        mUserId = LoginServiceManegerB.instance().getLoginId();
//        try {
//            mJsonObject.put("Customerid", mUserId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 会在子线程中做耗时操作进行登陆
//     *
//     * @param str 数组 账号密码
//     */
//    public void onEventBackgroundThread(MessageEntity str) {
//        MessageServiceManagerB.instance().sendMsg(str, MessageEntity.TYPE_TEXT);
//    }
//
//
//    /**
//     * 登陆之后,会调用此方法
//     * <p/>
//     * {
//     * "sms_req_content": "你好",
//     * "sid": "704807",
//     * "timeStamp": "1447324151733",
//     * "server_code": 9019,
//     * "tag": 10,
//     * "type": 4,
//     * "isGroupMessage": 0,
//     * "ValidFlag": 2,
//     * "duration": "0",
//     * "customerId": "225305",
//     * "sms_target_id": "227024",
//     * "serverId": "704807",
//     * "SERVICE_TYPE": "99999",
//     * "timeout": "1",
//     * "system_version": "1.0"
//     * }
//     */
//    public void onEventMainThread(MesgEvent log) {
//        if (log.code == SocketCode.RECEIVE_MSG_CODE) {//接受对方的普通消息 || 对方推送订单的消息
//            mChatAapter.addData(log.msg);
//            mListView.setSelection(mChatAapter.getCount());
//            //           String str = log.msg.getSenderId();
////            HTalkApplication.getApplication().setMTalkServiceId(log.msg.getSenderId());
////            mapID.put(merchant_id, HTalkApplication.getApplication().getmTalkServiceId());
//            setTalkID(log.msg.getSenderId());
//            deletMsg();
//        } else if (log.code == SocketCode.SERVICE_PUSH_ORDER) {
//            parseToRelist(log.what);
//        }
//    }
//
//    /**
//     * 删除离线
//     */
//    public void deletMsg() {
//        deletMsg("");
//    }
//
//    /**
//     * 删除离线
//     */
//    public void deletMsg(String mTalk) {
//        /**
//         *if (mChatAdapter.getCount() != 0) {
//         MessageEntity messageEntity = mChatAdapter.getList().get(mChatAdapter.getList().size() - 1);
//         onDeleteMessageFromMessgae(messageEntity);}
//         */
//        HTalkApplication.getAppData().clearAll();
//        RequestParams params = new RequestParams();
//        params.put("Type", "deleteLixian");
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("offid", String.valueOf(Integer.MAX_VALUE));
//        if (HStringUtil.isEmpty(mTalk))
//            params.put("sms_target_id", HTalkApplication.getApplication().getmTalkServiceId());
//        else
//            params.put("sms_target_id", mTalk);
//        // KLog.d("执行删除离线消息");
//        HttpRestClientB.doHttpLOADEDOFFLINEMESSAGE(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, JSONObject response) {
//                super.onSuccess(statusCode, response);
//                //   KLog.d("离线消息删除返回");
//                //  KLog.json(response.toString());
//            }
//        });
//    }
//
//    /**
//     * 离线消息加载成功
//     * 初始化历史记录
//     *
//     * @param appdata
//     */
//    public void onEventMainThread(AppData appdata) {
//
//        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        List<MessageEntity> entities = HTalkApplication.getAppData().messageNoRead;
//        if (entities.size() > 0) {
//            mChatAapter.addAll(HTalkApplication.getAppData().messageNoRead);
//            deletMsg(entities.get(0).getSenderId());
//            HTalkApplication.getAppData().clearAll();
//        }
//        initHttp();//初始化历史记录
//    }
//
//    private void parseToRelist(JSONObject jsonObject) {
//        JSONObject json = null;
//        try {
//            json = new JSONObject(jsonObject.optString("retlist"));
//
//            JSONArray btns = json.optJSONArray("btns");
//            if (btns != null) {
//                for (int i = 0; i < btns.length(); i++) {
//                    JSONObject jsonObject2 = btns.getJSONObject(i);
//                    if (jsonObject2.has("type") && 3 == jsonObject2.getInt("type")) {
//                        jsonObject2.put("type", 4);
//                    }
//                }
//            }
//            jsonObject.put("retlist", json.toString());
//            mJsonObject.put("smsTEXT", json);
//            JSONObject jsonObject2 = new JSONObject(mJsonObject.toString());
//            JSONArray array = new JSONArray();
//            array.put(jsonObject2);
//            array.put(jsonObject);
//            jsonObject2.put("redflag", "1");
//            onNewMesgHandler(array);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 响应新消息
//     *
//     * @param object
//     */
//    private void onNewMesgHandler(JSONArray object) {
//        object.put(mJsonObject);
//        Message message = mHandler.obtainMessage();
//        message.obj = object;
//        message.what = 2;
//        mHandler.sendMessageDelayed(message, 1000);
//    }
//
////    private void initLogin() {
////        if (HTalkApplication.getApplication().isNetWork() && isLoginedAction) {
////            isLoginedAction = false;
////            LoginEventB event = new LoginEventB(LoginEventB.Event.ACTION_LOGIN);
////            EventBus.getDefault().post(event);
////        } else {//判断如果未登录   就用null登陆
////            if (LoginServiceManeger.instance().getLoginState() == LoginEventB.Event.NONE) {
////                LoginServiceManeger.instance().loginEmpty();
////            }
////        }
////    }
//
//    /**
//     * 加载历史记录
//     */
//    private void onLoadHistoryMesgs() {
////        RequestParams params = new RequestParams();
////        params.put("terminalId", SystemUtils.getIMEI(this));
////        params.put("customerId", LoginServiceManegerB.instance().getLoginId());
////        params.put("V_send_code", V_send_code);
////        // params.put("key", HttpRestClient.mKey);
//////       params.put("appCode", "");
////        params.put("Type", "XiaoYi_chat_his_pb");
////        params.put("MERCHANT_ID", SOConfigs.MERCHANT_ID);//商户ID
//////        params.put("", "");//VIP卡号
////        //KLog.d("======开始加载历史V_send_code== " + V_send_code);
////        HttpRestClientB.doHttpMESGHISTORY(params, new JsonHttpResponseHandler() {
////            @Override
////            public void onFinish() {
////                super.onFinish();
////                chat_info_lv.onRefreshComplete();
////            }
////
////            @Override
////            public void onSuccess(JSONObject response) {
////                super.onSuccess(response);
////
////                if (response.optInt("code") != 1) return;
////                JSONObject jsonData = response.optJSONObject("result");
////                JSONArray arrays = jsonData.optJSONArray("c_main30");
////
////                //记录第一个vSendCode,以便下次使用
////                String code = V_send_code;
////                if (arrays != null && arrays.length() > 0) {
////                    V_send_code = arrays.optJSONObject(0).optString("vSendCode");
////                }
////                final List<MessageEntity> mList = getMessageEntities(arrays);
////                if (mChatAapter.getCount() == 0) {
////                    final MessageEntity messageEntity = new MessageEntity();
////                    messageEntity.setSendState(MessageEntity.STATE_OK);
//////                    messageEntity.setSenderId(mReciverId);
////                    messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
////                    messageEntity.setReceiverId(mUserId);
////                    messageEntity.setType(MessageEntity.TYPE_TEXT);
////                    messageEntity.setSendFlag(false);
////                    messageEntity.setContent(jsonData.optString("WelLan"));
////                    //  messageEntity.setContent(HStringUtil.chatRadom());
////                    messageEntity.setId("-1");
////                    messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
////                    messageEntity.setMerchant_head(merchant_head);
////                    if (!TextUtils.isEmpty(messageEntity.getContent())) {
////                        //  mList.add(messageEntity);
//////                        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//////                                .map(new Func1<Long, Object>() {
//////                                    @Override
//////                                    public Object call(Long aLong) {
//////                                        mChatAapter.addData(messageEntity);
//////                                        mListView.setSelection(mChatAapter.getCount());
//////                                        return  null;
//////                                    }
//////                                }).subscribe();
////                        wHandler.postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                mChatAapter.addData(messageEntity);
////                                mListView.setSelection(mChatAapter.getCount());
////                            }
////                        }, 1000);
////                        ;
////                    }
////                }
////                if (!isFinishing()) {
////                    if (mList.size() != 0) {
////                        if ("0".equals(code)) {
////                            mChatAapter.removeAll();
////                        }
////                        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
////                        mChatAapter.onPullDataChange(mList);
////                        chat_info_lv.postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                mListView.setSelection(mList.size());
////                            }
////                        }, 50);
//////                        mListView.setSelection(mList.size());
////                    } else {
//////                        ToastUtil.showShort(getApplicationContext(), "没有更多内容");
////                    }
////                }
////            }
////        });
//    }
//
//    /**
//     * 加载欢迎语
//     */
//    private void onLoadWelcome() {
//
//        HttpRestClientB.doHttpSOWel( new JsonHttpResponseHandler() {
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                chat_info_lv.onRefreshComplete();
//            }
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if ("0".equals(response.optString("code"))){
//                    if (mChatAapter.getCount() == 0) {
//                        final MessageEntity messageEntity = new MessageEntity();
//                        messageEntity.setSendState(MessageEntity.STATE_OK);
////                    messageEntity.setSenderId(mReciverId);
//                        messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//                        messageEntity.setReceiverId(mUserId);
//                        messageEntity.setType(MessageEntity.TYPE_TEXT);
//                        messageEntity.setSendFlag(false);
//                        messageEntity.setContent(response.optString("server_params"));
//                        //  messageEntity.setContent(HStringUtil.chatRadom());
//                        messageEntity.setId("-1");
//                        messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                        messageEntity.setMerchant_head(merchant_head);
//                        if (!TextUtils.isEmpty(messageEntity.getContent())) {
//                            //  mList.add(messageEntity);
////                        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
////                                .map(new Func1<Long, Object>() {
////                                    @Override
////                                    public Object call(Long aLong) {
////                                        mChatAapter.addData(messageEntity);
////                                        mListView.setSelection(mChatAapter.getCount());
////                                        return  null;
////                                    }
////                                }).subscribe();
//                            wHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mChatAapter.addData(messageEntity);
//                                    mListView.setSelection(mChatAapter.getCount());
//                                }
//                            }, 1000);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//
//
//    /**
//     * 解析历史记录
//     *
//     * @param arrays
//     * @return
//     */
//    @NonNull
//    private List<MessageEntity> getMessageEntities(JSONArray arrays) {
//        List<MessageEntity> mList = new ArrayList<MessageEntity>();
//        MessageEntity messageEntity;
//        for (int j = 0; j < arrays.length(); j++) {
//            try {
//                TyJsonObject jsonResult = null;
//                jsonResult = new TyJsonObject(arrays.optString(j));
//                String mesgId = jsonResult.optString("offline_id");
//                if (mChatAapter.doEquestoId(mesgId)) {
//                    continue;
//                }
//                messageEntity = new MessageEntity();
//                messageEntity.setId(mesgId);
//                if (!HStringUtil.isEmpty(jsonResult.optString("cardtype"))) {
//                    messageEntity.setContent(jsonResult.optString("retlist"));
//                } else if (jsonResult.optInt("VALID_FLAG") == 1) {
//
//                    messageEntity.setSenderId(mUserId);
//                    messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
////                    messageEntity.setReceiverId(mReciverId);
//
//                    messageEntity.setContent(jsonResult.optString("retlist"));
//                } else {
//
//
//                    TyJsonObject json = new TyJsonObject(jsonResult.optString("retlist"));
//                    JSONArray btns = json.optJSONArray("btns");
//                    if (btns != null) {
//                        for (int i = 0; i < btns.length(); i++) {
//                            JSONObject jsonObject2 = btns.getJSONObject(i);
//                            if (jsonObject2.has("type") && 3 == jsonObject2.getInt("type")) {
//                                jsonObject2.put("type", 4);
//                            }
//                        }
//                    }
//                    jsonResult.put("retlist", json.toString());
//                    mJsonObject.put("smsTEXT", json);
//                    JSONObject jsonObject2 = new JSONObject(mJsonObject.toString());
//                    JSONArray array = new JSONArray();
//                    array.put(jsonObject2);
//                    array.put(jsonResult);
//                    jsonObject2.put("redflag", "1");
//                    array.put(mJsonObject);
////                  messageEntity.setSenderId(mReciverId);
//                    messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//                    messageEntity.setReceiverId(mUserId);
//                    messageEntity.setContentJsonArray(array);
//
//                }
//                messageEntity.setSendState(MessageEntity.STATE_OK);
//                messageEntity.setCARDTYPE(HStringUtil.stringFormat(jsonResult.optString("cardtype")));
//                String type = jsonResult.getString("cardtype");
//                if (!HStringUtil.isEmpty(type)) {
//                    messageEntity.setType(Integer.parseInt(type));
//                    if (type.equals("2")) {
//                        float fl = Float.parseFloat(new JSONObject(jsonResult.getString("dataHolder")).getString("duration"));
//                        messageEntity.setContent(messageEntity.getContent() + "&" + Math.round(fl) + "\"");
//                    }
//                } else {
//                    messageEntity.setType(MessageEntity.TYPE_TEXT);
//                }
////1是图片 2语音 4文字
//                messageEntity.setSendFlag(jsonResult.optInt("VALID_FLAG") == 1);
//                messageEntity.setDate(Long.valueOf(jsonResult.optString("PARA_NOTE_TIME")));
//                messageEntity.setMerchant_head(merchant_head);
//                mList.add(messageEntity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return mList;
//    }
//
//    //点击事件
//    @Override
//    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.title_back://回退
//                onBackPressed();
//                break;
////            case R.id.title_setting://商户选择
////                showPop(TITLE_CHANGE);
////                break;
//            case R.id.chat_send_change:
//                changeT();//切换小壹客服
//                break;
//            case R.id.title_change://语音
//                // showPop(TITLE_SETTINT);
//                boolean flag = SharePreUtils.getSoundsBoolean(ChatActivityB.this);
//                SharePreUtils.saveSoundsBoolean(ChatActivityB.this, !flag);
//                title_change.setSelected(!flag);
//                if (flag && mChatAapter != null) {
//                    mChatAapter.stopRecoard();
//                }
//                break;
//            case R.id.pop_info://个人信息
////                intent = new Intent(this, PersonalInfoActivity.class);
////                intent.putExtra("CUSTOMER_ID", LoginServiceManeger.instance().getLoginUserId());
////                intent.putExtra("MERCHANT_ID", merchant_id);
////                startActivity(intent);
////                mPop.dismiss();
//                break;
//            case R.id.pop_phone://修改电话
////                startActivity(new Intent(this, ChangePhoneActivity.class));
////                mPop.dismiss();
//                break;
//            case R.id.pop_pwd://修改密码
////                startActivity(new Intent(this, ForgetPswdActivity.class));
////                mPop.dismiss();
//                break;
//            case R.id.pop_out://设置
//                mPop.dismiss();
//                showPop(LOGIN_OUT);
//                break;
////            case R.id.pop_out_cancle:
////                mPop.dismiss();
////                break;
////            case R.id.pop_out_sure://退出
////                LoginServiceManeger mng = LoginServiceManeger.instance();
////                mng.loginOutAccount();
////                mng.setUser(null);//退出登录后，清空当前user信息
////                if (SocketManager.isConnected()) {
////                    CoreServiceB.actionLogOut(ChatActivityB.this);
////                    mng.loginOut();
////                }
////                startActivity(new Intent(ChatActivityB.this, LoginRegisterActivity.class));
////                finish();
////                break;
//            case R.id.chat_send_project://展开项
//                speedyAction();
//                break;
//            case R.id.chat_add://5月16号 展开功能按钮
//                changeProject();
//                break;
//
//            case R.id.chat_send_img://图片
//                hideMoreProject();
//                Intent i = new Intent(
//                        // 相册
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//                break;
//            case R.id.chat_send_camera://图片
//                takeCamera();
//                break;
//            case R.id.chat_send_voice://语音//5月16号
//                // hideMoreProject();
//                // voiceAction();
//                changeSound();
//                break;
//            case R.id.chat_input_text://输入框 //5月16号
//                hideMoreProject();
//                hideSound();
//                hideProject();
//                mListView.setSelection(mListView.getAdapter().getCount() - 1);
//                break;
//            case R.id.chat_send_text://发送
//                // SystemUtils.hideSoftBord(this, chat_input_text);
//                String str = chat_input_text.getEditableText().toString().trim();
//                chat_input_text.setText(null);
//                if (HStringUtil.isEmpty(str)) return;
//                mJsonObject.remove("btnflag");
//                try {
//                    mJsonObject.put("Customerid", "231897");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                onMessageSend(str);
////                onMessageSend2(str);
//                mChatAapter.isSoftword = true;
////              showPop(VOICE_TEXT);//语音转汉字的pop暂时放在这里
//                break;
//        }
//    }
//
//    /**
//     * 展开项
//     */
//    @SuppressLint("NewApi")
//    private void speedyAction() {
//        if (IS_SHOW_B) {
//            hideMoreProject();
//        } else {
//            showBottomProject();
//            hideSound();
//            // hideProject();
//        }
//    }
//
//    /**
//     * 语音
//     */
//    @SuppressLint("NewApi")
//    private void voiceAction() {
//        if (IS_SPEECH) {
//            IS_SPEECH = false;
//            chat_send_voice.setImageDrawable(this.getResources().getDrawable(R.drawable.chat_bottom_voice));
//            chat_speech_tv.setVisibility(View.GONE);
//            chat_input_text.setVisibility(View.VISIBLE);
//            chat_input_text.requestFocus();
////            SystemUtils.openSoftBord(this, chat_input_text);
//        } else {
//            IS_SPEECH = true;
//            chat_send_voice.setImageDrawable(this.getResources().getDrawable(R.drawable.chat_bottom_culter));
//            SystemUtils.hideSoftBord(this, chat_input_text);
//            chat_input_text.setVisibility(View.GONE);
//            chat_speech_tv.setVisibility(View.VISIBLE);
//            chat_speech_tv.setText("按住\t说话");
//        }
//    }
//
//    private String path;//图片路径
//
//    /**
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        switch (requestCode) {
//            case RESULT_LOAD_IMAGE:
//                if (data == null) return;
//                if (resultCode == RESULT_OK && null != data) {// 相册返回
//                    Uri uri = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//
////                        bitmap = Bimp.createFramedPhoto(480, 480, bitmap, (int) (UnitUtil.dip2px(this, 10) * 1.6f));//剪裁之后获取到的bitmap图片
//                        upBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                    if (uri != null) {
////                        startPhotoZoom(uri);
////                    }
//                }
//                break;
//            case CUT_PHOTO_REQUEST_CODE:
//                if (data == null) return;
////                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
////                    try {
////                        Bitmap bitmap;
////
////                        bitmap = Bimp.getLoacalBitmap(path);
////                        bitmap = Bimp.createFramedPhoto(480, 480, bitmap, (int) (UnitUtil.dip2px(this, 10) * 1.6f));//剪裁之后获取到的bitmap图片
////                        upBitmap(bitmap);
////                    } catch (Exception e) {
////                        //  bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(path));
////
////
////                    }
////
////
////                    // TODO: 2016-3-29 已获取到图片，功能开发中
//////                    onMessageSend(bitmap);
////                }
//                break;
//            case BIND_OK:
//                if (data == null) return;
//                if (resultCode == RESULT_OK && null != data) {//绑定成功后返回商户信息
//                    initMerchantInfo(data.getStringExtra("MERCHANT_NAME"), data.getStringExtra("MERCHANT_ID"), data.getStringExtra("ACTIVATE_CODE"), data.getStringExtra("ICON_ADDRESS"));
//                    merchantArr = null;
//                    HTalkApplication.getApplication().setMTalkServiceId("");
//                    requestMyMerchant();
//                }
//                break;
//            case RESULT_LOAD_CAMERA:
//                File f = new File(Environment.getExternalStorageDirectory()
//                        + "/" + "XiaoYi" + "/" + cameraName);
//                try {
//                    Uri u =
//                            Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),
//                                    f.getAbsolutePath(), null, null));
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
//
////                        bitmap = Bimp.createFramedPhoto(480, 480, bitmap, (int) (UnitUtil.dip2px(this, 10) * 1.6f));//剪裁之后获取到的bitmap图片
//                    upBitmap(bitmap);
//                    //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case 100:
//                if (data == null) return;
//                try {
//                    String value = data.getStringExtra("parame");
//                    if (value == null || value.length() == 0) return;
//                    mJsonObject.put("smsTEXT", "就是这些症状了");
//                    JSONObject jsonObject = new JSONObject(mJsonObject.toString());
//                    mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                    if (!"".equals(mRetserial)) {
//                        mJsonObject.put("smsTEXT", mRetserial + " " + value);
//                    } else {
//                        mJsonObject.put("smsTEXT", value);
//                    }
//
//                    JSONArray array = new JSONArray();
//                    array.put(jsonObject);
//                    array.put(mJsonObject);
//                    MessageEntity messageEntity = new MessageEntity();
//                    messageEntity.setSendState(MessageEntity.STATE_PROCESING);
//                    messageEntity.setSenderId(mUserId);
//                    messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
////                  messageEntity.setReceiverId(mReciverId);
//                    messageEntity.setType(MessageEntity.TYPE_TEXT);
//                    messageEntity.setSendFlag(true);
//                    messageEntity.setContent("就是这些症状了");
//                    messageEntity.setMerchant_head(merchant_head);
//                    messageEntity.setContentJsonArray(array);
//                    messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    mChatAapter.onDataChange(messageEntity);
//                    mJsonObject.put("original_smstext", messageEntity.getContent());
//                    mJsonObject.put("btnflag", "1");
//                    HttpRestClientB.doHttpVirtualDoctor(mJsonObject, new MJsonHttpResponseHandler(messageEntity));
//                    mJsonObject.remove("btnflag");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case 200://修改资料
//                if (data == null) return;
//                try {
//                    if (data.hasExtra("data"))
//                        mActioncacheJson = new JSONObject(data.getStringExtra("data"));
//
//                    mJsonObject.put("Modify_flag", "1");
//                    mJsonObject.put("Modify_Content", data.getStringExtra("json"));
//                    //
//                    mJsonObject.put("smsTEXT", mJsonObject.getString("smsserial") + " " + data.getStringExtra("json"));
//                    mJsonObject.put("smsCode", mActioncacheJson.optString("retCode"));
//                    mJsonObject.put("smsserial", mActioncacheJson.optString("retserial"));
//                    mJsonObject.put("smsClass", mActioncacheJson.optString("retClass"));
//                    mJsonObject.put("vpage", mActioncacheJson.optString("vpage"));
//                    mJsonObject.put("btnflag", "1");
//                    sendMessage(mJsonObject, "您看填的对不对?");
//                    mJsonObject.put("Modify_flag", "0");
//                    mJsonObject.remove("btnflag");
//                } catch (JSONException e) {
//                }
//                break;
//            case 300://修改
//                if (data == null) return;
//                mChatAapter.removeAll();
//                V_send_code = "0";
////                initData();
//                break;
//            case 400://填写订单信息
//                if (data == null) return;
//                if (!data.hasExtra("message")) return;
//                messageFromSelf(data.getStringExtra("message"));
//                break;
//            case 500://点击查看详情内容 返回的药品购买 (此跳转在adapter中)
//                if (data == null) return;
//                if (data.hasExtra("data")) {
//                    try {
//                        mChatAapter.sendMsgToJD(new JSONObject(data.getStringExtra("data")));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    onMessageSend(data.getStringExtra("data"));
//                }
//                break;
//        }
//    }
//
//    private void startPhotoZoom(Uri uri) {
//        try {
//            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
//            SimpleDateFormat sDateFormat = new SimpleDateFormat(
//                    "yyyyMMddhhmmss");
//            String address = sDateFormat.format(new java.util.Date());
//            if (!FileUtils.isFileExist("")) {
//                FileUtils.createSDDir("");
//
//            }
//            path = "";
//            path = FileUtils.SDPATH + address + ".JPEG";
//            Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
//                    + ".JPEG");
//
//            final Intent intent = new Intent("com.android.camera.action.CROP");
//
//            // 照片URL地址
//            intent.setDataAndType(uri, "image/*");
//
//            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 480);
//            intent.putExtra("outputY", 480);
//            // 输出路径
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            // 输出格式
//            intent.putExtra("outputFormat",
//                    Bitmap.CompressFormat.JPEG.toString());
//            // 不启用人脸识别
//            intent.putExtra("noFaceDetection", false);
//            intent.putExtra("return-data", false);
//            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 展示pop
//     *
//     * @param which
//     */
//    private LinearLayout pop_change_ll;
//    private TextView pop_voice_text;
//    private TextView pop_out_cancle, pop_out_sure;
//    RadioGroup.LayoutParams params = null;
//
//    private void showPop(int which) {
//        ScreenUtil mScreen = new ScreenUtil(ChatActivityB.this);
//        if (mPop != null && mPop.isShowing()) {
//            mPop.dismiss();
//            return;
//        }
//        mPop = null;
//        switch (which) {
//            case TITLE_SETTINT://设置
//                hideMoreProject();
//                popView = mInflater.inflate(R.layout.layout_pop_set, null);
//                popView.findViewById(R.id.pop_info).setOnClickListener(this);
//                popView.findViewById(R.id.pop_phone).setOnClickListener(this);
//                popView.findViewById(R.id.pop_pwd).setOnClickListener(this);
//                popView.findViewById(R.id.pop_out).setOnClickListener(this);
//                mPop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                PopUtil.showPop(getWindow(), mPop, findViewById(R.id.gray_title_layout), TITLE_SETTINT);
//                break;
//            case TITLE_CHANGE://切换
//                hideMoreProject();
//                if (merchantArr == null) {
//                    break;
//                }
//                popView = mInflater.inflate(R.layout.layout_pop_change, null);
//                pop_change_ll = (LinearLayout) popView.findViewById(R.id.pop_change_ll);
//                JSONObject merchantObj = null;
//                for (int i = 0; i < merchantArr.length() + 1; i++) {
//                    params = new RadioGroup.LayoutParams(UnitUtil.dip2px(this, 54), UnitUtil.dip2px(this, 54));
//                    ImageView img = new ImageView(ChatActivityB.this);
//                    if (i < merchantArr.length()) {
//                        try {
//                            merchantObj = merchantArr.getJSONObject(i);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        params.setMargins(0, 0, UnitUtil.dip2px(this, 10), 0);
//                        Bitmap bitmap = null;
//                        Picasso.with(ChatActivityB.this).load(merchantObj.optString("ICON_ADDRESS")).into(img);
//                    } else {
//                        params.setMargins(0, 0, 0, 0);
//                        img.setBackgroundResource(R.drawable.title_pop_add);
//                    }
//                    img.setTag(i);
//                    img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if ((Integer) v.getTag() == merchantArr.length()) {
////                                startActivityForResult(new Intent(ChatActivityB.this, AddVipActivity.class), BIND_OK);
//                            } else {
//                                JSONObject obj = merchantArr.optJSONObject((Integer) v.getTag());
//                                initMerchantInfo(obj.optString("MERCHANT_NAME"), obj.optString("MERCHANT_ID"), obj.optString("ACTIVATE_CODE"), obj.optString("ICON_ADDRESS"));
////                                HTalkApplication.getApplication().setMTalkServiceId(mapID.get(merchant_id));
////                                mapID.put(merchant_id, HTalkApplication.getApplication().getmTalkServiceId());
//                                setTalkID(mapID.get(merchant_id));
//                                if (TextUtils.isEmpty(HTalkApplication.getApplication().getmTalkServiceId())) {
//                                    chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_people));
//                                } else {
//                                    chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_ms));
//
//                                }
//                            }
//                            mPop.dismiss();
//                        }
//                    });
//                    pop_change_ll.addView(img, params);
//                }
//                mPop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//                PopUtil.showPop(getWindow(), mPop, findViewById(R.id.gray_title_layout), TITLE_CHANGE);
//                break;
//            case LOGIN_OUT:
////                popView = mInflater.inflate(R.layout.layout_pop_login_out, null);
////                pop_out_cancle = (TextView) popView.findViewById(R.id.pop_out_cancle);
////                pop_out_cancle.setOnClickListener(this);
////                pop_out_sure = (TextView) popView.findViewById(R.id.pop_out_sure);
////                pop_out_sure.setOnClickListener(this);
////                mPop = new PopupWindow(popView, mScreen.getWidth() - UnitUtil.dip2px(this, 50), ViewGroup.LayoutParams.WRAP_CONTENT, true);
////                mPop.setAnimationStyle(R.style.PopScreenShow);
////                PopUtil.showPop(getWindow(), mPop, findViewById(R.id.layout_title), LOGIN_OUT);
////                mPop.showAtLocation(mInflater.inflate(R.layout.act_chat, null), Gravity.CENTER, 0, 0);
//                break;
//            case VOICE_TEXT:
////                popView = mInflater.inflate(R.layout.layout_pop_voice2text, null);
////                popView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        mPop.dismiss();
////                    }
////                });
////                pop_voice_text = (TextView) popView.findViewById(R.id.pop_voice_text);
////                mPop = new PopupWindow(popView, mScreen.getWidth(), mScreen.getHeight() - getStatusBarHeight(), true);
////                mPop.setAnimationStyle(R.style.PopScreenShow);
////                mPop.showAtLocation(mInflater.inflate(R.layout.act_chat, null), Gravity.NO_GRAVITY, 0, getStatusBarHeight());
//                break;
//        }
//    }
//
//    /**
//     * 获取状态栏高度
//     *
//     * @return
//     */
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//    /**
//     * 有未读消息就进行更新
//     */
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        AppData appData = HTalkApplication.getAppData();
//        if (appData.getNoReadMesgSize2() != 0) {
//            mChatAapter.addAll(appData.messageNoRead);
//            appData.clearAll();
//        }
//        if (!HStringUtil.isEmpty(appData.getToAutoContent())) {
//            try {
//                mJsonObject.put("phonetype", "555");
//                onMessageSend(appData.getToAutoContent());
//                mJsonObject.put("phonetype", "");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            HTalkApplication.getAppData().clearToAutoContent();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mChatAapter.stopRecoard();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        CoreServiceB.actionLogOut(this);
//        ChatActivityB.this.stopService(new Intent(this, CoreServiceB.class));
//        mChatAapter.destorySpeed();
//        EventBus.getDefault().unregister(this);
//        if (mHandler != null)
//            mHandler.removeCallbacksAndMessages(null);
//        if (mIat != null) {
//            mIat.cancel();
//            mIat.destroy();
//            mIat = null;
//        }
//    }
//
//    private void messageFromSelf(String content) {
//        mChatAapter.sendMsgToService(content);
//    }
//
//    /**
//     * 跳转到人体
//     *
//     * @param sex         性别
//     * @param symptomCode 症状编码
//     */
//    private void onStartBoadyActivity(int sex, String symptomCode) {
//        Intent intent = new Intent(this, HumanBodyActivity.class);
//        intent.putExtra("sex", sex);
//        intent.putExtra("code", symptomCode);
//        startActivityForResult(intent, 100);
//    }
//
//    private int screenHeight;
//    private int keyHeight;
//
//    private void listenerSoftInput() {
//        //获取屏幕高度
//        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
//        //阀值设置为屏幕高度的1/3
//        keyHeight = screenHeight / 3;
//        final View activityRootView = findViewById(R.id.chat_root_view);
//        activityRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
//                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//                    chat_info_lv.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mListView.setSelection(mChatAapter.getCount());
//                        }
//                    }, 50);
//                    //KLog.d("键盘弹出");
//                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//                }
//            }
//        });
//    }
//
//    /**
//     * 隐藏展开项
//     */
//    public void hideMoreProject() {
//        IS_SHOW_B = false;
//        chat_bottom_project.setVisibility(View.GONE);
//        chat_send_project.setImageDrawable(this.getResources().getDrawable(R.drawable.chat_bottom_project));
//    }
//
//    /**
//     * 显示展开项
//     */
//    public void showBottomProject() {
//        IS_SHOW_B = true;
//        chat_send_project.setImageDrawable(this.getResources().getDrawable(R.drawable.chat_bottom_project_up));
//        chat_bottom_project.setVisibility(View.VISIBLE);
//        SystemUtils.hideSoftBord(ChatActivityB.this, chat_input_text);
//    }
//
//    RelativeLayout relativeLayoutSound;
//
//
//    /**
//     * 隐藏语音//5月16号
//     */
//    public void hideSound() {
//        if (relativeLayoutSound == null) {
//            relativeLayoutSound = (RelativeLayout) findViewById(R.id.chat_sound_layout);
//        }
//        relativeLayoutSound.setVisibility(View.GONE);
//        //  SystemUtils.openSoftBord(this, chat_input_text);
//    }
//
//    /**
//     * 切换语音//5月16号
//     */
//    public void changeSound() {
//        if (relativeLayoutSound == null) {
//            relativeLayoutSound = (RelativeLayout) findViewById(R.id.chat_sound_layout);
//        }
//        if (relativeLayoutSound.getVisibility() == View.GONE) {
//
//            relativeLayoutSound.setVisibility(View.VISIBLE);
//            SystemUtils.hideSoftBord(this, chat_input_text);
//            hideMoreProject();
//            hideProject();
//        } else {
//            relativeLayoutSound.setVisibility(View.GONE);
//            SystemUtils.openSoftBord(this, chat_input_text);
//
//        }
//    }
//
//
//    /**
//     * 切换功能按钮5月16号
//     */
//    public void changeProject() {
////        if (linearLayoutChatPoject.getVisibility() == View.GONE) {
////
////            linearLayoutChatPoject.setVisibility(View.VISIBLE);
////            SystemUtils.hideSoftBord(this, chat_input_text);
////            hideSound();
////            hideMoreProject();//5月17号
////        } else {
////            linearLayoutChatPoject.setVisibility(View.GONE);
////            hideMoreProject();
////        }
//    }
//
//    /**
//     * 隐藏功能按钮
//     * 5月16号
//     */
//    public void hideProject() {
//        linearLayoutChatPoject.setVisibility(View.GONE);
//
//    }
//
//    // 语音听写对象
//    private SpeechRecognizer mIat;
//    private SharedPreferences mSharedPreferences;
//    /**
//     * 初始化监听器。
//     */
//    private InitListener mInitListener = new InitListener() {
//
//        @Override
//        public void onInit(int code) {
//            if (code != ErrorCode.SUCCESS) {
////                ToastUtil.showToastPanl("初始化失败，错误码：" + code);
////                mTalkRipView.setVisibility(View.GONE);
////                ToastUtil.showShort(ChatActivityB.this, "初始化失败，错误码：" + code);
////				showTip("初始化失败，错误码：" + code);
//            }
//        }
//    };
//
//    /**
//     * 参数设置
//     *
//     * @param
//     * @return
//     */
//    public void setParam() {
//        // 清空参数
//        mIat.setParameter(SpeechConstant.PARAMS, null);
//
//        // 设置听写引擎
//        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//        // 设置返回结果格式
//        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
//        if (lag.equals("en_us")) {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
//        } else {
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
//        }
//
//        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "40000"));
//
//        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "30000"));
//
//        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
//        // 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
////        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");
//
//        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
//        // 注：该参数暂时只对在线听写有效
//        mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
//    }
//
//    RecognizerListener mRecognizerListener = new RecognizerListener() {
//        @Override
//        public void onVolumeChanged(int i, byte[] bytes) {
//
////            mChatVm.setMediaRecord(i);
//        }
//
//        @Override
//        public void onBeginOfSpeech() {
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//        }
//
//        @Override
//        public void onResult(RecognizerResult recognizerResult, boolean b) {
//            if (b) {
//                mChatAapter.isSoftword = false;
//                SystemUtils.hideSoftBord(ChatActivityB.this, chat_input_text);
//                hideMoreProject();
//                hideSound();
////                voiceAction();
//
//
//                StringBuffer resultBuffer = new StringBuffer();
//                for (String key : mIatResults.keySet()) {
//                    resultBuffer.append(mIatResults.get(key));
//                }
//                //LogUtil.d("----------------------", "--------" + resultBuffer.toString());
//                chat_input_text.setText(resultBuffer.toString());
//                chat_input_text.setSelection(chat_input_text.length());
//                //5月16号
//                if (mLodingFragmentDialog != null) {
//
//                    mLodingFragmentDialog.dismissAllowingStateLoss();
//                }
//            } else {
//                printResult(recognizerResult);
//            }
//
//        }
//
//        @Override
//        public void onError(SpeechError speechError) {
////            mTalkRipView.setVisibility(View.GONE);
//            if (mLodingFragmentDialog != null) {
//                mLodingFragmentDialog.dismissAllowingStateLoss();
//            }
//        }
//
//        @Override
//        public void onEvent(int i, int i1, int i2, Bundle bundle) {
//
//        }
//    };
//
//
//    public interface DoctorInputControlListerner {
//        void onMessageSend(String content);
//
//        void onSoftWord(boolean isSoft);
//    }
//
//    private void initVoiceData() {
//        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
//        // 初始化识别无UI识别对象
//        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
//        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
//        mSharedPreferences = this.getSharedPreferences(PREFER_NAME, Activity.MODE_PRIVATE);
//        // 设置参数
//        setParam();
//    }
//
//    private final String PREFER_NAME = "com.iflytek.setting";
//    // 引擎类型
//    private String mEngineType = SpeechConstant.TYPE_CLOUD;
//    // 用HashMap存储听写结果
//    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
//
//    private void printResult(RecognizerResult results) {
////        RingPlayer.playPressSound(mActivity);
//        String text = JsonParser.parseIatResult(results.getResultString());
//
//        String sn = null;
//        // 读取json结果中的sn字段
//        try {
//            JSONObject resultJson = new JSONObject(results.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mIatResults.put(sn, text);
//
//
//    }
//
//
//    private void upBitmap(final Bitmap bitmap2) {
//        mLodingFragmentDialog = LodingFragmentDialogB.showLodingDialog(getSupportFragmentManager(), "");
//        final RequestParams params = new RequestParams();
//        params.put("serverId", HTalkApplication.getApplication().getmTalkServiceId());//消息返回状态id,与前台id进行对应
//        params.put("sms_req_content", "");//接受者
//        params.put("customerId", mUserId);//发送方
//        params.put("isGroupMessage", "0");
//        params.put("MERCHANT_ID", merchant_id);
//        params.put("timeStamp", String.valueOf(System.currentTimeMillis()));
//        params.put("sms_target_id", HTalkApplication.getApplication().getmTalkServiceId());
////        params.put("sms_target_id", mReciverId);
//        params.put("type", sendBitmapType);
//        params.put("duration", String.valueOf(0));
////ValidFlag      --1-客户输入，2-客服输入的
//        params.put("ValidFlag", 1 + "");
//        params.put("SERVICE_TYPE", "99999");
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                Bitmap bitmap = Bimp.compressBitmap(bitmap2, 200);
//                String str = new Date().getTime() + ".jpg";
//                Bimp.saveBitmap(str, bitmap);
//                File picFile = new File("/sdcard/XIAOYI/" + str);
//
//                try {
//                    if (picFile != null && picFile.exists()) {
//                        JSONObject js = new JSONObject();
//                        js.put("height", bitmap.getHeight());
//                        js.put("width", bitmap.getWidth());
//                        params.put("image", picFile);
//                        params.put("dataHolder", js.toString());
//                    } else {
//                        params.putNullFile("image", new File(""));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                HttpRestClientB.mAsyncHttpClient.post(HttpRestClientB.getmHttpUrlsB().UPBITMAP
//                        , params, new AsyncHttpResponseHandler() {
//
//
//                            @Override
//                            public void onSuccess(int statusCode, String content) {
//                                super.onSuccess(statusCode, content);
//                                mLodingFragmentDialog.dismissAllowingStateLoss();
//
//                                try {
//                                    MessageEntity messageEntity = new MessageEntity();
//                                    messageEntity.setSendState(MessageEntity.STATE_OK);
//                                    messageEntity.setSenderId(mUserId);
////                            messageEntity.setReceiverId(mReciverId);
//                                    messageEntity.setSendFlag(true);
//                                    //  messageEntity.setId("-1");
//                                    messageEntity.setMerchant_head(merchant_head);
//                                    messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//
//                                    JSONObject jsonResult = null;
//
//
//                                    jsonResult = new JSONObject(content);
//                                    if (jsonResult.getString("code").equals("1")) {
//                                        jsonResult = jsonResult.getJSONObject("result");
//                                        messageEntity.setContent(jsonResult.getString("sms_req_content"));
//
////                                HTalkApplication.getApplication().setMTalkServiceId(jsonResult.getString("sms_target_id"));
////                                mapID.put(merchant_id, HTalkApplication.getApplication().getmTalkServiceId());
//
//                                        boolean isHaveTalkId = !"".equals(HTalkApplication.getApplication().getmTalkServiceId());
//                                        setTalkID(jsonResult.getString("sms_target_id"));
//                                        messageEntity.setReceiverId(HTalkApplication.getApplication().getmTalkServiceId());
//
//
//                                        String type = "1";
//                                        if (!HStringUtil.isEmpty(type)) {
//
//                                            messageEntity.setType(Integer.parseInt(type));
//                                        } else {
//                                            messageEntity.setType(MessageEntity.TYPE_TEXT);
//                                        }
//
//                                        messageEntity.setSendFlag(true);
//                                        messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//
//                                        messageEntity.setMerchant_head(merchant_head);
//
//                                        Message message = mHandler.obtainMessage();
//                                        message.obj = messageEntity;
//                                        message.what = 4;
//                                        mHandler.sendMessage(message);
//                                        if (!isHaveTalkId) {
//
//                                            Message messageT = mHandler.obtainMessage();
//                                            messageT.obj = new JSONObject(content).optString("message");
//                                            messageT.what = 7;
//                                            mHandler.sendMessage(messageT);
//
//                                        }
//                                    } else {
//
//                                        Message message = mHandler.obtainMessage();
//                                        message.obj = jsonResult.getString("message");
//                                        message.what = 5;
//                                        mHandler.sendMessage(message);
//                                    }
//
//                                } catch (JSONException e) {
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Throwable error, String content) {
//                                super.onFailure(error, content);
//                                mLodingFragmentDialog.dismissAllowingStateLoss();
//                                Message message = mHandler.obtainMessage();
//                                message.obj = "发送失败!";
//                                message.what = 5;
//                                mHandler.sendMessage(message);
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                            }
//
//                            @Override
//                            public void onStart() {
//                                super.onStart();
//                            }
//                        });
//            }
//        }.start();
//
//
//    }
//
//    private LodingFragmentDialogB mLodingFragmentDialog;
//
//    /**
//     * 切换客服机器人
//     */
//    private void changeT() {
//        mLodingFragmentDialog = LodingFragmentDialogB.showLodingDialog(getSupportFragmentManager(), "");
//        RequestParams params = new RequestParams();
//        if (!(boolean) chart_change.getTag()) {
//
//            params.put("Type", "xiaoyi_kefu_fenpei_99999_pb");
//        } else {
//            params.put("Type", "updateGxStatus");
//
//        }
//        params.put("MERCHANT_ID", merchant_id);
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        HttpRestClientB.doHttpChange(params, new JsonHttpResponseHandler(null) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                mLodingFragmentDialog.dismiss();
//                try {
//                    if (response.getString("code").equals("1")) {
//                        if (!(boolean) chart_change.getTag()) {
//                            HTalkApplication.getApplication().setMTalkServiceId(response.getJSONObject("result").getString("redflag"));
//                            chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_ms));
//                            addliftMessage(response.getJSONObject("result").getString("retgretting"));
//                        } else {
//                            HTalkApplication.getApplication().setMTalkServiceId("");
//                            addliftMessage(response.getString("message"));
//                            chart_change.setImageDrawable(getResources().getDrawable(R.drawable.change_people));
//                        }
//                        mapID.put(merchant_id, HTalkApplication.getApplication().getmTalkServiceId());
//                        chart_change.setTag(!(boolean) chart_change.getTag());
//                    } else {
//                        //ToastUtil.showShort(ChatActivityB.this, response.getString("message"));
//                        addliftMessage(response.getString("message"));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }
//
//    private void addliftMessage(String text) {
//        MessageEntity messageEntity = new MessageEntity();
//        messageEntity.setSendState(MessageEntity.STATE_OK);
////                    messageEntity.setSenderId(mReciverId);
//        messageEntity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//        messageEntity.setReceiverId(mUserId);
//        messageEntity.setType(MessageEntity.TYPE_TEXT);
//        messageEntity.setSendFlag(false);
//        messageEntity.setContent(text);
//        //  messageEntity.setContent(HStringUtil.chatRadom());
//        messageEntity.setId("-1");
//        messageEntity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//        messageEntity.setMerchant_head(merchant_head);
//        if (!TextUtils.isEmpty(messageEntity.getContent())) {
//            mChatAapter.addData(messageEntity);
//        }
//        mListView.setSelection(mListView.getCount());
//    }
//
//    public void setTalkID(String id) {
//        Message message = mHandler.obtainMessage();
//        message.obj = id;
//        message.what = 6;
//        mHandler.sendMessage(message);
//
//    }
//
//
//    private View mTalkRipView;
//    private RippleLayout mTalkBtn;
//
//
//    String cameraName;
//
//    public void takeCamera() {
//        cameraName = new Date().getTime() + "";
//        String status = Environment.getExternalStorageState();
//        if (status.equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                File dir = new File(Environment.getExternalStorageDirectory() + "/" + "XiaoYi");
//                if (!dir.exists()) dir.mkdirs();
//
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                File f = new File(dir, cameraName);//localTempImgDir和localTempImageFileName是自己定义的名字
//                Uri u = Uri.fromFile(f);
//                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
//                startActivityForResult(intent, RESULT_LOAD_CAMERA);
//            } catch (ActivityNotFoundException e) {
//// TODO Auto-generated catch block
//                Toast.makeText(ChatActivityB.this, "没有找到储存目录", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            Toast.makeText(ChatActivityB.this, "没有储存卡", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//
//}
