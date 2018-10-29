package com.yksj.consultation.son.smallone.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.smallone.bean.TagMenuEntity;
import com.yksj.consultation.son.smallone.view.MyGridView;
import com.yksj.consultation.son.smallone.view.WrapContentHeightViewPager;
import com.yksj.consultation.son.views.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 * Created by jack_tang on 16/1/18.
 */
public class TagPageFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private WrapContentHeightViewPager mViewPager;
    public  AdapterView.OnItemClickListener mListener;
    private MyAdapter adapter;
    private CirclePageIndicator mIndicator;

    public void setItemClickListener(AdapterView.OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.tag_page_fragment, null);
        initView(vi);
        return vi;
    }

    private void initView(View vi) {
        mViewPager = ((WrapContentHeightViewPager) vi.findViewById(R.id.pager));
//      mViewPager.setPageTransformer(true, new DepthPageTransformer())
//      mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        mIndicator = (CirclePageIndicator) vi.findViewById(R.id.indicator);
        adapter = new MyAdapter();
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
      //mIndicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        KLog.d("onPageScrolled=position"+(position+1)+"=adapter.pages="+adapter.pages);
//        KLog.d("positionOffset="+positionOffset);
//            if (positionOffset>0.1 && (position+1) ==adapter.pages-1){
//                KLog.d("跳转");
//            }
    }

    @Override
    public void onPageSelected(int position) {
//        KLog.d("------onPageSelected"+"---position=="+position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setData(List<TagMenuEntity> data) {
        MyAdapter ad = new MyAdapter();
        mViewPager.setAdapter(ad);
        ad.onBoundData(data);
    }

    class MyAdapter extends PagerAdapter {
        List<View> views = new ArrayList<>();
        List<TagMenuEntity> datas = new ArrayList<>();
        public int pages = 0;//页数
        int columns = 3;//列数
        int rows = 4;//行数

        public void onBoundData(List<TagMenuEntity> datas) {
            if (datas == null) return;
            this.datas.clear();
            this.datas.addAll(datas);
            calculate();
            notifyDataSetChanged();
        }

        /**
         * 计算页数
         */
        public void calculate() {
            pages = ((datas.size() - 1) / (columns * rows)) + 1;
            if(pages==1){
                mIndicator.setVisibility(View.INVISIBLE);
            }else{
                mIndicator.setVisibility(View.VISIBLE);
            }
            views.clear();
            for (int i = 0; i < pages; i++) {
                if (getActivity()!=null){
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_griview, null);
                    views.add(view);
                }
            }
        }


        @Override
        public int getCount() {
            return pages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            MyGridView mGridView = (MyGridView) v.findViewById(R.id.view);
            if (mListener != null)
                mGridView.setOnItemClickListener(mListener);
            mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            MyGridAdapter adapter = new MyGridAdapter();
            mGridView.setAdapter(adapter);
            int size = columns * rows * position;
//            KLog.d("***一共有" + views.size() + "页面" + datas.size() + "元素");
            if (position == views.size() - 1) {
//                KLog.d("**最后一个**" + size + "====" + (datas.size()));
                adapter.onBoundData(datas.subList(size, datas.size()));
            } else {
                int end = size + columns * rows;
//                KLog.d("****" + size + "====" + end);
                if (end > datas.size()) {
                    adapter.onBoundData(datas.subList(size, datas.size()));
                } else {
                    adapter.onBoundData(datas.subList(size, end));
                }
            }
            if(datas.size()==0){
                mGridView.setVisibility(View.GONE);
                v.findViewById(R.id.tag_default).setVisibility(View.VISIBLE);
            }else{
                mGridView.setVisibility(View.VISIBLE);
                v.findViewById(R.id.tag_default).setVisibility(View.GONE);
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }


    private class MyGridAdapter extends BaseAdapter {

        private List<TagMenuEntity> lists = new ArrayList<TagMenuEntity>();

        public void onBoundData(List<TagMenuEntity> datas) {
            if (datas == null) return;
            lists.clear();
            lists.addAll(datas);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TagMenuEntity e = lists.get(position);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.chat_bottom_tag_layout, null);
            TextView textView = (TextView) view.findViewById(R.id.txt);
            textView.setText(e.menu_name);
            view.setTag(e);
            return view;
        }
    }
}
