package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.dossier.AtyDossierDetails;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by zheng on 2015/7/13.
 */
public class AtySearchAdapter extends SimpleBaseAdapter<JSONObject> {

//    private ImageLoader instance;
    private Context context;
//    private Activity maActivity;
//    private DisplayImageOptions mOptions;
    private OnClickChildItemListener followListener;
//    private CustomerInfoEntity personMessage;
    public AtySearchAdapter(Context context) {
        super(context);
        this.context=context;
//        instance = ImageLoader.getInstance();
//        maActivity=(Activity) context;
//        mOptions= DefaultConfigurationFactory.createSeniorDoctorDisplayImageOptions(maActivity);
    }

    @Override
    public int getItemResource() {
        return R.layout.look_list_itme;
    }

    @Override
    public View getItemView(final int position, View convertView,
                            com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
        final JSONObject obj=(JSONObject) getItem(position);
        TextView memberName = (TextView) holder.getView(R.id.Look_name);//会员名字
        TextView memberduty = (TextView) holder.getView(R.id.look_history_time);//职称
        RelativeLayout mlayout = (RelativeLayout)holder.getView(R.id.dossier_layout);
        mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AtyDossierDetails.class);
                intent.putExtra("TITLE", obj.optString("MEDICAL_NAME"));
                intent.putExtra("ID", obj.optString("MEDICAL_RECORD_ID"));
                context.startActivity(intent);
            }
        });
//        TextView associationduty = (TextView) holder.getView(R.id.association_duty);//协会职位
//        TextView memberyiyuan = (TextView) holder.getView(R.id.member_yiyuan);//所在医院
//        TextView membertreat = (TextView) holder.getView(R.id.member_treat);//主治
//        ImageView memberHead= (ImageView) holder.getView(R.id.member_head);//头像
        memberName.setText(obj.optString("MEDICAL_NAME"));
        memberduty.setText(TimeUtil.format(obj.optString("SHARE_TIME")));//SHARE_TIME
//        memberduty.setText(obj.optString("SHARE_TIME"));
//        memberyiyuan.setText(obj.optString("DOCTOR_HOSPITAL"));
//        membertreat.setText(obj.optString("DOCTOR_SPECIALLY"));
//        associationduty.setText(obj.optString("DUTY"));
//        instance.displayImage(obj.optString("CLIENT_ICON_BACKGROUND"), memberHead,mOptions);
//        memberHead.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                personMessage = new CustomerInfoEntity();
//                personMessage.setId(obj.optString("CUSTOMER_ID"));
//				PersonInfoUtil.choiceActivity(obj.optString("CUSTOMER_ID"), context, obj.optString("ROLE_ID"));//诊所
//				followListener.onFollowClick(personMessage, position);
//				followListener.onFollowClick(personMessage, position);
//                followListener.onHeadIconClick(personMessage, position);
//            }
//        });
        return convertView;
    }
    public void setOnClickFollowListener(OnClickChildItemListener l){
        this.followListener = l;
    };
}
