package com.play.robot.view.home.help;

import com.play.robot.MyApplication;
import com.play.robot.bean.MarkerBean;

import java.util.List;

public class SendHelp {

    //----------------指令 start----------
    static StringBuilder msg = new StringBuilder();

    //方向
    public static void sendRocker(String ipPort, int upLevel, int turnLevel) {
        msg.setLength(0);

        msg.append("$1,1,1");
        msg.append("," + upLevel);
        msg.append("," + turnLevel);

        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //急刹 $1,1,3
    public static void sendJS(String ipPort) {
        msg.setLength(0);
        msg.append("$1,1,3");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //熄火 $1,1,6,0
    public static void sendXH(String ipPort) {
        msg.setLength(0);
        msg.append("$1,1,6,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //启动 $1,1,6,1
    public static void sendQD(String ipPort) {
        msg.setLength(0);
        msg.append("$1,1,6,1");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    public static void sendMsg(String ipPort, String str) {
        msg.setLength(0);
        msg.append(str);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //智能控车模式 摇杆
    public static void sendRockerAngle(String ipPort, int x, int y, int w, int h) {
        msg.setLength(0);
        msg.append("$1,2,3");
        msg.append("," + x);
        msg.append("," + y);
        msg.append("," + w);
        msg.append("," + h);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //跟随模式
    //初始化跟人
    public static void sendTrackPeopleInit(String ipPort) {
        msg.setLength(0);
        msg.append("$1,6,1,1,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //初始化跟车
    public static void sendTrackCarInit(String ipPort) {
        msg.setLength(0);
        msg.append("$1,6,1,2,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //开始跟人
    public static void sendTrackPeopleStart(String ipPort) {
        msg.setLength(0);
        msg.append("$1,6,1,3,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //开始跟车
    public static void sendTrackCarStart(String ipPort) {
        msg.setLength(0);
        msg.append("$1,6,1,4,0,0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //一键清除
    public static void sendTrackQc(String ipPort, boolean isOK) {
        msg.setLength(0);
        msg.append("$1,6,2");
        msg.append(isOK ? ",1" : ",0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //跟踪轨迹
    public static void sendTrackGj(String ipPort, boolean isOK) {
        msg.setLength(0);
        msg.append("$1,6,6");
        msg.append(isOK ? ",1" : ",0");
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //自主跟随模式下的加减速
    public static void sendTrackSpeed(String ipPort, int num) {
        msg.setLength(0);
        msg.append("$1,6,3");
        msg.append("," + num);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    //自主跟随模式下的跟随距离调节
    public static void sendTrackSpace(String ipPort, int num) {
        msg.setLength(0);
        msg.append("$1,6,4");
        msg.append("," + num);
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }

    public static void sendMarker(String ipPort, List<MarkerBean> markers) {
        msg.setLength(0);
        msg.append("$1,8,1");
        msg.append("," + markers.size());
        for (int i = 0; i < markers.size(); i++) {
            msg.append("," + markers.get(i).getLongitude());
            msg.append("," + markers.get(i).getLatitude());
            int type = markers.get(i).getType();
            msg.append("," + (type == 1 ? "3" : type == -1 ? "4" : "0"));
        }
        MyApplication.getInstance().sendMsg(ipPort, msg.toString());
    }
}
