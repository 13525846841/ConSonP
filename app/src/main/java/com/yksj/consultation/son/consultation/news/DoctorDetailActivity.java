package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.adapter.CommentAdapter;
import com.yksj.consultation.adapter.RecomDocAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

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
 * 名医讲堂详情界面
 */
public class DoctorDetailActivity extends BaseFragmentActivity implements View.OnClickListener, PlatformActionListener {
    private View headView;
    private ImageView share;
    private List<JSONObject> mmList = new ArrayList<>();//评论数据
    private EditText edit_commont;
    private LinearLayout mImageLayout;
    private TextView mConnentNum;//评论次数\
    private EditText mCommonContent;// 评论内容
    private ListView mLv;//评论列表的listview
    public CommentAdapter adapter;//评论的适配器
    private TextView mNewTitle;
    private ImageLoader mImageLoader;
    private TextView mNewTimes;// 动态消息时间
    private List<String> urlList = new ArrayList<String>();// 所有图片路径,便于点击
    PopupWindow mPopupWindow;
    private String forum_id;

    private ListView mListView;//名医推荐
    private RecomDocAdapter mAdapter;//名医推荐适配器
    private List<JSONObject> reList = new ArrayList<>();


    /**
     * 弹出评论
     */
    PopupWindow mPopBottom;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            SystemUtils.showSoftMode(mCommonContent);
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setVisibility(View.INVISIBLE);
        share = (ImageView) findViewById(R.id.iv_share);
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(this);
        titleLeftBtn.setOnClickListener(this);

        Intent intent = getIntent();
        forum_id = intent.getStringExtra("FORUM_ID");

        mConnentNum = (TextView) findViewById(R.id.totalCommentTxt);
        edit_commont = (EditText) findViewById(R.id.edit_commont);
        mNewTitle = (TextView) findViewById(R.id.headerTxt);
        mNewTimes = (TextView) findViewById(R.id.new_time);

        mListView = (ListView) findViewById(R.id.doctor_lv);
        mAdapter = new RecomDocAdapter(this,reList);
        mListView.setAdapter(mAdapter);


        mLv = (ListView) findViewById(R.id.comment_lv);
        adapter = new CommentAdapter(this,mmList);
        mLv.setAdapter(adapter);
        mImageLoader = ImageLoader.getInstance();
        //点击editveiw时，不弹出输入键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_commont.getWindowToken(), 0);
        edit_commont.setInputType(0);
        edit_commont.setOnClickListener(this);
        initData();
        CommentData();
        showComment();
    }

    /**
     * 创建头部view
     *
     * @return
     */
    private void onCreateHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.dynamic_content_header_layout2, null);
        mNewTitle = (TextView) headView.findViewById(R.id.headerTxt);
        mNewTimes = (TextView) headView.findViewById(R.id.new_time);
        mImageLayout = (LinearLayout) headView.findViewById(R.id.news_count_images);
        mConnentNum = (TextView) headView.findViewById(R.id.totalCommentTxt);
    }
    /**
     * 加载内容数据
     */
    public JSONObject contentObject;//内容数据
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("forum_id",forum_id);//forum_id
        HttpRestClient.OKHttpFamDocDetail(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray commentsArray = obj.optJSONArray("forum");
                        for (int j=0;j<commentsArray.length();j++){
                            contentObject = commentsArray.getJSONObject(j);
                            onParseData();//适配数据
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    /***
     * 推荐医生
     */
    private void CommentData(){
        Map<String,String> map=new HashMap<>();
        map.put("forum_id",forum_id);//forum_id
        HttpRestClient.OKHttpSCommentDoctor(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray commentsArray = obj.optJSONArray("doctor");
                        for (int j=0;j<commentsArray.length();j++){
                            JSONObject commentJson = commentsArray.getJSONObject(j);
                            reList.add(commentJson);
                        }
                        mAdapter.onBoundData(reList);
                        if (reList.size()==0){
                            mListView.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }
    /**
     * 加载内容适配数据
     */
    private void onParseData() {
        DisplayImageOptions mDisplayImageOptions = DefaultConfigurationFactory
                .createGalleryDisplayImageOptions(this);
        DisplayImageOptions mDisplayVideoOptions = DefaultConfigurationFactory
                .createVideoDisplayImageOptions(this);
        mNewTitle.setText(contentObject.optString("FORUM_NAME"));//标题
        mNewTimes.setText(TimeUtil.format(contentObject.optString("CREATE_TIME")));//时间
        mConnentNum.setText("" + contentObject.optInt("COMMENT_COUNT"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       //mImageLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
        try {
            JSONArray jArray = contentObject.getJSONArray("CONTENTLIST");
            for (int i = 0; i < jArray.length(); i++) {
                final JSONObject object = jArray.getJSONObject(i);
                if (object.optInt("CONTENT_TYPE") == 10) {//文字
                    TextView mNewContent = new TextView(this);
                    mNewContent.setTextColor(getResources().getColor(R.color.color_text1));
                    mNewContent.setTextSize(16);
                    mNewContent.setPadding(0, 20, 0, 20);
                    if (!HStringUtil.isEmpty(object.optString("INFO_CONTENT"))) {
                        mNewContent.setText(object.optString("INFO_CONTENT"));
                        mImageLayout.addView(mNewContent);
                    }
                }
//                else if (object.optInt("CONTENT_TYPE") == 20) {//图片
//
//                    final ImageView mImageView = new ImageView(this);
//                    mImageView.setAdjustViewBounds(true);
//                    mImageView.setLayoutParams(params);
//                    mImageView.setPadding(0, 12, 0, 12);
//                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    mImageView.setMaxWidth(ScreenUtils.getScreenWidth(this));
//                    mImageView.setMaxHeight(ScreenUtils.getScreenWidth(this) * 5);
//                    mImageView.setTag(picCount);
//                    picCount++;
//                    urlList.add(object.optString("BIG_PICTURE"));
//                    mImageLoader.displayImage(object.optString("BIG_PICTURE"), mImageView, mDisplayImageOptions);
//                    mImageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(DoctorDetailActivity.this, ImageGalleryActivity.class);
//                            intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
//                            intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
//                            intent.putExtra(ImageGalleryActivity.POSITION, (Integer) mImageView.getTag());// 0,1单个,多个
//                            startActivityForResult(intent, 100);
//                        }
//                    });
//                    mImageLayout.addView(mImageView);
//
//                }
//               else if (object.optInt("CONTENT_TYPE") == 30) {
//                    final View view = LayoutInflater.from(this).inflate(R.layout.item_video, null);
//                    ImageView mImageView = (ImageView) view.findViewById(R.id.image);
//                    mImageView.setPadding(0, 12, 0, 12);
//                    mImageLoader.displayImage(object.optString("SMALL_PICTURE"), mImageView, mDisplayVideoOptions);
//                    mImageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(DoctorDetailActivity.this, InternetVideoDemo.class);
//                            i.putExtra("url", object.optString("BIG_PICTURE"));
//                            startActivity(i);
//                        }
//                    });
                 //  mImageLayout.addView(view);
 //               }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看评论
     */
    private void showComment() {
        Map<String,String> map=new HashMap<>();
        map.put("forum_id",forum_id);//forum_id
        HttpRestClient.OKHttpSeeFamDocComment(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {

                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        mmList = new ArrayList<>();
                        JSONArray commentsArray = obj.optJSONArray("comments");
                        for (int j=0;j<commentsArray.length();j++){
                            JSONObject object1 = commentsArray.getJSONObject(j);
                            mmList.add(object1);
                        }
                        adapter.onBoundData(mmList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.edit_commont://评论
                showCommonentPop(v);
                break;
            case R.id.friendcircle://微信朋友圈
                showShare(WechatMoments.NAME);
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
                showShare(Wechat.NAME);
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
            case R.id.release:// 发布评论
                SystemUtils.hideSoftBord(this, mCommonContent);
                String comment =  mCommonContent.getText().toString().trim();
                if (HStringUtil.isEmpty(comment))
                    return;
                mPopBottom.dismiss();
                addComment(comment);
                break;
        }
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
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
       // sp.setSiteUrl("http://220.194.46.204/DuoMeiHealth/html/newsShare.html?info_id=1?pageNum=1");
        //sp.setTitleUrl(getString(R.string.string_share_website));  //网友点进链接后，可以看到分享的详情
        sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?" + "info_id=1?pageNum=1");  //网友点进链接后，可以看到分享的详情
        sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?" + "info_id=1?pageNum=1");  //网友点进链接后，可以看到分享的详情

        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(DoctorDetailActivity.this); // 设置分享事件回调
        // 执行分享
        sinaWeibo.share(sp);
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
                    WindowManager.LayoutParams params = DoctorDetailActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DoctorDetailActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(DoctorDetailActivity.this.findViewById(R.id.ll_main_doc_detail), Gravity.BOTTOM, 0, 0);
    }

    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
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
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();

    /**
     * 添加评论
     * @param comment
     */
    private void addComment(String comment) {
        Map<String,String> map=new HashMap<>();
        map.put("Comment_content", comment);
        map.put("customer_id",customer_id);
        map.put("forum_id",forum_id);//名医讲堂id//forum_id
        HttpRestClient.OKHttpAddFamDocComment(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        showComment();
                        ToastUtil.showShort(obj.optString("message"));
                    } else{
                        ToastUtil.showShort(obj.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    private void showCommonentPop(View view) {
        if (mPopBottom == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.new_common_pop_layout, null);
            mCommonContent = (EditText) v.findViewById(R.id.content);
            mCommonContent.setHint("");
            v.findViewById(R.id.cancle).setOnClickListener(this);
            v.findViewById(R.id.release).setOnClickListener(this);
            mPopBottom = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopBottom.setBackgroundDrawable(new BitmapDrawable());
            mPopBottom.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            mPopBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = DoctorDetailActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DoctorDetailActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopBottom.isShowing()) {
            mPopBottom.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        View bottomLayout;
        // bottomLayout = findViewById(R.id.);
        mPopBottom.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mHandler.sendEmptyMessage(0);
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
}
