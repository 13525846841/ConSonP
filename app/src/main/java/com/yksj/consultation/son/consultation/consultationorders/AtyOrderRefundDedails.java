package com.yksj.consultation.son.consultation.consultationorders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

/**
 * Created by HEKL on 15/9/22.
 * 会诊订单退款详情页
 */
public class AtyOrderRefundDedails extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_orderrefunddetails);
        initView();
    }

    private void initView() {
        initTitle();
        String price = getIntent().getStringExtra("Price");
        int type = getIntent().getIntExtra("Type", 0);
        titleTextV.setText("查看详情");
        titleLeftBtn.setOnClickListener(this);
        TextView mTextPrice = (TextView) findViewById(R.id.tv_price);
        TextView mTextTip = (TextView) findViewById(R.id.tv_state);
        if (!TextUtils.isEmpty(price)) {
            StringBuffer sbPirce = new StringBuffer();
            sbPirce.append(price + "元");
            mTextPrice.setText(sbPirce);
        }
        if (type == 0) {
            mTextTip.setText("退款中");
        } else if (type == 1) {
            mTextTip.setText("已退款");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}