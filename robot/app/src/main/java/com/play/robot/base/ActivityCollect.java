package com.play.robot.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.play.robot.view.home.HomeActivity;

import java.util.List;
import java.util.Stack;

/**
 * Created by LGQ
 * Time: 2018/7/19
 * Function: activity 统一管理
 */
public class ActivityCollect {
    private static Stack<Activity> activityStack;
    private static ActivityCollect instance;

    private ActivityCollect() {
    }

    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static ActivityCollect getAppCollect() {
        if (instance == null) {
            instance = new ActivityCollect();
        }
        return instance;
    }


    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 结束除homeActivity外的所有activity
     */
    public void finishAllNotHome() {
        for (Activity activity : activityStack) {
            if (!activity.getClass().equals(HomeActivity.class)) {
                activity.finish();
            }
        }

    }


    /**
     * 获取指定activity 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 判斷當前activity是否在堆棧倒數第二個
     *
     * @param cls
     * @return
     */
    public boolean isLastActivity(Class<?> cls) {
        int position = activityStack.size() - 2;
        if (position < 0) {
            return false;
        }
        if (cls == activityStack.get(position).getClass()) {
            return true;
        }
        return false;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    public void clearTop(Class<?> cls) {
        int i = 0;
        for (; i < activityStack.size(); i++) {
            if (cls.equals(activityStack.get(i).getClass())) {
                break;
            }
        }

        int startPosition = i + 1, endPosition = activityStack.size();

        if (startPosition < activityStack.size() && endPosition >= startPosition) {
            List<Activity> list = activityStack.subList(startPosition, endPosition);
            if (null != list && !list.isEmpty()) {
                for (Activity acts : list) {
                    acts.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
