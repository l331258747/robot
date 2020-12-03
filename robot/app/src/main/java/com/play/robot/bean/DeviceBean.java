package com.play.robot.bean;

public class DeviceBean {

    private String ip;
    private int port;
    private int type;

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
