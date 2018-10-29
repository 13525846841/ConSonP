package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

/**
 * Created by ${chen} on 2016/11/15.
 *
 *
 */
public class DetailDocAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public DetailDocAdapter(Context context){
        this.context =context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 3;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_det_doc, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_doc_name);
            holder.headView = (ImageView) convertView.findViewById(R.id.det_img_head);
            holder.pro = (TextView) convertView.findViewById(R.id.tv_doc_pro);
            holder.place = (TextView) convertView.findViewById(R.id.tv_doc_place);
            holder.goodsth = (TextView) convertView.findViewById(R.id.detail_doc_word);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public ImageView headView;
        public TextView pro;
        public TextView place;
        public TextView goodsth;
    }
}
