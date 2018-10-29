package com.yksj.consultation.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yksj.healthtalk.entity.ServiceContent;
import com.yksj.consultation.son.R;

public class DoctorServiceAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<ServiceContent> contents;

	public DoctorServiceAdapter(Context context,ArrayList<ServiceContent> contents) {
		inflater = LayoutInflater.from(context);
		this.contents = contents;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.doctor_service_item, null);
			holder.name=(TextView) convertView.findViewById(R.id.name);
			holder.content=(TextView) convertView.findViewById(R.id.content);
			holder.detail=(ImageButton) convertView.findViewById(R.id.detail);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		ServiceContent content = contents.get(position);
		holder.name.setText(content.getSERVICE_CONTENT());
		holder.content.setText(content.getSERVICE_DESC());
		return convertView;
	}
	
	class ViewHolder{
		private TextView name;
		private TextView content;
		private ImageButton detail;
	}
}
