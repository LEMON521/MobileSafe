package com.ajohn.mobilesafe.activities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.safeactivities.SetSafeStep1Activity;
import com.ajohn.mobilesafe.receiver.AdminReceiver;

/**
 * Created by John on 2016/10/19.
 */

public class SafeActivity extends Activity {
    // TODO 设备管理员公共接口
    private DevicePolicyManager dpm;
    private ComponentName componentName;
    private boolean isAdminActivate = false;

    private SharedPreferences sp;
    private TextView tv_safe_phonenum;
    private ImageView iv_safe_isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        tv_safe_phonenum = (TextView) findViewById(R.id.tv_safe_phonenum);
        iv_safe_isLock = (ImageView) findViewById(R.id.iv_safe_isLock);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        String number = sp.getString("safeNumber","");
       // boolean protacting = sp.getBoolean("protacting",false);
        tv_safe_phonenum.setText(number);

        //实例化设备管理员
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //将要激活哪个组件
        componentName = new ComponentName(this, AdminReceiver.class);

        isAdminActivate = dpm.isAdminActive(componentName);

        if (!isAdminActivate) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("adminActivateOpen",false);
            editor.putBoolean("protacting",false);
        }

        //设置 - 开启/关闭 - 图标
        if (isAdminActivate) {
            iv_safe_isLock.setImageResource(R.drawable.lock);
        }else {
            iv_safe_isLock.setImageResource(R.drawable.unlock);
        }


    }


    //*******************************进入设置页面按钮事件*******************************
    public void enterSafeSet(View view)
    {
        Intent intent = new Intent(this,SetSafeStep1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in,R.anim.tran_next_out);


    }

}
