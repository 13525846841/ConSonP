package com.yksj.consultation.adapter;

import java.util.ArrayList;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnClickChildItemListener;

public class TopicMemberListAdapter extends SimpleBaseAdapter<CustomerInfoEntity> {

	private ImageLoader mInstance;
	private String creatorId;
	private OnClickChildItemListener followListener;
	public TopicMemberListAdapter(Context context) {
		super(context);
		mInstance=ImageLoader.getInstance();
	}
	
	public void setDataAndType(ArrayList<CustomerInfoEntity> data){
		this.datas=data;
	}
	public void setGroupId(String id){
		this.creatorId=id;
	}

	@Override
	public int getItemResource() {
		return R.layout.topic_member_item;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final CustomerInfoEntity cus=datas.get(position);
		ImageView headIcon=(ImageView) holder.getView(R.id.topic_member_item_headicon);
		TextView tvName=(TextView) holder.getView(R.id.topic_member_item_title);
		TextView creFlag=(TextView) holder.getView(R.id.topic_member_item_creflag);//是不是创建者
		TextView tvSign=(TextView) holder.getView(R.id.topic_member_item_signature);
//		ImageView relation=(ImageView) holder.getView(R.id.topic_member_item_relation);
		if(position==0)
			creFlag.setVisibility(View.VISIBLE);
		else
			creFlag.setVisibility(View.GONE);
//		if(SmartFoxClient.getLoginUserId().equals(cus.getId())){//如果是自己
//			relation.setVisibility(View.GONE);
//		}else{
//			relation.setVisibility(View.VISIBLE);
//			relation.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					followListener.onFollowClick(cus, position);
//				}
//			});
//		}
//		LevelListDrawable relationDrawable = (LevelListDrawable)relation.getDrawable();
//		relationDrawable.setLevel(cus.getAttentionNumber());
		mInstance.displayImage(cus.getSex(), cus.getNormalHeadIcon(), headIcon);
		headIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				followListener.onHeadIconClick(cus, position);//点击头像
			}
		});
//		tvName.setText(cus.getName());
		tvName.setText(cus.getNickName());
		tvSign.setText(cus.getDescription());
		return convertView;
	}
	
	public void setClickChildListener(OnClickChildItemListener l){
		this.followListener=l;
	}

}
