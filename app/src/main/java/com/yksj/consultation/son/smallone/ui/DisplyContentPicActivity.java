package com.yksj.consultation.son.smallone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.CommonAdapter;
import com.yksj.consultation.comm.CommonViewHolder;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.net.http.HttpUrls;

import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.List;


/**
 * Created by jack_tang on 15/8/26.
 * 显示症状图片的页面
 */
public class DisplyContentPicActivity extends BaseFragmentActivity {

    private String[] path;//图片路径
    private List<String> mPaths;
    public  static String PATHS = "path";
    public  static String TITLE_NAME = "title_name";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_displty_pic);
        initView();
    }

    private void initView() {
        initTitle();
        setTitle("图片展示");
        titleLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        path = getIntent().getStringExtra(PATHS).split(",");
        GridView listView = (GridView) findViewById(R.id.list);
        MyAdapter myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        mPaths = Arrays.asList(path);
        myAdapter.onBoundData(mPaths);
        if(getIntent().hasExtra(TITLE_NAME)){
            titleTextV.setText(getIntent().getStringExtra(TITLE_NAME));
        }
    }


    class MyAdapter extends CommonAdapter<String> {

        private final ImageLoader mImageLoader;
        DisplayImageOptions galleryDisplayImageOptions;
        HttpUrls httpUrls;

        public MyAdapter(Context context) {
            super(context);
            mImageLoader = ImageLoader.getInstance();
            httpUrls = HTalkApplication.getHttpUrls();
//            galleryDisplayImageOptions = HTalkApplication.createGalleryDisplayImageOptions();
        }

        @Override
        public void onBoundView(CommonViewHolder helper, final String item) {
            ImageView draweeView = helper.getView(R.id.news_item_image);
//            mImageLoader.displayImage(httpUrls.LOAD_PIC_DISPLAY+item, draweeView, galleryDisplayImageOptions);
            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DisplyContentPicActivity.this, ImageGalleryActivity.class);
                    intent.putExtra(ImageGalleryActivity.URLS_KEY, mPaths.toArray());
                    intent.putExtra(ImageGalleryActivity.TYPE_KEY, 1);//0,1单个,多个
                    intent.putExtra(ImageGalleryActivity.POSITION, mPaths.indexOf(item));//选中第几个
                    startActivity(intent);
                }
            });
        }

        @Override
        public int viewLayout() {
            return R.layout.pic_item;
        }
    }

}
