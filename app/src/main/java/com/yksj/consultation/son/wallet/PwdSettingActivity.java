package com.yksj.consultation.son.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.ToastUtil;


/**
 * 支付密码设置
 *
 * @author Administrator
 */
public class PwdSettingActivity extends BaseFragmentActivity implements OnClickListener {

    private boolean isPayPwd;//是否设置过支付密码
    private EditText mRePwd;
    private EditText mPwd;
    private String isBDPhoneNum;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.pwd_setting);
        initWidget();
        initDate();
    }

    private void initDate() {
        // TODO Auto-generated method stub
        isPayPwd = getIntent().getExtras().getBoolean("isPayPwd");
        isBDPhoneNum = getIntent().getExtras().getString("isBDPhoneNum");
        if (isPayPwd) {
            findViewById(R.id.input).setVisibility(View.GONE);
            titleRightBtn2.setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.change).setVisibility(View.GONE);
            titleRightBtn2.setVisibility(View.VISIBLE);

        }
    }

    private void initWidget() {
        // TODO Auto-generated method stub
        initTitle();
        mRePwd = (EditText) findViewById(R.id.re_pwd);
        mPwd = (EditText) findViewById(R.id.pwd);
        titleTextV.setText("支付密码设置");
        titleRightBtn2.setText("确定");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right2:
                if (mPwd.getText().toString().trim().length() == 6 && mRePwd.getText().toString().trim().length() == 6) {
                    if (!mPwd.getText().toString().equals(mRePwd.getText().toString())) {
                        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "两次密码输入不一致");
                        return;
                    }
                    if (HStringUtil.isEmpty(isBDPhoneNum)) {
                        return;
                    }
                    RequestParams params = new RequestParams();
                    params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
                    params.put("TYPE", "SETPASS");
                    params.put("TELEPHONE", isBDPhoneNum);
                    params.put("PASS", MD5Utils.getMD5(mPwd.getText().toString().trim()));
//                    params.put("RE_PAYMENT_PASS", MD5Utils.getMD5(mRePwd.getText().toString().trim()));
                    HttpRestClient.doHttpWalletSetting(params,
                            new JsonsfHttpResponseHandler(this) {
                                @Override
                                public void onSuccess(int statusCode, JSONObject response) {
                                    if (response.containsKey("code")) {
                                        if (response.getIntValue("code") == 0) {
                                            ToastUtil.showShort(getApplicationContext(), response.getString("message"));
                                            setResult(RESULT_OK);
                                            finish();
                                        } else {
                                            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), response.getString("message"));
                                        }
                                    }
                                    super.onSuccess(statusCode, response);
                                }
                            });
                } else {
                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "密码必须为6位");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 修改密码
     *
     * @param v
     */
    public void onClickChangePwd(View v) {
        Intent intent = new Intent(getApplicationContext(), PwdChangeActivity.class);
        startActivity(intent);
    }

    /**
     * 忘记支付密码
     *
     * @param v
     */
    public void onClickforgetPassword(View v) {
        Intent intent = new Intent(getApplicationContext(), PwdForgetActivity.class);
        intent.putExtra("isBDPhoneNum", isBDPhoneNum);
        startActivity(intent);
    }

}
