package com.ajohn.mobilesafe.activities.baseactivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 手指的横向滑动识别器
 * Created by John on 2016/10/20.
 */

public abstract class BaseHorizontalGestureDetector extends Activity {
    private GestureDetector gestureDetector;
    //便于我们的子类用到SharedPreferences
    protected SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config",MODE_PRIVATE);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                /**
                 * 为防止用户大幅度的斜着滑动而产生效果,我们将之屏蔽
                 */
                if ((Math.abs(e2.getY()-e1.getY())>100)) {
                    return true;
                }

                /**
                 * 横向滑动事件
                 * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
                 * 在这里我们返回了true,用来我们让该滑动事件就此终结
                 */
                if (e2.getX() - e1.getX() > 200) {
                    showLeft();
                    return true;
                }

                /**
                 * 始点在右,终点在左,向左滑动,打开下一个Activity/ImageView
                 * 在这里我们返回了true,用来我们让该滑动事件就此终结
                 */
                if (e1.getX() - e2.getX() > 200) {
                    showRight();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }


        });

    }

    /**
     * 横向滑动事件
     *
     * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
     *
     * 这里放置跳转页面/图画的代码
     */
    public abstract void showLeft();

    /**
     * 横向滑动事件
     *
     * 始点在右,终点在左,向左滑动,打开下一个Activity/ImageView
     *
     * 这里放置跳转页面/图画的代码
     */
    public abstract void showRight();

    /**
     * 使用手势识别器
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
}
