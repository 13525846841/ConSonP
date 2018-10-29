package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 支付宝界面(六一健康)
 * @author Administrator
 * HttpRestClient.doHttpConsultationWalletPay(consultationId, "",2, new AyncHander("Alipay"));
 */
public class ConsultationAliPayActivity extends BaseFragmentActivity implements
		OnClickListener {

	private WebView wv;
	private String consultationId;
	private String couponId;
	private String message;// 提示语句
	private Boolean isFirstLoad = true;

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
		if (intent.hasExtra("conId")) {//得到会诊id
			consultationId = intent.getStringExtra("conId");
			couponId = intent.getStringExtra("couponId");
		}
		titleTextV.setText("订单支付");
		if (TextUtils.isEmpty(consultationId)) {
			ToastUtil.showShort(getApplicationContext(), "数据加载出错");
			return;
		}
		// 设置WebView属性，能够执行JavaScript脚本
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(Configs.WEB_IP+ "/DuoMeiHealth/ConsultationBuyServlet?OPTION=2&COUPONSID="+couponId+"&CONSULTATIONID=" + consultationId+
				"&CUSTID="+SmartFoxClient.getLoginUserId());
		MyWebViewClient myWebView = new MyWebViewClient();
		wv.setWebViewClient(myWebView);
	}

	private void initWidget() {
		initTitle();
//		LoginServiceManeger.instance().login();
//		CoreService.actionLogin(this);
		wv = (WebView) findViewById(R.id.wv);
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("订单支付");
	}

	@Override
	public void onClick(View v) {
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
			//healthchat2://com.dummyvision?type=healthConsultation
			if (url.contains("type=healthConsultation")) {
//				Intent intent=new Intent(ConsultationAliPayActivity.this,AtyConsultServer.class);
//				startActivity(intent);
				setResult(RESULT_OK);
				ConsultationAliPayActivity.this.finish();
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

}
