package com.yksj.healthtalk.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 实例数据库操作对象
 * @author zhao yuan
 *@version 3.0
 */
public abstract class DataBase {
	
	protected static SQLiteDatabase userDatabase;//用户数据库对象
	protected static SQLiteDatabase dictionDatabase;//程序自带数据库对象
	
	/**
	 * 系统自带数据库对象初始化
	 * @param context
	 */
	DataBase(Context context) {
		if(dictionDatabase == null){
			SQLiteAssetOpenHelper dictionaryOpenHelper = new SQLiteAssetOpenHelper(context,null);
			dictionDatabase = dictionaryOpenHelper.getReadableDatabase();
		}
	}
	
	/**
	 * 用户创建数据库
	 * @param context
	 * @param name
	 */
	DataBase(Context context,String name){
		if(userDatabase == null){
			ChatUserOpenHelper chatUserOpenHelper = new ChatUserOpenHelper(context, name);
			userDatabase = chatUserOpenHelper.getWritableDatabase();
		}
	}
	/**
	 * 关闭用户数据库
	 */
	protected  synchronized void closeUserDb(){
		if(userDatabase != null){
			userDatabase.close();
			userDatabase = null;
		}
	}
	/**
	 * 关闭程序自带数据库
	 */
	protected  synchronized void closeDictionDb(){
		if(dictionDatabase != null){
			dictionDatabase.close();
			dictionDatabase = null;
		}
	}
	
}
