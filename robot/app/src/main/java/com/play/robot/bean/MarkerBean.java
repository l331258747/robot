package com.play.robot.bean;

//途经点marker数据
public class MarkerBean {

    double longitude;
    double latitude;
    int type;
    int num;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNumStr() {
        if (type == 0)
            return "起";
        else if (type == -1)
            return "终";
        else
            return num + "";
    }
}
