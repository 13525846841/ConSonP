package com.yksj.healthtalk.permission.target;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hww on 17/11/20.
 */

public class AppFragmentTarget implements Target {

    private Fragment mFragment;

    public AppFragmentTarget(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public Context getContext() {
        return mFragment.getActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        mFragment.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mFragment.startActivityForResult(intent, requestCode);
    }
}
