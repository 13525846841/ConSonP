package com.yksj.consultation.son.consultation.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;

/**
 * Created by tidus on 2016/11/9.
 */
public class FrgSixOneMessage  extends RootFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sixone_common_viewpager, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}
