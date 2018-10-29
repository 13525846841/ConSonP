package com.yksj.consultation.son.smallone;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by jack_tang on 15/12/13.
 */
public class TyJsonArray extends JSONArray {
    public TyJsonArray(String data) throws JSONException {
        super(data);
    }

    @Override
    public Object opt(int index) {
        return super.opt(index);
    }


    @Override
    public TyJsonObject optJSONObject(int index) {
        Object object = super.opt(index);
        return object instanceof TyJsonObject ? (TyJsonObject) object : null;
    }
}
