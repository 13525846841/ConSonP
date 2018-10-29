/*
 * 文 件 名:  ViewfinderResultPointCallback.java
 * 版    权:  ISOFTSTONE GROUP AND RELATED PARTIES. ALL RIGHTS RESERVED.
 * 描    述:  <文件主要内容描述>
 * 作    者:  ddliu
 * 创建时间:  2012-6-13 下午05:02:33
 */
package com.yksj.healthtalk.views;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/**
 * 扫描回调对象
 * @author  ddliu
 * @version  [1.0.0.0, 2012-6-13]
 */
public final class ViewfinderResultPointCallback implements ResultPointCallback {

    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
      this.viewfinderView = viewfinderView;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
      viewfinderView.addPossibleResultPoint(point);
    }

  }