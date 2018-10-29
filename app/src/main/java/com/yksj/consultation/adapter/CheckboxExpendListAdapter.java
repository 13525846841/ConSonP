package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

public class CheckboxExpendListAdapter extends BaseExpandableListAdapter {
	
	List mChoosedList = new ArrayList();//选中集合
	List<List<Map>> mChildList = new ArrayList<List<Map>>();
	List<String> mGroupList = new ArrayList<String>();
	LayoutInflater mInflater;
	private final ImageLoader mImageLoader;
	
	public CheckboxExpendListAdapter(LayoutInflater inflater,Context context){
		this.mInflater = inflater;
		this.mImageLoader = ImageLoader.getInstance();
	}
	
	public List getChoosesList(){
		return mChoosedList;
	}
	
	public void onChoosedAll(){
		mChoosedList.clear();
		for (List listMaps : mChildList) {
			mChoosedList.addAll(listMaps);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getGroupCount() {
		return mGroupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.chat_lately_group_title_layout, null);
			holder.nameTextV = (TextView)convertView.findViewById(R.id.group_name);
			holder.mesgTextV = (TextView)convertView.findViewById(R.id.group_msg_number);
			holder.levelImageV = (ImageView)convertView.findViewById(R.id.group_arrow);
			holder.mesgTextV.setVisibility(View.GONE);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RotateDrawable drawable = (RotateDrawable)holder.levelImageV.getDrawable();
		if(isExpanded){
			drawable.setLevel(10000);
		}else{
			drawable.setLevel(0);
		}
		holder.nameTextV.setText((String)getGroup(groupPosition));
		return convertView;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent){
		final ViewHolder viewHolder;
		final Map<String, String> entity = (Map)getChild(groupPosition, childPosition);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.commn_checkbox_list_item,null);
			viewHolder.headerImageV = (ImageView)convertView.findViewById(R.id.head_image);
			viewHolder.nameTextV = (TextView)convertView.findViewById(R.id.name);
			viewHolder.sexImageV = (ImageView)convertView.findViewById(R.id.head_sex);
			viewHolder.levelImageV = (ImageView)convertView.findViewById(R.id.levl);
			viewHolder.noteTextV = (TextView)convertView.findViewById(R.id.note);
			viewHolder.chooseBox = (CheckBox)convertView.findViewById(R.id.choose);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.nameTextV.setText(entity.get("name"));
		viewHolder.chooseBox.setChecked(mChoosedList.contains(entity));
		viewHolder.chooseBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox box  = (CheckBox)v;
				if(box.isChecked()){
					mChoosedList.add(entity);
				}else{
					mChoosedList.remove(entity);
				}
			}
		});
		mImageLoader.displayImage("0", entity.get("headerUrl"),viewHolder.headerImageV);
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView headerImageV;
		TextView nameTextV;
		TextView mesgTextV;
		ImageView sexImageV;
		ImageView levelImageV;
		TextView noteTextV;
		CheckBox chooseBox;
	}

	
}
