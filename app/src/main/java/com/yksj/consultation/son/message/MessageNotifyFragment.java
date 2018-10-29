package com.yksj.consultation.son.message;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.services.MessagePushService;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 底部消息通知
 * 
 * @author jack_tang
 * 
 */
public class MessageNotifyFragment extends RootFragment implements OnClickListener {

	private long showTime;// 切换时间
	private int TYPE = 1;// 2 表示消息厅 1 医患对话
	private int noReadCount = 0;
	final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context, Intent intent) {
			String action = intent.getAction();
			handler.removeMessages(0);
			if (CoreService.ACTION_CONTENT_MESSAGE.equals(action) || MessagePushService.ACTION_MESSAGE.equals(action)) {

				loadMsgCount();

				// 如果有unread(未读消息)的时候,只显示未读消息,不切换了
				if (noReadCount != 0) {
					TYPE = 2;
					mMesgTextV.setText("未读消息 (" + noReadCount + ")");
					mMesgTextV.setCompoundDrawablesWithIntrinsicBounds(mNewMesgCount, null, null, null);
					return;
				}

				if (SmartFoxClient.getLoginUserInfo().isDoctor())
					return;
				String content = intent.getStringExtra("content");
				mMesgTextV.setText(content);
				mMesgTextV.startAnimation(mAnimation);
				if (intent.hasExtra("type")) {
					TYPE = 1;
					if (showTime != 5000)
						showTime = showTime * 1000;
					handler.sendEmptyMessageDelayed(0, showTime);
					mMesgTextV.setCompoundDrawablesWithIntrinsicBounds(mReceiveText, null, null, null);
				} else {
					TYPE = 2;
					mMesgTextV.setText("未读消息 (" + noReadCount + ")");
					mMesgTextV.setCompoundDrawablesWithIntrinsicBounds(mNewMesgCount, null, null, null);
				}
			}
		};
	};

	TextView mMesgTextV;
	TranslateAnimation mAnimation;
	private Drawable mMsgCount;// 没有消息
	private Drawable mNewMesgCount;// 新消息
	private Drawable mReceiveText;// 消息广播
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			messageCount();
		};
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mMsgCount = getResources().getDrawable(R.drawable.xinxi);
		mNewMesgCount = getResources().getDrawable(R.drawable.new_xinxi);
		mReceiveText = getResources().getDrawable(R.drawable.global_system_icon);
		mAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0f);
		mAnimation.setDuration(1000);
		mAnimation.setRepeatCount(0);
		mAnimation.setRepeatMode(Animation.REVERSE);
		showTime = Long.valueOf(HTalkApplication.getAppData().messageShowTime);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_message_fragment_layout, null);
		mMesgTextV = (TextView) view.findViewById(R.id.message);
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadMsgCount();

	}

	/**
	 * 加载离线消息
	 */
	// private synchronized void onLoadOffMessge() {
	// HttpRestClient.doHttpLoadOffMessage(SmartFoxClient.getLoginUserId(),
	// new ObjectHttpResponseHandler() {
	// @Override
	// public Object onParseResponse(String content) {
	// if(!SmartControlClient.getControlClient().isLogined()) return null;
	// JsonParseUtils.parseOffMessage(content);
	//
	// JSONObject object = new JSONObject(content);
	// JSONArray jsonArray = object.getJSONArray("messages");
	//
	// int size = jsonArray.length();
	// //
	// //
	// // int size = mApplication.getNoReadMesgSize();
	// // if(size > 0){
	// // mApplication.showNotify("未读消息("+size+")条");
	// // sendContentMesgBroad("未读消息("+size+")条");
	// // }
	// return null;
	// }
	// public void onSuccess(Object response) {
	// LogUtil.d(TAG, "=====加载离线消息 成功====");
	// };
	// public void onFinish() {
	// LogUtil.d(TAG, "=====加载离线消息 结束====");
	// };
	// public void onFailure(Throwable error) {
	// if(mControlClient.getLoginState() ==2)
	// onLoadOffMessge();
	// LogUtil.d(TAG, "=====加载离线消息 失败====");
	// if(error!=null)
	// LogUtil.d(TAG, "=====加载离线消息 失败====原因"+error.toString());
	// };
	// });
	// }



//	private synchronized void loadMsgCount() {
//		HttpRestClient.doHttpLoadOffMessage(SmartFoxClient.getLoginUserId(), new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(String content) {
//				super.onSuccess(content);
//				try {
//
//					if (!SmartControlClient.getControlClient().isLogined())
//						;
//					JsonParseUtils.parseOffMessage(content);
//
//					JSONObject object = new JSONObject(content);
//					JSONArray jsonArray = object.getJSONArray("messages");
//					noReadCount = jsonArray.length();
//					messageCount();
//				} catch (Exception e) {
//				}
//			}
//		});
//
//	}

	/**
	 * 加载离线消息条数,李亮新接口
	 */
	private synchronized void loadMsgCount() {
		RequestParams params=new RequestParams();
		params.put("TYPE","findMessageList");
		params.put("MYCUSTOMERID",SmartFoxClient.getLoginUserId());
		HttpRestClient.doHttpFRIENDSINFOSET(params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {

					if (!SmartControlClient.getControlClient().isLogined())
						;
//					JsonParseUtils.parseOffMessage(content);

					JSONObject object = new JSONObject(content);
					JSONArray jsonArray = object.getJSONArray("findMessageList");
					noReadCount=0;
					for(int i=0;i<jsonArray.length();i++){
						noReadCount+=jsonArray.getJSONObject(i).optInt("NUMS");
					}
					messageCount();
				} catch (Exception e) {
				}
			}
		});

	}

	private void messageCount() {
		if (noReadCount != 0) {
			mMesgTextV.setText("未读消息 (" + noReadCount + ")");
			mMesgTextV.setCompoundDrawablesWithIntrinsicBounds(mNewMesgCount, null, null, null);
		} else {
			mMesgTextV.setText("我的消息");
			mMesgTextV.setCompoundDrawablesWithIntrinsicBounds(mMsgCount, null, null, null);
		}

		TYPE = 2;
	}

	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CoreService.ACTION_CONTENT_MESSAGE);
		filter.addAction(MessagePushService.ACTION_MESSAGE);
		getActivity().registerReceiver(mReceiver, filter);
	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		if (!SmartFoxClient.getLoginUserInfo().isDoctor()) {
			intent = new Intent(getActivity(), MessageNotifyActivity.class);
			startActivity(intent);
		}
		// if(TYPE==2){
		// intent = new Intent(getActivity(),MessageNotifyActivity.class);
		// startActivity(intent);
		// }else{//医患对话
		// intent = new Intent(getActivity(),MessageNotifyActivity.class);
		// intent.putExtra("type",1);
		// startActivity(intent);
		// }
		// intent = new Intent(getActivity(),MessageHistoryFragment.class);
		// startActivity(intent);
	}
}
