package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
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
public class DoctorAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<CustomerInfoEntity> entities;
	onClickFriendAttentionListener attentionListener;
	onClickFriendHeadListener clickFriendHeadListener;
	private ImageLoader mImageLoader;
	private Drawable attenDrawable,
			notAttenDrawable, dayTicketDrawable, monthTicketDrawable;
	// private boolean isMessageVisible = false;
	private ConcurrentHashMap<String, List<MessageEntity>> messageMap;
	private boolean isDisableAtt;
	private AppData appData;
 	public DoctorAdapter(Context context, List<CustomerInfoEntity> entities) {
		init(context, entities);
	}

	public DoctorAdapter(Context context,
			List<CustomerInfoEntity> entities, Boolean isDisableAtt) {
		init(context, entities);
		this.isDisableAtt = isDisableAtt;
		appData = HTalkApplication.getAppData();
//		messageMap=HTalkApplication.getAppData().messageCllection;
	}

	private void init(Context context, List<CustomerInfoEntity> entities) {
		inflater = LayoutInflater.from(context);
		this.entities = entities;
		mImageLoader = ImageLoader.getInstance();
		attenDrawable = context.getResources()
				.getDrawable(R.drawable.attention);
		notAttenDrawable = context.getResources().getDrawable(
				R.drawable.not_attention);
		dayTicketDrawable = context.getResources().getDrawable(
				R.drawable.day_ticket);
		monthTicketDrawable = context.getResources().getDrawable(
				R.drawable.month_ticket);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAdapterData(ArrayList<CustomerInfoEntity> entitie) {
		entities.clear();
		entities.addAll(entitie);
		notifyDataSetChanged();
	}

	// 获取翻页的标记
	public String getPageMark(boolean isFristPage) {
		if (entities.size() > 0 && !isFristPage) {
			return entities.get(entities.size() - 1).getFlag();
		} else {
			return "-100000";
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final viewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			holder = new viewHolder();
			convertView = inflater.inflate(R.layout.doctor_item, null);
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.head = (ImageView) convertView.findViewById(R.id.head_image);
			holder.personName = (TextView) convertView.findViewById(R.id.name);
			holder.v = (ImageView) convertView.findViewById(R.id.v);
			holder.title_departments = (TextView) convertView.findViewById(R.id.title_departments);
			holder.hospital = (TextView) convertView.findViewById(R.id.hospital);
			holder.special = (TextView) convertView.findViewById(R.id.special);
			holder.attentionSalon = (ImageView) convertView.findViewById(R.id.is_friend);
			holder.message = (TextView) convertView.findViewById(R.id.item_dele);
			holder.add = (TextView)convertView.findViewById(R.id.add);
			holder.level = (ImageView)convertView.findViewById(R.id.level);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		final CustomerInfoEntity mCustomerInfoEntity = entities.get(position);

		mImageLoader.displayImage(mCustomerInfoEntity.getSex(),mCustomerInfoEntity.getNormalHeadIcon(),
				holder.head);
		// 姓名
		holder.personName.setText(mCustomerInfoEntity.getName());
		// 是否是医师沙龙
		if (mCustomerInfoEntity.isShow()|| mCustomerInfoEntity.isAudit()) {
			holder.v.setVisibility(View.VISIBLE);
		} else {
			holder.v.setVisibility(View.GONE);
		}
		// 地址
//		if (!TextUtils.isEmpty(mCustomerInfoEntity.getDoctorWorkaddress())) {
//			holder.add.setText(mCustomerInfoEntity.getDoctorWorkaddress());
//		}else {
//			holder.add.setText("");
//		}
//		holder.add.setVisibility(View.GONE);
		// 消息
		holder.message.setVisibility(View.VISIBLE);
//		if (messageMap.contains(mCustomerInfoEntity.getId());
//				&& messageMap.get(mCustomerInfoEntity.getId()).size() != 0) {
//			holder.message.setText(messageMap.get(mCustomerInfoEntity.getId())
//					.size());
//		} else {
//			holder.message.setVisibility(View.GONE);
//		}
		int size = appData.getNoReadMesgSize(mCustomerInfoEntity.getId());
		if (size != 0) {
			String readCount=size >100 ? "99+":String.valueOf(size);
			holder.message.setText(readCount);
		} else {
			holder.message.setVisibility(View.GONE);
		}
		// 关注状态 0 无关系 1 我关注的 2 黑名单 3是客户 4医生
		if (mCustomerInfoEntity.getIsAttentionFriend() != 0
				&& mCustomerInfoEntity.getIsAttentionFriend() != 2) {
			holder.attentionSalon.setImageDrawable(notAttenDrawable);
		} else {
			holder.attentionSalon.setImageDrawable(attenDrawable);
		}
		if (!isDisableAtt) {
			// 是自己 不显示关注按钮
			if (mCustomerInfoEntity.getId().equals(
					SmartFoxClient.getLoginUserId())) {
				holder.attentionSalon.setVisibility(View.INVISIBLE);
			} else {
				holder.attentionSalon.setVisibility(View.VISIBLE);
			}
		}else {
			holder.attentionSalon.setVisibility(View.INVISIBLE);
		}


		// 职称/科室
		String office2 = mCustomerInfoEntity.getOfficeName2();
		if (!TextUtils.isEmpty(office2)) {
			office2 = ","+office2;
		}
		holder.title_departments.setText(mCustomerInfoEntity.getDoctorTitle()
				+ "/" + mCustomerInfoEntity.getOfficeName1()+"");
		// 医院
		holder.hospital.setText(mCustomerInfoEntity.getHospital());
		// 专长
		holder.special.setText(mCustomerInfoEntity.getSpecial());
		// 点击头像进入个人资料
		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (clickFriendHeadListener != null) {
					clickFriendHeadListener.onClickFriendHead(mCustomerInfoEntity, position);
				}
			}
		});

		// 点击取消和添加关注按钮
		holder.attentionSalon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (attentionListener != null) {
					attentionListener.onClickFriendAttention(
							mCustomerInfoEntity.getIsAttentionFriend(),
							mCustomerInfoEntity, position);
				}
			}
		});

//		if (mCustomerInfoEntity.getServicePrice().trim().equals("0元")) {
			holder.money.setText("");
//		} else {
//			holder.money.setText(mCustomerInfoEntity.getServicePrice());
//		}
			holder.level.setImageLevel(mCustomerInfoEntity.getDoctorLevel());
		return convertView;
	}

	private static class viewHolder {
		private TextView title_departments;// 职称/科室
		private TextView hospital;// 医院
		private TextView special;// 专长
		private ImageView head; // 头像
		private TextView personName; // 名称
		private ImageView v;
		private TextView message;// 消息的图标
		private ImageView attentionSalon;// 是否是关注的好友
		private TextView money;
		private TextView add;//地址
		private ImageView level;//医生等级
	}

	public interface onClickFriendHeadListener {
		void onClickFriendHead(CustomerInfoEntity entity, int positon);
	}

	public void setonClickFriendHeadListener(
			onClickFriendHeadListener headListener) {
		this.clickFriendHeadListener = headListener;
	};

	public interface onClickFriendAttentionListener {
		void onClickFriendAttention(int attentioned, CustomerInfoEntity entity,
				int positon);
	}

	public void setonClickFriendAttentionListener(
			onClickFriendAttentionListener attentionListener) {
		this.attentionListener = attentionListener;
	};
}
