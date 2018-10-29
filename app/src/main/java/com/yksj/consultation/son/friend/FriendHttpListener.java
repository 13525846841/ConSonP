package com.yksj.consultation.son.friend;

import java.util.ArrayList;

import com.yksj.healthtalk.entity.CustomerInfoEntity;

public interface FriendHttpListener{
	 public void responseSuccess(int  type, int param2   ,ArrayList<CustomerInfoEntity> c);
	 public void responseError(int  type, int param2 ,String content);
}