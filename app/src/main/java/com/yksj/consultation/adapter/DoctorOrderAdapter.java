package com.yksj.consultation.adapter;

import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;

/**
 * 医生订单 adapter
 *
 * @author jack_tang
 */
public class DoctorOrderAdapter extends SimpleBaseAdapter<JSONObject> {

    private ImageLoader instance;
    private Context context;
    private Activity maActivity;
    private int color;
    private DisplayImageOptions mOptions;

    public DoctorOrderAdapter(Context context) {
        super(context);
        this.context = context;
        maActivity = (Activity) context;
        color = context.getResources().getColor(R.color.service_time_text);
        instance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(maActivity);

    }

    @Override
    public int getItemResource() {
        return R.layout.doctor_order_item_layout;
    }

    @Override
    public View getItemView(final int position, final View convertView,
                            com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
        final JSONObject object = (JSONObject) getItem(position);
        TextView name = (TextView) holder.getView(R.id.name);
        SpannableStringBuilder ss = new SpannableStringBuilder();
        //昵称
        ss.clear();
        ss.append("医生姓名:   " + object.optString("DOCTOR_REAL_NAME"));
        ss.setSpan(new ForegroundColorSpan(color), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setText(ss);
        //时间
        TextView time = (TextView) holder.getView(R.id.time);

        ss.clear();
        String serviceTime = TimeUtil.formatTime(object.optString("SERVICE_START"));
        ss.append("服务时间:   " + serviceTime);
        ss.setSpan(new ForegroundColorSpan(color), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        time.setText(ss);


        //类型
        TextView type = (TextView) holder.getView(R.id.type);
        ss.clear();
        ss.append("服务地址:   " + object.optString("SERVICE_PLACE"));
        ss.setSpan(new ForegroundColorSpan(color), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        type.setText(ss);

        Button status = (Button) holder.getView(R.id.status);
        status.setText(object.optString("serviceStatusInfo"));
//        LevelListDrawable drawable = (LevelListDrawable) status.getBackground();
//        drawable.setLevel(object.optInt("serviceStatusFlag", 0));
        //serviceStatusFlag
        TextView right_view = (TextView) holder.getView(R.id.right_view);
        right_view.setText(object.optString("serviceOperation"));

        ImageView chatHead = (ImageView) holder.getView(R.id.chat_head);
//        chatHead.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PersonInfoUtil.choiceActivity(object.optString("customerId"), context, object.optString("roleId"));
//            }
//        });
        instance.displayImage(object.optString("ICON_DOCTOR_PICTURE"), chatHead, mOptions);
//        instance.displayImage(object.optString("customerSex"), object.optString("clientIconBackground"), chatHead);
        convertView.findViewById(R.id.ll_entry).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(maActivity, OrderDetailedPatient.class);
//                intent.putExtra("ORIDERID", object.optString("ORDER_ID"));
//                intent.putExtra("CUSTOMER_ID", LoginServiceManeger.instance().getLoginEntity().getId());
//                intent.putExtra("DOCTORID", object.optString("CUSTOMER_ID"));
//                intent.putExtra("data", object.toString());
//                maActivity.startActivity(intent);
                Intent intent = new Intent(maActivity, AtyOutPatientDetail.class);
                intent.putExtra("ORIDERID", object.optString("ORDER_ID"));
//                intent.putExtra("CUSTOMER_ID", LoginServiceManeger.instance().getLoginEntity().getId());
//                intent.putExtra("DOCTORID", object.optString("CUSTOMER_ID"));
//                intent.putExtra("data", object.toString());
                maActivity.startActivity(intent);
//				String serviceStatusCode = object.optString("serviceStatusCode");
//				Intent intent;
//				if(serviceStatusCode.equals("175")||serviceStatusCode.equals("180")){
//					intent=new Intent(context,DoctorServiceStatusContent.class);
//					intent.putExtra("ORIDERID",object.optString("orderId"));
//					intent.putExtra("CUSTOMER_ID",object.optString("customerId"));
//					intent.putExtra("DOCTORID", SmartFoxClient.getLoginUserId());
//					intent.putExtra("tag", 1000);
//					maActivity.startActivityForResult(intent,5000);
//				}else if("1".equals(object.optString("serviceTypeId"))){//普通
//					intent=new Intent(context,DoctorServiceStatusContent.class);
//					intent.putExtra("entity", object.toString());
//					intent.putExtra("tag", 2000);
//					maActivity.startActivityForResult(intent,5000);
//					
//				}else if("2".equals(object.optString("serviceTypeId"))||"3".equals(object.optString("serviceTypeId"))||"50".equals(serviceStatusCode)){//预约
//					intent=new Intent(context,DoctorServiceStatusContent.class);
//					intent.putExtra("entity", object.toString());
//					intent.putExtra("tag", 3000);
//					maActivity.startActivityForResult(intent,5000);
//				}
            }
        });
        return convertView;
    }


}
