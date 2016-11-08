package com.ajohn.mobilesafe.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import static android.location.LocationProvider.AVAILABLE;
import static android.location.LocationProvider.OUT_OF_SERVICE;
import static android.location.LocationProvider.TEMPORARILY_UNAVAILABLE;


/**
 * Created by John on 2016/10/24.
 */

public class GpsLocationService extends Service {

    private String TAG = "GpsLocationService";
    //    private SmsManager sendSms;
    private LocationManager lm;
    private SharedPreferences sp;
    private MyLocationListener listener;

    private String safeNumber = "";

    /**
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //实例化位置管理器(位置服务)
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        safeNumber = sp.getString("safeNumber", "");

        listener = new MyLocationListener();
        //获取到做好的定位方式
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // TODO 注意啦,如果用户自己关闭GPS,那么聚会出现 bestProvider=null的情况,应用程序就会停止运行,在真机上不知道会不会根据网络定位,模拟器上模拟不出来
        String bestProvider = lm.getBestProvider(criteria, true);

        if (bestProvider == null) {
            SmsManager.getDefault().sendTextMessage(safeNumber,"","The GPS is down!",null,null);
            Log.e(TAG, "bestProvider  is   null");
            Intent GPSIntent = new Intent();
            GPSIntent.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
            GPSIntent.setData(Uri.parse("custom:3"));
            try {
                PendingIntent.getBroadcast(this, 0, GPSIntent, 0).send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        } else {
            lm.requestLocationUpdates(bestProvider, 0, 0, listener);
        }


    }

    private class MyLocationListener implements LocationListener {

        /**
         * Called when the location has changed.
         * <p>
         * <p> There are no restrictions on the use of the supplied Location object.
         *
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "位置发生了变化");
            StringBuffer sb = new StringBuffer();
            sb.append("getAccuracy()" + location.getAccuracy() + "\n");//精度
            sb.append("getAltitude()" + location.getAltitude() + "\n");
            sb.append("getBearing()" + location.getBearing() + "\n");
            sb.append("getExtras()" + location.getExtras() + "\n");//bundle数据
            sb.append("getLatitude()" + location.getLatitude() + "\n");//纬度
            sb.append("getLatitude()" + location.getLongitude() + "\n");//经度
            sb.append("getProvider()" + location.getProvider() + "\n");//定位方式
            sb.append("getSpeed()" + location.getSpeed() + "\n");
            sb.append("getTime()" + location.getTime() + "\n");//定位时的  时间点
            Log.e(TAG, sb.toString());
            SmsManager.getDefault().sendTextMessage(safeNumber, null,
                    "当前位置:经度:" + location.getLongitude() + "\n纬度:" + location.getLatitude() + "\n精度:" + location.getTime() + "\n时间:" + location.getTime(), null, null);

        }

        /**
         * 定位方式改变的时候
         * Called when the provider status changes. This method is called when
         * a provider is unable to fetch a location or if the provider has recently
         * become available after a period of unavailability.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
         *                 provider is out of service, and this is not expected to change in the
         *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
         *                 the provider is temporarily unavailable but is expected to be available
         *                 shortly; and {@link LocationProvider#AVAILABLE} if the
         *                 provider is currently available.
         * @param extras   an optional Bundle which will contain provider specific
         *                 status variables.
         *                 <p>
         *                 <p> A number of common key/value pairs for the extras Bundle are listed
         *                 below. Providers that use any of the keys on this list must
         *                 provide the corresponding value as described below.
         *                 <p>
         *                 <ul>
         *                 <li> satellites - the number of satellites used to derive the fix
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case OUT_OF_SERVICE:
                    Log.e(TAG, "当前定位方式为:" + provider);
                    break;
                case TEMPORARILY_UNAVAILABLE:
                    Log.e(TAG, "当前定位方式为:" + provider);
                    break;
                case AVAILABLE:
                    Log.e(TAG, "当前定位方式为:" + provider);
                    break;
            }
        }

        /**
         * 调试程序的时候
         * Called when the provider is enabled by the user.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled():" + provider);
        }

        /**
         * 用户禁止程序使用定位时
         * Called when the provider is disabled by the user. If requestLocationUpdates
         * is called on an already disabled provider, this method is called
         * immediately.
         *
         * @param provider the name of the location provider associated with this
         *                 update.
         */
        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled():" + provider);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);
        listener = null;
    }

}
