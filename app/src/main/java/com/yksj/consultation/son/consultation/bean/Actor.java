package com.yksj.consultation.son.consultation.bean;

import android.content.Context;

/**
 * Created by ${chen} on 2016/12/1.
 * 优惠卷的实体数据
 */
public class Actor {
    public  String name;
    public  String picName;


    public Actor(String name){
        this.name= name;
    }
    public Actor(String name, String picName){
            this.name = name;
            this.picName = picName;
    }

//    public int getImageResourceId( Context context ){
//        try{
//            return context.getResources().getIdentifier(this.picName, "drawable", context.getPackageName());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return -1;
//        }
//    }

}
