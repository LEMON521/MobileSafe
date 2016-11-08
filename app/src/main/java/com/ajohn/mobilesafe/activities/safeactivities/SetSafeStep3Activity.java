package com.ajohn.mobilesafe.activities.safeactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.baseactivities.BaseHorizontalGestureDetector;


/**
 * Created by John on 2016/10/19.
 */

public class SetSafeStep3Activity extends BaseHorizontalGestureDetector {

    /**
     * 页面的请求码
     */
    private static int START_ACTIVITY_SELECT = 0;

    /**
     * 页面的返回码
     *
     * @param savedInstanceState
     */
    private static int GETDATA_ACTIVITY_SELECT = 0;

    private static String TAG = "SetSafeStep3Activity";

    private EditText et_safe_select_contact;

    private String number = "";
    //*************************--初始化页面--*************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safesetstep3);
        et_safe_select_contact = (EditText) findViewById(R.id.et_safe_select_contact);
        number = sp.getString("safeNumber", "");
        et_safe_select_contact.setText(number);


    }





    //*************************--手指滑动事件--*************************

    /**
     * 横向滑动事件
     * <p>
     * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
     * <p>
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showLeft() {
        Intent intent = new Intent(this, SetSafeStep2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
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

        //判断输入框是否为空
        number = et_safe_select_contact.getText().toString().trim();

        if (!number.equals("")) {
            //将安全号码保存起来
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("safeNumber",number );
            editor.commit();


            Intent intent = new Intent(this, SetSafeStep4Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
        }else {
            Toast.makeText(this,"哥,不设置安全号码怎么找回手机呢~~",Toast.LENGTH_LONG).show();
        }

    }


    //*************************--按钮事件--*************************
    public void btn_next_onclick(View view) {
        showRight();
    }

    public void btn_previous_onclick(View view) {
        showLeft();
    }

    public void safe_selectContact(View view) {
        Intent intent = new Intent(this, SafeSelectContactActivity.class);
        //因为我们要打开的Activity是要返回数据的.所以要用
//        intent.setAction("activity");
//        intent.putExtra("citvity","shujulaile");
        startActivityForResult(intent, START_ACTIVITY_SELECT);
        //startActivity(intent);
    }


    /**
     * 从其他页面传递回来的值,用这个重载方法来获取
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG,"requestCode=="+requestCode);
//        Log.e(TAG,"resultCode=="+resultCode);
        if (data != null) {
            number = data.getStringExtra("number").replace("-", "");
            et_safe_select_contact.setText(number);
        }

    }
}
