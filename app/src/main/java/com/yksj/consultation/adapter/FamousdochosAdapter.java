package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/4.
 */
public class FamousdochosAdapter extends BaseAdapter {

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> mList = null;
    public FamousdochosAdapter( Context context) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fam_doc_hos, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.hospital);
            holder.headView = (ImageView) convertView.findViewById(R.id.picture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(mList.get(position).optString("HOSPITAL_NAME"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(mList.get(position).optString("HOSPITAL_NAME"));
        }

        //图片展示
        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + mList.get(position).optString("PICTURE_URL");
        Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(holder.headView);

        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public ImageView headView;

    }
    public void onBoundData(List<JSONObject> datas) {
        if (mList != null) {
            mList.clear();
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }
    public void removeAll(){
        this.mList.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<JSONObject> datas){
        this.mList.addAll(datas);
        notifyDataSetChanged();
    }
}
