package com.yksj.consultation.son.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hekl on 18/10/24.
 */

public class HomeBannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<String> list;
    private List<View> imageViewList;
    private Context context;

    public void setImgUrlList(List<String> list) {
        this.list = list;
        startCarousel();
    }

    public HomeBannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public HomeBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeBannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        context = getContext();
    }

    private void startCarousel(){
        imageViewList=new ArrayList<>();
        ViewPager viewPager = new ViewPager(context);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(context);
                Glide.with(context).load(list.get(position)).centerCrop().placeholder(R.drawable.waterfall_default)
                        .error(R.drawable.waterfall_default).into(imageView);
                container.addView(imageView);
                return imageView;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((ImageView)object);
            }
        });
        viewPager.addOnPageChangeListener(this);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER);

        for (int i = 0; i < list.size(); i++) {
            View tipView = new View(context);

            LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgLp.width=300;
            imgLp.height=100;
            tipView.setLayoutParams(imgLp);
            imageViewList.add(tipView);
            if (i==0)
                tipView.setBackgroundResource(R.drawable.shape_patient_home_indicator_select);
            else
                tipView.setBackgroundResource(R.drawable.shape_patient_home_indicator_unselect);

            linearLayout.addView(tipView);
        }
        LayoutParams thisLp = new LayoutParams(getLayoutParams());
        thisLp.gravity=Gravity.BOTTOM;
        thisLp.bottomMargin=50;
        linearLayout.setLayoutParams(thisLp);
        addView(viewPager);
        addView(linearLayout);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i==position){
                imageViewList.get(position).setBackgroundResource(R.drawable.shape_patient_home_indicator_select);
            }else {
                imageViewList.get(i).setBackgroundResource(R.drawable.shape_patient_home_indicator_unselect);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
