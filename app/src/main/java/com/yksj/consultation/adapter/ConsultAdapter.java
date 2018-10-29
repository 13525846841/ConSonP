package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.consultation.main.AppraiseTuAty;
import com.yksj.consultation.son.consultation.main.DarkBackActivity;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/4/5.
 */
public class ConsultAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    public Context context;
    public List<JSONObject> list = new ArrayList<JSONObject>();
    private String type;

    public ConsultAdapter(List<JSONObject> list, Context context) {
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_consult, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_doc_name);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.imageview = (ImageView) convertView.findViewById(R.id.det_img_head);
            holder.tvDocPro = (TextView) convertView.findViewById(R.id.tv_doc_pro);
            holder.tvDocAccount = (TextView) convertView.findViewById(R.id.tv_doc_account);
            holder.time_long = (TextView) convertView.findViewById(R.id.tv_circle);
            holder.order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            holder.service_money = (TextView) convertView.findViewById(R.id.tv_order_money);
            holder.startTime = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.darkback = (TextView) convertView.findViewById(R.id.darkback);
            holder.darkbacktext = (TextView) convertView.findViewById(R.id.darkbacktext);
            holder.endTime = (TextView) convertView.findViewById(R.id.end_time);
            holder.detail_content = (RelativeLayout) convertView.findViewById(R.id.detail_content);
            holder.rl_darkback = (RelativeLayout) convertView.findViewById(R.id.rl_darkback);
            holder.talkNumber = (TextView) convertView.findViewById(R.id.talk_number);
            holder.refuseReason = (TextView) convertView.findViewById(R.id.refuse_reason);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (list.get(position).optString("isBack").equals("0")) {//订单已支付可退款
            holder.darkback.setVisibility(View.VISIBLE);
            holder.darkbacktext.setVisibility(View.GONE);
        } else if (list.get(position).optString("isBack").equals("1")) {//正在退款中
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.VISIBLE);
        } else if (list.get(position).optString("isBack").equals("2")) {//退款成功
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.VISIBLE);
            holder.darkbacktext.setText("退款成功");
        } else if (list.get(position).optString("isBack").equals("5")) {
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.VISIBLE);
            holder.darkbacktext.setText("待支付");

        } else if (list.get(position).optString("isBack").equals("6")) {
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.VISIBLE);
            holder.darkbacktext.setText("支付失败");
        } else if (list.get(position).optString("isBack").equals("3")) {//退款失败
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.VISIBLE);
            holder.darkbacktext.setText("退款失败");
            if (!HStringUtil.isEmpty(list.get(position).optString("refuse_reason"))) {
                holder.refuseReason.setVisibility(View.VISIBLE);
                holder.refuseReason.setText("退款失败原因: " + list.get(position).optString("refuse_reason"));
            }
        } else if (list.get(position).optString("isBack").equals("4")) {//订单结束 可评价
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.GONE);
            if (list.get(position).optBoolean("isEnd")) {
                holder.darkback.setVisibility(View.VISIBLE);
                holder.darkbacktext.setVisibility(View.GONE);
                if (list.get(position).has("isComment")) {
                    if (list.get(position).optBoolean("isComment")) {
                        holder.darkback.setText("已评价");
                    } else {
                        holder.darkback.setText("去评价");
                    }
                } else {
                    holder.darkback.setText("已完成");
                }

            }
        }


        if ("9".equals(type)) {
            holder.darkback.setVisibility(View.GONE);
            holder.darkbacktext.setVisibility(View.GONE);
        }


        final String order_number = list.get(position).optString("PAY_ID");
        String service_money = list.get(position).optString("SERVICE_GOLD");


        if (HStringUtil.isEmpty(list.get(position).optString("PAY_ID"))) {
            holder.order_number.setVisibility(View.GONE);
        } else {
            holder.order_number.setText("订单号: " + list.get(position).optString("PAY_ID"));
        }


        holder.service_money.setText("服务费用: " + list.get(position).optString("SERVICE_GOLD") + "元");

        if (HStringUtil.isEmptyAndZero(list.get(position).optString("overplusCount"))) {
            holder.talkNumber.setVisibility(View.GONE);
        } else {
            holder.talkNumber.setText("剩余服务条数: " + list.get(position).optString("overplusCount") + "条");
        }


        //  holder.talkNumber.setText("剩余服务条数: "+list.get(position).optString("overplusCount")+ "条");

        if (!HStringUtil.isEmpty(list.get(position).optString("SERVICE_START"))) {
            holder.startTime.setVisibility(View.VISIBLE);
            holder.endTime.setVisibility(View.VISIBLE);
            holder.startTime.setText("服务开始时间: " + TimeUtil.getFormatDate(list.get(position).optString("SERVICE_START")));
            holder.endTime.setText("服务结束时间: " + TimeUtil.getFormatDate(list.get(position).optString("SERVICE_END")));

        } else {
            holder.startTime.setVisibility(View.GONE);
            holder.endTime.setVisibility(View.GONE);
        }

        if (!list.get(position).optBoolean("isEnd") && !list.get(position).optString("isBack").equals("5")) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText("距离结束还有" + list.get(position).optString("LASTTIME"));
        } else {
            holder.title.setVisibility(View.GONE);
        }


        if (list.get(position).optBoolean("isEnd")) {
            holder.time_long.setText("已结束");
        } else {
            if (!list.get(position).optString("isBack").equals("5")) {
                if (ServiceType.TW.equals(type) || ServiceType.BY.equals(type)) {
                    holder.time_long.setText("服务周期: " + list.get(position).optString("cycle"));
                } else {
                    holder.time_long.setVisibility(View.GONE);
                }
            } else {
                holder.time_long.setVisibility(View.GONE);
            }
        }

        String tvDocPro = "";
        String name = "";
        String tvDocAccount = "";
        String doctor_id = "";
        String url = "";

        try {
            tvDocPro = list.get(position).getJSONObject("doctor_info").optString("TITLE_NAME");
            name = list.get(position).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
            tvDocAccount = list.get(position).getJSONObject("doctor_info").optString("OFFICE_NAME");
            doctor_id = list.get(position).getJSONObject("doctor_info").optString("CUSTOMER_ID");

            holder.tvDocPro.setText(tvDocPro);
            holder.name.setText(name);
            holder.tvDocAccount.setText(tvDocAccount);
            //图片展示
            url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + list.get(position).getJSONObject("doctor_info").optString("DOCTOR_PICTURE");
            Picasso.with(context).load(url).placeholder(R.drawable.default_head_doctor).into(holder.imageview);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        if(HStringUtil.isEmpty(list.get(position).optString("site"))){
//            try {
//                tvDocPro = list.get(position).getJSONObject("doctor_info").optString("TITLE_NAME");
//                name = list.get(position).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
//                tvDocAccount = list.get(position).getJSONObject("doctor_info").optString("OFFICE_NAME");
//                doctor_id = list.get(position).getJSONObject("doctor_info").optString("CUSTOMER_ID");
//
//                holder.tvDocPro.setText(tvDocPro);
//                holder.name.setText(name);
//                holder.tvDocAccount.setText(tvDocAccount);
//                //图片展示
//                String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+list.get(position).getJSONObject("doctor_info").optString("DOCTOR_PICTURE");
//                Picasso.with(context).load(url).placeholder(R.drawable.default_head_doctor).into(holder.imageview);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else {
//            try {
//                tvDocPro = list.get(position).getJSONObject("site").optString("TITLE_NAME");
//                name = list.get(position).getJSONObject("site").optString("SITE_NAME");
//                tvDocAccount = list.get(position).getJSONObject("site").optString("OFFICE_NAME");
//                doctor_id = list.get(position).getJSONObject("site").optString("CUSTOMER_ID");
//
//                holder.tvDocPro.setText(tvDocPro);
//                holder.name.setText(name);
//                holder.tvDocAccount.setText(tvDocAccount);
//
//                //图片展示
//                String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+list.get(position).getJSONObject("site").optString("SITE_BIG_PIC");
//                Picasso.with(context).load(url).placeholder(R.drawable.default_head_doctor).into(holder.imageview);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

//
        final String finalName = name;
        final String finalTvDocPro = tvDocPro;
        final String finalTvDocAccount = tvDocAccount;//科室
        final String finalDoctor_id = doctor_id;
        final String finalUrl = url;

        holder.darkback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("4".equals(list.get(position).optString("isBack"))) {
                    if (list.get(position).has("isComment")) {
                        Intent intent = new Intent(context, AppraiseTuAty.class);//订单完成  去评价 .
                        intent.putExtra(AppraiseTuAty.ISCOMMENT, list.get(position).optBoolean("isComment"));
                        intent.putExtra("ORDER_ID", list.get(position).optString("ORDER_ID"));
                        try {
                            if (!HStringUtil.isEmpty(list.get(position).optString("site"))) {
                                intent.putExtra(AppraiseTuAty.SITE_ID, list.get(position).getJSONObject("site").optString("SITE_ID"));
                            }
                            intent.putExtra("NAME", finalName);
                            intent.putExtra("TITLE", finalTvDocPro);
                            intent.putExtra("DOCTORID", finalDoctor_id);
                            intent.putExtra("IMAGE", finalUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(context, DarkBackActivity.class);
                    intent.putExtra("doctor_id", finalDoctor_id);
                    intent.putExtra("order_id", list.get(position).optString("ORDER_ID"));
                    intent.putExtra("name", finalName);
                    intent.putExtra("title", finalTvDocPro);
                    intent.putExtra("order_number", order_number);
                    intent.putExtra("service_money", list.get(position).optString("SERVICE_GOLD"));
                    intent.putExtra("tvDocAccount", finalTvDocAccount);
                    intent.putExtra("service_type_id", list.get(position).optString("SERVICE_TYPE_ID"));
                    context.startActivity(intent);
                }

            }
        });

        return convertView;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView title;
        public ImageView imageview;
        public TextView name;
        public TextView tvDocPro;//职称
        public TextView tvDocAccount;//科室
        public TextView endTime;//结束时间
        public TextView time_long;//周期
        public TextView order_number;//订单号
        public TextView service_money;//服务费用
        public TextView startTime;//服务费用
        public TextView talkNumber;//剩余条数
        public TextView refuseReason;//拒绝原因

        public TextView darkback;//申请退款
        public TextView darkbacktext;//正在退款中
        public RelativeLayout detail_content;//订单详情数据
        public RelativeLayout rl_darkback;//
    }

    public void onBoundData(List<JSONObject> datas, String type) {
        this.type = type;
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }

}
