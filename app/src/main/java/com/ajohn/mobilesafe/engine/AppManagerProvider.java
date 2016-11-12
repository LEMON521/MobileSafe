package com.ajohn.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.ajohn.mobilesafe.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ApplicationInfo.FLAG_EXTERNAL_STORAGE;
import static android.content.pm.ApplicationInfo.FLAG_SYSTEM;

/**
 * 此类在于获取应用程序的信息
 * Created by lemon on 2016/11/12.
 */

public class AppManagerProvider {
    /**
     * 获取到全部应用程序的信息
     *
     * @param context
     * @return List<AppInfo>:
     * AppInfo：应用程序图标；名称；包名；安装位置；程序类型（系统程序／用户程序）
     */
    public static List<AppInfo> getAllAppInfos(Context context) {

        ArrayList<AppInfo> infos = new ArrayList<AppInfo>();

        //得到包管理器
        PackageManager pm = context.getPackageManager();

        /**
         * TODO 下面二者的不同之处：
         *
         * getInstalledPackages(0)：返回的是已经安装的程序包列表返回List
         *
         * getInstalledApplications(0)：返回全部的安装程序，包括已经删除的
         */
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(0);


        for (PackageInfo packageInfo : installedPackages) {
            AppInfo info = new AppInfo();

            String packageName = packageInfo.packageName;
            info.setPackName(packageName);
            Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
            info.setIcon(icon);
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();
            info.setName(name);

            //applicationInfo:从<应用>标记收集的信息,如果没有或null。
            int flags = packageInfo.applicationInfo.flags;

            //TODO flags&FLAG_SYSTEM 这是一个知识点--答题卡例子
            info.setSystem((flags & FLAG_SYSTEM) == 1);
            info.setRom((flags & FLAG_EXTERNAL_STORAGE) == 1);


            infos.add(info);
        }

        return infos;
    }
}
