package com.yksj.healthtalk.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yksj.consultation.son.chatting.ChatHelperUtils;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.friend.FriendHttpListener;


/**
 * @author Administrator 朋友圈网络请求
 */
public class FriendHttpUtil {

    /**
     * 各种按条件找病友都要用的一个方法
     *
     * @param c
     * @param pageSize            第几页,还一个参数是pageNum 默认每页使显示20条数据
     * @param mFriendHttpListener
     */
    public static void getFriends(final CustomerInfoEntity c,
                                  final int pageSize, final FriendHttpListener mFriendHttpListener) {
        HttpRestClient.doHttpRequestSearchFriends2(c, pageSize,
                new ObjectHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Object response) {
                        if (response instanceof ArrayList) {
                            mFriendHttpListener.responseSuccess(c.getType(), pageSize,
                                    (ArrayList<CustomerInfoEntity>) response);
                        }
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public Object onParseResponse(String content) {
                        return jsonAnalysisFriendEntity(content, false);
                    }
                });
    }

    /**
     * 只有找病友主界面使用
     *
     * @param c
     * @param sourceType          0--最新 1--活跃 2--推荐 3--附近 4--同城
     * @param mFriendHttpListener 加载数据成功后的回调
     */
    public static void getFriendsByType(final CustomerInfoEntity c,
                                        final int sourceType, final FriendHttpListener mFriendHttpListener) {
        HttpRestClient.doHttpRequestSearchFriends1(c, sourceType,
                new ObjectHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Object response) {
                        if (response instanceof ArrayList) {
                            mFriendHttpListener.responseSuccess(c.getType(), sourceType,
                                    (ArrayList<CustomerInfoEntity>) response);
                        }
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public Object onParseResponse(String content) {
                        return jsonAnalysisFriendEntity(content, false);
                    }
                });
    }

    public static ArrayList<CustomerInfoEntity> jsonAnalysisFriendEntity(
            String content, Boolean hasMyself) {
        ArrayList<CustomerInfoEntity> friendEntities = new ArrayList<CustomerInfoEntity>();
        try {
            JSONTokener jsonTokener = new JSONTokener(content);
            Object valueObject = jsonTokener.nextValue();
            JSONArray mJsonArray;
            if (valueObject instanceof JSONObject) {
                mJsonArray = new JSONArray();
                if (((JSONObject) valueObject).has("findCustomerByName")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findCustomerByName"));
                } else if (((JSONObject) valueObject).has("findSalonCustomers")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findSalonCustomers"));
                } else if (((JSONObject) valueObject).has("findMyList")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findMyList"));
                } else {
                    mJsonArray.put((JSONObject) valueObject);
                }
            } else {
                mJsonArray = (JSONArray) valueObject;
            }
            CustomerInfoEntity mCustomerInfoEntity;
            int length = mJsonArray.length();
            friendEntities.ensureCapacity(length);
            for (int i = 0; i < length; i++) {
                JSONObject object = mJsonArray.getJSONObject(i);
                mCustomerInfoEntity = DataParseUtil.jsonToCustmerInfo(object);
                friendEntities.add(mCustomerInfoEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendEntities;
    }

    public static ArrayList<CustomerInfoEntity> jsonAnalysisExpertData(String content, Boolean hasMyself) {
        ArrayList<CustomerInfoEntity> friendEntities = new ArrayList<CustomerInfoEntity>();
        try {
            JSONTokener jsonTokener = new JSONTokener(content);
            Object valueObject = jsonTokener.nextValue();
            JSONArray mJsonArray;
            if (valueObject instanceof JSONObject) {
                mJsonArray = new JSONArray();
                if (((JSONObject) valueObject).has("findCustomerByName")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findCustomerByName"));
                } else if (((JSONObject) valueObject).has("findSalonCustomers")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findSalonCustomers"));
                } else if (((JSONObject) valueObject).has("findMyList")) {
                    mJsonArray = (((JSONObject) valueObject).getJSONArray("findMyList"));
                } else {
                    mJsonArray.put((JSONObject) valueObject);
                }
            } else {
                mJsonArray = (JSONArray) valueObject;
            }
            CustomerInfoEntity mCustomerInfoEntity;
            int length = mJsonArray.length();
            friendEntities.ensureCapacity(length);
            for (int i = 0; i < length; i++) {
                JSONObject object = mJsonArray.getJSONObject(i);
                mCustomerInfoEntity = DataParseUtil.jsonToCustmerInfo(object);
                friendEntities.add(mCustomerInfoEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendEntities;
    }

    /**
     * @param context
     * @param content
     * @param friendType 好友类型
     * @return
     */
    public static ArrayList<CustomerInfoEntity> jsonAnalysisFriendEntity(
            Context context, String content, int friendType) {
        ArrayList<CustomerInfoEntity> friendEntities = new ArrayList<CustomerInfoEntity>();
        ChatUserHelper helper = ChatUserHelper.getInstance();
        try {
            JSONTokener jsonTokener = new JSONTokener(content);
            Object valueObject = jsonTokener.nextValue();
            JSONArray mJsonArray;
            if (valueObject instanceof JSONObject) {
                mJsonArray = new JSONArray();
                mJsonArray.put((JSONObject) valueObject);
            } else {
                mJsonArray = (JSONArray) valueObject;
            }
            helper.initPersonInfo(mJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friendEntities;
    }

    /**
     * @param @param group 所在组的ID
     * @param @param child 所在子列表的ID
     * @param @param type 0是取消关注 1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户 6 我的医生
     *               7取消医生 8添加合作者
     * @param @param returnInfo 是否返回全部资料
     * @return void
     * @throws
     * @Title: requestHttpAboutFriend
     * @Description: 取消关注和取消黑名单的http请求
     */
    public static void requestHttpAboutFriend(int userId,
                                              CustomerInfoEntity entity, int type) {
//		entity.setAttentionFriendFlag(type);
        entity.setIsAttentionFriend(type);
        JSONObject json = new JSONObject();
        try {
            json.put("0", type);
            json.put("1", userId);
            json.put("2", entity.getId());
            json.put("3", 0);
            json.put("4", "40");
            SmartFoxClient.sendStrRequest(
                    SmartFoxClient.RequestCode.CODE_COLLECT_FRIEND,
                    json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对六一健康的关注与取消关注医生
     *
     * @param entity
     * @param type   1-收藏好友 0-取消收藏好友  Y
     *               2-加入黑名单  3-移除黑名单  B
     *               4-加为患者  5 -取消患者 C
     *               6 -收藏医生  7-取消收藏医生  D
     */
    public static CustomerInfoEntity requestAttentionDoctor(CustomerInfoEntity entity) {
//		entity.setAttentionFriendFlag(type);
        int mUserId = Integer.valueOf(SmartFoxClient.getLoginUserId());
        int type = 0;
        if ("D".equals(entity.getRelType())) {
            type = 7;
            entity.setRelType("");
        } else {
            type = 6;
            entity.setRelType("D");
        }
        JSONObject json = new JSONObject();
        try {
            json.put("0", type);
            json.put("1", mUserId);
            json.put("2", entity.getId());
            json.put("3", 0);
            json.put("4", "40");
            SmartFoxClient.sendStrRequest(
                    SmartFoxClient.RequestCode.CODE_COLLECT_FRIEND, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * @param @param group 所在组的ID
     * @param @param child 所在子列表的ID
     * @param @param type 0是取消关注 1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户
     * @param @param returnInfo 0不返回全部资料 1 返回
     * @return void
     * @throws
     * @Title: requestHttpAboutFriend
     * @Description: 取消关注和取消黑名单的http请求
     */
    public static void requestHttpAboutFriend(int userId,
                                              CustomerInfoEntity entity, int type, int returnInfo) {
//		entity.setAttentionFriendFlag(type);
        entity.setIsAttentionFriend(type);
        JSONObject json = new JSONObject();
        try {
            json.put("0", type);
            json.put("1", userId);
            json.put("2", entity.getId());
            json.put("3", returnInfo);
            SmartFoxClient.sendStrRequest(
                    SmartFoxClient.RequestCode.CODE_COLLECT_FRIEND,
                    json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关注好友
     *
     * @param context
     * @param userid
     * @param friendid
     * @param flag             是否是加入黑名单
     * @param entity
     * @param showLodingDialog
     * @return 0是取消关注 1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户 6 我的医生 7取消医生 8添加合作者
     */
    public static CustomerInfoEntity requestAttentionTofriends(Context context,
                                                               Boolean flag, CustomerInfoEntity entity) {
        if (flag == null) {
            flag = false;//默认不加入黑名单
        }
        // 关系 关系 0 没有关系 1是关注的 2是黑名单 3是客户 4医生
        int isFlage = entity.getIsAttentionFriend();
        int mUserId = Integer.valueOf(SmartFoxClient.getLoginUserId());
        if (flag == false) {//拉黑之外的操作
            if (isFlage == 0) {//没有关系,直接操作
                if (SmartFoxClient.getLoginUserInfo().isDoctor()) {//我是医生,对方肯定是我的患者
                    FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 4);
                } else {//我是患者
                    if (entity.isDoctor())//对方是医生,为我的医生
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 6);
                    else//对方也是患者,直接加好友
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 1);
                }
            } else if (isFlage == 1) {//正在关注---->取消关注
                FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 0);
            } else if (isFlage == 2) {//取消拉黑 并且加关注
                DialogUtils.PromptDialogBox(context, context.getString(R.string.att_blacklist), entity);
            } else if (isFlage == 3) {//我的客户,要做取消关注操作
                DialogUtils.PromptDialogBox(context, context.getString(R.string.att_client), entity);
            } else if (isFlage == 4) {
                FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 7);
            } else if (isFlage == 5) {// 取消我的合作者
                FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 9);
            }
        } else {// 拉黑操作
            switch (isFlage) {// 关系 关系 0 没有关系 1是关注的 2是黑名单 3是客户 4医生
                case 0://加入黑名单操作
                    FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 2);
                    break;
                case 1://关注 到 黑名单
                    DialogUtils.attToBlacklistDialog(context, entity);
                    break;
                case 2://取消黑名操作
                    FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 3);
                    break;
                case 3://关注 到 黑名单
//				DialogUtils.PromptDialogBox(context,context.getString(R.string.att_client), entity);
                    DialogUtils.attToBlacklistDialog(context, entity);
                    break;
                case 4://加入黑名单操作
                    FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 2);
                    break;
            }
        }
        return entity;
    }

    /**
     * 没有关系直接关注
     * 三种情况:
     * 1-我的医生  (我的患者,对方是医生)
     * 2-我的患者 (我的医生,对方是患者)
     * 3-我的病友 (我的患者,对方也是患者)
     */
    public static void followImmediate() {

    }

    /**
     * 选择弹出框
     */
    public static void showuploadPopWindow(Context context,
                                           final CustomerInfoEntity entity) {
        final int mUserId = Integer.valueOf(SmartFoxClient.getLoginUserId());
        LayoutInflater inflater = LayoutInflater.from(context);
        Button mydoctor = null;
        Button friend = null;
        Button cancel = null;
        Button collaborators = null;
        Button client = null;
        boolean mUserIsDoctor = SmartFoxClient.getLoginUserInfo().isDoctor();
        View popView = null;
        if (mUserIsDoctor || entity.isDoctor()) {
            if (mUserIsDoctor && entity.isDoctor()) {// 医生加医生
                popView = inflater.inflate(R.layout.change_relation_popwindow_d_d, null);
                ViewFinder finder = new ViewFinder(popView);
                mydoctor = finder.button(R.id.my_doctor);
                friend = finder.button(R.id.my_friend);
                cancel = finder.button(R.id.cancel);
                collaborators = finder.button(R.id.my_hezuozhe);
                client = finder.button(R.id.extraadd);

            } else if (mUserIsDoctor && !entity.isDoctor()) {// 医生加患者
                popView = inflater.inflate(R.layout.change_relation_popwindow_layout, null);
                ViewFinder finder = new ViewFinder(popView);
                friend = finder.button(R.id.my_friend);
                cancel = finder.button(R.id.cancel);
                client = finder.button(R.id.extraadd);

            } else if (entity.isDoctor() && !mUserIsDoctor) {// 患者加医生
                popView = inflater.inflate(R.layout.change_relation_popwindow_d_c, null);
                ViewFinder finder = new ViewFinder(popView);
                mydoctor = finder.button(R.id.my_doctor);
                friend = finder.button(R.id.my_friend);
                cancel = finder.button(R.id.cancel);
            }
            final PopupWindow mPopupWindow = new PopupWindow(popView,
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            WheelUtils.setPopeWindow(context, popView, mPopupWindow);
            // 监听器
            // 我的患者
            if (client != null)
                client.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 4);
                        mPopupWindow.dismiss();
                    }
                });

            // 我的合作者
            if (collaborators != null)
                collaborators.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 8);
                        mPopupWindow.dismiss();
                    }
                });
            // 我的医生
            if (mydoctor != null)
                mydoctor.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 6 我的医生 7取消医生
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 6);
                        mPopupWindow.dismiss();
                    }
                });

            // 我的病友
            if (friend != null)
                friend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 1);
                        mPopupWindow.dismiss();
                    }
                });
            // 取消
            if (cancel != null)
                cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
        } else {
            // 对方不是医生 不提示 直接加为我的好友
            FriendHttpUtil.requestHttpAboutFriend(mUserId, entity, 1);
        }
    }

    /**
     * 关注 黑名单 客户 操作的 结果处理
     *
     * @param context
     * @param entity
     * @param mAppData 0是取消关注 1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户
     */
    public static void requestAttentionTofriendsResult(Context context,
                                                       CustomerInfoEntity entity) {
        if (entity == null) return;
        ToastUtil.showToastPanl("修改成功");
        // 1是关注 2是加入黑名单 3是取消黑名单 4 收藏客户 5 是取消收藏客户 6加医生
        // 7取消医生 8我的合作者 9取消我的合作者
        int attentionFlag = entity.getIsAttentionFriend();
        switch (attentionFlag) {
            case 0:
            case 5:
            case 3:
            case 7:
            case 9:
                entity.setIsAttentionFriend(0);//没有关系
                break;
            case 1://是关注的
                entity.setIsAttentionFriend(1);
                break;
            case 2:// 黑名单
                entity.setIsAttentionFriend(2);
                break;
            case 4:
                entity.setIsAttentionFriend(3);
                break;
            case 6:
                entity.setIsAttentionFriend(4);
                break;
            case 8:
                entity.setIsAttentionFriend(5);
                break;
        }
        //0 陌生 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者 6 助理 7相互关注 8 小壹 9我关注的话题  10 我创建的话题
        ChatUserHelper.getInstance().changeRelType(entity);
    }

    /**
     * 点击聊天
     *
     * @param context
     * @param entity
     * @param manager
     * @param listener
     */
    public static void chatFromPerson(final FragmentActivity activity,
                                      final CustomerInfoEntity entity) {
        ChatHelperUtils.chatFromPerson(activity, entity);
    }

    ;

    public static void chatFromPerson(final FragmentActivity activity,
                                      final String id, final String name) {
        ChatHelperUtils.chatFromPerson(activity, id, name);
    }
  public static void chatFromPerson(final FragmentActivity activity,
                                      final String id, final String name, String orderId,String objectType) {
        ChatHelperUtils.chatFromPerson(activity, id, name, orderId,objectType);
    }  public static void chatFromPerson(final Activity activity,
                                      final String id, final String name, String orderId,String objectType) {
        ChatHelperUtils.chatFromPerson(activity, id, name, orderId,objectType);
    }

    ;

    /**
     * 我的医生
     *
     * @param context
     * @param object      myDoctors我关注的医生 myReceDoctors我最近联系的医生 myRegDoctors我的挂号医生
     *                    myRegHisDoctors我的历史挂号医生 "myBuyFailureDoctorsName": "支付失败医生",
     * @param nameIndexs
     * @param nameByIndex
     * @param nameIndexs
     * @param nameByIndex
     * @param keyByIndex
     * @return
     */
    public static LinkedHashMap<String, ArrayList<CustomerInfoEntity>> getMyDoctors(
            Context context, JSONObject object, ArrayList<Integer> nameIndexs,
            Map<Integer, String> nameByIndex) {
        Map<Integer, String> keyByIndex = new HashMap<Integer, String>();// key
        // 各项的Index
        // value
        // 惊悚数据每个列表对应的key
        // 我最近联系的医生
        if (object.has("myReceDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myReceDoctorsIndex"),
                        object.getString("myReceDoctorsName"));
                keyByIndex.put(object.getInt("myReceDoctorsIndex"),
                        "myReceDoctors");
                if (!nameIndexs.contains(object.getInt("myReceDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myReceDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 我的医生
        if (object.has("myDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myDoctorsIndex"),
                        object.getString("myDoctorsName"));
                keyByIndex.put(object.getInt("myDoctorsIndex"), "myDoctors");
                if (!nameIndexs.contains(object.getInt("myDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 挂号医生
        if (object.has("myRegDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myRegDoctorsIndex"),
                        object.getString("myRegDoctorsName"));
                keyByIndex.put(object.getInt("myRegDoctorsIndex"),
                        "myRegDoctors");
                if (!nameIndexs.contains(object.getInt("myRegDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myRegDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 历史挂号医生
        if (object.has("myRegHisDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myRegHisDoctorsIndex"),
                        object.getString("myRegHisDoctorsName"));
                keyByIndex.put(object.getInt("myRegHisDoctorsIndex"),
                        "myRegHisDoctors");
                if (!nameIndexs.contains(object.getInt("myRegHisDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myRegHisDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 支付失败医生
        if (object.has("myBuyFailureDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myBuyFailureDoctorsIndex"),
                        object.getString("myBuyFailureDoctorsName"));
                keyByIndex.put(object.getInt("myBuyFailureDoctorsIndex"),
                        "myBuyFailureDoctors");
                if (!nameIndexs.contains(object
                        .getInt("myBuyFailureDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myBuyFailureDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 超时支付
        if (object.has("myRiskChargeDoctorsName")) {
            try {
                nameByIndex.put(object.getInt("myRiskChargeDoctorsIndex"),
                        object.getString("myRiskChargeDoctorsName"));
                keyByIndex.put(object.getInt("myRiskChargeDoctorsIndex"),
                        "myRiskChargeDoctors");
                if (!nameIndexs.contains(object
                        .getInt("myRiskChargeDoctorsIndex"))) {
                    nameIndexs.add(object.getInt("myRiskChargeDoctorsIndex"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(nameIndexs);
        LinkedHashMap<String, ArrayList<CustomerInfoEntity>> hashMap = new LinkedHashMap<String, ArrayList<CustomerInfoEntity>>();
        JSONArray array;
        try {
            for (int i = 0; i < nameIndexs.size(); i++) {
                array = object.getJSONArray(keyByIndex.get(nameIndexs.get(i)));
                hashMap.put(
                        nameByIndex.get(nameIndexs.get(i)),
                        FriendHttpUtil.jsonAnalysisFriendEntity(
                                array.toString(), true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    //跳转导医护士
    public static void jumpChatFromDy(FragmentActivity context) {
        CustomerInfoEntity entity = new CustomerInfoEntity();
        entity.setName(HTalkApplication.getAppData().DYHSID_NAME);
        entity.setId(HTalkApplication.getAppData().DYHSID);
        chatFromPerson(context, entity);
    }

}
