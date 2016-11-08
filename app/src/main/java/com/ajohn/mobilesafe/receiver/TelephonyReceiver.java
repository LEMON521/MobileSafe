package com.ajohn.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ajohn.mobilesafe.db.dao.NumberAddressQueryDao;

import static android.content.ContentValues.TAG;

/**
 * 去电广播接收者
 * Created by John on 2016/10/24.
 */

public class TelephonyReceiver extends BroadcastReceiver {
    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();
        Log.e(TAG,number);
        String address = NumberAddressQueryDao.getAddess(number);
        Toast.makeText(context,address,Toast.LENGTH_LONG).show();
    }
}
