package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamDocPopAdapter;
import com.yksj.consultation.adapter.FamDoctorAdapter;
import com.yksj.consultation.adapter.SortAdapper;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DoctorInfoActivity;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 名医义诊
 */
public class FamDoctorActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView back;
    private ImageView onLine;
    private ImageView belowLine;
    private View mEmptyView;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private FamDoctorAdapter adapter;
    private List<JSONObject> mList = null;
    private String isOnline = "0";//1线上
    private CheckBox intelligent_sorting;//排序
    private CheckBox room_region;//科室
    private LinearLayout popupWLayout;
    private ListView listview;//popup 选择科室
    private ListView listview1;//popup 智能排序
    private FamDocPopAdapter popAdapter;
    private List<JSONObject> list = null;
    private String office_id ="";//科室id
    private SortAdapper sortAdapper;
    private String isOrder = "0";//是否智能排序 1.价格由高到低 2。人气由高到低
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fam_doctor);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.title_back7);
        back.setOnClickListener(this);
        onLine = (ImageView) findViewById(R.id.iv_famous_fragment1);
        belowLine = (ImageView) findViewById(R.id.iv_famous_fragment2);
        onLine.setOnClickListener(this);
        belowLine.setOnClickListener(this);
        onLine.setSelected(true);
        mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.fam_doctor_list);
        mListView = mPullToRefreshListView.getRefreshableView();
        listview = (ListView) findViewById(R.id.pop_list);
        listview1 = (ListView) findViewById(R.id.pop_list);

        adapter = new FamDoctorAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        intelligent_sorting = (CheckBox)findViewById(R.id.intelligent_sorting);
        intelligent_sorting.setOnClickListener(this);

        room_region = (CheckBox) findViewById(R.id.room_region);
        popupWLayout = (LinearLayout) findViewById(R.id.popwindow_layout1);

        //排序
        intelligent_sorting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    popupWLayout.setVisibility(View.GONE);
                    intelligent_sorting.setChecked(false);
                } else {
                    popupWLayout.setVisibility(View.VISIBLE);
                    sortAdapper = new SortAdapper(FamDoctorActivity.this);
                    listview1.setAdapter(sortAdapper);
                    listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position==0){
                                isOrder = "1";
                                intelligent_sorting.setText(sortAdapper.NAME(position));
                            }else if (position==1){
                                isOrder = "2";
                                intelligent_sorting.setText(sortAdapper.NAME(position));
                            }
                            initData();
                            outPopup1();
                        }
                    });
                }
            }
        });
        //科室
        room_region.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    popupWLayout.setVisibility(View.GONE);
                    room_region.setChecked(false);
                } else {
                    popupWLayout.setVisibility(View.VISIBLE);
                    popAdapter = new FamDocPopAdapter(FamDoctorActivity.this);
                    listview.setAdapter(popAdapter);
                    initDataTitle();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                room_region.setText(popAdapter.datas.get(position).optString("OFFICE_NAME"));
                office_id = popAdapter.datas.get(position).optString("OFFICE_SEQ");
                initData();
                outPopup();
            }
        });

        mEmptyView = findViewById(R.id.empty_view_famous);
        initData();
    }


    /**
     * 加载pop数据
     */
    private void initDataTitle() {
        HttpRestClient.OKHttpFindOffice(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("0".equals(obj.optString("code"))){
                        list = new ArrayList<JSONObject>();
                            JSONArray array = obj.getJSONArray("office");
                            JSONObject item;
                            for (int i = 0; i < array.length(); i++) {
                                item = array.getJSONObject(i);
                                list.add(item);
                            }
                        popAdapter.onBoundData(list);
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }

    //关闭popupwindow
    private void outPopup() {
        if (room_region.isChecked()){
            room_region.setChecked(false);
        }else{
            room_region.setChecked(true);
        }
        popupWLayout.setVisibility(View.GONE);
    }
    //关闭智能排序的popup
    private void outPopup1() {
        if (intelligent_sorting.isChecked()){
            intelligent_sorting.setChecked(false);
        }else{
            intelligent_sorting.setChecked(true);
        }
        popupWLayout.setVisibility(View.GONE);
    }
    /**
     * 加载数据
     */
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("isOnline",isOnline);
        map.put("isOrder",isOrder);
        map.put("office_id",office_id);
//        Log.i("yiz", "initData: http://192.168.1.110:8080/DuoMeiHealth/see.do?op=findseeList?isOnline="+isOnline+"&isOrder="+isOrder+"&office_id="+office_id);
        HttpRestClient.OKHttpFamDoc(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray array = obj.optJSONArray("sees");
                        mList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList.add(jsonobject);
                        }
                        adapter.onBoundData(mList);
                        if (mList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                        }
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                        mEmptyView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back7:
                onBackPressed();
                break;
            case R.id.iv_famous_fragment1:
                onLine.setSelected(true);
                belowLine.setSelected(false);
                isOnline = "0";
                initData();
                break;
            case R.id.iv_famous_fragment2:
                onLine.setSelected(false);
                belowLine.setSelected(true);
                isOnline = "1";
                initData();
                break;
//            case R.id.intelligent_sorting:
//                isOrder = "1";
//                initData();
//                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, DoctorStudioActivity.class);
//        intent.putExtra("DOCTOR_ID",adapter.doctorId(position));
//        startActivity(intent);
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra("customer_id",Integer.valueOf(adapter.doctorId(position)));
        startActivity(intent);
    }
}
