package com.yksj.consultation.son.consultation.consultationorders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.casehistory.CaseShowFragment;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.consultation.AtyPatientMassage;
import com.yksj.consultation.son.consultation.PAtyConsultGoPaying;
import com.yksj.consultation.son.consultation.bean.ConsultDetailsFBean;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.main.AppraiseActivity;
import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;
import com.yksj.consultation.son.doctor.AtyDoctorMassage;
import com.yksj.consultation.son.doctor.SelectExpertMainUI;
import com.yksj.consultation.son.friend.BuyServiceListFromPatientActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 15/9/23.
 * 订单详情_
 */
public class AtyOrderDetails extends BaseFragmentActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView> {
    private TextView mPrice, tProcess1, tProcess2, tProcess3, tProcess4, mTip, mCaseSupply;
    private ImageView iProcess1, iProcess2, iProcess3, mImageHeadP, mImageHeadD, mImageHeadE;
    private Button mOpinion, mOutPatient, mEvaluate;
    private PullToRefreshScrollView mPullToRefreshScrollView;//整体滑动布局
    private CaseShowFragment fragment;//病历
    private FragmentTransaction beginTransaction;
    private ConsultDetailsFBean bean;//订单实体
    private CustomerInfoEntity entity;//个人信息实体
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOption;//异步加载图片的操作
    private String patientIcon;//患者头像
    private String value;//传入到fragment的值
    private String doctorIcon;//医生头像
    private String doctorName;//医生姓名
    private String expertName;//专家姓名
    private String doctorTitle;//医生科室
    private String doctorHospital;//医生医院
    private String conName;//会诊名称
    private Bundle bundle;//点击头像传入的值
    private int docId, expId;//医生和专家id
    private int getDataCount;//刷新订单的次数
    private int openComment;//是否开启评价
    private int invitation;//是否有医生预约
    private int openOrder;//是否有专家预约
    private int promoter;//发起者类型
    private int officeId;//科室id
    private int conId;//会诊id
    private String price;//会诊价钱
    //    private double price2;//会诊价钱
    private int orderId;//预约门诊详情id
    private int type;//订单状态status
    private int loadCount;//请求次数
    private String cusId;
    private String groupId = "";//群聊id


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_orderdetail);
        EventBus.getDefault().register(this);
        if (null != arg0) {
            if (!TextUtils.isEmpty(arg0.getString("CONID"))) {
                conId = Integer.valueOf(arg0.getString("CONID"));
            }
        } else {
            conId = getIntent().getIntExtra("CONID", 0);//会诊Id
        }
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromServer(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beginTransaction != null || fragment != null) {
            beginTransaction = null;
            fragment = null;
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 控件的初始化
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        //标题初始化
        initTitle();
        //titleTextV.setText("查看详情"+" "+conId);

        titleTextV.setText("会诊详情");

        titleLeftBtn.setOnClickListener(this);
        //初始化
        mImageLoader = ImageLoader.getInstance();//异步加载图片的初始化
        mOption = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);//头像默认显示操作
        entity = new CustomerInfoEntity();//门诊预约传入实体类
        cusId = LoginServiceManeger.instance().getLoginUserId();
        //病历
        fragment = new CaseShowFragment();
        bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);//滑动
        mCaseSupply = (TextView) findViewById(R.id.tv_casesupply);//病历补充要求
        mImageHeadP = (ImageView) findViewById(R.id.image_head_p);//患者头像
        mImageHeadD = (ImageView) findViewById(R.id.image_head_d);//医生头像
        mImageHeadE = (ImageView) findViewById(R.id.image_head_e);//专家头像
        mOutPatient = (Button) findViewById(R.id.outpatient);//按钮1
        mEvaluate = (Button) findViewById(R.id.evaluate);//按钮2
        mOpinion = (Button) findViewById(R.id.opinion);//按钮3
        //进度
        iProcess1 = (ImageView) findViewById(R.id.iv_process1);
        iProcess2 = (ImageView) findViewById(R.id.iv_process2);
        iProcess3 = (ImageView) findViewById(R.id.iv_process3);
        tProcess1 = (TextView) findViewById(R.id.tv_process1);
        tProcess2 = (TextView) findViewById(R.id.tv_process2);
        tProcess3 = (TextView) findViewById(R.id.tv_process3);
        tProcess4 = (TextView) findViewById(R.id.tv_process4);

        mPrice = (TextView) findViewById(R.id.tv_price);//价钱
        mTip = (TextView) findViewById(R.id.tv_consul);//提示
        //点击
        findViewById(R.id.btn_talk).setOnClickListener(this);//对话
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mImageHeadP.setOnClickListener(this);
        mImageHeadD.setOnClickListener(this);
        mImageHeadE.setOnClickListener(this);
        mOutPatient.setOnClickListener(this);
        mEvaluate.setOnClickListener(this);
        mOpinion.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CONID", conId + "");
    }

    @Override
    public void onClick(View view) {
        Intent i = null;
        switch (view.getId()) {
            case R.id.title_back://回退
                onBackPressed();
                break;
            case R.id.title_right2://取消服务跳转按钮
                i = new Intent(AtyOrderDetails.this, AtyCancelConsult.class);
                i.putExtra("CONID", conId + "");
                i.putExtra("CON", 0);
                startActivity(i);
                break;
            case R.id.btn_talk://对话
                if (loadCount == 0) {
                    getDataFromServer(1);
                }
                break;
            case R.id.opinion://按钮1
                switch (type) {
                    case 60://患者重选专家
                        if (officeId != 0) {//存在科室id
                            i = new Intent(AtyOrderDetails.this, SelectExpertMainUI.class);
                            i.putExtra("consultId", conId + "");//会诊id
                            i.putExtra("OFFICECODE", officeId + "");//科室id
                            i.putExtra("goalType", 2);//从哪里跳到选专家中
                            startActivity(i);
                        }
                        break;
                    case 99://已完成后查看会诊意见
                        if (!((openOrder != 1) && (openComment != 1))) {//有预约和评价
                            i = new Intent(AtyOrderDetails.this, AtyConsultOpinion.class);
                            i.putExtra("conId", conId);
                            startActivity(i);
                        }
                        break;
                }
                break;
            case R.id.outpatient://按钮2
                switch (type) {
                    case 20:
                    case 30://医生填完病历,未发给专家
                    case 55:
                    case 85:
                        entity.setId(docId + "");
                        switch (invitation) {
                            case 1://预约加号
                                i = new Intent(AtyOrderDetails.this, BuyServiceListFromPatientActivity.class);
                                i.putExtra("consultId", conId + "");
                                i.putExtra("titleName", doctorName);
                                i.putExtra("type", 3);
                                i.putExtra("mCustomerInfoEntity", entity);
                                i.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, docId + "");//医生ID
                                startActivity(i);
                                break;
                            case 2://预约详情
                                i = new Intent(AtyOrderDetails.this, AtyOutPatientDetail.class);
                                i.putExtra("ORIDERID", orderId + "");
                                startActivity(i);
                                break;
                        }
                        break;
                    case 70://待支付
                        i = new Intent(AtyOrderDetails.this, PAtyConsultGoPaying.class);
                        i.putExtra("consultId", conId + "");
                        i.putExtra("price", price);//价格
                        startActivity(i);
                        break;
                    case 80://等意见提醒专家
                    case 88://与80相同
                        mindExpert();
                        break;
                    case 99://查看会诊意见
                        entity.setId(expId + "");
                        if ((openOrder == 1) && (openComment == 1)) {//有预约和评价,跳转预约
                            i = new Intent(AtyOrderDetails.this, BuyServiceListFromPatientActivity.class);
                            i.putExtra("consultId", conId + "");
                            i.putExtra("titleName", expertName);
                            i.putExtra("type", 3);
                            i.putExtra("mCustomerInfoEntity", entity);
                            i.putExtra(BuyServiceListFromPatientActivity.DOCTOR_ID, expId + "");//医生ID
                            startActivity(i);
                        } else if ((openOrder != 1) && (openComment != 1)) {//没有预约和评价,跳转意见详情
                            i = new Intent(AtyOrderDetails.this, AtyConsultOpinion.class);
                            i.putExtra("conId", conId);
                            startActivity(i);
                        }
                        break;
                    case 232://退款中
                        i = new Intent(this, AtyOrderRefundDedails.class);
                        i.putExtra("Price", mPrice.getText().toString());
                        i.putExtra("Type", 0);
                        startActivity(i);
                        break;
                    case 242://已退款
                        i = new Intent(this, AtyOrderRefundDedails.class);
                        i.putExtra("Price", mPrice.getText().toString());
                        i.putExtra("Type", 1);
                        startActivity(i);
                        break;
                }
                break;
            case R.id.evaluate://按钮3
                switch (type) {
                    case 99://已完成
                        entity.setId(expId + "");
                        if ((openOrder == 1) && (openComment != 1)) {//有预约没有评价
                            i = new Intent(AtyOrderDetails.this, BuyServiceListFromPatientActivity.class);//门诊预约
                            i.putExtra("consultId", conId + "");
                            i.putExtra("titleName", expertName);
                            i.putExtra("type", 3);
                            i.putExtra("mCustomerInfoEntity", entity);
                            startActivity(i);
                        } else if (openComment == 1) {
                            i = new Intent(this, AppraiseActivity.class);
                            i.putExtra("ORDER_ID", conId + "");
                            startActivity(i);
//                            i = new Intent(AtyOrderDetails.this, AtyConsultEvaluate.class);//评价
//                            i.putExtra("DoctorIcon", doctorIcon);
//                            i.putExtra("DoctorName", doctorName);
//                            i.putExtra("DoctorTitle", doctorTitle);
//                            i.putExtra("DoctorHospital", doctorHospital);
//                            i.putExtra("conId", conId);
//                            startActivity(i);
                        }
                        break;
                }
                break;
            case R.id.image_head_p://患者头像
                if (!"".equals(patientIcon)) {
                    i = new Intent(AtyOrderDetails.this, AtyPatientMassage.class);//查看个人信息
                    i.putExtra("ORDER", 0);//是否有选择他
                    startActivity(i);
                }
                break;
            case R.id.image_head_d://医生头像
                if (docId != 0) {//如果存在医生
                    i = new Intent(AtyOrderDetails.this, AtyDoctorMassage.class);//查看个人信息
                    i.putExtra("id", docId + "");//医生id
                    i.putExtra("type", 1);//类型0时是专家,1是医生
                    i.putExtra("ORDER", 0);//是否有选择他
                    startActivity(i);
                }
                break;
            case R.id.image_head_e://专家头像
                if (expId != 0) {//如果存在专家id
                    i = new Intent(AtyOrderDetails.this, AtyDoctorMassage.class);//查看个人信息
                    i.putExtra("id", expId + "");//专家id
                    i.putExtra("type", 0);//类型0时是专家,1是医生
                    i.putExtra("ORDER", 0);//是否有选择他
                    startActivity(i);
                }
                break;
        }
    }

    /**
     * 会诊详情
     *
     * @param loadType 0 加载订单详情 1 判断对话状态
     */
    private void getDataFromServer(final int loadType) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CONSULTATIONID", conId + ""));
        pairs.add(new BasicNameValuePair("CUSTID", cusId));
        HttpRestClient.OKHttpConsultInfo(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                loadCount = 1;
                if (loadType == 0) {
                    mPullToRefreshScrollView.setRefreshing();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                loadCount = 0;
                if (loadType == 0) {
                    mPullToRefreshScrollView.onRefreshComplete();
                } else {
                    switch (type) {
                        case 20:
                        case 30:
                        case 50:
                        case 55:
                        case 60:
                        case 70:
                            jumpChat(docId + "", doctorName);
                            break;
                        case 80:
                        case 85:
                        case 88:
                        case 99:
                            doChat(conId + "", conName);
                            break;
                        case 90:
                        case 95:
                        case 222:
                        case 232:
                        case 243:
                        case 252:
                            ToastUtil.showShort("对不起,服务已取消,不能对话");
                            break;
                    }
                }

            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    handleData(response);
                }

            }
        }, this);
    }

    /**
     * 提醒专家
     */
    private void mindExpert() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("OPTION", "10"));
        pairs.add(new BasicNameValuePair("CONSULTATIONID", conId + ""));
        pairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.OKHttpmindExpert(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        ToastUtil.showShort(object.optString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, this);

    }

    /**
     * 刷新会诊详情
     *
     * @param event
     */
    public void onEvent(MyEvent event) {
        if ("refresh".equals(event.what)) {
            getDataFromServer(0);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        getDataFromServer(0);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("BACK")) {
            int backMain = getIntent().getIntExtra("BACK", 0);
            if (backMain == 2) {
                Intent intent = new Intent(AtyOrderDetails.this, AtyMyOrders.class);
                intent.putExtra("BACK", 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }


    /**
     * 处理订单数据绑定
     *
     * @param response 返回数据
     */
    private void handleData(String response) {
        Gson gson = new Gson();
        bean = gson.fromJson(response, ConsultDetailsFBean.class);
        JSONObject obj = null;
        JSONObject object = null;
        try {
            obj = new JSONObject(response);
            TextView pName = (TextView) findViewById(R.id.tv_patientname);
            TextView dName = (TextView) findViewById(R.id.tv_docName);
            TextView eName = (TextView) findViewById(R.id.tv_expertName);
            type = obj.getJSONObject("result").optInt("STATUS");
            value = obj.getJSONObject("result").toString();
            orderId = obj.getJSONObject("result").optInt("ORDER_ID_DOCTOR");
            groupId = obj.getJSONObject("result").optString("GROUP_ID");
            if (type <= 70) {
                titleRightBtn2.setOnClickListener(AtyOrderDetails.this);
                titleRightBtn2.setText("取消");
                titleRightBtn2.setVisibility(View.VISIBLE);
            } else {
                titleRightBtn2.setVisibility(View.GONE);
            }
            //患者头像
            if (!"".equals(obj.getJSONObject("result").optString("PATIENT"))) {
                patientIcon = obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTICON");
                if ("null".equals(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME")) || ("".equals(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME")))) {
                    pName.setText("患者");
                } else {
                    pName.setText(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTNAME"));
                }
//                            Picasso.with(AtyOrderDetails.this).load(obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTICON")).placeholder(R.drawable.default_head_female).error(R.drawable.default_head_female).into(mImageHeadP);
                String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+obj.getJSONObject("result").getJSONObject("PATIENT").optString("PATIENTICON");
//                Picasso.with(AtyOrderDetails.this).load(url).error(R.drawable.default_head_patient).placeholder(R.drawable.default_head_patient).into(mImageHeadP);
                mImageLoader.displayImage(url, mImageHeadP, mOption);
            } else {
                findViewById(R.id.rl_head_p).setVisibility(View.GONE);
            }
            //医生头像
            if (!"".equals(obj.getJSONObject("result").optString("DOCTOR"))) {
                docId = obj.getJSONObject("result").getJSONObject("DOCTOR").optInt("DOCTORID");
                doctorName = obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORNAME");
                doctorIcon = obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORICON");
                doctorTitle = obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORTITLE");
                doctorHospital = obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORHOSPITAL");
//                            Picasso.with(AtyOrderDetails.this).load(obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORICON")).placeholder(R.drawable.default_head_female).error(R.drawable.default_head_female).into(mImageHeadD);
                mImageLoader.displayImage(obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORICON"), mImageHeadD, mOption);


//                String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+obj.getJSONObject("result").getJSONObject("DOCTOR").optString("DOCTORICON");
//                Picasso.with(AtyOrderDetails.this).load(url).error(R.drawable.default_head_doctor).placeholder(R.drawable.default_head_doctor).into(mImageHeadD);
                if ("null".equals(doctorName) || ("".equals(doctorName))) {
                    dName.setText("会诊医生");
                } else {
                    dName.setText(doctorName);
                }
            } else {
                findViewById(R.id.rl_head_d).setVisibility(View.GONE);
            }
            //专家头像
            if (!"".equals(obj.getJSONObject("result").optString("EXPERT"))) {
                expId = obj.getJSONObject("result").getJSONObject("EXPERT").optInt("EXPERTID");
                expertName = obj.getJSONObject("result").getJSONObject("EXPERT").optString("EXPERTNAME");
//                            Picasso.with(AtyOrderDetails.this).load(obj.getJSONObject("result").getJSONObject("EXPERT").optString("EXPERTICON")).placeholder(R.drawable.default_head_female).error(R.drawable.default_head_female).into(mImageHeadE);
                mImageLoader.displayImage(obj.getJSONObject("result").getJSONObject("EXPERT").optString("EXPERTICON"), mImageHeadE, mOption);
                if ("null".equals(expertName) || ("".equals(expertName))) {
                    eName.setText("会诊专家");
                } else {
                    eName.setText(expertName);
                }
            } else {
                findViewById(R.id.rl_head_e).setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (bean == null) {
            return;
        } else if ("0".equals(bean.getCode())) {
            ToastUtil.showShort(bean.getMessage());
        } else {
            conName = bean.getResult().getCON_NAME();
            officeId = bean.getResult().getOFFICE_ID();
            mPrice.setText(bean.getResult().getPRICE());
            mTip.setText(bean.getResult().getSTATUSNAME());
            invitation = bean.getResult().getINVITATION_FLAG();
            openComment = (bean.getResult().getOPENCOMMENT());
            openOrder = (bean.getResult().getOPENEXPERTORDER());
            tProcess1.setText(bean.getResult().getPROCESS().get(0).getName());
            tProcess2.setText(bean.getResult().getPROCESS().get(1).getName());
            tProcess3.setText(bean.getResult().getPROCESS().get(2).getName());
            tProcess4.setText(bean.getResult().getPROCESS().get(3).getName());
            if ("0".equals(bean.getResult().getPROCESS().get(0).getColor())) {
                tProcess1.setTextColor(getResources().getColor(R.color.color_blue));
            } else if ("1".equals(bean.getResult().getPROCESS().get(0).getColor())) {
                tProcess1.setTextColor(getResources().getColor(R.color.red));
            }
            if ("0".equals(bean.getResult().getPROCESS().get(1).getColor())) {
                tProcess2.setTextColor(getResources().getColor(R.color.color_blue));
                iProcess1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            } else if ("1".equals(bean.getResult().getPROCESS().get(1).getColor())) {
                tProcess2.setTextColor(getResources().getColor(R.color.red));
                iProcess1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            }
            if ("0".equals(bean.getResult().getPROCESS().get(2).getColor())) {
                tProcess3.setTextColor(getResources().getColor(R.color.color_blue));
                iProcess2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            } else if ("1".equals(bean.getResult().getPROCESS().get(2).getColor())) {
                tProcess3.setTextColor(getResources().getColor(R.color.red));
                iProcess2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            }
            if ("0".equals(bean.getResult().getPROCESS().get(3).getColor())) {
                tProcess4.setTextColor(getResources().getColor(R.color.color_blue));
                iProcess3.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            } else if ("1".equals(bean.getResult().getPROCESS().get(3).getColor())) {
                tProcess4.setTextColor(getResources().getColor(R.color.red));
                iProcess3.setImageDrawable(getResources().getDrawable(R.drawable.arrow_green));
            }
            if (fragment != null) {
                if (getDataCount < 1) {
                    bundle.putString("result", value);
                    fragment.setArguments(bundle);// 将bundle数据加到Fragment中
                    beginTransaction.add(R.id.ll_case, fragment, "fragment");
                    beginTransaction.commitAllowingStateLoss();
                } else {
                    fragment.setResult(value);
                }
            }
            getDataCount++;
            if (expId != 0) {
                findViewById(R.id.ll_price).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.ll_done).setVisibility(View.GONE);
            mOpinion.setVisibility(View.GONE);
            mOutPatient.setVisibility(View.GONE);
            mEvaluate.setVisibility(View.GONE);
            switch (type) {
                case 20://填病历
                case 30://修改病历
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    if ("".equals(bean.getResult().getPRICE())) {
                        findViewById(R.id.ll_price).setVisibility(View.GONE);
                    }
                    switch (invitation) {
                        case 1:
                            mOutPatient.setVisibility(View.VISIBLE);
                            mOutPatient.setText("预约门诊");
                            break;
                        case 2:
                            mOutPatient.setVisibility(View.VISIBLE);
                            mOutPatient.setText("预约详情");
                            break;
                    }
                    break;
                case 55://预约门诊
                case 85:
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    if (!"".equals(bean.getResult().getRECORDSUPPLY())) {
                        findViewById(R.id.rl_casesupply).setVisibility(View.VISIBLE);
                    }
                    switch (invitation) {
                        case 1:
                            mOutPatient.setVisibility(View.VISIBLE);
                            mOutPatient.setText("预约门诊");
                            break;
                        case 2:
                            mOutPatient.setVisibility(View.VISIBLE);
                            mOutPatient.setText("预约详情");
                            break;
                    }
                    mCaseSupply.setText(bean.getResult().getRECORDSUPPLY());
                    break;
                case 60://重选专家
                    promoter = bean.getResult().getPROMOTER_TYPE();
                    if (promoter == 10) {
                        findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                        mOpinion.setVisibility(View.VISIBLE);
                        mOpinion.setText("重选专家");
                    }
                    break;
                case 70://支付
                    price = bean.getResult().getPRICE();
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    mOutPatient.setVisibility(View.VISIBLE);
                    mOutPatient.setText("去支付");
                    break;
                case 80://等意见
                case 88://等意见
                    if (bean.getResult().getOPENREMIND() == 1) {
                        findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                        mOutPatient.setVisibility(View.VISIBLE);
                        // mOutPatient.setText("提醒专家");
                        mOutPatient.setText("点一下，提醒专家尽快给出意见");
                    }
                    break;
                case 99://已完成
                    findViewById(R.id.ll_done).setVisibility(View.GONE);
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    mOpinion.setVisibility(View.GONE);
                    mOutPatient.setVisibility(View.GONE);
                    mEvaluate.setVisibility(View.GONE);
                    if ((openOrder == 1) && (openComment == 1)) {
                        mOpinion.setVisibility(View.VISIBLE);
                        mOpinion.setText("会诊意见");
                        mOutPatient.setVisibility(View.VISIBLE);
                        mOutPatient.setText("预约门诊");
                        mEvaluate.setVisibility(View.VISIBLE);
                        mEvaluate.setText("评价");
                    } else if ((openOrder != 1) && (openComment == 1)) {
                        mOpinion.setVisibility(View.VISIBLE);
                        mOpinion.setText("会诊意见");
                        mEvaluate.setVisibility(View.VISIBLE);
                        mEvaluate.setText("评价");
                    } else if ((openOrder == 1) && (openComment != 1)) {
                        mOpinion.setVisibility(View.VISIBLE);
                        mOpinion.setText("会诊意见");
                        mEvaluate.setVisibility(View.VISIBLE);
                        mEvaluate.setText("预约门诊");
                    } else if ((openOrder != 1) && (openComment != 1)) {
                        mOutPatient.setVisibility(View.VISIBLE);
                        mOutPatient.setText("会诊意见");
                    }
                    break;
                case 232:
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    mOutPatient.setVisibility(View.VISIBLE);
                    mOutPatient.setText("退款中");
                    break;
                case 242:
                    findViewById(R.id.ll_done).setVisibility(View.VISIBLE);
                    mOutPatient.setVisibility(View.VISIBLE);
                    mOutPatient.setText("已退款");
                    break;
            }
        }
    }

    /**
     * 群聊天
     */
    public void doChat(String conId, String conName) {
        Intent intent = new Intent();
        if (type > 80) {
            intent.putExtra(ChatActivity.CONSULTATION_TYPE, "1");
        }
        intent.putExtra(ChatActivity.CONSULTATION_ID, conId);
        intent.putExtra(ChatActivity.GROUP_ID, groupId);
        intent.putExtra(ChatActivity.CONSULTATION_NAME, conName);
        intent.putExtra(ChatActivity.OBJECT_TYPE, ObjectType.CONSULT);
        intent.setClass(AtyOrderDetails.this, ChatActivity.class);
        startActivity(intent);
    }


    /**
     * 私聊
     */
    private void jumpChat(String otherId, String otherName) {
        Intent intent = new Intent();
        intent.putExtra(ChatActivity.CONSULTATION_ID, conId + "");
        intent.putExtra(ChatActivity.SINGLE_ID, otherId);
        intent.putExtra(ChatActivity.SINGLE_NAME, otherName);
        intent.putExtra(ChatActivity.OBJECT_TYPE, ObjectType.CONSULT);
        intent.setClass(AtyOrderDetails.this, ChatActivity.class);
        startActivity(intent);
    }
}

