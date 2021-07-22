package com.play.robot.util.rxbus.rxbusEvent;

public class ReceiveCarEvent {

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
    String n3;
    String n4;
    String n5;
    String n6;
    String n7;
    String n8;
    String n9;
    String n10;
    String n11;

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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getN3() {
        return n3;
    }

    public void setN3(String n3) {
        this.n3 = n3;
    }

    public String getN4() {
        return n4;
    }

    public int getN4Int() {
        try {
            return Integer.parseInt(n4);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setN4(String n4) {
        this.n4 = n4;
    }

    public String getN5() {
        return n5;
    }

    public void setN5(String n5) {
        this.n5 = n5;
    }

    public String getN6() {
        return n6;
    }

    public float getN6Int() {
        try {
            return Float.parseFloat(n6);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setN6(String n6) {
        this.n6 = n6;
    }

    public String getN7() {
        return n7;
    }

    public float getN7Int() {
        try {
            return Float.parseFloat(n7);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setN7(String n7) {
        this.n7 = n7;
    }

    public String getN8() {
        return n8;
    }

    public float getN8Float() {
        try {
            return Float.parseFloat(n8);
        } catch (Exception e) {
            return 0;
        }
    }
    public int getN8Int() {
        return (int) getN8Float();
    }

    public void setN8(String n8) {
        this.n8 = n8;
    }

    public String getN9() {
        return n9;
    }

    public void setN9(String n9) {
        this.n9 = n9;
    }

    public String getN10() {
        return n10;
    }

    public void setN10(String n10) {
        this.n10 = n10;
    }

    public String getN11() {
        return n11;
    }

    public void setN11(String n11) {
        this.n11 = n11;
    }
}
