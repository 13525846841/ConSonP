package com.yksj.consultation.adapter;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.PConsultApplyActivity;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.utils.LogUtil;

/**
 * 会诊专家列表
 * @author zheng
 *
 */
public class ConsultationExpertAdapter extends SimpleBaseAdapter<CustomerInfoEntity>{

    private boolean FLAG = true;
    private ImageLoader mInstance;
    private DisplayImageOptions mOptions;
    private Activity mActivity;
    private Context context;
    private OnClickChildItemListener followListener;
    public ConsultationExpertAdapter(Context context) {
        super(context);
        mInstance=ImageLoader.getInstance();
        this.context=context;
        mActivity=(Activity) context;
        mOptions=DefaultConfigurationFactory.createHeadDisplayImageOptions(mActivity);
    }

    @Override
    public int getItemResource() {
        return R.layout.consultation_expert_viewpager_listview;
    }

    @Override
    public View getItemView(final int position, View convertView,
                            com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
        final CustomerInfoEntity see=datas.get(position);
        ImageView ehImage =(ImageView) holder.getView(R.id.expert_header_image);//头像
        TextView eName=(TextView) holder.getView(R.id.expert_header_name);//名字
        LinearLayout eDuty=(LinearLayout) holder.getView(R.id.expert_duty1);//职务
//        TextView ePayment=(TextView) holder.getView(R.id.consultation_payment1);//会诊金额
        Button applyBtn=(Button) holder.getView(R.id.apply_btn);//申请会诊
        final TextView eSpeciality1=(TextView) holder.getView(R.id.expert_speciality1);//专长
        mInstance.displayImage(see.getDoctorClientPicture(), ehImage, mOptions);//头像
//        final String duty=see.getDoctorTitleName();//职务
        final String duty=see.getAcademicJob();
        LogUtil.d("TAG",duty);
        addTextView1(duty,eDuty);//动态添加职务
        if(see.getSpecial()!=null){
            if(!see.getSpecial().equals("null")){
//				eSpeciality1.setVisibility(View.VISIBLE);
                eSpeciality1.setText(see.getSpecial());//添加专长
            }
        }
//		ePayment.setText("¥"+see.getServicePrice());//添加金额
        String name=see.getRealname().concat("(").concat(see.getUsername()).concat(")");
        eName.setText(name);//添加名字及多美好
        ehImage.setOnClickListener(new OnClickListener() {//進診所//头像监听器

            @Override
            public void onClick(View v) {
                followListener.onHeadIconClick(see,position);
            }
        });
        if(!SmartFoxClient.getLoginUserInfo().isDoctor()){
            applyBtn.setVisibility(View.VISIBLE);
        }
        applyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PConsultApplyActivity.class);
                intent.putExtra("CUSTOMER_ID", see.getId());
                intent.putExtra("APPLY", 3);
                intent.putExtra("PAYMENT",see.getServicePrice());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    /*
     *动态添加职务
     */
    private void addTextView1(String duty,LinearLayout eDuty){
        eDuty.removeAllViews();
        if(!"".equals(duty)){
            if(duty.contains("，")){
                String[] duty1=duty.split("，");
                eDuty.removeAllViews();
                int length = 0;
                if(duty1.length>=3){
                    length=3;
                }else {
                    length=duty1.length;
                }
                for(int i=0;i < length;i++){
                    TextView text=new TextView(context);
                    text.setTextSize(15);
                    text.setTextColor(context.getResources().getColor(R.color.duty1));
                    text.setSingleLine(true);
                    text.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                    text.setText(duty1[i]);
                    eDuty.addView(text);
                }
            }else {
                TextView text=new TextView(context);
                text.setTextSize(15);
                text.setTextColor(context.getResources().getColor(R.color.duty1));
                text.setSingleLine(true);
                text.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                text.setText(duty);
                eDuty.addView(text);
            }
        }
    }
    public void setFollowListener(OnClickChildItemListener followListener) {
        this.followListener = followListener;
    }
}
