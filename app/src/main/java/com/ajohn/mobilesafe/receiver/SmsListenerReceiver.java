package com.ajohn.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ajohn.mobilesafe.R;
import com.ajohn.mobilesafe.service.GpsLocationService;

import static android.app.admin.DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY;

/**
 * Created by John on 2016/10/24.
 */

public class SmsListenerReceiver extends BroadcastReceiver {

    // TODO 设备管理员公共接口
    private DevicePolicyManager dpm;
    private ComponentName componentName;
    private boolean isAdminActivate = false;

    private String TAG = "SmsListenerService";

    // private DevicePolicyManager dpm;
    private SharedPreferences sp;
    //获取安全号码
    private String safeNumber = "";
    private String comingNumber = "";
    private String simbody = "";
    private boolean protacting = false;


    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // getResultCode();
        // dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        safeNumber = sp.getString("safeNumber", "");
        protacting = sp.getBoolean("protacting", false);
        // Log.e(TAG, "protacting为:"+protacting);
        //实例化设备管理员
        dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
        //将要激活哪个组件
        componentName = new ComponentName(context, AdminReceiver.class);
        isAdminActivate = dpm.isAdminActive(componentName);
        if (isAdminActivate) {
            if (protacting) {

                //短信是pdus协议,所以,短信的事件类型为以pdus为名称的Object[]数组
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (Object pdu : pdus) {
                    //获取到了短信
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    //对于短信,我们只关心的是安全号码,以及安全号码短信的内容
                    //短信的内容具体来说我们关心的更是--与指令有关的内容

                    //获取发来的短信的号码
                    comingNumber = sms.getOriginatingAddress();
                    Log.e(TAG, "safeNumber为:" + safeNumber);
                    Log.e(TAG, "comingNumber为:" + comingNumber);
                    //获取短信内容
                    simbody = sms.getMessageBody();

                    //如果获取到的是安全号码,根据内容再进行判断
                    if (comingNumber.equals(safeNumber)) {//因为我们保存的号码可能不加国际码,但是接收的号码一般都有国际码
                        if (simbody.equals("#location#")) {
                            Log.e(TAG, "发送位置");
                            Intent GPSintent = new Intent(context, GpsLocationService.class);
                            context.startService(GPSintent);
                            abortBroadcast();//拦截消息不会再向系统发送广播
                        } else if (simbody.equals("#alarm#")) {
                            Log.e(TAG, "响起了警报");
                            MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                            player.setVolume(1.0f, 1.0f);
                            player.start();
                            abortBroadcast();//拦截消息不会再向系统发送广播,状态栏不会再提示

                        } else if (simbody.equals("#wipdata#")) {

                            if (sp.getBoolean("adminActivateOpen", false)) {
                                Log.e(TAG, "删除手机数据");
                                //同时格式化SD卡的数据
                                //dpm.wipeData(WIPE_EXTERNAL_STORAGE);
                                //恢复出厂设置
                                //dpm.wipeData(WIPE_RESET_PROTECTION_DATA);
                            } else {
                                Log.e(TAG, "没有开启设备管理器,无法远程删除手机数据");
                            }
                            abortBroadcast();//拦截消息不会再向系统发送广播
                        } else if (simbody.equals("#lockscreen#")) {

                            if (sp.getBoolean("adminActivateOpen", false)) {
                                Log.e(TAG, "远程锁屏");
                                dpm.resetPassword("123456", RESET_PASSWORD_REQUIRE_ENTRY);
                                dpm.lockNow();
                            } else {
                                Log.e(TAG, "没有开启设备管理器,无法远程锁屏");
                            }

                            abortBroadcast();//拦截消息不会再向系统发送广播
                        }
                    }
                }
            }
        }


    }
}
