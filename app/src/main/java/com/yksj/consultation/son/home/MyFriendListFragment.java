package com.yksj.consultation.son.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.CommonAdapter;
import com.yksj.consultation.comm.CommonViewHolder;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.services.MessagePushService;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.PersonInfoUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author 个人中心 病友 listFragment
 */
@SuppressLint("ValidFragment")
public class MyFriendListFragment extends RootFragment implements OnRefreshListener2<ListView>, OnItemClickListener{
	
	private PullToRefreshListView mRefreshListView;
	private final int PAGENUM = 20;
	private int PAGESIZE = 1;
	private CommonAdapter<CustomerInfoEntity> mAdapter;
	private int type = 1;//1 关注   2 粉丝   3黑名单
	private ImageLoader instance;
	private CustomerInfoEntity cacheInfoEntity;
	private ImageView mAttiention;//关注的按钮
	private RelativeLayout loadFailLayout;//没有加载导数据的布局
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MessagePushService.ACTION_COLLECT_FRIEND)) {
				LodingFragmentDialog.dismiss(getFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(getActivity(), R.string.groupNewFail);
				} else if (result.equals("-1")) {
					ToastUtil.showShort(getActivity(),R.string.against__blacklist_operations);
				} else {
					if(cacheInfoEntity==null)return ;
					if(type==1 || type ==3){//从关注  和 黑名单中移除
						mAdapter.remove(cacheInfoEntity);
						return ;
					}else{//粉丝
						FriendHttpUtil.requestAttentionTofriendsResult(getActivity(), cacheInfoEntity);
						mAdapter.notifyDataSetChanged();
					}
					//0  表示没有关系
//					if(cacheInfoEntity.getIsAttentionFriend() ==0 || cacheInfoEntity.getIsAttentionFriend() ==2){
//						mAttiention.setText("关注");
//						mAttiention.setBackgroundResource(R.drawable.btn_follow_bg_2x);
//					}else{
//						mAttiention.setText("取消关注");
//						mAttiention.setBackgroundResource(R.drawable.btn_unfolloe_bg_2x);
//					}
				}
				
			} else if (action.equals(MessagePushService.ACTION_MESSAGE)) {//消息
//				if (intent.hasExtra("senderId")) {
//					mAdapter.notifyDataSetChanged();
//				}
			}else if(CoreService.ACTION_COMMONT_CONTENT.equals(intent.getAction())){
				DoubleBtnFragmentDialog.showDefault(getChildFragmentManager(),  "亲，您的好评是对我们最大的鼓励~", "下次吧", "鼓励一下",new OnDilaogClickListener() {
					@Override
					public void onDismiss(DialogFragment fragment) {
						SharePreUtils.updateCommentGoodDay();
					}
					@Override
					public void onClick(DialogFragment fragment, View v) {
						SharePreUtils.updateCommentGood();
						SystemUtils.commentGood(getActivity());
					}
				});
			}
		}
	};
	public MyFriendListFragment(int i) {
		this.type=i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v= inflater.inflate(R.layout.my_friend_list_fragment_layout,null);
		mRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_listview);
		loadFailLayout=(RelativeLayout) v.findViewById(R.id.load_faile_layout);
		ListView mListView = mRefreshListView.getRefreshableView();
		 mListView.setOnItemClickListener(this);
		 mRefreshListView.setOnRefreshListener(this);
		 mAdapter = new CommonAdapter<CustomerInfoEntity>(mActivity) {
				@Override
				public void onBoundView(CommonViewHolder helper, final CustomerInfoEntity item) {
					ImageView mView = helper.getView(R.id.chat_head);
					instance.displayImage(item.getSex(), item.getNormalHeadIcon(), mView);
					mView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							PersonInfoUtil.choiceActivity(item.getId(), mActivity,String.valueOf(item.getRoldid()) );
						}
					});
					helper.setText(R.id.my_friend_item_name, item.getName());
					helper.setText(R.id.desc, item.getDescription());
					ImageView imgSex=(ImageView) helper.getView(R.id.my_friend_item_sex);
					LevelListDrawable listDrawable = (LevelListDrawable)imgSex.getDrawable();
					listDrawable.setLevel(Integer.valueOf(item.getSex()));
					ImageView attention = helper.getView(R.id.attiention);
					LevelListDrawable relationDrawable = (LevelListDrawable)attention.getDrawable();
					relationDrawable.setLevel(item.getAttentionNumber());
					attention.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							changeRelation(item,v);
						}
					});
				}
				
				@Override
				public int viewLayout() {
					return R.layout.my_firend_list_item;
				}
			};
		mListView.addFooterView(inflater.inflate(R.layout.list_line_layout, null),null,false);
		mListView.setAdapter(mAdapter);
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pagesaize", PAGESIZE);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = ImageLoader.getInstance();
	}
	
	@Override
	public void onStop() {
		getActivity().unregisterReceiver(receiver);
		super.onStop();
	}

	@Override
	public void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessagePushService.ACTION_COLLECT_FRIEND);
//		filter.addAction(SFSEvent.CONNECTION_LOST);
		filter.addAction(MessagePushService.ACTION_MESSAGE);
		filter.addAction(CoreService.ACTION_COMMONT_CONTENT);
		mActivity.registerReceiver(receiver, filter);
		PAGESIZE=1;
		initData();//从新加载一下数据
		super.onStart();
	}
	
	private void initData() {
		RequestParams params = new RequestParams();
		switch (type) {
		case 1://我关注的
			params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			params.put("PAGESIZE",String.valueOf(PAGESIZE));
			params.put("PAGENUM", String.valueOf(PAGENUM));
			params.put("TYPE", "findMyFocusFriends");
			params.put("NAME", "");
			HttpRestClient.doHttpFINDMYFOCUSFRIENDS(params,new ResponData());
			break;
		case 2://我的粉丝
			params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			params.put("PAGENUM",String.valueOf(PAGENUM));
			params.put("PAGESIZE", String.valueOf(PAGESIZE));
			HttpRestClient.doHttpFINDMYFRIENDS32(params,new ResponData());
			break;
		case 3://我的黑名单
			params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			params.put("PAGENUM",String.valueOf(PAGENUM));
			params.put("PAGESIZE", String.valueOf(PAGESIZE));
			HttpRestClient.doHttpFINDMYBLACK32(params,new ResponData());
			break;
		}
	}

	/**
	 * 改变关系
	 * @param item  实体
	 * @param v  button的view
	 */
	private void changeRelation(CustomerInfoEntity item,View v) {
		mAttiention = (ImageView) v;
		cacheInfoEntity = FriendHttpUtil.requestAttentionTofriends(getActivity(), null, item);
	}
	
	/**
	 * http 响应
	 * @author jack_tang
	 *
	 */
	class ResponData extends ObjectHttpResponseHandler{
		@Override
		public Object onParseResponse(String content) {
			return FriendHttpUtil.jsonAnalysisFriendEntity(content,true);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			mRefreshListView.setRefreshing();
		}
		
		@Override
		public void onSuccess(int statusCode, Object response) {
			super.onSuccess(statusCode, response);
			if (response != null && response instanceof ArrayList) {
				ArrayList<CustomerInfoEntity> entities = (ArrayList<CustomerInfoEntity>) response;
				if (entities.size() != 0) {//有数据
					if(PAGESIZE==1){//第一次加载且加载到数据
						mAdapter.removeAll();
						loadFailLayout.setVisibility(View.GONE);
						mRefreshListView.setVisibility(View.VISIBLE);
					}
					PAGESIZE++;
					mAdapter.addAll(entities);
				} else {//没有加载到数据
					if(PAGESIZE==1){//第一次加载且加载到数据
						loadFailLayout.setVisibility(View.VISIBLE);
						mRefreshListView.setVisibility(View.GONE);
					}
				}
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();
			mRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CustomerInfoEntity item = mAdapter.getItem(position-1);
		FriendHttpUtil.chatFromPerson(mActivity, item);
	}

	//下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		PAGESIZE=1;
		initData();
	}

	//上拉加载
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		initData();
	}
	
}
