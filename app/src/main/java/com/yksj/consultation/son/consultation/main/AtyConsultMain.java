package com.yksj.consultation.son.consultation.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.dmsj.newask.Activity.WelcomeActivity;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAty;
import com.yksj.consultation.son.consultation.PConsultMainActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatProfile;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.common.NimUIKit;
import com.yksj.consultation.son.consultation.avchat.string.MD5;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.news.AtyNewsCenter;
import com.yksj.consultation.son.consultation.news.SixOneActivity;
import com.yksj.consultation.son.doctor.ExpertMainUI;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.bean.AlertEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpUrls;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SocketManager;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.AlertDialogUtils;
import com.yksj.healthtalk.utils.DialogUtils;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import de.greenrobot.event.EventBus;


/**
 * Created by HEKL on 2015/9/14.
 * Used for 新六一健康患者端主页__
 */
public class AtyConsultMain extends BaseFragmentActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private PullToRefreshScrollView mPullToRefreshScrollView;//整体滑动布局
    private String officeName = "";//输入框里的内容

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_mainlayout);
//        if (SharePreUtils.getLoginState()) {
//            loginAvChat();
//        }
//        test();
        initView();
    }

    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!LoginServiceManeger.instance().isVisitor) {//不是游客
            if (TextUtils.isEmpty(LoginServiceManeger.instance().getLoginEntity().getPoneNumber().trim())) {
                DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "六一健康", "绑定手机号码,有助于您找回密码,现在就去绑定吗?", "去绑定", "退出", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                        Intent intent = new Intent(AtyConsultMain.this, SettingPhoneBound.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        CoreService.actionLogout(getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                        startActivity(intent);
//                      AtyConsultMain.this.finish();
                    }
                });
            }
            EventBus.getDefault().post(new MyEvent("mainrefresh", 2));
        }
//        if (!LoginServiceManeger.instance().isVisitor) {//不是游客
//            EventBus.getDefault().post(new MyEvent("mainrefresh", 2));
//        }
        loginAvChat();
    }

    /**
     * View的初始化
     */
    private void initView() {
        initTitle();
        titleLeftBtn.setVisibility(View.INVISIBLE);
        personBtn.setVisibility(View.VISIBLE);

        titleTextV.setText(R.string.app_name);
        titleTextV.setVisibility(View.GONE);
        title_change.setVisibility(View.GONE);

        titleRightBtn2.setText("专家");
        // titleRightBtn2.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(this);
        mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);//滑动
        mPullToRefreshScrollView.setOnRefreshListener(this);
        findViewById(R.id.imge_consult).setOnClickListener(this);
        findViewById(R.id.imge_smallone).setOnClickListener(this);
        findViewById(R.id.btn_news).setOnClickListener(this);
        findViewById(R.id.btn_donate).setOnClickListener(this);
        findViewById(R.id.btn_know).setOnClickListener(this);
        personBtn.setOnClickListener(this);

        frameLayout_search.setVisibility(View.VISIBLE);
        editSearch_top.setVisibility(View.VISIBLE);
        editSearch_top.setHint(getString(R.string.main_title_search_hint));
        editSearch_top.setOnClickListener(this);
//        editSearch_top.addTextChangedListener(new TextWatcher() {//搜索框字数变化监听
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                officeName = editSearch_top.getText().toString().trim();
//                initseatch(editSearch_top);
//            }
//        });
    }
//    private void initseatch(final EditText Search){
//        Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    officeName = Search.getText().toString().trim();
////                    if (officeName != null) {
//                    if (!"".equals(officeName)) {
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(Search.getWindowToken(), 0);//关闭软键盘
//                       // initData();
//                    } else {
//                        // initData();
//                        ToastUtil.showShort("输入科室名称");
//                    }
//                    handled = true;
//                }
//                return handled;
//            }
//        });

//    }

    private void choiseService() {
        if (SocketManager.init().isConnected()) {
            SocketManager.init().disConnect();
        }
        List<AlertEntity> lists = new ArrayList<>();
        lists.add(new AlertEntity("本地服务器", "1"));
        lists.add(new AlertEntity("204服务器", "2"));
        lists.add(new AlertEntity("正式服务器", "3"));
        AlertDialogUtils.show(this, getLayoutInflater(), lists, new AlertDialogUtils.OnClickListener() {
            @Override
            public void onClick(View v, AlertEntity entity) {
                int service = Integer.valueOf(entity.code);
                HttpUrls httpUrls;
                switch (service) {
                    case 1:
//                        SocketManager.IP = "192.168.1.123";
//                        httpUrls = new HttpUrls("http://192.168.1.123:8080");
                        SocketManager.IP = "192.168.1.128";
                        httpUrls = new HttpUrls("http://192.168.1.128:8080");
                        HttpRestClient.setmHttpUrls(httpUrls);
                        break;
                    case 2:
                        SocketManager.IP = "220.194.46.204";
                        httpUrls = new HttpUrls("http://220.194.46.204:80");
                        HttpRestClient.setmHttpUrls(httpUrls);
                        break;
                    case 3:
                        SocketManager.IP = "ad.61120.net";
                        httpUrls = new HttpUrls("http://ad.61120.net");
                        HttpRestClient.setmHttpUrls(httpUrls);
                        break;
                }
            }
        }, "选择服务器,此功能开发者使用");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
//            case R.id.main_listmenuP://右上角二维码
//                if (!LoginServiceManeger.instance().isVisitor) {
//                    intent = new Intent(AtyConsultMain.this, FriendSearchAboutZxingActivity.class);
//                    intent.putExtra("type", "");// 0-社交场 1-医生馆 2 商户
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(AtyConsultMain.this, UserLoginActivity.class);
//                    startActivity(intent);
//                }
//                break;
            case R.id.imge_consult://发起会诊  找专家
//                intent = new Intent(this, HumanBodyActivity.class);
//                intent.putExtra("sex", 2);
//                intent.putExtra("code", "腿");
                if (!LoginServiceManeger.instance().isVisitor) {
                    intent = new Intent(AtyConsultMain.this, PConsultMainActivity.class);
//                    intent = new Intent(this, MyDoctorMainUI.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AtyConsultMain.this, UserLoginActivity.class);
////                intent = new Intent(AtyConsultMain.this, RecordMadeAty.class);
                    startActivity(intent);
                }

                break;
            case R.id.imge_smallone://小壹咨询
//              choiseService();
//                intent = new Intent(AtyConsultMain.this, WelcomeActivityB.class);

                WelcomeActivity.MERCHANT_ID = Configs.MERCHANT_ID;
                WelcomeActivity.site_id = Configs.site_id;
                WelcomeActivity.themeId = Configs.theme_Id;
                intent = new Intent(AtyConsultMain.this, com.dmsj.newask.Activity.ChatActivity.class);


                startActivity(intent);
//                intent = new Intent(this, CommonwealAidAty.class);
//                intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().HTML+"5/robot.html"+ "?customer_id="+LoginServiceManeger.instance().getLoginUserId());
//                intent.putExtra(CommonwealAidAty.TITLE,"六一博士(问诊)");
//                startActivity(intent);
                break;
            case R.id.title_right2://发起会诊
                if (!LoginServiceManeger.instance().isVisitor) {
                    intent = new Intent(AtyConsultMain.this, ExpertMainUI.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AtyConsultMain.this, UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_person://个人中心
                if (!LoginServiceManeger.instance().isVisitor) {
                    mIntent = new Intent(AtyConsultMain.this, AtyPersonCenter.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(AtyConsultMain.this, UserLoginActivity.class);
                    startActivity(mIntent);
                }
                break;
            case R.id.btn_news://新闻中心
//              intent = new Intent(AtyConsultMain.this, NewsCenterAty.class);
                intent = new Intent(AtyConsultMain.this, AtyNewsCenter.class);
                intent.putExtra(AtyNewsCenter.TYPE, "News");
                startActivity(intent);
                break;
            case R.id.btn_donate:
//              intent = new Intent(AtyConsultMain.this, CommonwealAidAty.class);//公益捐助
                intent = new Intent(AtyConsultMain.this, CommonwealAty.class);//公益活动
                startActivity(intent);
                break;
            case R.id.btn_know://六一百科//健康百科

//               intent = new Intent(AtyConsultMain.this,TopUpActivity.class);
//               intent.putExtra("money", "10");
//               startActivity(intent);

                intent = new Intent(AtyConsultMain.this, SixOneActivity.class);
                intent.putExtra(AtyNewsCenter.TYPE, "Encyclopedia");
                startActivity(intent);
                break;
            case R.id.edit_search_top://首页搜索界面跳转的界面
                if (!LoginServiceManeger.instance().isVisitor) {
                    intent = new Intent(this, HomePageActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(AtyConsultMain.this, UserLoginActivity.class);
                    startActivity(intent);
                }

        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!LoginServiceManeger.instance().isVisitor) {//不是游客
            EventBus.getDefault().post(new MyEvent("mainrefresh", 2));
        }
        mPullToRefreshScrollView.onRefreshComplete();
    }

    /**
     * back提示
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确定要退出吗?", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                @Override
                public void onDismiss(DialogFragment fragment) {

                }

                @Override
                public void onClick(DialogFragment fragment, View v) {
                    finish();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
    // 在这里直接使用同步到云信服务器的帐号和token登录。
    // 这里为了简便起见，demo就直接使用了密码的md5作为token。
    // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
    final String account = "15210270585";
    final String token = tokenFromPassword("123456");


    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(HTalkApplication.getApplication());
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }


    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AbortableFuture<LoginInfo> loginRequest;

    private void loginAvChat() {


        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
//        String pass = "123456";
//        final String account = "15210270585";
//        final String token = MD5.getStringMD5(pass);

        if (LoginServiceManeger.instance().getLoginEntity() != null) {
            final String token = LoginServiceManeger.instance().getLoginEntity().getAvToken();
            final String account = LoginServiceManeger.instance().getLoginEntity().getSixOneAccoutn();

            // 登录
//            loginRequest = NimUIKit.doLogin(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            loginRequest = NimUIKit.doLogin(new LoginInfo(account, token, getResources().getString(R.string.avchat_appkey)), new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo param) {
                    System.out.println("login success");
                    DemoCache.setAccount(account);
//                saveLoginInfo(account, token);
                    // 初始化消息提醒配置
//                initNotificationConfig();

                    // 进入主界面
//                startActivity(new Intent(this,LoginA));
//                MainActivity.start(LoginActivity.this, null);
//                finish();
                    if (SharePreUtils.getisAvChatState()) {
                        checkCall();
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 302 || code == 404) {
//                        DialogUtils.showLoginOutDialog2(HTalkApplication.getApplication(), HTalkApplication.getApplication().getResources().getString(R.string.login_failed));
                    } else {
//                        DialogUtils.showLoginOutDialog2(HTalkApplication.getApplication(), HTalkApplication.getApplication().getResources().getString(R.string.login_failed));
                    }

                }

                @Override
                public void onException(Throwable exception) {
                    Toast.makeText(HTalkApplication.getApplication(), R.string.login_exception, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 是否有视频通话
     */
    private void checkCall() {
        if (HTalkApplication.getApplication().isNetWork()) {
            startAudioVideoCall();
        } else {
            ToastUtil.showShort(getResources().getString(R.string.getway_error_note));
        }
    }

    /************************ 音视频通话 ***********************/

    public void startAudioVideoCall() {
//        AVChatActivity.launch(this, "15210270585", AVChatType.VIDEO.getValue(), AVChatActivity.FROM_BROADCASTRECEIVER);
        if (LoginServiceManeger.instance().getLoginEntity() != null && LoginServiceManeger.instance().getLoginEntity().getAvData() != null)
            AVChatProfile.getInstance().launchActivity(LoginServiceManeger.instance().getLoginEntity().getAvData(), AVChatActivity.FROM_BROADCASTRECEIVER);
        SharePreUtils.updateAvChateState(true);
    }

    private void test() {

//    String path = "https://www.61120.cn";
        String path = "https://61.jiansion.com";

        try {
            //获取证书
//        InputStream stream = getAssets().open("www61120cn.cer");
            InputStream stream = getAssets().open("61jiansion.keystore");

            SSLContext tls = SSLContext.getInstance("TLS");
            //使用默认证书
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            //去掉系统默认证书
            keystore.load(null);
            Certificate certificate =
                    CertificateFactory.getInstance("X.509").generateCertificate(stream);
            //设置自己的证书
            keystore.setCertificateEntry("61jiansion", certificate);
            //通过信任管理器获取一个默认的算法
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            //算法工厂创建
            TrustManagerFactory instance = TrustManagerFactory.getInstance(algorithm);
            instance.init(keystore);
            tls.init(null, instance.getTrustManagers(), null);
            SSLSocketFactory socketFactory = tls.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

            URL url = new URL(path);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //设置ip授权认证：如果已经安装该证书，可以不设置，否则需要设置
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            InputStream inputStream = conn.getInputStream();
            String result = inputStream.toString();
            OkHttpClientManager.getInstance().setCertificates(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
