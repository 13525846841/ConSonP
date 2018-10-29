package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.consultation.son.consultation.main.OrderActivity;
import com.yksj.healthtalk.utils.FriendHttpUtil;

/**
 * 支付成功界面
 */
public class PAytSuccess extends BaseFragmentActivity implements View.OnClickListener {


    private TextView pay_id;
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ORDER_ID = "order_id";
    public static final String PAY_ID = "pay_id";
    public static final String ORDER_TYPE = "order_type";// 订单类型  5图文

    private String name = "";
    private String id = "";
    private String order_id = "";
    private String order_type = "";// 订单类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payt_success);
        initView();
    }

    private void initView() {

        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("支付成功");
        pay_id = (TextView) findViewById(R.id.pay_id);
        pay_id.setText("订单号:      " + getIntent().getStringExtra(PAY_ID));

        if (getIntent().hasExtra(NAME)) {
            name = getIntent().getStringExtra(NAME);
        }
        if (getIntent().hasExtra(ID)) {
            id = getIntent().getStringExtra(ID);
        }
        if (getIntent().hasExtra(ORDER_ID)) {
            order_id = getIntent().getStringExtra(ORDER_ID);
        }
        if (getIntent().hasExtra(ORDER_TYPE)) {
            order_type = getIntent().getStringExtra(ORDER_ID);
        }
        if (order_type.equals(ServiceType.TW) || order_type.equals(ServiceType.BY)) {
            findViewById(R.id.consulting).setVisibility(View.VISIBLE);
            findViewById(R.id.consulting).setOnClickListener(this);
        }
        findViewById(R.id.checkOrder).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.consulting:
                FriendHttpUtil.chatFromPerson(this, id, name, order_id, ObjectType.SPECIAL_SERVER);
                finish();
                break;
            case R.id.checkOrder:
                intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
