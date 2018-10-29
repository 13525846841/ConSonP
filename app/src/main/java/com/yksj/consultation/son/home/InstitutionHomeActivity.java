package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousDoctorAreaSelectAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.main.HomePageActivity;
import com.yksj.consultation.son.fragment.InstitutionHomeFragment;
import com.yksj.consultation.son.listener.OnRecyclerTypeClickListener;
import com.yksj.healthtalk.entity.SelectChildCityEntity;
import com.yksj.healthtalk.entity.SelectProvinceEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class InstitutionHomeActivity extends FragmentActivity implements View.OnClickListener, OnRecyclerTypeClickListener {

    private PopupWindow popupWindow;
    private ImageView insHomeMore;
    private List<SelectProvinceEntity.AreaBean> provinceList = new ArrayList<>();
    private List<SelectChildCityEntity.AreaBean> childCityList = new ArrayList<>();
    private FamousDoctorAreaSelectAdapter cityAdapter;
    private FamousDoctorAreaSelectAdapter provinceAdapter;
    private String provinceAreaCode = "";//城市编码
    private String cityAreaCode = "";//城市编码
    private LinearLayout otherAreaLinear;
    public static final int EVENT_HOME_MSG = 0x232323;
    private TextView tvHomeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_home);
        initView();
    }

    private void initView() {
        popupWindow = new PopupWindow(this);
        Button title_back = (Button) findViewById(R.id.title_back);
        title_back.setOnClickListener(this);
        insHomeMore = (ImageView) findViewById(R.id.insHomeMore);
        insHomeMore.setOnClickListener(this);
        LinearLayout insHomeCitySelect = (LinearLayout) findViewById(R.id.insHomeCitySelect);
        insHomeCitySelect.setOnClickListener(this);
        LinearLayout insHomeTijian = (LinearLayout) findViewById(R.id.insHomeTijian);
        insHomeTijian.setOnClickListener(this);
        LinearLayout insHomeTuozhan = (LinearLayout) findViewById(R.id.insHomeTuozhan);
        insHomeTuozhan.setOnClickListener(this);
        LinearLayout insHomeKangfu = (LinearLayout) findViewById(R.id.insHomeKangfu);
        insHomeKangfu.setOnClickListener(this);
        LinearLayout insHomeXingque = (LinearLayout) findViewById(R.id.insHomeXingque);
        insHomeXingque.setOnClickListener(this);
        EditText insHomeSearchEdt = (EditText) findViewById(R.id.insHomeSearchEdt);
        insHomeSearchEdt.setFocusable(false);
        insHomeSearchEdt.setOnClickListener(this);
        ImageView insHomeImg= (ImageView) findViewById(R.id.insHomeImg);
        Glide.with(this).load(R.drawable.top_image).centerCrop().into(insHomeImg);
        tvHomeAddress = (TextView) findViewById(R.id.tvHomeAddress);
        TabLayout insHomeTabLayout = (TabLayout) findViewById(R.id.insHomeTabLayout);
        ViewPager insHomePager = (ViewPager) findViewById(R.id.insHomePager);
        insHomePager.setOffscreenPageLimit(3);
        final List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fragmentList.add(InstitutionHomeFragment.newInstance(i, ""));
        }
        insHomePager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        insHomeTabLayout.setupWithViewPager(insHomePager);
        insHomeTabLayout.getTabAt(0).setText("热门");
        insHomeTabLayout.getTabAt(1).setText("新入");
        insHomeTabLayout.getTabAt(2).setText("附近");

        //更多地区选择
        RecyclerView provinceRecycler = (RecyclerView) findViewById(R.id.provinceRecycler);
        provinceAdapter = new FamousDoctorAreaSelectAdapter(this, provinceList, 1);
        provinceAdapter.setOnRecyclerClick(this);
        provinceRecycler.setLayoutManager(new LinearLayoutManager(this));
        provinceRecycler.setAdapter(provinceAdapter);

        RecyclerView childCityRecycler = (RecyclerView) findViewById(R.id.childCityRecycler);
        cityAdapter = new FamousDoctorAreaSelectAdapter(this, childCityList, 2);
        cityAdapter.setOnRecyclerClick(this);
        childCityRecycler.setLayoutManager(new LinearLayoutManager(this));
        childCityRecycler.setAdapter(cityAdapter);
        otherAreaLinear = (LinearLayout) findViewById(R.id.otherAreaLinear);
        otherAreaLinear.setOnClickListener(this);
        //type  1是省  2是市
        loadDataProvince(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.insHomeSearchEdt:
                startActivity(new Intent(this, HomePageActivity.class));
                break;
            case R.id.insHomeMore:
                insMorePopup();
                break;
            case R.id.insHomeCitySelect:
                otherAreaLinear.setVisibility(View.VISIBLE);
                break;
            case R.id.insHomeTijian:
                Intent intent1 = new Intent(this, InstitutionListActivity.class);
                intent1.putExtra(InstitutionListActivity.SORT, 0);
                intent1.putExtra("addressCode", cityAreaCode);
                startActivity(intent1);
                break;
            case R.id.insHomeTuozhan:
                Intent intent2 = new Intent(this, InstitutionListActivity.class);
                intent2.putExtra(InstitutionListActivity.SORT, 1);
                intent2.putExtra("addressCode", cityAreaCode);
                startActivity(intent2);
                break;
            case R.id.insHomeKangfu:
                Intent intent3 = new Intent(this, InstitutionListActivity.class);
                intent3.putExtra(InstitutionListActivity.SORT, 2);
                intent3.putExtra("addressCode", cityAreaCode);
                startActivity(intent3);
                break;
            case R.id.insHomeXingque:
                Intent intent4 = new Intent(this, InstitutionListActivity.class);
                intent4.putExtra(InstitutionListActivity.SORT, 3);
                intent4.putExtra("addressCode", cityAreaCode);
                startActivity(intent4);
                break;
            case R.id.myIns:
                Intent intent = new Intent(this, InstitutionListActivity.class);
                intent.putExtra(InstitutionListActivity.SORT, -1);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.joinIns:
                startActivity(new Intent(this, InstitutionJoinActivity.class));
                popupWindow.dismiss();
                break;
            case R.id.parentLayout:
                popupWindow.dismiss();
                break;
            case R.id.otherAreaLinear:
                otherAreaLinear.setVisibility(View.GONE);
                break;
        }
    }

    private void insMorePopup() {
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        View inflate = View.inflate(this, R.layout.popupwindow_instituion_home_more, null);
        inflate.findViewById(R.id.myIns).setOnClickListener(this);
        inflate.findViewById(R.id.joinIns).setOnClickListener(this);
        inflate.findViewById(R.id.parentLayout).setOnClickListener(this);
        inflate.findViewById(R.id.line).setOnClickListener(this);
        popupWindow.setContentView(inflate);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(insHomeMore);
    }
    String address = "";
    @Override
    public void setOnCliCkListener(View view, int position, int type) {

        if (type == 1) {
            childCityList.clear();
            provinceAreaCode = provinceList.get(position).getAREA_CODE();
//                areaCode= provinceList.get(position).getAREA_CODE();
            address = provinceList.get(position).getAREA_NAME2();
            provinceAreaCode = provinceList.get(position).getAREA_CODE();
            loadDataProvince(2);
            provinceAdapter.setSelectProvince(position);
            provinceAdapter.notifyDataSetChanged();
        } else {
            SelectChildCityEntity.AreaBean areaBean = childCityList.get(position);
            cityAreaCode = areaBean.getAREA_CODE();
            otherAreaLinear.setVisibility(View.GONE);
            EventBus.getDefault().post(new MyEvent(cityAreaCode, EVENT_HOME_MSG));
            tvHomeAddress.setText(address);
         }
    }

    private void loadDataProvince(final int type) {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "findArea"));
        if (type == 2) {
            pairs.add(new BasicNameValuePair("area_code", provinceAreaCode));
        }

        HttpRestClient.doGetProvinceSee(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                if (type == 1) {
                    SelectProvinceEntity selectProvinceEntity = gson.fromJson(response, SelectProvinceEntity.class);
                    List<SelectProvinceEntity.AreaBean> area = selectProvinceEntity.getArea();
                    provinceList.addAll(area);
                    provinceAdapter.notifyDataSetChanged();
                } else {
                    SelectChildCityEntity childCityEntity = gson.fromJson(response, SelectChildCityEntity.class);
                    List<SelectChildCityEntity.AreaBean> area = childCityEntity.getArea();
                    childCityList.addAll(area);
//                    childCityList.add(0, null);
                    cityAdapter.notifyDataSetChanged();
                }
            }
        }, this);
    }
}
