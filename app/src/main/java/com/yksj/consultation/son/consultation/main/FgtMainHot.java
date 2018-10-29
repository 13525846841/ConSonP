package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAidAty;

/**
 * Created by ${chen} on 2016/11/14.
 */
public class FgtMainHot extends RootFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fgt_mainhot,null,false);
        view.setOnClickListener(this);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(getActivity(),HotActivity.class);额
//        startActivity(intent);
        Intent intent = new Intent(getActivity(),CommonwealAidAty.class);
        //intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().SHAREHTML+"?customer_id=" + LoginServiceManeger.instance().getLoginUserId());
        intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().SHAREHTML+"?customer_id=" + "124862");
        intent.putExtra(CommonwealAidAty.TITLE,"名医分享");

        startActivity(intent);
    }
}
