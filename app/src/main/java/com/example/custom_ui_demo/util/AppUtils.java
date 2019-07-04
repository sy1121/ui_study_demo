package com.example.custom_ui_demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

//跟App相关的辅助类
public class AppUtils {
    private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25;
    private static String channel = "unknown";

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getAppIcon(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;

        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }

        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        //BitmapDrawable bd = (BitmapDrawable) d;

        return d;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取设备分辨率
     * @param context
     * @return
     */
    public static float getDensity(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

    /**
     * Android SDK版本
     * @return
     */
    public static int getSDKVersion()
    {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> clazz = null;
        Object obj = null;
        Field field = null;
        int statusBarHeight = DensityUtils.dp2px(context,
                STATUS_BAR_DEFAULT_HEIGHT_DP);
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            if (isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (field != null && obj != null) {
            try {
                int id = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(
                        id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (statusBarHeight <= 0
                || statusBarHeight > DensityUtils.dp2px(context,
                STATUS_BAR_DEFAULT_HEIGHT_DP * 2)) {
            // 安卓默认状态栏高度为25dp，如果获取的状态高度大于2倍25dp的话，这个数值可能有问题，用回默认值。出现这种可能性较低，只有小部分手机出现
            float density = context.getResources().getDisplayMetrics().density;
            if (density == -1) {
                statusBarHeight = DensityUtils.dp2px(context,
                        STATUS_BAR_DEFAULT_HEIGHT_DP);
            } else {
                statusBarHeight = (int) (STATUS_BAR_DEFAULT_HEIGHT_DP * density + 0.5f);
            }
        }
        return statusBarHeight;
    }

    public static boolean isMeizu() {
        String brand = Build.BRAND.toLowerCase();
        if (brand.contains("meizu")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static boolean isInstallApp(Context context, String packageName) {
        try {
            PackageManager mPackageManager = context.getPackageManager();
            mPackageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void startApp(Context context, String packageName){
        PackageManager packageManager = context.getPackageManager();
        Intent intent=new Intent();
        intent =packageManager.getLaunchIntentForPackage(packageName);
        if(intent != null){
            context.startActivity(intent);
        }
    }

    /**
     * 获取当前安装包的渠道号
     */
    public static String getChannel(Context context) {
        if(channel.contentEquals("unknown")){
            try {
                PackageManager packageManager = context.getApplicationContext()
                        .getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                        context.getPackageName(), PackageManager.GET_META_DATA);
                channel = applicationInfo.metaData.getString("CHANNEL");
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return channel;
    }
}
