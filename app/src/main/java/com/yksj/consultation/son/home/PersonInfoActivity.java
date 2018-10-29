package com.yksj.consultation.son.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.yksj.consultation.adapter.InterestAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.TagsGridView;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.MessagePushService;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.List;
/**
 * 个人资料
 * @author jack_tang
 *
 */
public class PersonInfoActivity extends BaseFragmentActivity implements OnClickListener, OnItemClickListener {
	private ViewFinder mFinder;
	private ScrollView mScrollView;
	private String qrCode;
	private String type;;// 0-社交场  1-医生馆
	private CustomerInfoEntity mInfoEntity;
	private List<TagEntity> xingQuAdapterList;// 个性签名喜好标签
	private List<TagEntity> bodyAdapterList;// 获取的关注身体标签
	private CustomerInfoEntity cacheCustomerInfoEntity;
	private Button mAttention_bt;//是否关注
	private Button mBlack_bt;//拉黑 取消拉黑
	private final int PERSON_INFO = 1000;//修改个人资料
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra("result");
			if (result.equals("-1")) {
				ToastUtil.showShort(PersonInfoActivity.this,R.string.against__blacklist_operations);
			} else if (result.equals("0")) {
				ToastUtil.showShort(PersonInfoActivity.this,R.string.groupNewFail);
				return;
			} else {
				FriendHttpUtil.requestAttentionTofriendsResult(PersonInfoActivity.this, cacheCustomerInfoEntity);
				mInfoEntity.setIsAttentionFriend(cacheCustomerInfoEntity.getIsAttentionFriend());
				switch (cacheCustomerInfoEntity.getIsAttentionFriend()) {// 0 没有关系 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者
				case 7:
				case 5:// 取消客户收藏成功
				case 0:// 取消关注成功
					mAttention_bt.setText("加关注");
					mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clinic_icon_add_follow), null, null, null);
					mBlack_bt.setText("拉黑");
					mBlack_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.im_black_normal), null, null, null);
					break;
				case 6:
				case 4:// 客户收藏成功
				case 1:// 关注成功
					mAttention_bt.setText("取消关注");
					mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clinic_icon_add_followed), null, null, null);
					mBlack_bt.setText("拉黑");
					mBlack_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.im_black_normal), null, null, null);
					break;
				case 2:// 加入黑名单成功
					mBlack_bt.setText("取消拉黑");
					mBlack_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.im_black_press), null, null, null);
					mAttention_bt.setText("加关注");
					mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clinic_icon_add_follow), null, null, null);
					break;
				case 3:// 
//					mBlack_bt.setText("拉黑");
					mAttention_bt.setText("取消关注");
					mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clinic_icon_add_follow), null, null, null);
					break;
				}
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.person_info_layout);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		mFinder = new ViewFinder(this);
		mScrollView=(ScrollView) findViewById(R.id.msv);
		mScrollView.smoothScrollTo(0,100);//
		Intent intent =  getIntent();
		if (intent.hasExtra("id")) {
			initData(type,qrCode,intent.getStringExtra("id"));
		} else if(intent.hasExtra("qrCode")){
			initData(intent.getStringExtra("type"),intent.getStringExtra("qrCode"),"");
		}else{
			initData(type,qrCode,SmartFoxClient.getLoginUserId());
		}
	}

	/**
	 * 根据id查询资料
	 * @param type
	 * @param qrCode
	 * @param id
	 */
	private void initData(String type , String qrCode ,String id) {
		HttpRestClient.doHttpFindCustomerInfoByCustId(type,qrCode,id,SmartFoxClient.getLoginUserId(),
				new ObjectHttpResponseHandler(this) {
					@Override
					public CustomerInfoEntity onParseResponse(String content) {
						CustomerInfoEntity entity = null;
						if (TextUtils.isEmpty(content) && content.equalsIgnoreCase("N")) {
							ToastUtil.showShort(getApplicationContext(),R.string.request_error);
						} else {
							try {
								entity = DataParseUtil.jsonToCustmerInfo(new JSONObject(content));
							} catch (JSONException e) {
							}
						}
						return entity;
					}
					@Override
					public void onSuccess(int statusCode, Object response) {
						if (response != null) {
							mInfoEntity = (CustomerInfoEntity) response;
							onBoundView();
						}else {
							setResult(RESULT_OK,getIntent());
							finish();
						}
					}
				});
	}

	/**
	 * view绑定数据
	 */
	@SuppressLint("NewApi") protected void onBoundView() {
		ImageLoader mImageLoader=ImageLoader.getInstance();
		ImageView headImage = mFinder.imageView(R.id.head);
		mImageLoader.displayImage(mInfoEntity.getSex(),mInfoEntity.getNormalHeadIcon(),headImage);
		mFinder.setText(R.id.username, mInfoEntity.getName());
		mFinder.setText(R.id.desc_content, mInfoEntity.getDescription());
		mFinder.onClick(this,new int[]{R.id.attention,R.id.talk,R.id.black_action,R.id.head});
		mAttention_bt = mFinder.button(R.id.attention);
		mBlack_bt = mFinder.button(R.id.black_action);
	//账号
		mFinder.setText(R.id.account,mInfoEntity.getUsername());
	//性别
		mFinder.setText(R.id.sex, mInfoEntity.getSexText());
	//生日
		mFinder.setText(R.id.birthday,mInfoEntity.getBirthday());
	//所在地
		mFinder.setText(R.id.location, mInfoEntity.getDwellingplace());
	//职业
		mFinder.setText(R.id.occupation, mInfoEntity.getMetier());
	
		//个性标签 style_view
		TagsGridView mGridViewXingQu = (TagsGridView) findViewById(R.id.style_view);
		bodyAdapterList = DataParseUtil.JsonToListEntity(mInfoEntity.getSameExperience(), 1);
		TagsGridView mBodyBiaoQian = (TagsGridView) findViewById(R.id.health_view);
		mBodyBiaoQian.setAdapter(new InterestAdapter(this, bodyAdapterList,R.layout.textiterm));
		if(bodyAdapterList.size()==0)findViewById(R.id.health_empty_view).setVisibility(View.VISIBLE);
		else findViewById(R.id.health_empty_view).setVisibility(View.GONE);
		
		
		
		//健康关注 health_view
		xingQuAdapterList = DataParseUtil.JsonToListEntity(mInfoEntity.getLableJson(), 0);
		InterestAdapter mAdapterXingQu = new InterestAdapter(this, xingQuAdapterList,R.layout.textiterm);
		mGridViewXingQu.setAdapter(mAdapterXingQu);
		if(xingQuAdapterList.size()==0)findViewById(R.id.style_empty_view).setVisibility(View.VISIBLE);
		else findViewById(R.id.style_empty_view).setVisibility(View.GONE);
		
		
		mBodyBiaoQian.setOnItemClickListener(this);
		mGridViewXingQu.setOnItemClickListener(this);
		
		
		if(mInfoEntity.getId().equals(SmartFoxClient.getLoginUserId())){
			titleRightBtn2.setVisibility(View.VISIBLE);
			titleRightBtn2.setText("编辑");
			titleRightBtn2.setOnClickListener(this);
			findViewById(R.id.person_set_layout).setVisibility(View.GONE);
			
		}else{//处理朋友之间的关系
			findViewById(R.id.person_set_layout).setVisibility(View.VISIBLE);
			mFinder.setText(R.id.style_hint, "暂无标签");
			mFinder.setText(R.id.health_hint, "暂无标签");
		}
		
		
		if(mInfoEntity.getIsAttentionFriend() ==2){
			mBlack_bt.setText("取消拉黑");
			mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.im_black_press), null, null, null);
		}else if(mInfoEntity.getIsAttentionFriend() !=0){
			mAttention_bt.setText("取消关注");
			mAttention_bt.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.clinic_icon_add_followed), null, null, null);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=RESULT_OK || data==null)return ;
		switch (requestCode) {
		case PERSON_INFO:
			mInfoEntity=SmartFoxClient.getLoginUserInfo();
			onBoundView();
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.title_right2://编辑
			Intent in =new Intent(this,PersonInfoEditActivity.class);
			startActivityForResult(in, PERSON_INFO);
			break;
		case R.id.attention://    关注/取消关注
			if (!SystemUtils.isNetWorkValid(this)) {
				ToastUtil.showBasicShortToast(this, "请检查网络是否可用...");
				return;
			}
			cacheCustomerInfoEntity = FriendHttpUtil.requestAttentionTofriends(
					this,  null, mInfoEntity);
			break;
		case R.id.black_action://  拉黑/取消拉黑
			if (!SystemUtils.isNetWorkValid(this)) {
				ToastUtil.showBasicShortToast(this, "请检查网络是否可用...");
				return;
			}
			cacheCustomerInfoEntity = FriendHttpUtil.requestAttentionTofriends(this, true, mInfoEntity);
			break;
		case R.id.talk://对话
			FriendHttpUtil.chatFromPerson(this, mInfoEntity);
			break;
		case R.id.head://头像
			if (!SystemUtils.isNetWorkValid(this)) {
				ToastUtil.showBasicShortToast(this, "请检查网络是否可用...");
				return;
			}
			if (!HStringUtil.isEmpty(mInfoEntity.getBigHeadIcon()) && !mInfoEntity.getBigHeadIcon().startsWith("assets/")) {
				ZoomImgeDialogFragment.show(mInfoEntity.getBigHeadIcon(),getSupportFragmentManager());
			}
			break;
		}
	}

	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter(MessagePushService.ACTION_COLLECT_FRIEND);
		registerReceiver(receiver, filter);
		super.onStart();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(receiver);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		CustomerInfoEntity entity = new CustomerInfoEntity();
//		entity.setId(SmartFoxClient.getSmartFoxClient().getUserId());
//		Intent intent = new Intent(this, FriendSearchResultActivity.class);
//		switch (parent.getId()) {
//		case R.id.style_view://个性标签
//			entity.setType(0);
//			entity.setInterestName(xingQuAdapterList.get(position).getName());
//			entity.setInterestCode(xingQuAdapterList.get(position).getId());
//			intent.putExtra("title", xingQuAdapterList.get(position).getName());
//			break;
//		case R.id.health_view://健康关注
//			entity.setType(2);
//			entity.setSameExperienceCode(Integer.valueOf(bodyAdapterList.get(position).getId()));
//			entity.setSameExperience(bodyAdapterList.get(position).getName());
//			intent.putExtra("title", bodyAdapterList.get(position).getName());
//			break;
//		}
//		intent.putExtra("mCustomerInfoEntity", entity);
//		startActivity(intent);
	}
}
