package com.yksj.consultation.son.site;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import org.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * 抽象类  主要作为基类实现列表Activity 有下拉刷新  加载出错 搜索等
 * Created by lmk on 15/10/21.
 */
public abstract class BaseListActivity extends BaseFragmentActivity {

    protected PullToRefreshListView mPullListView;
    protected ListView mListView;
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.search_result_layout);
        initWitget();
        initView();
        initData();
    }

    protected abstract void initData();

    protected void initWitget(){
        initTitle();
        titleLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPullListView= (PullToRefreshListView) findViewById(R.id.search_result_pulllist);
        mListView=mPullListView.getRefreshableView();
    }

    protected abstract void initView();
    
    
}
