package com.yksj.consultation.son.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.EvelateAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrderDetails;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.friend.BuyServiceListFromPatientActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 医生个人信息
 * Created by android_zl on 15/9/16.
 */
public class AtyDoctorMassage extends BaseFragmentActivity implements View.OnClickListener {
    private TextView dName, dPosition, dSpecialty, dHospital, dSpecial, dInfo, consultMoney, dNum;
    private ImageView dHeader;
    private Button selectDoc, selectAss;
    private String doctorType;
    private RatingBar mBar;// 评价星级
    private LinearLayout evalatell;
    private RelativeLayout appointmentll,selectedHim,starLL;
    private Intent intent;
     private ImageLoader mInstance;
    private ListView lv;
    private EvelateAdapter mAdapter;
    private DoctorSimpleBean dsb;
    private List<Map<String, String>> mLists;
    private Map<String, String> mMap;
    private String pid = null;
    private String TITLE_NAME = "", OFFICE_NAME = "", UNIT_NAME = "", DOCTOR_SPECIALLY = "", INTRODUCTION = "";
    private TextView evelateNum;//customerNum客户数量
    private String customerNumStr;//evelateNumStr;
    private int index = 0, start = 0, end = 0, color;
    private SpannableStringBuilder spBuilder;
    private String doctorId = "";
    private String customerid = "";
    private int collection = 0;
    private JSONObject obj;
    private Handler handler = new Handler();
    private ScrollView topSv;
    private String consultId;
    private String officeCode;
    private String doctorName;
    private int goalType;
    private CustomerInfoEntity entity;
    private String officeName;
//    private PatientConsuListAdapter EAdapter;
//    private List<JSONObject> jsonLst;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.doctor_massage_aty);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        mInstance = ImageLoader.getInstance();
        topSv = (ScrollView) findViewById(R.id.scrollview_top1);
        topSv.fullScroll(ScrollView.FOCUS_UP);
        dName = ((TextView) findViewById(R.id.select_name));
        dPosition = ((TextView) findViewById(R.id.select_position));
        dSpecialty = ((TextView) findViewById(R.id.select_specialty));
        dHospital = ((TextView) findViewById(R.id.select_hospital));
        dHeader = ((ImageView) findViewById(R.id.select_header));
        dSpecial = ((TextView) findViewById(R.id.doctor_special_m));
        dInfo = ((TextView) findViewById(R.id.doctor_info));
        dNum = ((TextView) findViewById(R.id.tv_num));
        //专家
        selectedHim = ((RelativeLayout) findViewById(R.id.select_him_ll));
        appointmentll = (RelativeLayout) findViewById(R.id.appointment_ll);
        consultMoney = ((TextView) findViewById(R.id.consult_money));
        selectDoc = ((Button) findViewById(R.id.select_him2));
        //医生
        starLL = ((RelativeLayout) findViewById(R.id.select_star_ll));
        evalatell = ((LinearLayout) findViewById(R.id.evaluate_ll));
//        customerNum = (TextView) findViewById(R.id.customer_num);//客户数量业务已取消
        evelateNum = (TextView) findViewById(R.id.evelate_num);
        selectAss = ((Button) findViewById(R.id.all_select_him1));
        lv = (ListView) findViewById(R.id.listview_test);
        lv.setFocusable(false);
        mAdapter = new EvelateAdapter(this);
        lv.setAdapter(mAdapter);
        mBar = (RatingBar) findViewById(R.id.star);
        mBar.setStepSize(1f);
        if (!LoginServiceManeger.instance().isVisitor) {
            customerid = LoginServiceManeger.instance().getLoginEntity().getId();
        }
        if (getIntent().hasExtra("OFFICECODE")) {
            officeCode = getIntent().getStringExtra("OFFICECODE");
        }
        if (getIntent().hasExtra("OFFICENAME")){
            officeName = getIntent().getStringExtra("OFFICENAME");
        }
        doctorId = getIntent().getStringExtra("id");
        selectDoc.setOnClickListener(this);
        selectAss.setOnClickListener(this);


        if (getIntent().hasExtra("type")) {
            int type = getIntent().getIntExtra("type", 0);
            switch (type) {
                case 0:
                    doctorType = "findDocInfo";
                    initData();
                    break;
                case 1:
                    doctorType = "findAssiInfo";
                    initData();
                    break;
            }
        }

    }

    private void initData() {
        HttpRestClient.doHttpFindInfo(customerid, doctorId, doctorType, new ObjectHttpResponseHandler(AtyDoctorMassage.this) {
            @Override
            public Object onParseResponse(String content) {
                if (content != null) {
                    return content;
                } else {
                    return null;
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                try {
                    if (response != null) {
                        JSONObject object = new JSONObject(response.toString());
                        if (object.optString("code").equals("1")) {
                            JSONObject massage = object.optJSONObject("result");
                            LogUtil.d("TAG", "医生的信息" + response.toString());
                            onBandMassage(massage);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //绑定数据
    private void onBandMassage(JSONObject object) {
        obj = object;
        collection = object.optInt("IS_COL");
        LogUtil.d("TAG", "首场标记" + collection);
        if (LoginServiceManeger.instance().isVisitor) {
        } else {
//            customerid = LoginServiceManeger.instance().getLoginEntity().getId();

            /***************************************************
             * 收藏按钮，暂时隐藏掉
             */
//            titleRightBtn2.setVisibility(View.VISIBLE);
//            titleRightBtn2.setOnClickListener(this);
//            if (collection > 0) {
//                titleRightBtn2.setText("取消收藏");
//            } else if (collection <= 0) {
//                titleRightBtn2.setText("收藏");
//            }
        }
        dsb = new DoctorSimpleBean();
        String dNameText = null;
        String dNUm = null;
        switch (doctorType) {
            case "findDocInfo"://专家
                dNameText = object.optString("DOCTOR_REAL_NAME");
                dNUm="(剩余"+object.optString("NUMS")+"名额)";
                if (getIntent().hasExtra("CLINIC")) {
                    doctorName=object.optString("DOCTOR_REAL_NAME");
                    appointmentll.setVisibility(View.VISIBLE);
                    entity = new CustomerInfoEntity();
                    entity.setId(doctorId);
                    findViewById(R.id.appointment).setOnClickListener(this);
                } else {
                    selectedHim.setVisibility(View.VISIBLE);
                    consultMoney.setText(object.optString("SERVICE_PRICE") + "元");
                    if (getIntent().hasExtra("ORDER")) {
                        findViewById(R.id.select_him2).setVisibility(View.GONE);
                    }
                    if ("0".equals(object.optString("NUMS"))) {
//                        findViewById(R.id.select_him2).setVisibility(View.GONE);
                        selectDoc.setClickable(false);
                        selectDoc.setBackgroundResource(R.drawable.icon_bg_gray_72);
                    }
                }
              //  dsb.SERVICE_PRICE = Integer.parseInt(object.optString("SERVICE_PRICE"));
                if (getIntent().hasExtra("PID")) {
                    pid = getIntent().getStringExtra("PID");
                }
                break;
            case "findAssiInfo"://医生
                dNameText = object.optString("DOCTOR_REAL_NAME");
                starLL.setVisibility(View.VISIBLE);
                evalatell.setVisibility(View.VISIBLE);
                if (!"".equals(object.optString("averageValue"))) {
                    mBar.setRating(Float.parseFloat(object.optString("averageValue")));
                }
                if (getIntent().hasExtra("ORDER")) {
                    findViewById(R.id.all_select_him1).setVisibility(View.GONE);
                }
                JSONArray array = object.optJSONArray("commentList");
                mLists = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.optJSONObject(i);
                    mMap = new HashMap<>();
                    mMap.put("COMMENT_RESULT", item.optString("COMMENT_RESULT"));//
                    mMap.put("SERVICE_LEVEL", item.optString("SERVICE_LEVEL"));//
                    mMap.put("REAL_NAME", item.optString("REAL_NAME"));//
                    mLists.add(mMap);
                }
                mAdapter.add(mLists);
//                evelateNumStr="共参与"+object.optString("num")+"次会诊";//业务已取消
//                evelateNum.setText(changeColor(evelateNumStr));
                customerNumStr = "用户评价(" + object.optString("num") + ")";
                evelateNum.setText(changeColor(customerNumStr));
                break;
        }
        titleTextV.setText(object.optString("DOCTOR_REAL_NAME") + "医生");
        dName.setText(dNameText);
        dNum.setText(dNUm);
        mInstance.displayImage("", object.optString("ICON_DOCTOR_PICTURE"), dHeader);
        dHeader.setOnClickListener(this);
        if ("null".equals(object.optString("TITLE_NAME"))) {
            TITLE_NAME = "暂无";
        } else {
            TITLE_NAME = object.optString("TITLE_NAME");
        }
//        dPosition.setText(TITLE_NAME);
        dSpecialty.setText(TITLE_NAME);
        if ("null".equals(object.optString("OFFICE_NAME"))) {
            OFFICE_NAME = "暂无";
        } else {
            OFFICE_NAME = object.optString("OFFICE_NAME");
        }
//        dSpecialty.setText(OFFICE_NAME);
        dPosition.setText(OFFICE_NAME);
        if ("null".equals(object.optString("UNIT_NAME"))) {
            UNIT_NAME = "暂无";
        } else {
            UNIT_NAME = object.optString("UNIT_NAME");
        }
        dHospital.setText(UNIT_NAME);
        if ("null".equals(object.optString("DOCTOR_SPECIALLY"))) {
            DOCTOR_SPECIALLY = "暂无";
        } else {
            DOCTOR_SPECIALLY = object.optString("DOCTOR_SPECIALLY");
        }
        dSpecial.setText(DOCTOR_SPECIALLY);
        if ("null".equals(object.optString("INTRODUCTION"))) {
            INTRODUCTION = "暂无";
        } else {
            INTRODUCTION = object.optString("INTRODUCTION");
        }
        dInfo.setText(INTRODUCTION);
        handler.post(new Runnable() {
            @Override
            public void run() {
                topSv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        dsb.DOCTOR_REAL_NAME = object.optString("DOCTOR_REAL_NAME");
       // dsb.CUSTOMER_ID = Integer.parseInt(getIntent().getStringExtra("id"));
        dsb.ICON_DOCTOR_PICTURE = object.optString("ICON_DOCTOR_PICTURE");
        dsb.TITLE_NAME = object.optString("TITLE_NAME");
        dsb.DOCTOR_SPECIALLY = object.optString("DOCTOR_SPECIALLY");
        dsb.UNIT_NAME = object.optString("UNIT_NAME");
        dsb.INTRODUCTION = object.optString("INTRODUCTION");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.select_him2:
                if (getIntent().hasExtra("consultId")) {
                    consultId = getIntent().getStringExtra("consultId");
                    List<BasicNameValuePair> valuePairs = new ArrayList<>();
                    valuePairs.add(new BasicNameValuePair("TYPE", "reSelectedExpert"));
                    valuePairs.add(new BasicNameValuePair("CUSTOMERID", dsb.CUSTOMER_ID + ""));
                    valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));
                    valuePairs.add(new BasicNameValuePair("SERVICE_PRICE", "" + dsb.SERVICE_PRICE));
                    HttpRestClient.doGetConsultationInfoSet(valuePairs, new MyResultCallback<String>(this) {
                        @Override
                        public void onError(Request request, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(response, BaseBean.class);

                            if ("1".equals(bb.code)) {
                                EventBus.getDefault().post(new MyEvent("refresh", 2));
                                Intent intent = new Intent(AtyDoctorMassage.this, AtyOrderDetails.class);
                                intent.putExtra("CONID", Integer.parseInt(consultId));
                                intent.putExtra("BACK", 2);
                                startActivity(intent);
                            } else
                                ToastUtil.showShort(AtyDoctorMassage.this, bb.message);
                        }
                    }, this);
                } else {
                    intent = new Intent(AtyDoctorMassage.this, FlowMassageActivity.class);
                    intent.putExtra("data", dsb);
                    intent.putExtra("PROMTER", "10");
                    intent.putExtra("OFFICECODE", officeCode);
                    intent.putExtra("OFFICENAME", officeName);
                    startActivity(intent);
                }
                break;
            case R.id.all_select_him1:
                intent = new Intent();
                intent.putExtra("data", dsb);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.title_right2:
                if (collection > 0) {
                    cancelCollectedPerson();
                } else {
                    collectedPerson();
                }
                break;
            case R.id.select_header:
                ZoomImgeDialogFragment.show(obj.optString("DOCTOR_PICTURE"), getSupportFragmentManager());
                break;
            case R.id.appointment:
                intent = new Intent(AtyDoctorMassage.this, BuyServiceListFromPatientActivity.class);
                intent.putExtra("consultId", consultId);
                intent.putExtra("titleName", doctorName);
                intent.putExtra("type", 3);
                intent.putExtra("mCustomerInfoEntity", entity);
                startActivity(intent);
                break;
        }
    }

    /**
     * 收藏医生
     */
    private void collectedPerson() {
        HttpRestClient.OKHttpCollectedPerson("collectedPerson", doctorId, customerid, new MyResultCallback<JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                LogUtil.d("TAG", response.toString());
                ToastUtil.showShort(response.optString("message"));
                if ("1".equals(response.optString("code"))) {
                    collection = 1;
                    titleRightBtn2.setText("取消收藏");
                }
            }
        }, this);
    }

    /**
     * 取消收藏医生
     */
    private void cancelCollectedPerson() {
        HttpRestClient.OKHttpCollectedPerson("cancelCollectedPerson", doctorId, customerid, new MyResultCallback<JSONObject>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                LogUtil.d("TAG", response.toString());
                ToastUtil.showShort(response.optString("message"));
                if ("1".equals(response.optString("code"))) {
                    collection = 0;
                    titleRightBtn2.setText("收藏");
                }
            }
        }, this);
    }

    //字体颜色
    private SpannableStringBuilder changeColor(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                if (index == 0) {
                    start = i;
                }
                index++;
            }
        }
        end = start + index;
        spBuilder = new SpannableStringBuilder(str);
        color = getResources().getColor(R.color.charge_red);//charge_red
        CharacterStyle charaStyle = new ForegroundColorSpan(color);
        spBuilder.setSpan(charaStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        index = 0;
        return spBuilder;
    }

}
