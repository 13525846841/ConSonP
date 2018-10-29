package com.yksj.consultation.son.home;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.DialogOnClickListener;
import com.yksj.consultation.son.views.LoopView;
import com.yksj.healthtalk.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/5.
 * 有滑轮的dialog
 */
public class WheelViewDialog implements View.OnClickListener {
    private Dialog mDialog;
    private View mDialogView;
    private static Context mContext;
    private Builder mBuilder;
    private Button finish;
    private TextView mTitle;
    private LoopView loopView2;
    private String height;
    private String weight;
    public WheelViewDialog(Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mContext, R.style.MyDialogStyle);
        mDialogView = View.inflate(mContext, R.layout.dialog_wheel_view, null);
        finish = (Button) mDialogView.findViewById(R.id.finish);
        mTitle = (TextView) mDialogView.findViewById(R.id.title);

        loopView2 = (LoopView) mDialogView.findViewById(R.id.lv_loop1);

        //设置初始位置
        loopView2.setInitPosition(0);
        //设置字体大小
        loopView2.setTextSize(22);
        loopView2.setListener(new LoopView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        height = index + "";
                        weight = index + "";
                    }
                });

        finish.setOnClickListener(this);
        mDialogView.setMinimumHeight((int)(ScreenSizeUtils.getInstance(mContext).getScreenHeight
                () * builder.getHeight()));
        mDialog.setContentView(mDialogView);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mContext).getScreenWidth() * builder.getWidth());
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        initDialog();
    }
    public String getIntHeight(){
        return height;
    }
    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    private void initDialog() {
        mDialog.setCanceledOnTouchOutside(mBuilder.isTouchOutside());
        mTitle.setText(mBuilder.getTitleText());
        loopView2.setItems(mBuilder.getmList());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.finish && mBuilder.getListener() != null) {
            mBuilder.getListener().clickButton(finish);
            return;
        }
    }
    public static class Builder {

        private String titleText;
        private boolean isTouchOutside;
        private float height;
        private float width;
        private ArrayList<String> list;
        private List<String> mList;
        private DialogOnClickListener listener;

        public Builder(Context context) {
            mContext = context;
            listener = null;
            isTouchOutside = true;
            height = 0.21f;
            width = 0.73f;
            mList = null;
        }
        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setCanceledOnTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }

        public List<String> getmList(){
            return mList;
        }

        public  Builder setmList(List<String> mList){
            this.mList=mList;
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

        public WheelViewDialog build() {
            return new WheelViewDialog(this);
        }

    }
}
