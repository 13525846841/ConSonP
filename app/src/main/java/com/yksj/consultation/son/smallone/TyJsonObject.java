package com.yksj.consultation.son.smallone;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack_tang on 15/12/13.
 */
public class TyJsonObject extends JSONObject {
    public TyJsonObject(String data) throws JSONException {
        super(data);
    }

    /**
     * null 处理
     *
     * @param name
     * @return
     */
    @Override
    public String optString(String name) {
        if (super.isNull(name))
            return "";
        else
            return super.optString(name);
    }

    @Override
    public TyJsonArray optJSONArray(String name) {
        Object object = super.opt(name);
        return object instanceof TyJsonArray ? (TyJsonArray) object : null;
    }

    /**
     * TyJsonArray  转化 list
     *
     * @param name
     * @return
     */
    public List<TyJsonObject> optListObj(String name) {
        TyJsonArray array = optJSONArray(name);
        List<TyJsonObject> lists = new ArrayList<TyJsonObject>();
        for (int i = 0; i < array.length(); i++) {
            lists.add(array.optJSONObject(i));
        }
        return lists;
    }
}
