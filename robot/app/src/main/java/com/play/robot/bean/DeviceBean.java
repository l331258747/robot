package com.play.robot.bean;

import com.play.robot.util.udp.UdpClient;

//公用设备数据
public class DeviceBean {

    private String ip;
    private int port;
    private int type;
    UdpClient mUdpClient;
    boolean isLead;
    private String rtsp;

    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
    }

    public UdpClient getUdpClient() {
        return mUdpClient;
    }

    public void setUdpClient(UdpClient udpClient) {
        mUdpClient = udpClient;
    }

    public boolean isLead() {
        return isLead;
    }

    public void setLead(boolean lead) {
        isLead = lead;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIpPort() {
        return ip + ":" + port;
    }
}
