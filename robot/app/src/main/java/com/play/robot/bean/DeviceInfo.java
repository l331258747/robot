package com.play.robot.bean;

//缓存ip数据
public class DeviceInfo {

    private String ip;
    private int port;
    private String rtsp;

    public String getRtsp() {
        return rtsp;
    }

    public void setRtsp(String rtsp) {
        this.rtsp = rtsp;
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
