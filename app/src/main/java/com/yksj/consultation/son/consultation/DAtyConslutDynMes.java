package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AdtDynamicComment2;
import com.yksj.consultation.adapter.CommentAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.smallone.ui.InternetVideoDemo;
import com.yksj.healthtalk.entity.NewEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.PersonInfoUtil;
import com.yksj.healthtalk.utils.ScreenUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ThreadManager;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WeakHandler;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
 * @author HEKL
 *         动态消息详情界面
 */
public class DAtyConslutDynMes extends BaseFragmentActivity implements OnClickListener,
        OnRefreshListener<ListView>, OnItemClickListener, OnCheckedChangeListener, PlatformActionListener {
    private View headView;
    private LinearLayout llHide;
    private TextView mNewTitle;
    private TextView mNewTimes;// 动态消息时间
    //private TextView mNewContent;
    //private ImageView mImageView
    private LinearLayout mImageLayout;
    private RadioButton mGoodAction;// 好评
    private ImageLoader mImageLoader;
    private PullToRefreshListView mRefreshListView;
    private AdtDynamicComment2 mAdapter;
    private String consultationId = "1", infoId = "256";//会诊id,动态消息id
    private int praiseFlag = 0, praiseCount = 0;//是否点过赞,点赞数量
    private JSONObject contentObject;//内容JSONObject
    private int pageSize = 1;//第几页
    private int commCount = 1;//评论次数
    private ArrayList<NewEntity> commDatas;//评论
    private boolean isReplay = false;//   true为回复某人   false 为评论
    private String otherCustomerId = "";
    private ImageView mTv;//右上角分享
    //评论的edittext
    private EditText edit_commont;
    public Platform[] platforms;
    /**
     * 弹出评论
     */
    PopupWindow mPopBottom;
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private EditText mCommonContent;// 评论内容
    private ListView mListView;
    private TextView mConnentNum;//评论次数\

    private ListView mLv;//评论列表的listview
    public CommentAdapter adapter;//评论的适配器
    //	private WebView mWebView;
    private Uri uri;
    private List<JSONObject> mmList = new ArrayList<>();//评论数据

    private List<String> urlList = new ArrayList<String>();// 所有图片路径,便于点击
    String REPLY_ID = "123";
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            SystemUtils.showSoftMode(mCommonContent);
        }

        ;
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //  private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_dynmaicmessage_layout);
        platforms = ShareSDK.getPlatformList();

        Intent intent = getIntent();
        if (intent.hasExtra("conId")) {
            consultationId = intent.getStringExtra("conId");
        } else {
            consultationId = "1";
        }
        if (intent.hasExtra("infoId")) {
            infoId = intent.getStringExtra("infoId");
        } else {
            infoId = "256";
        }
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        if (getIntent().hasExtra("title"))
            titleTextV.setText(getIntent().getStringExtra("title"));

        mTv = (ImageView) findViewById(R.id.iv_share);
        mTv.setVisibility(View.VISIBLE);
        mTv.setOnClickListener(this);


        //评论
        mLv = (ListView) findViewById(R.id.comment_lv);
        adapter = new CommentAdapter(this, mmList);
        mLv.setAdapter(adapter);
        mLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        mNewTitle = (TextView) findViewById(R.id.headerTxt);
        mNewTimes = (TextView) findViewById(R.id.new_time);
//		mNewContent = (TextView) headView.findViewById(R.id.contentTxt);
//		mWebView= (WebView) headView.findViewById(R.id.wv_mp4);
//		mImageView = (ImageView) headView.findViewById(R.id.image);
        mImageLayout = (LinearLayout) findViewById(R.id.news_count_images);
        mGoodAction = (RadioButton) findViewById(R.id.good_action);
        mConnentNum = (TextView) findViewById(R.id.totalCommentTxt);

        edit_commont = (EditText) findViewById(R.id.edit_commont);
        llHide=(LinearLayout) findViewById(R.id.ll_hide);
        //点击editveiw时，不弹出输入键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_commont.getWindowToken(), 0);
        edit_commont.setInputType(0);
        edit_commont.setOnClickListener(this);

//        mRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
//        mListView = mRefreshListView.getRefreshableView();
//        mAdapter = new AdtDynamicComment2(DAtyConslutDynMes.this);
        mImageLoader = ImageLoader.getInstance();
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.launcher_logo);
//        onCreateHeadView();
        initData();
        showComment();
//		onDoQueryComment();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            //		case R.id.comment_to_new:// 评论
            //			isReplay=false;
            //			showCommonentPop(v);
            //			break;
            //		case R.id.share:// 分享
            //			sharePopOut();
            //			break;

            case R.id.iv_share://分享
                showShare();
                break;
            case R.id.edit_commont://评论
                if (!LoginServiceManeger.instance().isVisitor) {
                    showCommonentPop(v);
                } else {
                    Intent intent = new Intent(DAtyConslutDynMes.this, UserLoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.release:// 发布评论
                SystemUtils.hideSoftBord(this, mCommonContent);
                String comment = mCommonContent.getText().toString().trim();
                if (HStringUtil.isEmpty(comment))
                    return;
                mPopBottom.dismiss();
                //onComment();
                addComment(comment);
                break;
            case R.id.cancle:// 退出pop
                SystemUtils.hideSoftBord(this, mCommonContent);
                if (mPopBottom != null && mPopBottom.isShowing()) {
                    mPopBottom.dismiss();
                }
                break;
            case R.id.friendcircle://微信朋友圈
                // showShare(WechatMoments.NAME);
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
                //  showShare(Wechat.NAME);
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
            default:
//                if (v instanceof ImageView) { // 图片点击
//                    Intent intent = new Intent(this, ImageGalleryActivity.class);
//                    intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
//                    intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);// 0,1单个,多个
//                    intent.putExtra("type", 1);// 0,1单个,多个
//                    startActivityForResult(intent, 100);
//                }
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
//        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        String url = HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + infoId + "&pageNum=1";
        sp.setUrl(url);   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对 象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(DAtyConslutDynMes.this); // 设置分享事件回调
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
        sp.setUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + infoId + "&pageNum=1");   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(DAtyConslutDynMes.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

    /**
     * 第三方分享之微博，空间
     *
     * @param name
     */
    private void showShare(String name) {
//        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
//                + R.drawable.launcher_logo);
        //2、设置分享内容
        String url = HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + infoId + "&pageNum=1";
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(getString(R.string.string_share_title));
        sp.setText(getString(R.string.string_share_content));
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setSiteUrl(url);
        sp.setTitleUrl(url);
        sp.setImageData(getBitmapFromUri(uri));
        //sp.setTitleUrl(getString(R.string.string_share_website));  //网友点进链接后，可以看到分享的详情
        if (Wechat.NAME.equals(name)) {//微信好友
            sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + infoId + "&pageNum=1");
//            sp.setTitleUrl("www.baidu.com");

        } else {//其他
            sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML + "/newsShare.html?" + "info_id=" + infoId + "&pageNum=1");
//            sp.setTitleUrl("www.baidu.com");
        }
        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(DAtyConslutDynMes.this); // 设置分享事件回调
        // 执行分享
        sinaWeibo.share(sp);
    }

    //退出分享的popwindow
    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private String customer_id = LoginServiceManeger.instance().getLoginUserId();

    //添加评论
    private void addComment(String comment) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_content", comment);
        map.put("customer_id", customer_id);
        map.put("info_id", infoId);
        HttpRestClient.OKHttpAddComment(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        showComment();
                        ToastUtil.showShort(obj.optString("message"));
                        // onDoQueryComment();
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     * 查看评论
     */
    private void showComment() {
        Map<String, String> map = new HashMap<>();
        map.put("info_id", infoId);
        map.put("pageNum", "1");
        HttpRestClient.OKHttpQueryComment(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONObject jsonobject1 = obj.optJSONObject("news");
                        JSONArray artsArray = jsonobject1.optJSONArray("arts");
                        for (int i = 0; i < artsArray.length(); i++) {
                            JSONObject Jsonobject = artsArray.getJSONObject(i);
                            String connentnum = "";
                            connentnum = Jsonobject.optString("COMMENT_COUNT");
                            mConnentNum.setText(connentnum);
                        }
                        mmList = new ArrayList<>();
                        JSONArray commentsArray = jsonobject1.optJSONArray("comments");
                        for (int j = 0; j < commentsArray.length(); j++) {
                            JSONObject object1 = commentsArray.getJSONObject(j);
                            mmList.add(object1);
                        }

                        adapter.onBoundData(mmList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     * 弹出分享按钮
     */
    private void showShare() {
        if (mPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_share, null);
            view.findViewById(R.id.friendcircle).setOnClickListener(this);
            view.findViewById(R.id.wechat).setOnClickListener(this);
            view.findViewById(R.id.weibo).setOnClickListener(this);
            view.findViewById(R.id.qqroom).setOnClickListener(this);
            view.findViewById(R.id.btn_cancel).setOnClickListener(this);

            mPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = DAtyConslutDynMes.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DAtyConslutDynMes.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(DAtyConslutDynMes.this.findViewById(R.id.ll_main), Gravity.BOTTOM, 0, 0);

    }

    /**
     * 弹出分享框,开启分享功能
     * DynamicMessagePageServlet?CONSULTATION_CENTER_ID=&INFOID=&CUSTOMERID=
     */
    private void sharePopOut() {
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
//		OneClickShare ocs = new OneClickShare(this, bottomLayout);
//		ocs.disableSSOWhenAuthorize();
//    	ocs.setNotification(R.drawable.launcher_logo, getString(R.string.app_name));
//		 String title = mNewTitle.getText().toString().trim();
//		 String txt = mNewContent.getText().toString().trim();
//		 txt = title + "\r\n" + txt;
//		 if (txt.length() > 65)
//		 txt = txt.substring(0, 65);
//		 txt="我在神经变性病六一健康看到一条值得分享的内容:"+txt+getString(R.string.share_web)+
//		 "/DuoMeiHealth/DynamicMessagePageServlet?CONSULTATION_CENTER_ID="+consultationId+"&INFOID="
//		 +infoId+"&CUSTOMERID="+SmartFoxClient.getLoginUserId();
//		 ocs.setText(txt);
//		 ocs.setTitle(title);
//		 ocs.setUrl(getString(R.string.share_web)+
//		 "/DuoMeiHealth/DynamicMessagePageServlet?CONSULTATION_CENTER_ID="+consultationId+"&INFOID="
//		 +infoId+"&CUSTOMERID="+SmartFoxClient.getLoginUserId());
//		 String imageFilePath = null;
//		 // 如果有图片的话 带图片一起分享
//		 if (StorageUtils.isSDMounted() && (!urlList.isEmpty())) {
//			 String dir = StorageUtils.getImagePath();
//			 String imagePath = urlList.get(0);
//			 File file = mImageLoader.getOnDiscFileName(new File(dir), imagePath);
//			 if (file.exists()) {
//				 imageFilePath = file.getAbsolutePath();
//				 ocs.setImagePath(imageFilePath);
//			 }
//		 }
//		ocs.show();
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
//		mNewContent = (TextView) headView.findViewById(R.id.contentTxt);
//		mWebView= (WebView) headView.findViewById(R.id.wv_mp4);
//		mImageView = (ImageView) headView.findViewById(R.id.image);
        mImageLayout = (LinearLayout) headView.findViewById(R.id.news_count_images);
        mGoodAction = (RadioButton) headView.findViewById(R.id.good_action);
        mConnentNum = (TextView) headView.findViewById(R.id.totalCommentTxt);
    }

    /**
     * 加载消息内容
     */
    private void initData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("INFOID", infoId));
        pairs.add(new BasicNameValuePair("TYPE", "findConsuInfo"));
        HttpRestClient.doGetConsultationInfoSet(pairs, new MyResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.optString("code"))) {
                        if (jsonObject.has("result")) {
                            JSONArray array = jsonObject.getJSONArray("result");
                            contentObject = array.getJSONObject(0);
                            llHide.setVisibility(View.VISIBLE);
                            edit_commont.setVisibility(View.VISIBLE);
                            onParseData();//适配数据
                        }
                    } else {
                        ToastUtil.showShort(DAtyConslutDynMes.this, jsonObject.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        }, this);
    }

    /**
     * 解析数据
     *
     * @param
     */
    private void onParseData() {
        int picCount = 0;
        DisplayImageOptions mDisplayImageOptions = DefaultConfigurationFactory
                .createGalleryDisplayImageOptions(this);
        DisplayImageOptions mDisplayVideoOptions = DefaultConfigurationFactory
                .createVideoDisplayImageOptions(this);
        mNewTitle.setText(contentObject.optString("INFO_NAME"));//标题
        mNewTimes.setText(TimeUtil.format(contentObject.optString("PUBLISH_TIME")));//时间
        mConnentNum.setText("" + contentObject.optInt("COMMENT_COUNT"));
        praiseCount = contentObject.optInt("PRAISE_COUNT");
//      headView.findViewById(R.id.ll_prase).setVisibility(View.GONE);
        mGoodAction.setText("" + praiseCount);
        praiseFlag = contentObject.optInt("CON");
        if (praiseFlag > 0) {//点过赞
            mGoodAction.setChecked(true);
            mGoodAction.setClickable(false);
            mGoodAction.setEnabled(false);
        } else {
            mGoodAction.setChecked(false);
            mGoodAction.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    praiseFlag = 1;
                    actionPraise();
                }
            });
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mImageLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
        //数组格式,为了实现图文混排,但是现在只考虑一张图片一段文字,只不过格式不变
        try {
            JSONArray jArray = contentObject.getJSONArray("CONTENTLIST");
            for (int i = 0; i < jArray.length(); i++) {
                final JSONObject object = jArray.getJSONObject(i);
                if (object.optInt("CONTENT_TYPE") == 10) {//文字
                    TextView mNewContent = new TextView(this);
                    mNewContent.setTextColor(getResources().getColor(R.color.color_text1));
                    mNewContent.setTextSize(16);
                    mNewContent.setPadding(0, 20, 0, 20);
                    mNewContent.setMovementMethod(LinkMovementMethod.getInstance());
                    if (!HStringUtil.isEmpty(object.optString("INFO_CONTENT"))) {
//                        Spanned spanned= Html.fromHtml(object.optString("INFO_CONTENT"));
//                        mNewContent.setText(object.optString("INFO_CONTENT"));
//                        mNewContent.setText(spanned);

                        handleFriendTalkContent(object.optString("INFO_CONTENT"), mNewContent);

                        mImageLayout.addView(mNewContent);
                    }
//					mNewContent.setText(object.optString("INFO_CONTENT"));
                } else if (object.optInt("CONTENT_TYPE") == 20) {//图片

                    final ImageView mImageView = new ImageView(this);
                    mImageView.setAdjustViewBounds(true);
                    mImageView.setLayoutParams(params);
                    mImageView.setPadding(0, 12, 0, 12);
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    mImageView.setMaxWidth(ScreenUtils.getScreenWidth(this));
                    mImageView.setMaxHeight(ScreenUtils.getScreenWidth(this) * 5);
                    mImageView.setTag(picCount);
                    picCount++;
                    urlList.add(object.optString("BIG_PICTURE"));
//					mImageView.setVisibility(View.VISIBLE);
//					Picasso.with(this).load(object.optString("BIG_PICTURE")).into(mImageView);
                    mImageLoader.displayImage(object.optString("BIG_PICTURE"), mImageView, mDisplayImageOptions);
                    mImageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DAtyConslutDynMes.this, ImageGalleryActivity.class);
                            intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
                            intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                            intent.putExtra(ImageGalleryActivity.POSITION, (Integer) mImageView.getTag());// 0,1单个,多个
//                    intent.putExtra("type", 1);// 0,1单个,多个
                            startActivityForResult(intent, 100);
                        }
                    });
                    mImageLayout.addView(mImageView);

                } else if (object.optInt("CONTENT_TYPE") == 30) {
                    final View view = LayoutInflater.from(this).inflate(R.layout.item_video, null);
                    ImageView mImageView = (ImageView) view.findViewById(R.id.image);
                    mImageView.setPadding(0, 12, 0, 12);
                    mImageLoader.displayImage(object.optString("SMALL_PICTURE"), mImageView, mDisplayVideoOptions);
                    mImageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(DAtyConslutDynMes.this, InternetVideoDemo.class);
                            i.putExtra("url", object.optString("BIG_PICTURE"));
                            startActivity(i);
                        }
                    });
                    mImageLayout.addView(view);
//					urlList.add(object.optString("BIG_PICTURE"));

                }
            }
//			for(int i=0;i<jArray.length();i++){
//				JSONObject object=jArray.getJSONObject(i);
//				if(object.optInt("CONTENT_TYPE")==10){//文字
////					mNewContent.setText(object.optString("INFO_CONTENT"));
//				}else if(object.optInt("CONTENT_TYPE")==20){//图片
//					urlList.add(object.optString("BIG_PICTURE"));
//					mImageView.setVisibility(View.VISIBLE);
//					mImageLoader.displayImage(object.optString("BIG_PICTURE"),mImageView,mDisplayImageOptions);
//					mImageView.setOnClickListener(this);
//				}else if (object.optInt("CONTENT_TYPE")==30){
////					urlList.add(object.optString("BIG_PICTURE"));
//					// 设置WebView属性，能够执行Javascript脚本
////					mWebView.getSettings().setJavaScriptEnabled(true);
//////					mWebView.getSettings().setPluginsEnabled(true);
////					mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
////					mWebView.setVisibility(View.VISIBLE);
////					mWebView.getSettings().setUseWideViewPort(true);
////					mWebView.loadUrl(object.optString("BIG_PICTURE"));
//				}
//			}
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mListView.addHeaderView(headView, null, false);
//        mListView.setAdapter(mAdapter);
//        mRefreshListView.setOnRefreshListener(this);
//        mListView.setOnItemClickListener(this);
    }

    /**
     * 网络图片Getter
     */
    private NetworkImageGetter mImageGetter;

    /**
     * 处理PC客服发来的内容
     *
     * @param editText
     */
    private void handleFriendTalkContent(final String contents, final TextView editText) {
        final String content = contents.replace("\n", "<br>");
        final WeakHandler handler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x101:
                        editText.setText((CharSequence) msg.obj);
                        break;
                }
                return false;
            }
        });
        // 因为从网上下载图片是耗时操作 所以要开启新线程
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mImageGetter = new NetworkImageGetter(editText, content, handler);
                CharSequence test = Html.fromHtml(content, mImageGetter, null);
                Message msg = Message.obtain();
                msg.what = 0x101;
                msg.obj = test;
                handler.sendMessage(msg);

            }
        });
    }


    /**
     * 网络图片
     *
     * @author Susie
     */
    private final class NetworkImageGetter implements Html.ImageGetter {
        private TextView mEditText;
        private String content;
        private WeakHandler handler;

        public NetworkImageGetter(TextView mEditText, String content, WeakHandler handler) {
            this.mEditText = mEditText;
            this.content = content;
            this.handler = handler;
        }

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            String handleSource = source;
            String[] sourceStrArray = null;
            if (source.contains("faces")) {
                sourceStrArray = handleSource.split("faces/");
            } else {
                sourceStrArray = handleSource.split("path=/");
            }
            // 封装路径
            if (sourceStrArray.length < 2) {
                return null;
            }
            String path = sourceStrArray[1].replace("/", "");
            File file = new File(Environment.getExternalStorageDirectory(), path);
            // 判断是否以http开头
            if (source.startsWith("http")) {
                // 判断路径是否存在
                if (file.exists()) {
                    // 存在即获取drawable
                    drawable = Drawable.createFromPath(file.getAbsolutePath());
                    if (drawable != null) {
                        int a = drawable.getIntrinsicWidth();
                        int b = drawable.getIntrinsicHeight();
                        drawable.setBounds(0, 0, 4 * a, 4 * b);
                    }
                } else {
                    // 不存在即开启异步任务加载网络图片
                    AsyncLoadNetworkPic networkPic = new AsyncLoadNetworkPic(mEditText, content);
                    networkPic.execute(source);
                }
            }


            return drawable;
        }
    }

    /**
     * 加载网络图片异步类
     *
     * @author Susie
     */
    private final class AsyncLoadNetworkPic extends AsyncTask<String, Integer, Void> {
        private TextView mEditText;
        private String content;

        public AsyncLoadNetworkPic(TextView editText, String content) {
            this.mEditText = editText;
            this.content = content;
        }

        @Override
        protected Void doInBackground(String... params) {
            // 加载网络图片
            loadNetPic(params);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // 当执行完成后再次为其设置一次
            mEditText.setText(Html.fromHtml(content, mImageGetter, null));
        }

        /**
         * 加载网络图片
         */
        private void loadNetPic(String... params) {
            String path = params[0];
            String handleSource = path;
            String[] sourceStrArray = null;
            if (handleSource.contains("faces/")) {
                sourceStrArray = handleSource.split("faces/");
            } else {
                sourceStrArray = handleSource.split("path=/");
            }

            if (sourceStrArray.length < 2) {
                return;
            }
            String paths = sourceStrArray[1].replace("/", "");
            File file = new File(Environment.getExternalStorageDirectory(), paths);

            InputStream in = null;

            FileOutputStream out = null;

            try {
                URL url = new URL(path);

                HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();

                connUrl.setConnectTimeout(5000);

                connUrl.setRequestMethod("GET");

                if (connUrl.getResponseCode() == 200) {

                    in = connUrl.getInputStream();

                    out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    int len;

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } else {
//                    Log.i(TAG, connUrl.getResponseCode() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     *  查询评论
     *  GroupConsultationList?TYPE=findCommentList&INFOID=256&PAGESIZE=1&PAGENUM=5
     */
    private void onDoQueryComment() {
        RequestParams params = new RequestParams();
        params.put("TYPE", "findCommentList");
        params.put("INFOID", infoId);
        params.put("VALID_MARK", HTalkApplication.APP_VALID_MARK);
        params.put("PAGESIZE", "" + pageSize);
        params.put("PAGENUM", "5");
        // 第一次1,之后为0;
        HttpRestClient.doHttpGroupConsultationList(params, new AsyncHttpResponseHandler(this) {

            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                pageSize++;
                try {
                    JSONObject response = new JSONObject(content);
                    JSONObject jsonObject = response.getJSONObject("findCommentList");
                    commCount = jsonObject.optInt("count", 0);
                    JSONArray commArray = jsonObject.optJSONArray("commentList");
                    commDatas = new ArrayList<>();
                    NewEntity entity;
                    for (int i = 0; i < commArray.length(); i++) {
                        entity = new NewEntity();
                        entity.COMMENT_CONTENT = commArray.optJSONObject(i).optString("COMMENT_CONTENT");
                        entity.CUSTOMER_NICKNAME = commArray.optJSONObject(i).optString("CUSTOMER_NICKNAME");
                        entity.REPLY_ID = commArray.optJSONObject(i).optString("COMMENT_ID");
                        entity.REPLYTIME = commArray.optJSONObject(i).optString("COMMENT_TIME");
                        entity.CLIENT_ICON_BACKGROUND = commArray.optJSONObject(i).optString("CLIENT_ICON_BACKGROUND");
                        entity.CUSTOMER_ID = commArray.optJSONObject(i).optString("CUSTOMER_ID");
                        entity.UPPER_REPLY_ID = commArray.optJSONObject(i).optString("UPPER_COMMENT_ID");
                        commDatas.add(entity);
                    }

                    if (pageSize == 2) {//第一次加载
                        NewEntity ne = new NewEntity();
                        ne.CUSTOMER_ID = "-1";
                        commDatas.add(0, ne);
                        mAdapter.onBoundData(commDatas);
                    } else {//第二次加载
                        if (commDatas.size() > 1) {
                            mAdapter.addAll(commDatas);
                        } else if (pageSize > 2) {
                            ToastUtil.showShort("没有更多了");
                        }
                    }

                    //点击头像到个人资料的监听
                    mAdapter.setOnImageclicl(new AdtDynamicComment2.ImageClick() {
                        @Override
                        public void onImageClick(NewEntity en) {
                            PersonInfoUtil.choiceActivity(en.CUSTOMER_ID, DAtyConslutDynMes.this, "0");
                        }
                    });
                    mAdapter.setNumberCount(commCount + "");
//					 mConnentNum.setText(commCount+"条");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRefreshListView.onRefreshComplete();
            }
        });
    }

    /*
     * 点赞操作
     * GroupConsultationList?TYPE=praiseConsuInfo&INFOID=&CUSTOMERID=
     */
    private void actionPraise() {
        praiseCount++;
        mGoodAction.setText("" + praiseCount);
        RequestParams mParams = new RequestParams();
        mParams.put("TYPE", "praiseConsuInfo");
        mParams.put("INFOID", infoId);
        mParams.put("CUSTID", SmartFoxClient.getLoginUserId());
        HttpRestClient.doHttpGroupConsultationList(mParams, new AsyncHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, String content) {
                super.onSuccess(statusCode, content);
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.has("error_message")) {
                        praiseCount--;
                        mGoodAction.setText("" + praiseCount);
                        mGoodAction.setChecked(false);
                        mGoodAction.setClickable(true);
                        mGoodAction.setEnabled(true);
                        ToastUtil.showShort(jsonObject.optString("error_message", "操作失败"));
                    } else {
                        mGoodAction.setClickable(false);
                        mGoodAction.setEnabled(false);
                        ToastUtil.showShort(jsonObject.optString("message", "操作成功"));
                    }
                } catch (JSONException e) {

                }
            }
        });
    }


    /**
     * 回复某个人
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        NewEntity entity = mAdapter.datas.get(position - 2);
        REPLY_ID = entity.REPLY_ID;
        otherCustomerId = entity.CUSTOMER_ID;
//			 showCommonentPop(findViewById(R.id.comment_to_new),"回复"+mName.getText().toString());
        isReplay = true;
//		 showCommonentPop(findViewById(R.id.comment_to_new));
    }

    // @Override
    // public void onFinish() {
    // super.onFinish();
    // mCollection.setClickable(true);
    // }
    // });
    // } else {
    // HttpRestClient.doHttpNewsCollectionRemove(mSmartUserId, mNewsId,
    // "news", new AsyncHttpResponseHandler(this) {
    // @Override
    // public void onSuccess(int statusCode, String content) {
    // if ("1".equals(content)) {
    // onUpdateCollectionNumber(isCollection);
    // }
    // mCollection.setClickable(true);
    // }
    // });
    // }
    // }

    private void showCommonentPop(View view) {

        if (mPopBottom == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.new_common_pop_layout, null);
            mCommonContent = (EditText) v.findViewById(R.id.content);
            mCommonContent.setHint("");
            v.findViewById(R.id.cancle).setOnClickListener(this);
            v.findViewById(R.id.release).setOnClickListener(this);
            mPopBottom = new PopupWindow(v, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
            mPopBottom.setBackgroundDrawable(new BitmapDrawable());
            mPopBottom.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            mPopBottom.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = DAtyConslutDynMes.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    DAtyConslutDynMes.this.getWindow().setAttributes(params);
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

    /**
     * 评论提交
     * GroupConsultationList?TYPE=commentOnconsultationInfo&INFOID=&CUSTOMERID=&COMMENT_CONTENT=
     */
    private void onComment() {
        RequestParams params = new RequestParams();
        params.put("INFOID", infoId);
        params.put("COMMENT_CONTENT", mCommonContent.getText().toString());
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        if (isReplay) {//回复
            params.put("TYPE", "quotetCommentOnconsultationInfo");
            params.put("COMMENTID", REPLY_ID);
            params.put("OTHER_CUSTOMER_ID", otherCustomerId);
        } else {//评论
            params.put("TYPE", "commentOnconsultationInfo");
        }
        mCommonContent.setText("");
        HttpRestClient.doHttpGroupConsultationList(params, new AsyncHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.has("error_message")) {
                        ToastUtil.showShort(jsonObject.optString("error_message"));
                    } else {
                        ToastUtil.showShort(jsonObject.optString("message"));
                        pageSize = 1;
                        onDoQueryComment();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//		initData();
        mRefreshListView.onRefreshComplete();
//		onDoQueryComment();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//		case R.id.collection_action:// 是否收藏
//			onCollectionCountUpdate(isChecked);
//			break;
        }
    }

    /**
     * 收藏计数更新
     * 收藏
     * http://220.194.46.204:8080/DuoMeiHealth/
     * GroupConsultationList?TYPE=collectedConsuInfo&CUSTOMERID=&INFOID=
     * 取消收藏
     * GroupConsultationList?TYPE=cancelCollectedInfoBatch&CUSTOMERIDS=&INFOID=
     *
     * @param isCollection
     */
    private void onCollectionCountUpdate(final boolean isCollection) {
        RequestParams params = new RequestParams();
        params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
        if (isCollection) {
            params.put("INFOID", infoId);
            params.put("TYPE", "collectedConsuInfo");
        } else {
            params.put("INFOIDS", infoId);
            params.put("TYPE", "cancelCollectedInfoBatch");
        }
        HttpRestClient.doHttpGroupConsultationList(params, new AsyncHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, String content) {
//				if ("1".equals(content)) {
//					onUpdateCollectionNumber(isCollection);
//				}
                try {
                    JSONObject object = new JSONObject(content);
                    if (object.has("message")) {
                        ToastUtil.showShort(object.optString("message"));
                    } else if (object.has("error_message")) {
                        ToastUtil.showShort(object.optString("error_message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
            handler.sendEmptyMessage(4);
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
     *
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
}
