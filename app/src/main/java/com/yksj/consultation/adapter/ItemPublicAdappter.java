package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/11/21.
 */
public class ItemPublicAdappter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    public ItemPublicAdappter(List<JSONObject> list, Context context) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
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
            convertView = mInflater.inflate(R.layout.item_item_pub, null);
            holder = new ViewHolder();
            holder.headView = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
    // 图片展示
    Picasso.with(context).load(list.get(position).optString("PICTURE_URL_2X")).placeholder(R.drawable.waterfall_default).into(holder.headView);
        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public ImageView headView;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
