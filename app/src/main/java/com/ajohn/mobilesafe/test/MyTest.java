package com.ajohn.mobilesafe.test;

import android.test.AndroidTestCase;

import com.ajohn.mobilesafe.db.BlackNumberDBHelper;
import com.ajohn.mobilesafe.db.dao.BlackNumberDao;

/**
 * Created by John on 2016/11/6.
 */

public class MyTest extends AndroidTestCase {

    public void test() throws Exception {
        BlackNumberDBHelper helper;
        helper = new BlackNumberDBHelper(getContext());
        helper.getWritableDatabase();
        }
    public void testA() throws Exception {
        BlackNumberDao dao;
        dao = new BlackNumberDao(getContext());
        long result = dao.addBlackNum("13937260956","2");
        System.out.print(result);
    }
    public void testAddBlackers()
    {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        for (int i = 0;i<100;i++) {
            long result = blackNumberDao.addBlackNum("139372609"+i,i%3+"");
            System.out.print(result);
        }
    }
}
