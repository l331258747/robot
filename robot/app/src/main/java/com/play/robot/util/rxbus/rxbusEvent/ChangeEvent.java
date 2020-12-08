package com.play.robot.util.rxbus.rxbusEvent;

public class ChangeEvent {
    String ip;
    int port;
    int type;

    public ChangeEvent(String ip, int port, int type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getType() {
        return type;
    }
}
