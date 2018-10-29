package com.yksj.consultation.son.home;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.DialogOnClickListener;
import com.yksj.consultation.son.views.LoopView;
import com.yksj.healthtalk.utils.ScreenSizeUtils;

import java.util.List;

/**
 * Created by ${chen} on 2016/12/9.
 */
public class SexAlertDialog implements View.OnClickListener {

    private Dialog mDialog;
    private View mDialogView;
    private static Context mContext;
    private Builder mBuilder;
    private Button finish;
    private TextView mTitle;
    private String sex;
    private LoopView loopView;
    public SexAlertDialog(Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mContext, R.style.MyDialogStyle);
        mDialogView = View.inflate(mContext, R.layout.dialog_sex, null);
        finish = (Button) mDialogView.findViewById(R.id.finish);
        mTitle = (TextView) mDialogView.findViewById(R.id.title);

        loopView = (LoopView) mDialogView.findViewById(R.id.lv_loop1);
        //设置初始位置
        loopView.setInitPosition(0);
        //设置字体大小
        loopView.setTextSize(22);
        loopView.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if(index==0){
                    sex = "男";
                }else if (index==1){
                    sex ="女";
                }
            }
        });

        finish.setOnClickListener(this);
//        mDialogView.setMinimumHeight((int)(ScreenSizeUtils.getInstance(mContext).getScreenHeight
//                () * builder.getHeight()));
        mDialogView.setMinimumHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(mDialogView);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mContext).getScreenWidth() * builder.getWidth());
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        initDialog();
    }
    private void initDialog() {
        mDialog.setCanceledOnTouchOutside(mBuilder.isTouchOutside());
        mTitle.setText(mBuilder.getTitleText());
        loopView.setItems(mBuilder.getSex());
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
    public String getInitPosition(){
        return sex;
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.finish && mBuilder.getListener() != null) {
            mBuilder.getListener().clickButton(finish);
            return;
        }
    }
    public static class Builder {

        private String titleText;
        private String edittext;
        private boolean isTouchOutside;
        private float height;
        private float width;
        private DialogOnClickListener listener;

        private List<String> sex;

        public List<String> getSex() {
            return sex;
        }

        public Builder setSex(List<String> sex) {
            this.sex = sex;
            return this;
        }

        public Builder(Context context) {
            mContext = context;
            listener = null;
            edittext="";
            isTouchOutside = true;
            height = 0.21f;
            width = 0.73f;
            sex = null;
        }
        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setCanceledOnTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }
        public String getTitleText(){
            return titleText;
        }
        public Builder setTitleText(String titleText){
            this.titleText=titleText;
            return this;
        }


        public float getHeight() {
            return height;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;
        }

        public float getWidth() {
            return width;
        }

        public Builder setWidth(float width) {
            this.width = width;
            return this;
        }

        public DialogOnClickListener getListener() {

            return listener;
        }

        public Builder setOnclickListener(DialogOnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public SexAlertDialog build() {
            return new SexAlertDialog(this);
        }

    }
}
