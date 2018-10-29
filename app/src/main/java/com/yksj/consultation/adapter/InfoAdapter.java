package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

public class InfoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater inflater;
	private boolean isSingle = false; //是否单选
	private int layoutID = R.layout.attention_item;
	private boolean isArrows = false;
	private boolean isFristChecked = false;
	private boolean isInfoLayNameVisible = false;
	private ArrayList<Integer> selects = new ArrayList<Integer>();
	private String infoLayName;
	private int upperPosition;
	private boolean isHasLevel = false;//在服务中心用到
	private final Drawable isSecterPic;
	public InfoAdapter(Context context, ArrayList<HashMap<String, Object>> data,boolean isSingle) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
		this.isSingle = isSingle;
		isSecterPic=context.getResources().getDrawable(R.drawable.selector_on);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attention_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.att_title);
			holder.content = (TextView) convertView.findViewById(R.id.att_content);
			holder.icon = (ImageView) convertView.findViewById(R.id.att_icon);
			holder.infoLayName = (TextView)convertView.findViewById(R.id.info_name);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		
		if (!isArrows) {
			holder.icon.setImageDrawable(isSecterPic);
			if(selects.contains(position))
				holder.icon.setVisibility(View.VISIBLE);
			else
				holder.icon.setVisibility(View.GONE);
		}else {
			//服务中心用到
			if (isHasLevel) {
				holder.icon.setImageResource(R.drawable.arrows);
				if(selects.contains(position))
					holder.icon.setVisibility(View.VISIBLE);
				else
					holder.icon.setVisibility(View.GONE);
			}else {
				holder.icon.setVisibility(View.VISIBLE);
			}
		}
		
		if (isFristChecked ==true&&position ==0) {
			holder.icon.setImageResource(R.drawable.selector_on);
		}else if(isFristChecked ==true) {
			holder.icon.setImageResource(R.drawable.arrows);
		}
		
		if (isInfoLayNameVisible()) {
			if (position == 0) {
				holder.icon.setVisibility(View.GONE);
			}else if(position == getUpperPosition()){
				holder.infoLayName.setText(getInfoLayName());
			}else {
				holder.infoLayName.setText("");
			}
		}else {
			holder.infoLayName.setText("");
		}
		
		HashMap<String, Object> map = data.get(position);
		holder.title.setText((String)map.get("title"));
		holder.content.setText((String)map.get("content"));
		setBackGroud(convertView, position);
		return convertView;
	}
	
	public void setLayoutID(int layoutID) {
		this.layoutID = layoutID;
	}
	
	private void setBackGroud(View view,int position){
		if(position == 0)
			view.setBackgroundResource(R.drawable.textview_background_up);
		else if(position == (data.size()-1))
			view.setBackgroundResource(R.drawable.textview_background_down);
		else if(1== data.size()){
			view.setBackgroundResource(R.drawable.text_background_single);
		}else
			view.setBackgroundResource(R.drawable.textview_background_middle);
	}
	
	private class ViewHolder{
		TextView title;
		TextView content;
		ImageView icon;
		TextView infoLayName;
	}
	
	public void seticonImagView(Boolean isArrows){
		this.isArrows = isArrows;
	}
	
	//放表设置第一个"全部"选项 打沟
	public void seticonImagViewExceptTheFirst(Boolean isFristChecked){
		this.isFristChecked = isFristChecked;
	}
	
	public Boolean geticonImagViewExceptTheFirst(Boolean isFristChecked){
		return isFristChecked;
	}
	
	
	public boolean isInfoLayNameVisible() {
		return isInfoLayNameVisible;
	}

	public void setInfoLayNameVisible(boolean isInfoLayNameVisible) {
		this.isInfoLayNameVisible = isInfoLayNameVisible;
	}
	
	

	public boolean isHasLevel() {
		return isHasLevel;
	}

	public void setHasLevel(boolean isHasLevel) {
		this.isHasLevel = isHasLevel;
	}

	/**
	 * 显示小类的名称
	 * @param position
	 * @param name
	 */
	public void setInfoLayName(int position , String name){
		this.upperPosition = position;
		this.infoLayName = name;
	}
	
	/**
	 * 小类名称
	 * @return
	 */
	public String getInfoLayName(){
		return infoLayName;
		
	}
	
	
	/**
	 * 大类索引值
	 * @return
	 */
	public int getUpperPosition(){
		return upperPosition;
	}
	
	
	public void addSelect(Integer position){
		selects.add(position);
	}
	
	public void addSelectAll(Collection<Integer> collection){
		selects.addAll(collection);
	}
	
	public void removeSelect(Integer position){
		if(!isSingle)
		    selects.remove(position);
		else
			selects.clear();
	}
	
	public ArrayList<Integer> getSelects(){
		return selects;
	}
}
