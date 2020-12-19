package com.play.robot.bean;

public class MyListBean {
    int id;
    String content;
    String ip;
    int port;



    public String getIpPort() {
        return ip + ":" + port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
