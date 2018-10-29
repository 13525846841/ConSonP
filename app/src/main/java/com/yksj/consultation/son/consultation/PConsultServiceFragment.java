/**
*
*/
package com.yksj.consultation.son.consultation;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;

/**
* 患者端会诊服务Fragment
* @author zheng
*
*/
public class PConsultServiceFragment extends RootFragment{

	private ViewPager mVP;
	private PConsultServiceAdapter csa;
	private List<Fragment> fragList;
	private RadioGroup mTopRadioGroup;
	private ConsultationServiceListFragment frag1,frag2,frag3;//,frag4,frag5,frag6,frag7;
	private Bundle bun1,bun2,bun3;//,bun4,bun5,bun6,bun7;

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.consultation_service_fragment,container, false);
		mVP = (ViewPager) view.findViewById(R.id.consultation_service_vp);
		initFrag();
		mVP.setAdapter(csa);
        mVP.setOffscreenPageLimit(0);
		mTopRadioGroup = (RadioGroup) view.findViewById(R.id.consultation_service_rg1);
		initFunction();
		return view;
	}

	private void initFrag(){
		fragList = new ArrayList<Fragment>();
		frag1=new ConsultationServiceListFragment();
		bun1=new Bundle();
		bun1.putInt("TYPE", 0);
		frag1.setArguments(bun1);
		fragList.add(frag1);

		frag2=new ConsultationServiceListFragment();
		bun2=new Bundle();
		bun2.putInt("TYPE", 1);
//		bun2.putInt("TYPE", 7);
		frag2.setArguments(bun2);
		fragList.add(frag2);

		frag3=new ConsultationServiceListFragment();
		bun3=new Bundle();
		bun3.putInt("TYPE", 2);
//		bun3.putInt("TYPE", 8);
		frag3.setArguments(bun3);
		fragList.add(frag3);

//		frag4=new ConsultationServiceListFragment();
//		bun4=new Bundle();
//		bun4.putInt("TYPE", 3);
//		frag4.setArguments(bun4);
//		fragList.add(frag4);
//
//		frag5=new ConsultationServiceListFragment();
//		bun5=new Bundle();
//		bun5.putInt("TYPE", 4);
//		frag5.setArguments(bun5);
//		fragList.add(frag5);
//
//		frag6=new ConsultationServiceListFragment();
//		bun6=new Bundle();
//		bun6.putInt("TYPE", 5);
//		frag6.setArguments(bun6);
//		fragList.add(frag6);
//
//		frag7=new ConsultationServiceListFragment();
//		bun7=new Bundle();
//		bun7.putInt("TYPE", 6);
//		frag7.setArguments(bun7);
//		fragList.add(frag7);
		csa = new PConsultServiceAdapter(getFragmentManager(), fragList);
	}
	private void initFunction(){
		mVP.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					((RadioButton) mTopRadioGroup.getChildAt(0)).setChecked(true);
					break;
				case 1:
					((RadioButton) mTopRadioGroup.getChildAt(1)).setChecked(true);
					break;
				case 2:
					((RadioButton) mTopRadioGroup.getChildAt(2)).setChecked(true);
					break;
//				case 3:
//					((RadioButton) mTopRadioGroup.getChildAt(3)).setChecked(true);
//					break;
//				case 4:
//					((RadioButton) mTopRadioGroup.getChildAt(4)).setChecked(true);
//					break;
//				case 5:
//					((RadioButton) mTopRadioGroup.getChildAt(5)).setChecked(true);
//					break;
//				case 6:
//					((RadioButton) mTopRadioGroup.getChildAt(6)).setChecked(true);
//					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mTopRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for(int i=0;i<mTopRadioGroup.getChildCount();i++){
					if(mTopRadioGroup.getChildAt(i).getId()==checkedId){
						if(mVP.getCurrentItem()!=i){
							mVP.setCurrentItem(i,false);
						}
						break;
					}
				}
			}
		});
	}
}
