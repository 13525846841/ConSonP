package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.adapter.SeePlanAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 查看计划
 */
public class SeePlanActivity extends BaseFragmentActivity implements View.OnClickListener ,PlatformActionListener {

    private ListView mListView;
    private SeePlanAdapter mAdapter;
    private View header;
    private ImageView headImage;
    private TextView textname;
    private TextView textsex;
    private TextView textage;
    private TextView title;
    private TextView tv_targe;
    private TextView tv_time_text;
    private List<JSONObject> mList = null;
    private ImageView success;
    private ImageView failure;
    private ImageView share;
    private boolean isSuccess = false;
    private boolean isFailure = false;
    private String name = "";
    private String sex = "";
    private String age = "";
    private String plan_status;//成功与否状态
    private static final int WRITFLAG = 10002;
    //弹出分享窗口
    PopupWindow mPopupWindow;

    public String  PLAN_START;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_plan);
        initView();
        initData();
    }

    private String PLAN_ID = "";
    private String children_id = "";
    private String CUSTOMER_REMARK = "";

    private void initView() {
        initTitle();
        Intent intent = getIntent();
        PLAN_ID = intent.getStringExtra("plan_id");
        children_id = intent.getStringExtra("children_id");
        PLAN_START = intent.getStringExtra("PLAN_START");

        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        sex = intent.getStringExtra("sex");

        titleTextV.setText("计划详情");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("填写关爱计划");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.lv_seeplan);

        header = View.inflate(this, R.layout.head_see_plan, null);
        headImage = (ImageView) header.findViewById(R.id.image_head);
        textname = (TextView) header.findViewById(R.id.tv_member_name);
        textsex = (TextView) header.findViewById(R.id.tv_member_sex);
        textage = (TextView) header.findViewById(R.id.tv_member_age);

        Picasso.with(SeePlanActivity.this).load(intent.getStringExtra("url")).placeholder(R.drawable.waterfall_default).into(headImage);

        textname.setText(name);

        if ("null".equals(intent.getStringExtra("age"))){
            textage.setText("");
        }else{
            textage.setText(intent.getStringExtra("age"));
        }

        if ("1".equals(intent.getStringExtra("sex"))){
            textsex.setText("男");
        }else if ("0".equals(intent.getStringExtra("sex"))){
            textsex.setText("女");
        }else {
            textsex.setText("");
        }


        title = (TextView) header.findViewById(R.id.title);
        tv_targe = (TextView) header.findViewById(R.id.tv_targe);
        tv_time_text = (TextView) header.findViewById(R.id.tv_time_text);

        success = (ImageView) header.findViewById(R.id.success);
        failure = (ImageView) header.findViewById(R.id.failure);
        share = (ImageView) header.findViewById(R.id.plan_share);

        if ("20".equals(PLAN_START)){
            success.setSelected(true);
        }else if ("30".equals(PLAN_START)){
            failure.setSelected(true);
        }

        success.setOnClickListener(this);
        failure.setOnClickListener(this);
        share.setOnClickListener(this);

        mListView.addHeaderView(header);
        mList = new ArrayList<JSONObject>();
        mAdapter = new SeePlanAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        // initData();
    }

    public void initData() {//100084 100013
        Map<String, String> map = new HashMap<>();
        map.put("plan_id", PLAN_ID);
        HttpRestClient.OKHttpPlanDetail(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String content) {
                try {
                    LoginServiceManeger.instance().getLoginUserId();
                    if (content != null) {
                        JSONObject object = new JSONObject(content);
                        JSONObject massage = object.optJSONObject("plan");
                        title.setText(massage.optString("PLAN_TITLE"));
                        tv_targe.setText(massage.optString("PLAN_TARGET"));
                        CUSTOMER_REMARK = massage.optString("CUSTOMER_REMARK");
                        tv_time_text.setText(massage.optString("PLAN_CYCLE") +"周"+ "(" + TimeUtil.getFormatDate2(massage.optString("PLAN_START")) + "至" + TimeUtil.getFormatDate2(massage.optString("PLAN_END")) + ")");

                        mList = new ArrayList<JSONObject>();
                        JSONArray record = object.getJSONArray("record");
                        int count = record.length();
                        if (count > 0) {
                            for (int i = 0; i < record.length(); i++) {
                                JSONObject jsonObject = record.getJSONObject(i);
//                                for (int j = 0; j < 40; j++) {
                                mList.add(jsonObject);
//                                }
                            }
                            mAdapter.onBoundData(mList);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                intent = new Intent(SeePlanActivity.this, WritePlanActivity.class);
                intent.putExtra("plan_id", PLAN_ID);
                intent.putExtra("children_id", children_id);
                intent.putExtra("CUSTOMER_REMARK", CUSTOMER_REMARK);
                startActivityForResult(intent, WRITFLAG);
                break;
            case R.id.success:
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确定计划成功了吗", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        plan_status = "20";
                        planChange();
                    }
                });

                if (isSuccess == false) {
                    isSuccess = true;
                    success.setSelected(true);
                    failure.setSelected(false);

                } else if (isSuccess == true) {
                    isSuccess = false;
                    success.setSelected(false);
                    failure.setSelected(true);
                }
                break;
            case R.id.failure:
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确定计划失败了吗", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {

                    }
                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        plan_status = "30";
                        planChange();
                    }
                });
                if (isFailure == false) {
                    isFailure = true;
                    success.setSelected(false);
                    failure.setSelected(true);

                } else if (isFailure == true) {
                    isFailure = false;
                    success.setSelected(true);
                    failure.setSelected(false);
                }
                break;
            case R.id.plan_share:
                showShare();
                break;
            case R.id.friendcircle://微信朋友圈
               // showShare(WechatMoments.NAME);
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
               // showShare(Wechat.NAME);
                sendWX();
                quitPopWindow();
                break;
            case R.id.weibo://新浪微博分享
                showShare(SinaWeibo.NAME);
                quitPopWindow();
                break;
            case R.id.qqroom://qq空间
                showShare(QZone.NAME);
                quitPopWindow();
                break;
            case R.id.btn_cancel:
                ToastUtil.showShort("取消");
                quitPopWindow();
                break;
        }
    }
    /**
     * 微信朋友圈
     */
    private void sendWXMS() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl(HTalkApplication.getHttpUrls().HTML+"/plan.html?"+"plan_id="+PLAN_ID);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(SeePlanActivity.this); // 设置分享事件回调
        // 执行分享
        wechat.share(sp);
    }

    /**
     * 微信分享
     */
    private void sendWX() {
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title)); //分享标题
        sp.setText(getString(R.string.string_share_content)); //分享文本
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl(HTalkApplication.getHttpUrls().HTML+"/plan.html?"+"plan_id="+PLAN_ID);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(SeePlanActivity.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

    /**
     * 第三方分享
     * @param name
     */
    private Uri uri;
    private void showShare(String name) {
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.launcher_logo);
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(getString(R.string.string_share_title));
        sp.setText(getString(R.string.string_share_content));
//        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML+"/plan.html?"+"plan_id="+PLAN_ID);//100033
        sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML+"/plan.html?"+"plan_id="+PLAN_ID);//100033

        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(SeePlanActivity.this); // 设置分享事件回调
        // 执行分享
        sinaWeibo.share(sp);
    }

    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void showShare() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_share, null);
            view.findViewById(R.id.friendcircle).setOnClickListener(this);
            view.findViewById(R.id.wechat).setOnClickListener(this);
            view.findViewById(R.id.weibo).setOnClickListener(this);
            view.findViewById(R.id.qqroom).setOnClickListener(this);
            view.findViewById(R.id.btn_cancel).setOnClickListener(this);

            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = SeePlanActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    SeePlanActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(SeePlanActivity.this.findViewById(R.id.ll_main_seeplan), Gravity.BOTTOM, 0, 0);

    }

    /**
     * 变更计划状态
     */
    private void planChange() {
        Map<String, String> map = new HashMap<>();
        map.put("plan_id", PLAN_ID);
        map.put("plan_status", plan_status);
        HttpRestClient.OKHttpPlanChange(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {
                        ToastUtil.showShort(object.optString("message"));
                        finish();
                    } else {
                        ToastUtil.showShort(object.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITFLAG && resultCode == RESULT_OK) {
            initData();
        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {//回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(5);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(6);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 5:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败啊" + msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 获取图片Bitmap
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    if (picList.size() > 0) {
//        view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.VISIBLE);
//    } else {
//        view.findViewById(R.id.fgt_case_img_layout_horscrollview).setVisibility(View.GONE);
//    }
}
