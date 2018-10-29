package com.yksj.consultation.comm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
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
public abstract class RootListFragment extends RootFragment implements OnClickChildItemListener{
	private static CustomerInfoEntity cacheCustomerInfoEntity;
	private static GroupInfoEntity cacheGroupEntity;
	public boolean isVisiable=false;//是否可见,如果不可见即使接受到广播也不理睬
	private boolean isRequesting=false;//是否正在执行关注
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("RootListFragment", "----广播----"+"isVisiable----"+isVisiable+"------"+intent.getAction());
			if(!isVisiable)return;//不可见即使接受到广播也不理睬
			String action = intent.getAction();
			if (action.equals(MessagePushService.ACTION_COLLECT_FRIEND)) {//关注或者取消关注朋友
				isRequesting=false;
					LodingFragmentDialog.dismiss(getFragmentManager());
					String result = intent.getStringExtra("result");
					if (result.equals("0")) {
						ToastUtil.showShort(getActivity(), R.string.groupNewFail);
					} else if (result.equals("-1")) {
						ToastUtil.showShort(getActivity(),R.string.against__blacklist_operations);
					} else {
						FriendHttpUtil.requestAttentionTofriendsResult(getActivity(), cacheCustomerInfoEntity);
//						SimpleBaseAdapter<CustomerInfoEntity> tAdapter=(SimpleBaseAdapter<CustomerInfoEntity>) getAdapter();
//						for(int j=0;j<tAdapter.datas.size();j++){
//							if(tAdapter.datas.get(j).getId().equals(cacheCustomerInfoEntity.getId()))
//								tAdapter.datas.get(j).setIsAttentionFriend(cacheCustomerInfoEntity.getIsAttentionFriend());
//						}
//						tAdapter.notifyDataSetChanged();
						BaseFragmentActivity ac=(BaseFragmentActivity) getActivity();
//						ac.cacheKeys.put(cacheCustomerInfoEntity.getId(), cacheCustomerInfoEntity.getIsAttentionFriend());
						getAdapter().notifyDataSetChanged();
					}
			} else if (action.equals(MessagePushService.ACTION_MESSAGE)) {//消息
				if (intent.hasExtra("senderId")) {
//						adapter.notifyDataSetChanged();
					}
			}else if (action.equals(MessagePushService.ACTION_COLLECT_GROUP_NOT)) {//取消关注话题
				isRequesting=false;
				LodingFragmentDialog.dismiss(getFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(getActivity(), R.string.groupNewFail);
				} else {
					SalonHttpUtil.requestUnfollowToSalonResult(getActivity(),cacheGroupEntity);
//					BaseViewPagerActivtiy ac=(BaseViewPagerActivtiy) getActivity();
//					ac.cacheKeys.put(cacheGroupEntity.getId(),cacheGroupEntity);
					getAdapter().notifyDataSetChanged();
				}
			} else if (action.equals(MessagePushService.ACTION_COLLECT_GROUP)) {//关注话题
				isRequesting=false;
				LodingFragmentDialog.dismiss(getFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(getActivity(), R.string.groupNewFail);
				} else {
					SalonHttpUtil.requestAttentionToSalonResult(getActivity(),cacheGroupEntity);
//					SimpleBaseAdapter<GroupInfoEntity> tAdapter=(SimpleBaseAdapter<GroupInfoEntity>) getAdapter();
//					for(int j=0;j<tAdapter.datas.size();j++){
//						if(tAdapter.datas.get(j).getId().equals(cacheGroupEntity.getId()))
//							tAdapter.datas.get(j).setSalonAttention(cacheGroupEntity.isSalonAttention());
//					}
//					tAdapter.notifyDataSetChanged();
//					BaseViewPagerActivtiy ac=(BaseViewPagerActivtiy) getActivity();
//					ac.cacheKeys.put(cacheGroupEntity.getId(), cacheGroupEntity);
					getAdapter().notifyDataSetChanged();
				}
			}
		}
	};
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			isVisiable=true;
		}else{
			isVisiable=false;
		}
	}
	
//	private void registerBro(){
//		if(registered)return;//已经注册直接返回
//		registered=true;
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP_NOT);//话题的取消关注
//		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP);//加关注
//		filter.addAction(MessagePushService.ACTION_COLLECT_FRIEND);//取消关注/添加关注的时候
//		filter.addAction(MessagePushService.ACTION_MESSAGE);//当消息来的时候
//		filter.addAction(CoreService.ACTION_COMMONT_CONTENT);//弹出好评
//		getActivity().registerReceiver(receiver, filter);
//		Log.e("RootListFragment", "--------onStart----注册广播-----");
//	}


	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP_NOT);//话题的取消关注
		filter.addAction(MessagePushService.ACTION_COLLECT_GROUP);//加关注
		filter.addAction(MessagePushService.ACTION_COLLECT_FRIEND);//取消关注/添加关注的时候
		filter.addAction(MessagePushService.ACTION_MESSAGE);//当消息来的时候
		filter.addAction(CoreService.ACTION_COMMONT_CONTENT);//弹出好评
		getActivity().registerReceiver(receiver, filter);
		Log.e("RootListFragment", "--------onStart----注册广播-----");
		getAdapter().notifyDataSetChanged();
	}
	
	@Override
	public void onStop() {
		Log.e("RootListFragment", "--------onStop----取消注册广播-----");
		getActivity().unregisterReceiver(receiver);
		super.onStop();
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract ListView getListView();
	
	/**
	 * 
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
//			if(SmartFoxClient.getLoginUserId().equals(cusEntity.getId())){
//				
//			}
			PersonInfoUtil.choiceActivity(cusEntity.getId(), getActivity(), ""+cusEntity.getRoldid());
		}else if(topicName instanceof GroupInfoEntity){
			GroupInfoEntity groupEntity=(GroupInfoEntity) topicName;
			if (SmartFoxClient.getLoginUserId().equals(groupEntity.getCreateCustomerID())) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), ModifyTopicInfoUi.class);
//				intent.putExtra("topicId", groupEntity.getId());
//				startActivityForResult(intent, 11);
			} else {//看别人的话题
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), FriendCreateedTopicInfoUi.class);
//				intent.putExtra("id", groupEntity.getId());
//				startActivityForResult(intent, 10);
			}
		}
	}
	
	/**
	 * 当用户点击关注时操作的方法
	 * 点击后面是否关注按钮
	 * 	0 无关系 1 我关注的 2 黑名单
	 * 用户对话题的关注操作
	 */
	@Override
	public void onFollowClick(BaseInfoEntity entity, int position) {
		if(isRequesting)return;
		isRequesting=true;
		if(entity==null)return;
		if(entity instanceof CustomerInfoEntity){
			cacheCustomerInfoEntity= FriendHttpUtil.requestAttentionTofriends(getActivity(), null, (CustomerInfoEntity)entity);
		}else if(entity instanceof GroupInfoEntity){
			cacheGroupEntity = SalonHttpUtil.requestAttOrUnfollowToSalon(getActivity(),(GroupInfoEntity)entity);
		}
	}
	
	
	
}
