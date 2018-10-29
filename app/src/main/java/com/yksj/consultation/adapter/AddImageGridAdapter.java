package com.yksj.consultation.adapter;


import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.Bimp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddImageGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;
	private Context context;
	private int max=0;
	private String key;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public AddImageGridAdapter(Context context,String key) {
		this.context = context;
		this.key=key;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		if(Bimp.dataMap.get(key).size() == 8){
			return 9;
		}
		return (Bimp.dataMap.get(key).size()+ 1);
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position ==Bimp.dataMap.get(key).size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.img_create_topic_add_icon));
			if (position == 8) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.dataMap.get(key).get(position).getBitmap());
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}
	
}
