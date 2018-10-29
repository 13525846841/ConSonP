package com.yksj.healthtalk.permission.target;

import android.content.Context;
import android.content.Intent;

/**
 * Created by hww on 17/11/20.
 */

public interface Target {

    Context getContext();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

}