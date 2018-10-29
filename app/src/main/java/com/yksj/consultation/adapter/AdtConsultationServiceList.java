/**
 *
 */
package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.dmsj.newask.Views.CircleImageView;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.consultation.DAtyConsultDetails;
import com.yksj.consultation.son.consultation.PAtyAlreadyApply;
import com.yksj.consultation.son.consultation.old.DAtyConsultSuggestion;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity;
import com.yksj.healthtalk.entity.ConsultationServiceEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.SalonHttpUtil;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

/**
 * 个人中心-我的服务 -list
 *
 * @author zheng
 */
public class AdtConsultationServiceList extends SimpleBaseAdapter<ConsultationServiceEntity> implements OnClickListener, SalonSelectPaymentOptionActivity.OnBuyTicketHandlerListener {

    private Button state;
    private LevelListDrawable lld;
    private Intent intent;
    private ImageLoader mInstance;
    private DisplayImageOptions mOptions;
    private FragmentActivity mActivity;
    private CustomerInfoEntity entityE = new CustomerInfoEntity(), entityD = new CustomerInfoEntity();
    private PopupWindow messageChat;
    private JSONObject obj;

    public AdtConsultationServiceList(Context context) {
        super(context);
        this.context = context;
        mInstance = ImageLoader.getInstance();
        mActivity = (FragmentActivity) context;
        mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(mActivity);
    }

    @Override
    public int getItemResource() {
        return R.layout.consultation_manager_item_layout;
    }

    @Override
    public View getItemView(final int position, View convertView,
                            com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
        final ConsultationServiceEntity cse = datas.get(position);
        final CustomerInfoEntity chatEntity = new CustomerInfoEntity();
        TextView getTime = (TextView) holder.getView(R.id.tv_receive);
        ImageView talkMark = (ImageView) holder.getView(R.id.iv_talkbg);
        TextView expertName = (TextView) holder.getView(R.id.tv_consultCenter);
        //TextView expertName = (TextView) holder.getView(R.id.tv_patientName);
        TextView applytime = (TextView) holder.getView(R.id.tv_applyTime);
        TextView right_view = (TextView) holder.getView(R.id.right_view);
        TextView consultationName = (TextView) holder.getView(R.id.tv_patientName);
        //TextView consultationName = (TextView)
        //holder.getView(R.id.tv_consultCenter);
        state = (Button) holder.getView(R.id.btn_status);
        lld = (LevelListDrawable) state.getBackground();
        expertName.setText(cse.getConsultationName());
        applytime.setText(TimeUtil.formatTime(cse.getApplytime()));
        consultationName.setText("会诊专家:" + cse.getCustomerNickName());
        final int NUMS[] = new int[]{90, 222, 232, 242};
        for (int status : NUMS) {
            if (status == Integer.valueOf(cse.getConsultationState())) {
                holder.getView(R.id.iv_talk).setVisibility(View.GONE);
                talkMark.setVisibility(View.GONE);
            } else {
                talkMark.setOnClickListener(new OnClickListener() {// 聊天弹出

                    @Override
                    public void onClick(View v) {
                        if ("10".equals(cse.getConsultationState())) {
                            ToastUtil.showShort("对不起，您的会诊申请还没有人接诊！");
                        } else if ("20".equals(cse.getConsultationState()) || "25".equals(cse.getConsultationState()) || "30".equals(cse.getConsultationState()) || "50".equals(cse.getConsultationState()) || "60".equals(cse.getConsultationState())) {
                            chatEntity.setName(datas.get(position).getCreateDoctorIdName());
                            chatEntity.setId(datas.get(position).getCreateDoctorId());
                            Log.e("TTTTTTTTTTTTTTTT", datas.get(position).getCreateDoctorIdName());
                            Log.e("TTTTTTTTTTTTTTTT", datas.get(position).getCreateDoctorId());
                            FriendHttpUtil.chatFromPerson(mActivity, chatEntity);
                        } else {
//					popData(v, cse.getConsultationId());
                            doChatGroup(cse.getConsultationId(), "0", cse.getConsultationName());
                        }
                    }
                });
            }
        }
        getTime.setText("申请时间:");
        String conState = cse.getConsultationState();
        int consuState = Integer.parseInt(conState);
//		switch (key) {
//		case 0://全部中
        switch (consuState) {
            case 10://已申请
                state.setText(cse.getServiceStatusName());
                lld.setLevel(2);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, PAtyAlreadyApply.class);
                        intentData(cse);
                        intent.putExtra("KEY", 998);
                        context.startActivity(intent);
                    }
                });
                break;
            case 20:
            case 25:
            case 30:
            case 50://填病历
                state.setText(cse.getServiceStatusName());
                lld.setLevel(7);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, DAtyConsultDetails.class);
                        intentData(cse);
                        intent.putExtra("HEKL", 21);
                        context.startActivity(intent);
                    }
                });
                break;
            case 40:
            case 60:
            case 70://待付款
                state.setText(cse.getServiceStatusName());
                lld.setLevel(1);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, DAtyConsultDetails.class);
                        intentData(cse);
                        intent.putExtra("HEKL", 22);
                        context.startActivity(intent);
                    }
                });
                break;
            case 80:
            case 85:
            case 88:// 待服务
                state.setText(cse.getServiceStatusName());
                lld.setLevel(3);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, DAtyConsultSuggestion.class);
                        intentData(cse);
                        intent.putExtra("HEKL", 23);
                        context.startActivity(intent);
                    }
                });
                break;
            case 222:
            case 232:// 待退款

                state.setText(cse.getServiceStatusName());
                lld.setLevel(4);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, DAtyConsultDetails.class);
                        intentData(cse);
                        intent.putExtra("STATUS", Integer.parseInt(cse.getConsultationState()));
                        intent.putExtra("HEKL", 24);
                        context.startActivity(intent);
                    }
                });
                break;
            case 99:// 已完成
                if ("0".equals(cse.getIsTalk())) {
                    holder.getView(R.id.iv_talk).setVisibility(View.GONE);
                    talkMark.setVisibility(View.GONE);
                }
                state.setText(cse.getServiceStatusName());
                lld.setLevel(0);
                right_view.setText(cse.getServiceOperation().toString());
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, DAtyConsultSuggestion.class);
                        intentData(cse);
                        intent.putExtra("HEKL", 25);
                        context.startActivity(intent);
                    }
                });
                break;
            case 242:
            case 90:// 已取消
                convertView.findViewById(R.id.fl_button).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(context, PAtyAlreadyApply.class);
                        intentData(cse);
                        context.startActivity(intent);
                    }
                });
                state.setText(cse.getServiceStatusName());
                lld.setLevel(0);
                right_view.setText(cse.getServiceOperation().toString());
                break;
        }
        return convertView;
    }

    private void intentData(ConsultationServiceEntity entiy) {
        intent.putExtra("ILLNAME", entiy.getConsultationName());
        intent.putExtra("CONID", Integer.parseInt(entiy.getConsultationId()));
    }


    /**
     * 以下是Popwindow
     *
     * @param view
     */
    private void popwin(View view) {
        view = LayoutInflater.from(mActivity).inflate(R.layout.layout_look_doctororpatient, null);
        CircleImageView firstHead = (CircleImageView) view.findViewById(R.id.frist_person_head);
        TextView firstName = (TextView) view.findViewById(R.id.frist_person_name);
        CircleImageView SecondHead = (CircleImageView) view.findViewById(R.id.second_person_head);
        TextView SecondName = (TextView) view.findViewById(R.id.second_person_name);
        view.findViewById(R.id.frist_Look_message).setOnClickListener(this);
        view.findViewById(R.id.frist_click_chat).setOnClickListener(this);
        view.findViewById(R.id.second_Look_message).setOnClickListener(this);
        view.findViewById(R.id.second_click_chat).setOnClickListener(this);
        messageChat = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
        mInstance.displayImage(obj.optString("EXPERTICON"), firstHead, mOptions);
        mInstance.displayImage(obj.optString("DOCTORICON"), SecondHead, mOptions);
        firstName.setText(obj.optString("EXPERTNAME"));
        SecondName.setText(obj.optString("DOCTORNAME"));
        if ("".equals(obj.optString("DOCTORNAME")) || !obj.has("DOCTORNAME")) {
            view.findViewById(R.id.second_person).setVisibility(View.GONE);
            view.findViewById(R.id.view_line).setVisibility(View.GONE);
        }
        messageChat.setAnimationStyle(R.style.anim_popupwindow_share);
        messageChat.setBackgroundDrawable(new BitmapDrawable());
        messageChat.setFocusable(true);
        messageChat.setOutsideTouchable(true);
        messageChat.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                setAlpha((float) 1.0);
            }
        });
        setAlpha((float) 0.5);
        messageChat.showAtLocation(view, Gravity.CENTER, 10, 10);
    }

    private void popData(final View view, String consultationId) {
        HttpRestClient.doHttpServerDetailServletChatMessage(consultationId, new AsyncHttpResponseHandler(
                (FragmentActivity) mActivity) {

            @Override
            public void onSuccess(String content) {
                try {
                    obj = new JSONObject(content);
                    if (obj.has("error_code")) {
                        ToastUtil.showShort("errormessage");

                    } else {
                        //entityE=new CustomerInfoEntity();
                        //entityD=new CustomerInfoEntity();
                        entityE.setId(obj.optString("EXPERTID"));
                        entityE.setName(obj.optString("EXPERTNAME"));
                        entityE.setDoctorClientPicture(obj.optString("EXPERTICON"));
                        entityE.setDoctorTitle(obj.optString("EXPERTTITLE"));
                        entityD.setId(obj.optString("DOCTORID"));
                        entityD.setName(obj.optString("DOCTORNAME"));
                        entityD.setDoctorClientPicture(obj.optString("DOCTORICON"));
                        entityD.setDoctorTitle(obj.optString("DOCTORTITLE"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(content);
            }

            @Override
            public void onFinish() {
//				popwin(view);
                super.onFinish();
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frist_Look_message:
                if (entityE.getId().equals("")) {
                    ToastUtil.showShort("frist_Look_message");
                    return;
                }
                // PersonInfoUtil.choiceActivity(entityE.getId(), mActivity, "" +
                // entityE.getRoldid());
                Intent intent = new Intent(mActivity, DoctorClinicMainActivity.class);
                intent.putExtra("id", entityE.getId());
                // intent.putExtra("position", position-1);
                mActivity.startActivity(intent);
                break;
            case R.id.frist_click_chat:
                if (entityE.getId().equals("")) {
                    ToastUtil.showShort("frist_click_chat");
                    return;
                }
                FriendHttpUtil.chatFromPerson((FragmentActivity) mActivity, entityE);
                break;
            case R.id.second_Look_message:
                if (entityD.getId().equals("")) {
                    ToastUtil.showShort("second_Look_message");
                    return;
                }
                // PersonInfoUtil.choiceActivity(entityD.getId(), mActivity, "" +
                // entityD.getRoldid());
                Intent intent1 = new Intent(mActivity, DoctorClinicMainActivity.class);
                intent1.putExtra("id", entityD.getId());
                mActivity.startActivity(intent1);
                break;
            case R.id.second_click_chat:
                if (entityD.getId().equals("")) {
                    ToastUtil.showShort("second_click_chat");
                    return;
                }
                FriendHttpUtil.chatFromPerson((FragmentActivity) mActivity, entityD);
                break;

        }
    }

    private void setAlpha(float alpha) {// alpha0.0到1.0
        android.view.WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    /**
     * chat
     *
     * @param id   groupid
     * @param isBl ;//是否是病历：（1是病历，0是会诊）
     * @param name groupname
     */
    private void doChatGroup(String id, String isBl, String name) {
        GroupInfoEntity entity = new GroupInfoEntity();
        entity.setId(id);
        entity.setIsBL(isBl);
        entity.setName(name);
        SalonHttpUtil.onItemClick(context, AdtConsultationServiceList.this, mActivity.getSupportFragmentManager(), entity, false);
    }


    @Override
    public void onTicketHandler(String state, GroupInfoEntity entity) {
        if ("0".equals(state)) {
        } else if ("-1".equals(state)) {
            ToastUtil.showShort("服务器出错");
        } else {
            Intent intent1 = new Intent();
            intent1.putExtra(ChatActivity.KEY_PARAME, entity);
            intent1.setClass(context, ChatActivity.class);
            context.startActivity(intent1);
        }
    }
}
