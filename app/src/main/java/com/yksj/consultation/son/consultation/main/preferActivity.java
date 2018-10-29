package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;

public class preferActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefer);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("我的优惠");
        findViewById(R.id.coupon).setOnClickListener(this);
        findViewById(R.id.native_activity).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.coupon:
                Intent intent = new Intent(this,CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.native_activity:
                ToastUtil.showShort("本地优惠");
                break;
        }
    }
}
