package com.play.robot.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by LGQ
 * Time: 2018/7/17
 * Function: log 工具类
 */

public class LogUtil {

    public static String tagPrefix = "";
    public static boolean showLog = true;

    public static void setShowLog(boolean showLog) {
        LogUtil.showLog = showLog;
    }

    /**
     * 得到tag（所在类.方法（L:行））
     *
     * @return
     */
    private static String generateTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = "%s.%s(L:%d)";
//        tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        tag = String.format(tag, new Object[]{"----LOG-----" + callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        //给tag设置前缀
        tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
        return tag;
    }

    public static void v(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.v(tag, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
//            Log.d(tag, msg);

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.d(tag, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, msg);// 打印剩余日志
            }
        }
    }

    public static void d(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.i(tag, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
//            Log.e(tag, msg, tr);

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.e(tag, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    Log.e(tag, logContent);
                }
                Log.e(tag, msg);// 打印剩余日志
            }
        }
    }

    public static void wtf(String msg) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if(TextUtils.isEmpty(msg))
            msg = "null";
        if (showLog) {
            String tag = generateTag();
            Log.wtf(tag, msg, tr);
        }
    }
}
