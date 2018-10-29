package com.yksj.consultation.son.smallone.bean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_tang on 16/1/28.
 */
public class TagMenuEntity {

    public String menu_code;


    public String menu_name;
    public String menu_seq;
    public int click_count;
    public String used_flag;

    public static final String MENU_CLASS = "menu_class";

    public String menu_class;


    public static final String IS_CUSTOM = "is_custom";
    public String is_custom = "0";
    public String guide_content;

    /**
     * MENU_CODE: 1,
     * MENU_NAME: "问诊",
     * MENU_SEQ: 1,
     * CLICK_COUNT: 0,
     * USED_FLAG: 1,
     * MENU_CLASS: 2,
     * CONTENT: [
     * {
     * GUIDE_CONTENT: "我要问诊"
     * }
     * ]
     */
    public static List<TagMenuEntity> parseToList(JSONArray array) {
        List<TagMenuEntity> lists = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                TagMenuEntity en = new TagMenuEntity();
                JSONObject json = new JSONObject(array.optString(i));
                en.menu_code = json.optString("QUICK_CODE");
                en.menu_name = json.optString("QUICK_CONTENT");
                en.menu_seq = json.optString("QUICK_SEQ");
                //修改默认的点击次数
                en.click_count = 0;
//                en.click_count = Integer.valueOf(json.optString("CLICK_COUNT"));
                en.used_flag = json.optString("USED_FLAG");
                en.menu_class = json.optString("MENU_CLASS");
                en.guide_content = json.optString("QUICK_CONTENT");
                lists.add(en);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  lists;
    }


//    public static boolean query(String MENU_CODE) {
//        List<TagMenuEntity> lists = LiteOrmDBUtil.getLiteOrm().query(new QueryBuilder<TagMenuEntity>(TagMenuEntity.class).
//                where(TagMenuEntity.IS_CUSTOM + " =?", new String[]{"1"})
//                .whereAppendAnd()
//                .where("menu_code =?", new String[]{MENU_CODE}));
//        if (lists == null || lists.size() == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }
}
