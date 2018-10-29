package com.yksj.healthtalk.entity;

/**

 * Created by ${chen} on 2016/12/13.
 * 添加成员的实体类
 *
 */
public class AddMemberEntity {
    public String image;
    public String children_id;
    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String name;
    public String sex;
    public String customer_id;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getChildren_id() {
        return children_id;
    }

    public void setChildren_id(String children_id) {
        this.children_id = children_id;
    }
}
