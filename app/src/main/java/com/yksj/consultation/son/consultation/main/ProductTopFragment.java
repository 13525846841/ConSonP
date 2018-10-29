package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.CustomScrollDurationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/8/10.
 */
public class ProductTopFragment extends RootFragment implements ViewPager.OnPageChangeListener {

    private int CHATTINGCODE = 2;
    private int item = 0;//Viewpager的被选中项
    private TabsPagerAdapter adapter;
    View mView;
    private int current = 0;
    private ViewPager mViewPager;
    private List<JSONObject> list = null;
    private final int TIME = 3000;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1000:
                    current = mViewPager.getCurrentItem();
                    current++;
                    mViewPager.setCurrentItem(current, true);
                    mHandler.sendEmptyMessageDelayed(1000, TIME);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.product_top_frag, null);

        initView(mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


            JSONObject jsonobject = null;

            try {
                for (int i = 0; i < pictureArray.length(); i++) {
                    jsonobject = pictureArray.getJSONObject(i);
                    list.add(jsonobject);
                }
                adapter.onBoundData(list);
                mHandler.sendEmptyMessageDelayed(1000, TIME);
            } catch (JSONException e) {
                e.printStackTrace();
            }





//        Map<String,String> map=new HashMap<>();
//        map.put("op", "findVisitingPersonByCustomer");
//        HttpRestClient.OKHttPersonSeek(map, new HResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//            }
//            @Override
//            public void onResponse(String content) {
//                try {
//                    JSONObject obj = new JSONObject(content);
//                    list = new ArrayList<>();
//                    if ("1".equals(obj.optString("code"))){
//
//                        if (!HStringUtil.isEmpty(obj.optString("result"))){
//                            JSONArray array = obj.optJSONArray("result");
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject jsonobject = array.getJSONObject(i);
//                                list.add(jsonobject);
//                            }
//                            adapter.onBoundData(list);
//                            mHandler.sendEmptyMessageDelayed(1000, TIME);
//
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        },this);
    }

    private JSONArray pictureArray = null;
    public void updataUI(JSONArray picArray){
        pictureArray = picArray;
    }


    private void initView(View mView) {
        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        adapter = new TabsPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);

  		/*主要代码段  限制viewpage 滑动速度*/
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            //设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
            CustomScrollDurationView mScroller = new CustomScrollDurationView(mViewPager.getContext(), new AccelerateInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int size = adapter.datas.size();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TabsPagerAdapter extends PagerAdapter {
        public TabsPagerAdapter() {
            super();
            instance = ImageLoader.getInstance();
        }

        final List<JSONObject> datas = new ArrayList<JSONObject>();
        private ImageLoader instance;

        public void onBoundData(List<JSONObject> list) {
            datas.clear();
            datas.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.d("tag+++++++++++=========",String.valueOf(position));
            if (datas.size() > 0) {
                int location = position % datas.size();
                final ImageView view = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.main_viewpage_image,
                        null);
                container.addView(view);
                try {
                    final String url = datas.get(location).getString("BIG_PICTURE");
                    instance.displayImage(url, view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } catch (Exception e) {
                }
                return view;
            } else {
                return null;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
