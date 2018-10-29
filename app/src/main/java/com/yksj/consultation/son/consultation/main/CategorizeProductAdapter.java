package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/8/9.
 */
public class CategorizeProductAdapter extends FragmentStatePagerAdapter {
    private List<JSONObject> mList = new ArrayList<>();

    public CategorizeProductAdapter(FragmentManager fm, List<JSONObject> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CategorizeListContentFragment();
        Bundle bundle = new Bundle();
        // 把选中的index指针传入过去
        bundle.putString("index", mList.get(position).optString("CLASS_ID"));
        // 设定在fragment当中去
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
