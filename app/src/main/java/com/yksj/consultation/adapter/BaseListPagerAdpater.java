package com.yksj.consultation.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yksj.consultation.son.friend.FindFriendListFragment;

/**
 * 找病友列表ViewPager的PagerAdapter
 * 
 * @author lmk
 * 
 */
public class BaseListPagerAdpater extends FragmentStatePagerAdapter {
	FragmentManager fm;
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	public FindFriendListFragment activeFragment;
	public BaseListPagerAdpater(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}
	
	public void onBoundFragment(ArrayList<Fragment> fragments){
		this.fragments.clear();
		this.fragments.addAll(fragments);
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}


	@Override
	public int getCount() {
		return fragments.size();
	}


}
