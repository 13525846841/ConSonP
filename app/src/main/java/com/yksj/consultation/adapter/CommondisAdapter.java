package com.yksj.consultation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/4.
 */
public class CommondisAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> mList =null;

    public CommondisAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        mList= new ArrayList<JSONObject>();
    }
    @Override
    public int getCount() {
        return mList.size();
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
            convertView = mInflater.inflate(R.layout.item_com_dis, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txt_disease_name);
            holder.photo=(ImageView)convertView.findViewById(R.id.img_disease) ;
            holder.content=(TextView)convertView.findViewById(R.id.txt_disease_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position).optString("DISEASE_NAME"));
        String string = mList.get(position).optString("EXPLAIN");
        String substring = string.substring(10);
        holder.content.setText(substring);
        Picasso.with(context).load("https://www.61120.vip/DuoMeiHealth/HeadDownLoadServlet.do?path="+mList.get(position).optString("PICTURE_URL")).placeholder(R.drawable.waterfall_default).into(holder.photo);
        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public ImageView photo;
        public TextView name;
        public TextView content;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (mList != null) {
            mList.clear();
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
