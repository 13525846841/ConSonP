package com.yksj.consultation.infa;

import com.yksj.healthtalk.entity.GroupInfoEntity;

// 删除订单
public interface OnClickDeleteListener {
		void OnClickDelete(GroupInfoEntity entity, String orderId);
}