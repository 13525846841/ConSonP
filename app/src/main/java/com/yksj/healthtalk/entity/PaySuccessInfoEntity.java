package com.yksj.healthtalk.entity;

import java.io.Serializable;

/**
 * Created by ${chen} on 2017/6/13.
 */
public class PaySuccessInfoEntity extends BaseInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int PAYLOGO ;//门诊订单，咨询订单 区分标示  2000门诊订单 1000图文咨询等

    private String name ="";
    private String id = "";
    private String order_id = "";

    public int getPAYLOGO() {
        return PAYLOGO;
    }

    public void setPAYLOGO(int PAYLOGO) {
        this.PAYLOGO = PAYLOGO;
    }

    private String pay_id = "";


    private static PaySuccessInfoEntity instance = null;

    public static PaySuccessInfoEntity getInstance() {
        if (instance == null) {
            instance = new PaySuccessInfoEntity();
        }
        return instance;
    }

    public PaySuccessInfoEntity() {

    }

    @Override
    public String getName() {
        return name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getPay_id() {
        return pay_id;
    }

    @Override

    public String getId() {
        return id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public void setOrder_id(String order_id) {

        this.order_id = order_id;
    }

    @Override
    public void setId(String id) {

        this.id = id;
    }
}
