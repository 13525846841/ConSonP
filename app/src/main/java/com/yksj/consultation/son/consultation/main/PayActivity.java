package com.yksj.consultation.son.consultation.main;


import android.os.Bundle;
import android.view.View;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

public class PayActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("支付");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}
