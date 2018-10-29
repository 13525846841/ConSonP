package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.buyandsell.MyOrdersActivity;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.consultation.consultationorders.AtyMyOrders;

/**
 * 预约订单
 */
public class OrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();

    }

    private void initView() {
        initTitle();
        titleTextV.setText("我的订单");
        titleLeftBtn.setOnClickListener(this);

//        findViewById(R.id.rl_mianzhen).setOnClickListener(this);
//        findViewById(R.id.rl_huizhen).setOnClickListener(this);

        findViewById(R.id.rl_experience).setOnClickListener(this);
        findViewById(R.id.rl_picandcul).setOnClickListener(this);
        findViewById(R.id.rl_phone).setOnClickListener(this);
        findViewById(R.id.rl_consul).setOnClickListener(this);
        findViewById(R.id.rl_video).setOnClickListener(this);
        findViewById(R.id.rl_addnum).setOnClickListener(this);
        findViewById(R.id.rl_online2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rl_addnum://门诊界面
                intent = new Intent(this, MyOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_online2://会诊界面
                intent = new Intent(this, AtyMyOrders.class);
                startActivity(intent);
                break;
            case R.id.rl_experience://体验咨询
                intent = new Intent(this, ConsultActivity.class);
                intent.putExtra(ConsultActivity.TITLE,"体验咨询");
                intent.putExtra(ConsultActivity.SERVICE_TYPE_ID,"9");
                startActivity(intent);
                break;
            case R.id.rl_picandcul://图文咨询
                intent = new Intent(this,ConsultActivity.class);
                intent.putExtra(ConsultActivity.TITLE,"图文咨询");
                intent.putExtra(ConsultActivity.SERVICE_TYPE_ID,ServiceType.TW);
                startActivity(intent);
                break;
            case R.id.rl_phone://电话咨询
                intent = new Intent(this,ConsultActivity.class);
                intent.putExtra(ConsultActivity.TITLE,"电话咨询");
                intent.putExtra(ConsultActivity.SERVICE_TYPE_ID,ServiceType.DH);
                startActivity(intent);
                break;
            case R.id.rl_consul://包月咨询
                intent = new Intent(this,ConsultActivity.class);
                intent.putExtra(ConsultActivity.TITLE,"包月咨询");
                intent.putExtra(ConsultActivity.SERVICE_TYPE_ID,ServiceType.BY);
                startActivity(intent);
                break;
            case R.id.rl_video://视频咨询d
                intent = new Intent(this,ConsultActivity.class);
                intent.putExtra(ConsultActivity.TITLE,"视频咨询");
                intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, ServiceType.SP);
                startActivity(intent);
                break;
        }
    }
}
