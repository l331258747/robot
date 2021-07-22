package com.play.robot.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGQ
 * Time: 2018/8/24
 * Function:
 */

public class SettingInfo {

    public static boolean isCameraUp;
    public static boolean isCameraSide;
    public static boolean isCameraAfter;
    public static boolean isCameraUpZt;
    public static boolean isCameraUpZtSb;
    public static boolean isCamera6;
    public static boolean isCamera7;

    public static boolean isRouteFh;
    public static boolean isRouteNxfh;
    public static boolean isRouteSxfh;

    public static boolean isBatteryFh;
    public static int batteryBj;

    public static String moreJd;
    public static String moreWd;
    public static String moreJl;

    public static String shapeMode;
    public static String shapeZkc;
    public static String shapeZkcBH;
    public static List<String> shapeList;


    public static void initData(){
        isCameraUp = true;
        isCameraSide =false;
        isCameraAfter = false;
        isCameraUpZt = false;
        isCameraUpZtSb = false;
        isCamera6 = false;
        isCamera7 = false;

        isRouteFh =false;
        isRouteNxfh =false;
        isRouteSxfh =false;

        isBatteryFh = false;
        batteryBj = 0;

        moreJd = "0";
        moreWd = "0";
        moreJl = "0";

        shapeMode = "";
        shapeZkc = "";
        shapeZkcBH = "";
        shapeList = new ArrayList<>();
    }

}
