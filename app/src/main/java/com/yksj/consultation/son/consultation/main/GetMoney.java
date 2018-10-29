package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 提现界面
 */

public class GetMoney extends BaseFragmentActivity implements View.OnClickListener{

    private Button next;
    private EditText et_number;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("余额提现");
        titleLeftBtn.setOnClickListener(this);
        next = (Button) findViewById(R.id.getmon_next);
        mTv = (TextView) findViewById(R.id.tv);
        mTv.setText(getIntent().getStringExtra("balance"));
        et_number = (EditText) findViewById(R.id.et_number);
        next.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.getmon_next:

                String number = et_number.getText().toString();
                if (HStringUtil.isEmpty(number)){
                    ToastUtil.showShort("请输入提现金额");
                }else if (Integer.parseInt(getIntent().getStringExtra("balance"))<Integer.parseInt(number)){
                    ToastUtil.showShort("提现金额不能大于余额");
                }else if (Integer.parseInt(number)<5){
                    ToastUtil.showShort("提现金额不能小于5元");
                } else{
                    Intent intent = new Intent(this,GmNexeActivity.class);
                    intent.putExtra("NUMBER",number);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }
}
