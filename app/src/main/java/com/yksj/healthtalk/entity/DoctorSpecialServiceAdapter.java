package com.yksj.healthtalk.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;

public class DoctorSpecialServiceAdapter extends BaseAdapter {

	public List<DoctorServiceGroupEntity> list = new ArrayList<DoctorServiceGroupEntity>();
	private LayoutInflater layoutInflater;
	OnUpdateClickListener clickListener;
	private final Drawable upDrawable,downDrawable,middleDrawable,singleDrawable;
	public interface OnUpdateClickListener{
		void onUpdateClick(DoctorServiceGroupEntity mEntity);
	}
	
	public DoctorSpecialServiceAdapter(List<DoctorServiceGroupEntity> list,OnClickListener listener,Context context){
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		this.clickListener=(OnUpdateClickListener) listener;
		upDrawable=context.getResources().getDrawable(R.drawable.textview_background_up);
		downDrawable=context.getResources().getDrawable(R.drawable.textview_background_down);
		middleDrawable=context.getResources().getDrawable(R.drawable.textview_background_middle);
		singleDrawable=context.getResources().getDrawable(R.drawable.text_background_single);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final DoctorServiceGroupEntity mEntity = list.get(position);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.doctor_special_service__group_item, null);
		}
		TextView view = (TextView) convertView.findViewById(R.id.name);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		view.setText(mEntity.getSPECIAL_GROUP());
		price.setText(mEntity.getSPECIAL_PRICE()+"元");
		
		if (position == 0) {
			convertView.setBackgroundDrawable(upDrawable);
		} else if (position == list.size() - 1) {
			convertView.setBackgroundDrawable(downDrawable);
		} else {
			convertView.setBackgroundDrawable(middleDrawable);
		}
		if (list.size() == 1)
			convertView.setBackgroundDrawable(singleDrawable);
		
		TextView mStatus=(TextView) convertView.findViewById(R.id.status);
		mStatus.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						clickListener.onUpdateClick(mEntity);
					}
				});
		return convertView;
	}

	public void boundData(List<DoctorServiceGroupEntity> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}
}