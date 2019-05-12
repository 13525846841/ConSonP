package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.HealthLectureAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.HealthLectureWorksEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HealthLectureActivity extends Activity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
    private View mEmptyView;
    private org.handmark.pulltorefresh.library.PullToRefreshListView pullListView;
    private ListView mListView;
    private List<HealthLectureWorksEntity.ResultBean> mList = new ArrayList<>();
    private HealthLectureAdapter mAdapter;
    private Button  zansortAll, sortZannei, sortZanwai, priceSortAll, sortFufei, sortMianfei;
    private String lectureType;//doctor 医生主页点击进来  works  工作站点击进来  all首页点进来
    private LinearLayout lineZan, linePrice,lineAll;
    private String site_id,customer_id;
    private int pageNum=1;
    private String sortType="4";//1-科普 2-学术 3-人文 4-最新 5-最热
    private TextView sortAll,sortNew,sortHot,sortXueshu,sortRenwen,sortKepu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_lecture);
        initView();
    }

    private void initView() {
        lectureType = getIntent().getStringExtra("lectureType");
        site_id = getIntent().getStringExtra("site_id");
        customer_id = getIntent().getStringExtra("customer_id");
        String doctorName = getIntent().getStringExtra("doctorName");
        TextView title = (TextView) findViewById(R.id.title_lable);
        title.setText("健康讲堂");
        findViewById(R.id.title_back).setOnClickListener(this);
        sortAll = (TextView) findViewById(R.id.sortAll);
        sortAll.setOnClickListener(this);
        sortNew = (TextView) findViewById(R.id.sortNew);
        sortNew.setOnClickListener(this);
        sortHot = (TextView) findViewById(R.id.sortHot);
        sortHot.setOnClickListener(this);
        sortKepu = (TextView) findViewById(R.id.sortKepu);
        sortKepu.setOnClickListener(this);
        sortXueshu = (TextView) findViewById(R.id.sortXueshu);
        sortXueshu.setOnClickListener(this);
        sortRenwen = (TextView) findViewById(R.id.sortRenwen);
        sortRenwen.setOnClickListener(this);
        zansortAll = (Button) findViewById(R.id.zansortAll);
        zansortAll.setOnClickListener(this);
        sortZannei = (Button) findViewById(R.id.sortZannei);
        sortZannei.setOnClickListener(this);
        sortZanwai = (Button) findViewById(R.id.sortZanwai);
        sortZanwai.setOnClickListener(this);
        priceSortAll = (Button) findViewById(R.id.priceSortAll);
        priceSortAll.setOnClickListener(this);
        sortFufei = (Button) findViewById(R.id.sortFufei);
        sortFufei.setOnClickListener(this);
        sortMianfei = (Button) findViewById(R.id.sortMianfei);
        sortMianfei.setOnClickListener(this);
        lineZan = (LinearLayout) findViewById(R.id.lineZan);
        lineAll = (LinearLayout) findViewById(R.id.lineAll);
        linePrice = (LinearLayout) findViewById(R.id.linePrice);
        setSortBtnbg(sortNew);
        sortAll.setVisibility(View.GONE);
        linePrice.setVisibility(View.GONE);
        lineZan.setVisibility(View.GONE);
        //医生列表适配
        pullListView = (org.handmark.pulltorefresh.library.PullToRefreshListView) findViewById(R.id.healthPulllist);
        mEmptyView = findViewById(R.id.load_faile_layout);
        pullListView.setOnRefreshListener(this);
        mListView = pullListView.getRefreshableView();
        mListView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        mListView.setOnItemClickListener(this);
        mAdapter = new HealthLectureAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        if (lectureType.equals("doctor")) {
            lineAll.setVisibility(View.GONE);
            title.setText(doctorName+"健康讲堂");}
        loadData();
    }

    private void loadData(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        if (lectureType.equals("works")){
            pairs.add(new BasicNameValuePair("type", sortType));
            pairs.add(new BasicNameValuePair("site_id", site_id));
        }else if (lectureType.equals("all")){
            pairs.add(new BasicNameValuePair("op", "AllCourse"));
            pairs.add(new BasicNameValuePair("type", sortType));
        } else {
            pairs.add(new BasicNameValuePair("op", "queryPersonClassroom"));
            pairs.add(new BasicNameValuePair("customer_id", customer_id));
        }
        pairs.add(new BasicNameValuePair("pageNum", ""+pageNum));
        if (lectureType.equals("works")){
            HttpRestClient.doGetHuanZheWorkSiteClass(pairs, new OkHttpClientManager.ResultCallback<String>() {

                @Override
                public void onError(Request request, Exception e) {
                }

                @Override
                public void onBefore(Request request) {
                    super.onBefore(request);
                    pullListView.setRefreshing();
                }

                @Override
                public void onAfter() {
                    pullListView.onRefreshComplete();
                    super.onAfter();
                }

                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    HealthLectureWorksEntity healthLectureWorksEntity = gson.fromJson(response, HealthLectureWorksEntity.class);
                    List<HealthLectureWorksEntity.ResultBean> result = healthLectureWorksEntity.getResult();
                    mList.addAll(result);
                    mAdapter.onBoundData(mList);
                    emptyView(response);
                }
            },this);
        }else {
            HttpRestClient.doGetPersonClassroom(pairs, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                }

                @Override
                public void onBefore(Request request) {
                    super.onBefore(request);
                    pullListView.setRefreshing();
                }

                @Override
                public void onAfter() {
                    pullListView.onRefreshComplete();
                    super.onAfter();
                }

                @Override
                public void onResponse(String response) {
                    if (response.equals("")){
                        emptyView(response);
                        return;
                    }
                    Gson gson = new Gson();
                    HealthLectureWorksEntity healthLectureWorksEntity = gson.fromJson(response, HealthLectureWorksEntity.class);
                    List<HealthLectureWorksEntity.ResultBean> result = healthLectureWorksEntity.getResult();
                    mList.addAll(result);
                    mAdapter.onBoundData(mList);
                    emptyView(response);
                }
            },this);
        }
    }

    private void emptyView(String s) {
        if (mList.size()==0||s==null){
            mEmptyView.setVisibility(View.VISIBLE);
            pullListView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.GONE);
            pullListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HealthLectureWorksEntity.ResultBean resultBean = mList.get(position-1);
//        if (resultBean.getCOURSE_PAY().equals("1")&&resultBean.getCOURSE_STATUS().equals("0")) {
            Intent intent = new Intent(this, HealthLecturePayInfoActivity.class);
            intent.putExtra("course_ID",resultBean.getCOURSE_ID());
            intent.putExtra("tuwenTime",resultBean.getCOURSE_UP_TIME());
            startActivity(intent);
//        }else {
//            if (resultBean.getCOURSE_CLASS().equals("20")){
//                Intent intent = new Intent(this, HealthLectureTuwenActivity.class);
//                intent.putExtra("course_ID",resultBean.getCOURSE_ID());
//                intent.putExtra("info",resultBean);
//                startActivity(intent);
//            }else if (resultBean.getCOURSE_CLASS().equals("30")){
//                Intent intent = new Intent(this, HealthLectureHomeActivity.class);
//                intent.putExtra("course_ID",resultBean.getCOURSE_ID());
//                intent.putExtra("info",resultBean);
//                startActivity(intent);
//            }
//
//        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
       pageNum=1;
        mList.clear();
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
       pageNum++;
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.sortAll:
                setSortBtnbg(sortAll);
                break;
            case R.id.sortNew:
                sortType="4";
                setSortBtnbg(sortNew);
                mList.clear();
                loadData();
                break;
            case R.id.sortHot:
                sortType="5";
                setSortBtnbg(sortHot);
                mList.clear();
                loadData();
                break;
            case R.id.sortKepu:
                sortType="1";
                setSortBtnbg(sortKepu);
                mList.clear();
                loadData();
                break;
            case R.id.sortXueshu:
                sortType="2";
                setSortBtnbg(sortXueshu);
                mList.clear();
                loadData();
                break;
            case R.id.sortRenwen:
                sortType="3";
                setSortBtnbg(sortRenwen);
                mList.clear();
                loadData();
                break;
            case R.id.zansortAll:
                setZanSortBtnbg(zansortAll);
                break;
            case R.id.sortZannei:
                setZanSortBtnbg(sortZannei);
                break;
            case R.id.sortZanwai:
                setZanSortBtnbg(sortZanwai);
                break;
            case R.id.priceSortAll:
                setPriceSortBtnbg(priceSortAll);
                break;
            case R.id.sortFufei:
                setPriceSortBtnbg(sortFufei);
                break;
            case R.id.sortMianfei:
                setPriceSortBtnbg(sortMianfei);
                break;
        }
    }

    private void setSortBtnbg(TextView btn) {
        sortNew.setBackgroundResource(sortType.equals("4")?R.drawable.btn_green_line_bg:0);
        sortNew.setTextColor(sortType.equals("4")?Color.parseColor("#41b8b6"):Color.parseColor("#989898"));
        sortHot.setBackgroundResource(sortType.equals("5")?R.drawable.btn_green_line_bg:0);
        sortHot.setTextColor(sortType.equals("5")?Color.parseColor("#41b8b6"):Color.parseColor("#989898"));
        sortKepu.setBackgroundResource(sortType.equals("1")?R.drawable.btn_green_line_bg:0);
        sortKepu.setTextColor(sortType.equals("1")?Color.parseColor("#41b8b6"):Color.parseColor("#989898"));
        sortXueshu.setBackgroundResource(sortType.equals("2")?R.drawable.btn_green_line_bg:0);
        sortXueshu.setTextColor(sortType.equals("2")?Color.parseColor("#41b8b6"):Color.parseColor("#989898"));
        sortRenwen.setBackgroundResource(sortType.equals("3")?R.drawable.btn_green_line_bg:0);
        sortRenwen.setTextColor(sortType.equals("3")?Color.parseColor("#41b8b6"):Color.parseColor("#989898"));
//        sortAll.setBackgroundResource(sortType.equals("4")?R.drawable.btn_green_line_bg:0);
//        sortAll.setTextColor(sortType.equals("4")?Color.parseColor("#989898"):Color.parseColor("#41b8b6"));//        sortHot.setBackgroundResource(R.drawable.btn_green_line_bg);
//        sortHot.setTextColor(Color.parseColor("#000000"));
//        sortKepu.setBackgroundResource(R.drawable.btn_green_line_bg);
//        sortKepu.setTextColor(Color.parseColor("#000000"));
//        sortXueshu.setBackgroundResource(R.drawable.btn_green_line_bg);
//        sortXueshu.setTextColor(Color.parseColor("#000000"));
//        sortRenwen.setBackgroundResource(R.drawable.btn_green_line_bg);
//        sortRenwen.setTextColor(Color.parseColor("#000000"));
//        btn.setBackgroundResource(R.drawable.btn_green_bg);
//        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    private void setZanSortBtnbg(Button btn) {
        zansortAll.setBackgroundResource(R.drawable.btn_green_line_bg);
        zansortAll.setTextColor(Color.parseColor("#000000"));
        sortZannei.setBackgroundResource(R.drawable.btn_green_line_bg);
        sortZannei.setTextColor(Color.parseColor("#000000"));
        sortZanwai.setBackgroundResource(R.drawable.btn_green_line_bg);
        sortZanwai.setTextColor(Color.parseColor("#000000"));
        btn.setBackgroundResource(R.drawable.btn_green_bg);
        btn.setTextColor(Color.parseColor("#ffffff"));
    }

    private void setPriceSortBtnbg(Button btn) {
        priceSortAll.setBackgroundResource(R.drawable.btn_green_line_bg);
        priceSortAll.setTextColor(Color.parseColor("#000000"));
        sortFufei.setBackgroundResource(R.drawable.btn_green_line_bg);
        sortFufei.setTextColor(Color.parseColor("#000000"));
        sortMianfei.setBackgroundResource(R.drawable.btn_green_line_bg);
        sortMianfei.setTextColor(Color.parseColor("#000000"));
        btn.setBackgroundResource(R.drawable.btn_green_bg);
        btn.setTextColor(Color.parseColor("#ffffff"));

    }

}
