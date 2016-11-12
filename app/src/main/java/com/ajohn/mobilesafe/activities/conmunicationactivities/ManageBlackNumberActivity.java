package com.ajohn.mobilesafe.activities.conmunicationactivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.adapters.BlackNumberItemsAdapter;

/**
 * Created by John on 2016/11/6.
 */

public class ManageBlackNumberActivity extends Activity {

    private ListView lv_black_blackitems;
    private BlackNumberItemsAdapter blackNumberItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);

        //获取到全部的联系人

        //初始化ListView
        blackNumberItemsAdapter = new BlackNumberItemsAdapter(this);
        lv_black_blackitems = (ListView) findViewById(R.id.lv_black_blackitems);
        lv_black_blackitems.setAdapter(blackNumberItemsAdapter);

        //当ListView状态变化的时候回调--设置滑动到底部的监听事件
        lv_black_blackitems.setOnScrollListener(new AbsListView.OnScrollListener() {

            //当状态发生变化当时候回调当方法
            //静止-->滚动
            //滑动-->静止
            //手指滑动-->惯性滚动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {

                    //滑动／滚动状态
                    case SCROLL_STATE_FLING:
                        break;
                    //静止／空闲状态
                    case SCROLL_STATE_IDLE:
                        int last = lv_black_blackitems.getLastVisiblePosition();//获取到最后一个条目到位置数
                        int total = lv_black_blackitems.getCount();//获取到现在的Adapter的条目总数
                        if (last == total) {
                            //当
                            Toast.makeText(ManageBlackNumberActivity.this, "滚动到了最后，加载剩下到数据", Toast.LENGTH_SHORT).show();

                        }
                        //blackNumberItemsAdapter.
                        break;
                    //触摸滑动／滚动状态
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            //当滚动当时候执行的方法
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }
}
