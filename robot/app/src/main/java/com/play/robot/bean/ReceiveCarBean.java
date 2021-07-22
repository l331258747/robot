package com.play.robot.bean;

public class ReceiveCarBean {

    //    n1----速度  m/s
//n2----档位
//N3----底层控制器错误码
//N4----电量
//N5----油量
//N6----车体当前经度
//N7----车体当前纬度
//N8----车体方向角
//N9----车体横滚角
//N10----车体俯仰角
//N11----当前车辆模型名

    String ip;
    int port;
    String carNo;

    String info;//模块编号

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getIpPort() {
        return ip + ":" + port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
