package com.yksj.consultation.son.friend;

import java.util.ArrayList;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.consultation.adapter.FindFriendListAdapter;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.RootListFragment;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.BaiduLocationManager;
import com.yksj.consultation.son.app.BaiduLocationManager.LocationListenerCallBack;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;

/**
 * 找病友的切换的列表的fragment
 * @author lmk
 *
 */
public class FindFriendListFragment  extends RootListFragment implements FriendHttpListener,
OnRefreshListener2<ListView>,LocationListenerCallBack{
	
	private PullToRefreshListView mPullListView;
	private ListView mListview;
	private int sourceType;//确定要加载那个列表(1--最新 2--活跃  3--附近 0--个性)
	private CustomerInfoEntity mCustomerInfoEntity;
	private ArrayList<CustomerInfoEntity> allList=new ArrayList<CustomerInfoEntity>();//加载出来的所有数据(朋友列表)
	private String DEFAUTPAGEMARK = "-100000";
	private String pageMark = DEFAUTPAGEMARK;// 分页的标记
	private FindFriendListAdapter adapter;
	private boolean mHasLoaded=false;
	private String lo,la;//经纬度信息
	
	BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(CoreService.ACTION_COMMONT_CONTENT.equals(action)){
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
	private BaiduLocationManager manager;
	public static FindFriendListFragment newInstance(int sourceType){
		FindFriendListFragment fragment=new FindFriendListFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("source", sourceType);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle=getArguments();
		if(bundle!=null){
			sourceType=bundle.getInt("source");
			loadFriendList();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.find_friend_list_fragment, null);
		mPullListView=(PullToRefreshListView) view.findViewById(R.id.find_friend_pulllist);
		mPullListView.setOnRefreshListener(this);
		mListview=mPullListView.getRefreshableView();
		adapter = new FindFriendListAdapter(getActivity(),allList);
		adapter.setOnClickFollowListener(this);
		mListview.setAdapter(adapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FriendHttpUtil.chatFromPerson(getActivity(), adapter.datas.get(position-1));
			}
		});
		return view;
	}

	@Override
	public void responseSuccess(int type, int sourcetype,
			ArrayList<CustomerInfoEntity> c) {
		mPullListView.onRefreshComplete();
		if(sourceType==sourcetype){
			if(pageMark.equals("-100000")){
				adapter.removeAll();
			}
			if(c!=null&&c.size()!=0){
				adapter.addAll(c);
			}
		}
	}

	@Override
	public void responseError(int type, int sourceType, String content) {
		mPullListView.onRefreshComplete();
		try {
			JSONObject obj=new JSONObject(content);
			Toast.makeText(getActivity(), obj.optString("error_message"), Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CoreService.ACTION_COMMONT_CONTENT);//弹出好评
		getActivity().registerReceiver(broadcastReceiver, filter);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(broadcastReceiver);
	
	}
	
	/**
	 * 加载数据
	 */
	public void loadFriendList(){
		if(mCustomerInfoEntity==null){
			mCustomerInfoEntity=new CustomerInfoEntity();
		}
		if(sourceType==3){
			if(lo!=null&&la!=null){
				mCustomerInfoEntity.setLatitude(la);
				mCustomerInfoEntity.setLongitude(lo);
			}else{
				BaiduLocationManager.init(getActivity());
				manager = BaiduLocationManager.getInstance();
				manager.setCallBack(this);
				manager.startLocation();
				return;
			}
		}
		mCustomerInfoEntity.setId(SmartFoxClient.getLoginUserId());
		mCustomerInfoEntity.setFlag(pageMark);
		mPullListView.setRefreshing();
		FriendHttpUtil.getFriendsByType(mCustomerInfoEntity, sourceType, this);
	}
	
	/**
	 * 根据标记搜索相应的页数
	 * 根据服务器返回的标记加载数据
	 */
	public void loadFriendByFlag(String flag){
		mCustomerInfoEntity.setFlag(flag);
		FriendHttpUtil.getFriendsByType(mCustomerInfoEntity, sourceType, this);
	}

	/**
	 * 下拉刷新
	 * @param refreshView
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageMark=DEFAUTPAGEMARK;//从第一页开始
		loadFriendList();
	}

	/**
	 * 上拉加载
	 * @param refreshView
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageMark=adapter.getPageMark(false);
		loadFriendByFlag(pageMark);
	}


	@Override
	public ListView getListView() {
		return mListview;
	}

	@Override
	public BaseAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void locationListenerCallBack(double longitude, double latitude) {
		lo=""+longitude;
		la=""+latitude;
		manager.stopLocation();
		loadFriendList();
	}

	@Override
	public void locationListenerCallBackFaile() {

	}
}
