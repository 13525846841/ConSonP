package com.yksj.consultation.son.consultation.outpatient;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;

/**
 * Created by HEKL on 15/9/22.
 * 退款详情页
 */
public class AtyRefundDedails extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_refunddetails);
        initView();
    }

    private void initView() {
        initTitle();
        String price = getIntent().getStringExtra("Price");
        String aTime = getIntent().getStringExtra("aTime");
        String bTime = getIntent().getStringExtra("bTime");
        titleTextV.setText("查看详情");
        titleLeftBtn.setOnClickListener(this);
        TextView mTextPrice = (TextView) findViewById(R.id.tv_price);
        TextView mTextBtime = (TextView) findViewById(R.id.tv_btime);
        TextView mTextAtime = (TextView) findViewById(R.id.tv_atime);
        TextView mTextTip = (TextView) findViewById(R.id.tv_tip);
        if (!TextUtils.isEmpty(price)) {
            StringBuffer sbPirce = new StringBuffer();
            sbPirce.append(price + "元");
            mTextPrice.setText(sbPirce);
        }
        if (!"".equals(bTime)) {
            StringBuffer sb = new StringBuffer();
            String beforeTime = TimeUtil.formatTime(bTime);
            sb.append(beforeTime + "  已受理");
            mTextBtime.setText(sb);
        }
        if (!"".equals(aTime)) {
            StringBuffer sb2 = new StringBuffer();
            String afterTime = TimeUtil.formatTime(aTime);
            sb2.append(afterTime + "  已完成");
            mTextAtime.setText(sb2);
        } else {
            mTextAtime.setVisibility(View.GONE);
            mTextTip.setTextColor(getResources().getColor(R.color.color_blue));
            mTextTip.setText("退款中");
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