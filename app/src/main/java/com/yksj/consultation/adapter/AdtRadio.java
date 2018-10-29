package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yksj.healthtalk.views.ChoiceListItemView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HEKL on 15/10/9.
 * User for 多选
 */
public class AdtRadio extends BaseAdapter {
    private ArrayList<HashMap<String,String>> list;
    private Context c;

    public AdtRadio(Context c,ArrayList<HashMap<String,String>> list) {
        super();
        this.c = c;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        //返回每一条Item的Id
        return arg0;
    }

    @Override
    public boolean hasStableIds() {
        //getCheckedItemIds()方法要求此处返回为真
        return true;
    }
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ChoiceListItemView choiceListItemView = new ChoiceListItemView(c, null);
        choiceListItemView.setName(list.get(arg0).get("NAME"));
        return choiceListItemView;
    }

}
