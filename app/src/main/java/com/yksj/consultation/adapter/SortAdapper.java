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
 * Created by ${chen} on 2017/3/13.
 */
public class SortAdapper  extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private String[] NAME = {"价格由高到低","人气由高到低"};
    onClickMsgeListener clickdeleteMsgeListener;

    public SortAdapper(Context context){
        this.context =context;
        this.mInflater = LayoutInflater.from(context);
    }
    public interface onClickMsgeListener{
        void onClickMsg(View view, int positon);
    }
    public void setonClickMsgeListener(onClickMsgeListener attentionListener){
        this.clickdeleteMsgeListener = attentionListener;
    }

    public String NAME(int position){

        return NAME[position];
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return NAME[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder= new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pop_pro, null);
            holder.name = (TextView) convertView.findViewById(R.id.pop_place);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final View view = convertView;
        final int pos = position;
        holder.name.setText(NAME[position]);
       // clickdeleteMsgeListener.onClickMsg(view,pos);
        return convertView;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public TextView utils;
        public ImageView utils_bg;
    }
}
