package com.yksj.consultation.comm;

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
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 支付宝界面(话题)
 * @author Administrator
 * 
 */
public class SalonPayActivity extends BaseFragmentActivity implements
		OnClickListener {

	private WebView wv;
	private String payId;// 订单号
	private String message;// 提示语句
	private Boolean isFirstLoad = true;
	private Object object;// 沙龙,人
	private CustomerInfoEntity mCustomerInfoEntity;
	private GroupInfoEntity mGroupInfoEntity;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.salon_introduction);
		initWidget();
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent.hasExtra("payId")) {
			payId = intent.getStringExtra("payId");
		}
		if (intent.hasExtra("send_code")) {
			message = intent.getStringExtra("send_code");
			;
		}
		// intent.putExtra("entity", mInfoEntity);
		if (intent.hasExtra("entity")) {
			object = intent.getParcelableExtra("entity");
			if (object instanceof CustomerInfoEntity) {
				mCustomerInfoEntity = (CustomerInfoEntity) object;
			} else if (object instanceof GroupInfoEntity) {
				mGroupInfoEntity = (GroupInfoEntity) object;
			}
		}

		titleTextV.setText("订单支付");
		if (TextUtils.isEmpty(payId)) {
			ToastUtil.showShort(getApplicationContext(), "payId is null");
			return;
		}
		// 设置WebView属性，能够执行JavaScript脚本
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("http://" + Configs.SOCKET_IP
				+ ":8080/DuoMeiHealth/servlet/Trade?WIDout_trade_no=" + payId);
		MyWebViewClient myWebView = new MyWebViewClient();
		wv.setWebViewClient(myWebView);
	}

	private void initWidget() {
		initTitle();
		wv = (WebView) findViewById(R.id.wv);
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("订单支付");
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

	private class MyWebViewClient extends WebViewClient {
		private LodingFragmentDialog dialog;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// healthchat://com.dummyvision?type=guahao&customerId= 社交
			// type="buyTicket"&customerId= 沙龙
			if (url.contains("healthchat2://com.dummyvision")) {
				Intent intent = new Intent();
				String id = url.substring(url.lastIndexOf("=") + 1);
				if (url.contains("guahao")) {
					if (mCustomerInfoEntity == null
							&& !mCustomerInfoEntity.getId().equals(id)) {
						ToastUtil.showShort(getApplicationContext(),
								"mCustomerInfoEntity is null");
					} else {
						FriendHttpUtil.chatFromPerson(SalonPayActivity.this, mCustomerInfoEntity);
					}

				} else if (url.contains("buyTicket")) {
					if (mGroupInfoEntity == null
							&& !mGroupInfoEntity.getId().equals(id)) {
						ToastUtil.showShort(getApplicationContext(),
								"mGroupInfoEntity is null");
					} else {
						intent.setClass(getApplicationContext(),
								ChatActivity.class);
						if (!TextUtils.isEmpty(message)) {
							intent.putExtra("NOTE", message);
						}
						intent.putExtra(ChatActivity.KEY_PARAME,
								mGroupInfoEntity);
						startActivity(intent);
					}
				}
				finish();
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
