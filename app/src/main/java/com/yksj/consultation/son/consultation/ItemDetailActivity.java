package com.yksj.consultation.son.consultation;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.smallone.ui.InternetVideoDemo;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ScreenUtils;
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
 * 项目发布详情  公益基金  医学前沿
 */
public class ItemDetailActivity extends BaseFragmentActivity implements View.OnClickListener, PlatformActionListener {

    private ImageView iv_share;
    private TextView mNewTitle;
    private ImageLoader mImageLoader;
    private TextView mNewTimes;// 动态消息时间
    //弹出分享窗口
    PopupWindow mPopupWindow;
    private String frontiers_id = "";
    private List<String> urlList = new ArrayList<String>();// 所有图片路径,便于点击
    private LinearLayout mImageLayout;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setVisibility(View.GONE);
        titleLeftBtn.setOnClickListener(this);

//        iv_share = (ImageView) findViewById(R.id.iv_share);
//        iv_share.setVisibility(View.VISIBLE);
//        iv_share.setOnClickListener(this);


        if (!HStringUtil.isEmpty(getIntent().getStringExtra("FUND_ID"))){
            frontiers_id = getIntent().getStringExtra("FUND_ID");//公益基金
            initFundData();
        }

        if (!HStringUtil.isEmpty(getIntent().getStringExtra("PROJECT_ID"))){
            frontiers_id = getIntent().getStringExtra("PROJECT_ID");//项目发布
            initData();
        }else if (!HStringUtil.isEmpty(getIntent().getStringExtra("frontiers_id"))){
            frontiers_id = getIntent().getStringExtra("frontiers_id");//医学前沿
            initData();
        }


        mImageLayout = (LinearLayout) findViewById(R.id.news_count_images);

        mNewTitle = (TextView) findViewById(R.id.headerTxt);
        mNewTimes = (TextView) findViewById(R.id.new_time);
        findViewById(R.id.apply_for).setOnClickListener(this);
        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
                + R.drawable.xxxxx);
//        initData();
    }

    /**
     * 查询公益基金内容的数据
     */
    private void initFundData() {
        Map<String, String> map = new HashMap<>();
        map.put("fund_id", frontiers_id);//frontiers_id
        HttpRestClient.OKHttpFindFundContent(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONArray commentsArray = obj.optJSONArray("fund");
                        for (int j = 0; j < commentsArray.length(); j++) {
                            contentObject = commentsArray.getJSONObject(j);
                            onParseFundData();//适配数据
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     * 加载数据
     */
    public JSONObject contentObject;//内容数据
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("frontiers_id", frontiers_id);//frontiers_id
        HttpRestClient.OKHttpFroMedicineDetail(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONArray commentsArray = obj.optJSONArray("frontier");
                        for (int j = 0; j < commentsArray.length(); j++) {
                            contentObject = commentsArray.getJSONObject(j);
                            onParseData();//适配数据
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }
    /**
     * 加载内容适配数据
     */
    private void onParseData() {
        int picCount = 0;
        DisplayImageOptions mDisplayImageOptions = DefaultConfigurationFactory
                .createGalleryDisplayImageOptions(this);
        DisplayImageOptions mDisplayVideoOptions = DefaultConfigurationFactory
                .createVideoDisplayImageOptions(this);
        mNewTitle.setText(contentObject.optString("FRONTIERS_NAME"));//标题
        if (HStringUtil.isEmpty(contentObject.optString("CREATE_TIME"))){
            mNewTimes.setVisibility(View.GONE);
        }else {
            mNewTimes.setText(TimeUtil.format(contentObject.optString("CREATE_TIME")));//时间
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mImageLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
        try {
            JSONArray jArray = contentObject.getJSONArray("frontier");
            for (int i = 0; i < jArray.length(); i++) {
                final JSONObject object = jArray.getJSONObject(i);
                if (object.optInt("CONTENT_TYPE") == 10) {//文字
                    TextView mNewContent = new TextView(this);
                    mNewContent.setTextColor(getResources().getColor(R.color.color_text1));
                    mNewContent.setTextSize(16);
                    mNewContent.setPadding(0, 20, 0, 20);
                    if (!HStringUtil.isEmpty(object.optString("INFO_CONTENT"))) {
                        mNewContent.setText(object.optString("INFO_CONTENT"));
                    }else if (object.optInt("CONTENT_TYPE") == 20) {//图片

                        final ImageView mImageView = new ImageView(this);
                        mImageView.setAdjustViewBounds(true);
                        mImageView.setLayoutParams(params);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mImageView.setMaxWidth(ScreenUtils.getScreenWidth(this));
                        mImageView.setMaxHeight(ScreenUtils.getScreenWidth(this) * 5);
                        mImageView.setTag(picCount);
                        picCount++;
                        urlList.add(object.optString("SMALL_PICTURE"));
//					    mImageView.setVisibility(View.VISIBLE);
//					    Picasso.with(this).load(object.optString("BIG_PICTURE")).into(mImageView);
                        mImageLoader.displayImage(object.optString("SMALL_PICTURE"), mImageView, mDisplayImageOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ItemDetailActivity.this, ImageGalleryActivity.class);
                                intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                                intent.putExtra(ImageGalleryActivity.POSITION, (Integer) mImageView.getTag());// 0,1单个,多个
//                              intent.putExtra("type", 1);// 0,1单个,多个
                                startActivityForResult(intent, 100);
                            }
                        });
                        mImageLayout.addView(mImageView);

                    } else if (object.optInt("CONTENT_TYPE") == 30) {
                        final View view = LayoutInflater.from(this).inflate(R.layout.item_video, null);
                        ImageView mImageView = (ImageView) view.findViewById(R.id.image);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageLoader.displayImage(object.optString("PICTURE_URL_2X"), mImageView, mDisplayVideoOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ItemDetailActivity.this, InternetVideoDemo.class);
                                i.putExtra("url", object.optString("PICTURE_URL_2X"));
                                startActivity(i);
                            }
                        });
                        mImageLayout.addView(view);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 加载内容适配数据         (公益基金数据)
     */
    private void onParseFundData() {
        int picCount = 0;
        DisplayImageOptions mDisplayImageOptions = DefaultConfigurationFactory
                .createGalleryDisplayImageOptions(this);
        DisplayImageOptions mDisplayVideoOptions = DefaultConfigurationFactory
                .createVideoDisplayImageOptions(this);
        mNewTitle.setText(contentObject.optString("FUND_NAME"));//标题
        mNewTimes.setText(TimeUtil.format(contentObject.optString("CREATE_TIME")));//时间
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mImageLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_line));
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
                    }else if (object.optInt("CONTENT_TYPE") == 20) {//图片

                        final ImageView mImageView = new ImageView(this);
                        mImageView.setAdjustViewBounds(true);
                        mImageView.setLayoutParams(params);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mImageView.setMaxWidth(ScreenUtils.getScreenWidth(this));
                        mImageView.setMaxHeight(ScreenUtils.getScreenWidth(this) * 5);
                        mImageView.setTag(picCount);
                        picCount++;
                        urlList.add(object.optString("PICTURE_URL"));
//					    mImageView.setVisibility(View.VISIBLE);
//					    Picasso.with(this).load(object.optString("BIG_PICTURE")).into(mImageView);
                        mImageLoader.displayImage(object.optString("PICTURE_URL"), mImageView, mDisplayImageOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ItemDetailActivity.this, ImageGalleryActivity.class);
                                intent.putExtra(ImageGalleryActivity.URLS_KEY, urlList.toArray(new String[urlList.size()]));
                                intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);// 0,1单个,多个
                                intent.putExtra(ImageGalleryActivity.POSITION, (Integer) mImageView.getTag());// 0,1单个,多个
//                              intent.putExtra("type", 1);// 0,1单个,多个
                                startActivityForResult(intent, 100);
                            }
                        });
                        mImageLayout.addView(mImageView);

                    } else if (object.optInt("CONTENT_TYPE") == 30) {
                        final View view = LayoutInflater.from(this).inflate(R.layout.item_video, null);
                        ImageView mImageView = (ImageView) view.findViewById(R.id.image);
                        mImageView.setPadding(0, 12, 0, 12);
                        mImageLoader.displayImage(object.optString("PICTURE_URL_2X"), mImageView, mDisplayVideoOptions);
                        mImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ItemDetailActivity.this, InternetVideoDemo.class);
                                i.putExtra("url", object.optString("PICTURE_URL_2X"));
                                startActivity(i);
                            }
                        });
                        mImageLayout.addView(view);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.apply_for://申请援助
                intent = new Intent(this, CommonwealAidAty.class);
                intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().HTML+"/btn.html");
                intent.putExtra(CommonwealAidAty.TITLE, "申请援助");
                startActivity(intent);
                break;
            case R.id.friendcircle://微信朋友圈
               // showShare(WechatMoments.NAME);
                sendWXMS();
                quitPopWindow();
                break;
            case R.id.wechat://微信好友
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
//        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setImageData(getBitmapFromUri(uri));
        sp.setUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");   //网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(ItemDetailActivity.this); // 设置分享事件回调
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
        sp.setUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");   //网友点进链接后，可以看到分享的详情
//      sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");
//      sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");//网友点进链接后，可以看到分享的详情
        //3、非常重要：获取平台对象
        Platform wechatms = ShareSDK.getPlatform(Wechat.NAME);
        wechatms.setPlatformActionListener(ItemDetailActivity.this); // 设置分享事件回调
        // 执行分享
        wechatms.share(sp);
    }

    private void quitPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
    /**
     * 第三方分享之微博，QQ空间
     * @param name
     */
    private void showShare(String name) {
//        uri = Uri.parse("android.resource://" + HTalkApplication.getApplication().getPackageName() + "/"
//                + R.drawable.xxxxx);
        //2、设置分享内容
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(getString(R.string.string_share_title));
        sp.setText(getString(R.string.string_share_content));
//      sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
        sp.setSiteUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");
        sp.setTitleUrl(HTalkApplication.getHttpUrls().HTML+"/newsShare.html?"+"info_id="+"1"+"?pageNum=1");//网友点进链接后，可以看到分享的详情
        sp.setImageData(getBitmapFromUri(uri));
        //3、非常重要：获取平台对象
        Platform sinaWeibo = ShareSDK.getPlatform(name);
        sinaWeibo.setPlatformActionListener(ItemDetailActivity.this); // 设置分享事件回调
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
                    WindowManager.LayoutParams params = ItemDetailActivity.this.getWindow()
                            .getAttributes();
                    params.alpha = 1.0f;
                    ItemDetailActivity.this.getWindow().setAttributes(params);
                }
            });
        } else if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return;
        }

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.showAtLocation(ItemDetailActivity.this.findViewById(R.id.ll_main_item), Gravity.BOTTOM, 0, 0);

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
