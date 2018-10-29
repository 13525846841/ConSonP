package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEKL on 15/12/21.
 */

/**
 * 修改与16／11／7
 */
public class AtyAccountInfo extends BaseFragmentActivity implements View.OnClickListener {

    private EditText mZFBNUMS, mZFBName, mBankNUMs, mBankName, mBankAccName, mPhone;
    private TextView number;
    private RelativeLayout rl_chongzhi;
    private RelativeLayout rl_tixian;
    private String balance;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_accountinfo);
        intView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData2();
    }

    private void initData2() {
        HttpRestClient.OKHttpACCOUNTBALANCE(LoginServiceManeger.instance().getLoginUserId(), new ObjectHttpResponseHandler(this) {
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
                        balance = object.optString("balance");
                        number.setText(object.optString("balance"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void intView() {
        initTitle();
        titleTextV.setText("账户管理");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("账单明细");
        titleRightBtn2.setOnClickListener(this);

        number = (TextView) findViewById(R.id.tv_number);
        rl_chongzhi = (RelativeLayout) findViewById(R.id.rl_chongzhi);
        rl_tixian = (RelativeLayout) findViewById(R.id.rl_tixian);

        rl_chongzhi.setOnClickListener(this);
        rl_tixian.setOnClickListener(this);
//        mZFBNUMS = (EditText) findViewById(R.id.et_zhifubaozhanghao);
//        mZFBName = (EditText) findViewById(R.id.et_zhifubaoname);
//        mBankNUMs = (EditText) findViewById(R.id.et_banknums);
//        mBankName = (EditText) findViewById(R.id.et_bankname);
//        mBankAccName = (EditText) findViewById(R.id.et_bankacceptname);
//        mPhone = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2://账单明细
                intent = new Intent(this,AccountList.class);
                startActivity(intent);
                break;
            case R.id.rl_chongzhi://充值
//              intent = new Intent(this,TopUpActivity.class);
                intent = new Intent(this,Recharge.class);
                startActivity(intent);
                break;
            case R.id.rl_tixian://提现
                intent = new Intent(this,GetMoney.class);
                intent.putExtra("balance",balance);
                startActivity(intent);
                break;
//            case R.id.title_right2:
//                if ("".equals(mPhone.getText().toString().trim())){
//                    ToastUtil.showShort("请输入手机号码");
//                }else {
//                    DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "确认提交您修改的账户信息吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                        @Override
//                        public void onDismiss(DialogFragment fragment) {
//
//                        }
//                        @Override
//                        public void onClick(DialogFragment fragment, View v) {
//                            doConfirm();
//                        }
//                    });
//                }
//                break;
        }
    }
    /**
     * 加载账户信息
     */
    private void initData() {
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("OPTION", "17"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.OKHttpgetAccountInfo(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        org.json.JSONObject obj = new org.json.JSONObject(response);
                        if (1 == obj.optInt("code")) {
                            mZFBName.setText(obj.getJSONObject("result").optString("ALI_PAY_NAME"));
                            mZFBNUMS.setText(obj.getJSONObject("result").optString("ALI_PAY_ACCOUNT"));
                            mBankName.setText(obj.getJSONObject("result").optString("ACCOUNT_BANK"));
                            mBankNUMs.setText(obj.getJSONObject("result").optString("UNIONPAY_ACCOUNT"));
                            mBankAccName.setText(obj.getJSONObject("result").optString("UNIONPAY_NAME"));
                            mPhone.setText(obj.getJSONObject("result").optString("TELPHONE"));
                        } else {
                            ToastUtil.showShort(obj.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        }, this);
    }

    /**
     * 提交账户修改
     */
    private void doConfirm() {
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("OPTION", "16"));
        valuePairs.add(new BasicNameValuePair("CUSTID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("ALI_PAY_NAME", mZFBName.getText().toString().trim()));
        valuePairs.add(new BasicNameValuePair("ALI_PAY_ACCOUNT", mZFBNUMS.getText().toString().trim()));
        valuePairs.add(new BasicNameValuePair("ACCOUNT_BANK", mBankName.getText().toString().trim()));
        valuePairs.add(new BasicNameValuePair("UNIONPAY_ACCOUNT", mBankNUMs.getText().toString().trim()));
        valuePairs.add(new BasicNameValuePair("UNIONPAY_NAME", mBankAccName.getText().toString().trim()));
        valuePairs.add(new BasicNameValuePair("TELPHONE", mPhone.getText().toString().trim()));
        HttpRestClient.OKHttpgetAccountInfo(valuePairs, new MyResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        org.json.JSONObject obj = new org.json.JSONObject(response);
                        if (1 == obj.optInt("code")) {
                            ToastUtil.showShort(obj.optString("message"));
                            initData();
                        } else {
                            ToastUtil.showShort(obj.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        }, this);
    }
}

