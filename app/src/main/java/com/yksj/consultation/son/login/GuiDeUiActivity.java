package com.yksj.consultation.son.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.yksj.consultation.comm.BaseFragmentActivity;

/**
 * Created by HEKL on 15/12/11.
 * Used for 首次登录
 */
public class GuiDeUiActivity extends BaseFragmentActivity {


    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        SharedPreferences sp = getSharedPreferences("isfrist", 0);
        if (sp.getBoolean("is_first", false)) {
            startActivity(new Intent(this, WelcomeActivity.class));
//            startActivity(new Intent(this, GuiDeUitwoActivity.class));
            finish();
        } else {
            sp.edit().putBoolean("is_first", true).commit();
//            startActivity(new Intent(this, WelcomeActivity.class));
            startActivity(new Intent(this, GuiDeUitwoActivity.class));
            finish();
        }
    }


}
