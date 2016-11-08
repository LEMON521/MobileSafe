package com.ajohn.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 * Created by John on 2016/11/5.
 */

public class BlackNumberDBHelper extends SQLiteOpenHelper {
    /**
     * @param context to use to open or create the database
     */
    public BlackNumberDBHelper(Context context) {
        super(context, "BlackNum.db", null, 1);
    }
    /**
     * 创建表,如果存在则不再创建
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber(_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
    }

    /**
     * 升级数据库时调用的方法
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
