package com.ajohn.mobilesafe.activities.toolsactivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;

/**
 * Created by John on 2016/11/3.
 */

public class ChangePositionToolsActivity extends Activity {

    private static final String TAG = "事件";
    private TextView tv_tools_changeposition_discription;
    private ImageView iv_tools_changeposition_set;
    private SharedPreferences sp;
    private int positionLeft = -200;
    private int positionRight = 200;
    private int positionTop = -200;
    private int positionBottom = 200;
    private int lastX= 0;
    private int lastY= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools_changeposition);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        tv_tools_changeposition_discription = (TextView) findViewById(R.id.tv_tools_changeposition_discription);

        iv_tools_changeposition_set = (ImageView) findViewById(R.id.iv_tools_changeposition_set);

        //设置图标已保存的位置
        positionLeft = sp.getInt("ToastPositionLeft", 100);
        positionRight = sp.getInt("ToastPositionRight", 100);
        positionTop = sp.getInt("ToastPositionTop", 100);
        positionBottom = sp.getInt("ToastPositionBoyyom", 100);
        lastX = sp.getInt("lastX", 0);
        lastY = sp.getInt("lastY", 0);
        Log.e("保存的位置Left", "" + positionLeft);
        Log.e("保存的位置Right", "" + positionRight);
        Log.e("保存的位置Top", "" + positionTop);
        Log.e("保存的位置Bottom", "" + positionBottom);
        Log.e("保存的位置lastX", "" + lastX);
        Log.e("保存的位置lastY", "" + lastY);
        RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) iv_tools_changeposition_set.getLayoutParams() ;
        params.leftMargin = lastX;
        params.topMargin = lastY;
        iv_tools_changeposition_set.setLayoutParams(params);
        Log.e("执行", "" + "他妈的");
        //iv_tools_changeposition_set.layout(lastX,lastY,iv_tools_changeposition_set.getWidth()+lastX,iv_tools_changeposition_set.getHeight()+lastY);
        Log.e("执行过", "" + "你麻痹");
        //设置图片的拖动事件
        iv_tools_changeposition_set.setOnTouchListener(new View.OnTouchListener() {

            //切记不能放在onTouch方法里面,不然会被一直赋值为 0 !
            float startX = 0;
            float startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {

                    //按下
                    case MotionEvent.ACTION_DOWN:
                        //获取到当前坐标
                        startX = event.getRawX();
                        startY = event.getRawY();
                        //注意,getRawX()是获取到控件的中心位置
                        //getX()获取到控件的右上角边缘
//                        startX = event.getX();
//                        startY = event.getY();
                        break;
                    //移动
                    case MotionEvent.ACTION_MOVE:
                        //获取到移动后的坐标
                        float nowX = event.getRawX();
                        float nowY = event.getRawY();
                        //获取位移量
                        int offsetX = (int) (nowX - startX);
                        int offsetY = (int) (nowY - startY);
                        //设置拖动后的位置,以免他又回去
                        if (offsetX == 0 && offsetY == 0) {

                        }
                        iv_tools_changeposition_set.layout(iv_tools_changeposition_set.getLeft() + offsetX, iv_tools_changeposition_set.getTop() + offsetY, iv_tools_changeposition_set.getRight() + offsetX, iv_tools_changeposition_set.getBottom() + offsetY);

                        //重新获取控件的位置
                        startX = event.getRawX();
                        startY = event.getRawY();


                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        positionLeft = iv_tools_changeposition_set.getLeft();
                        positionRight = iv_tools_changeposition_set.getRight();
                        positionTop = iv_tools_changeposition_set.getTop();
                        positionBottom = iv_tools_changeposition_set.getBottom();

                        lastX = iv_tools_changeposition_set.getLeft();
                        lastY = iv_tools_changeposition_set.getTop();

                        //保存位置
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("ToastPositionLeft", positionLeft);
                        editor.putInt("ToastPositionRight", positionRight);
                        editor.putInt("ToastPositionTop", positionTop);
                        editor.putInt("ToastPositionBottom", positionBottom);
                        editor.putInt("lastX",lastX);
                        editor.putInt("lastY",lastY);
                        editor.commit();
                        Log.e("退出时保存的位置Left", "" + positionLeft);
                        Log.e("退出时保存的位置Right", "" + positionRight);
                        Log.e("退出时保存的位置Top", "" + positionTop);
                        Log.e("退出时保存的位置Bottom", "" + positionBottom);
                        Log.e("退出时保存的位置lastX", "" + lastX);
                        Log.e("退出时保存的位置lastY", "" + lastY);

                }

                return true;
            }
        });
//        iv_tools_changeposition_set.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                Log.e(TAG,v.toString());
//                Log.e(TAG,left+"");
//                Log.e(TAG,top+"");
//                Log.e(TAG,right+"");
//                Log.e(TAG,bottom+"");
//                Log.e(TAG,oldLeft+"");
//                Log.e(TAG,oldTop+"");
//                Log.e(TAG,oldRight+"");
//                Log.e(TAG,oldBottom+"");
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将拖拉到的位置保存起来
//        if (positionLeft != -200 && positionRight != 200 && positionTop != 200 && positionBottom != 200) {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putInt("ToastPositionLeft", positionLeft);
//            editor.putInt("ToastPositionRight", positionRight);
//            editor.putInt("ToastPositionTop", positionTop);
//            editor.putInt("ToastPositionBottom", positionBottom);
//            editor.putInt("lastX",lastX);
//            editor.putInt("lastY",lastY);
//            editor.commit();
//            Log.e("退出时保存的位置Left", "" + positionLeft);
//            Log.e("退出时保存的位置Right", "" + positionRight);
//            Log.e("退出时保存的位置Top", "" + positionTop);
//            Log.e("退出时保存的位置Bottom", "" + positionBottom);
//        }


    }
}
