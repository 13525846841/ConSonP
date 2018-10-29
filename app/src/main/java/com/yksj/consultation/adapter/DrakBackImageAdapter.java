package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.DensityUtils;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.views.MessageImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/5/2.
 */
public class DrakBackImageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    public List<ImageItem> mData =  new ArrayList<ImageItem>();
    private ImageLoader mInstance;

    public DrakBackImageAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mInstance = ImageLoader.getInstance();
    }
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getCount() {
        return mData.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            switch (type) {
                case 0://添加按钮
                    convertView = mInflater.inflate(R.layout.item_darkback_add, null);
                    holder.addHeadView = (ImageView) convertView.findViewById(R.id.addHeadview);
                    convertView.setTag(holder);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.item_db_image, null);
                    holder.headView = (ImageView) convertView.findViewById(R.id.chat_head);
                    convertView.setTag(holder);
                    break;
                default:
                    break;
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type){
            case 1:
                if (mData.size()!=0){
                    if (mData.get(position).isNetPic) {
                        mInstance.displayImage(mData.get(position).getThumbnailPath(),  holder.headView);
                    } else {
                        holder.headView.setImageBitmap(mData.get(position).getBitmap());
                    }
                    holder.headView.setImageBitmap(mData.get(position).getBitmap());
                }

                break;
        }

        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public ImageView headView;
        public ImageView addHeadView;
    }

    public void onBoundData(List<? extends ImageItem> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.mData.clear();
        notifyDataSetChanged();
    }

}
