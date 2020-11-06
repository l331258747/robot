package com.play.robot.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;

import com.play.robot.R;
import com.play.robot.util.StatusBarUtil;
import com.play.robot.util.ToastUtil;
import com.play.robot.view.home.HomeActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by LGQ
 * Time: 2018/7/19
 * Function:
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    public Activity activity;
    public Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.setContentView(getLayoutId()); // 注意此处设置带title的布局

        context = this;
        activity = this;

        ActivityCollect.getAppCollect().addActivity(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.color_1A1919));

        /////////////////
        getIntentData();
        initView();
        initData();
        /////////////////

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    private long mExitTime;
    @Override
    public void onBackPressed() {
        if(activity instanceof HomeActivity){
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showLongToast("再按一次退出系统");
                mExitTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();
    }


    /**
     * 获取传递的参数
     **/
    public void getIntentData() {
        intent = getIntent();
    }

    /**
     * 获取布局 Layout
     **/
    public abstract int getLayoutId();

    /**
     * 初始化 View
     **/
    public abstract void initView();

    /**
     * 初始化数据
     **/
    public abstract void initData();

    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    public <T extends View> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

    public void showShortToast(String msg) {
        ToastUtil.showShortToast(context, msg);
    }

    public void showLongToast(String msg) {
        ToastUtil.showLongToast(this, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollect.getAppCollect().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;
    }

    //-------------start 适配 字体
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
    //-------------end 适配 字体

}
