package com.yksj.consultation.son.consultation.outpatient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yksj.consultation.adapter.AdtOutPatient;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.LogUtil;

import org.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by HEKl on 2015/9/15.
 * Used for 门诊预约列表_
 */
public class FgtOutPatient extends RootFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("onCreateView", "onCreateView");

        View view = inflater.inflate(R.layout.fgt_outpatient, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        int TYPELIST = getArguments().getInt("typeList");
        PullToRefreshListView mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        ListView mListView = mRefreshableView.getRefreshableView();
        AdtOutPatient mAdapter = new AdtOutPatient(getActivity(), TYPELIST);
        mListView.setAdapter(mAdapter);
    }
}
