package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.DepartementFindTeamAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.DepartmentFindTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class DepartmentFindTeamFragment extends Fragment implements OnRecyclerClickListener {

    private RecyclerView departmentRecycler;

    private List<DepartmentFindTeamEntity.ResultBean> list;
    private DepartementFindTeamAdapter departementFindTeamAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_departement_find_team, null);
        list=new ArrayList<>();
        initView(view);
        loadDataWss();
        return view;
    }

    private void initView(View view) {
        departmentRecycler = (RecyclerView) view.findViewById(R.id.departmentRecycler);
        departmentRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        departementFindTeamAdapter = new DepartementFindTeamAdapter(list, getActivity());
        departementFindTeamAdapter.setOnRecyclerClickListener(this);
        departmentRecycler.setAdapter(departementFindTeamAdapter);
    }

    private void loadDataWss() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "findWorkSiteByOffice"));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DepartmentFindTeamEntity departmentFindTeamEntity = gson.fromJson(response, DepartmentFindTeamEntity.class);
                list.addAll(departmentFindTeamEntity.getResult());
                departementFindTeamAdapter.notifyDataSetChanged();
                Log.i("www", "onResponse--------: " + response);
            }
        }, this);

    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView,int type) {
        Intent intent = new Intent(getActivity(), DoctorWorkstationActivity.class);
        intent.putExtra("title",list.get(position).getOFFICE_NAME());
        intent.putExtra("office_id",list.get(position).getOFFICE_ID());
        startActivity(intent);
    }
}
