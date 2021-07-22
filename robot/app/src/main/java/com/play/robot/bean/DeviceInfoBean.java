package com.play.robot.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class DeviceInfoBean {

    String carNo;
    String speed;
    String gear;
    String lng;
    String lat;
    String status;
    String distance;

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getStatusStr() {
        if (status.equals("1")) {
            return "目标暂时丢失但还保留历史信息";
        } else if (status.equals("2")) {
            return "初始化失败";
        }else if (status.equals("3")) {
            return "确认目标丢失，停车";
        }
        return "有目标";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";
        if(!TextUtils.isEmpty(carNo)){
            str = str  + "编号:" + carNo;
        }
        if(!TextUtils.isEmpty(speed)){
            str = str  + "\nV:" + speed + "m/s";
        }
        if(!TextUtils.isEmpty(gear)){
            str = str  + "\nD:" + gear + "m";
        }
        if(!TextUtils.isEmpty(lng)){
            str = str  + "\n经度:" + lng;
        }
        if(!TextUtils.isEmpty(lat)){
            str = str  + "\n维度:" + lat;
        }
        if(!TextUtils.isEmpty(status)){
            str = str  + "\n跟随状态:" + getStatusStr();
        }
        if(!TextUtils.isEmpty(distance)){
            str = str  + "\n目标距离:" + distance;
        }
        return str;
    }
}
