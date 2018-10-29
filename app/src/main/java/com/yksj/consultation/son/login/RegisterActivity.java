package com.yksj.consultation.son.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.dmsj.newask.http.LodingFragmentDialog;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.consultation.son.setting.SettingWebUIActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.StringFormatUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WeakHandler;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 新六一健康注册界面
 * Created by lmk on 2015/9/15.
 */
public class RegisterActivity extends BaseFragmentActivity implements View.OnClickListener {

    private EditText editPhone, editPsw, editVerifyCode;
    private Button btnVerifyCode, btnComplete;
    private CheckBox checkBox;
    private TextView tvProtocol;
    private Runnable runnable;
    Bundle bundle;
    private boolean Sendcode = false;//验证码是否发送
    Handler handler = new Handler();
    private LodingFragmentDialog mDialog;
    String registerType = "";
    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1://登录超时
                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "登录超时,请稍后重试!", new SingleBtnFragmentDialog.OnClickSureBtnListener() {
                        @Override
                        public void onClickSureHander() {
                            LoginServiceManeger.instance().loginOut();//防止 时间到了,正好登上了,那就退出
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismissAllowingStateLoss();
                            }
                        }
                    });
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_register);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        bundle = getIntent().getExtras();
        if (getIntent().hasExtra("registerType")) {
            registerType = getIntent().getStringExtra("registerType");
        }
        initTitle();
        titleTextV.setText(R.string.register);
        titleLeftBtn.setOnClickListener(this);
        editPhone = (EditText) findViewById(R.id.register_input_phone);
        editPsw = (EditText) findViewById(R.id.register_input_psw);
        editVerifyCode = (EditText) findViewById(R.id.register_input_verifycode);
        btnComplete = (Button) findViewById(R.id.register_btn_complete);
        btnVerifyCode = (Button) findViewById(R.id.register_btn_verifycode);
        checkBox = (CheckBox) findViewById(R.id.register_checkbox);
        btnComplete.setOnClickListener(this);
        btnVerifyCode.setOnClickListener(this);
        findViewById(R.id.register_ptotocol).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        closeKeyboard();
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.register_btn_complete:
                registe();
                break;
            case R.id.register_btn_verifycode:
                if (Sendcode)
                    return;
                if (!SystemUtils.isNetWorkValid(this)) {
                    ToastUtil.showShort(this, R.string.getway_error_note);
                    return;
                }
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showShort(RegisterActivity.this, "请输入手机号码");
                    editPhone.requestFocus();
                    return;
                }
                if (!StringFormatUtils.isPhoneNum(phone)) {
                    ToastUtil.showShort("请输入正确的手机号");
                    return;
                }
                ToastUtil.showShort(RegisterActivity.this, "您的手机号是" + phone);
                getVerifyCode(phone);

                break;
            case R.id.register_ptotocol:
                Intent intent = new Intent(this, SettingWebUIActivity.class);
                intent.putExtra("url", HTalkApplication.getApplication().getDoctorUserAgentPath());
                intent.putExtra("title", "用户协议与隐私条款");
                startActivity(intent);
                break;
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //注册
    private void registe() {
        final String phone = editPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort(RegisterActivity.this, "请输入手机号码");
            editPhone.requestFocus();
            return;
        }
        final String psw = editPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.showShort(RegisterActivity.this, "请输入密码");
            editPhone.requestFocus();
            return;
        }
        if (psw.length() < 6) {
            ToastUtil.showShort(RegisterActivity.this, "密码最少输入6位");
            editPhone.requestFocus();
            return;
        }
        if (psw.length() > 12) {
            ToastUtil.showShort(RegisterActivity.this, "密码最多只能输入12位");
            editPhone.requestFocus();
            return;
        }
        String code = editVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShort(RegisterActivity.this, "请输入验证码");
            editPhone.requestFocus();
            return;
        }
        if (!checkBox.isChecked()) {//未同意隐私条款
            ToastUtil.showShort(RegisterActivity.this, "您必须同意隐私用户协议");
            editPhone.requestFocus();
            return;
        }
        org.json.JSONObject object = new org.json.JSONObject();
        if (bundle != null) {
            try {
                object.put("PLATFORM_NAME", bundle.getString("PLATFORM_NAME"));
                object.put("EXPIRESIN", String.valueOf(bundle.getLong("EXPIRESIN")));
                object.put("EXPIRESTIME", String.valueOf(bundle.getLong("EXPIRESTIME")));
                object.put("TOKEN", bundle.getString("TOKEN"));
                object.put("TOKENSECRET", bundle.getString("TOKENSECRET"));
                object.put("USERGENDER", bundle.getString("USERGENDER"));
                object.put("USERICON", bundle.getString("USERICON"));
                object.put("USERNAME", bundle.getString("USERNAME"));
                object.put("USERID", bundle.getString("USERID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //DuoMeiHealth/ConsultationInfoSet?TYPE=registePatient&PHONENUM=&VERIFICATION_CODE=&PASSWORD=
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE", "registePatient"));
        pairs.add(new BasicNameValuePair("PHONENUM", phone));
        pairs.add(new BasicNameValuePair("VERIFICATION_CODE", code));
        pairs.add(new BasicNameValuePair("PASSWORD", psw));
        if (!"".equals(registerType)) {
            pairs.add(new BasicNameValuePair("FLAG", getIntent().getStringExtra("registerType")));
        }
        pairs.add(new BasicNameValuePair("PARAM", object.toString()));
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
        HttpRestClient.doGetConsultationInfoSet(pairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
                ToastUtil.showShort(RegisterActivity.this, bb.message);
                if ("1".equals(bb.code)) {
                    SharePreUtils.saveUserLoginCache(phone, psw, true);
                    EventBus.getDefault().post(new String[]{phone, psw, "", "", "0"});
                    EventBus.getDefault().post(new MyEvent("", 14));
                    finish();
                } else if (!"1".equals(bb.code)) {
                    ToastUtil.showShort(bb.message);
                }
            }
        }, this);
    }

    private void getVerifyCode(String phone) {
        //192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet?TYPE=sendPatientResgistCode&PHONENUM=
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE", "sendPatientResgistCode"));
        pairs.add(new BasicNameValuePair("PHONENUM", phone));
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
        HttpRestClient.doGetConsultationInfoSet(pairs, new MyResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort(RegisterActivity.this, "验证码发送失败");
            }

            @Override
            public void onResponse(String response) {
                BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
                if (bb != null && "1".equals(bb.code)) {
                    Sendcode = true;
                    timerTaskC();
                }
                ToastUtil.showShort(RegisterActivity.this, bb.message);
            }
        }, this);

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
                    btnVerifyCode.setText("发送验证码");
                    btnVerifyCode.setEnabled(true);
                    Sendcode = false;
                    return;
                } else {
                    --i;
                    handler.postDelayed(this, 1000);
                    btnVerifyCode.setText(i + "");
                    btnVerifyCode.setEnabled(false);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * 会在子线程中做耗时操作进行登录
     *
     * @param str 数组 账号密码
     */
    public void onEventBackgroundThread(String[] str) {
        LoginServiceManeger.instance().login(str[0], str[1], str[2], str[3], str[4]);
    }

    /**
     * 登录之后,会调用此方法
     *
     * @param log
     */
    public void onEventMainThread(MyEvent log) {
        mHandler.removeMessages(1);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismissAllowingStateLoss();
            mDialog = null;
        }
        if (log.code == 1) {//登录成功
            ToastUtil.showShort("登录成功");
            Intent intent = new Intent(RegisterActivity.this, PatientHomeActivity.class);
            intent.putExtra("isFromLogin", true);
            startActivity(intent);
            EventBus.getDefault().post(new MyEvent("loginSuccess", 12));
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismissAllowingStateLoss();
                mDialog = null;
            }
            SharePreUtils.updateLoginState(true);
            RegisterActivity.this.finish();
        } else if (log.code == 0) {//登录失败
            ToastUtil.showShort(log.what);
        } else if (log.code == 13) {//注册登录失败后
            if (mDialog != null) {
                mDialog.dismissAllowingStateLoss();
            }
        }
    }
}
