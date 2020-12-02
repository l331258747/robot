package com.play.robot.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGQ
 * Time: 2018/8/24
 * Function:
 */

public class MyDeviceList {

    private final static List<String> deviceList = new ArrayList<>();

    private MyDeviceList() {
    }

    private final static class HolderClass {
        private final static MyDeviceList INSTANCE = new MyDeviceList();

    }

    public static MyDeviceList getInstance() {
        return HolderClass.INSTANCE;
    }

    public List<String> getDeviceList() {
        return deviceList;
    }

    public void addDevice(String ip) {
        deviceList.add(ip);
    }

    public void removeDevice(String ip) {
        deviceList.remove(ip);
    }
}
