package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

/**
 * 用于适配二级菜单中的导航列表
 * @author lmk
 *
 */
public class NavigationListAdapter extends BaseAdapter {

	public List<Map<String, String>> datas =new ArrayList<Map<String,String>>();//数据
	private Context context;
	private int mSelectedPosition = -1,currentPosition,mLeftPosition=-1,mRightPosition=-1; // 选中的item位置
	private boolean isRight=false;//是否是适配左边的ListView
	
	public NavigationListAdapter(Context context) {
		super();
		this.context = context;
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("name","全部");
		map.put("code","0");
		datas.add(map);
	}

	public void clearDatas(){
		datas.clear();
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("name","全部");
		map.put("code","0");
		datas.add(map);
		notifyDataSetChanged();
	}
	
	public void setDatas(List<Map<String, String>> datas){
		if(datas==null)return;
		this.datas.clear();
		this.datas.addAll(datas);
		notifyDataSetChanged();
	}
	

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getmSelectedPosition() {
		return mSelectedPosition;
	}

	public void setmSelectedPosition(int mSelectedPosition,View convertView) {
		this.mSelectedPosition = mSelectedPosition;
	}
	
	

	public int getmLeftPosition() {
		return mLeftPosition;
	}

	public void setmLeftPosition(int mLeftPosition) {
		this.mLeftPosition = mLeftPosition;
	}

	public int getmRightPosition() {
		return mRightPosition;
	}

	public void setmRightPosition(int mRightPosition) {
		this.mRightPosition = mRightPosition;
	}

	public boolean isLeft() {
		return isRight;
	}

	public void setLeft(boolean isRight) {
		this.isRight = isRight;
	}

	@Override
	public int getCount() {
		
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.navigation_list_item, null);
			holder=new ViewHolder();
			holder.tv=(TextView) convertView.findViewById(R.id.navigation_item_name);
			holder.img=(ImageView) convertView.findViewById(R.id.navigation_item_img_index);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.tv.setText(datas.get(position).get("name"));
		if(isRight){
			holder.img.setVisibility(View.INVISIBLE);//使向右箭头不可见
			if(currentPosition==mLeftPosition&&position==mRightPosition){
				holder.tv.setTextColor(context.getResources().getColor(R.color.color_green_blue_bg));
			}else{
				holder.tv.setTextColor(context.getResources().getColor(R.color.selector_index_color));
			}
		}
		if(mSelectedPosition==position){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.color_index_gray_bg));
		}else{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		}
		return convertView;
	}


	static class ViewHolder{
		TextView tv;
		ImageView img;
	}
}
