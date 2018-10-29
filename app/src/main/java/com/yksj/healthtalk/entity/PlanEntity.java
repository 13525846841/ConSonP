package com.yksj.healthtalk.entity;

/**
 * Created by ${chen} on 2016/12/12.
 * 个人计划中进行中的计划和已完成的计划
 */
public class PlanEntity {
    public String plan_title;
    public String start_time;
    public String plan_cycle;

    public String getPlan_status() {
        return plan_status;
    }

    public void setPlan_status(String plan_status) {
        this.plan_status = plan_status;
    }

    public String plan_status;
    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    private String plan_id;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getPlan_cycle() {
        return plan_cycle;
    }

    public void setPlan_cycle(String plan_cycle) {
        this.plan_cycle = plan_cycle;
    }

    public String getPlan_title() {
        return plan_title;

    }

    public void setPlan_title(String plan_title) {
        this.plan_title = plan_title;
    }
}
