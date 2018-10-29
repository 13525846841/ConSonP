package com.yksj.consultation.son.site;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.home.MyAssessActivity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class StationActivity extends BaseFragmentActivity implements View.OnClickListener{

    private TextView workName;
    private TextView docName;
    private TextView hospital;
    private TextView introduce;

    private TextView servicePrice1;
    private TextView servicePrice2;
    private TextView servicePrice4;
    private ImageView imgHead;
    public final static String SITE_ID = "site_id";
    private String siteId = "";//医生集团id

    public static final int SERVICEONE = 1;//留言咨询
    public static final int SERVICETWO = 2;//预约咨询
    public static final int SERVICETHREE = 3;//门诊加号
    public static final int SERVICEFOUR = 4;//定制服务
    public static final int SERVICEFIVE = 5;//图文咨询
    public static final int SERVICESIX = 6;//电话咨询
    public static final int SERVICESEVEN = 7;//包月咨询
    public static final int SERVICEEIGHT = 8;//视频咨询
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("医生集团");
        titleLeftBtn.setOnClickListener(this);

        if (getIntent().hasExtra(SITE_ID)) {
            siteId = getIntent().getStringExtra(SITE_ID);
        }
        findViewById(R.id.station_member).setOnClickListener(this);
        workName = (TextView) findViewById(R.id.tv_work_name);
        docName = (TextView) findViewById(R.id.tv_name);
        hospital = (TextView) findViewById(R.id.tv_doc_place);
        imgHead = (ImageView) findViewById(R.id.det_img_head);
        introduce = (TextView) findViewById(R.id.select_expert_list_item_num);
        servicePrice1 = (TextView) findViewById(R.id.service1_price);
        servicePrice2 = (TextView) findViewById(R.id.service2_price);
        servicePrice4 = (TextView) findViewById(R.id.service4_price);
        findViewById(R.id.service1).setOnClickListener(this);
        findViewById(R.id.service2).setOnClickListener(this);
        findViewById(R.id.service4).setOnClickListener(this);
        findViewById(R.id.patient_comments).setOnClickListener(this);
    }

    private void initData() {
        if (HStringUtil.isEmptyAndZero(siteId)) {
            ToastUtil.showShort("数据异常");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("site_id", siteId);
        map.put("op", "queryWorkSiteInfo");
        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!HStringUtil.isEmpty(response)) {
                    makeDate(response);
                }
            }
        }, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.service1).setVisibility(View.GONE);
        findViewById(R.id.service2).setVisibility(View.GONE);
        findViewById(R.id.service4).setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.station_member://集团成员
                intent = new Intent(this, StationMemberAty.class);
                intent.putExtra(StationMemberAty.SITE_ID, siteId);
                startActivity(intent);
                break;
            case R.id.service1://图文咨询
                type = SERVICEFIVE;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔图文咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        sendService(type);
                    }
                });
                break;
            case R.id.service2://电话咨询
                type = SERVICESIX;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔电话咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        sendService(type);
                    }
                });
                break;
            case R.id.service4://视频咨询
                type = SERVICEEIGHT;
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您要发起一笔视频咨询吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        sendService(type);
                    }
                });
                break;
            case R.id.patient_comments://患者评价
                intent = new Intent(this, MyAssessActivity.class);
//                intent = new Intent(this, SiteCommentListActivity.class);
                intent.putExtra(MyAssessActivity.SITE, siteId);
                startActivity(intent);
                break;

        }
    }

    private void makeDate(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
            if ("1".equals(obj.optString("code"))) {
                JSONObject siteInfo = obj.getJSONObject("result").getJSONObject("siteInfo");
                workName.setText(siteInfo.optString("SITE_NAME"));
                docName.setText(siteInfo.optString("DOCTOR_REAL_NAME"));
                hospital.setText(siteInfo.optString("OFFICE_NAME"));
                introduce.setText(siteInfo.optString("SITE_DESC"));
                String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + siteInfo.optString("SITE_SMALL_PIC");
                Picasso.with(StationActivity.this).load(url).placeholder(R.drawable.default_head_doctor).into(imgHead);

                JSONArray siteServiceInfo = obj.getJSONObject("result").getJSONArray("serviceInfo");
                if (siteServiceInfo != null) {
                    for (int i = 0; i < siteServiceInfo.length(); i++) {
                        JSONObject objService = siteServiceInfo.getJSONObject(i);
                        int type = objService.optInt("SERVICE_TYPE_ID");
                        String price = objService.optString("SERVICE_PRICE");
                        boolean open = "1".equals(objService.optString("ORDER_ON_OFF"));
                        setService(type, price, open);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setService(int type, String price, boolean open) {
        if (open) {
            switch (type) {
                case SERVICEFIVE:
                    servicePrice1.setText(price);
                    findViewById(R.id.service1).setVisibility(View.VISIBLE);
                    break;
                case SERVICESIX:
                    servicePrice2.setText(price + "元/小时");
                    findViewById(R.id.service2).setVisibility(View.VISIBLE);
                    break;
                case SERVICEEIGHT:
                    servicePrice4.setText(price + "元/小时");
                    findViewById(R.id.service4).setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void sendService(int serviceType) {
        Map<String, String> map = new HashMap<>();
        map.put("service_customer_id", LoginServiceManeger.instance().getLoginEntity().getId());
        map.put("service_type_id", serviceType + "");
        map.put("site_id", siteId);
        map.put("op", "addWorkSiteOrder");
        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!HStringUtil.isEmpty(response)) {
                    makeSendDate(response);
                }
            }
        }, this);
    }

    private void makeSendDate(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
            ToastUtil.showShort(obj.optString("message"));
            if ("1".equals(obj.optString("code"))) {
//                JSONObject siteInfo = obj.getJSONObject("result").getJSONObject("siteInfo");
//                workName.setText(siteInfo.optString("SITE_NAME"));
//                docName.setText(siteInfo.optString("DOCTOR_REAL_NAME"));
//                hospital.setText(siteInfo.optString("OFFICE_NAME"));
//                introduce.setText(siteInfo.optString("SITE_DESC"));
//                String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + siteInfo.optString("SITE_SMALL_PIC");
//                Picasso.with(StationActivity.this).load(url).placeholder(R.drawable.default_head_doctor).into(imgHead);
//
//                JSONArray siteServiceInfo = obj.getJSONObject("result").getJSONArray("serviceInfo");
//                if (siteServiceInfo != null) {
//                    for (int i = 0; i < siteServiceInfo.length(); i++) {
//                        JSONObject objService = siteServiceInfo.getJSONObject(i);
//                        int type = objService.optInt("SERVICE_TYPE_ID");
//                        String price = objService.optString("SERVICE_PRICE");
//                        setService(type, price);
//                    }
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
