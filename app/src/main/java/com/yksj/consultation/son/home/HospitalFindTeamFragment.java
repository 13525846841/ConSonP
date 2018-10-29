package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousDoctorAreaSelectAdapter;
import com.yksj.consultation.adapter.HospitalFindTeamAdapter;
import com.yksj.consultation.adapter.HotAreaAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.consultation.son.listener.OnRecyclerTypeClickListener;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.entity.HospitalHotAreaEntity;
import com.yksj.healthtalk.entity.SelectChildCityEntity;
import com.yksj.healthtalk.entity.SelectProvinceEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class HospitalFindTeamFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, OnRecyclerClickListener, OnRecyclerTypeClickListener, AdapterView.OnItemClickListener, StationMainUI.EdtSearchChanageListener {
    private View mEmptyView;
    private org.handmark.pulltorefresh.library.PullToRefreshListView pullListView;
    private List<HospitaFindTeamEntity.ResultBean.ListBean> mList = new ArrayList<>();
    private List<HospitalHotAreaEntity.ResultBean> mHotAreaList = new ArrayList<>();
    private List<SelectProvinceEntity.AreaBean> provinceList=new ArrayList<>();
    private List<SelectChildCityEntity.AreaBean> childCityList=new ArrayList<>();
    private ListView mListView;
    private int pageSize = 1;
    private String areaCode="";//城市编码  340100
    private String tempAreaCode="";
    private String provinceAreaCode="";//城市编码
    private String Search_Str="";//搜索条件
    private HospitalFindTeamAdapter mAdapter;
    private HotAreaAdapter hotAreaAdapter;
    private LinearLayout province;
    private TextView tvProvince;
    private FamousDoctorAreaSelectAdapter cityAdapter;
    private FamousDoctorAreaSelectAdapter provinceAdapter;
    private LinearLayout otherAreaLinear;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydoc_list_fragment, null);
        initView(view);
        StationMainUI activity = (StationMainUI) getActivity();
        activity.setEdtSearchChanageListener(this);
        loadDataWss();
        loadDataHotCityWss();
        //type  1是省  2是市
        loadDataProvince(1);
        return view;
    }

    private void initView(View view) {
        //热门地区适配
        RecyclerView cityRecycler= (RecyclerView) view.findViewById(R.id.cityRecycler);
        hotAreaAdapter = new HotAreaAdapter(mHotAreaList, getActivity());
        hotAreaAdapter.setOnRecyclerClickListener(this);
        cityRecycler.setLayoutManager(new GridLayoutManager(getActivity(),4));
        cityRecycler.setAdapter(hotAreaAdapter);
        //更多地区选择
        RecyclerView provinceRecycler= (RecyclerView) view.findViewById(R.id.provinceRecycler);
        provinceAdapter = new FamousDoctorAreaSelectAdapter(getActivity(), provinceList, 1);
        provinceAdapter.setOnRecyclerClick(this);
        provinceRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        provinceRecycler.setAdapter(provinceAdapter);

        RecyclerView childCityRecycler= (RecyclerView) view.findViewById(R.id.childCityRecycler);
        cityAdapter = new FamousDoctorAreaSelectAdapter(getActivity(), childCityList, 2);
        cityAdapter.setOnRecyclerClick(this);
        childCityRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        childCityRecycler.setAdapter(cityAdapter);
        //地区选择
        province = (LinearLayout) view.findViewById(R.id.province);
        tvProvince = (TextView) view.findViewById(R.id.tvProvince);
        province.setOnClickListener(this);
        view.findViewById(R.id.city).setOnClickListener(this);
        //医生列表适配
        pullListView = (org.handmark.pulltorefresh.library.PullToRefreshListView) view.findViewById(R.id.find_friend_pulllist);
        mEmptyView = view.findViewById(R.id.load_faile_layout);
        pullListView.setOnRefreshListener(this);
        mListView = pullListView.getRefreshableView();
        mListView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        mListView.setOnItemClickListener(this);
        mAdapter = new HospitalFindTeamAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        otherAreaLinear = (LinearLayout) view.findViewById(R.id.otherAreaLinear);
        otherAreaLinear.setOnClickListener(this);

    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        Log.i("wewe", "loadDataWss:     "+Search_Str+"======"+areaCode);
            pairs.add(new BasicNameValuePair("Type", "findWorkSiteByHospital"));
            pairs.add(new BasicNameValuePair("Search_Str", Search_Str));
            pairs.add(new BasicNameValuePair("Area_Code", areaCode));
            pairs.add(new BasicNameValuePair("PageSize", "10"));
            pairs.add(new BasicNameValuePair("Page", pageSize+""));
            pairs.add(new BasicNameValuePair("Office_Id", ""));
        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
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
                    HospitaFindTeamEntity hospitaFindTeamEntity = gson.fromJson(response, HospitaFindTeamEntity.class);
                List<HospitaFindTeamEntity.ResultBean.ListBean> list = hospitaFindTeamEntity.getResult().getList();
                    mList.addAll(list);
                    mAdapter.onBoundData(mList);
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    pullListView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    pullListView.setVisibility(View.VISIBLE);
                }
                Search_Str="";
            }
        },this);

    }
    private void loadDataProvince(final int type){
        List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("op", "findArea"));
        if (type==2){
            pairs.add(new BasicNameValuePair("area_code",provinceAreaCode));
        }

        HttpRestClient.doGetProvinceSee(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                if (type==1){
                    SelectProvinceEntity selectProvinceEntity = gson.fromJson(response, SelectProvinceEntity.class);
                    List<SelectProvinceEntity.AreaBean> area = selectProvinceEntity.getArea();
                    provinceList.addAll(area);
                    provinceList.add(0,null);
                    provinceAdapter.notifyDataSetChanged();
                }else{
                    SelectChildCityEntity childCityEntity = gson.fromJson(response, SelectChildCityEntity.class);
                    List<SelectChildCityEntity.AreaBean> area = childCityEntity.getArea();
                    childCityList.addAll(area);
                    childCityList.add(0,null);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        },this);
    }
    private void loadDataHotCityWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "hotArea"));
        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
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
                HospitalHotAreaEntity hospitalHotAreaEntity = gson.fromJson(response, HospitalHotAreaEntity.class);
                List<HospitalHotAreaEntity.ResultBean> beanList = hospitalHotAreaEntity.getResult();
                mHotAreaList.addAll(beanList);
                hotAreaAdapter.notifyDataSetChanged();
            }
        },this);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageSize=1;
        mList.clear();
        loadDataWss();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageSize++;
        loadDataWss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.province:
                if (otherAreaLinear.getVisibility()== View.GONE) {
                    otherAreaLinear.setVisibility(View.VISIBLE);
                }else {
                    otherAreaLinear.setVisibility(View.GONE);
                }
                break;
            case R.id.city:break;
            case R.id.otherAreaLinear:
                otherAreaLinear.setVisibility(View.GONE);
                break;
        }
    }


     //热门地区点击事件
    @Override
    public void onRecyclerItemClickListener(int position, View itemView,int type) {
        mList.clear();
        otherAreaLinear.setVisibility(View.GONE);
        areaCode=mHotAreaList.get(position).getAREA_CODE();
        loadDataWss();
        areaCode=tempAreaCode;
    }
    //地区选择的点击事件
    @Override
    public void setOnCliCkListener(View view, int position, int type) {
       if (type==1){
           if (position==0){
               childCityList.clear();
               childCityList.add(0,null);
               areaCode="";//等于""时表示查询全部的数据
           }else {
               childCityList.clear();
               provinceAreaCode=provinceList.get(position).getAREA_CODE();
               areaCode= provinceList.get(position).getAREA_CODE();
               loadDataProvince(2);
           }
           provinceAdapter.setSelectProvince(position);
           provinceAdapter.notifyDataSetChanged();
       }else {
           if (position==0){
               loadDataWss();
               tvProvince.setText("全部");
               tempAreaCode=areaCode;
           }else {
               SelectChildCityEntity.AreaBean areaBean = childCityList.get(position);
               areaCode= areaBean.getAREA_CODE();
               tvProvince.setText(areaBean.getAREA_NAME2());
               loadDataWss();
           }
           mList.clear();
           otherAreaLinear.setVisibility(View.GONE);
       }
    }
    //listView点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DoctorWorkstationMainActivity.class);
        intent.putExtra(DoctorWorkstationMainActivity.SITE_ID,mList.get(position-1).getSITE_ID()+"");
        startActivity(intent);
    }

    @Override
    public void edtSearchListener(String searchContent) {
        areaCode="";
        Search_Str=searchContent;
        pageSize=1;
        mList.clear();
        loadDataWss();
    }
}
