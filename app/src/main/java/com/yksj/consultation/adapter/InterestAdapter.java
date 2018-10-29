package com.yksj.consultation.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.healthtalk.entity.TagEntity;
import com.yksj.consultation.son.R;
/**
 * 
 * @author Tang
 *
 */
public class InterestAdapter extends BaseAdapter {
	private List<TagEntity> list;
	private int resourceId;
	LayoutInflater mLayoutInflater;
	//isMyself 是否是自己感兴趣的   true是,将在最后添加+  resourceId传入的xml
	public InterestAdapter(Context context,List<TagEntity> list,int resourceId) {
		this.list=list;
		this.resourceId=resourceId;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void onBound(List<TagEntity> entities){
		list.clear();
		list.addAll(entities);
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = mLayoutInflater.inflate(resourceId,null);
		TextView button = (TextView)arg1.findViewById(R.id.text);
		button.setText(list.get(arg0).getName());
		return arg1;
	}
	
}
