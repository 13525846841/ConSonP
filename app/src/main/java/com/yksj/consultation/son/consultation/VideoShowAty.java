package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by HEKL on 16/6/23.
 * Used for
 */

public class VideoShowAty extends BaseFragmentActivity implements View.OnClickListener {


    private WebView mWebView;
    public static final String URL = "url";
    String url = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_video_show);
        if (getIntent().hasExtra(URL)) url = getIntent().getStringExtra(URL);
        findViewById(R.id.back).setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.wv_video);

        mWebView.getSettings().setJavaScriptEnabled(true);
//					mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl(url);
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView,(Object[])null);
            mWebView.getClass().getMethod("onResume").invoke(mWebView,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView=null;
    }
}
