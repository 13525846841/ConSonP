/**
 * 
 */
package com.yksj.consultation.son.consultation;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 会诊服务Adapter
 * @author zheng
 *
 */
public class PConsultServiceAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragList;
	public PConsultServiceAdapter(FragmentManager fm,List<Fragment> fragList) {
		super(fm);
		this.fragList=fragList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragList.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void addList(List<Fragment> fragList){
		this.fragList=fragList;
	}
}
