package com.yksj.consultation.comm;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.avchat.AVChatActivity;
import com.yksj.consultation.son.consultation.avchat.AVChatProfile;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 基类
 */
public class BaseFragmentActivity extends FragmentActivity {

    public ImageView titleLeftBtn;
    public Button titleRightBtn;
    public Button titleRightBtn2;
    public TextView titleTextV;
    public RelativeLayout title;
    public RadioButton leftRadio, rightRadio;
    public RadioGroup radioGroup;
    public EditText editSearch;
    public ImageView personBtn;
    public ImageView mImageView;
    public ImageView mImageViewD;
    public ImageView mImageViewP;
    public ImageView title_change;
    public String mLoginUserId;
    public EditText editSearch_top;
    public FrameLayout frameLayout_search;

    protected Intent mIntent = null;


    public void initTitle() {
        titleLeftBtn = (ImageView) findViewById(R.id.title_back);
        personBtn = (ImageView) findViewById(R.id.btn_person);
        titleRightBtn = (Button) findViewById(R.id.title_right);
        titleTextV = (TextView) findViewById(R.id.title_lable);

        frameLayout_search = (FrameLayout) findViewById(R.id.frameLayout_search);
        editSearch_top = (EditText) findViewById(R.id.edit_search_top);

        titleRightBtn2 = (Button) findViewById(R.id.title_right2);
        mImageView = (ImageView) findViewById(R.id.title_rigth_pic);
        mImageViewP = (ImageView) findViewById(R.id.main_listmenuP);
        mImageViewD = (ImageView) findViewById(R.id.main_listmenuD);
        title = (RelativeLayout) findViewById(R.id.title_root);
        title_change = (ImageView) findViewById(R.id.title_change);
    }


    public final void setBackBtn(String text, OnClickListener listener) {
        titleLeftBtn.setBackgroundDrawable(null);
//		titleLeftBtn.setText(text);
        titleLeftBtn.setOnClickListener(listener);
    }

    public final void setBlackColor(TextView textView) {
        if (textView != null) {
            textView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public final void setTitle(String txt) {
        titleTextV.setText(txt);
    }

    public final void setRight(String txt, OnClickListener listener) {
        if (titleRightBtn2 == null) return;
        titleRightBtn2.setText(txt);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(listener);
    }

    public void setRightChange(View.OnClickListener listener) {
        title_change.setVisibility(View.VISIBLE);
        title_change.setOnClickListener(listener);
    }

    public final void setRightBtnYellowBg(String text, OnClickListener listener) {
        if (titleRightBtn2 == null) return;
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText(text);
        titleRightBtn2.setOnClickListener(listener);
    }

    public final void setRightMore(OnClickListener listener) {
        titleRightBtn2.setBackgroundResource(R.drawable.ig_more);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(listener);
    }

    public final void setRightMore(OnClickListener listener, int drawableId) {
        titleRightBtn2.setBackgroundResource(drawableId);
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setOnClickListener(listener);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        LogUtil.d("CLASS_NAME", getClass().toString());
        mLoginUserId = LoginServiceManeger.instance().getLoginUserId();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initRadioGroupView() {
        titleLeftBtn = (ImageView) findViewById(R.id.title_back);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        leftRadio = (RadioButton) findViewById(R.id.left_radio);
        rightRadio = (RadioButton) findViewById(R.id.right_radio);
        titleRightBtn2 = (Button) findViewById(R.id.title_right2);
        titleRightBtn = (Button) findViewById(R.id.title_right);
    }

    public void initSearchView() {
        titleRightBtn = (Button) findViewById(R.id.title_right);
        editSearch = (EditText) findViewById(R.id.title_edit_search);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharePreUtils.getisAvChatState()&&AVChatProfile.getInstance().isAVChatting()) {
            checkCall();
        }
    }

    /**
     * 是否有视频通话
     */
    private void checkCall() {
        if (HTalkApplication.getApplication().isNetWork()) {
            startAudioVideoCall();
        } else {
            ToastUtil.showShort(getResources().getString(R.string.getway_error_note));
        }
    }

    /************************ 音视频通话 ***********************/

    public void startAudioVideoCall() {
//        AVChatActivity.launch(this, "15210270585", AVChatType.VIDEO.getValue(), AVChatActivity.FROM_BROADCASTRECEIVER);
        if (LoginServiceManeger.instance().getLoginEntity() != null && LoginServiceManeger.instance().getLoginEntity().getAvData() != null)
            AVChatProfile.getInstance().launchActivity(LoginServiceManeger.instance().getLoginEntity().getAvData(), AVChatActivity.FROM_BROADCASTRECEIVER);
    }
}
