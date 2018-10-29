package com.yksj.consultation.son.consultation.news;

import android.os.Bundle;
import android.view.View;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

/**
 * 名医名院详情界面
 */
public class FamousHosActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_hos);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setVisibility(View.GONE);
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
