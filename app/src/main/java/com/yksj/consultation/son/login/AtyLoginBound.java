package com.yksj.consultation.son.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.consultation.son.wallet.FindWithdrawPassword;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;
import com.yksj.healthtalk.utils.WeakHandler;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

//import cn.sharesdk.framework.utils.UIHandler;

public class AtyLoginBound extends BaseFragmentActivity implements View.OnClickListener {

    private EditText mPhone;
    private String phoneStr;
    private EditText mPwd;
    private Button btnLogin;
    Bundle bundle;
    String passwordMD5;
    HTalkApplication mApplication;
    private LodingFragmentDialog mDialog;
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
        setContentView(R.layout.aty_login_bound);
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
        initTitle();
        titleLeftBtn.setImageResource(R.drawable.icon_cancel_delete);
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("绑定");
        bundle=getIntent().getExtras();
        ViewFinder finder = new ViewFinder(this);
        finder.onClick(this, new int[]{R.id.login});
        findViewById(R.id.forget_pswd).setOnClickListener(this);
        btnLogin = finder.find(R.id.login);
        mPhone = finder.find(R.id.phone);
        mPwd = finder.find(R.id.pswd);
        mApplication = HTalkApplication.getApplication();
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.login:
                login();
                break;
            case R.id.forget_pswd:
                intent = new Intent(AtyLoginBound.this, FindWithdrawPassword.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {
        if (!mApplication.isNetWork()) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "网络不可用");
            return;
        }
        final String name = mPhone.getEditableText().toString();
        final String password = mPwd.getEditableText().toString();
        if (name.length() == 0) {
            mPhone.setError("手机号不能为空  ");
            return;
        }
        if (password.length() == 0) {
            mPwd.setError("密码不能为空");
            return;
        }
//        if (bundle!=null){
//            EventBus.getDefault().post(new String[]{name, password, bundle.getString("PLATFORM_NAME"), bundle.getString("USERID")});
//        }
        JSONObject object = new JSONObject();
        if (bundle!=null){
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
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("TYPE", "relatedOtherAccount"));
        params.add(new BasicNameValuePair("PHONENUM", name));
        params.add(new BasicNameValuePair("PASSWORD", password));
        params.add(new BasicNameValuePair("TERMINAL", "0"));
        params.add(new BasicNameValuePair("PARAM", object.toString()));
        HttpRestClient.OKHttpConPhone(params, new MyResultCallback<String>(this) {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if ("1".equals(obj.optString("code"))) {
                            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), obj.optString("result"), new SingleBtnFragmentDialog.OnClickSureBtnListener() {
                                @Override
                                public void onClickSureHander() {
                                    mDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
                                    mHandler.sendEmptyMessageDelayed(1, SmartControlClient.CONNECTION_TIMEOUT);
                                    SharePreUtils.saveUserLoginCache(name, password, true);
                                    if (password.length() <= 16)
                                        passwordMD5 = MD5Utils.getMD5(password);
                                    SmartControlClient.getControlClient().setUserPassword(name, passwordMD5);
                                    EventBus.getDefault().post(new String[]{name, passwordMD5, "", "", "0"});
                                    btnLogin.setClickable(false);
                                }
                            });
                        } else {
                            btnLogin.setClickable(true);
                            SingleBtnFragmentDialog.show(getSupportFragmentManager(), "六一健康", obj.optString("message"), "知道了");
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
     * 会在子线程中做耗时操作进行登陆
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
        mDialog.dismissAllowingStateLoss();
        if (log.code == 1) {//登录成功
            ToastUtil.showShort("登录成功");
            Intent intent = new Intent(AtyLoginBound.this, PatientHomeActivity.class);
            startActivity(intent);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismissAllowingStateLoss();
                mDialog = null;
            }
            SharePreUtils.updateLoginState(true);
            EventBus.getDefault().post(new MyEvent("loginSuccess", 12));
            finish();
        } else if (log.code == 0) {//登录失败
            btnLogin.setClickable(true);
            ToastUtil.showShort(log.what);
        } else if (log.code == 1111) {//第三方登录失败
            btnLogin.setClickable(true);
            ToastUtil.showShort(log.what);
        }
    }
}

