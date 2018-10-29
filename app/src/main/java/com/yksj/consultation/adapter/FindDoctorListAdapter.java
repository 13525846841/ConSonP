package com.yksj.consultation.adapter;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.utils.FriendHttpUtil;

/**
 * 找专家列表的适配器
 * @author lmk
 *
 */
public class FindDoctorListAdapter extends SimpleBaseAdapter<CustomerInfoEntity> {

	private ImageLoader mInstance;
	private OnClickChildItemListener followListener;
	
	public FindDoctorListAdapter(Context context) {
		super(context);
		mInstance=ImageLoader.getInstance();
	}

	@Override
	public int getItemResource() {
		return R.layout.find_doctor_list_item;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final CustomerInfoEntity cus=datas.get(position);
		TextView tvName=(TextView) convertView.findViewById(R.id.find_doctor_list_item_name);
		TextView tvTitle=(TextView) convertView.findViewById(R.id.find_doctor_list_item_doctitle);
		TextView tvHospital=(TextView) convertView.findViewById(R.id.find_doctor_list_item_hospital);
		TextView tvSpecial=(TextView) convertView.findViewById(R.id.find_doctor_list_item_spetical);
		ImageView imgFollow=(ImageView) convertView.findViewById(R.id.find_doctor_list_item_follow);
		ImageView icon=(ImageView) convertView.findViewById(R.id.find_doctor_list_item_headicon);
		mInstance.displayImage(cus.getSex(), cus.getNormalHeadIcon(), icon);
		
		if(AppData.DYHSID.equals(cus.getId())){//如果是导医护士
			icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FriendHttpUtil.chatFromPerson((FragmentActivity) context, cus);
//					PersonInfoUtil.choiceActivity(cus.getId(), context, ""+cus.getRoldid());
				}
			});
			imgFollow.setVisibility(View.GONE);
			tvName.setText(cus.getNickName());
			tvTitle.setText(cus.getSpecial());
			tvHospital.setVisibility(View.GONE);
			tvSpecial.setVisibility(View.GONE);
		}else{
			icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent=new Intent(context,DoctorClinicMainActivity.class);
//					context.startActivity(intent);
//					.get(position)
//					PersonInfoUtil.choiceActivity(cus.getId(), context, ""+cus.getRoldid());
					followListener.onHeadIconClick(cus, position);
				}
			});
			tvName.setText(cus.getNickName());
			tvTitle.setText(cus.getDoctorTitle()+"-"+cus.getOfficeName1());
			tvHospital.setText(cus.getHospital());
			tvSpecial.setText(cus.getSpecial());
			tvHospital.setVisibility(View.VISIBLE);
			tvSpecial.setVisibility(View.VISIBLE);
			imgFollow.setVisibility(View.VISIBLE);
//			if(cus.getIsAttention()){
//				btnFollow.setText(R.string.cancelCollect);
//				btnFollow.setBackgroundResource(R.drawable.btn_unfolloe_bg_2x);
//			}else{
//				btnFollow.setText(R.string.collect);
//				btnFollow.setBackgroundResource(R.drawable.btn_follow_bg_2x);
//			}
			LevelListDrawable relationDrawable = (LevelListDrawable)imgFollow.getDrawable();
			relationDrawable.setLevel(cus.getAttentionNumber());
			imgFollow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(followListener!=null){
						followListener.onFollowClick(cus, position);
					}
				}
			});
		}
		return convertView;
	}

	public void setFollowListener(OnClickChildItemListener followListener) {
		this.followListener = followListener;
	}
}
