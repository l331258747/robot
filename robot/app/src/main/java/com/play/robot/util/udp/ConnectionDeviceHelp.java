package com.play.robot.util.udp;

import com.play.robot.bean.ConnectionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGQ
 * Time: 2018/8/24
 * Function:
 */

public class ConnectionDeviceHelp {

    private final static List<ConnectionBean> deviceList = new ArrayList<>();

    private ConnectionDeviceHelp() {
    }

    private final static class HolderClass {
        private final static ConnectionDeviceHelp INSTANCE = new ConnectionDeviceHelp();

    }

    public static ConnectionDeviceHelp getInstance() {
        return HolderClass.INSTANCE;
    }

    public List<ConnectionBean> getDeviceList() {
        return deviceList;
    }

    public void addDevice(UdpClient udpClient, String ip, int port) {
        for (int i = 0; i < deviceList.size(); i++) {
            if ((deviceList.get(i).getIpPort()).equals(ip + ":" + port))
                return;
        }
        ConnectionBean item = new ConnectionBean();
        item.setIp(ip);
        item.setPort(port);
        item.setUdpClient(udpClient);
        deviceList.add(item);
    }

    public void removeDevice(String ip, int port) {
        for (int i = 0; i < deviceList.size(); i++) {
            if ((deviceList.get(i).getIpPort()).equals(ip + ":" + port)){
                deviceList.get(i).getUdpClient().disconnect();
                deviceList.remove(i);
            }

        }
    }

    public ConnectionBean getSingleBean(){
        if(getDeviceList() == null || getDeviceList().size() == 0){
            return null;
        }
        return getDeviceList().get(0);
    }

}
