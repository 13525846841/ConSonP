package com.yksj.healthtalk.permission;

/**
 * Created by hww on 17/11/20.
 */

public interface Rationale extends Cancelable {

    /**
     * Go request permission.
     */
    void resume();

}
