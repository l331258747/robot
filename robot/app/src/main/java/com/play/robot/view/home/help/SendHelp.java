package com.play.robot.view.home.help;

import com.play.robot.MyApplication;
import com.play.robot.bean.MarkerBean;
import com.play.robot.util.AppUtils;
import com.play.robot.util.LngLonUtil;
import com.play.robot.util.ToastUtil;

import java.util.List;

public class SendHelp {

    //----------------指令 start----------
    static StringBuilder msg = new StringBuilder();

    //方向
    public static void sendRocker(String number,String ipPort, int upLevel, int turnLevel) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",1,1");
        msg.append("," + upLevel);
        msg.append("," + turnLevel);

        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //急刹 $1,1,3
    public static void sendJS(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",1,3");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"急刹-发送成功");
    }

    //熄火 $1,1,6,0
    public static void sendXH(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",1,6,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"熄火-发送成功");
    }

    //启动 $1,1,6,1
    public static void sendQD(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",1,6,1");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"启动-发送成功");
    }

    public static void sendMsg(String ipPort, String str) {
        msg.setLength(0);
        msg.append(str);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"发送成功");
    }

    //智能控车模式 摇杆
    public static void sendRockerAngle(String number,String ipPort, int x, int y, int w, int h) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",2,3");
        msg.append("," + x);
        msg.append("," + y);
        msg.append("," + w);
        msg.append("," + h);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"屏幕中准心-发送成功");
    }

    //跟随模式
    //初始化跟人
    public static void sendTrackPeopleInit(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,1,1,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //初始化跟车
    public static void sendTrackCarInit(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,1,2,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //开始跟人
    public static void sendTrackPeopleStart(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,1,3,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"开始跟人");
    }

    //开始跟车
    public static void sendTrackCarStart(String number,String ipPort) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,1,4,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"开始跟车");
    }

    //一键清除
    public static void sendTrackQc(String number,String ipPort, boolean isOK) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,2");
        msg.append(isOK ? ",1" : ",0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),isOK?"开启一键清除":"关闭一键清除");
    }

    //跟踪轨迹
    public static void sendTrackGj(String number,String ipPort, boolean isOK) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,6");
        msg.append(isOK ? ",1" : ",0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),isOK?"开启跟踪轨迹":"关闭跟踪轨迹");
    }

    //自主跟随模式下的加减速
    public static void sendTrackSpeed(String number,String ipPort, int num) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,3");
        msg.append("," + num);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //自主跟随模式下的跟随距离调节
    public static void sendTrackSpace(String number,String ipPort, int num) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",6,4");
        msg.append("," + num);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());

    }

    //marker点
    public static void sendMarker(String number,String ipPort, List<MarkerBean> markers) {
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",8,1");
        msg.append("," + markers.size());
        for (int i = 0; i < markers.size(); i++) {

            double[] ds = LngLonUtil.bd09_To_gps84(markers.get(i).getLatitude(),markers.get(i).getLongitude());

            msg.append("," + ds[1]);
            msg.append("," + ds[0]);
//            msg.append("," + markers.get(i).getLongitude());
//            msg.append("," + markers.get(i).getLatitude());
            int type = markers.get(i).getType();
            String typeStr = "3";//起点
            if(type == 1){//必经点
                typeStr = "1";
            }else if(type == 2){//约束点
                typeStr = "2";
            }else if(type == -1){//终点
                typeStr = "4";
            }
            msg.append("," + typeStr);
        }
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),"任务点-发送成功");
    }

    //自主模式 - 一键返航
    public static void sendFh(String number,String ipPort,int type,String typeStr,int sort){
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",8,2");
        msg.append("," + type);
        msg.append("," + sort);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),typeStr);
    }

    public static void sendCamera(String number,String ipPort, int type){
        msg.setLength(0);
        msg.append("$" + number);
        msg.append(",9,1");
        msg.append("," + type);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
        ToastUtil.showShortToast(AppUtils.getContext(),getCameraStr(type));
    }


    public static String getCameraStr(int type){
        switch (type){
            case 1:
                return "切换摄像头:前置摄像头";
            case 2:
                return "切换摄像头:前置+态资";
            case 3:
                return "切换摄像头:前置+态资+目标识别";
            case 4:
                return "切换摄像头:侧视摄像头";
            case 5:
                return "切换摄像头:后视摄像头";
            case 6:
                return "切换摄像头:摄像头6";
            case 7:
                return "切换摄像头:摄像头7";
        }
        return "切换摄像头成功";
    }
}
