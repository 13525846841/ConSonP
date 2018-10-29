package com.yksj.consultation.adapter;

import java.util.ArrayList;

import org.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.PersonInfoUtil;

/**
 * 找病友的列表适配器
 * @author lmk
 *
 */
public class FindFriendListAdapter extends SimpleBaseAdapter<CustomerInfoEntity>{

	private ImageLoader mInstance;
	private OnClickChildItemListener followListener;

	public FindFriendListAdapter(Context context,ArrayList<CustomerInfoEntity> allList) {
		super(context,allList);
		mInstance = ImageLoader.getInstance();
	}

	public FindFriendListAdapter(Context context) {
		super(context);
		mInstance = ImageLoader.getInstance();
	}

	@Override
	public int getItemResource() {
		return R.layout.find_friend_list_item;//item布局文件
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final CustomerInfoEntity cus=datas.get(position);
        LogUtil.d("DDD",cus.getDoctorName());
		TextView tvName= (TextView) holder.getView(R.id.find_list_item_name);
		ImageView imgFollow=(ImageView) holder.getView(R.id.find_list_item_follow);
		TextView tvMood=(TextView) holder.getView(R.id.find_list_item_mood);
		ImageView icon=(ImageView) holder.getView(R.id.find_list_item_headicon);
		tvName.setText(cus.getNickName());
		
		ImageView imgSex=(ImageView) holder.getView(R.id.find_list_item_sex);
		LevelListDrawable listDrawable = (LevelListDrawable)imgSex.getDrawable();
		listDrawable.setLevel(Integer.valueOf(cus.getSex()));
		LevelListDrawable relationDrawable = (LevelListDrawable)imgFollow.getDrawable();
		relationDrawable.setLevel(cus.getAttentionNumber());
		
//		if(cus.getIsAttention()){
//			btnFollow.setText(R.string.cancelCollect);
//			btnFollow.setBackgroundResource(R.drawable.btn_unfolloe_bg_2x);
//		}else{
//			btnFollow.setText(R.string.collect);
//			btnFollow.setBackgroundResource(R.drawable.btn_follow_bg_2x);
//		}
		tvMood.setText(cus.getDescription());
		mInstance.displayImage(cus.getSex(), cus.getNormalHeadIcon(), icon);
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PersonInfoUtil.choiceActivity(cus.getId(), context, ""+cus.getRoldid());
			}
		});
		if(SmartFoxClient.getLoginUserId().equals(cus.getId())){//如果是自己
			imgFollow.setVisibility(View.GONE);
		}else{
			imgFollow.setVisibility(View.VISIBLE);
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
	
	//获取翻页的标记
	public String getPageMark(boolean isFristPage){
		if (datas.size() > 0 && !isFristPage) {
			return datas.get(datas.size()-1).getFlag(); 
		}else {
			return "-100000";
		}
	}
	
	//点击关注的回调
	public void setOnClickFollowListener(OnClickChildItemListener l){
		this.followListener = l;
	};
	
}
