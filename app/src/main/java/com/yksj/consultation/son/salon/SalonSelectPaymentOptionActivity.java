package com.yksj.consultation.son.salon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.unionpay.UPPayAssistEx;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SalonPayActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.WalletPayFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.WalletPayFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.setting.SettingPhoneBound;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.ToastUtil;
/**
 * 沙龙选择支付页面
 * @author jack_tang
 *
 */
public class SalonSelectPaymentOptionActivity extends BaseFragmentActivity implements OnClickSureBtnListener, OnClickListener{
	private TextView mWalletMoney;
	private GroupInfoEntity mGroupInfoEntity;
	 private static final int PLUGIN_NOT_INSTALLED = -1;
	    private static final int PLUGIN_NEED_UPGRADE = 2;

	    /*****************************************************************
	     * mMode参数解释：
	     *      "00" - 启动银联正式环境
	     *      "01" - 连接银联测试环境
	     *****************************************************************/
	    private String mMode = "00";
		private String payId;
		private String type="";
	    private static final String TN_URL_01 = "http://222.66.233.198:8080/sim/gettn";
		
		public interface OnBuyTicketHandlerListener{
			/**
			 * @param state 1 成功,0多美币不足,-1失败
			 * @param entity
			 */
			void onTicketHandler(String state,GroupInfoEntity entity);
		}
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.select_payment_option);
		initWidget();
		initDate();
	}

	private void initDate() {
		if (getIntent().hasExtra("type")) {//没有订单 
			type = getIntent().getExtras().getString("type");
		}else {
			payId =  getIntent().getStringExtra("payId");
//			send_code = getIntent().getStringExtra("send_code");
		}
		if (getIntent().hasExtra("entity")) {
			mGroupInfoEntity = (GroupInfoEntity) getIntent().getExtras().get("entity");
		}
	}

	@Override
	protected void onStart() {
		HttpRestClient.doHttpGetQianBao(SmartFoxClient.getLoginUserId(), new AyncHander("WalletBalance"));
		super.onStart();
	}
	private void initWidget() {
		initTitle();
		titleTextV.setText("选择支付方式");
		titleLeftBtn.setOnClickListener(this);
		mWalletMoney = (TextView) findViewById(R.id.wallet_money);
		findViewById(R.id.onclick_alipay).setOnClickListener(this);
		findViewById(R.id.onclick_unionpay).setOnClickListener(this);
	}
	
	/**
	 * 支付宝支付
	 * @param v
	 */
	public void onClickAlipay(View v){
		if (getIntent().hasExtra("payId") && !getIntent().getExtras().getString("payId").equals("")) {
			Intent intent = new Intent(getApplicationContext(),SalonPayActivity.class);
			intent.putExtra("payId", getIntent().getStringExtra("payId"));
			if (mGroupInfoEntity != null) {
				intent.putExtra("entity",mGroupInfoEntity);
			}
			intent.putExtra("send_code",getIntent().getStringExtra("send_code"));// 提示语句.putString("send_code",										// response.getString("send_code"));//提示语句
			startActivity(intent);
		}else {
			HttpRestClient.doHttpBuyTicket(mGroupInfoEntity.getId(),SmartFoxClient.getLoginUserId(),type,"10",new AyncHander("Alipay"));
		}
	}
	
	/**
	 * 银联支付
	 * @param v
	 */
	public void onClickUnionpay(View v){
//		HttpRestClient.doHttpGetUnionPay(url, new AyncHander("Unionpay"));
		HttpRestClient.doHttpUnionBuyTicket(mGroupInfoEntity.getId(),SmartFoxClient.getLoginUserId(),type,"20",new AyncHander("Unionpay"));
	}
	
	/**
	 * 钱包
	 * @param v
	 */
	public void onClickWallet(View v){
		String poneNumber = SmartFoxClient.getLoginUserInfo().getPoneNumber().trim();
		if(TextUtils.isEmpty(poneNumber)){
			DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(),"使用钱包支付,需绑定手机并设置支付密码,你目前未绑定手机", "稍后再说", "现在绑定", new OnDilaogClickListener() {
				@Override
				public void onDismiss(DialogFragment fragment) {
				}
				@Override
				public void onClick(DialogFragment fragment, View v) {
					Intent intent=new Intent(SalonSelectPaymentOptionActivity.this,SettingPhoneBound.class);
					startActivity(intent);
				}
			});
		}else{
			WalletPayFragmentDialog.show(getSupportFragmentManager(), "输入支付密码","");
		}
	}
	
	class AyncHander extends AsyncHttpResponseHandler{
		private String type;
		
		public AyncHander(String string) {
			super(SalonSelectPaymentOptionActivity.this);
			this.type = string;
		}
		
		@Override
		public void onSuccess(int statusCode, String content) {
			try {
				JSONObject response = JSON.parseObject(content);
				if (response.containsKey("error_message")) {
					SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),response.getString("error_message"));
					return;
				}else if (type.equals("WalletBalance")) {  
//					{"Balance":"0"}
					if (response.containsKey("Balance")) {
						findViewById(R.id.wallet_lin).setVisibility(View.VISIBLE);
						mWalletMoney.setText("余额:"+response.getString("Balance")+"元");
					}
				}else if(type.equals("Alipay")){
					String code = "10002";
					try {
						code = response.getString("code");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if("10003".equals(code)){
						ToastUtil.showShort(getApplicationContext(), "超出话题所要求最大人数限制");
//						SmartFoxClient.getSmartFoxClient().getCustomerInfoEntity().setMoney(SmartFoxClient.getSmartFoxClient().getCustomerInfoEntity().getMoney() - Integer.valueOf(mony));
//						if(mListener != null)mListener.onTicketHandler("1",mInfoEntity);
					}else if("10002".equals(code)){//失败
							ToastUtil.showShort(getApplicationContext(), "服务器出错");
					}else if("10001".equals(code)){//成功
//						{"payId":"130916005122496","ticPrice":1,"code":"10001"}
						if (response.containsKey("payId")) {
							Intent intent = new Intent(getApplicationContext(),SalonPayActivity.class);
							try {
								intent.putExtra("payId", response.getString("payId"));
								intent.putExtra("entity", mGroupInfoEntity);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							startActivity(intent);
						}else {
								Intent intent1 = new Intent();
								intent1.putExtra(ChatActivity.KEY_PARAME, mGroupInfoEntity);
								intent1.setClass(getApplicationContext(), ChatActivity.class);
								startActivity(intent1);
						}
					}
				}else if(response.containsKey("tn")){
//					{"PAY_ID":"1140227007141885","tn":"201402271420460080192"}
					  /************************************************* 
		             * 
		             *  步骤2：通过银联工具类启动支付插件 
		             *  
		             ************************************************/
		            // mMode参数解释：
		            // 0 - 启动银联正式环境
		            // 1 - 连接银联测试环境
		            int ret = UPPayAssistEx.startPay(SalonSelectPaymentOptionActivity.this, null, null, response.getString("tn"), mMode);
		            if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
		                // 需要重新安装控件
		                AlertDialog.Builder builder = new AlertDialog.Builder(SalonSelectPaymentOptionActivity.this);
		                builder.setTitle("提示");
		                builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

		                builder.setNegativeButton("确定",
		                        new DialogInterface.OnClickListener() {
		                            @Override
		                            public void onClick(DialogInterface dialog,
		                                    int which) {
		                                dialog.dismiss();
		                                UPPayAssistEx.installUPPayPlugin(SalonSelectPaymentOptionActivity.this);
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
				}else if(type.equals("Wallet")){
					if (!response.containsKey("error_message")) {
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "支付成功");
						HttpRestClient.doHttpGetQianBao(SmartFoxClient.getLoginUserId(), new AyncHander("WalletBalance"));
					}
				}
			} catch (Exception e) {
			}
			super.onSuccess(statusCode, content);
		}
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /************************************************* 
         * 
         *  步骤3：处理银联手机支付控件返回的支付结果 
         *  
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  msg);
    }

	@Override
	public void onClickSureHander(String pwd) {
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showShort(getApplicationContext(), "密码不能为空");
		}else {
			HttpRestClient.doHttpWalletBuyTicket(MD5Utils.getMD5(pwd),mGroupInfoEntity.getId(),SmartFoxClient.getLoginUserId(),type,"30",new AyncHander("Wallet"));
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.onclick_alipay://支付宝
			onClickAlipay(v);
			break;
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.onclick_unionpay://银联
			onClickUnionpay(v);
			break;
		}
		
	}
}
