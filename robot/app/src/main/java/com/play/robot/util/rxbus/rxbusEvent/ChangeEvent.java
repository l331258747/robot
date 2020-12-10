package com.play.robot.util.rxbus.rxbusEvent;

public class ChangeEvent {
    String ip;
    int port;
    int type;
    String rtsp;

    public ChangeEvent(String ip, int port, int type,String rtsp) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.rtsp = rtsp;
    }

    public String getRtsp() {
        return rtsp;
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
