package com.yksj.consultation.comm;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.CustomerInfoEntity;

/**
 * 支付宝界面
 *
 * @author Administrator
 */
public class PayActivity extends BaseFragmentActivity implements
        OnClickListener {

    private WebView wv;
    private Boolean isFirstLoad = true;
    private CustomerInfoEntity mCustomerInfoEntity;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.salon_introduction);
        initWidget();
        initData();
    }

    private void initData() {
        if (getIntent().hasExtra("mCustomerInfoEntity")) {
            mCustomerInfoEntity = (CustomerInfoEntity) getIntent().getExtras().get("mCustomerInfoEntity");
        }
        // 设置WebView属性，能够执行JavaScript脚本
//		wv.getSettings().setJavaScriptEnabled(true);
//		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportZoom(false);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setDefaultFontSize(18);
//		wv.loadData( getIntent().getStringExtra("summary"),"text/html", "utf-8");
        try {
            wv.loadDataWithBaseURL(null, getIntent().getStringExtra("summary").replace("am-loading-text", "aa").replace("J-loading am-loading", "bb").replace("加载中...", ""), "text/html", "utf-8", null);
        } catch (Exception e) {
            wv.loadDataWithBaseURL(null, getIntent().getStringExtra("summary"), "text/html", "utf-8", null);
        }

        wv.setWebViewClient(new MyWebViewClient());

    }


    private void initWidget() {
        initTitle();
        wv = (WebView) findViewById(R.id.wv);
        titleTextV.setText("订单支付");
        titleLeftBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        private LodingFragmentDialog dialog;

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            if (url.contains("healthchat2://com.dummyvision")) {
                if (url.contains("guahao")) {
                    PayActivity.this.finish();
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (dialog != null) {
                dialog.dismissAllowingStateLoss();
                dialog = null;
                isFirstLoad = false;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (isFirstLoad) {
                dialog = LodingFragmentDialog.showLodingDialog(
                        getSupportFragmentManager(), getResources());
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

    }
}
