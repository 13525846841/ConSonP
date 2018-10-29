package com.yksj.consultation.son.consultation.outpatient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.doctor.AtyDoctorMassage;
import com.yksj.consultation.son.friend.ServicePayMainUi;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.DoctorOrderDeatils;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;


/**
 * Created by HEKL on 2015/9/18.
 * Used for 查看预约详情
 */
public class AtyOutPatientDetail extends BaseFragmentActivity implements View.OnClickListener {
    private DoctorOrderDeatils mDeatils;//预约订单详情实体类
    private ImageView imageHead;//头像
    private ImageView mImageMore;//查看更多
    private TextView textDocName;//医生姓名
    private TextView textDocOffices;//医生科室
    private TextView textDocTitle;//医生头衔
    private TextView textDocHosipital;//医生医院
    private TextView textRemarks;//备注信息
    private TextView textPatientName;//患者姓名
    private TextView textPatientPhone;//患者手机
    private TextView textTip;//温馨提示
    private TextView mState, mrefund;//预约订单状态,退款
    private TextView textTime, textAddress, textPrice;//时间,地址,价格
    private Button mCancel;//取消预约
    private int state, docId;//订单id,医生id
    private int conId;//会诊id
    private String orderId;//预约id
    private String cancelMan;//取消人
    private String cancelTime;//取消时间
    private String cancelReason;//取消原因
    private String aTime;//提交退款申请时间
    private String bTime;//退款成功时间
    private int doctorType;//医生类型
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private boolean Expanded = false;//true展开,false隐藏

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_outpatient_detail);
        EventBus.getDefault().register(AtyOutPatientDetail.this);
        if (null != arg0) {
            orderId = arg0.getString("orderId");
        } else {
            orderId = getIntent().getStringExtra("ORIDERID");
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(AtyOutPatientDetail.this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("orderId", orderId);
    }

    private void initView() {
        initTitle();
        textDocHosipital = (TextView) findViewById(R.id.tv_hospital);
        imageHead = (ImageView) findViewById(R.id.image_dochead);
        textAddress = (TextView) findViewById(R.id.tv_address);
        textDocOffices = (TextView) findViewById(R.id.tv_office);
        textDocTitle = (TextView) findViewById(R.id.tv_title);
        textDocName = (TextView) findViewById(R.id.tv_docName);
        mImageMore = (ImageView) findViewById(R.id.image_more);
        textPrice = (TextView) findViewById(R.id.tv_price);
        mrefund = (TextView) findViewById(R.id.tv_refund);
        textTime = (TextView) findViewById(R.id.tv_time);
        mCancel = (Button) findViewById(R.id.btn_cancel);
        mState = (TextView) findViewById(R.id.tv_state);
        textRemarks = (TextView) findViewById(R.id.tv_remarks);
        textPatientName = (TextView) findViewById(R.id.tv_pname);
        textPatientPhone = (TextView) findViewById(R.id.tv_phone);
        textTip = (TextView) findViewById(R.id.tv_warn);
        mImageLoader = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(this);
        titleTextV.setText("查看详情");
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.rl_refund).setOnClickListener(this);
        findViewById(R.id.btn_gopay).setOnClickListener(this);
        findViewById(R.id.rl_docinfo).setOnClickListener(this);
        titleLeftBtn.setOnClickListener(this);
        mImageMore.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        initData();
    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.title_back://回退
                onBackPressed();
                break;
            case R.id.image_more://备注更多
                if (Expanded) {
                    Expanded = false;
                    textRemarks.setMaxLines(2);
                    mImageMore.setImageResource(R.drawable.gengduos);
                } else {
                    Expanded = true;
                    textRemarks.setMaxLines(100);
                    mImageMore.setImageResource(R.drawable.shouqis);
                }
                break;
            case R.id.rl_docinfo://查看个人信息
                i = new Intent(AtyOutPatientDetail.this, AtyDoctorMassage.class);
                i.putExtra("id", docId + "");//医生id
                i.putExtra("type", doctorType);//类型0时是专家,1是医生
                i.putExtra("ORDER", 0);//是否有选择他
                startActivity(i);

                break;
            case R.id.btn_gopay://去支付
                switch (state) {
                    case 10:
                        try {
                            JSONObject json = new JSONObject();
                            json.put("PATIENT_NAME", mDeatils.getPATIENT_NAME());
                            json.put("PATIENT_PHONE", mDeatils.getPATIENT_PHONE());
                            json.put("ADVICE_CONTENT", mDeatils.getADVICE_CONTENT());
                            json.put("SERVICE_PRICE", mDeatils.getSERVICE_GOLD());
                            json.put("SERVICE_ITEM_ID", mDeatils.getSERVICE_ITEM_ID());
                            json.put("SERVICE_TYPE_SUB_ID", mDeatils.getSERVICE_TYPE_SUB_ID());
                            json.put("SERVICE_TYPE_ID", mDeatils.getSERVICE_TYPE_ID());
                            json.put("ORDER_ID", mDeatils.getORDER_ID());
                            json.put("SERVICE_START", mDeatils.getSERVICE_START());
                            json.put("SERVICE_PLACE", mDeatils.getSERVICE_PLACE());
                            CustomerInfoEntity infoEntity = new CustomerInfoEntity();
                            infoEntity.setId(mDeatils.getSERVICE_CUSTOMER_ID());
//                          infoEntity.setName(mDataJson.optString("customerNickname",""));
                            i = new Intent(AtyOutPatientDetail.this, ServicePayMainUi.class);
                            i.putExtra("json", json.toString());
                            i.putExtra("doctorInfoEntity", infoEntity);
                            i.putExtra("Type", 0);
                            i.putExtra("consultationId", conId + "");
                            i.putExtra(ServicePayMainUi.DOCTOR_ID, mDeatils.getSERVICE_CUSTOMER_ID());
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

                break;
            case R.id.btn_cancel://取消预约/删除
                switch (state) {
                    case 10:
                        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确定要删除吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                            @Override
                            public void onDismiss(DialogFragment fragment) {
                            }

                            @Override
                            public void onClick(DialogFragment fragment, View v) {
                                HttpRestClient.doHttpDeleteOrder(mDeatils.getENJOY_CUSTOMER_ID(), mDeatils.getSERVICE_CUSTOMER_ID(), mDeatils.getORDER_ID(), new AsyncHander(R.id.btn_cancel));
                            }
                        });
                        break;
                    case 50:
                        i = new Intent(this, AtyCancelOutPatient.class);
                        i.putExtra("orderId", orderId);
                        i.putExtra("docId", docId);
                        startActivity(i);
                        break;
                }
                break;
            case R.id.rl_refund://查看取消原因/退款
                switch (state) {
                    case 175:
                    case 180:
                        i = new Intent(this, AtyCancelReasons.class);
                        i.putExtra("MAN", cancelMan);
                        i.putExtra("TIME", cancelTime);
                        i.putExtra("REASONS", cancelReason);
                        startActivity(i);
                        break;
                    case 222:
                    case 232:
                    case 242:
                        i = new Intent(this, AtyRefundDedails.class);
                        i.putExtra("Price", textPrice.getText().toString());
                        i.putExtra("aTime", aTime);
                        i.putExtra("bTime", bTime);
                        startActivity(i);
                        break;
                }
                break;
        }
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.put("ORDER_ID", orderId);
        params.put("Type", "queryOrderMessage");
        HttpRestClient.doHttpSERVICESETSERVLETRJ420(params, new AsyncHttpResponseHandler(this) {


            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (!TextUtils.isEmpty(content)) {

                    try {
                        JSONObject object = new JSONObject(content);
                        if (object.optInt("code") == 1) {
                            mDeatils = DoctorOrderDeatils.parsToEntity(object.getJSONObject("result").toString());
                            JSONObject obj = object.getJSONObject("result");
                            aTime = obj.optString("ATIME");
                            bTime = obj.optString("BTIME");
                            state = obj.optInt("SERVICE_STATUS");
                            docId = obj.optInt("SERVICE_CUSTOMER_ID");
                            textTime.setText(TimeUtil.getDemo(obj.optString("SERVICE_START"), obj.optString("SERVICE_END")));
                            textAddress.setText(obj.optString("SERVICE_PLACE"));
                            textPrice.setText(obj.optString("SERVICE_GOLD"));
                            mImageLoader.displayImage(obj.getJSONObject("DOCTOR").optString("ICON"), imageHead, mOptions);
                            textDocName.setText(obj.getJSONObject("DOCTOR").optString("NAME"));
                            textDocTitle.setText(obj.getJSONObject("DOCTOR").optString("TITLE"));
                            textDocOffices.setText(obj.getJSONObject("DOCTOR").optString("OFFICE"));
//                            textPatientName.setText(obj.optString("PATIENT_NAME"));

                            if (HStringUtil.isEmpty(obj.optString("REAL_NAME"))) {
                                textPatientName.setText("匿名");
                            } else {
                                textPatientName.setText(obj.optString("REAL_NAME"));
                            }

                            if (obj.getJSONObject("DOCTOR").optInt("TYPE") == 10) {
                                doctorType=0;
                            } else if (obj.getJSONObject("DOCTOR").optInt("TYPE") == 20) {
                                doctorType=1;
                            }

                            if ("null".equals(obj.optString("ADVICE_CONTENT"))) {
                                findViewById(R.id.view3).setVisibility(View.GONE);
                            } else {
                                textRemarks.setText(obj.optString("ADVICE_CONTENT"));
                            }
                            textPatientPhone.setText(obj.optString("PATIENT_PHONE"));
                            cancelTime = obj.optString("CANCEL_TIME");
                            cancelReason = obj.optString("CANCEL_REASON");
                            textDocHosipital.setText(obj.getJSONObject("DOCTOR").optString("HOSPITAL"));
                            conId = obj.optInt("MERCHANT_ID");
                            findViewById(R.id.image_more).setOnClickListener(AtyOutPatientDetail.this);
                            //判断是否展开
                            if (textRemarks.getText().length() > 30) {
                                findViewById(R.id.image_more).setVisibility(View.VISIBLE);
                            }
                            if (state == 175) {
                                cancelMan = obj.optString("PATIENT_NAME");
                            } else if (state == 180) {
                                cancelMan = obj.getJSONObject("DOCTOR").optString("NAME");
                            }
                            findViewById(R.id.rl_gopay).setVisibility(View.GONE);
                            findViewById(R.id.ll_tip).setVisibility(View.GONE);
                            switch (state) {
                                case 10://待支付
                                    findViewById(R.id.rl_gopay).setVisibility(View.VISIBLE);
                                    findViewById(R.id.btn_gopay).setVisibility(View.VISIBLE);
                                    findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
                                    mCancel.setText("删除");
                                    mState.setText("待付款");
                                    break;
                                case 30://支付失败
                                    textTip.setText("支付失败，或未支付超过60分钟，订单关闭");
                                    mState.setText("支付失败");
                                    break;
                                case 40://用户主动撤单
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    findViewById(R.id.rl_gopay).setVisibility(View.GONE);
                                    mState.setText("已取消");
                                    break;
                                case 50://支付后的状态
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    findViewById(R.id.rl_gopay).setVisibility(View.GONE);
                                    if (TimeUtil.formatMillion(obj.optString("SERVICE_START")) <= TimeUtil.formatMillion(obj.optString("SYSTEMTIME"))) {
                                        mState.setText("服务中");
                                    } else if (TimeUtil.formatMillion(obj.optString("SERVICE_START")) > TimeUtil.formatMillion(obj.optString("SYSTEMTIME")) && TimeUtil.formatMillion(obj.optString("SERVICE_START")) < TimeUtil.formatMillion(obj.optString("SYSTEMTIME")) + 2 * 60 * 60 * 1000) {
                                        mState.setText("待服务");
//                                        textTip.setText("温馨提示: " + "当前时间距预约服务开始时间已不足12个小时,不能取消预约服务");
                                    } else {
                                        mState.setText("待服务");
                                        findViewById(R.id.rl_gopay).setVisibility(View.VISIBLE);
                                        findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case 60:
                                case 70:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    mState.setText("已取消");
                                    break;
                                case 175://取消状态
                                case 180:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    findViewById(R.id.rl_gopay).setVisibility(View.GONE);
                                    findViewById(R.id.btn_cancel).setVisibility(View.GONE);
                                    findViewById(R.id.ll_tip).setVisibility(View.GONE);
                                    findViewById(R.id.rl_refund).setVisibility(View.VISIBLE);
                                    mrefund.setText("取消原因");
                                    mState.setText("已取消");
                                    break;
                                case 198:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    mState.setText("已终止");
                                    break;
                                case 199://自然终结
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    mState.setText("已完成");
                                    break;
                                case 222:
                                case 232:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    findViewById(R.id.rl_refund).setVisibility(View.VISIBLE);
                                    mrefund.setText("退款中");
                                    mState.setText("待退款");
                                    break;
                                case 242:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    findViewById(R.id.rl_refund).setVisibility(View.VISIBLE);
                                    mrefund.setText("已退款");
                                    mState.setText("退款已完成");
                                    break;
                                case 252:
                                    findViewById(R.id.ll_tip).setVisibility(View.VISIBLE);
                                    textTip.setText(obj.optString("PMESSAGE"));
                                    mState.setText("退款失败");
                                    break;

                            }
                        } else {
                            ToastUtil.showShort(object.optString("message"));
                        }
                    } catch (Exception e) {
                        onBackPressed();
                    }
                }

            }
        });
    }

    public void onEvent(MyEvent event) {
        if ("outPatientRefresh".equals(event.what)) {
            initData();
        }
    }

    class AsyncHander extends AsyncHttpResponseHandler {
        private int id;

        public AsyncHander(int id) {
            super(AtyOutPatientDetail.this);
            this.id = id;
        }

        @Override
        public void onSuccess(int statusCode, String content) {
            try {
                super.onSuccess(statusCode, content);
                JSONObject object2 = null;
                if (content != null)
                    object2 = new JSONObject(content);

                switch (id) {
                    case R.id.btn_cancel://删除订单
                        if (object2 != null) {
                            ToastUtil.showShort(object2.optString("message"));
                            if (object2.optInt("code") == 1) {
                                AtyOutPatientDetail.this.finish();
                            }
                        }
                        break;
                }

            } catch (Exception e) {
            }
        }
    }
}
