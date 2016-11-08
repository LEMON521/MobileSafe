package com.ajohn.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ajohn.mobilesafe.bean.BlackNumberBean;
import com.ajohn.mobilesafe.db.BlackNumberDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 2016/11/5.
 */

public class BlackNumberDao {
    private BlackNumberDBHelper helper;
    private SQLiteDatabase db;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberDBHelper(context);
        helper.getWritableDatabase();
    }

    //增加黑名单

    /**
     * 插入黑名单
     *
     * @param number 将要插入的黑名单号码
     * @param mode   拦截模式---0电话拦截,   1:短信拦截   ;2:全部拦截
     * @return 新插入的行的行标识，如果发生错误，返回   -1
     */
    public long addBlackNum(String number, String mode) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        long result = db.insert("blacknumber", null, values);
        db.close();
        return result;
    }


    /**
     * 删除黑名单
     *
     * @param number 将要删除的黑名单号码
     * @return 删除的行数
     */
    public int deleteBlackNum(String number) {
        db = helper.getWritableDatabase();
        int result = db.delete("blacknumber", "number = ?", new String[]{number});
        db.close();
        return result;
    }

    /**
     * 修改拦截模式
     *
     * @param number  要修改的号码
     * @param newMode 要修改后的模式
     * @return 瘦影响的号码
     */
    public int changeBlackNumMod(String number, String newMode) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newMode);
        int result = db.update("blacknumber", values, "number = ?", new String[]{number});
        db.close();
        return result;
    }

    /**
     * 查询黑名单
     *
     * @return HashMap<String,String>---<联系人,模式>
     */
    public /*ArrayList<HashMap<String,String>>*/List<BlackNumberBean> queryAllBlackNum() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"_id", "number", "mode"}, null, null, null, null, null);
       /* ArrayList al = new ArrayList();*/
        List<BlackNumberBean> blacks = new ArrayList<BlackNumberBean>();
        while (cursor.moveToNext()) {
            //cursor.getColumnName(cursor.getColumnIndex("_id"));
            BlackNumberBean black = new BlackNumberBean();
            String number = cursor.getString(cursor.getColumnIndex("number"));
            black.setNumber(number);
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            black.setMode(mode);
            blacks.add(black);
        }
        return blacks;
    }
}
