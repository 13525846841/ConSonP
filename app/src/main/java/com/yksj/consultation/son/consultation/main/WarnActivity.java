package com.yksj.consultation.son.consultation.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

public class WarnActivity extends BaseFragmentActivity implements View.OnClickListener {

    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("余额提现");
        titleLeftBtn.setOnClickListener(this);
        sure = (Button) findViewById(R.id.sure);
        sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.sure:
                finish();
                break;
        }
    }
}
