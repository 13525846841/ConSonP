package com.yksj.consultation.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/8/18.
 */
public class ShopCouponAdapter extends SimpleBaseAdapter<JSONObject> {

    private int  type ;
    private String mCouponId = "";
    public ShopCouponAdapter(Context context,int type,String couponId) {
        super(context);
        this.type = type;
        this.mCouponId = couponId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {

        return R.layout.item_shop_coupon;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView content = holder.getView(R.id.coupon_content);
        TextView couponTime = holder.getView(R.id.coupon_time);
        TextView number = holder.getView(R.id.money_number);
        RelativeLayout background = holder.getView(R.id.coupon_background);
        ImageView couponIconUsed = holder.getView(R.id.icon_used);
        ImageView couponSelectIcon = holder.getView(R.id.couponicon);


        if (type ==2){
            background.setBackgroundResource(R.drawable.shop_coupon_ed);
            couponIconUsed.setVisibility(View.VISIBLE);

        }

        if (!HStringUtil.isEmpty(mCouponId)){
            if (mCouponId.equals(datas.get(position).optString("COUPONS_ID"))){
                couponSelectIcon.setVisibility(View.VISIBLE);
            }
        }
        content.setText(datas.get(position).optString("COUPONS_NAME"));
        couponTime.setText(TimeUtil.getTimeStr(datas.get(position).optString("USE_BEGIN_TIME").substring(0,8)) + "至" + TimeUtil.getTimeStr(datas.get(position).optString("USE_END_TIME").substring(0,8)));

        if ("20".equals(datas.get(position).optString("COUPONS_TYPE"))){
            String discount = datas.get(position).optString("COUPONS_VALUE")+"折";
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(discount);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30);
            spannableStringBuilder2.setSpan(absoluteSizeSpan, discount.length()-1, discount.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            number.setText(spannableStringBuilder2);
        }else {
            String cash = "￥" + datas.get(position).optString("COUPONS_VALUE");
            SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(cash);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30);
            spannableStringBuilder1.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            number.setText(spannableStringBuilder1);
        }

        return convertView;
    }
}
