package com.yksj.consultation.son.friend;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.views.DoctorServiceItemLayout;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.MessagePushService;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.JsonParseUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import cn.sharesdk.framework.ShareSDK;

//import com.yksj.healthtalk.share.utils.OneClickShare;

/**
 * 医生诊所主界面
 * @author lmk
 *
 */
public class DoctorClinicMainActivity extends BaseFragmentActivity implements OnClickListener{

	private TextView tvTitle,tvDoctorDesc,tvDoctorName,tvDoctorHospital,tvDoctorDuomei;
	private TextView tvSpecial,tvDoctorMsg,tvDoctorMsgTime;//医生专长公告,及时间
	private TextView tvFollow,tvLeaveMsg,tvAddPraise;//加关注,留言,点赞
	private ImageView imgSpecialIndex,imgNoticeIndex;
	//================职务与经历=====================
	private RelativeLayout dutyLayout,experLayout;
	private TextView duty,exper;
	private ImageView imgDutyIndex,imgExperIndex;
	private boolean dutyExpanded=false,experExpanded=false;
	private String dutyText,experText;
	//============================================
	private boolean specialExpanded=false,noticeExpanded=false;
	private boolean praiseFlag=false;//是否点赞
	private boolean followed=false;//是否关注
	private int praiseCount;//点赞数量
	private String doctorId;
	private int position=-2,isAttentionFriend=-1;//在上一个列表中的位置,关注好友的标记值

	private DoctorServiceItemLayout genaralServiceLayout;//,orderServiceLayout;//普通服务,订制服务
	private CustomerInfoEntity mInfoEntity,cacheCustomerInfoEntity;
	private ImageView headIcon;

	private PullToRefreshScrollView mPullScrollView;
	private ImageLoader imageLoader;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MessagePushService.ACTION_COLLECT_FRIEND)) {
				LodingFragmentDialog.dismiss(getSupportFragmentManager());
				String result = intent.getStringExtra("result");
				if (result.equals("0")) {
					ToastUtil.showShort(DoctorClinicMainActivity.this, R.string.groupNewFail);
				} else if (result.equals("-1")) {
					ToastUtil.showShort(DoctorClinicMainActivity.this,R.string.against__blacklist_operations);
				} else {
					FriendHttpUtil.requestAttentionTofriendsResult(DoctorClinicMainActivity.this, cacheCustomerInfoEntity);
					followed=!followed;
					if(followed){//关注成功
						Drawable drawable1= getResources().getDrawable(R.drawable.clinic_icon_add_followed);
						/// 这一步必须要做,否则不会显示.
						drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
						tvFollow.setCompoundDrawables(drawable1,null,null,null);
						tvFollow.setText(R.string.cancelCollect);
					}else{//取消关注成功
						Drawable drawable2= getResources().getDrawable(R.drawable.clinic_icon_add_follow);
						drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
						tvFollow.setCompoundDrawables(drawable2,null,null,null);
						tvFollow.setText(R.string.add_follow);
					}
				}
			} 
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.doctor_clinic_main_ui);
		initView();
		Intent intent=getIntent();
		if(intent.hasExtra("position")){
			position=intent.getIntExtra("position", -2);
		}
		if(intent.hasExtra("id")){

			doctorId=intent.getStringExtra("id");
			initData(null,null,doctorId);
		}
//		AnimationUtils.startGuiPager(this, getClass().getName());
		//initData(null,null,"80000521");//现在测试用customerId="80000521"
	}

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessagePushService.ACTION_COLLECT_FRIEND);//取消关注/添加关注的时候
		DoctorClinicMainActivity.this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onStop() {
		DoctorClinicMainActivity.this.unregisterReceiver(receiver);
		super.onStop();
	}

	/**
	 * 根据用户id初始化数据
	 * @param id
	 */
	private void initData(String type, String qrCode, String id) {
		HttpRestClient.doHttpFindCustomerInfoByCustId(type, qrCode, id,
				SmartFoxClient.getLoginUserId(),new ObjectHttpResponseHandler(this) {
			@Override
			public Object onParseResponse(String content) {
				try {
					Object object = JsonParseUtils.filterErrorMessage(content);
					//如果失败,就把失败信息返回
					if (object != null && object instanceof String) {
						return object.toString();
					} else {//否则 进行解析
//						JSONObject obj = new JSONObject(content);
//						experText=obj.optString("RESUME_CONTENT");
//						dutyText=obj.optString("ACADEMIC_JOB");
						return DataParseUtil.jsonToCustmerInfo(new JSONObject(content));
					}
				} catch (JSONException e) {
					return null;
				}
			}

			@Override
			public void onSuccess(int statusCode, Object response) {
				super.onSuccess(statusCode, response);
				if (response != null&& response instanceof CustomerInfoEntity) {
					mInfoEntity = (CustomerInfoEntity) response;
					onBounData();
					if(mInfoEntity.getServiceTypes()!=null&&!"".equals(mInfoEntity.getServiceTypes())){
						try {
							JSONArray serviceArray=new JSONArray(mInfoEntity.getServiceTypes());//普通服务
							if(serviceArray!=null&&0!=serviceArray.length()){
								findViewById(R.id.clinic_layout_item3).setVisibility(View.VISIBLE);
								genaralServiceLayout.setServiceTypes(mInfoEntity);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					//由于为了版本上线,订制服务先不要
					//if(mInfoEntity.getAccomplishedname()!=null&&!"".equals(mInfoEntity.getAccomplishedname())){
					//	try {
					//		JSONArray orderArray=new JSONArray(mInfoEntity.getAccomplishedname());//订制服务
					//		if(orderArray!=null&&0!=orderArray.length()){
					//		findViewById(R.id.clinic_layout_item4).setVisibility(View.VISIBLE);
					//		orderServiceLayout.setServiceTypes(mInfoEntity);
					//		}
					//	} catch (JSONException e) {
					//		e.printStackTrace();
					//	}
					//}

				} else {
					ToastUtil.showShort(getApplicationContext(),response.toString());
					finish();//直接返回
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (mPullScrollView != null
						&& mPullScrollView.isRefreshing()) {
					mPullScrollView.onRefreshComplete();
				}
			}
			@Override
			public void onStart() {
				super.onStart();
				if (mPullScrollView != null
						&& !mPullScrollView.isRefreshing()) {
					mPullScrollView.setRefreshing();
				}
			}
		});
	}


	private void onBounData() {
		if(mInfoEntity.getAcademicJob().equals("")){
			experLayout.setVisibility(View.GONE);
		}
		if(mInfoEntity.getResumeContent().equals("")){
			dutyLayout.setVisibility(View.GONE);
		}
//		if(experText==null){
//			experLayout.setVisibility(View.GONE);
//		}
//		if(dutyText==null){
//			dutyLayout.setVisibility(View.GONE);
//		}
		imageLoader=ImageLoader.getInstance();
		if(AppData.DYHSID.equals(mInfoEntity.getId())){//如果是导医护士
			tvTitle.setText(mInfoEntity.getNickName());
			tvFollow.setVisibility(View.GONE);
		}else{
			tvTitle.setText(mInfoEntity.getNickName()+getString(R.string.doctor_clinic));
			tvFollow.setVisibility(View.VISIBLE);
		}
		titleRightBtn2.setOnClickListener(this);
		tvDoctorName.setText(mInfoEntity.getNickName());
		tvDoctorDuomei.setText("("+mInfoEntity.getUsername()+")");//账号
		tvDoctorHospital.setText(mInfoEntity.getHospital());
		tvAddPraise.setText(mInfoEntity.getPraiseCount());
		praiseFlag=!("0".equals(mInfoEntity.getPraiseFlag()));//自己是否对对方点过赞   赞标记  0-没赞过
		if(praiseFlag){
			Drawable drawable= getResources().getDrawable(R.drawable.clinic_icon_add_praised);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tvAddPraise.setCompoundDrawables(drawable,null,null,null);
			tvAddPraise.setTextColor(DoctorClinicMainActivity.this.getResources().getColor(R.color.color_green_blue_bg));
		}
		praiseCount=Integer.parseInt(mInfoEntity.getPraiseCount());
		imageLoader.displayImage(mInfoEntity.getSex(), mInfoEntity.getNormalHeadIcon(), headIcon);
		followed=mInfoEntity.getIsAttention();
		if(followed){
			Drawable drawable= getResources().getDrawable(R.drawable.clinic_icon_add_followed);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tvFollow.setCompoundDrawables(drawable,null,null,null);
			tvFollow.setText(R.string.cancelCollect);
		}
		tvFollow.setOnClickListener(this);
		tvLeaveMsg.setOnClickListener(this);
		tvAddPraise.setOnClickListener(this);
		isAttentionFriend=mInfoEntity.getIsAttentionFriend();
		if(!TextUtils.isEmpty(mInfoEntity.getOfficeName1())){
			tvDoctorDesc.setText(mInfoEntity.getDoctorTitle()+" - "+mInfoEntity.getOfficeName1());
		}else{
			tvDoctorDesc.setText(mInfoEntity.getDoctorTitle());
		}

		//医生专长
		if (!TextUtils.isEmpty(mInfoEntity.getSpecial())) {//
//		if (!TextUtils.isEmpty(dutyText)) {//
			StringBuilder b = new StringBuilder();
			b.append(mInfoEntity.getSpecial());
//			b.append(dutyText);
			tvSpecial.setText(b);
			if(tvSpecial.getLineCount()<2)//行数小于2,将展开按钮隐藏
				findViewById(R.id.clinic_specialty_index).setVisibility(View.INVISIBLE);
			else
				findViewById(R.id.clinic_layout_item1).setOnClickListener(this);
		} 
		//医生公告
		if(!TextUtils.isEmpty(mInfoEntity.getDoctorMessage())){
			StringBuilder b=new StringBuilder();
			b.append(mInfoEntity.getDoctorMessage());
			tvDoctorMsg.setText(b);
		}else{
			StringBuilder b=new StringBuilder();
			b.append("欢迎您的光临，很高兴为您服务!");
			tvDoctorMsg.setText(b);
		}
		if(tvDoctorMsg.getLineCount()<=2)
			findViewById(R.id.clinic_doctor_notice_index).setVisibility(View.INVISIBLE);
		else
			findViewById(R.id.clinic_layout_item2).setOnClickListener(this);
		tvDoctorMsgTime.setText(mInfoEntity.getMessageTime());
		
		//职务
//		if (!TextUtils.isEmpty(dutyText)) {
		if (!TextUtils.isEmpty(mInfoEntity.getAcademicJob())) {
//			dutyLayout.setVisibility(View.VISIBLE);
			StringBuilder b = new StringBuilder();
			b.append(mInfoEntity.getAcademicJob());
//			b.append(dutyText);
			duty.setText(b);
			if(duty.getLineCount()<2)//行数小于2,将展开按钮隐藏
				findViewById(R.id.clinic_doctor_notice_index11).setVisibility(View.INVISIBLE);
			else
				findViewById(R.id.clinic_layout_item11).setOnClickListener(this);
		} 
		//经历
//		if (!TextUtils.isEmpty(experText)) {
		if (!TextUtils.isEmpty(mInfoEntity.getResumeContent())) {
//			experLayout.setVisibility(View.VISIBLE);
			StringBuilder b = new StringBuilder();
			b.append(mInfoEntity.getResumeContent());
//			b.append(experText);
			exper.setText(b);
			if(exper.getLineCount()<2)//行数小于2,将展开按钮隐藏
				findViewById(R.id.clinic_doctor_notice_index33).setVisibility(View.INVISIBLE);
			else
				findViewById(R.id.clinic_layout_item33).setOnClickListener(this);
		} 
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setText(R.string.share);
		//titleRightBtn2.setVisibility(View.VISIBLE);//分享
		tvTitle=(TextView) findViewById(R.id.title_lable);
		headIcon = (ImageView) findViewById(R.id.doctor_clinic_title_headicon);
		headIcon.setOnClickListener(this);//头像点击事件
		tvDoctorDesc=(TextView) findViewById(R.id.doctor_clinic_title_desc);
		tvDoctorDuomei=(TextView) findViewById(R.id.doctor_clinic_title_duomeihao);
		tvDoctorName=(TextView) findViewById(R.id.doctor_clinic_title_nickname);
		tvDoctorHospital=(TextView) findViewById(R.id.doctor_clinic_title_hospital);
		mPullScrollView=(PullToRefreshScrollView) findViewById(R.id.clinic_pullrefresh_scrollview);
		tvSpecial=(TextView) findViewById(R.id.clinic_specialty_content);
		tvDoctorMsg=(TextView) findViewById(R.id.clinic_doctor_notice_content);
		tvDoctorMsgTime=(TextView) findViewById(R.id.clinic_doctor_notice_time);
		tvFollow=(TextView) findViewById(R.id.clinic_title_add_follow);
		tvLeaveMsg=(TextView) findViewById(R.id.clinic_title_leave_msg);
		tvAddPraise=(TextView) findViewById(R.id.clinic_title_add_praise);
		genaralServiceLayout=(DoctorServiceItemLayout) findViewById(R.id.clinic_general_service_item_layout);
//		orderServiceLayout=(DoctorServiceItemLayout) findViewById(R.id.clinic_order_service_item_layout);
		imgSpecialIndex=(ImageView) findViewById(R.id.clinic_specialty_index);
		imgNoticeIndex=(ImageView) findViewById(R.id.clinic_doctor_notice_index);

		//职务
		dutyLayout=(RelativeLayout) findViewById(R.id.clinic_layout_item11);
		duty=(TextView) findViewById(R.id.clinic_doctor_notice_content11);
		imgDutyIndex=(ImageView) findViewById(R.id.clinic_doctor_notice_index11);
		//经历
		experLayout=(RelativeLayout) findViewById(R.id.clinic_layout_item33);
		exper=(TextView) findViewById(R.id.clinic_doctor_notice_content33);
		imgExperIndex=(ImageView) findViewById(R.id.clinic_doctor_notice_index33);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back://返回
			onBackPressed();
			break;
		case R.id.title_right2://分享
			shareDoctorToFriend();
			break;
		case R.id.clinic_title_add_follow://加关注或者取消关注
			cacheCustomerInfoEntity=FriendHttpUtil.requestAttentionTofriends(DoctorClinicMainActivity.this, false, mInfoEntity);
			break;
		case R.id.clinic_title_leave_msg://留言
			if(mInfoEntity!=null)
				FriendHttpUtil.chatFromPerson(DoctorClinicMainActivity.this, mInfoEntity);
			break;
		case R.id.doctor_clinic_title_headicon://点击头像
			ZoomImgeDialogFragment.show(mInfoEntity.getBigHeadIcon(),
					getSupportFragmentManager());
			break;
		case R.id.clinic_title_add_praise://点赞
			praise();
			break;
		case R.id.clinic_layout_item1://专长文本收缩展开
			if(specialExpanded){
				specialExpanded=false;
				tvSpecial.setMaxLines(2);
				imgSpecialIndex.setImageResource(R.drawable.gengduos);
			}else{
				specialExpanded=true;
				tvSpecial.setMaxLines(100);
				imgSpecialIndex.setImageResource(R.drawable.shouqis);
			}
			break;
		case R.id.clinic_layout_item11://职务文本收缩展开
			if(dutyExpanded){
				dutyExpanded=false;
				duty.setMaxLines(2);
				imgDutyIndex.setImageResource(R.drawable.gengduos);
			}else{
				dutyExpanded=true;
				duty.setMaxLines(100);
				imgDutyIndex.setImageResource(R.drawable.shouqis);
			}
			break;
		case R.id.clinic_layout_item33://经历文本收缩展开
			if(experExpanded){
				experExpanded=false;
				exper.setMaxLines(2);
				imgExperIndex.setImageResource(R.drawable.gengduos);
			}else{
				experExpanded=true;
				exper.setMaxLines(100);
				imgExperIndex.setImageResource(R.drawable.shouqis);
			}
			break;
		case R.id.clinic_layout_item2://公告文本
			if(noticeExpanded){
				noticeExpanded=false;
				tvDoctorMsg.setMaxLines(2);
				imgNoticeIndex.setImageResource(R.drawable.tv_arrow_expand);
			}else{
				noticeExpanded=true;
				tvDoctorMsg.setMaxLines(100);
				imgNoticeIndex.setImageResource(R.drawable.tv_arrow_pack_up);
			}
			break;
		}
	}

	//将此医生分享到第三方平台
	private void shareDoctorToFriend() {
		ShareSDK.initSDK(this);
//		OneClickShare ocs = new OneClickShare(this,titleRightBtn2);
//		ocs.disableSSOWhenAuthorize();
//		ocs.setNotification(R.drawable.launcher_logo,getString(R.string.app_name));
//		String title = "推荐一个非常棒的医生";
//		ocs.setText("推荐一个非常棒的医生："+mInfoEntity.getNickName()+","+mInfoEntity.getDoctorTitle()+"--"+mInfoEntity.getOfficeName1()+
//				getString(R.string.share_web)+"/DuoMeiHealth/DoctorShareServlet?DOCTORID="+mInfoEntity.id);
//		ocs.setTitle(title);
//		ocs.setUrl(getString(R.string.share_web)+"/DuoMeiHealth/DoctorShareServlet?DOCTORID="+mInfoEntity.id);
//		String imgurl=mInfoEntity.getNormalHeadIcon();
//		if(imgurl!=null&&imgurl.length()!=0){
//			if(imgurl.contains("assets")){//使用的是默认头像
//			//String path = "file:///android_asset/customerIcons/default_head_mankind.png";
//			//imgurl=CustomerInfoEntity.parseHeaderUrl(imgurl);
//			//ocs.setImagePath(path);
//			}else{//网上有头像
//				ocs.setImageUrl(HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE+mInfoEntity.getNormalHeadIcon());
//			}
//		}
//		ocs.show();
	}

	//点赞操作
	private void praise() {
		if(praiseFlag)return;//点过赞了就不能取消
		HttpRestClient.doHttpOperatePraiseToFriend(DoctorClinicMainActivity.this, praiseFlag, mInfoEntity,
				new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode,
					JSONObject response) {
				super.onSuccess(statusCode, response);
				if(response!=null&&!response.toString().contains("error_message")){
					if(praiseFlag){
						praiseCount--;
						Drawable drawable= getResources().getDrawable(R.drawable.clinic_icon_add_praise);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						tvAddPraise.setCompoundDrawables(drawable,null,null,null);
						tvAddPraise.setTextColor(DoctorClinicMainActivity.this.getResources().getColor(R.color.color_grey_three_text));
					}else{
						praiseCount++;
						Drawable drawable= getResources().getDrawable(R.drawable.clinic_icon_add_praised);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						tvAddPraise.setCompoundDrawables(drawable,null,null,null);
						tvAddPraise.setTextColor(DoctorClinicMainActivity.this.getResources().getColor(R.color.color_green_blue_bg));
					}
					praiseFlag=!praiseFlag;
					tvAddPraise.setText(""+praiseCount);
				}else{
					ToastUtil.showShort(DoctorClinicMainActivity.this, response.optString("error_message"));
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if(position!=-2){//来自列表的跳转,回去有可能改变状态
			Intent data=new Intent();
			if(mInfoEntity!=null&&isAttentionFriend!=-1){
				if(mInfoEntity.getIsAttentionFriend()!=isAttentionFriend){//关注状态改变
					data.putExtra("isChangeAttention", true);
					data.putExtra("position", position);
					data.putExtra("attentionFriend", mInfoEntity.getIsAttentionFriend());
				}
			}
			setResult(RESULT_OK, data);
		}
		super.onBackPressed();
		finish();
	}
}
