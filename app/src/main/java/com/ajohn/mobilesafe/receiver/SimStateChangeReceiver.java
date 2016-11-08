package com.ajohn.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;



/**
 * Created by John on 2016/10/21.
 */

public class SimStateChangeReceiver extends BroadcastReceiver {
    private static String TAG = "SimStateChangeReceiver";
    private TelephonyManager tm;
    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
//        SIM_STATE_UNKNOWN
//        SIM_STATE_ABSENT
//        SIM_STATE_PIN_REQUIRED
//        SIM_STATE_PUK_REQUIRED
//        SIM_STATE_NETWORK_LOCKED
//        SIM_STATE_READY
//        ERROR(/#SIM_STATE_NOT_READY)
//        ERROR(/#SIM_STATE_PERM_DISABLED)
//        ERROR(/#SIM_STATE_CARD_IO_ERROR)
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            int simState = tm.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    Log.e(TAG,"SIM_STATE_UNKNOWN:"+simState);
                    break;
                case TelephonyManager.SIM_STATE_ABSENT:
                    Log.e(TAG,"SIM_STATE_ABSENT:"+simState);
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    Log.e(TAG,"SIM_STATE_PIN_REQUIRED:"+simState);
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    Log.e(TAG,"SIM_STATE_PUK_REQUIRED:"+simState);
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    Log.e(TAG,"SIM_STATE_NETWORK_LOCKED:"+simState);
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    Log.e(TAG,"SIM_STATE_UNKNOWN:"+simState);
                    break;

            }
        }

    }

}
