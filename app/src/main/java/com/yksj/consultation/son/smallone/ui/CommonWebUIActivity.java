package com.yksj.consultation.son.smallone.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.DialogUtils;
import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * web页面
 * getIntent().getStringExtra("url")   地址 http://www.baidu.com
 * getIntent().getStringExtra("title")  title标题
 *
 * @author root
 */
public class CommonWebUIActivity extends BaseFragmentActivity implements OnClickListener {

    private WebView mWebView;
    private WebSettings settings;
    private JSONObject jsonObject;
    public static String URL = "url";
    public static String TITLE_NAME = "title";


    public static void startWeb(Context con, String url, String title) {
        Intent intent = new Intent(con, CommonWebUIActivity.class);
        intent.putExtra(URL, url);
        if (HStringUtil.isEmpty(title)) {
            intent.putExtra(TITLE_NAME, "商品详情");
        } else {
            intent.putExtra(TITLE_NAME, title);
        }
        con.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.setting_web_ui);
        mWebView = (WebView) findViewById(R.id.webview);
        settings = mWebView.getSettings();
        initView();
    }

    private void initView() {
        initTitle();
        setTitle("查看详情");
        int textSize = getIntent().getIntExtra("TextSize", 100);//网页大小
        initTextSize(textSize);
        setTitle(getIntent().getStringExtra(TITLE_NAME));
//        setLeftBack(this);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true); // 支持js
        settings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        settings.setSupportZoom(true); // 支持缩放
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 支持内容重新布局
        settings.supportMultipleWindows(); // 多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
        settings.setAllowFileAccess(true); // 设置可以访问文件
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        settings.setBuiltInZoomControls(true); // 设置支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                CommonWebUIActivity.this.setProgress(progress * 1000);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView arg0, String arg1) {
                super.onPageFinished(arg0, arg1);
                try {
                    // removeDialog(1);
                } catch (Exception e) {
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("jd.com")) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        CommonWebUIActivity.this.startActivity(intent);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        });
        String url = getIntent().getStringExtra(URL);
        if (!url.startsWith("http")) url = "http://" + url;
        mWebView.loadUrl(url, HttpRestClient.getDefaultHeaders());
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        if (getIntent().hasExtra("data")) {//如果是抽奖的,就显示右上角
//            setRightText("分享", this);

            try {
                jsonObject = new JSONObject(getIntent().getStringExtra("data"));
                setTitle(jsonObject.optString("ACTIVITIES_NAME"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    private void initTextSize(int textSize) {
        // TODO Auto-generated method stub
        Object[] objs = TextSize.class.getEnumConstants();
        for (Object obj : objs) {
            Method m;
            try {
                m = obj.getClass().getDeclaredMethod("values", new Class<?>[0]);
                Object[] results = (Object[]) m.invoke(obj, new Object[0]);
                Object objOne = results[0];
                Field code = objOne.getClass().getDeclaredField("value");
                code.setAccessible(true);
                code.set(objOne, textSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 重写onKeyDown
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack())) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected Dialog onCreateDialog(int arg0) {
        Dialog dialog = DialogUtils.getLoadingDialog(this, getResources().getString(R.string.load_hint));
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                shareAction();
                break;
            case R.id.title_back:
                finish();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                break;
        }
    }

    public void shareAction() {
        View popV = findViewById(R.id.view);
        final RequestParams params = new RequestParams();
//        params.put("ACTIVITIES_ID", jsonObject.optString("ACTIVITIES_ID"));
//        params.put("CUSTOMERID", getIntent().getStringExtra("CUSTOMERID"));
//        DialogUtils.sharePopOut(this, popV, jsonObject.optString("SHARE_DESC"), jsonObject.optString("url").replace("&customerID=", "&retype="), jsonObject.optString("SHARE_NAME"), new OneClickShare.ShareStatueListenter() {
//            @Override
//            public void ShareStatue(int tag) {
//                if (1 == tag) {
//                    HttpRestClient.doHttpSHARECHOUJIANGREC(params);
//                }
//            }
//        });


    }

    public String getPhoneVersion() {
        return Build.VERSION.RELEASE;
    }
}