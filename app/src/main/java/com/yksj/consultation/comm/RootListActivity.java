package com.yksj.consultation.comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.entity.BaseInfoEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.services.MessagePushService;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.PersonInfoUtil;
import com.yksj.healthtalk.utils.SalonHttpUtil;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 抽象基类
 * 作用:1,朋友列表用
 * 			2,话题列表
 * @author Administrator
 *
 */
public abstract class RootListActivity extends BaseFragmentActivity implements OnClickChildItemListener{
	private GroupInfoEntity cacheGroupEntity;
	private CustomerInfoEntity cacheCustomerInfoEntity;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MessagePushService.ACTION_COLLECT_FRIEND)) {
					LodingFragmentDialog.dismiss(getSupportFragmentManager());
					String result = intent.getStringExtra("result");
					if (result.equals("0")) {
						ToastUtil.showShort(RootListActivity.this, R.string.groupNewFail);
					} else if (result.equals("-1")) {
						ToastUtil.showShort(RootListActivity.this,R.string.against__blacklist_operations);
					} else {
						FriendHttpUtil.requestAttentionTofriendsResult(RootListActivity.this, cacheCustomerInfoEntity);
						getAdapter().notifyDataSetChanged();
					}
			} else if (action.equals(MessagePushService.ACTION_MESSAGE)) {
				if (intent.hasExtra("senderId")) {
//						adapter.notifyDataSetChanged();
					}
			}else if (action.equals(MessagePushService.ACTION_COLLECT_GROUP_NOT)) {//取消关注话题
				LodingFragmentDialog.dismiss(getSupportFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(RootListActivity.this, R.string.groupNewFail);
				} else {
					SalonHttpUtil.requestUnfollowToSalonResult(RootListActivity.this,cacheGroupEntity);
					getAdapter().notifyDataSetChanged();
				}
			} else if (action.equals(MessagePushService.ACTION_COLLECT_GROUP)) {//关注话题
				LodingFragmentDialog.dismiss(getSupportFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(RootListActivity.this, R.string.groupNewFail);
				} else {
					SalonHttpUtil.requestAttentionToSalonResult(RootListActivity.this,cacheGroupEntity);
					getAdapter().notifyDataSetChanged();
				}
			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP_NOT);//话题的取消关注
		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP);//加关注
		filter.addAction(MessagePushService.ACTION_COLLECT_FRIEND);//取消关注/添加关注的时候
		filter.addAction(MessagePushService.ACTION_MESSAGE);//当消息来的时候
		filter.addAction(CoreService.ACTION_COMMONT_CONTENT);//弹出好评
		RootListActivity.this.registerReceiver(receiver, filter);
	}
	
	
	@Override
	protected void onStop() {
		RootListActivity.this.unregisterReceiver(receiver);
		super.onStop();
	}
	
	/**
	 * 得到ListView以操作
	 * @return
	 */
	public abstract ListView getListView();
	
	/**
	 * 得到Adapter以操作
	 * @return
	 */
	public abstract BaseAdapter getAdapter();
	
	/**
	 * 点击头像回调
	 */
	@Override
	public void onHeadIconClick(BaseInfoEntity topicName, int position) {
		if(topicName==null)return;
		if(topicName instanceof CustomerInfoEntity){
			CustomerInfoEntity cusEntity=(CustomerInfoEntity) topicName;
			PersonInfoUtil.choiceActivity(cusEntity.getId(), this, ""+cusEntity.getRoldid());
		}else if(topicName instanceof GroupInfoEntity){
			GroupInfoEntity groupEntity=(GroupInfoEntity) topicName;
			if (SmartFoxClient.getLoginUserId().equals(groupEntity.getCreateCustomerID())) {
//				Intent intent = new Intent();
////				intent.setClass(getActivity(), SalonReadSelf.class);
//				intent.setClass(this, ModifyTopicInfoUi.class);
//				intent.putExtra("topicId", groupEntity.getId());
//				startActivityForResult(intent, 11);
			} else {//看别人的话题
//				Intent intent = new Intent();
//				intent.setClass(this, FriendCreateedTopicInfoUi.class);
////				intent.setClass(getActivity(), CreateTopicInfoUI.class);
//				intent.putExtra("id", groupEntity.getId());
//				startActivityForResult(intent, 10);
			}
		}
	}
	
	/**
	 * 当用户点击关注时操作的方法
	 * 点击后面是否关注按钮
	 * 	0 无关系 1 我关注的 2 黑名单
	 * 话题
	 */
	@Override
	public void onFollowClick(BaseInfoEntity entity, int position) {
		if(entity==null)return;
		if(entity instanceof CustomerInfoEntity){
			cacheCustomerInfoEntity= FriendHttpUtil.requestAttentionTofriends(RootListActivity.this, null, (CustomerInfoEntity)entity);
		}else if(entity instanceof GroupInfoEntity){
			cacheGroupEntity = SalonHttpUtil.requestAttOrUnfollowToSalon(RootListActivity.this,(GroupInfoEntity)entity);
		}
	}
	
}
