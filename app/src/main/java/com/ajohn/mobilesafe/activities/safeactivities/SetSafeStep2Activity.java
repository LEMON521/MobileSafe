package com.ajohn.mobilesafe.activities.safeactivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.baseactivities.BaseHorizontalGestureDetector;
import com.ajohn.mobilesafe.view.CustomwedgitTTCView;

/**
 * Created by John on 2016/10/19.
 */

public class SetSafeStep2Activity extends BaseHorizontalGestureDetector {

    private static final String TAG = "SetSafeStep2Activity";
    private CustomwedgitTTCView safe_step2_bindSIM;

    //获取到电话设备的管理器
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safesetstep2);
        safe_step2_bindSIM = (CustomwedgitTTCView) findViewById(R.id.cwv_safe_bindSIM);
        //对自定义控件进行初始化---描述
        setCWVDiscription();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        boolean isSimBind = sp.getBoolean("simBind",false);
        if (isSimBind) {
            safe_step2_bindSIM.setChecked(isSimBind);
        }else {
            safe_step2_bindSIM.setChecked(isSimBind);
        }
        safe_step2_bindSIM.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String phoneNum = telephonyManager.getLine1Number();//一般国内厂商不会将电话号码输入在卡中
                String simNum = telephonyManager.getSimSerialNumber();
                SharedPreferences.Editor editor = sp.edit();
                if (safe_step2_bindSIM.isChecked()) {
                    safe_step2_bindSIM.setChecked(false);
                    editor.putString("phoneNum", "");
                    editor.putString("simNum", "");
                    editor.putBoolean("simBind",false);
                } else {
                    safe_step2_bindSIM.setChecked(true);
                    editor.putString("phoneNum", phoneNum);
                    editor.putString("simNum", simNum);
                    editor.putBoolean("simBind",true);

                }
                editor.commit();
                Log.e(TAG, "PhoneNum:" + phoneNum + "***SimNum:" + simNum);
            }
        });


    }


    //*********************************初手指滑动事件*********************************

    /**
     * 横向滑动事件
     * <p>
     * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
     * <p>
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showLeft() {
        Intent intent = new Intent(this, SetSafeStep1Activity.class);
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


        if (sp.getBoolean("simBind",false)) {

            Intent intent = new Intent(this, SetSafeStep3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
        }else {
            Toast.makeText(this,"哥,不绑定sim卡无法进行下一步呢~",Toast.LENGTH_SHORT).show();
            return;
        }


    }

    //*********************************初始化页面*********************************

    /**
     * 对自定义控件进行初始化---描述
     */
    private void setCWVDiscription() {
        safe_step2_bindSIM.setTitle("点击绑定SIM卡");
        safe_step2_bindSIM.setDescription("SIM卡没有绑定");

    }
    //******************************************************************


    //*************************--按钮事件--*************************
    public void btn_next_onclick(View view) {
        showRight();
    }

    public void btn_previous_onclick(View view) {
        showLeft();
    }
}
