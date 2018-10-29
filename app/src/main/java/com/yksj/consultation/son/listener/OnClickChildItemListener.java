package com.yksj.consultation.son.listener;

import com.yksj.healthtalk.entity.BaseInfoEntity;

/**
 * 1.点击关注或者取消关注时的监听器
 * 2.点击头像时的监听
 */
public interface OnClickChildItemListener{
	void onHeadIconClick(BaseInfoEntity topicName,int position);
	void onFollowClick(BaseInfoEntity topicName,int position);
}