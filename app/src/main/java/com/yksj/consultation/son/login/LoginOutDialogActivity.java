package com.yksj.consultation.son.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;

/**
 * 异地登陆的Activity
 * Created by lmk on 2015/8/20.
 */
public class LoginOutDialogActivity extends FragmentActivity implements View.OnClickListener {

    public final static String CONTENT = "content";
    RelativeLayout layout;
    TextView tvContent;
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login_out_dialog);
        setFinishOnTouchOutside(false);
        tvContent = (TextView) findViewById(R.id.dialog_text);
        layout = (RelativeLayout) findViewById(R.id.login_out_layout);
        if (getIntent().hasExtra(CONTENT)) {
            content = getIntent().getStringExtra(CONTENT);
            tvContent.setText(content);
        }
        Button button = (Button) findViewById(R.id.dialog_btn);
        button.setOnClickListener(this);
        layout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LoginOutDialogActivity.this, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginOutDialogActivity.this, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
