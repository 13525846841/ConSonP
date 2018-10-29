package com.netease.neliveplayer.playerkit.sdk.view;

import android.content.Context;
import android.util.AttributeSet;

import com.netease.neliveplayer.playerkit.core.view.BaseSingleTextureView;


/**
 * @author netease
 * 单一TextureView
 * 适用于播放页面只有一个TextureView时，支持后台播放
 *
 */

public class AdvanceSingleTextureView extends BaseSingleTextureView {
    public AdvanceSingleTextureView(Context context) {
        super(context);
    }

    public AdvanceSingleTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvanceSingleTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
