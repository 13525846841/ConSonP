package com.yksj.consultation.son.dossier;

/**
 * 病历项的实体
 * 用于专家助理创建病历时的数据适配以及病历上传
 * Created by lmk on 2015/7/13.
 *
 * "INFO"
 * "INFO2"
 * "SELECTION"
 *
 * "ITEMID": 193,
 "ITEMNAME": "Babinski sign",
 "ITEMTYPE": 20,
 "SPIC": 0,
 "CLASSID": 25,
 "CLASSNAME": "病理反射",
 "ITEMDESC": null,
 "NEFILL": 0,
 "PICNUM": 0,
 "NUMSTART": 0,
 "NUMEND": 0,
 "SEQ": 93,
 "OPTION": [
 */
public class CaseItemEntity {
    public String INFO;
    public String INFO2;
    public String SELECTION;
    public int ITEMID;
    public String ITEMNAME;
    public int ITEMTYPE;
    public int SPIC;
    public int CLASSID;
    public String CLASSNAME;
    public String ITEMDESC;
    public int NEFILL;
    public int PICNUM;
    public int NUMSTART;
    public int NUMEND;
    public int SEQ;
    public String OPTION;
    public boolean isChecked=false;//此项是否被选中
    public boolean isEdited=false;//用户是否编辑过
    private String sortLetters;  //显示数据拼音的首字母

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
