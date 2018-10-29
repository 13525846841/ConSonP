package com.yksj.consultation.adapter;

import java.util.ArrayList;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.consultation.adapter.FriendAdapter.onClickFriendHeadListener;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;

public class FriendSearchAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;
	private Context context;
	private ImageLoader mImageLoader;
	private ArrayList<CustomerInfoEntity> entities;
	private Drawable femalDrawable;
	private Drawable maleDrawable;
	private Drawable unKownDrawable;
	private OnClickListener onClickListener;
	private Drawable attenDrawable;
	private Drawable notAttenDrawable;
	private onClickFriendHeadListener headListener;
	public FriendSearchAdapter(Context context,ArrayList<CustomerInfoEntity> entities) {
		init(context,entities);
	}

	private void init(Context context,ArrayList<CustomerInfoEntity> entities) {
		if (entities == null) {
			entities = new ArrayList<CustomerInfoEntity>();	
		}else {
			this.entities =entities;
		}
		if (context instanceof OnClickListener) {
			this.onClickListener = (OnClickListener) context;
		}
		if (context instanceof OnClickListener) {
			this.headListener = (onClickFriendHeadListener) context;
		}
		this.context = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
		femalDrawable = context.getResources().getDrawable(R.drawable.sex_women);
		maleDrawable = context.getResources().getDrawable(R.drawable.sex_man);
		unKownDrawable = context.getResources().getDrawable(R.drawable.sex_unknown);
		attenDrawable = context.getResources().getDrawable(R.drawable.icon_relation_attention);
		notAttenDrawable = context.getResources().getDrawable(R.drawable.icon_relation_cancel_attention);
	}

	public FriendSearchAdapter(Context context,ArrayList<CustomerInfoEntity> entities,Boolean isContact) {
		init(context,entities);
	}

	@Override
	public int getCount() {
		return entities.size();
	}
	

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.friend_search_by_contact_item, null);
			holder = new ViewHolder();
			holder.head = (ImageView) convertView.findViewById(R.id.head_image);
			holder.personName = (TextView) convertView.findViewById(R.id.name);
			holder.headSex = (ImageView) convertView.findViewById(R.id.head_sex);
			holder.note = (TextView) convertView.findViewById(R.id.note);
			holder.inivte = (Button)convertView.findViewById(R.id.invite);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CustomerInfoEntity  customerInfoEntity=entities.get(position);
		mImageLoader.displayImage(customerInfoEntity.getSex(),customerInfoEntity.getNormalHeadIcon(), holder.head);
		if (TextUtils.isEmpty(customerInfoEntity.getId())) {
			holder.personName.setText(customerInfoEntity.getPhonefriendName());
			holder.note.setText(customerInfoEntity.getPoneNumber());
			holder.inivte.setBackgroundResource(R.drawable.bt_short_double_green);
			holder.inivte.setText("邀请");
			holder.inivte.setTextColor(context.getResources().getColor(R.color.white));
			holder.headSex.setVisibility(View.GONE);
		}else {
			holder.headSex.setVisibility(View.VISIBLE);
			//签名
			holder.note.setText(customerInfoEntity.getDescription());
			//姓名
			holder.personName.setText(customerInfoEntity.getPhonefriendName()+"("+customerInfoEntity.getPoneNumber()+")");
			holder.inivte.setText("");
			if (customerInfoEntity.getIsAttentionFriend() !=0 && customerInfoEntity.getIsAttentionFriend() !=2) {
				holder.inivte.setBackgroundDrawable(notAttenDrawable);
			}else {
				holder.inivte.setBackgroundDrawable(attenDrawable);
			}
		}
		
		holder.inivte.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					v.setTag(position);
					onClickListener.onClick(v);
				}else {
					ToastUtil.showShort(context, "activity must be implements OnClickListener");
				}
				
			}
		});
		
		holder.head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (headListener != null) {
					headListener.onClickFriendHead(customerInfoEntity, position);
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		private TextView note;//描述
		private ImageView head; // 头像
		private TextView personName; // 名称
		private ImageView headSex; // 性别
		private Button inivte;//是否是关注的好友
	}
}
