package com.yksj.healthtalk.entity;

import java.io.Serializable;

/**
 * Created by hww on 17/5/11.
 * Used for
 */

public class User implements Serializable {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
