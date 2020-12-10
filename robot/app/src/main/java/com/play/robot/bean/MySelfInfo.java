package com.play.robot.bean;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.play.robot.util.GsonUtil;
import com.play.robot.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGQ
 * Time: 2018/8/24
 * Function:
 */

public class MySelfInfo {

    private MySelfInfo() {
    }

    private final static class HolderClass {
        private final static MySelfInfo INSTANCE = new MySelfInfo();
    }

    public static MySelfInfo getInstance() {
        return HolderClass.INSTANCE;
    }

    public boolean isLogin() {
        if (getUserId() != 0) {
            return true;
        }
        return false;
    }

    public void setData(LoginBean model) {

        SPUtils.getInstance().putInt(SPUtils.SP_USER_ID, model.getUserId());
        SPUtils.getInstance().putString(SPUtils.SP_PASSWORD, model.getPassword());
        SPUtils.getInstance().putString(SPUtils.SP_USER_NAME, model.getUserName());
    }

    public int getUserId() {
        return SPUtils.getInstance().getInt(SPUtils.SP_USER_ID);
    }

    public String getPassword() {
        return SPUtils.getInstance().getString(SPUtils.SP_PASSWORD);
    }

    public String getUserName() {
        return SPUtils.getInstance().getString(SPUtils.SP_USER_NAME);
    }


    public void setDevice(List<DeviceInfo> lists) {
        if (lists == null) return;
        SPUtils.getInstance().putString(SPUtils.SP_DEVICE, GsonUtil.convertVO2String(lists));
    }

    public void addDevice(String ip, int port,String rtsp) {
        String ipPort = ip + ":" + port;
        List<DeviceInfo> results = new ArrayList<>();

        List<DeviceInfo> lists = getDevice();
        for (DeviceInfo item : lists) {
            if (TextUtils.equals(ipPort, item.getIpPort())) {
                lists.remove(item);
                break;
            }
        }

        while (lists.size() > 5) {
            lists.remove(lists.size() - 1);
        }

        DeviceInfo data = new DeviceInfo();
        data.setIp(ip);
        data.setPort(port);
        data.setRtsp(rtsp);

        results.add(data);
        results.addAll(lists);

        setDevice(results);
    }

    public void removeDevice(String ip,int port) {
        String ipPort = ip + ":" + port;
        List<DeviceInfo> results = getDevice();
        for (DeviceInfo item : results) {
            if (TextUtils.equals(ipPort, item.getIpPort())) {
                results.remove(item);
                break;
            }
        }
        setDevice(results);
    }

    public List<DeviceInfo> getDevice() {
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPUtils.SP_DEVICE)))
            return new ArrayList<>();
        return GsonUtil.convertString2Collection(SPUtils.getInstance().getString(SPUtils.SP_DEVICE), new TypeToken<List<DeviceInfo>>() {
        });
    }

}
