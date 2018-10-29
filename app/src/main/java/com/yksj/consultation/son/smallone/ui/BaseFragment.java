package com.yksj.consultation.son.smallone.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by jack_tang on 15/9/6.
 */
public class BaseFragment extends Fragment {

    public Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity =activity;
    }
}
