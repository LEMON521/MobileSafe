package com.ajohn.mobilesafe.activities.safeactivities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.receiver.AdminReceiver;

/**
 * TODO 此页面意在引导用户开启设备管理器
 * Created by John on 2016/10/25.
 */

public class LockScreenActivity extends Activity {

    // TODO 设备管理员公共接口
    private DevicePolicyManager dpm;
    private ComponentName componentName;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_lockscreen);

        sp = getSharedPreferences("config",MODE_PRIVATE);

        //实例化设备管理员
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        //将要激活哪个组件
        componentName = new ComponentName(this,AdminReceiver.class);

        //我们让用户第设置开启安全服务的时候,让用户强行选择开启设备管理
        boolean isOpen = dpm.isAdminActive(componentName);
        if (isOpen) {
            //将设备管理器是否激活写入配置文件
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("adminActivateOpen",false);
            editor.commit();
            finish();
            onDestroy();

        }else {
            openActivateIntent();
            onDestroy();
        }

    }

    /**
     * 一键锁屏按钮事件
     */

    public void lockScreen(View view){
        dpm.lockNow();
    }
    /**
     * 激活设备按钮事件
     */

    public void activateDevice(View view){

        openActivateIntent();
    }

    private void openActivateIntent() {
        //定义意图,设备,添加设备管理员
        Intent activateIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //激活的说明
        activateIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        activateIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启设备管理员,设备会更加安全");
        startActivity(activateIntent);
    }
}
