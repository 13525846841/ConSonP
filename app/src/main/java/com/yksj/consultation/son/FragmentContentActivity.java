package com.yksj.consultation.son;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.setting.SettingMainUI;


/**
 * 
 * 设置,信息厅
 * @author jack_tang
 *
 */
public class FragmentContentActivity extends BaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_content_layout);
		
//		int type = getIntent().getIntExtra("type",0);
		Fragment fragment;
		fragment = new SettingMainUI();
		FragmentManager fg = getSupportFragmentManager();
		FragmentTransaction ft = fg.beginTransaction();
		ft.replace(R.id.fragmentcontent,fragment);
		ft.commit();
	}
}
