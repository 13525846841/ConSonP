package com.yksj.consultation.adapter;

import java.util.ArrayList;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;

/**
* @ClassName: FriendAdapter 
* @Description: 
* @author 
* @date 2013-1-10 上午10:06:51
 */
public class FriendAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private ArrayList<CustomerInfoEntity> entities;
	onClickFriendAttentionListener attentionListener;
	onClickFriendHeadListener clickFriendHeadListener;
	private  ImageLoader mImageLoader;
	private  Drawable attenDrawable,notAttenDrawable,dayTicketDrawable,monthTicketDrawable;
	private Boolean isSalonMemberList =false;
	private View clickView;
	private Drawable []  ds ;
//	private boolean isMessageVisible = false;
	private boolean isDoctorList = false;
	private AppData appData; 
	private   int SOURCETYPE = 0; 
	public FriendAdapter(Context context,ArrayList<CustomerInfoEntity> entities) {
		init(context, entities);
	}
	
	public FriendAdapter(Context context,ArrayList<CustomerInfoEntity> entities,Boolean isDoctorList){
		init(context, entities);
		this.isDoctorList = isDoctorList;
	}

	public void setSOURCETYPE( int SOURCETYPE){
		this.SOURCETYPE = SOURCETYPE;
	}
	private void init(Context context, ArrayList<CustomerInfoEntity> entities) {
		inflater = LayoutInflater.from(context);
		this.entities = entities;
		mImageLoader = ImageLoader.getInstance();
		attenDrawable = context.getResources().getDrawable(R.drawable.attention);
		notAttenDrawable = context.getResources().getDrawable(R.drawable.not_attention);
		dayTicketDrawable = context.getResources().getDrawable(R.drawable.day_ticket);
		monthTicketDrawable = context.getResources().getDrawable(R.drawable.month_ticket);
		ds = new Drawable[]{null,dayTicketDrawable,monthTicketDrawable};
		appData  = HTalkApplication.getAppData();
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override 
	public long getItemId(int position) {
		return position;
	}
	
	public void setIsSalonMemberList(Boolean b){
		this.isSalonMemberList =b;
	}
	//获取翻页的标记
	public String getPageMark(boolean isFristPage){
		if (entities.size() > 0 && !isFristPage) {
			return entities.get(entities.size()-1).getFlag(); 
		}else {
			return "-100000";
		}
	}
	
	
	public void removeEntity(CustomerInfoEntity entity){
		entities.remove(entity);
		notifyDataSetChanged();
	}
	
	public void setAdapterData(ArrayList<CustomerInfoEntity> entitie){
		entities.clear();
		entities.addAll(entitie);
		notifyDataSetChanged();
	}
	
	public View getClickView(){
		return clickView;
	}
	
	public void setClickView(View v){
		this.clickView = v;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final viewHolder holder;
		final CustomerInfoEntity  customerInfoEntity=entities.get(position);
		if (convertView ==null||convertView.getTag()==null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.friendlist_iterm,null) ;
			holder.distance = (TextView)convertView.findViewById(R.id.distance);
			holder.head = (ImageView) convertView.findViewById(R.id.head_image);
			holder.personName = (TextView) convertView.findViewById(R.id.name);
			holder.headSex = (ImageView) convertView.findViewById(R.id.head_sex);
			holder.v = (ImageView) convertView.findViewById(R.id.v);
			holder.note = (TextView) convertView.findViewById(R.id.note);
			holder.attentionSalon = (ImageView)convertView.findViewById(R.id.is_friend);
			holder.message = (TextView) convertView.findViewById(R.id.item_dele);
			convertView.setTag(holder);
		}else {
			holder = (viewHolder) convertView.getTag();
		}
		mImageLoader.displayImage(customerInfoEntity.getSex(), customerInfoEntity.getNormalHeadIcon(), holder.head);
		//姓名
		holder.personName.setText(customerInfoEntity.getName());
		//是否是医师沙龙
		if (customerInfoEntity.isShow() == true || customerInfoEntity.isAudit() == true) {
			holder.v.setVisibility(View.VISIBLE);
		}else {
			holder.v.setVisibility(View.GONE);
		}
		//签名
		holder.note.setText(customerInfoEntity.getDescription());
		//消息
		holder.message.setVisibility(View.VISIBLE);
		int size = appData.getNoReadMesgSize(customerInfoEntity.getId());
		if (appData.getNoReadMesgSize(customerInfoEntity.getId()) != 0) {
			holder.message.setText(size + "");
		} else {
			holder.message.setVisibility(View.GONE);
		}
		//关注状态 0 无关系 1 我关注的 2 黑名单 3是客户 4医生
		if (isSalonMemberList == false) {//好友列表
			if (customerInfoEntity.getIsAttentionFriend() !=0 && customerInfoEntity.getIsAttentionFriend() !=2) {
				holder.attentionSalon.setImageDrawable(notAttenDrawable);
			}else {
				holder.attentionSalon.setImageDrawable(attenDrawable);
			}
			//是自己 不显示关注按钮
			if (customerInfoEntity.getId().equals(SmartFoxClient.getLoginUserId())) {
				holder.attentionSalon.setVisibility(View.INVISIBLE);
			}else {
				holder.attentionSalon.setVisibility(View.VISIBLE);
			}
		}else {//沙龙
			if (customerInfoEntity.getTicketFlag().equals("")) {
				customerInfoEntity.setTicketFlag("0");
			}
			if (Integer.valueOf(customerInfoEntity.getTicketFlag()) !=0) {
				holder.attentionSalon.setImageDrawable(ds[Integer.valueOf(customerInfoEntity.getTicketFlag())]);
				holder.attentionSalon.setVisibility(View.VISIBLE);
			}else {
				holder.attentionSalon.setVisibility(View.GONE);
			}
		}
		
		//点击头像进入个人资料
		holder.head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (clickFriendHeadListener != null) {
					clickFriendHeadListener.onClickFriendHead(customerInfoEntity , position);
				}
			}
		});
		
		//点击取消和添加关注安妮
		holder.attentionSalon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (attentionListener != null) {
					attentionListener.onClickFriendAttention(customerInfoEntity.getIsAttentionFriend() ,customerInfoEntity , position );
				}
					setClickView(v);
			}
		});
		
		//挂号金额
		if (isDoctorList) {
			holder.distance.setText(customerInfoEntity.getServicePrice());
		}else {
			if (SOURCETYPE == 3) {//附近
				//距离
				if (((int)customerInfoEntity.getDistance()) != 0) {
					holder.distance.setText(((int)customerInfoEntity.getDistance())+"米");
				}else {
					holder.distance.setText("0米");
				}
			}else{
				holder.distance.setText("");
			}
		}
		// 性别
		LevelListDrawable listDrawable = (LevelListDrawable) holder.headSex.getDrawable();
		listDrawable.setLevel(Integer.valueOf(customerInfoEntity.getSex()));
		return convertView;
	}
	
	private static  class viewHolder {
		private TextView note;//描述
		private ImageView head; // 头像
//		private TextView messageNum;//信息数量
		private TextView personName; // 名称
		private ImageView headSex; // 性别
		private ImageView v;
		private TextView message;//消息的图标
		private ImageView attentionSalon;//是否是关注的好友
		private TextView distance;
	}
	
	public interface onClickFriendHeadListener{
		void onClickFriendHead(CustomerInfoEntity entity , int positon);
	}
	
	public void setonClickFriendHeadListener(onClickFriendHeadListener headListener){
		this.clickFriendHeadListener = headListener;
	};
	
	public interface onClickFriendAttentionListener{
		void onClickFriendAttention(int attentioned,CustomerInfoEntity entity , int positon);
	}
	
	public void setonClickFriendAttentionListener(onClickFriendAttentionListener attentionListener){
		this.attentionListener = attentionListener;
	};

}