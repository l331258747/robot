package com.play.robot.util.rxbus.rxbusEvent;

public class ConnectIpEvent {
    String ip;
    int port;
    int type;

    public ConnectIpEvent(String ip, int port, int type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getIpPort() {
        return ip + ":" + port;
    }
}
