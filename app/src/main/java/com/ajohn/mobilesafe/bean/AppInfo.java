package com.ajohn.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by lemon on 2016/11/12.
 */
public class AppInfo {
    /**
     * 应用程序图标
     */
    private Drawable icon;
    /**
     * 应用程序名称
     */
    private String name;
    /**
     * 应用程序包名
     */
    private String packName;
    /**
     * 应用程序安装位置
     * true 安装在内部
     * false 用户程序
     */
    private boolean isRom;
    /**
     * 应用程序类型（系统程序或者用户程序）
     * true 系统程序
     * false 用户程序
     */
    private boolean isSystem;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "AppInfos{" +
                ", name='" + name + '\'' +
                ", packName='" + packName + '\'' +
                ", isRom=" + isRom +
                ", isSystem=" + isSystem +
                '}';
    }
}
