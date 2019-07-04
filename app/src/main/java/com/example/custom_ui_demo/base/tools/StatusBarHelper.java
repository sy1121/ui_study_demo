package com.example.custom_ui_demo.base.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by robincxiao on 2017/3/21.
 */

public class StatusBarHelper {
    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;

        return isMIUI;
    }

    /**StatusBar
     * 初始化
     * @param activity
     * @param resColorId StatusBar的颜色
     * @param darkTitleBar 顶部title是否是深色(如果是浅色，那么就需要修改StatusBar上的icon为深色，防止看不清)
     */
    public static void initStatusBar(Activity activity, int resColorId, boolean darkTitleBar){
        /**
         * 沉浸式效果设置
         * 一、MIUI系统：
         * 5.0以上：完全沉浸式效果
         * 4.4到5.0：StatusBar背景是灰色的
         * 其它：无效果
         * 二、其它系统：
         * 6.0以上:完全沉浸式效果
         * 其它：无效果
         */
        if(isMIUI()){
            if(Build.VERSION.SDK_INT >= 21){
                setStatusBarColor(activity, resColorId);
            }else if(Build.VERSION.SDK_INT >= 19){
                setTranslucentStatus(activity, true);
            }

            setStatusBarDarkMode(activity, !darkTitleBar);
        }else {
            if(Build.VERSION.SDK_INT >= 23){
                setStatusBarColor(activity, resColorId);
            }
            //注：setDarkStatusIcon需在setContentView后执行
            setDarkStatusIcon(activity, !darkTitleBar);
        }
    }

    /**
     * 设置Status的背景颜色(系统方法)
     */
    public static void setStatusBarColor(Activity activity, int resId){
        if(Build.VERSION.SDK_INT >= 21) {//5.0以上才支持
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(resId));// SDK21
        }
    }


    /**
     * 设置状态栏上icon的颜色(系统方法)
     * @param bDark
     */
    public static void setDarkStatusIcon(Activity activity, boolean bDark) {//6.0以上才支持
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //设置状态栏上icon的颜色
            View decorView = activity.getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();
                if(bDark){
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else{
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }


    /**
     * miuiv6只支持4.4及以上版本，调用状态栏透明的方法可以直接用原生的安卓方法(系统方法)
     * @param on
     */
    @TargetApi(19)
    protected static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * MIUI 6上调用setDarkStatusIcon是无效的，MIUI自定义了其效果，采用如下方法调用
     * @param darkmode
     * @param activity
     */
    public static void setStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
