package com.yksj.consultation.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import com.yksj.consultation.son.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private ImageLoader mInstance;
	private DisplayImageOptions mOptions;
	private ArrayList<HashMap<String, String>> list;

	public ImageGridAdapter(Context context, ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		mInstance = ImageLoader.getInstance();
		mOptions = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			mInstance.displayImage(list.get(position).get("SMALL").toString(), holder.image, mOptions);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

}
