package com.yksj.consultation.son.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppManager;
import com.yksj.consultation.son.app.AppUpdateManager;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.app.SettingManager;
import com.yksj.consultation.son.consultation.avchat.annotation.MPermission;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionDenied;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionGranted;
import com.yksj.consultation.son.consultation.avchat.annotation.OnMPermissionNeverAskAgain;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.common.NimUIKit;
import com.yksj.consultation.son.consultation.avchat.team.Container;
import com.yksj.consultation.son.consultation.avchat.team.ModuleProxy;
import com.yksj.consultation.son.consultation.avchat.team.TeamAVChatAction;
import com.yksj.consultation.son.consultation.avchat.utils.MD5;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.consultation.son.wallet.FindWithdrawPassword;
import com.yksj.healthtalk.db.ChatUserHelper;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.share.utils.LoginApi;
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

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;

/**
 * 登录界面
 */
public class UserLoginActivity extends BaseFragmentActivity implements OnClickListener, ModuleProxy {

    private EditText mPhone;
    private String phoneStr;
    private EditText mPwd;
    private TextView tvForgetPsw;
    String platForm;
    private HTalkApplication mApplication;
    private static final int MSG_USERID_FOUND = 3;
    private static final int MSG_LOGIN = 4;
    private static final int MSG_AUTH_CANCEL = 5;
    private static final int MSG_AUTH_ERROR = 6;
    private static final int MSG_AUTH_COMPLETE = 7;

    WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1://登陆超时
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
                case MSG_USERID_FOUND:
//                    Toast.makeText(UserLoginActivity.this, "用户信息已存在，正在跳转登录操作…", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LOGIN:

                    break;
            }
            return false;
        }
    });

    private LodingFragmentDialog mDialog;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.user_login_layout);
//        requestBasicPermission();
        ShareSDK.initSDK(this);
        if (getIntent().hasExtra("phone"))
            phoneStr = getIntent().getStringExtra("phone");
        initView();

        CoreService.actionStart(this);
        AppManager.getInstance().finishAllActivity();
        SettingManager.destory();
        ChatUserHelper.close();
        new AppUpdateManager(this, false).checkeUpdate();
        mApplication = HTalkApplication.getApplication();
        findViewById(R.id.rl_weixin).setOnClickListener(this);
        findViewById(R.id.rl_qq).setOnClickListener(this);
        findViewById(R.id.rl_sina).setOnClickListener(this);
    }


    private void initView() {
        initTitle();
        titleLeftBtn.setImageResource(R.drawable.icon_cancel_delete);
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("登录");
        ViewFinder finder = new ViewFinder(this);
        finder.onClick(this, new int[]{R.id.login, R.id.registe});
        findViewById(R.id.forget_pswd).setOnClickListener(this);
        mPhone = finder.find(R.id.phone);
        mPwd = finder.find(R.id.pswd);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.login:
//                ArrayList<String> list =new ArrayList<>();
//                list.add("15210270585");
//                list.add("wshwwzbh");
//                onCreateRoomSuccess(list);
                login();
                break;
            case R.id.registe:
//                loginAvChat();
                intent = new Intent(UserLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_pswd:
                intent = new Intent(UserLoginActivity.this, FindWithdrawPassword.class);
                startActivity(intent);
                break;
            case R.id.rl_weixin:
                platForm = Wechat.NAME;
                login(Wechat.NAME);
                break;
            case R.id.rl_qq:
                platForm = QQ.NAME;
                login(QQ.NAME);
                break;
            case R.id.rl_sina:
                platForm = SinaWeibo.NAME;
                login(SinaWeibo.NAME);
                break;
        }
    }

    private void login() {
        if (!mApplication.isNetWork()) {
            SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "网络不可用");
            return;
        }
        String name = mPhone.getEditableText().toString();
        String password = mPwd.getEditableText().toString();
        if (name.length() == 0) {
            mPhone.setError("账号不能为空  ");
            return;
        }
        if (password.length() == 0) {
            mPwd.setError("密码不能为空");
            return;
        }
        mDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
        mHandler.sendEmptyMessageDelayed(1, SmartControlClient.CONNECTION_TIMEOUT);

        SharePreUtils.saveUserLoginCache(name, password, true);
        if (password.length() <= 16)
            password = MD5Utils.getMD5(password);
        EventBus.getDefault().post(new String[]{name, password, "", "", "0"});
    }


    /**
     * 会在子线程中做耗时操作进行登录
     *
     * @param str 数组 账号密码
     */
    public void onEventBackgroundThread(String[] str) {
        if (str.length == 3) {
//            judgeLogin(str[0], str[1], str[2]);
        } else if (str.length == 5) {
            LoginServiceManeger.instance().login(str[0], str[1], str[2], str[3], str[4]);
        }
    }


    /**
     * 登录之后,会调用此方法
     *
     * @param log
     */
    public void onEventMainThread(MyEvent log) {
//        Log.i("kkk", "onEventMainThread: "+log.code+"     "+log.what);
        mHandler.removeMessages(1);
        mDialog.dismissAllowingStateLoss();
        if (log.code == 1) {//登录成功
            SharePreUtils.updateLoginState(true);
            ToastUtil.showShort("登录成功");
            EventBus.getDefault().post(new MyEvent("mainrefresh", 2));
            Intent intent = new Intent(UserLoginActivity.this, PatientHomeActivity.class);
            intent.putExtra("isFromLogin", true);
//            intent.putExtras(((Intent) msg.obj).getExtras());
            startActivity(intent);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismissAllowingStateLoss();
                mDialog = null;
            }
            finish();
        } else if (log.code == 0) {//登录失败
            ToastUtil.showShort(log.what);
        } else if (log.code == 12) {//绑定登录成功后
            finish();
        } else if (log.code == 13) {//绑定登录成功后
            if (mDialog != null) {
                mDialog.dismissAllowingStateLoss();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        String[] cache = SharePreUtils.fatchUserLoginCache();
        if (phoneStr != null && phoneStr.length() > 0) {
            mPhone.setText(phoneStr);
        } else {
            mPhone.setText(cache[0]);
            if (cache[0] != null && !"".equals(cache[0])) {
                mPhone.setSelection(cache[0].length());
            }
        }
        if (SharePreUtils.fatchisLoginCache()) {
            mPwd.setText(cache[1]);
            mPwd.setTag(cache[1]);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    /*
     *
	 * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
	 */
    private void login(final String platformName) {
        mDialog = LodingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
//        api.setOnLoginListener(new OnLoginListener() {
//            public boolean onLogin(String platform, HashMap<String, Object> res) {
////                ShareSDK.getPlatform(platformName).getDb().getUserId()
//                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
//                // 此处全部给回需要注册
//                return true;
//            }
//
//            public boolean onRegister() {
//                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
//                return true;
//            }
//        });
        api.login(this);
    }

    /**
     * 判断第三方登录是否可登录
     */
    private void judgeLogin(final String userId, final String platForm, final String terminal) {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("TYPE", "verifyLoginOtherAccount"));
        params.add(new BasicNameValuePair("USERID", userId));
        params.add(new BasicNameValuePair("PLATFORM_NAME", platForm));
        params.add(new BasicNameValuePair("TERMINAL", terminal));
        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
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
                            EventBus.getDefault().post(new String[]{"", "", userId, platForm, terminal});
                        } else {
                            AtyThirdLogin.setPlatform(platForm);
                            Intent intent = new Intent(UserLoginActivity.this, AtyThirdLogin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    /**
     * 基本权限管理
     */

    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
    };

    private void requestBasicPermission() {
        MPermission.with(UserLoginActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }
    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
//        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean sendMessage(IMMessage msg) {
        return false;
    }

    @Override
    public void onInputPanelExpand() {

    }

    @Override
    public void shouldCollapseInputPanel() {

    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {

    }


    private AbortableFuture<LoginInfo> loginRequest;
    private void loginAvChat() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        String pass = "123456";
        final String account = "15028760690";
//        final String account = "7101804";
        final String token = MD5.getStringMD5(pass);

//        if (LoginServiceManeger.instance().getLoginEntity() != null) {
//            final String token =MD5.getStringMD5(LoginServiceManager.instance().getLoginEntity().getAvToken()) ;
//            final String token = LoginServiceManager.instance().getLoginEntity().getAvToken();
//            final String account = LoginServiceManager.instance().getLoginEntity().getSixOneAccount();
            // 登录
//            loginRequest = NimUIKit.doLogin(new LoginInfo(account, token, getResources().getString(R.string.avchat_appkey)), new RequestCallback<LoginInfo>() {
            loginRequest = NimUIKit.doLogin(new LoginInfo(account, token, getResources().getString(R.string.avchat_appkey_test)), new RequestCallback<LoginInfo>() {
                //            loginRequest = NimUIKit.doLogin(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
//                LogUtil.i(TAG, "login success");
                    DemoCache.setAccount(account);
//                    checkCall(tarAccount, order_Id);
//                saveLoginInfo(account, token);
                    Toast.makeText(HTalkApplication.getApplication(), "1", Toast.LENGTH_SHORT).show();
                    // 初始化消息提醒配置
//                initNotificationConfig();

                    // 进入主界面
//                startActivity(new Intent(this,LoginA));
//                MainActivity.start(LoginActivity.this, null);
//                finish();
//                if (SharePreUtils.getisAvChatState()) {
//                    checkCall();
//                }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 302 || code == 404) {
//                        Toast.makeText(HTalkApplication.getApplication(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(HTalkApplication.getApplication(), "登录失败: " + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onException(Throwable exception) {
                    Toast.makeText(HTalkApplication.getApplication(), R.string.login_exception, Toast.LENGTH_LONG).show();
                }
            });
//        }

    }












    private void onCreateRoomSuccess(ArrayList<String> accounts) {
        // 定制加号点开后可以包含的操作， 默认已经有图片，视频等消息了
        final TeamAVChatAction avChatAction = new TeamAVChatAction(AVChatType.VIDEO);
        Container container = new Container(this, "137182216", SessionTypeEnum.ChatRoom, this);
        avChatAction.setContainer(container);
        avChatAction.onSelectedAccountsResult(accounts);
    }
}
