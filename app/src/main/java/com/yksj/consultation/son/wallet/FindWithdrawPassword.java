package com.yksj.consultation.son.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.setting.SettingPassWordUI;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.utils.StringFormatUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 找回提现密码
 * Created by lmk on 15/9/18.
 */
public class FindWithdrawPassword extends BaseFragmentActivity implements View.OnClickListener{

    private int type=0;//0表示找回提现密码   1表示登录时忘记密码
    private String verify="",phone;
    private EditText editPhone,editVerify;
    private boolean isSend=false;
    private Runnable runnable;
    private Button btnCode;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_find_withdraw_psw);
        type=getIntent().getIntExtra("type",0);
        initView();

    }

    private void initView() {
        initTitle();
        if (type==0)
//            titleTextV.setText("找回提现密码");
            titleTextV.setText("找回密码");
        else
            titleTextV.setText("找回密码");
        titleLeftBtn.setOnClickListener(this);
        editPhone= (EditText) findViewById(R.id.find_paswd_phone);
        editVerify= (EditText) findViewById(R.id.find_paswd_input_verifycode);
        btnCode= (Button) findViewById(R.id.find_paswd_btn_verifycode);
        btnCode.setOnClickListener(this);
        findViewById(R.id.find_paswd_btn_verifycode).setOnClickListener(this);
        findViewById(R.id.find_paswd_next).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.find_paswd_next:
                verify=editVerify.getText().toString().trim();
                if (!isSend) {
                    ToastUtil.showShort(this,"您还未获取验证码");
                    return;
                }
                if (TextUtils.isEmpty(verify)){
                    ToastUtil.showShort(this,"请输入验证码");
                    return;
                }
                Intent intent=new Intent(FindWithdrawPassword.this,SettingPassWordUI.class);
                intent.putExtra("CODE",verify);
                intent.putExtra("PHONE",phone);
                startActivity(intent);
                break;
            case R.id.find_paswd_btn_verifycode:
                phone=editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)){
                    ToastUtil.showShort(this,"请输入手机号码");
                    return;
                }
                if(!StringFormatUtils.isPhoneNum(phone)){
                    ToastUtil.showShort("请输入正确的手机号");
                    return;
                }
                getVerifyCode(phone);
                break;
        }
    }

    private void getVerifyCode(String phone){
        //192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet?TYPE=sendFindUpdatePasswordCode&PHONENUM=
        List<BasicNameValuePair> pairs=new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE","sendFindUpdatePasswordCode"));
        pairs.add(new BasicNameValuePair("PHONENUM", phone));
        pairs.add(new BasicNameValuePair("FLAG", "1"));//
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
        HttpRestClient.doGetConsultationInfoSet(pairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort(FindWithdrawPassword.this, "验证码发送失败");
            }

            @Override
            public void onResponse(String response) {
                if (response==null||response.length()==0)
                    return;
                try {
                    JSONObject object=new JSONObject(response);
                    if ("1".equals(object.optString("code"))){
                        isSend=true;
                        timerTaskC();
                    }
                    ToastUtil.showShort(FindWithdrawPassword.this,object.optString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);

    }
    /**
     * 设置六十秒
     */
    private void timerTaskC() {
        runnable = new Runnable() {
            int i = 60;
            @Override
            public void run() {
                if (i == 0) {
                    btnCode.setText("发送验证码");
                    btnCode.setEnabled(true);
                    isSend=false;
                    return;
                } else {
                    --i;
                    handler.postDelayed(this, 1000);
                    btnCode.setText(i + "");
                    btnCode.setEnabled(false);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}