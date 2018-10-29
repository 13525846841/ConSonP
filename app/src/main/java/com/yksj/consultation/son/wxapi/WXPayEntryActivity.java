package com.yksj.consultation.son.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.buyandsell.MyOrdersActivity;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.main.OrderActivity;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.healthtalk.entity.PaySuccessInfoEntity;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import de.greenrobot.event.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private TextView mConsulting;
    private TextView checkOrder;
    private TextView pay_id;
    private TextView payfail;
    public LinearLayout paySuccess;
    public LinearLayout payFail;
    public LinearLayout outPatient;//门诊订单
    public ImageView titleLeftBtn;

    private int orderType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay);
        titleLeftBtn = (ImageView) findViewById(R.id.title_back);
        paySuccess = (LinearLayout) findViewById(R.id.ll_success);
        payFail = (LinearLayout) findViewById(R.id.ll_fail);
        outPatient = (LinearLayout) findViewById(R.id.entryOrder);

        mConsulting = (TextView) findViewById(R.id.consulting);
        checkOrder = (TextView) findViewById(R.id.checkOrder);

        payfail = (TextView) findViewById(R.id.fail);
        payfail.setOnClickListener(this);
        findViewById(R.id.consulting).setOnClickListener(this);
        findViewById(R.id.checkOrder).setOnClickListener(this);
        titleLeftBtn.setOnClickListener(this);
//    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api = WXAPIFactory.createWXAPI(this, null);
// 将该app注册到微信
        api.registerApp(Constants.APP_ID);
        api.handleIntent(getIntent(), this);
//		initTitle();
//		titleLeftBtn.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (0 == resp.errCode) {
            if (1000 == PaySuccessInfoEntity.getInstance().getPAYLOGO()) {
                paySuccess.setVisibility(View.VISIBLE);
                outPatient.setVisibility(View.GONE);
                payFail.setVisibility(View.GONE);
                pay_id = (TextView) findViewById(R.id.pay_id);
                pay_id.setText("订单号:      " + PaySuccessInfoEntity.getInstance().getPay_id());
                findViewById(R.id.consulting).setOnClickListener(this);
                findViewById(R.id.checkOrder).setOnClickListener(this);
            } else if (2000 == PaySuccessInfoEntity.getInstance().getPAYLOGO()) {
                paySuccess.setVisibility(View.GONE);
                outPatient.setVisibility(View.VISIBLE);
                payFail.setVisibility(View.GONE);
                findViewById(R.id.entry_order).setOnClickListener(this);
            } else if (3000 == PaySuccessInfoEntity.getInstance().getPAYLOGO()) {
                ToastUtil.onShow(this,"支付成功",1000);
                EventBus.getDefault().post(new MyEvent("", Constants.WXSUCCESS));
                finish();
            }else {
                orderType = 1;
                findViewById(R.id.consulting).setVisibility(View.GONE);
                paySuccess.setVisibility(View.VISIBLE);
                outPatient.setVisibility(View.GONE);
                payFail.setVisibility(View.GONE);
            }
        } else {
            paySuccess.setVisibility(View.GONE);
            outPatient.setVisibility(View.GONE);
            payFail.setVisibility(View.VISIBLE);
            payfail.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.consulting:
                FriendHttpUtil.chatFromPerson(WXPayEntryActivity.this, PaySuccessInfoEntity.getInstance().getId(), PaySuccessInfoEntity.getInstance().getName(), PaySuccessInfoEntity.getInstance().getOrder_id(), ObjectType.SPECIAL_SERVER);
                finish();
                break;
            case R.id.checkOrder:
                if (orderType == 0) {
                    intent = new Intent(this, OrderActivity.class);
                    startActivity(intent);
                } else {
                    EventBus.getDefault().post(new MyEvent("", Constants.WXSUCCESS));
                    finish();
                }
                finish();
                break;
            case R.id.fail:
                finish();
                break;
            case R.id.entry_order://门诊支付成功，进入我的订单界面
                intent = new Intent(this, MyOrdersActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MyEvent("", Constants.WXSUCCESS));
        super.onBackPressed();
    }
}