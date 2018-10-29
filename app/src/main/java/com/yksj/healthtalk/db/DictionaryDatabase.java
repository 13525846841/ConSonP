package com.yksj.healthtalk.db;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * 对程序本身自带的数据库进行基本的查询操作
 * @author zhao yuan
 * @version 3.0
 */
public class DictionaryDatabase extends DataBase{
	
	DictionaryDatabase(Context context) {
		super(context);
	}
	
	public static List<String> queryData(){
		Cursor cursor = dictionDatabase.query(Tables.ChatTable.TABLE_NAME,new String[]{Tables.ChatTable.NAME,Tables.ChatTable.TIME},Tables.ChatTable.ID+"> ?",new String[]{String.valueOf("0")},null,null,null,null);
		final List<String> list = new ArrayList<String>();
		try{
			while(cursor.moveToNext()){
				String valueName = cursor.getString(0);
				list.add(valueName);
			}
		}catch(SQLException e){
			
		}finally{
			cursor.close();
		}
		return list;
	}
	
	public static List<String> queryTreeData(){
		Cursor cursor = dictionDatabase.query("ServerTree",new String[]{"site"},null,null,null,null,null,null);
		final List<String> list = new ArrayList<String>();
		try{
			while(cursor.moveToNext()){
				String valueName = cursor.getString(0);
				list.add(valueName);
			}
		}catch(SQLException e){
			
		}finally{
			cursor.close();
		}
		return list;
	}
	/**
	 * 查询医生职称
	 * 
	 * @return
	 */
	public static Cursor LinkedHashDoctor() {
		return 	dictionDatabase.query("SMS_Title_Define", new String[] {
				"TITLE_NAME", "TITLE_CODE" }, null, null, null, null, null);
	
	}


	/**
	 * 收藏新闻
	 * 
	 * @param title
	 *            标题
	 * @param time
	 *            时间
	 * @param content
	 *            新闻内容
	 * @param connectionUserId
	 *            用户id
	 * @param typeCode
	 *            内容类型id
	 * @param contentId
	 *            内容id
	 * @return
	 */
	public static long insertData(String tablNameNewsConnection, String object,
			ContentValues contentValues) {
		return dictionDatabase.insert(tablNameNewsConnection, null,
				contentValues);
	}
	/**
	 * 根据类型和内容id查询当前内容是否已经收藏
	 * 
	 * @param typeCode
	 * @param contentId
	 * @return
	 */
	public static Cursor QueryData(String tablNameNewsConnection,
			String[] columns, String selection, String[] selectionArgs,
			Object object, Object object2, Object object3) {
		return dictionDatabase.query(tablNameNewsConnection,
				columns, selection, selectionArgs, null, null, null);
			
	}


	public static Cursor reQuery(String sql, String[] strings) {
		return dictionDatabase.rawQuery(sql, strings);
	}
	public static Cursor queryData(String string, String[] strings,
			String string2, String[] strings2, Object object, Object object2,
			String string3) {
		return dictionDatabase.query(string, strings, string2,strings2, null,
				null, string3);
		
	}

}
