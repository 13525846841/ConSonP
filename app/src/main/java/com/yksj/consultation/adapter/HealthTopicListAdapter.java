package com.yksj.consultation.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnClickChildItemListener;

/**
 * 健康话题的列表适配器
 * @author lmk
 *
 */
public class HealthTopicListAdapter extends SimpleBaseAdapter<GroupInfoEntity> {
	public static final int TYPE_MANAGE=0;
	public static final int TYPE_HEALTH_TOPIC=1;
	private ImageLoader mInstance;
	private Context context;
	private OnClickChildItemListener clickListener;
	private int adapterType=1;//0代表是医生话题管理的适配器,1代表患者端健康话题的适配器
	
	public void setClickListener(OnClickChildItemListener clickListener) {
		this.clickListener = clickListener;
	}

	public void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}

	public HealthTopicListAdapter(Context context) {
		super(context);
		this.context=context;
		mInstance=ImageLoader.getInstance();
	}

	@Override
	public int getItemResource() {
		return R.layout.health_topic_list_item;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		final GroupInfoEntity entity=datas.get(position);
		TextView tvName=(TextView) holder.getView(R.id.health_topic_item_name);
		TextView tvDesc=(TextView) holder.getView(R.id.health_topic_item_desc);
		TextView tvNum=(TextView) holder.getView(R.id.health_topic_item_num);
		ImageView btnFollowOperate=(ImageView) holder.getView(R.id.health_topic_item_follow);//删除或者关注操作
//		Button btnModify=(Button) holder.getView(R.id.health_topic_item_edit);//删除或者关注操作
		TextView tvFree=(TextView) holder.getView(R.id.health_topic_item_free);
		LinearLayout ticketLayout= (LinearLayout) holder.getView(R.id.health_topic_item_ticket_laout);
		TextView tvDayTicket=(TextView) holder.getView(R.id.health_topic_item_ticket_day);
		TextView tvMonthTicket=(TextView) holder.getView(R.id.health_topic_item_ticket_month);
		ImageView imgIcon=(ImageView) holder.getView(R.id.health_topic_item_headicon);
		ImageView imgIconUp=(ImageView) holder.getView(R.id.health_topic_item_headicon_up);
		tvName.setText(entity.getName());
		tvDesc.setText(entity.getRecordDesc());
		if(entity.getPersonNumber().contains("人")){
			tvNum.setText(entity.getPersonNumber());
		}else
			tvNum.setText(entity.getPersonNumber()+"人");
		if(entity.getTicketMsg().contains("TICKET_TYPE"))//表示有门票信息进行解析
		{
			imgIconUp.setVisibility(View.VISIBLE);
			ticketLayout.setVisibility(View.VISIBLE);
			tvFree.setVisibility(View.GONE);
			try {
				JSONArray array=new JSONArray(entity.getTicketMsg());
				for(int i=0;i<array.length();i++){
					JSONObject object=array.getJSONObject(i);
					if("1".equals(object.optString("TICKET_TYPE")))//日票
						tvDayTicket.setText(context.getResources().getString(R.string.day_ticket_price)+object.optString("CHARGING_STANDARDS"));
					else if("2".equals(object.optString("TICKET_TYPE"))){//月票
						tvMonthTicket.setText(context.getResources().getString(R.string.month_ticket_price)+object.optString("CHARGING_STANDARDS"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}else{
			imgIconUp.setVisibility(View.GONE);
			ticketLayout.setVisibility(View.GONE);
			tvFree.setVisibility(View.VISIBLE);
		}
		if(entity.getCreateCustomerID().equals(SmartFoxClient.getLoginUserId())){//是创建者
			btnFollowOperate.setImageResource(R.drawable.icon_topic_modify);
			btnFollowOperate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clickListener.onHeadIconClick(entity, position);
				}
			});
		}else{
			if(entity.isSalonAttention()){
				btnFollowOperate.setImageResource(R.drawable.icon_topic_unfollow);
			}else
				btnFollowOperate.setImageResource(R.drawable.icon_topic_follow);
			btnFollowOperate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(clickListener!=null){
						clickListener.onFollowClick(entity, position);
					}
				}
			});
		}
			
		mInstance.displayImage("4",entity.getNormalHeadIcon(), imgIcon);//4表示是群头像默认图片
		imgIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickListener.onHeadIconClick(entity, position);
			}
		});
		
		return convertView;
	}
	
	

}
