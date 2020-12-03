package com.play.robot.bean;

import com.play.robot.util.udp.UdpClient;

public class ConnectionBean {
    UdpClient mUdpClient;
    String ip;
    int port;
    boolean isLead;

    public boolean isLead() {
        return isLead;
    }

    public void setLead(boolean lead) {
        isLead = lead;
    }

    public UdpClient getUdpClient() {
        return mUdpClient;
    }

    public void setUdpClient(UdpClient udpClient) {
        mUdpClient = udpClient;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpPort(){
        return ip + ":" + port;
    }
}
