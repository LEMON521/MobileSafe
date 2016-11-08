package com.ajohn.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryDao {
    //private static String path = "/data/data/com.itheima.mobilesafe/files/address.db";
    private static String path = "/data/data/com.ajohn.mobilesafe/files/address.db";
    /**
     * 号码归属地的查询
     *
     * @param number 要查询的电话号码
     * @return 号码的归属地
     */
    public static String getAddess(String number) {
        String address = number;
        //String path = "file:///android_asset/address.db";//SQLiteDatabase对这个路径不识别
        //WebView 加载图片
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        //手机号码-个数11位，13,14,15,16,17,18；-手机号码正则表达式
        //^1[345678]\d{9}$

        if (number.matches("^1[345678]\\d{9}$")) {//手机电话号码
            Cursor cursor = db.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
            while (cursor.moveToNext()) {
                String location = cursor.getString(0);
                address = location;
            }
            cursor.close();
            db.close();
        } else {
            //110,119,5554，长途
            switch (number.length()) {
                case 3://110,119,120
                    address = "匪警号码";
                    break;
                case 4://5556,5554
                    address = "模拟器";
                    break;
                case 5://10086,10010
                    address = "客服电话";
                    break;

                case 6://10086,10010
                    address = "本地号码";
                    break;
                case 7://10086,10010
                    address = "本地号码";
                    break;
                case 8://10086,10010
                    address = "本地号码";
                    break;

                default:
                    if (number != null && number.startsWith("0") && number.length() >= 10) {
                        //010988777777
                        Cursor cursor = db.rawQuery("select location from data2 where area = ? ", new String[]{number.substring(1, 3)});
                        while (cursor.moveToNext()) {
                            String location = cursor.getString(0);
                            //黔东南电信-->黔东南
                            address = location.substring(0, location.length() - 2);
                        }

                        //0855888277277
                        cursor = db.rawQuery("select location from data2 where area = ? ", new String[]{number.substring(1, 4)});
                        while (cursor.moveToNext()) {
                            String location = cursor.getString(0);
                            //黔东南电信-->黔东南
                            address = location.substring(0, location.length() - 2);
                        }

                        cursor.close();
                        db.close();

                    }
                    break;
            }

        }


        return address;
    }

}
