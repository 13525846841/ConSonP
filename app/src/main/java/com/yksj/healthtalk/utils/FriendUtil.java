package com.yksj.healthtalk.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.TickMesg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wt
 * @ClassName: FriendUtil
 * @Description: 社交场的工具类
 * @date 2013-1-17 上午02:55:02
 */
public class FriendUtil {

    public static void showPopwindowDown(PopupWindow mPopupWindowDown,
                                         View whereDown) {
        mPopupWindowDown.setFocusable(true);
        mPopupWindowDown.setTouchable(true);
        mPopupWindowDown.setOutsideTouchable(true);
        mPopupWindowDown.setBackgroundDrawable(new BitmapDrawable());
        if (mPopupWindowDown.isShowing()) {

            mPopupWindowDown.dismiss();
        } else {
            mPopupWindowDown.showAsDropDown(whereDown);
        }

    }

    /**
     * @param @param  context
     * @param @param  size
     * @param @param  tag
     * @param @param  str
     * @param @param  v
     * @param @return
     * @return ArrayList<Button>
     * @throws
     * @Title: showSearchCategoryPopWindowDown
     * @Description:
     */
    public static void showSearchCategoryPopWindowDown(Context context,
                                                       String[] str, View v, ListView list, ArrayAdapter<String> ad) {
        // ArrayList<Button> btns = new ArrayList<Button>();
        // LinearLayout layout = new LinearLayout(context);
        // layout.setLayoutParams(new
        // ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        // layout.setBackgroundResource(R.drawable.popwindow_classify);
        // layout.setOrientation(LinearLayout.VERTICAL);
        // for (int i = 0; i < size; i++) {
        // Button button = new Button(context);
        // button.setBackgroundColor(Color.TRANSPARENT);
        // button.setText(str[i]);
        // button.setId(tag+i);
        // button.setTextColor(R.drawable.button);
        // layout.addView(button);
        // btns.add(button);
        // }
        if (ad == null) {
            ad = new ArrayAdapter<String>(context,
                    R.layout.friend_search_pop_item, R.id.pop_tv, str);
            list.setAdapter(ad);
        } else {
            ad.notifyDataSetChanged();
        }

        PopupWindow popupWindow = new PopupWindow(list,
                android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(v, -0, -10);
        }
    }

    /**
     * @param @param btn
     * @param @param text
     * @return void
     * @throws
     * @Title: setButtonText
     * @Description: 设置按钮的文字(图文混排)
     */
    public static void setButtonText(Context context, Button btn, String text) {
        btn.setText("");
        SpannableString spannableString = new SpannableString("right");
        Bitmap b = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.title_middle_triangle);
        ImageSpan imageSpan = new ImageSpan(b,
                DynamicDrawableSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btn.append(text);
        btn.append(spannableString);
    }

    /**
     * 使CustomerInfoEntity其他数据恢复默认 避免交叉查询
     *
     * @param type 1 找朋友 2 找医生 3 相同经历
     */
    public static void setCustomerInfoEntityOthersDefault(
            CustomerInfoEntity mCustomerInfoEntity, int type) {
        switch (type) {
            case 1:
                FindDoctorInfoEntityDefault(mCustomerInfoEntity);
                FindSameExperienceDefault(mCustomerInfoEntity);
                break;
            case 2:
                FindFriendInfoEntityDefault(mCustomerInfoEntity);
                FindSameExperienceDefault(mCustomerInfoEntity);
                break;
            case 3:
                FindFriendInfoEntityDefault(mCustomerInfoEntity);
                FindDoctorInfoEntityDefault(mCustomerInfoEntity);
                break;
            default:
                System.err
                        .println("FriendUtil --> setCustomerInfoEntityOthersDefault type is error");
                break;
        }
    }

    /**
     * 找朋友默认
     *
     * @param mCustomerInfoEntity
     */
    public static void FindFriendInfoEntityDefault(
            CustomerInfoEntity mCustomerInfoEntity) {
        mCustomerInfoEntity.setUsername("");
        mCustomerInfoEntity.setSex("?");
        mCustomerInfoEntity.setMinAge(0);
        mCustomerInfoEntity.setMaxAge(100);
        mCustomerInfoEntity.setAreaCode(0);
        mCustomerInfoEntity.setAreaName("");
        mCustomerInfoEntity.setInterestCode("");
    }

    /**
     * 找医生默认
     *
     * @param mCustomerInfoEntity
     */
    public static void FindDoctorInfoEntityDefault(
            CustomerInfoEntity mCustomerInfoEntity) {
        mCustomerInfoEntity.setRealname("");
        mCustomerInfoEntity.setOfficeCode1("0");
        mCustomerInfoEntity.setOfficeName1("");
        mCustomerInfoEntity.setSpecial("");
        mCustomerInfoEntity.setDoctorTitle("0");
        mCustomerInfoEntity.setHospital("");
        mCustomerInfoEntity.setAreaName("");
    }

    /**
     * 相同经历
     *
     * @param mCustomerInfoEntity
     */
    public static void FindSameExperienceDefault(
            CustomerInfoEntity mCustomerInfoEntity) {
        mCustomerInfoEntity.setSameExperienceCode(0);
    }

    /**
     * 获取二维码地址
     *
     * @param str
     */
    public static String getQrCode(String str) {
        // http://jiankangliao.com?code=32B991E5D77AD140559FFB95522992D0
        if (!TextUtils.isEmpty(str)) {
            String[] spr = str.split("=");
            return spr[1].split("&")[0];
        }
        return null;
    }

    /**
     * 获取医生客户端类型
     *
     * @param str
     */
    public static String getQrDoctorClientType(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] spr = str.split("=");
            return spr[2];
        }
        return null;
    }

    /**
     * 判断是否是六一健康的二维码
     *
     * @param str
     * @return
     */
    public static String isHealthQrCode(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.contains("yijiankang") || str.contains("jiankangliao.com")
                    || (str.contains(Configs.SOCKET_IP) && (str
                    .contains("code=") || str.contains("cm=")))) {
                if (str.contains("cm")) {
                    return "cm";// 商户的二维码
                } else if (str.contains("code")) {
                    return "code";// 人的二维码
//					{"content":"http://www.yijiankangv.com/DuoMeiHealth/YijiankangDimensionalCodeServlet","icon":"/yijiankangDimensionalCode/yijiankang.png","code":""}
                } else if (str.contains("yijiankang")) {//一健康的二维码
                    return "yijiankang";
                } else {
                    return null;
                }

            }
        }
        return null;
    }

    /*
     * 退号 判断支付宝账号
     */
    public static String inputVerify(String mess, String str, String again,
                                     String tel, int imagePosition) {
        if (TextUtils.isEmpty(mess)) {
            return "请填写退号原因";
        } else if (imagePosition == 1) {
            return "请选择一张服务截图";
        } else if (TextUtils.isEmpty(str)) {
            return "请输入您的支付宝账号";
        } else if (TextUtils.isEmpty(again)) {
            return "请再次输入您的支付宝账号";
        } else if (!str.equals(again)) {
            return "两次输入的支付宝账号不一致";
        } else if (TextUtils.isEmpty(tel)) {
            return "请输入您的联系电话";
        }
        return null;
    }

    /*
     * 退款 判断支付宝账号
     */
    public static String inputVerify(String str, String again, String tel) {
        if (TextUtils.isEmpty(str)) {
            return "请输入您的支付宝账号";
        } else if (TextUtils.isEmpty(again)) {
            return "请再次输入您的支付宝账号";
        } else if (!str.equals(again)) {
            return "两次输入的支付宝账号不一致";
        } else if (TextUtils.isEmpty(tel)) {
            return "请输入您的联系电话";
        }
        return null;
    }

    /**
     * 进入聊天的按钮文字
     *
     * @param selfEntity
     * @param entity
     * @param appData
     * @return
     */
    public static String getChattingBtnText(CustomerInfoEntity selfEntity,
                                            CustomerInfoEntity entity) {
        if (isDoctor(selfEntity)) {
            return "对话";
        } else if (!isDoctor(selfEntity) && isDoctor(entity)) {
            return "看医生";
        } else {
            return "对话";
        }
    }

    public static Boolean isDoctor(CustomerInfoEntity entity) {
        if (entity.getRoldid() == 777 || entity.getRoldid() == 888) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到手机通讯录联系人信息
     *
     * @param mContext
     * @return
     */
    public static String getPhoneContacts(Context mContext) {
        Cursor cur = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        JSONArray array = new JSONArray();
        // ArrayList<String> list = new ArrayList<String>();
        while (cur.moveToNext()) {
            int columnId = cur.getColumnIndex(ContactsContract.Contacts._ID);
            int columnName = cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int columnPoneCount = cur
                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

            String id = cur.getString(columnId);
            String name = cur.getString(columnName); // 获取名称
            int count = cur.getInt(columnPoneCount);
            if (count > 0) {
                Cursor phone = mContext.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                + id, null, null);
                int columnPhoneNumber = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int columnPhoneType = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                while (phone.moveToNext()) {
                    String phoneNumber = phone.getString(columnPhoneNumber); // 获取手机号
                    String phoneType = phone.getString(columnPhoneType); // 获取类型
                    HashMap<String, String> jsonArr = new HashMap<String, String>();
                    jsonArr.put("FRIEND_NAME", name);
                    jsonArr.put("PHONE_NUMBER", phoneNumber);
                    array.add(jsonArr);

                }
                phone.close();
            }
        }
        cur.close();
        return array.toJSONString();
    }

    /**
     * 服务的开始时间和结束时间
     *
     * @param startTime
     * @param endTime
     */
    public static String getTimeSpace(String beginTime, String endTime) {
        if (TextUtils.isEmpty(beginTime) || TextUtils.isEmpty(endTime)) {
            return null;
        } else {
            SimpleDateFormat mDateFormat3 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat mDateFormat1 = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String str = mDateFormat2.format(mDateFormat1.parse(beginTime));
                beginTime = mDateFormat3.format(mDateFormat1.parse(beginTime));
                endTime = mDateFormat3.format(mDateFormat1.parse(endTime));
                return str + " " + beginTime + "-" + endTime;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 四个按钮是否显示和温馨提示
     *
     * @param code
     * @param lin
     * @param content
     * @param title
     */
    public static void isVisibleButtons(CustomerInfoEntity mCustomerInfoEntity, TickMesg mesg, int code, LinearLayout lin, TextView content, TextView title, int id) {
        if (id == 5) {//支付失败
            lin.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            return;
        }

        if (id == 6) {//退款
            if (mesg.getSERVICE_TYPE_ID().equals("1")) {//普通
                lin.setVisibility(View.VISIBLE);
            } else {
                lin.setVisibility(View.GONE);
            }
        } else {
            //判断是否能够延时
            if ((!TextUtils.isEmpty(mesg.getEXTEND_FLAG()) && mesg.getEXTEND_FLAG().equals("1"))) {
                lin.getChildAt(1).setVisibility(View.GONE);
            } else {
                lin.getChildAt(1).setVisibility(View.VISIBLE);
            }
        }
        lin.getChildAt(3).setVisibility(View.GONE);

        switch (code) {
            case 150://患者请求延时服务
                content.setText("此服务正在请求延时中，请耐心等待医生回复");
                lin.getChildAt(1).setVisibility(View.GONE);
                break;
            case 170://医生主动延时服务
                content.setText(mCustomerInfoEntity.getName() + "医生已将服务延长了3小时");
                lin.getChildAt(1).setVisibility(View.GONE);
                break;
            case 160://医生同意延时
                content.setText(mCustomerInfoEntity.getName() + "医生已将服务延长了3小时");
                break;
            case 185://已延时，可停止服务
                content.setText(mCustomerInfoEntity.getName() + "医生已将服务延长了3小时");
                break;
            case 60://客户发起退号，医生未回应
                content.setText("此服务正在申请退订中，请耐心等待医生回复");
//			lin.getChildAt(3).setVisibility(View.GONE); 暂时不加
                break;
            case 70://客户发起退号，医生拒绝退号，等待客户再次发起退号；
//			lin.getChildAt(3).setVisibility(View.VISIBLE); 暂时不加
                content.setText("医生已拒绝您的退号请求.");
                break;
            case 175:
            case 180:
                content.setText("请认真填写退款账号,以保证退款顺利完成");
                break;
            case 222://超时支付（冒费）生成，未设置退款帐号（由于超过支付时限，订单失效，但支付成功，定义为冒费状态，这笔支付应及时退款给客户）
                content.setText("请认真填写退款账号,以保证退款顺利完成");
//			lin.getChildAt(3).setVisibility(View.VISIBLE); 暂时不加
                break;
            case 232://超时支付（冒费）完成退款支付宝账号设置、尚未退款；
            case 80://客户发起退号，医生同意退号，业务未核实；
                content.setText("工作人员正在为您办理退款,还请耐心等待,如有疑问请联系:010-51668809");
                break;
            case 242:
                content.setText("已为您办理退款，请检查支付宝余额");
                break;
            case 50:
                if (!TextUtils.isEmpty(mesg.getSYSTEMTIME()) && !TextUtils.isEmpty(mesg.getSERVICE_START()) && TimeUtil.formatMillion(mesg.getSERVICE_START()) > TimeUtil.formatMillion(mesg.getSYSTEMTIME()) && TimeUtil.formatMillion(mesg.getSERVICE_START()) < TimeUtil.formatMillion(mesg.getSYSTEMTIME()) + 2 * 60 * 60 * 1000) {
                    content.setText("当前时间距预约服务开始时间已不足2个小时,不能取消该预约服务");
                } else {
                    content.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                }
                break;
            default:
                content.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 返回九宫格头像的地址
     *
     * @param array
     * @return
     */
    public static ArrayList<String> getHeaderPath(JSONArray array) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < array.size(); i++) {
            list.add(array.getJSONObject(i).getString("CLIENT_ICON_BACKGROUND"));
        }
        return list;
    }
}
