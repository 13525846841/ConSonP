package com.yksj.consultation.son.consultation.avchat.common.framwork;


import com.yksj.consultation.son.R;

/**
 *
 * Created by hzxuwen on 2016/6/16.
 */
public class ToolBarOptions {
    /**
     * toolbar的title资源id
     */
    public int titleId = 0;
    /**
     * toolbar的title
     */
    public String titleString;
    /**
     * toolbar的logo资源id
     */
    public int logoId = R.drawable.launcher_logo;
    /**
     * toolbar的返回按钮资源id，默认开启的资源nim_actionbar_dark_back_icon
     */
    public int navigateId = R.drawable.nim_actionbar_dark_back_icon;
    /**
     * toolbar的返回按钮，默认开启
     */
    public boolean isNeedNavigate = true;
}
