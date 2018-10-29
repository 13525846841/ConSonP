package com.yksj.consultation.adapter;

;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;
import org.json.JSONObject;

/**
 * Created by ${chen} on 2016/11/28.
 * 优惠卷适配器
 */

public class CouponCardAdapter extends SimpleBaseAdapter<JSONObject> {
    private Context context;
    private LayoutInflater inflater;

    public CouponCardAdapter(Context context){
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
        return R.layout.item_coupon_card;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<JSONObject>.ViewHolder holder) {
        TextView money_number = holder.getView(R.id.money_number);
        TextView coupon_time= holder.getView(R.id.coupon_time);
        TextView coupon_place= holder.getView(R.id.coupon_place);

        money_number.setText("¥"+datas.get(position).optString("COUPONS_VALUE"));
        coupon_time.setText(TimeUtil.getTimeStr(datas.get(position).optString("USE_BEGIN_TIME").substring(0,8)) + "至" + TimeUtil.getTimeStr(datas.get(position).optString("USE_END_TIME").substring(0,8)));
        if ("null".equals(datas.get(position).optString("AREA_NAME"))) {
            coupon_place.setText("全国地区可用");
        } else {
            coupon_place.setText(datas.get(position).optString("AREA_NAME")+"地区可用");
        }
        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView money_number;
        public TextView coupon_time;
        public TextView coupon_place;
    }
}
//    @Override
//    public View getItemView(int position, View convertView, SimpleBaseAdapter<PlanEntity>.ViewHolder holder) {
//        TextView title = holder.getView(R.id.doc_plan_title);
//        TextView time = holder.getView(R.id.doc_plan_time);
//        TextView time_long = holder.getView(R.id.doc_plan_long);
//        PlanEntity pEntity = datas.get(position);
//        title.setText(pEntity.getPlan_title());
//
//        time.setText(TimeUtil.getFormatDate2(pEntity.getStart_time()));
//        time_long.setText(pEntity.getPlan_cycle());
//        return convertView;
//    }

//public class CouponCardAdapter extends RecyclerView.Adapter<CouponCardAdapter.ViewHolder> {
//
//    private List<Actor> actors;
//
//    private Context mContext;
//    private List<JSONObject> list = new ArrayList<JSONObject>();
//    private Coupon coupon;
//    public CouponCardAdapter(Context context, List<JSONObject> list) {
//        this.mContext = context;
//        this.list = list;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        // 给ViewHolder设置布局文件
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coupon_card, viewGroup, false);
//        return new ViewHolder(v);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        // 给ViewHolder设置元素
//
//        viewHolder.money_number.setText("¥"+list.get(i).optString("COUPONS_VALUE"));
//        viewHolder.coupon_time.setText(TimeUtil.format(list.get(i).optString("USE_BEGIN_TIME")) + "至" + TimeUtil.format(list.get(i).optString("USE_END_TIME")));
//        if ("null".equals(list.get(i).optString("AREA_CODE"))) {
//            viewHolder.coupon_place.setText("全国地区可用");
//        } else {
//            viewHolder.coupon_place.setText(list.get(i).optString("AREA_CODE"));
//        }
//      //  viewHolder.mImageView.setImageDrawable(mContext.getDrawable(p.getImageResourceId(mContext)));
//    }
//
//    @Override
//    public int getItemCount() {
//        // 返回数据总数
//        return list == null ? 0 : list.size();
//    }
//
//    // 重写的自定义ViewHolder
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView money_number;
//      //  public ImageView mImageView;
//        public TextView coupon_place;
//        public TextView coupon_time;
//        public ViewHolder(View v) {
//            super(v);
//            money_number = (TextView) v.findViewById(R.id.money_number);
//            coupon_place = (TextView) v.findViewById(R.id.coupon_place);
//            coupon_time = (TextView) v.findViewById(R.id.coupon_time);
//          //mImageView = (ImageView) v.findViewById(R.id.pic111);
//        }
//    }
//    public void onBoundData(List<JSONObject> datas) {
//        if (list != null) {
//            list.clear();
//            list.addAll(datas);
//            notifyDataSetChanged();
//        }
//    }
//}
//
//    private Context context;
//    private LayoutInflater mIflatter;
//
//    public CouponCardAdapter(Context context){
//        this.context = context;
//        this.mIflatter = LayoutInflater.from(context);
//    }
//
////    @Override
////    public int getCount() {
////        return 2;
////    }
////
////    @Override
////    public Object getItem(int position) {
////        return null;
////    }
//
////    @Override
////    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////        return null;
////    }
////
////    @Override
////    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////
////    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mIflatter.inflate(R.layout.item_coupon_card, null);
//            holder = new ViewHolder();
////            holder.name = (TextView) convertView.findViewById(R.id.case_dis_comment_item_name);
////            holder.headView = (ImageView) convertView.findViewById(R.id.case_dis_comment_item_img);
////            holder.time = (TextView) convertView.findViewById(R.id.case_dis_comment_item_time);
////            holder.content = (TextView) convertView.findViewById(R.id.case_dis_comment_item_content);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//      //  holder.name.setText("陈某某");
//        return convertView;
//    }
//    /**
//     * 存放控件
//     */
////    public final class ViewHolder extends RecyclerView.ViewHolder {
////        public TextView name;
////        public ImageView headView;
////        public TextView time;
////        public TextView content;
////    }
//}
