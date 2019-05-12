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
import com.yksj.consultation.son.consultation.bean.Model;

import java.util.List;

/**
 * Created by ${chen} on 2016/11/29.
 */
public class PconsultmainGVAdapter extends BaseAdapter {
    private List<Model> mDatas;
    private Context context;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    public PconsultmainGVAdapter(Context context, List<Model> mDatas, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.context = context;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }
    public PconsultmainGVAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }
    /**
     * 先判断数据集的大小是否足够显示满本页？mDatas.size() > (curIndex+1)*pageSize,
     * 如果够，则直接返回每一页显示的最大条目个数pageSize,
     * 如果不够，则有几项返回几,(mDatas.size() - curIndex * pageSize);(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize，
         */
        int pos = position + curIndex * pageSize;
        viewHolder.tv.setText(mDatas.get(pos).name);
        //图片展示
       // String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+mDatas.get(pos).iconRes;
        Picasso.with(context).load("https://www.61120.vip/DuoMeiHealth/HeadDownLoadServlet.do?path="+mDatas.get(pos).iconRes).placeholder(R.drawable.waterfall_default).into(viewHolder.iv);
      //  Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(viewHolder.iv);
      //  viewHolder.iv.setImageResource(Integer.parseInt(String.valueOf(mDatas.get(pos).iconRes)));
        return convertView;
    }

    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
    public void onBoundData(List<Model> datas,int curIndex) {
        if (mDatas != null) {
            this.curIndex=curIndex;
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();

        }
    }

    public void setCurIndex(int curIndex) {
        this.curIndex = curIndex;
        notifyDataSetChanged();
    }
}
