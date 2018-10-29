package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.consultation.adapter.DetailDocAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

/**
 * 名医义诊详情界面
 */

public class DetailDocActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private DetailDocAdapter adapter;
    private TextView title,time,text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_doc);
        initView();

    }

    private void initView() {
        initTitle();
        titleTextV.setText("名医义诊");
        titleLeftBtn.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.detail_doc_lv);
        View header = View.inflate(DetailDocActivity.this,R.layout.detail_doc_head,null);
        title = (TextView) header.findViewById(R.id.tv_title);
        time = (TextView) header.findViewById(R.id.tv_time);
        text = (TextView) header.findViewById(R.id.tv_text);
        mListView.addHeaderView(header);
        adapter =new DetailDocAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent(this,DoctorStudioActivity.class);
        startActivity(intent);
    }
}
