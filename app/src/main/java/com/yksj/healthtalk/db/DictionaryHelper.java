package com.yksj.healthtalk.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.PingYinUtil;
import com.yksj.healthtalk.utils.StringFormatUtils;

import static com.baidu.location.b.g.i;

/**
 * 对程序本身自带的数据库进行操作
 *
 * @author zhao yuan
 * @version 3.0
 */
public class DictionaryHelper {
    private static DictionaryHelper dictionaryHelper;
    private DictionaryDatabase mDatabase;
    public static final String DB_DICTIONARY_NAME = "dictionary.";
    // 虚拟医生消息
    public static final String TABLE_VIRTUAL_DOCTOR_MESSAGE = "virtual_doctor_message";
    // 新闻收藏
    public static final String TABLE_NEWS_COllECTION = "news_collection";

    private DictionaryHelper(Context context) {
        mDatabase = new DictionaryDatabase(context.getApplicationContext());
    }

    /**
     * 获得对象实例
     *
     * @param context
     * @return
     */
    public static synchronized DictionaryHelper getInstance(Context context) {
        if (dictionaryHelper == null) {
            dictionaryHelper = new DictionaryHelper(context);
        }
        return dictionaryHelper;
    }

    /**
     * 关闭数据库,只在后台服务销毁的时候进行调用,其他地方无需关闭数据库
     */
    public synchronized void close() {
        if (dictionaryHelper != null) {
            mDatabase.closeDictionDb();
            dictionaryHelper = null;
            mDatabase = null;
        }
    }

    /**
     * 查询医生职称
     *
     * @return
     */
    public LinkedHashMap<String, String> querydoctorTitles(Context context) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Cursor cursor = null;
        try {
            cursor = DictionaryDatabase.LinkedHashDoctor();
            while (cursor.moveToNext()) {
                map.put(cursor.getString(0), cursor.getString(1));
            }
        } catch (SQLException e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    /**
     * 查询医生职称2
     *
     * @return
     */
    public List<Map<String, String>> querydoctorTitle(Context context) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Cursor cursor = null;
        try {
            cursor = DictionaryDatabase.LinkedHashDoctor();
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", cursor.getString(0));
                map.put("code", cursor.getString(1));
                list.add(map);
            }
        } catch (SQLException e) {
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 查询城市数据信息
     *
     * @param context
     */
    public List<Map<String, String>> queryCity(Context context,
                                               String subCode) {
        String sql;
        if (subCode == null) {
            sql = "select area_code,area_name from SMS_AREA_DEFINE where sub_area_code is null order by area_code asc";
        } else {
            sql = "select area_code,area_name from SMS_AREA_DEFINE where sub_area_code = ? order by area_code asc";
        }
        return queryData(context, sql, subCode);
    }

    /**
     * 查询环境城市数据信息
     *
     * @param context
     */
    public List<Map<String, String>> queryEnvironmentCity(
            Context context, String subCode) {
        String sql;
        if (subCode == null) {
            sql = "select cityid,cityname from CITYLIST where proid is null order by id asc";
        } else {
            sql = "select cityid,cityname from CITYLIST where proid = ? order by id asc";
        }
        return queryData(context, sql, subCode);
    }

    /**
     * 封装城市数据信息
     *
     * @param proList 省级集合（第一级）
     * @param cityMap 省辖市集合（第二级）
     */
    public List<Map<String, String>> setCityData(Context context,
                                                 Map<String, List<Map<String, String>>> map) {
        List<Map<String, String>> proList = queryCity(context, "0");

        for (int i = 0; i < proList.size(); i++) {
            Map<String, String> hash = proList.get(i);
            List<Map<String, String>> list = queryCity(context,
                    hash.get(Tables.TableCity.CODE));
            Map<String, String> has = new HashMap<>();
            has.put(Tables.TableCity.CODE, "");
            has.put(Tables.TableCity.NAME, "全部");
            list.add(0, has);
            map.put(hash.get(Tables.TableCity.NAME), list);
        }

        //省级
        Map<String, String> has = new HashMap<>();
        has.put(Tables.TableCity.CODE, "");
        has.put(Tables.TableCity.NAME, "全部");
        proList.add(0, has);

        //市级，地区级
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", "全部");
        hashMap.put("code", "");
        List<Map<String, String>> mList = new ArrayList<>();
        mList.add(hashMap);
        map.put(proList.get(0).get(Tables.TableCity.NAME), mList);
        return proList;
    }

    /**
     * 环境城市信息
     *
     * @param context
     * @param map
     * @return
     */
    public List<Map<String, String>> setEnviroCityData(
            Context context,
            Map<String, List<Map<String, String>>> map) {
        List<Map<String, String>> proList = queryEnvironmentCity(
                context, "0");
        for (int i = 0; i < proList.size(); i++) {
            Map<String, String> hash = proList.get(i);
            List<Map<String, String>> list = queryEnvironmentCity(
                    context, hash.get(Tables.TableCity.CODE));
            map.put(hash.get(Tables.TableCity.NAME), list);
        }
        return proList;
    }

    /**
     * 封装城市数据信息,用以再次查询每个城市名
     *
     * @param proList 省级集合（第一级）
     * @param cityMap 省辖市集合（第二级）
     */
    public List<Map<String, String>> getCityData(Context context,
                                                 List<List<Map<String, String>>> mylist) {
        List<Map<String, String>> proList = queryCity(context, null);
        for (int i = 0; i < proList.size(); i++) {
            Map<String, String> hash = proList.get(i);
            List<Map<String, String>> list = queryCity(context,
                    hash.get(Tables.TableCity.CODE));
            mylist.add(list);
        }
        return proList;
    }

    /**
     * 根据城市编码查询城市
     *
     * @param context
     * @param area_code 城市编码
     * @return
     */
    public String queryAddress(Context context, String area_code) {
        String sql = "select AREA_NAME from SMS_AREA_DEFINE where AREA_CODE=?";
        Cursor cursor = null;
        String address = null;
        try {
            DictionaryDatabase.reQuery(sql, new String[]{area_code});
            address = area_code;
            if (cursor.moveToFirst()) {
                address = cursor.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return address;
    }

    /**
     * 查询信息层面数据
     *
     * @param context
     * @param args
     */
    public String queryInFolayName(Context context, String args) {
        String value = "";
        Cursor cur = null;
        try {

            String sql = "select UPPER_LAY_ID,INFO_LAY_NAME from information_lay_define where INFO_LAY_ID = ?";
            cur = DictionaryDatabase.reQuery(sql, new String[]{args});
            if (cur.moveToFirst()) {
                String name = cur.getString(1);
                String code = cur.getString(0);
                cur = DictionaryDatabase.reQuery(sql, new String[]{code});
                if (cur.moveToFirst())
                    value = cur.getString(1) + "-" + name;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return value;
    }

    /**
     * 查询信息层面数据
     *
     * @param context
     * @param args
     */
    public HashMap<String, String> queryInFolayInfo(Context context, String args) {
        String sql = "select fatherid,name from information_lay_define where id =?";
        Cursor cursor = null;
        HashMap<String, String> hash = null;
        try {
            if (args != null) {
                cursor = DictionaryDatabase.reQuery(sql, new String[]{args});
            } else {
                cursor = DictionaryDatabase.reQuery(sql, null);
            }

            while (cursor.moveToNext()) {
                hash = new HashMap<String, String>();
                hash.put(Tables.TableCity.CODE, cursor.getString(0));
                hash.put(Tables.TableCity.NAME, cursor.getString(1));
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return hash;
    }

    private List<Map<String, String>> queryData(Context context,
                                                String sql, String args) {
        Cursor cursor = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            if (args != null) {
                cursor = DictionaryDatabase.reQuery(sql, new String[]{args});
            } else {
                cursor = DictionaryDatabase.reQuery(sql, null);
            }

            while (cursor.moveToNext()) {
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put(Tables.TableCity.CODE, cursor.getString(0));
                hash.put(Tables.TableCity.NAME, cursor.getString(1));
                list.add(hash);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 收藏新闻
     *
     * @param title            标题
     * @param time             时间
     * @param content          新闻内容
     * @param connectionUserId 用户id
     * @param typeCode         内容类型id
     * @param contentId        内容id
     * @return
     */
    public boolean saveNewsConnection(Context context, String type_id,
                                      String title, String time, String content, String connectionUserId,
                                      String typeCode, String contentId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.TableNewsCollection.TYPE_ID, type_id);
        contentValues.put(Tables.TableNewsCollection.NEWS_TITLE, title);
        contentValues.put(Tables.TableNewsCollection.NEWS_CONTENT, content);
        contentValues.put(Tables.TableNewsCollection.CONNECTION_USERID,
                connectionUserId);
        contentValues.put(Tables.TableNewsCollection.NEWS_TIME, time);
        contentValues.put(Tables.TableNewsCollection.TYPE_CODE, typeCode);
        contentValues.put(Tables.TableNewsCollection.CONTENT_ID, contentId);
        int i = queryNewsConnectionAmount(context, typeCode, contentId);
        if (i > 0)
            return false;
        long id = DictionaryDatabase.insertData(
                Tables.TableNewsCollection.TABL_NAME_NEWS_CONNECTION, null,
                contentValues);
        return true;
    }

    /**
     * 根据类型和内容id查询当前内容是否已经收藏
     *
     * @param typeCode
     * @param contentId
     * @return
     */
    public int queryNewsConnectionAmount(Context context, String typeCode,
                                         String contentId) {
        Cursor cursor = null;
        try {
            String[] columns = new String[]{Tables.TableNewsCollection.NEWS_ID};
            String selection = Tables.TableNewsCollection.TYPE_CODE
                    + "= ? and " + Tables.TableNewsCollection.CONTENT_ID + "=?";
            String[] selectionArgs = new String[]{typeCode, contentId};
            cursor = DictionaryDatabase.QueryData(
                    Tables.TableNewsCollection.TABL_NAME_NEWS_CONNECTION,
                    columns, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                return cursor.getCount();
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * 查询用户的所有新闻收藏
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryNewConnections(Context context,
                                                         String userId, String type_id) {
        List<Map<String, Object>> listMaps = null;
        Cursor cursor = null;
        try {
            listMaps = new ArrayList<Map<String, Object>>();
            String[] columns = new String[]{
                    Tables.TableNewsCollection.NEWS_ID,
                    Tables.TableNewsCollection.NEWS_TITLE,
                    Tables.TableNewsCollection.NEWS_TIME};
            String selection = Tables.TableNewsCollection.CONNECTION_USERID
                    + " = ? and " + Tables.TableNewsCollection.TYPE_ID + " = ?";
            String[] selectionArgs = new String[]{userId, type_id};
            cursor = DictionaryDatabase.QueryData(
                    Tables.TableNewsCollection.TABL_NAME_NEWS_CONNECTION,
                    columns, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // 新闻id
                        String newsId = cursor.getString(0);
                        // 新闻标题
                        String newsTitle = cursor.getString(1);
                        // 新闻时间
                        String newsTime = cursor.getString(2);

                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Tables.TableNewsCollection.NEWSTITLE, newsTitle);
                        map.put(Tables.TableNewsCollection.NEWSTIME,
                                StringFormatUtils.getTimeStr(newsTime));
                        map.put(Tables.TableNewsCollection.NEWSID, newsId);
                        listMaps.add(map);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return listMaps;
    }

    /**
     * 根据收藏的新闻id查询新闻收藏内容
     *
     * @param newsId 收藏的新闻id
     * @return List<String> <newsId,title,content,time>
     */
    public List<String> queryNewsContentByNewsId(Context context, String newsId) {
        List<String> list = null;
        Cursor cursor = null;
        try {
            list = new ArrayList<String>();
            String[] columns = new String[]{
                    Tables.TableNewsCollection.NEWS_ID,
                    Tables.TableNewsCollection.NEWS_TITLE,
                    Tables.TableNewsCollection.NEWS_CONTENT,
                    Tables.TableNewsCollection.NEWS_TIME};
            String selection = Tables.TableNewsCollection.NEWS_ID + " = ?";
            String[] selectionArgs = new String[]{newsId};
            cursor = DictionaryDatabase.QueryData(
                    Tables.TableNewsCollection.TABL_NAME_NEWS_CONNECTION,
                    columns, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // 新闻标题
                        String newsTitle = cursor.getString(1);
                        // 新闻内容
                        String newsContent = cursor.getString(2);
                        // 新闻时间
                        String newsTime = cursor.getString(3);
                        list.add(newsId);
                        list.add(newsTitle);
                        list.add(newsContent);
                        list.add(newsTime);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 查知识页面 根据返回的id查询网站信息
     */
    public HashMap<String, ArrayList<HashMap<String, Object>>> queryKnowledgeNetwork(
            Context context, String id) {
        Cursor cursor = null;
        HashMap<String, ArrayList<HashMap<String, Object>>> data = new HashMap<String, ArrayList<HashMap<String, Object>>>();
        try {
            cursor = DictionaryDatabase.reQuery(
                    "select name,web from Knowledge_Web where id = ?",
                    new String[]{id});
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String name = cursor.getString(0);
                String firstChar = PingYinUtil.getPingYin(context,
                        name.substring(0, 1));
                map.put("zname", name);
                map.put("network", cursor.getString(1));
                map.put("sort_key", firstChar);
                if (data.containsKey(firstChar)) {
                    data.get(firstChar).add(map);
                } else {
                    ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
                    arr.add(map);
                    data.put(firstChar, arr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 查询医院类型
     */
    public HashMap<String, String> queryHosType(Context context, String zid) {
        Cursor cursor = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String sql = "select MENU_NAME,PARAMETERS_VALUE from SMS_NEARBY_MENU_DEFINE where SUB_MENU = ?";
            cursor = DictionaryDatabase.reQuery(sql, new String[]{zid});
            while (cursor.moveToNext()) {
                String id = cursor.getString(1);
                String name = cursor.getString(0);
                if (id.equals("1"))
                    continue;
                map.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    /**
     * 查询医师职称
     */
    public HashMap<String, String> queryDoctorTitle(Context context,
                                                    ArrayList<String> wheelData) {
        Cursor cursor = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            cursor = DictionaryDatabase
                    .reQuery(
                            "select TITLE_NAME,TITLE_CODE from SMS_Title_Define order by TITLE_CODE asc",
                            null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(1);
                String name = cursor.getString(0);
                wheelData.add(name);
                map.put(name, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    /**
     * 部位 信息
     *
     * @param proList （第一级）
     * @param cityMap （第二级）
     */
    public List<Map<String, String>> getPartData(Context context,
                                                 String id, Map<String, List<Map<String, String>>> map) {
        List<Map<String, String>> proList = queryKnowledge(context, id);
        for (int i = 0; i < proList.size(); i++) {
            Map<String, String> hash = proList.get(i);
            List<Map<String, String>> list = queryKnowledge(context,
                    hash.get("code"));
            map.put(hash.get(Tables.ChatTable.NAME), list);
        }
        return proList;
    }

    /**
     * 查知识页面 根据id查询信息 单滚轮
     */
    public List<Map<String, String>> queryKnowledge(Context context,
                                                    String zid) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Cursor cursor = null;
        try {
            cursor = DictionaryDatabase
                    .reQuery(
                            "select menu_id,menu_name,proviso,ICON_ADDR,GET_CONTENT_TYPE from SMS_Client_Menu_Define where UPPER_MENU_ID = ?",
                            new String[]{zid});
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("zfatherid", zid);
                map.put(Tables.ChatTable.NAME, name);
                map.put("code", id);
                map.put("type", cursor.getString(4));
                map.put("extra", cursor.getString(2));
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    // 返回地区编号
    public ArrayList<HashMap<String, Object>> queryArea(Context context,
                                                        String code, String SUB_AREA_CODE) {
        Cursor cursor = null;
        ArrayList<HashMap<String, Object>> data = null;
        try {
            data = new ArrayList<HashMap<String, Object>>();
            if ("1".equals(code)) {
                cursor = DictionaryDatabase
                        .reQuery(
                                "SELECT AREA_CODE,AREA_NAME,VALID_FLAG from SMS_area_Define WHERE VALID_FLAG =1 and AREA_CODE%? = 0 and SUB_AREA_CODE = ?",
                                new String[]{code, SUB_AREA_CODE});
            } else {
                cursor = DictionaryDatabase
                        .reQuery(
                                "SELECT AREA_CODE,AREA_NAME,VALID_FLAG from SMS_area_Define WHERE VALID_FLAG =1 and AREA_CODE%? = 0",
                                new String[]{code});
            }
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("AREA_CODE", cursor.getString(0));
                map.put("zname", cursor.getString(1));
                map.put("VALID_FLAG", cursor.getString(2));
                map.put("icon", R.drawable.arrows);
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 健康树
     *
     * @param context
     * @param flag    2 挂号 3 体检 4 购药
     * @return
     */
    public List<Map<String, String>> queryTree(Context context,
                                               Map<String, List<Map<String, String>>> data,
                                               String flag) {

        Cursor cursor = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        String address = "";
        try {
            cursor = DictionaryDatabase
                    .reQuery(
                            "select address,title,site from ServerTree where aflag = ?",
                            new String[]{flag});
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                address = cursor.getString(0);
                map.put("title", cursor.getString(1));
                map.put("network", cursor.getString(2));
                if (Tables.TableHealthTree.FLAG_TIJIAN.equals(flag)) {
                    if (data.containsKey(address))
                        data.get(address).add(map);
                    else {
                        List<Map<String, String>> arr = new ArrayList<Map<String, String>>();
                        arr.add(map);
                        data.put(address, arr);
                    }
                } else
                    list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 健康树
     */
    /*
     * public ArrayList<HashMap<String, Object>> queryTreeMult( Context context,
	 * String id) { Cursor cursor = null; ArrayList<HashMap<String, Object>>
	 * list = new ArrayList<HashMap<String, Object>>(); try { cursor =
	 * DictionaryDatabase.reQuery(
	 * "select title,network from tree_network where MENU_LEVEL= ?", new
	 * String[] { id }); while (cursor.moveToNext()) { HashMap<String, Object>
	 * map = new HashMap<String, Object>(); map.put("title",
	 * cursor.getString(0)); map.put("network", cursor.getString(1));
	 * list.add(map); } } catch (Exception e) { e.printStackTrace(); } finally{
	 * if(cursor!=null&& cursor.isClosed()){ cursor.close(); } } return list; }
	 */

    // 返回美容theme
    public ArrayList<HashMap<String, Object>> queryTheme(Context context,
                                                         String INFO_LAY_ID) {
        Cursor cursor = null;
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try {
            cursor = DictionaryDatabase.QueryData("sms_test_theme",
                    new String[]{"theme_id", "theme_title", "theme_desc",
                            "theme_type"}, "INFO_LAY_ID = ?",
                    new String[]{INFO_LAY_ID}, null, null, null);
            int i = 1;
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("theme_id", cursor.getString(0));
                map.put("itemText", cursor.getString(1));
                map.put("theme_desc",
                        ("".equals(cursor.getString(2)) ? cursor.getString(1)
                                : cursor.getString(2)));
                map.put("theme_type", cursor.getString(3));
                map.put("icon", R.drawable.arrows);
                i++;
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    // 返回美容Object
    public ArrayList<HashMap<String, Object>> queryObject(Context context,
                                                          String themeID) {
        Cursor cursor = null;
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try {
            cursor = DictionaryDatabase
                    .QueryData("sms_test_subject", new String[]{"subject_id",
                                    "subject_content", "THEME_ID"}, "theme_ID = ?",
                            new String[]{themeID}, null, null, null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("subject_id", cursor.getString(0));
                map.put("subject_content", cursor.getString(1));
                map.put("theme_id", cursor.getString(2));
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    // 返回美容option
    public ArrayList<HashMap<String, Object>> queryOption(Context context,
                                                          String selecs, String args) {
        Cursor cursor = null;
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try {
            cursor = DictionaryDatabase
                    .QueryData("sms_test_option", new String[]{"answer_tag",
                                    "answer_content", "ANSWER_RESULT_DESC",
                                    "ANSWER_MARK", "ANSWER_SEQ"}, selecs + " = ? ",
                            new String[]{args}, null, null, "ANSWER_SEQ asc");
            while (cursor.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("answer_tag", cursor.getString(0));
                map.put("answer_content", cursor.getString(1));
                map.put("answer_result_desc", cursor.getString(2));
                map.put("boolean", false);
                map.put("ANSWER_MARK", cursor.getString(3));
                map.put("ANSWER_SEQ", cursor.getString(4));
                data.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    // 返回美容Result
    public String queryResult(String themeID, String mark, String end) {
        Cursor cursor = null;
        String result = "";
        try {
            if ("".equals(end.trim())) {
                cursor = DictionaryDatabase
                        .reQuery(
                                "SELECT RESULT_CONTENT FROM sms_test_result where theme_id = ? and ? between COUNT_MIN and COUNT_MAX",
                                new String[]{themeID, mark});
            } else {
                cursor = DictionaryDatabase
                        .reQuery(
                                "SELECT RESULT_CONTENT FROM sms_test_result where theme_id = ? limit ?,?",
                                new String[]{themeID, mark, end});
            }

            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 更新账号信息
     *
     * @param context
     * @param pas     账号密码
     * @param flag    更新标志 0 密码 1 头像 2 是否默认
     */
    public void updateAccountInfo(Context context, String account,
                                  String[] args, int flag) {
        String sql = null;
        switch (flag) {
            case Tables.TableLogin.UPDATE_PAS:
                sql = "update accounts set password = ? where account = ?";
                break;
            case Tables.TableLogin.UPDATE_FACE:
                sql = "update accounts set face = ?,sex = ? ,name = ?,password = ? where account = ?";
                break;
            case Tables.TableLogin.UPDATE_ISDEF:
                sql = "update accounts set isDefault = ? where isDefault = ?";
                break;
            default:
                break;
        }
        if (args.length == 1)
            DictionaryDatabase.dictionDatabase.execSQL(sql, new String[]{
                    args[0], account});
        else
            DictionaryDatabase.dictionDatabase.execSQL(sql, new String[]{
                    args[0], args[1], args[2], args[3], account});
    }

    /**
     * 查知识页面 根据id查询信息
     */
    public LinkedHashMap<String, String> queryKeShi(Context context) {

        LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
        Cursor cursor = null;
        try {
            cursor = DictionaryDatabase.queryData("DoctorKeShi", new String[]{
                    "code", "name"}, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                data.put(name, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 查知识页面 根据id查询信息 首字母索引
     */
    public HashMap<String, ArrayList<HashMap<String, Object>>> queryKnowledgeName(
            Context context, String zid) {

        HashMap<String, ArrayList<HashMap<String, Object>>> data = new HashMap<String, ArrayList<HashMap<String, Object>>>();
        Cursor cursor = null;
        try {
            if (zid == null) {
                cursor = DictionaryDatabase.queryData("sms_disease_office",
                        new String[]{"OFFICE_CODE", "OFFICE_NAME"}, null,
                        null, null, null, null);
            } else {
                cursor = DictionaryDatabase
                        .reQuery(
                                "select menu_id,menu_name,proviso,ICON_ADDR,GET_CONTENT_TYPE from SMS_Client_Menu_Define where UPPER_MENU_ID = ?",
                                new String[]{zid});
            }
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String firstChar = PingYinUtil.getPingYin(context,
                        name.substring(0, 1));
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("zname", name);
                map.put("sort_key", firstChar);
                if (zid != null) {
                    map.put("zid", id);
                    map.put("zfatherid", zid);
                    map.put("type", cursor.getString(4));
                    map.put("extra", cursor.getString(2));
                } else {
                    map.put("type", "Disease");
                    map.put("zid", "Diseasekeshi," + id);
                }

                if (data.containsKey(firstChar)) {
                    data.get(firstChar).add(map);
                } else {
                    ArrayList<HashMap<String, Object>> arr = new ArrayList<HashMap<String, Object>>();
                    arr.add(map);
                    data.put(firstChar, arr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return data;
    }

    /**
     * 查询信息层面数据
     *
     * @param context
     * @param args
     */
    public List<Map<String, String>> queryInFolay(Context context,
                                                  String args) {
        String sql = "select INFO_LAY_ID,INFO_LAY_NAME from information_lay_define where UPPER_LAY_ID = ?";
        return queryData(context, sql, args);
    }

    /**
     * 封装信息层面数据
     *
     * @param infolayList 第一级
     * @param infolayMap  第二级
     */
    public List<Map<String, String>> setInfolayData(Context context,
                                                    Map<String, List<Map<String, String>>> map) {
        List<Map<String, String>> infolayList = queryInFolay(context,
                "0");
        for (int i = 0; i < infolayList.size(); i++) {
            Map<String, String> hash = infolayList.get(i);
            List<Map<String, String>> list = queryInFolay(context,
                    hash.get(Tables.TableCity.CODE));
            map.put(hash.get(Tables.TableCity.NAME), list);
        }
        return infolayList;
    }

    /**
     * 根据城市编码查询城市
     *
     * @param context
     * @param area_code 城市编码
     * @return
     */
    public String queryAddressNew(Context context, String area_code) {
        String sql = "select AREA_NAME from SMS_AREA_DEFINE where AREA_CODE=?";
        Cursor cursor = null;
        String address = null;
        try {
            cursor = DictionaryDatabase.reQuery(sql, new String[]{area_code});
            address = area_code;
            if (cursor.moveToFirst()) {
                address = cursor.getString(0);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return address;
    }
}
