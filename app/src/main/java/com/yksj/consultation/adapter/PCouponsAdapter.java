package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.bean.CouponBean;

import java.util.HashMap;

/**
 * 患者优惠券列表适配器
 * Created by lmk on 15/9/29.
 */
public class PCouponsAdapter extends SimpleBaseAdapter<CouponBean>{

    public PCouponsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.pconsult_coupon_list_item;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        CouponBean couponBean=datas.get(position);

//      TextView tvPrice= (TextView) convertView.findViewById(R.id.coupon_price);
//      TextView tvDate= (TextView) convertView.findViewById(R.id.coupon_effective_date);
//      TextView tvSatus= (TextView) convertView.findViewById(R.id.coupon_status);

        TextView money_number = holder.getView(R.id.money_number);
        TextView coupon_time= holder.getView(R.id.coupon_time);
        TextView coupon_place= holder.getView(R.id.coupon_place);
        final RadioButton coupon_select= holder.getView(R.id.iv_coupon);

        money_number.setText("¥"+couponBean.VALUE);
        coupon_time.setText(couponBean.STATUSNAME);
        coupon_place.setText(couponBean.AREA);
        if ("null".equals(couponBean.AREA)) {
            coupon_place.setText("全国地区可用");
        } else {
            coupon_place.setText(couponBean.AREA+"地区可用");
        }
//        if (couponBean.STATUS==1){
//            tvSatus.setText("已使用");
//        }else if (couponBean.STATUS==2){
//            tvSatus.setText("已完成");
//        }
        if (checked) {
            coupon_select.setSelected(true);
        } else {
            coupon_select.setSelected(false);
        }


        coupon_select.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // 重置，确保最多只有一项被选中
                for (String key : states.keySet()) {
                    states.put(key, false);

                }
                states.put(String.valueOf(position), coupon_select.isChecked());
                id = datas.get(position).ID;
                price = datas.get(position).VALUE;
                PCouponsAdapter.this.notifyDataSetChanged();
            }
        });

        boolean res = false;
        if (states.get(String.valueOf(position)) == null
                || states.get(String.valueOf(position)) == false) {
            res = false;
            states.put(String.valueOf(position), false);
        } else
            res = true;
        coupon_select.setChecked(res);

//        coupon_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checked ==false){
//                    checked = true;
//                    coupon_select.setSelected(true);
//                    id = datas.get(position).ID;
//                    price = datas.get(position).VALUE;
//                }else if (checked ==true){
//                    checked = false;
//                    id=0;
//                    price=0;
//                    coupon_select.setSelected(false);
//                }
//
//            }
//        });
        return convertView;
    }

    public int id(){
        return id;

    }
    public int price(){
        return price;
    }

    public int id;
    public int price;
    private boolean checked = false;

    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
}
