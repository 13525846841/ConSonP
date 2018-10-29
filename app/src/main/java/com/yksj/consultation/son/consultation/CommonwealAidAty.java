package com.yksj.consultation.son.consultation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * Created by HEKL on 16/5/6.
 * Used for
 */

public class CommonwealAidAty extends BaseFragmentActivity implements View.OnClickListener {
    public static final String URL = "url";
    public static final String TITLE = "TITLE";
    private WebView mWebView;
    String url = "";
    public static final String TYPE = "type";
    private String type = "";//加载类型 1名医名院

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_commonwealaid);

        initView();
    }

    private void initView() {
        initTitle();
        if (getIntent().hasExtra(TYPE)) {
            type = getIntent().getStringExtra(TYPE);
        }
        titleLeftBtn.setVisibility(View.VISIBLE);

        titleLeftBtn.setOnClickListener(this);
        titleRightBtn.setVisibility(View.VISIBLE);
        titleRightBtn.setText("复制");
        titleRightBtn.setOnClickListener(this);
        if (getIntent().hasExtra(TITLE))
            titleTextV.setText(getIntent().getStringExtra(TITLE));

        if (getIntent().hasExtra(URL))
            url = getIntent().getStringExtra(URL);
        mWebView = (WebView) findViewById(R.id.wv_web);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        setWebStyle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    onBackPressed();
                }
                break;
            case R.id.title_right:
                copyWord();
                break;
        }
    }

    private void copyWord() {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", url);//text是内容
        myClipboard.setPrimaryClip(myClip);
        ToastUtil.showShort("复制成功，可以发给朋友们了");
    }


    /**
     * WebView设置
     */
    private void setWebStyle() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.requestFocus();
        if (HStringUtil.isEmpty(url)) {
            url = HttpRestClient.getmHttpUrls().PUBLICDONATE;
        }

        if (!url.startsWith("http")) {
            mWebView.loadUrl("https://" + url);
        } else {
            mWebView.loadUrl(url);
        }


        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                // view.loadUrl(url);

                switch (type) {
                    case "1":
                        if (url.startsWith("tel")) {//六一健康
                            if (hasSimCard()) {
                                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));//跳转到拨号界面，同时传递电话号码
                                startActivity(dialIntent);
                            } else {
                                ToastUtil.showShort("请检查sim卡");
                            }
                        } else {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                    default:
                        view.loadUrl(url);
                        break;
                }


                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @return
     */
    public boolean hasSimCard() {

        TelephonyManager telMgr = (TelephonyManager)
                CommonwealAidAty.this.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }
}
