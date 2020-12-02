package com.play.robot.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import androidx.annotation.Nullable;

/**
 * Created by LGQ
 * Time: 2018/7/17
 * Function:SharedPreferences 工具类
 */
public class SPUtils {

    public static final String SP_USER_ID = "sp_user_id";
    public static final String SP_PASSWORD = "sp_password";
    public static final String SP_USER_NAME = "sp_user_name";
    public static final String SP_DEVICE = "sp_device";

    private static SharedPreferences mSharedPreferences;
    private static SPUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    public static final String PREFERENCE_NAME = "com.robot_preferences";

    public static synchronized void init(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new SPUtils(cxt);
        }
    }

    private SPUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static SPUtils getInstance() {
        if (mPreferenceUtils == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferenceUtils;
    }


    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key,
                            String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }


    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public boolean putLong(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key) {
        return getLong(key, -1);
    }


    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public boolean putFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }


    public float getFloat(String key) {
        return getFloat(key, -1);
    }


    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }


    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }


    public boolean getBoolean(String key,
                              boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }


    public void put(String key, @Nullable Set<String> values) {
        editor.putStringSet(key, values).apply();
    }


    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }


    public Set<String> getStringSet(String key, @Nullable Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }


    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void clearAll() {
        editor.clear().commit();
    }

}
