package com.ajohn.mobilesafe.activities.safeactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.baseactivities.BaseHorizontalGestureDetector;

/**
 * Created by John on 2016/10/19.
 */

public class SetSafeStep1Activity extends BaseHorizontalGestureDetector {

    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safesetstep1);



    }



    ////*************************--手指滑动事件--*************************


    /**
     * 横向滑动事件
     * <p>
     * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
     * <p>
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showLeft() {

    }

    /**
     * 横向滑动事件
     * <p>
     * 始点在右,终点在左,向左滑动,打开下一个Activity/ImageView
     * <p>
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showRight() {
        Intent intent = new Intent(this, SetSafeStep2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


    //*************************--按钮事件--*************************

    public void btn_next_onclick(View view) {
        showRight();
    }
}
