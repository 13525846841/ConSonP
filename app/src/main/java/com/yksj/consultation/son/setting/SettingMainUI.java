package com.yksj.consultation.son.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ViewFinder;

import org.universalimageloader.core.ImageLoader;

public class SettingMainUI extends RootFragment implements OnClickListener {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_layout_main, null);
        v = view;
        initUI();
        return view;
    }

    private void initUI() {
        initTitleView(v);
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText(getString(R.string.setting));
        ViewFinder finder = new ViewFinder(v);
        int[] ids = new int[]{R.id.setting_password, R.id.setting_feedback, R.id.setting_clear_cache,
                R.id.setting_update};

        finder.onClick(this, ids);
//        mPrivateChat.setChecked(getActivity().getSharedPreferences(SettingManager.KEY_PRIVATE_CHAT));
//        mPrivateChat.setChecked(manager.isPrivateChat());
        View mClearCacheV = getActivity().getLayoutInflater().inflate(R.layout.setting_leave, null);
        mClearCacheV.findViewById(R.id.setting_leave_qingchuhuancun).setOnClickListener(this);
        mClearCacheV.findViewById(R.id.setting_leave_quxiao).setOnClickListener(this);

    }

    private PopupWindow pop;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed(v);
                break;
            case R.id.setting_password:// 设定密码
                intent = new Intent(getActivity(), SettingPassWordUI.class);
                startActivity(intent);
                break;
            case R.id.setting_update:// 关于六一健康
                intent = new Intent(getActivity(), SettingAboutHealthActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback:// 意见反馈
                intent = new Intent(getActivity(), SettingFeedbackUI.class);
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:// 清理缓存
                DoubleBtnFragmentDialog.showDefault(getFragmentManager(), "是否清理缓存", "取消", "确定", new OnDilaogClickListener() {

                    @Override
                    public void onDismiss(DialogFragment fragment) {
                        fragment.dismiss();
                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        clearAllCache();
                    }
                });

                break;

            case R.id.setting_leave_qingchuhuancun:// 清除缓存的pop
                clearAllCache();
                pop.dismiss();
                break;
            case R.id.setting_leave_quxiao:
                if (pop != null && pop.isShowing())
                    pop.dismiss();
                break;
        }

    }

    /**
     * 清理缓存
     */
    private void clearAllCache() {
        new AsyncTask<Void, Void, Void>() {
            private LodingFragmentDialog showLodingDialog;

            @Override
            protected void onPreExecute() {
                showLodingDialog = LodingFragmentDialog.showLodingDialog(getFragmentManager(), getResources());
            }

            @Override
            protected Void doInBackground(Void... params) {
                ImageLoader.getInstance().clearDiscCache();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                showLodingDialog.dismissAllowingStateLoss();
                ToastUtil.showToastPanl("清理完毕");
            }
        }.execute();
    }
}
