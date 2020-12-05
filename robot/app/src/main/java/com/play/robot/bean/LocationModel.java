package com.play.robot.bean;

/**
 * Created by LGQ
 * Time: 2018/9/11
 * Function:
 */

public class LocationModel {

    String city;
    String cityChild;
    String error;
    double longitude;
    double latitude;

    public LocationModel(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationModel(String error) {
        this.error = error;
    }

    public String getCity() {
        return city;
    }


    public String getCityChild() {
        return cityChild;
    }

    public String getError() {
        return error;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
