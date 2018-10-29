package com.yksj.consultation.son.wallet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.JsonParseUtils;
import com.yksj.healthtalk.utils.ToastUtil;


/**
 * 支付宝界面
 * 
 * @author Administrator
 * 
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
		wv.getSettings().setJavaScriptEnabled(true);
		//wv.getSettings().setUserAgentString(System.getProperty("http.agent"));
		// 如果要播放Flash，需要加上这一句
//		wv.getSettings().setPluginsEnabled(true);
		wv.loadDataWithBaseURL(null, getIntent().getStringExtra("summary"),"text/html", "utf-8", null);
		
	
//		wv.loadData(getIntent().getStringExtra("summary"), "text/html", "utf-8");
//		wv.loadUrl(getIntent().getStringExtra("summary"));
		MyWebViewClient myWebView = new MyWebViewClient();
		wv.setWebViewClient(myWebView);
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
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains("healthchat://com.dummyvision")) {
				Intent intent = new Intent();
				String id = url.substring(url.lastIndexOf("=") + 1);
				if (url.contains("guahao")) {
					if (mCustomerInfoEntity == null
							&& !mCustomerInfoEntity.getId().equals(id)) {
						ToastUtil.showShort(getApplicationContext(),
								"mCustomerInfoEntity is null");
					} else {
						HttpRestClient.doHttpInitChat(SmartFoxClient.getLoginUserId(),mCustomerInfoEntity.getId(),new JsonsfHttpResponseHandler(){
							public void onSuccess(int statusCode, com.alibaba.fastjson.JSONObject response) {
								super.onSuccess(statusCode, response);
									String content;
									if ((content = JsonParseUtils.filterErrorMessage(response) )!= null) {
										SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),content);
									}else {
										String sendCode = response.getString("send_code");
										Intent intent = new Intent();
										intent.setClass(getApplicationContext(), ChatActivity.class);
										if (!TextUtils.isEmpty(sendCode)) {
											intent.putExtra("NOTE", sendCode);
										}
										intent.putExtra("pay_type", "pay_type_dialog");
										intent.putExtra(ChatActivity.KEY_PARAME, mCustomerInfoEntity);
										startActivity(intent);
										finish();
										}
								}
						});
					}

				} else if (url.contains("buyTicket")) {
//					if (mGroupInfoEntity == null
//							&& !mGroupInfoEntity.getId().equals(id)) {
//						ToastUtil.showShort(getApplicationContext(),
//								"mGroupInfoEntity is null");
//					} else {
//						intent.setClass(getApplicationContext(),
//								ChatActivity.class);
//						if (!TextUtils.isEmpty(message)) {
//							intent.putExtra("NOTE", message);
//						}
//						intent.putExtra(ChatActivity.KEY_PARAME,
//								mGroupInfoEntity);
//						startActivity(intent);
//					}
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
