package com.play.robot.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import androidx.core.content.FileProvider;

/**
 * Created by LGQ
 * Time: 2018/7/17
 * Function: 获取application context
 */
public class AppUtils {

    private static Context context;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        AppUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }


    /**
     * 得到版本号code
     */
    public static String getVersionCode() {
        if (getPackageInfo() == null) return null;
        return String.valueOf(getPackageInfo().versionCode);
    }

    public static int getVersionCodeInt() {
        if (getPackageInfo() == null) return 0;
        return getPackageInfo().versionCode;
    }

    /**
     * 得到版本name
     */
    public static String getVersionName() {
        if (getPackageInfo() == null) return null;
        return String.valueOf(getPackageInfo().versionName);
    }

    /**
     * 得到包名
     */
    public static String getPakgeName() {
        if (getPackageInfo() == null) return null;
        return getPackageInfo().packageName;
    }

    private static PackageInfo getPackageInfo() {
        try {
            PackageManager packageManager = getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕尺寸，但是不包括虚拟功能高度
     *
     * @return
     */
    public static int getNoHasVirtualKey(Activity activity) {
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return
     */
    private static int getHasVirtualKey(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    //底部虚拟按钮 高度
    public static int getNavigationBarHeight(Activity activity) {
        return getHasVirtualKey(activity) - getNoHasVirtualKey(activity);
    }

    //获取屏幕宽度
    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //获取屏幕高度
    public static int getDisplayHeight(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    public static String getManifestValue(String manifestName) {
        String manifestValue = "";
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getApplicationContext().getPackageManager().getApplicationInfo(getPakgeName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                manifestValue = appInfo.metaData.getString(manifestName);
            } else {
                LogUtil.e("需要在AndroidManifest.xml中配置" + manifestName + " meta数据");
                manifestValue = "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            manifestValue = "";
        }
        LogUtil.e("manifestValue:" + manifestValue);
        return manifestValue;
    }

    public static String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return null;
    }

    public static  void installApk(Context context,String downloadApk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(downloadApk);
        LogUtil.e("安装路径=="+downloadApk);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LogUtil.e("AppUtils.getPakgeName():"+AppUtils.getPakgeName());
            Uri apkUri = FileProvider.getUriForFile(context, AppUtils.getPakgeName()+".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }

}