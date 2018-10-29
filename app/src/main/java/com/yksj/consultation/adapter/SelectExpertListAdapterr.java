package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.bean.DoctorSimpleBean;

import org.universalimageloader.core.ImageLoader;

/**
 * Created by ${chen} on 2017/1/6.
 */
public class SelectExpertListAdapterr extends SimpleBaseAdapter<DoctorSimpleBean> {

    private ImageLoader mInstance;
    private OnClickSelectListener followListener;
    private int type = 0;//0 专家  1医生
    private int fromType = 0;//0默认情况  1表示来自我的医生不显示选择他按钮

    public SelectExpertListAdapterr(Context context, int type) {
        super(context);
        mInstance = ImageLoader.getInstance();
        this.type = type;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    @Override
    public int getItemResource() {
        return R.layout.select_expert_list_item;
    }

    @Override
    public View getItemView(final int position, View convertView,
                            ViewHolder holder) {
        //final DoctorSimpleBean cus=datas.get(position);
        TextView tvName = (TextView) convertView.findViewById(R.id.select_expert_list_item_name);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.select_expert_list_item_doctitle);
        TextView tvHospital = (TextView) convertView.findViewById(R.id.select_expert_list_item_hospital);
        TextView tvSpecial = (TextView) convertView.findViewById(R.id.select_expert_list_item_spetical);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.select_expert_list_item_price);
        TextView tvRemainNum = (TextView) convertView.findViewById(R.id.select_expert_list_item_num);
        Button imgFollow = (Button) convertView.findViewById(R.id.select_expert_list_item_select);

        final DoctorSimpleBean doctorSimple = datas.get(position);


        if (fromType == 1)
            imgFollow.setVisibility(View.GONE);
        ImageView icon = (ImageView) convertView.findViewById(R.id.select_expert_list_item_headicon);
        icon.setImageResource(R.drawable.default_head_female);

        mInstance.displayImage(doctorSimple.getICON_DOCTOR_PICTURE(), icon);

        tvName.setText(doctorSimple.getDOCTOR_REAL_NAME());
        tvTitle.setText(doctorSimple.getTITLE_NAME());
        tvHospital.setText(doctorSimple.getDOCTOR_HOSPITAL());
        tvSpecial.setText(doctorSimple.getDOCTOR_SPECIALLY());

        if (type == 1) {
            convertView.findViewById(R.id.select_expert_list_item_price_layout).setVisibility(View.GONE);
            imgFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followListener.onClickSelect(doctorSimple);

                }
            });
        } else {
            if (doctorSimple.getNUMS().length() > 0) {
                tvRemainNum.setTextColor(context.getResources().getColor(R.color.service_color_text));
                tvRemainNum.setText("剩余" + doctorSimple.getNUMS() + "个服务名额");
                imgFollow.setBackgroundResource(R.drawable.icon_btn_bg_80);
                imgFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        followListener.onClickSelect(doctorSimple);

                    }
                });
            } else {
                imgFollow.setBackgroundResource(R.drawable.icon_bg_gray_72);
                imgFollow.setOnClickListener(null);
                tvRemainNum.setTextColor(context.getResources().getColor(R.color.red_text));
                tvRemainNum.setText("服务名额已满");
            }
            tvPrice.setText(doctorSimple.getSERVICE_PRICE() + "元");
        }

    return convertView;
}

    public void setSelectListener(OnClickSelectListener followListener) {
        this.followListener = followListener;
    }

    public interface OnClickSelectListener {
        void onClickSelect(DoctorSimpleBean dsb);
    }
}