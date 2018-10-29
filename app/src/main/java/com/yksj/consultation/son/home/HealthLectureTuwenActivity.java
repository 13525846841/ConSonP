package com.yksj.consultation.son.home;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.Rotate3dAnimation;
import com.yksj.healthtalk.entity.HealthLectureWorksEntity;
import com.yksj.healthtalk.utils.TimeUtil;

import org.universalimageloader.core.ImageLoader;

public class HealthLectureTuwenActivity extends Activity implements View.OnClickListener {

    private String tuwenTime;
    private String pic;
    private String title1;
    private String content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_lecture_tuwen);
        initView();
    }

    private void initView() {
        tuwenTime = getIntent().getStringExtra("tuwenTime");
        pic = getIntent().getStringExtra("pic");
        title1 = getIntent().getStringExtra("title");
        content1 = getIntent().getStringExtra("content");
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView topTitle= (TextView) findViewById(R.id.title_lable);
        topTitle.setText("患教文章");
        TextView title= (TextView) findViewById(R.id.title);
        title.setText(title1);
        TextView time= (TextView) findViewById(R.id.time);
        time.setText(TimeUtil.getTimeStr8(tuwenTime));
        ImageView img= (ImageView) findViewById(R.id.img);
        Glide.with(this).load(ImageLoader.getInstance().getDownPathUri(pic)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(img);
        TextView content= (TextView) findViewById(R.id.content);
        content.setText(content1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }
}
