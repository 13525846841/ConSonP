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
import com.yksj.healthtalk.utils.ScreenSizeUtils;

/**
 * Created by ${chen} on 2016/11/14.
 * 更改昵称,姓名，备注的  dialog
 */
public class MyAlertDialog implements View.OnClickListener{

    private Dialog mDialog;
    private View mDialogView;
    private static Context mContext;
    private Builder mBuilder;
    private Button finish;
    public static EditText mEt;
    private TextView mTitle;
    public MyAlertDialog(Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mContext, R.style.MyDialogStyle);
        mDialogView = View.inflate(mContext, R.layout.dialog_name, null);
        finish = (Button) mDialogView.findViewById(R.id.finish);
        mTitle = (TextView) mDialogView.findViewById(R.id.title);
        mEt = (EditText) mDialogView.findViewById(R.id.edittext);
        finish.setOnClickListener(this);
        mDialogView.setMinimumHeight((int)(ScreenSizeUtils.getInstance(mContext).getScreenHeight
                () * builder.getHeight()));
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
        mEt.setText(mBuilder.getEdittext());
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public String edittext(){
        String text = mEt.getText().toString().trim();
        return text;
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

        public Builder(Context context) {
            mContext = context;
            listener = null;
            edittext="";
            isTouchOutside = true;
            height = 0.21f;
            width = 0.73f;
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

        public Builder setEdittext(String edittext) {
            this.edittext = edittext;
            return this;
        }

        public String getEdittext() {

            return edittext;
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

        public MyAlertDialog build() {
            return new MyAlertDialog(this);
        }

    }
}
