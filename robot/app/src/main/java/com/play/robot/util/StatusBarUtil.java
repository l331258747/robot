package com.play.robot.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * Created by LGQ
 * Time: 2019/2/20
 * Function:
 */

public class StatusBarUtil {
    /**
     * Android 6.0 以上设置状态栏颜色
     */
    public static void setStatusBar(Activity context, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色颜色
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context.getWindow().setStatusBarColor(color);

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }else if(isColorOS_3()){//oppo
            setOppoStausBar(context,true,false);
        }
    }

    private static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static boolean isColorOS_3() {
        try {
            Class<?> cla = Class.forName("android.os.SystemProperties");
            Method mtd = cla.getMethod("get", String.class);
            String val = (String) mtd.invoke(null, KEY_VERSION_OPPO);
            val = val.replaceAll("[vV]", "");
            val = val.substring(0, 1);
            int version = Integer.parseInt(val);
            return version >= 3;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void setOppoStausBar(Activity context, boolean isDarkText,boolean isTranslate){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //控制字体颜色，只有黑白两色
            int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = isDarkText ? 0x00000010 : 0x00190000;
            Window window = context.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility((isTranslate ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : 0) | SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); //可有可无
            window.setStatusBarColor(isTranslate ? Color.TRANSPARENT : Color.WHITE);
        }
    }

}
