package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yksj.consultation.adapter.SelExpertListAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.doctor.SearchExpertActivity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.ToastUtil;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 专家会诊中的会诊专家
 */
public class PCExpertMainUIActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener2<ListView>,PCExpertMainUIFragment.SelectorResultListener {
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private SelExpertListAdapter mAdapter;
    private LinearLayout navLayout;
    private FragmentManager manager;
    private PCExpertMainUIFragment navFragment;


    private List<JSONObject> mList = new ArrayList<>();
    private int conPageSize=1;//当前的页数
    private Bundle bundle;
    private int goalType=0;//0为默认 1找医生 2为患者重选专家
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcexpert_main_ui);

        initView();
        manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction = manager.beginTransaction();
        navFragment=new PCExpertMainUIFragment();
        navFragment.setSelectorListener(this);
        transaction.add(R.id.navigationbar_layout, navFragment);
        transaction.commit();
    }

    private void initView() {
        initTitle();
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setBackgroundResource(R.drawable.ig_seach);
        titleTextV.setText("会诊专家");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        mPullRefreshListView=(PullToRefreshListView) findViewById(R.id.select_expert_list);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
        mPullRefreshListView.setOnRefreshListener(this);
        mAdapter = new SelExpertListAdapter(PCExpertMainUIActivity.this,mList);
      //mAdapter.setFromType(1);
        mListView.setAdapter(mAdapter);

        if (getIntent().hasExtra("text")){
            String text = getIntent().getStringExtra("text");
            initSearchData(text);
        }
        initData();
    }

    /**
     * 根据搜索加载的数据
     * @param text
     */
    private void initSearchData(String text) {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2://搜索
                intent=new Intent(PCExpertMainUIActivity.this,SearchExpertActivity.class);
                intent.putExtra("type",goalType);
                intent.putExtra("CLINIC","CLINIC");
                intent.putExtra("OFFICECODE","");
                startActivity(intent);
                break;
            case R.id.select_expert_list_item_select://搜索
                intent=new Intent(PCExpertMainUIActivity.this,FlowMassageActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void goNotifyLoadData(String areaCode, String unitCode, String officeId) {

    }
}
