package com.yksj.consultation.son.consultation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.order.DoctorOrderListFragment;

import java.util.ArrayList;

/**
 * 预约列表
 *
 * @author zheng
 */
@SuppressLint("ValidFragment")
public class PDoctorServiceFragment extends RootFragment implements OnPageChangeListener, android.widget.RadioGroup.OnCheckedChangeListener {
    private Context context;
    private ViewPager mPager;
    private RadioGroup mGroup;

    public PDoctorServiceFragment(Context context) {
        super();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(context).inflate(R.layout.doctor_service_fragment, container, false);
        mGroup = (RadioGroup) view.findViewById(R.id.radio_group1);
        mGroup.setOnCheckedChangeListener(this);
        mPager = (ViewPager) view.findViewById(R.id.viewpager1);
        BaseListPagerAdpater mAdpater = new BaseListPagerAdpater(getChildFragmentManager());
        mPager.setAdapter(mAdpater);
        mPager.setOnPageChangeListener(this);
        ArrayList<Fragment> mlList = new ArrayList<Fragment>();


        /**
         * FindMyDoctorDetails32 患者端 我的订单
         * 列表序号 0-服务中 1-待服务  3-已完成 4-待支付(待支付)  6-待退款  8-全部
         */
        //已预约
        Fragment allFragment = new DoctorOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "0");
        allFragment.setArguments(bundle);
        mlList.add(allFragment);
        //历史
        Fragment pendingPay = new DoctorOrderListFragment();
        Bundle pp = new Bundle();
        pp.putString("type", "1");
        pendingPay.setArguments(pp);
        mlList.add(pendingPay);


        mAdpater.onBoundFragment(mlList);
        mPager.setCurrentItem(0, false);
        return view;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        RadioButton mButton = (RadioButton) mGroup.getChildAt(arg0);
        mButton.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.isChecked()) mPager.setCurrentItem(i, false);
        }
    }
}
