package com.ajohn.mobilesafe.activities.conmunicationactivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.adapters.BlackNumberItemsAdapter;

/**
 * Created by John on 2016/11/6.
 */

public class ManageBlackNumberActivity extends Activity {

    private ListView lv_black_blackitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);

        //获取到全部的联系人

        //初始化ListView
        lv_black_blackitems = (ListView) findViewById(R.id.lv_black_blackitems);
        lv_black_blackitems.setAdapter(new BlackNumberItemsAdapter(this));
    }
}
