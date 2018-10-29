package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.consultation.son.R;

public class MyAttentionListAdapter extends SimpleBaseAdapter<CustomerInfoEntity> {

	private ImageLoader mImageLoader;
	private HashMap<Integer, Boolean> selectedMap=new HashMap<Integer, Boolean>();//保存checkbox是否被选中的状态 
	
	public MyAttentionListAdapter(Context context) {
		super(context);
		mImageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getItemResource() {
		return R.layout.my_attention_friend_list_item;
	}

	@Override
	public View getItemView(final int position, View convertView,
			com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
		CustomerInfoEntity cus=datas.get(position);
		ImageView imgIcon=(ImageView) convertView.findViewById(R.id.my_attention_item_headicon);
		TextView tvTitle=(TextView) convertView.findViewById(R.id.my_attention_item_title);
		TextView tvDesc=(TextView) convertView.findViewById(R.id.my_attention_item_signature);
		CheckBox cb=(CheckBox) convertView.findViewById(R.id.my_attention_item_checkbox);
		mImageLoader.displayImage(cus.getSex(), cus.getNormalHeadIcon(),imgIcon);
		ImageView imgSex=(ImageView) holder.getView(R.id.find_list_item_sex);
		LevelListDrawable listDrawable = (LevelListDrawable)imgSex.getDrawable();
		listDrawable.setLevel(Integer.valueOf(cus.getSex()));
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectedMap.put(position, isChecked);
			}
		});
		if(selectedMap.containsKey(position))
			cb.setChecked(selectedMap.get(position));
		else
			cb.setChecked(false);
		tvDesc.setText(cus.getDescription());
		tvTitle.setText(cus.getNickName());
		return convertView;
	}
	
	//全选点击
	public void selectAllItem(boolean isChecked){
		if(isChecked){//全选
			for(int i=0;i<datas.size();i++){
				selectedMap.put(i, true);
			}
		}else{//全不选
			for(int i=0;i<datas.size();i++){
				selectedMap.put(i, false);
			}
		}
		notifyDataSetChanged();
	}
	
	//获取到所有被选中的项的id列表
	public ArrayList<String> getSelectedList(){
		ArrayList<String> ids=new ArrayList<String>();
		for(int i=0;i<datas.size();i++){
			if(selectedMap.containsKey(i)&&selectedMap.get(i))
				ids.add(datas.get(i).getId());
		}
		return ids;
	}

}
