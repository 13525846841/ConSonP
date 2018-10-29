package com.yksj.consultation.comm;

import java.util.HashMap;

import com.yksj.healthtalk.entity.BaseInfoEntity;

public class BaseViewPagerActivtiy extends BaseFragmentActivity {
	/**
	 * cacheKeys的作用:
	 * 当VIewpager中的不同Fragment有相同的数据时,当我们改变其中一个fragment的数据
	 * 另外的Fragment并不会改变,因为他并没有刷新数据,因此这里写一个HashMap来存放fragemnt中改变的内容
	 * 当其他的Fragment变得可见是将之对比这里的数据,存在这些数据则将之改变
	 */
	public HashMap<String, BaseInfoEntity> cacheKeys=new HashMap<String,BaseInfoEntity>();
}
