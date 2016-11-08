package com.ajohn.mobilesafe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


/**
 * 监听开机广播
 *
 * @author Created by John on 2016/10/21.
 */

public class BootCompeledService extends BroadcastReceiver {
    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */

    private SharedPreferences sp;
    private TelephonyManager tm;


    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //1.得到之前的sim卡信息
        String saveSim = sp.getString("simNum","")+"这里仅仅测试用";

        //2.得到当前手机的sim卡信息
        String currentSim = tm.getLine1Number();

        //首先判断是否为空,为空的情况:我们安装了软件,但是安装之后没有打开或者打开了没有设置安全号码
        if (saveSim!="") {
            //3.比较sim卡信息是否一致
            if (saveSim.equals(currentSim)) {

            }else {
                //4.如果不一致就发送短信给安全号码
                Log.e(TAG,"当前的手机号码为:"+currentSim);
                Toast.makeText(context,"当前的手机号码为:"+currentSim,Toast.LENGTH_LONG).show();

                //发送短信给安全号码
                SmsManager.getDefault().sendTextMessage(saveSim,null,"我的手机被盗啦",null,null);
            }
        }




    }
}
