/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yksj.consultation.son.views;

import android.support.v4.view.ViewPager;

/**
 * page指示器基类
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {
    /**
     * 绑定到 ViewPager.
     * @param view
     */
    void setViewPager(ViewPager view);

    /**
     * 绑定到 ViewPager.
     * @param view
     * @param initialPosition 初始化的索引位置
     */
    void setViewPager(ViewPager view, int initialPosition);

    /**
     * 设置当前的显示位置
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * 
     * @param listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    /**
     * 通知指示器改变
     */
    void notifyDataSetChanged();
}
