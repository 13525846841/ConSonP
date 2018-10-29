package com.yksj.consultation.son.smallone.bean;


import com.yksj.consultation.son.smallone.TyJsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jack_tang on 16/1/28.
 */
public class TagIndexEntity {
    //每个对象需要有一个主键


    public String menu_class;
    public String class_name;
    public String menu_seq;
    public String used_flag;
    public List<TagMenuEntity> data;

    public static List<TagIndexEntity> parseToList(JSONArray array) {
        List<TagIndexEntity> lists = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                TagIndexEntity e = new TagIndexEntity();
                TyJsonObject object = new TyJsonObject(array.optString(i));
                e.class_name = object.optString("CLASS_NAME");
                e.menu_class = object.optString("MENU_CLASS");
                e.menu_seq = object.optString("QUICK_SEQ");
                e.used_flag = object.optString("USED_FLAG");
                e.data=  TagMenuEntity.parseToList(object.getJSONArray("subFenlei"));
                lists.add(e);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lists;
    }
}
