package com.yksj.consultation.son.smallone.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.WheelView;
import com.yksj.healthtalk.utils.MResource;
import com.yksj.healthtalk.utils.ViewFinder;
import com.yksj.healthtalk.utils.WheelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DoctorChatMyinformation extends BaseFragmentActivity implements
        OnClickListener, OnCheckedChangeListener {
    private Button mButton;// 年龄
    private Button mButtonNumber;// 怀孕周期
    //    private RadioGroup gender;// 性别
    private RadioGroup pregnancy;// 是否怀孕
    private PopupWindow mPopupWindow;
    private List<Map<String, String>> mList;// 周期/年龄
    private ViewFinder finder;
    private String mSex = "";//性别 W M
    private String mPregnancy = "N";//怀孕 Y N
    private Button mSexBtn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.doctor_chat_infomation);
//        setContentView(R.layout.duomei_doctor_list);
        initView();
        initData();
    }

    private void initData() {
        String modifyContent = getIntent().getStringExtra("modify_Content");
        String[] split = modifyContent.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                String[] split2 = split[i].split("-");
                if ("1".equals(split2[0])) {// 年龄
                    mButton.setText(split2[1] + "岁");
                    mButton.setTag(split2[1].toString());
                }
                if ("2".equals(split2[0])) {// 性别
                    if ("M".equalsIgnoreCase(split2[1])) {// 男
                        mSexBtn.setText("男");
//                        RadioButton childAt = (RadioButton) gender.getChildAt(0);
//                        childAt.setChecked(true);
                        mSex = "M";
                    } else {// 女
                        mSexBtn.setText("女");
                        mSex = "W";
                        if (Integer.valueOf(mButton.getTag().toString()) <= 15) {
                            pregnancy.setVisibility(View.GONE);
                            mButtonNumber.setVisibility(View.GONE);
                            mPregnancy = "N";
                        } else {
                            pregnancy.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if ("3".equals(split2[0])) {// 是否怀孕
                    pregnancy.setVisibility(View.VISIBLE);
                    if ("N".equalsIgnoreCase(split2[1])) {//
                        RadioButton childAt = (RadioButton) pregnancy.getChildAt(0);
                        childAt.setChecked(true);
                    } else {// 女
                        RadioButton childAt = (RadioButton) pregnancy.getChildAt(1);
                        childAt.setChecked(true);
                    }
                }
                if ("4".equals(split2[0])) {// 怀孕周期
                    mButtonNumber.setText("怀孕" + split2[1] + "周");
                    mButtonNumber.setTag(split2[1].toString());

                }
            }
        } catch (Exception e) {
        }

    }

    private void initView() {
        initTitle();
        setTitle(getIntent().getStringExtra("titleName"));
        titleLeftBtn.setVisibility(View.VISIBLE);
        titleLeftBtn.setOnClickListener(this);
        finder = new ViewFinder(this);
        mButton = finder.button(R.id.button_age);
        mButton.setOnClickListener(this);
        mButtonNumber = finder.button(R.id.button_number);
        mButtonNumber.setOnClickListener(this);
        pregnancy = finder.find(R.id.pregnancy);

        for (int i = 0; i < pregnancy.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) pregnancy.getChildAt(i);
            childAt.setOnCheckedChangeListener(this);
        }

        mList = new ArrayList<Map<String, String>>();
        mSexBtn = (Button) findViewById(R.id.sex);
        mSexBtn.setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
                        "您确定修改完了嘛", "完成", "继续", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                            @Override
                            public void onDismiss(DialogFragment fragment) {
                                subMint();
                            }

                            @Override
                            public void onClick(DialogFragment fragment, View v) {
                                fragment.dismissAllowingStateLoss();
                            }
                        });
                break;
            case R.id.button_age:// 年龄
                showPop(1);
                break;
            case R.id.button_number:// 怀孕周期
                showPop(2);
                break;
            case R.id.sex://性别
                showPop(3);
                break;
            case R.id.sure://确定
                subMint();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(),
                "您确定修改完成了嘛?", "完成", "修改", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                        subMint();
                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        fragment.dismissAllowingStateLoss();
                    }
                });
    }

    private void subMint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("1-" + mButton.getTag().toString());
        buffer.append(",");
        buffer.append("2-" + mSex);
        if ("W".equalsIgnoreCase(mSex)) {//女
            buffer.append(",");
            buffer.append("3-" + mPregnancy);
            if ("Y".equalsIgnoreCase(mPregnancy)) {
                if (mButtonNumber.getTag() != null)//怀孕周期
                {
                    buffer.append(",");
                    buffer.append("4-" + mButtonNumber.getTag().toString());
                } else {
                    SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请填写怀孕周数", new SingleBtnFragmentDialog.OnClickSureBtnListener() {

                        @Override
                        public void onClickSureHander() {
                            showPop(2);
                        }
                    });
                    return;
                }
            }
        }
        getIntent().putExtra("json", buffer.toString());
        setResult(RESULT_OK, getIntent());
        finish();
    }


    private void showPop(int choose) {// 1是年龄,2是周期,3性别
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        mPopupWindow = null;
        mList.clear();
        if (1 == choose) {
            for (int i = 0; i < 120; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", String.valueOf(i + 1));
                mList.add(map);
            }
            mPopupWindow = WheelUtils.showSingleWheel(this, mList, mButton,
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index1 = (Integer) v.getTag(R.id.wheel_one);
                            Map<String, String> map = mList.get(index1);
                            String age = map.get("name");
                            mButton.setText(age + "岁");
                            mButton.setTag(age);
                            if (mSexBtn.getText().equals("女")) {
                                if (Integer.valueOf(age) > 15) {
                                    pregnancy.setVisibility(View.VISIBLE);
                                } else {
                                    pregnancy.setVisibility(View.GONE);
                                    mButtonNumber.setVisibility(View.GONE);
                                    mPregnancy = "N";
                                }
                            }
                        }
                    });
            if (mButton.getTag() != null) {
                WheelView wheelOne = (WheelView) mPopupWindow.getContentView().findViewById(R.id.wheel_one);
                wheelOne.setCurrentItem(Integer.valueOf(mButton.getTag().toString()) - 1);
            }
        } else if (2 == choose) {
            for (int i = 0; i < 60; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", String.valueOf(i + 1));
                mList.add(map);
            }
            mPopupWindow = WheelUtils.showSingleWheel(this, mList,
                    mButtonNumber, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index1 = (Integer) v.getTag(MResource.getIdByName(getApplicationContext(), "id", "wheel_one"));
//							int index1 = (Integer) v.getTag(R.id.wheel_one);
                            Map<String, String> map = mList.get(index1);
                            String age = map.get("name");
                            mButtonNumber.setText("怀孕" + age + "周");
                            mButtonNumber.setTag(age);
                        }
                    });
            if (mButtonNumber.getTag() != null) {
                WheelView wheelOne = (WheelView) mPopupWindow.getContentView().findViewById(R.id.wheel_one);
                wheelOne.setCurrentItem(Integer.valueOf(mButtonNumber.getTag().toString()) - 1);
            }
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "男");
            mList.add(map);
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("name", "女");
            mList.add(map1);
            mPopupWindow = WheelUtils.showSingleWheel(this, mList,
                    mSexBtn, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index1 = (Integer) v.getTag(R.id.wheel_one);
                            Map<String, String> map = mList.get(index1);
                            String age = map.get("name");
                            mSexBtn.setText(age);
                            mSexBtn.setTag(age);
                            if ("男".equals(mSexBtn.getTag().toString())) {
                                pregnancy.setVisibility(View.GONE);
                                mButtonNumber.setVisibility(View.GONE);
                                mSex = "M";
                            } else {
                                mSex = "W";
                                if (Integer.valueOf(mButton.getTag().toString()) <= 15) {
                                    pregnancy.setVisibility(View.GONE);
                                    mButtonNumber.setVisibility(View.GONE);
                                    mPregnancy = "N";
                                } else {
                                    pregnancy.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });

            if (mSexBtn.getTag() != null) {
                WheelView wheelOne = (WheelView) mPopupWindow.getContentView().findViewById(R.id.wheel_one);
                if ("男".equals(mSexBtn.getTag().toString())) {
                    wheelOne.setCurrentItem(0);
                } else {
                    wheelOne.setCurrentItem(1);
                }

            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int vId = buttonView.getId();

        if (vId == MResource.getIdByName(getApplicationContext(), "id", "fuck")) {
            if (isChecked) {
                mButtonNumber.setVisibility(View.VISIBLE);
                mPregnancy = "Y";
            }
        } else if (vId == MResource.getIdByName(getApplicationContext(), "id", "nofuck")) {
            if (isChecked) {
                mButtonNumber.setVisibility(View.GONE);
                mPregnancy = "N";
            }
        }


//		switch (buttonView.getId()) {
////		case R.id.man:// 男的
////			if(isChecked){
////				pregnancy.setVisibility(View.GONE);
////				mButtonNumber.setVisibility(View.GONE);
////				mSex="M";
////			}
////			break;
////		case R.id.woman:// 女的
////			if(isChecked){
////				pregnancy.setVisibility(View.VISIBLE);
////				mSex="W";
////			}
////			break;
////		case R.id.fuck:// 怀孕
////			if(isChecked){
////				mButtonNumber.setVisibility(View.VISIBLE);
////				mPregnancy="Y";
////			}
////			break;
//		case R.id.nofuck:// 未孕
//			if(isChecked){
//				mButtonNumber.setVisibility(View.GONE);
//				mPregnancy="N";
//			}
//			
//			break;
//		}
    }
}
