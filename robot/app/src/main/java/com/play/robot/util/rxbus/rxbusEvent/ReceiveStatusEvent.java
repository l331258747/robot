package com.play.robot.util.rxbus.rxbusEvent;

public class ReceiveStatusEvent {

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
    String mode;//模块编号
    String task;//任务编号
    String n1;//速度
    String n2;

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getN1() {
        return n1;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN2() {
        return n2;
    }

    public void setN2(String n2) {
        this.n2 = n2;
    }
}
