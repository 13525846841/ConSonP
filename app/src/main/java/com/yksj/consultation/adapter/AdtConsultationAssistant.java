/**
 * 
 */
package com.yksj.consultation.adapter;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
//import com.yksj.healthtalk.utils.FriendHttpUtil;

/**
 * @author HEKL
 * 
 *         会诊医生列表
 * 
 */
public class AdtConsultationAssistant extends SimpleBaseAdapter<CustomerInfoEntity> {

	private Boolean FLAG = true;
	private ImageLoader mInstance;
	private OnClickChildItemListener followListener;
	private ImageView imgFollow;
	private DisplayImageOptions mOptions;
	private Activity mActivity;
	private Context context;

	public AdtConsultationAssistant(Context context) {
		super(context);
		this.context = context;
		mInstance = ImageLoader.getInstance();
		mActivity = (Activity) context;
		mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(mActivity);
	}

	@Override
	public int getItemResource() {
		return R.layout.consultation_expert_assistant_viewpager_listview;
	}

	@Override
	public View getItemView(final int position, final View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final CustomerInfoEntity entity = datas.get(position);
		ImageView assistantImage = (ImageView) holder.getView(R.id.expert_assistant_header_image);// 专家助理头像
		TextView assistantName = (TextView) holder.getView(R.id.expert_assistant_name);// 专家助理姓名
		TextView aDuty = (TextView) holder.getView(R.id.expert_assistant_header_duty);// 专家助理职务
		TextView aDuty1 = (TextView) holder.getView(R.id.expert_assistant_header_duty1);// 专家助理医院
		TextView aDuty2 = (TextView) holder.getView(R.id.expert_assistant_header_duty2);// 专家助理特长
		imgFollow = (ImageView) holder.getView(R.id.find_doctor_list_item_follow);// 关注按钮
		mInstance.displayImage(entity.getDoctorClientPicture(), assistantImage, mOptions);
//		if(SmartFoxClient.getLoginUserInfo().getDoctorPosition()!=0){
			
			assistantImage.setOnClickListener(new OnClickListener() {//進診所
				
				@Override
				public void onClick(View v) {
					followListener.onHeadIconClick(entity,position);
				}
			});
//		}
//		else {
//			assistantImage.setOnClickListener(new OnClickListener() {//個人
//				
//				@Override
//				public void onClick(View v) {
//					PersonInfoUtil.choiceActivity(entity.getId(), context, ""+entity.getRoldid());
//				}
//			});
//		}
		LevelListDrawable relationDrawable = (LevelListDrawable)imgFollow.getDrawable();
//		if(FLAG){
			if(entity.getRelType().equals("Y")){
				relationDrawable.setLevel(1);
			} else if (entity.getRelType().equals("B")) {
				relationDrawable.setLevel(2);
			} else if (entity.getRelType().equals("D")) {
				relationDrawable.setLevel(1);
			} else if (entity.getRelType().equals("")) {
				relationDrawable.setLevel(0);
			}
//		}else {
//			LogUtil.d("DDD", entity.getAttentionNumber()+"专家助理关注");
//			relationDrawable.setLevel(entity.getAttentionNumber());
//		}
		imgFollow.setOnClickListener(new OnClickListener() {//关注
			
			@Override
			public void onClick(View v) {
				
				if(followListener!=null){
					followListener.onFollowClick(entity, position);
				}
			}
		});
		assistantName.setText(entity.getNickName());
		aDuty.setText(entity.getDoctorTitle() + "-" + entity.getOfficeName1());
		aDuty1.setText(entity.getHospital());
		aDuty2.setText(entity.getSpecial());
		return convertView;
	}

	public void changeFlag(boolean flag) {
		this.FLAG = flag;
	}

	public void setFollowListener(OnClickChildItemListener followListener) {
		this.followListener = followListener;
	}
}
