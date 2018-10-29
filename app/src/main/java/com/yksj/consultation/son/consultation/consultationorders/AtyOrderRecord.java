package com.yksj.consultation.son.consultation.consultationorders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.casehistory.CaseShowFragment;
import com.yksj.consultation.son.consultation.bean.ConsultDetailsFBean;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEKL on 15/2/11.
 * Used for 会诊记录详情_
 */
public class AtyOrderRecord extends BaseFragmentActivity implements View.OnClickListener {
    private PullToRefreshScrollView mPullToRefreshScrollView;//整体滑动布局
    private ImageView mImageHeadP, mImageHeadD, mImageHeadE;
    private TextView mPatientName,mDoctorName,mExpertName;
    private CaseShowFragment fragment;//病历
    private FragmentTransaction beginTransaction;
    private ConsultDetailsFBean bean;//订单实体
    private DisplayImageOptions mOption;//异步加载图片的操作
    private ImageLoader mImageLoader;
    private String value;//传入到fragment的值
    private Bundle bundle;//点击头像传入的值
    private int getDataCount;//刷新订单的次数
    private int conId;//会诊id
    private String cusId;//客户id
    private String doctorName;//医生姓名
    private String expertName;//专家姓名


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_orderdetail);
        initView();
        getDataFromServer();
    }

    /**
     * 控件的初始化
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        //标题初始化
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        //初始化
        mImageLoader = ImageLoader.getInstance();//异步加载图片的初始化
        mOption = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);//头像默认显示操作
        conId = getIntent().getIntExtra("CONID", 0);//会诊Id
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);//滑动
        if (getIntent().hasExtra("PERSONID")) {
            cusId = getIntent().getStringExtra("PERSONID");
            findViewById(R.id.ll_price).setVisibility(View.GONE);
            findViewById(R.id.ll_process).setVisibility(View.GONE);
        } else {
            cusId = LoginServiceManeger.instance().getLoginEntity().getId();
        }
        //病历
        fragment = new CaseShowFragment();
        bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        mImageHeadP = (ImageView) findViewById(R.id.image_head_p);//患者头像
        mImageHeadD = (ImageView) findViewById(R.id.image_head_d);//医生头像
        mImageHeadE = (ImageView) findViewById(R.id.image_head_e);//专家头像
        mPatientName = (TextView) findViewById(R.id.tv_patientname);//患者姓名
        mDoctorName = (TextView) findViewById(R.id.tv_docName);//医生姓名
        mExpertName = (TextView) findViewById(R.id.tv_expertName);//专家姓名
        Button mOutPatient = (Button) findViewById(R.id.outpatient);//按钮1
        findViewById(R.id.btn_talk).setVisibility(View.GONE);
        findViewById(R.id.line_view1).setVisibility(View.GONE);
        findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
        mOutPatient.setVisibility(View.VISIBLE);
        mOutPatient.setText("会诊意见");
        mOutPatient.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.title_back://回退
                onBackPressed();
                break;
            case R.id.outpatient://会诊意见
                i = new Intent(AtyOrderRecord.this, AtyConsultOpinion.class);
                i.putExtra("conId", conId);
                i.putExtra("record", 0);
                startActivity(i);
                break;
        }
    }

    /**
     * 加载订单详情
     */
    private void getDataFromServer() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CONSULTATIONID", conId + ""));
        pairs.add(new BasicNameValuePair("CUSTID", cusId));
        HttpRestClient.OKHttpConsultInfo(pairs, new MyResultCallback<String>(AtyOrderRecord.this) {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mPullToRefreshScrollView.setRefreshing();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    bean = gson.fromJson(response, ConsultDetailsFBean.class);
                    JSONObject obj = null;
                    JSONObject object = null;
                    try {
                        obj = new JSONObject(response);
                        value = obj.getJSONObject("result").toString();
                        //患者头像
                        if (!"".equals(obj.getJSONObject("result").optString("PATIENT"))) {
                            if ("null".equals(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME"))||("".equals(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME")))){
                                mPatientName.setText("患者");
                            }else {
                                mPatientName.setText(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME"));
                            }
                            mImageLoader.displayImage(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTICON"), mImageHeadP, mOption);
                        }
                        //医生头像
                        if (!"".equals(obj.getJSONObject("result").optString("DOCTOR"))) {
                            mImageLoader.displayImage(obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORICON"), mImageHeadD, mOption);
                            doctorName=obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORNAME");
                            if ("null".equals(doctorName)||("".equals(doctorName))){
                                mDoctorName.setText("会诊医生");
                            }else {
                                mDoctorName.setText(doctorName);
                            }
                        }
                        //专家头像
                        if (!"".equals(obj.getJSONObject("result").optString("EXPERT"))) {
                            mImageLoader.displayImage(obj.getJSONObject("result").getJSONObject("EXPERT").optString("EXPERTICON"), mImageHeadE, mOption);
                            expertName=obj.getJSONObject("result").getJSONObject("EXPERT").optString("EXPERTNAME");
                            if ("null".equals(expertName)||("".equals(expertName))){
                                mExpertName.setText("会诊专家");
                            }else {
                                mExpertName.setText(expertName);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (bean == null) {
                        return;
                    } else if ("0".equals(bean.getCode())) {
                        ToastUtil.showShort(bean.getMessage());
                    } else {
                        titleTextV.setText(bean.getResult().getCON_NAME());
                        if (getDataCount < 1) {
                            bundle.putString("result", value);
                            fragment.setArguments(bundle);// 将bundle数据加到Fragment中
                            beginTransaction.add(R.id.ll_case, fragment, "fragment");
                            beginTransaction.commitAllowingStateLoss();
                        } else {
                            fragment.setResult(value);
                        }
                        getDataCount++;
                    }
                }
            }
        }, this);
    }


}
