package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.main.OrderDetailActivity;
import com.yksj.consultation.son.consultation.main.ShopPayActivity;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/8/17.
 */
public class ShopOrderAdapter extends SimpleBaseAdapter<JSONObject>{

    private Context mContext;
    private String mOrderstatus = "";

    public ShopOrderAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_shop_order;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        TextView payOrder = holder.getView(R.id.gopay);
        TextView viewOrder = holder.getView(R.id.view_order);

        TextView goods = holder.getView(R.id.good_name);
        TextView goodsDetail = holder.getView(R.id.goods_detail);
        TextView orderStatus = holder.getView(R.id.order_status);


        ImageView imageGoods = holder.getView(R.id.image_goods);

        goods.setText(datas.get(position).optString("GOODS_NAME"));
        goodsDetail.setText("共"+datas.get(position).optString("GOOD_COUNT")+"件商品" + "  合计: ￥" +datas.get(position).optString("ORDER_GOLD_TOTAL")+ "(含运费"+datas.get(position).optString("FREIGHT")+"元)");
        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + datas.get(position).optString("GOOD_BIG_PIC");
        Picasso.with(mContext).load(url).placeholder(R.drawable.waterfall_default).into(imageGoods);


        if ("10".equals(mOrderstatus)){
            orderStatus.setText("待支付");
            payOrder.setText("待支付");
        }else if ("20".equals(mOrderstatus)){
            orderStatus.setText("待发货");
            payOrder.setText("提醒发货");
        }else if ("30".equals(mOrderstatus)){
            orderStatus.setText("待收货");
            orderStatus.setText("确认收货");
        }
        payOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext,ShopPayActivity.class);
                intent.putExtra("ORDERID",datas.get(position).optString("ORDER_ID"));
                mContext.startActivity(intent);
            }
        });

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("10".equals(datas.get(position).optString("ORDER_STATUS"))){
                    Intent intent  = new Intent(mContext,OrderDetailActivity.class);
                    intent.putExtra("ORDERID",datas.get(position).optString("ORDER_ID"));
                    mContext.startActivity(intent);
                } else if ("20".equals(datas.get(position).optString("ORDER_STATUS"))) {
                    ToastUtil.showShort("提醒发货");
                }else if ("30".equals(datas.get(position).optString("ORDER_STATUS"))) {
                    ToastUtil.showShort("确认收货");
                }
            }
        });
        return convertView;
    }
}
