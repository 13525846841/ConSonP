package com.yksj.consultation.son.home;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.DialogOnClickListener;
import com.yksj.consultation.son.views.LoopView;
import com.yksj.healthtalk.utils.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/7.
 */
public class TimeWheelDialog implements View.OnClickListener {
    private Dialog mDialog;
    private View mDialogView;
    private static Context mContext;
    private Builder mBuilder;
    private Button finish;
    private TextView mTitle;
    private LoopView loopView0;
    private LoopView loopView1;
    private LoopView loopView2;
    private int year;
    private int month;
    private int day;
    public TimeWheelDialog(Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mContext, R.style.MyDialogStyle);
        mDialogView = View.inflate(mContext, R.layout.dialog_wheel_time_view, null);
        finish = (Button) mDialogView.findViewById(R.id.finish);
        mTitle = (TextView) mDialogView.findViewById(R.id.title);
        loopView0  = (LoopView) mDialogView.findViewById(R.id.lv_loop0);
        loopView1  = (LoopView) mDialogView.findViewById(R.id.lv_loop1);
        loopView2 = (LoopView) mDialogView.findViewById(R.id.lv_loop2);

        //设置初始位置
        loopView0.setInitPosition(1980);
        loopView1.setInitPosition(6);
        loopView2.setInitPosition(15);
        //设置字体大小
        loopView0.setTextSize(22);
        loopView1.setTextSize(22);
        loopView2.setTextSize(22);
        loopView0.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                year = index;

            }
        });
        loopView1.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                month = index;

            }
        });
        loopView2.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                day = index;

            }
        });
        loopView1.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                month = index;
            }
        });
        loopView2.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                day = index;
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
    public int getIntYear(){
        return year;
    }
    public int getIntMonth(){
        return month;
    }
    public int getIntDay(){
        return day;
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
        loopView0.setItems(mBuilder.getmList0());
        loopView1.setItems(mBuilder.getmList1());
        loopView2.setItems(mBuilder.getmList2());
        loopView0.setInitPosition(mBuilder.getInitPosition0());
        loopView1.setInitPosition(mBuilder.getInitPosition1());
        loopView2.setInitPosition(mBuilder.getInitPosition2());
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
        private List<String> year;
        private List<String> month;
        private List<String> day;
        private List<String> mList;
        private DialogOnClickListener listener;
        private int yearPosition;
        private int monthPosition;
        private int dayPosition;
        public Builder(Context context) {
            mContext = context;
            listener = null;
            isTouchOutside = true;
            height = 0.21f;
            width = 0.73f;
            year = null;
            month = null;
            day = null;
            yearPosition = 1980 ;
            monthPosition = 0;
            dayPosition = 0;
        }

        public  void setInitPosition0(int initPosition) {
            if (initPosition < 0) {
                this.yearPosition = 0;
            } else {
                if (year != null && year.size() > initPosition) {
                    this.yearPosition = initPosition;
                }
            }
        }
        public  void setInitPosition1(int initPosition) {
            if (initPosition < 0) {
                this.monthPosition = 0;
            } else {
                if (month != null && month.size() > initPosition) {
                    this.monthPosition = initPosition;
                }
            }
        }
        public  void setInitPosition2(int initPosition) {
            if (initPosition < 0) {
                this.dayPosition = 0;
            } else {
                if (day != null && day.size() > initPosition) {
                    this.dayPosition = initPosition;
                }
            }
        }
        public int getInitPosition0(){
            return yearPosition;
        }
        public int getInitPosition1(){
            return monthPosition;
        }
        public int getInitPosition2(){
            return dayPosition;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setCanceledOnTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }

        public List<String> getmList0(){
            return year;
        }
        public List<String> getmList1(){
            return month;
        }
        public List<String> getmList2(){
            return day;
        }

        public  Builder setmList0(List<String> year){
            this.year=year;
            return this;
        }
        public  Builder setmList1(List<String> month){
            this.month=month;
            return this;
        }
        public  Builder setmList2(List<String> day){
            this.day=day;
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

        public TimeWheelDialog build() {
            return new TimeWheelDialog(this);
        }

    }
}
