package com.play.robot.util.rxbus.rxbusEvent;

public class ChangeEvent {
    String ip;
    int port;
    int type;
    String rtsp;
    String number;

    public ChangeEvent(String ip, int port, int type,String number, String rtsp) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.number = number;
        this.rtsp = rtsp;
    }

    public String getNumber() {
        return number;
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
