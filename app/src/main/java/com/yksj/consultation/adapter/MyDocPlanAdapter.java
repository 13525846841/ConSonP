package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DocPlanFragment;
import com.yksj.healthtalk.entity.DocPlanEntity;
import com.yksj.healthtalk.entity.PlanEntity;
import com.yksj.healthtalk.utils.TimeUtil;

/**
 * Created by ${chen} on 2016/11/13.
 */
public class MyDocPlanAdapter extends SimpleBaseAdapter<PlanEntity> {
    private Context context;
    private LayoutInflater inflater;

    public MyDocPlanAdapter(Context context){
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datas.size();
    }
    @Override
    public int getItemResource() {
        return R.layout.item_doc_plan;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<PlanEntity>.ViewHolder holder) {
        TextView title = holder.getView(R.id.doc_plan_title);
        TextView time = holder.getView(R.id.doc_plan_time);
        TextView time_long = holder.getView(R.id.doc_plan_long);
        PlanEntity pEntity = datas.get(position);
        title.setText(pEntity.getPlan_title());

        time.setText(TimeUtil.getFormatDate2(pEntity.getStart_time()));
        time_long.setText(pEntity.getPlan_cycle()+"周");
        return convertView;
    }
//    @Override
//    public int getCount() {
//        return 2;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//

//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_doc_plan, null);
//            holder = new ViewHolder();
//            holder.title = (TextView) convertView.findViewById(R.id.doc_plan_title);
//            holder.time = (TextView) convertView.findViewById(R.id.doc_plan_time);
//            holder.time_long = (TextView) convertView.findViewById(R.id.doc_plan_long);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        return convertView;
//    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView title;//标题
        public TextView time;//开始时间
        public TextView time_long;//周期
    }
}
