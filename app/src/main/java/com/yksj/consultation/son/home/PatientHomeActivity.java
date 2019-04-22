package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PatientHomeAdapter;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.DAtyConslutDynMes;
import com.yksj.consultation.son.consultation.PConsultMainActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatProfile;
import com.yksj.consultation.son.consultation.avchat.cache.DemoCache;
import com.yksj.consultation.son.consultation.avchat.common.NimUIKit;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.consultation.main.AtyPersonCenter;
import com.yksj.consultation.son.consultation.main.HomePageActivity;
import com.yksj.consultation.son.consultation.news.AtyNewsCenter;
import com.yksj.consultation.son.consultation.news.SixOneActivity;
import com.yksj.consultation.son.doctor.ExpertMainUI;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.message.MessageNotifyActivity;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.healthtalk.entity.DynamicMessageListEntity;
import com.yksj.healthtalk.entity.PatientHomeEntity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

//新首页 之前首页是AtyConsultMain
public class PatientHomeActivity extends FragmentActivity implements OnRecyclerClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private List<PatientHomeEntity.SowingListBean> imgUrl;
    private List<PatientHomeEntity.AllNewsBean> newsUrl;
    private List<PatientHomeEntity.AllNewsBean> newUrl;
    private PatientHomeAdapter patientHomeAdapter;
    private RecyclerView homeRecycler;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        initView();
        getData();
    }

    private void initView() {
        findViewById(R.id.editSearch).setOnClickListener(this);
        findViewById(R.id.homeMsg).setOnClickListener(this);
        findViewById(R.id.homeHuiZhen).setOnClickListener(this);
        findViewById(R.id.homeMain).setOnClickListener(this);
        findViewById(R.id.homeMyDoc).setOnClickListener(this);
        findViewById(R.id.homeBaiKe).setOnClickListener(this);
        findViewById(R.id.homeMy).setOnClickListener(this);
        findViewById(R.id.homeHuiZhen).setOnClickListener(this);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setProgressViewEndTarget(true,400);
        swipeRefresh.setColorSchemeColors(Color.parseColor("#37a6a2"));
        swipeRefresh.setRefreshing(true);
        newUrl=new ArrayList<>();
        imgUrl=new ArrayList<>();
        newsUrl=new ArrayList<>();
        homeRecycler = (RecyclerView) findViewById(R.id.homeRecycler);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<List> dataList = new ArrayList<>();
        dataList.add(imgUrl);
        dataList.add(newsUrl);
        patientHomeAdapter = new PatientHomeAdapter(this, dataList);
        patientHomeAdapter.setmOnRecyclerClickListener(this);

    }


    @Override
    public void onRecyclerItemClickListener(int position, View itemView, int type) {
        Intent intent = new Intent(PatientHomeActivity.this, DAtyConslutDynMes.class);
        intent.putExtra("conId", HTalkApplication.APP_CONSULTATION_CENTERID);
        intent.putExtra("infoId", newsUrl.get(position).getINFO_ID()+"");
        intent.putExtra("title", "热点新闻");
        startActivity(intent);
    }


    private void getData(){
        HttpRestClient.doGetPatientHome(null, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.onShow(PatientHomeActivity.this,"网络开小差啦",1000);
                swipeRefresh.setRefreshing(false);
                homeRecycler.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                PatientHomeEntity patientHomeEntity = new Gson().fromJson(response,PatientHomeEntity.class);
//                for (int i = 0; i <3; i++) {
//                    newUrl.add(patientHomeEntity.getAllNews().get(i));
//                }
//                newsUrl.addAll(newUrl);
                imgUrl.addAll(patientHomeEntity.getSowingList());
//                patientHomeAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                homeRecycler.setAdapter(patientHomeAdapter);
                homeRecycler.setEnabled(true);
            }
        }, this);
        Map<String,String> map=new HashMap<>();
        map.put("consultation_center_id",HTalkApplication.APP_CONSULTATION_CENTERID);
        map.put("info_class_id",101+"");//101 热点新闻
        HttpRestClient.OKHttpNewsCenter(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String string = jsonObject.optString("news");
                    JSONObject obj = new JSONObject(string);
                    JSONArray array = obj.getJSONArray("artList");
                   Log.e("qqqqqqqqqqqq", "onResponse: "+array.length() );
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("qqqqqqqqqqqq", "onResponse: "+e.toString() );
                }
            }


        },this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editSearch://首页搜索界面跳转的界面
                if (!LoginServiceManeger.instance().isVisitor) {
                    Intent intent = new Intent(this, HomePageActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PatientHomeActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.homeMsg://消息
                if (!LoginServiceManeger.instance().isVisitor) {
                    startActivity(new Intent(PatientHomeActivity.this, MessageNotifyActivity.class));
                } else {
                    Intent intent = new Intent(PatientHomeActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.homeMain:break;
            case R.id.homeMyDoc://我的医生
                if (!LoginServiceManeger.instance().isVisitor) {
                    startActivity(new Intent(PatientHomeActivity.this, MyDoctorMainUI.class));
                } else {
                    Intent intent = new Intent(PatientHomeActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.homeHuiZhen://专家会诊
                if (!LoginServiceManeger.instance().isVisitor) {
                    Intent intent = new Intent(PatientHomeActivity.this, PConsultMainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PatientHomeActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.homeBaiKe://六一百科
               Intent intent = new Intent(PatientHomeActivity.this, SixOneActivity.class);
                intent.putExtra(AtyNewsCenter.TYPE, "Encyclopedia");
                startActivity(intent);
                break;
            case R.id.homeMy://我的－－－个人中心
                if (!LoginServiceManeger.instance().isVisitor) {
                    Intent mIntent = new Intent(PatientHomeActivity.this, AtyPersonCenter.class);
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(PatientHomeActivity.this, UserLoginActivity.class);
                    startActivity(mIntent);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!LoginServiceManeger.instance().isVisitor) {//不是游客
            if (TextUtils.isEmpty(LoginServiceManeger.instance().getLoginEntity().getPoneNumber().trim())) {
                DoubleBtnFragmentDialog.show(getSupportFragmentManager(), "六一健康", "绑定手机号码,有助于您找回密码,现在就去绑定吗?", "去绑定", "退出", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                        Intent intent = new Intent(PatientHomeActivity.this, SettingPhoneBound.class);
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

    @Override
    public void onRefresh() {
        homeRecycler.setEnabled(false);
        imgUrl.clear();
        newsUrl.clear();
        getData();
    }
}
