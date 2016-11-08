package com.ajohn.mobilesafe.activities.safeactivities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.activities.SafeActivity;
import com.ajohn.mobilesafe.activities.baseactivities.BaseHorizontalGestureDetector;
import com.ajohn.mobilesafe.receiver.AdminReceiver;

/**
 * Created by John on 2016/10/19.
 */

public class SetSafeStep4Activity extends BaseHorizontalGestureDetector {

    // TODO 设备管理员公共接口
    private DevicePolicyManager dpm;
    private ComponentName componentName;

    private CheckBox cb_safe_ischeck;
    private CheckBox cb_safe_openAdminDevice;
    boolean isOpenAdminDevice = false;
    boolean isOpenSafe = false;
//    boolean cb_isOpenAdminDevice = false;
//    boolean cb_isOpenSafe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safesetstep4);
        cb_safe_openAdminDevice = (CheckBox) findViewById(R.id.cb_safe_openAdminDevice);
        cb_safe_ischeck = (CheckBox) findViewById(R.id.cb_safe_ischeck);


        //实例化设备管理员
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //将要激活哪个组件
        componentName = new ComponentName(this, AdminReceiver.class);


        //  获取配置文件的关键字的配置状态
        //isOpenAdminDevice = sp.getBoolean("adminActivateOpen", false);
        isOpenAdminDevice = dpm.isAdminActive(componentName);
        isOpenSafe = sp.getBoolean("protacting", false);
        isCheck(isOpenAdminDevice);
        cb_safe_openAdminDevice.setChecked(isOpenAdminDevice);
        cb_safe_openAdminDevice.setClickable(!isOpenAdminDevice);
     //   cb_isOpenAdminDevice = isOpenAdminDevice;
        safeCheckboxClickable(isOpenAdminDevice);


        cb_safe_openAdminDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //如果没有开启
                if (!dpm.isAdminActive(componentName)) {
                    cb_safe_openAdminDevice.setClickable(false);
                    //定义意图,设备,添加设备管理员
                    Intent activateIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

                    //激活的说明
                    activateIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    activateIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启设备管理员,设备会更加安全");
                    startActivity(activateIntent);

                }
                safeCheckboxClickable(isChecked);


            }
        });
        isOpenSafe = sp.getBoolean("protacting", false);

        cb_safe_ischeck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck(isChecked);
            }
        });


    }

    /**
     * 设置安全选框是否可选
     */
    private void safeCheckboxClickable(boolean ischecked) {
        cb_safe_ischeck.setClickable(ischecked);

    }

    /**
     * 获取到CheckBox的选中状态
     */
    private void isCheck(boolean ischecked) {

        if (ischecked) {
            cb_safe_ischeck.setText("防盗保护已经--开启");
        }else {
            cb_safe_ischeck.setText("防盗保护已经--关闭");
        }
        cb_safe_ischeck.setChecked(ischecked);

    }


//    cb_isChecked = ischecked;
//    SharedPreferences.Editor editor = sp.edit();
//    if (cb_isChecked) {
//
//        //判断设备管理器是否开启
//        if (sp.getBoolean("adminActivateOpen",false)) {
//            cb_safe_ischeck.setText("防盗保护已经--开启");
//        }else {
//            //没有开启就强制用户去开启
//            Intent intent = new Intent(this,LockScreenActivity.class);
//            startActivity(intent);
//        }
//
//    } else {
//        //如果不选择开启保护,则不关心设备管理器是否开启
//        cb_safe_ischeck.setText("防盗保护已经--关闭");
//    }
//    cb_safe_ischeck.setChecked(cb_isChecked);
//    editor.putBoolean("protacting", cb_isChecked);
//    editor.commit();
    //*************************--手指滑动事件--*************************

    /**
     * 横向滑动事件
     * 始点在左,终点在右,向右滑动,打开上一个Activity/ImageView
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showLeft() {
        Intent intent = new Intent(this, SetSafeStep3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);
    }

    /**
     * 横向滑动事件
     * 始点在右,终点在左,向左滑动,打开下一个Activity/ImageView
     * 这里放置跳转页面/图画的代码
     */
    @Override
    public void showRight() {
        Intent intent = new Intent(this, SafeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
    }


    //*************************--按钮事件--*************************
    public void btn_next_onclick(View view) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("protacting", cb_safe_ischeck.isChecked());
        editor.putBoolean("adminActivateOpen",cb_safe_openAdminDevice.isChecked());
        editor.commit();

        showRight();
    }

    public void btn_previous_onclick(View view) {
        showLeft();
    }
}
