package com.yksj.consultation.son.wallet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.squareup.okhttp.Request;
import com.unionpay.UPPayAssistEx;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.PayBean;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


/**
 * 充值
 * 
 * @author Administrator
 * 
 */
public class TopUpActivity extends BaseFragmentActivity implements
		OnClickListener {

	private String money;
	private static final int PLUGIN_NOT_INSTALLED = -1;
	private static final int PLUGIN_NEED_UPGRADE = 2;

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private String mMode = "00";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.top_up);
		initWidget();
		initDate();
	}

	private void initDate() {
		money = getIntent().getStringExtra("money");
	}

	private void initWidget() {
		initTitle();
		titleTextV.setText("选择支付方式");
		titleLeftBtn.setOnClickListener(this);
	}

	// 支付宝
	public void onClickAlipay(View v) {
		onClickAliPay();
//		HttpRestClient.doHttpTopUp(money, "10", new AyncHander());
//		HttpRestClient.doHttpTopUp( new AyncHander());
	}

	// 银联
	public void onClickUnionpay(View v) {
		HttpRestClient.doHttpTopUp(money, "20", new AyncHander());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/**
	 * 支付宝支付
	 *
	 */
	public void onClickAliPay() {
//		if (isPaying)//正在支付
//			return;
//		isPaying = true;
		HashMap<String,String> map=new HashMap<>();
		map.put("OPTION","2");
		map.put("CUSTID",LoginServiceManeger.instance().getLoginUserId());
		map.put("MONEY","10");

		HttpRestClient.OKHttpFillMoney(map, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(String response) {
				BaseBean baseBean = JSONObject.parseObject(response, BaseBean.class);
				if ("1".equals(baseBean.code)) {//成功
					PayBean payBean = JSONObject.parseObject(response, PayBean.class);
//                RSA_PRIVATE = payBean.sign;
					payZFB(payBean.source, payBean.sign);
				}
				ToastUtil.showShort(TopUpActivity.this, baseBean.message);
			}
		}, this);
	}

	private void payZFB(String orderInfo, String sign) {
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
//        String sign2 = sign(orderInfo);
//        try {
//            /**
//             * 仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(TopUpActivity.this);
				// 调用支付接口，获取支付结果

				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	/**
	 * get the sign type we use. 获取签名方式
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	private static final int SDK_PAY_FLAG = 1;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(TopUpActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
						EventBus.getDefault().post(new MyEvent("refresh", 0));
						finish();
					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(TopUpActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(TopUpActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
						}
					}
					break;
				}
				default:
					break;
			}
		}

		;
	};
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	// class AyncHander extends JsonsfHttpResponseHandler{
	//
	// @Override
	// public void onSuccess(int statusCode, JSONObject response) {
	// if (response.containsKey("error_message")) {
	// SingleBtnFragmentDialog.show(getSupportFragmentManager(), "提示",
	// response.getString("error_message"), "确定");
	// }else if(response.containsKey("tn")){
	// // {"PAY_ID":"1140227007141885","tn":"201402271420460080192"}
	// /*************************************************
	// *
	// * 步骤2：通过银联工具类启动支付插件
	// *
	// ************************************************/
	// // mMode参数解释：
	// // 0 - 启动银联正式环境
	// // 1 - 连接银联测试环境
	// int ret = UPPayAssistEx.startPay(TopUpActivity.this, null, null,
	// response.getString("tn"), mMode);
	// if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
	// // 需要重新安装控件
	// AlertDialog.Builder builder = new
	// AlertDialog.Builder(TopUpActivity.this);
	// builder.setTitle("提示");
	// builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
	//
	// builder.setNegativeButton("确定",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// UPPayAssistEx.installUPPayPlugin(TopUpActivity.this);
	// }
	// });
	//
	// builder.setPositiveButton("取消",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	//
	// }
	// super.onSuccess(statusCode, response);
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// super.onFailure(error, content);
	// }
	//
	// @Override
	// public void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, String content) {
	// if(content.contains("支付宝")) {
	// Intent intent = new Intent(getApplicationContext(),PayActivity.class);
	// intent.putExtra("summary", content);
	// startActivity(intent);
	// finish();
	// }
	// super.onSuccess(statusCode, content);
	// }
	//
	// @Override
	// public void onFinish() {
	// // TODO Auto-generated method stub
	// super.onFinish();
	// }
	// }

	class AyncHander extends AsyncHttpResponseHandler {
		public AyncHander() {
			super(TopUpActivity.this);
		}
		@Override
		public void onSuccess(int statusCode, String content) {
			try {
				JSONObject response = JSON.parseObject(content);
				if (response.containsKey("error_message")) {
					SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
							 response.getString("error_message"));
				} else if (response.containsKey("tn")) {
					// {"PAY_ID":"1140227007141885","tn":"201402271420460080192"}
					/*************************************************
					 * 
					 * 步骤2：通过银联工具类启动支付插件
					 * 
					 ************************************************/
					// mMode参数解释：
					// 0 - 启动银联正式环境
					// 1 - 连接银联测试环境
					int ret = UPPayAssistEx.startPay(TopUpActivity.this, null,
							null, response.getString("tn"), mMode);
					if (ret == PLUGIN_NEED_UPGRADE
							|| ret == PLUGIN_NOT_INSTALLED) {
						// 需要重新安装控件
						AlertDialog.Builder builder = new AlertDialog.Builder(
								TopUpActivity.this);
						builder.setTitle("提示");
						builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

						builder.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										UPPayAssistEx
												.installUPPayPlugin(TopUpActivity.this);
									}
								});

						builder.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				}
			} catch (Exception e) {
				if (content.contains("支付宝")) {
					Intent intent = new Intent(getApplicationContext(),
							PayActivity.class);
					intent.putExtra("summary", content);
					startActivity(intent);
				}
			}
			super.onSuccess(statusCode, content);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), msg
				);
		super.onActivityResult(arg0, arg1, data);

	}

}
