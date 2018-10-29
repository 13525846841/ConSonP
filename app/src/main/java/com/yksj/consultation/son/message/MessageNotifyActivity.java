package com.yksj.consultation.son.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
/**
 * 主页 底部  我的消息  患者端
 * @author jack_tang
 *
 */
public class MessageNotifyActivity extends BaseFragmentActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.message_notify_activity_layout);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("我的消息");
		Fragment fragment = new MessageHistoryFragment();
		FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
		beginTransaction.replace(R.id.fragment, fragment).commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		}
	}
	
}
