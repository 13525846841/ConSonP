package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yksj.healthtalk.entity.SymptomEntity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.HumanBodyActivity;
/**
 * 
 * @author zhao
 *
 */
public class SymptomAdapter extends BaseAdapter {
	List<String> mTagList = new ArrayList<String>();//标签
	List<SymptomEntity> mList = new ArrayList<SymptomEntity>();
	
	List<SymptomEntity> mSelectedList = new ArrayList<SymptomEntity>();//选中列表
	
	final int TYPECONTENT = 2;
	
	Context mContext;
	
	LayoutInflater mLayoutInflater;
	
	public SymptomAdapter(Context context){
		mContext = context;
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	public List<SymptomEntity> getmSelectedList() {
		return mSelectedList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SymptomEntity getItem(int arg0) {
		return mList.get(arg0);
	}

	public boolean isTag(int position){
		return mTagList.contains(mList.get(position).getName());
	}
	 
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup group) {
		ViewHolder viewHolder;
		
		int type = getItemViewType(position);
		final SymptomEntity symptomEntity  = getItem(position);
		
		if(view == null){
			viewHolder = new ViewHolder();
			if(type == 1){//标签
				view  = mLayoutInflater.inflate(R.layout.symptom_list_item_tag,null);
				TextView textView = (TextView)view.findViewById(R.id.list_textv_tag);
				viewHolder.contentTextV = textView;
				view.setTag(viewHolder);
			}else{
				view  = mLayoutInflater.inflate(R.layout.symptom_list_item,null);
				viewHolder.checkBox = (CheckBox)view.findViewById(R.id.symptom_chebox);
				viewHolder.contentTextV = (TextView)view.findViewById(R.id.symptom_content);
				viewHolder.detailTextV = (TextView)view.findViewById(R.id.symptom_detail);
				view.setTag(viewHolder);
			}
		}else{
			viewHolder = (ViewHolder)view.getTag();
		}
		
		if(type == 1){
			viewHolder.contentTextV.setText(symptomEntity.getName());
		}else{
			viewHolder.contentTextV.setText(symptomEntity.getName());
			viewHolder.checkBox.setChecked(symptomEntity.isSelected());
			viewHolder.detailTextV.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((HumanBodyActivity)mContext).queryContent(symptomEntity.getCode(),symptomEntity.getName());
				}
			});
		}
		return view;
	}
	
	
	@Override
	public int getItemViewType(int position) {
		int type = 0;
		if(isTag(position))
			type = 1;//type表示是标签
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return TYPECONTENT;
	}
	
	/**
	 * 改变选择状态
	 */
	public void changeSelectedState(View view,int position,LinkedHashMap<String,List<SymptomEntity>> map){
		SymptomEntity symptomEntity = mList.get(position);
		//大部位编码
		String positionName = symptomEntity.getPositionName();
		ViewHolder viewHolder = (ViewHolder)view.getTag();
		
		boolean isCheck = viewHolder.checkBox.isChecked();
		if(isCheck){
			symptomEntity.setSelected(false);
			viewHolder.checkBox.setChecked(false);
			mSelectedList.remove(symptomEntity);
		}else{
			symptomEntity.setSelected(true);
			viewHolder.checkBox.setChecked(true);
			mSelectedList.add(symptomEntity);
		}
		//包含
		if(map.containsKey(positionName)){
			List<SymptomEntity> list = map.get(positionName);
			if(symptomEntity.isSelected() && !list.contains(symptomEntity)){
				list.add(symptomEntity);
			}else if(!symptomEntity.isSelected() && list.contains(symptomEntity)){
				list.remove(symptomEntity);
				if(list.size()<=0)map.remove(positionName);
			}
		}else{
			List<SymptomEntity> list = new ArrayList<SymptomEntity>();
			list.add(symptomEntity);
			map.put(positionName, list);
		}
	}
	
	/**
	 * 检查选中的数据
	 * @param map
	 */
/*	public void checkSelectedList(LinkedHashMap<String,List<SymptomEntity>> map){
		map.
		if(map.containsKey(positionName)){
			List<SymptomEntity> list = map.get(positionName);
			if(symptomEntity.isSelected()){
				list.add(symptomEntity);
			}else{
				list.remove(symptomEntity);
			}
		}else{
			List<SymptomEntity> list = new ArrayList<SymptomEntity>();
			list.add(symptomEntity);
			map.put(positionName, list);
		}
	}*/
	
	public List<SymptomEntity> getListData(){
		return mList;
	}
	
	/**
	 * 改变数据源
	 * @param list
	 * @param tagList
	 */
	public void changeDataSource(List<SymptomEntity> list,List<String> tagList){
		//mList.clear();
		//mTagList.clear();
		//mTagList.addAll(tagList);
		//mList.addAll(list);
		mSelectedList.clear();
		mList= list;
		mTagList = tagList;
		notifyDataSetChanged();
	}
	
	/**
	 * 响应标签事件
	 */
	@Override
	public boolean isEnabled(int position) {
		return !isTag(position);
	}

	static class ViewHolder{
		CheckBox checkBox;//选中标记
		TextView contentTextV;//内容,分类标签
		TextView detailTextV;//详细
	}
	
}
