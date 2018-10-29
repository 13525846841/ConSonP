/**
 * 
 */
package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 患者端会诊服务Fragment
 * @author zheng
 *
 */
public class PFgtConsultService extends RootFragment implements OnPageChangeListener{

			private int mScreenWidth;//屏幕总长度
			private ViewPager mViewPager;
			private List<Fragment> fragments;
			RadioGroup mTopRadioGroup;
			HorizontalScrollView  mScrollView;
			private ConsultationServiceListFragment frag1,frag2,frag3;
			private Bundle bun1,bun2,bun3;
			private PConsultServiceAdapter csa;
			private String[] strs={"待会诊","会诊中","已完成"};
			@Nullable
			public View onCreateView(LayoutInflater inflater,
					@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
				final View view = inflater.from(mActivity).inflate(R.layout.pfgt_con_service, null);
				DisplayMetrics dm = AppTools.getWindowSize(mActivity);
		mScreenWidth = dm.widthPixels;

		mTopRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_view11);
		mScrollView = (HorizontalScrollView) view.findViewById(R.id.h_scrollview11);
		mViewPager = (ViewPager)view.findViewById(R.id.mViewPager11);
		mViewPager.setOffscreenPageLimit(0);
		final int screenHalf =mScreenWidth/2;//屏幕宽度的一半
		mTopRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int scrollX = mScrollView.getScrollX();
				RadioButton rb = (RadioButton) view.findViewById(checkedId);

				int left = rb.getLeft();
				int leftScreen = left-scrollX;
				mScrollView.smoothScrollBy((leftScreen-screenHalf), 0);

				int i=  (Integer) rb.getTag();
				if(mViewPager.getCurrentItem() !=i){
					mViewPager.setCurrentItem(i,false);
				}
			}
		});
		initTabView(strs);
		return view;
	}
	
	private void initTabView(String[] strlist){
		RadioButton button;
		mTopRadioGroup.removeAllViews();//tab_view_common_radiobutton
		for (int i = 0; i < strlist.length; i++) {
			if(strlist == null)continue;
			button =(RadioButton) mActivity.getLayoutInflater().inflate(R.layout.tab_view_common_radiobutton, null);
			button.setText(strlist[i]);
			button.setTag(i);
			if(strlist.length-1 == i){
				button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			mTopRadioGroup.addView(button, mScreenWidth/3, RadioGroup.LayoutParams.MATCH_PARENT);
		}
		initFrag();
	}
	private void initFrag(){
		RadioButton button;
		fragments = new ArrayList<Fragment>();
		fragments.clear();
		frag1=new ConsultationServiceListFragment();
		bun1=new Bundle();
		bun1.putInt("TYPE", 1);
		frag1.setArguments(bun1);
		fragments.add(frag1);

		frag2=new ConsultationServiceListFragment();
		bun2=new Bundle();
		bun2.putInt("TYPE", 7);
		frag2.setArguments(bun2);
		fragments.add(frag2);

		frag3=new ConsultationServiceListFragment();
		bun3=new Bundle();
		bun3.putInt("TYPE", 8);
		frag3.setArguments(bun3);
		fragments.add(frag3);

//		frag4=new ConsultationServiceListFragment();
//		bun4=new Bundle();
//		bun4.putInt("TYPE", 3);
//		frag4.setArguments(bun4);
//		fragments.add(frag4);
//
//		frag5=new ConsultationServiceListFragment();
//		bun5=new Bundle();
//		bun5.putInt("TYPE", 4);
//		frag5.setArguments(bun5);
//		fragments.add(frag5);
//
//		frag6=new ConsultationServiceListFragment();
//		bun6=new Bundle();
//		bun6.putInt("TYPE", 5);
//		frag6.setArguments(bun6);
//		fragments.add(frag6);
//
//		frag7=new ConsultationServiceListFragment();
//		bun7=new Bundle();
//		bun7.putInt("TYPE", 6);
//		frag7.setArguments(bun7);
//		fragments.add(frag7);
		csa = new PConsultServiceAdapter(getFragmentManager(), fragments);
		mViewPager.setAdapter(csa);
		mViewPager.setOnPageChangeListener(this);
		button=	(RadioButton) mTopRadioGroup.getChildAt(0);
		button.setChecked(true);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		RadioButton button =(RadioButton) mTopRadioGroup.getChildAt(arg0);
		button.setChecked(true);
	}
}
