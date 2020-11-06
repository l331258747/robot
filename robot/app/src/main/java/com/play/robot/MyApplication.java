package com.play.robot;

import android.app.Application;
import android.content.Context;

import com.play.robot.util.AppUtils;
import com.play.robot.util.LogUtil;
import com.play.robot.util.SPUtils;

public class MyApplication extends Application {

    private static MyApplication instance;

    private static Context context;

    /**
     * 屏幕尺寸
     */
    public static int displayWidth = 0;
    public static int displayHeight = 0;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();

        if (displayWidth <= 0) {
            displayWidth = getResources().getDisplayMetrics().widthPixels;
        }

        if (displayHeight <= 0) {
            displayHeight = getResources().getDisplayMetrics().heightPixels;
        }

        SPUtils.init(context);
        AppUtils.init(this);
        LogUtil.setShowLog(true);

    }

}
