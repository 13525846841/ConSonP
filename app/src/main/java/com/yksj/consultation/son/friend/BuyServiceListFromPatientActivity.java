package com.yksj.consultation.son.friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dmsj.newask.http.LodingFragmentDialog;
import com.squareup.okhttp.Request;
import com.yksj.consultation.caledar.CaledarViewFragment;
import com.yksj.consultation.caledar.CaledarViewFragment.OnItemClickCaladerListener;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;
import com.yksj.healthtalk.entity.CaledarObject;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.greenrobot.event.EventBus;

/**
 * 患者购买页面 (门诊加号  预约咨询)
 *
 * @author jack_tang
 */
public class BuyServiceListFromPatientActivity extends BaseFragmentActivity implements OnItemClickCaladerListener, OnClickListener, PlatformActionListener {

    private ImageView iv_share;
    private CaledarViewFragment mFragmentView;
    private String mData;
    //    private CustomerInfoEntity mCustomerInfoEntity;
    SimpleDateFormat mDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat mDateFormat3 = new SimpleDateFormat("HH:mm");
    Map<String, JSONArray> mDataMap = new LinkedHashMap<String, JSONArray>();
    // 闲忙状态
    Map<Date, String> mStateMap = new LinkedHashMap<Date, String>();
    private LinearLayout mGroupView;
    private int SERVICE_TYPE = 2;//2 预约咨询  3 门诊
    private LodingFragmentDialog showLodingDialog;
    private String consultId;
    private int CONSERVICETYPE = 0;   //0  医生 1  专家
    private int OUTPATIENTREQUESTCODE = 100;   //购买门诊
    private String docName = "";
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private Uri uri;

    public static final String DOCTOR_ID = "doctor_id";
    public static final String DOCTOR_NAME = "doctor_name";
    private int doctorId;
    private String doctorName = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.buy_service_list_from_patient_layout);
        consultId = getIntent().getStringExtra("consultId");

        initView();
        if (arg0 != null) {
            mData = arg0.getString("mData");
//            mCustomerInfoEntity = (CustomerInfoEntity) arg0.getSerializable("mCustomerInfoEntity");
        } else {
            if (getIntent().hasExtra(DOCTOR_ID)) {
                doctorId = getIntent().getIntExtra(DOCTOR_ID,0);
            }
            if (getIntent().hasExtra(DOCTOR_NAME)) {
                doctorName = getIntent().getStringExtra(DOCTOR_NAME);
            }
//			mData = getIntent().getStringExtra("response");
//            mCustomerInfoEntity = (CustomerInfoEntity) getIntent().getExtras().getSerializable("mCustomerInfoEntity");
        }
    }

    private void initView() {
        initTitle();
        mViewFinder = new ViewFinder(this);
        titleLeftBtn.setOnClickListener(this);

//        iv_share = (ImageView) findViewById(R.id.iv_share);
//        iv_share.setVisibility(View.VISIBLE);
//        iv_share.setOnClickListener(this);


        titleTextV.setText(getIntent().getStringExtra("titleName"));
        if (getIntent().hasExtra(DOCTOR_NAME)) {
            titleTextV.setText(getIntent().getStringExtra(DOCTOR_NAME));
        }
        SERVICE_TYPE = (getIntent().getIntExtra("type", 2));
        mFragmentView = (CaledarViewFragment) getSupportFragmentManager().findFragmentByTag("calendar");
        mFragmentView.setOnItemClickListener(this);
        mGroupView = (LinearLayout) findViewById(R.id.group);
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.launcher_logo);
//		AnimationUtils.startGuiPager(this, getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        intData();
    }

    private synchronized void intData() {
        showLodingDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
        //ServicePatientServlet?DOCTORID=2496&CUSTOMER_ID=116139&Type=queryOrderMessage&ORDER_ID=20549&PAY_ID=20549
        //Type=IsCanTalk&DOCTORID=3844&SERVICE_TYPE_ID=3&CUSTOMER_ID=225432
        if (HStringUtil.isEmpty(LoginServiceManeger.instance().getLoginEntity().getId())) {
            return;
        }
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("Type", "IsCanTalk"));
        valuePairs.add(new BasicNameValuePair("DOCTORID", getIntent().getIntExtra("doctor_id",0)+""));
        valuePairs.add(new BasicNameValuePair("SERVICE_TYPE_ID", String.valueOf(SERVICE_TYPE)));
        valuePairs.add(new BasicNameValuePair("CUSTOMER_ID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));
        HttpRestClient.doGetServicePatientServlet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (response == null || response.length() == 0)
                    return;
                try {
                    org.json.JSONObject object = new org.json.JSONObject(response);
                    if (object.optInt("code") == 1) {
                        mData = response.toString();
                        initParseView();
                    } else {
                        SingleBtnFragmentDialog.show(getSupportFragmentManager(), "六一健康", object.optString("message"), "知道了", new OnClickSureBtnListener() {

                            @Override
                            public void onClickSureHander() {
                                onBackPressed();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    public synchronized void initParseView() {
        JSONObject jsondata = JSON.parseObject(mData);// TickMesg
        final JSONObject jsonObject = jsondata.getJSONObject("result");// TickMesg
        CONSERVICETYPE = jsonObject.getInteger("CONSERVICETYPE");
        docName = jsonObject.getString("DOCTORNAME");
        if (jsonObject.getIntValue("witchPage") == 1 || jsonObject.getIntValue("witchPage") == 2 || jsonObject.getIntValue("witchPage") == 3) {
            new AsyncTask<Void, Void, Map<Calendar, String>>() {
                @Override
                protected Map<Calendar, String> doInBackground(Void... params) {
                    JSONArray array = jsonObject.getJSONArray("TickMesg");
                    if (array.size() == 0) {//没有设置预约时段不显示服务内容 价格 等
                        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "该医生尚未设置门诊服务", new OnClickSureBtnListener() {
                            @Override
                            public void onClickSureHander() {
                            }
                        });
                    }
                    if (mStateMap != null) mStateMap.clear();
                    if (mDataMap != null) mDataMap.clear();
                    int count = array.size();
//                    if (count <= 0) {
//                        ToastUtil.showShort(BuyServiceListFromPatientActivity.this,"对不起,医生还没有设置门诊时间");
//                    }
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject2 = array.getJSONObject(i);
                        String beginTime = jsonObject2.getString("SERVICE_TIME_BEGIN");
                        String busy = jsonObject2.getString("ISBUZY");
                        try {
                            Date date = mDateFormat2.parse(mDateFormat2.format(mDateFormat1.parse(beginTime)));
                            // 记录闲忙
                            if (mStateMap.containsKey(date) && "0".equals(busy)) {
                                mStateMap.put(date, busy);
                            } else if (!mStateMap.containsKey(date)) {
                                mStateMap.put(date, busy);
                            }
                            //
                            String endTime = jsonObject2.getString("SERVICE_TIME_END");
                            beginTime = mDateFormat3.format(mDateFormat1.parse(beginTime));
                            endTime = mDateFormat3.format(mDateFormat1.parse(endTime));
                            JSONObject object = new JSONObject();
                            object.put("time_space", beginTime + "-" + endTime);
                            object.put("SERVICE_ITEM_ID", jsonObject2.getString("SERVICE_ITEM_ID"));
                            object.put("SERVICE_PRICE", jsonObject2.getString("SERVICE_PRICE"));
                            object.put("ISBUY", jsonObject2.getString("ISBUY"));
                            object.put("ISBUZY", jsonObject2.getString("ISBUZY"));
                            object.put("ISTALK", jsonObject2.getString("ISTALK"));
                            object.put("ORDER_ID", jsonObject2.getString("ORDER_ID"));
                            object.put("SERVICE_TYPE_SUB_ID", jsonObject2.getIntValue("SERVICE_TYPE_SUB_ID"));
                            object.put("SELECTDATE", jsonObject2.getString("SERVICE_TIME_BEGIN"));
                            object.put("SERVICE_CONTENT", jsonObject2.getString("SERVICE_CONTENT"));
                            object.put("SERVICE_PLACE", jsonObject2.getString("SERVICE_PLACE"));
                            object.put("FREE_MEDICAL_PRICE", jsonObject2.getString("FREE_MEDICAL_PRICE"));
                            object.put("data", jsonObject2.toString());
                            String str = mDateFormat2.format(date);
                            if (mDataMap.containsKey(str)) {
                                mDataMap.get(str).add(object);
                            } else {
                                JSONArray array2 = new JSONArray();
                                array2.add(object);
                                mDataMap.put(str, array2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Map<Calendar, String> dates = new HashMap<Calendar, String>();
                    for (Map.Entry<Date, String> entry : mStateMap.entrySet()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(entry.getKey());
                        if (entry.getValue().equals("1")) {
                            dates.put(cal, CaledarObject.busy);
                        } else {
                            dates.put(cal, CaledarObject.noBusy);
                        }
                    }
                    return dates;
                }

                protected void onPostExecute(Map<Calendar, String> result) {
                    showLodingDialog.dismissAllowingStateLoss();
                    mFragmentView.addApplyDate(result);
                    updateBottomView(mPressDate);
                }
            }.execute();

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mData", mData);
//        outState.putSerializable("mCustomerInfoEntity", mCustomerInfoEntity);
    }

    private String mPressDate;
    private ViewFinder mViewFinder;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        CaledarObject descriptor = (CaledarObject) view.getTag();
        mPressDate = mDateFormat2.format(descriptor.getDate().getTime());
        updateBottomView(mPressDate);
    }


    public void updateBottomView(final String date) {
        if (HStringUtil.isEmpty(date)) return;
        mViewFinder.setText(R.id.current_date, TimeUtil.getFormatDate2(date));
//		current_date
        mGroupView.removeAllViews();
        if (mDataMap.containsKey(date)) {
            ViewFinder finder;
            JSONArray jsonArray = mDataMap.get(date);
            for (int i = 0; i < jsonArray.size(); i++) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                View itemView = getLayoutInflater().inflate(R.layout.buy_service_list_from_patient_item_layout, null);
                finder = new ViewFinder(itemView);
                finder.setText(R.id.time, jsonObject.getString("time_space"));
                if (HStringUtil.isEmptyAndZero(jsonObject.getString("FREE_MEDICAL_PRICE"))) {
                    finder.setText(R.id.edit, jsonObject.getString("SERVICE_PRICE"));
                } else {
                    finder.setText(R.id.edit, jsonObject.getString("FREE_MEDICAL_PRICE"));
                }
                Button btnBuy = (Button) itemView.findViewById(R.id.delete);

//                if (Integer.valueOf(jsonObject.getString("ISBUY")) != 0) {//已经购买
//                    btnBuy.setBackgroundResource(R.drawable.btn_gray_bg);
//                    btnBuy.setText(R.string.go_order);//支付已完成,去留言
//                    btnBuy.setTag(4);
//                    btnBuy.setOnClickListener(null);
//                } else if (!"".equals(jsonObject.getString("ORDER_ID"))) {//未支付
//                    btnBuy.setBackgroundResource(R.drawable.bt_short_green);
//                    if (CONSERVICETYPE == 0)
//                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
//                    else
////                        btnBuy.setText(R.string.go_pay);//订单以生成,去支付
//                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
//                    btnBuy.setTag(2);
//                } else if ("0".equals(jsonObject.getString("ISBUZY"))) {//闲
//                    btnBuy.setBackgroundResource(R.drawable.bt_short_green);
//                    if (CONSERVICETYPE == 0)
//                        btnBuy.setText("预约");//订单以生成,去支付
//                    else
////                        btnBuy.setText(R.string.go_pay);//订单以生成,去支付
//                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
//                    btnBuy.setTag(3);
//                } else if ("1".equals(jsonObject.getString("ISBUZY"))) {//忙
//                    btnBuy.setBackgroundResource(R.drawable.btn_gray_bg);
//                    btnBuy.setOnClickListener(null);
//                    if (CONSERVICETYPE == 0)
//                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
//                    else
////                        btnBuy.setText(R.string.go_pay);//订单以生成,去支付
//                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
//                    btnBuy.setTag(4);
//                } else {
//                }
                if ("0".equals(jsonObject.getString("ISBUZY"))) {//闲
                    btnBuy.setBackgroundResource(R.drawable.bt_short_green);
                    if (CONSERVICETYPE == 0)
                        btnBuy.setText("预约");//订单以生成,去支付
                    else
//                        btnBuy.setText(R.string.go_pay);//订单以生成,去支付
                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
                    btnBuy.setTag(3);
                } else if ("1".equals(jsonObject.getString("ISBUZY"))) {//忙
                    btnBuy.setBackgroundResource(R.drawable.bt_short_gray);
                    btnBuy.setOnClickListener(null);
                    if (CONSERVICETYPE == 0)
                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
                    else
//                        btnBuy.setText(R.string.go_pay);//订单以生成,去支付
                        btnBuy.setText(R.string.go_order);//订单以生成,去支付
                    btnBuy.setTag(4);
                } else {
                }

                btnBuy.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (CONSERVICETYPE == 0) {//若果是医生
                            String time = TimeUtil.getTimeStr(date) + " " + jsonObject.getString("time_space");
                            SingleBtnFragmentDialog.show(getSupportFragmentManager(), "确认预约信息",
                                    "预约医生:" + docName + "\n服务时间:" + time +
                                            "\n服务地点:" + jsonObject.getString("SERVICE_PLACE"), "确定", new OnClickSureBtnListener() {
                                        @Override
                                        public void onClickSureHander() {
                                            goOrder(jsonObject);
                                        }
                                    });
                            return;

                        }
                        switch ((Integer) v.getTag()) {
                            case 1:////支付已完成,去留言
                                FriendHttpUtil.chatFromPerson(BuyServiceListFromPatientActivity.this, doctorId+"", doctorName);
                                break;
                            case 2://订单以生成,去支付
                                actionBuy(jsonObject.getString("data"));
                                break;
                            case 3://n
                                if ("0".equals(jsonObject.getString("SERVICE_PRICE"))) {//0元

                                    Intent intent = new Intent(BuyServiceListFromPatientActivity.this, ServiceAddInfoActivity.class);
                                    intent.putExtra("response", jsonObject.getString("data").toString());
                                    intent.putExtra("consultId", consultId);
                                    startActivity(intent);

                                } else {
                                    actionBuy(jsonObject.getString("data"));
                                }
                                break;
                            case 4://忙 不能点击

                                break;
                        }
                    }
                });

                if (SERVICE_TYPE == 3) {
                    finder.setText(R.id.address, "服务地点: \n" + "\n" + jsonObject.getString("SERVICE_PLACE")).setVisibility(View.VISIBLE);
                } else {
                    finder.find(R.id.address).setVisibility(View.GONE);
                }
//				if(Integer.valueOf(jsonObject.getString("ISBUY")) ==0){
//					if("".equals(jsonObject.getString("ORDER_ID")))
//						btnBuy.setText(R.string.go_pay);//去购买
//					else
//						btnBuy.setText(R.string.go_pay);//订单以生成,去支付
//				}else{
//					btnBuy.setText("看医生");//支付已完成,去留言
//				}
                mGroupView.addView(itemView);
            }
        }

        if (mGroupView.getChildCount() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }
    }


    protected void actionBuy(String data) {
        Intent intent = new Intent(this, ServicePayMainUi.class);
        intent.putExtra("isAddress", true);
        intent.putExtra("json", data);
        intent.putExtra("Type", 1);
        intent.putExtra("SERVICE_TYPE", SERVICE_TYPE);
        intent.putExtra(ServicePayMainUi.DOCTOR_ID, doctorId);
        intent.putExtra(ServicePayMainUi.DOCTOR_NAME, docName);
        intent.putExtra("consultationId", consultId);
        // startActivityForResult(intent, OUTPATIENTREQUESTCODE);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.friendcircle://微信朋友圈
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
                sendWX();
                quitPopWindow();
                break;
            case R.id.weibo://新浪微博分享
                showShare(SinaWeibo.NAME);
                quitPopWindow();
                break;
            case R.id.qqroom://qq空间
                showShare(QZone.NAME);
                quitPopWindow();
                break;
            case R.id.btn_cancel:
                ToastUtil.showShort("取消");
                quitPopWindow();
                break;
        }
    }

    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 微信朋友圈
     */
    private void sendWXMS() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + "1" + "?pageNum=1");   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(BuyServiceListFromPatientActivity.this); // 设置分享事件回调
        // 执行分享
        wechat.share(sp);
    }

    /**
     * 微信分享
     */
    private void sendWX() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + "1" + "?pageNum=1");   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(BuyServiceListFromPatientActivity.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

    /**
     * 第三方分享
     *
     * @param name
     */

    private void showShare(String name) {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(getString(R.string.string_share_title));
        sp.setText(getString(R.string.string_share_content));
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + "1" + "?pageNum=1");
        sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + "1" + "?pageNum=1");//网友点进链接后，可以看到分享的详情
        sp.setImageData(getBitmapFromUri(uri));

        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(BuyServiceListFromPatientActivity.this); // 设置分享事件回调
        // 执行分享
        sinaWeibo.share(sp);
    }

    /**
     * 获取图片Bitmap
     *
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showShare() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_share, null);
            view.findViewById(R.id.friendcircle).setOnClickListener(this);
            view.findViewById(R.id.wechat).setOnClickListener(this);
            view.findViewById(R.id.weibo).setOnClickListener(this);
            view.findViewById(R.id.qqroom).setOnClickListener(this);
            view.findViewById(R.id.btn_cancel).setOnClickListener(this);

            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = BuyServiceListFromPatientActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    BuyServiceListFromPatientActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(BuyServiceListFromPatientActivity.this.findViewById(R.id.ll_buy_main), Gravity.BOTTOM, 0, 0);

    }

    private void goOrder(JSONObject jsonObject) {
        //ServicePatientPayServlet?PATIENT_PHONE=18612790135&CUSTOMER_ID=116139&SELECTDATE=20150922120000&
        // SERVICE_ITEM_ID=37866&DOCTORID=2496&Type=MedicallyRegistered330&SERVICE_TYPE_SUB_ID=8&
        // SERVICE_TYPE_ID=3&ADVICE_CONTENT=特殊收费人员测试&PATIENT_NAME=陈琴&ORDER_ID=12920&
        // CONSULTATIONID=225539
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("Type", "MedicallyRegistered330"));
        valuePairs.add(new BasicNameValuePair("DOCTORID", doctorId+""));
        valuePairs.add(new BasicNameValuePair("PATIENT_PHONE", ""));
        valuePairs.add(new BasicNameValuePair("SERVICE_ITEM_ID", jsonObject.getString("SERVICE_ITEM_ID")));
        valuePairs.add(new BasicNameValuePair("SELECTDATE", jsonObject.getString("SELECTDATE")));
        valuePairs.add(new BasicNameValuePair("SERVICE_TYPE_SUB_ID", ""));
        valuePairs.add(new BasicNameValuePair("ADVICE_CONTENT", ""));
        valuePairs.add(new BasicNameValuePair("PATIENT_NAME", ""));
        valuePairs.add(new BasicNameValuePair("ORDER_ID", ""));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));
        valuePairs.add(new BasicNameValuePair("CUSTOMER_ID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("SERVICE_TYPE_ID", String.valueOf(SERVICE_TYPE)));

        HttpRestClient.doGetServicePatientPayServlet(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        org.json.JSONObject object = new org.json.JSONObject(response);
                        if (object.optInt("code") == 1) {
                            final org.json.JSONObject obj = object.optJSONObject("result");
                            DoubleBtnFragmentDialog.showDoubleBtn(BuyServiceListFromPatientActivity.this, "预约成功", obj.optString("MESSAGE"),
                                    "查看详细", "关闭", new DoubleBtnFragmentDialog.OnFristClickListener() {
                                        @Override
                                        public void onBtn1() {
                                            Intent intent = new Intent(BuyServiceListFromPatientActivity.this, AtyOutPatientDetail.class);
                                            intent.putExtra("ORIDERID", obj.optString("ORDERID"));
                                            startActivity(intent);
                                            EventBus.getDefault().post(new MyEvent("refresh", 2));
                                            BuyServiceListFromPatientActivity.this.finish();
                                        }
                                    }, new DoubleBtnFragmentDialog.OnSecondClickListener() {
                                        @Override
                                        public void onBtn2() {
                                            ToastUtil.showShort("关闭");
                                            onBackPressed();
                                            EventBus.getDefault().post(new MyEvent("refresh", 2));
                                            BuyServiceListFromPatientActivity.this.finish();
                                        }
                                    }).show();
                        } else {
                            if (object.has("message")) {
                                ToastUtil.showShort(BuyServiceListFromPatientActivity.this, object.optString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OUTPATIENTREQUESTCODE) {
            EventBus.getDefault().post(new MyEvent("refresh", 2));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {//回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(5);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(6);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 5:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败啊" + msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };
}
