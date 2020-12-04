package com.play.robot;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.util.AppUtils;
import com.play.robot.util.LogUtil;
import com.play.robot.util.SPUtils;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.udp.UdpClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MyApplication extends Application {

    private static MyApplication instance;

    private static Context context;

    /**
     * 屏幕尺寸
     */
    public static int displayWidth = 0;
    public static int displayHeight = 0;

    //全局对象，用来控制使用
    List<DeviceBean> mDeviceBeans;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();

        if (displayWidth <= 0) {
            displayWidth = getResources().getDisplayMetrics().widthPixels;
        }

        if (displayHeight <= 0) {
            displayHeight = getResources().getDisplayMetrics().heightPixels;
        }


        SPUtils.init(context);
        AppUtils.init(this);
        LogUtil.setShowLog(true);

        initBaiduMap();

        initDeviceConnection();
    }


    private void initBaiduMap() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }


    Disposable disposable;

    public void initDeviceConnection() {
        //从缓存里面获取存在的ip
        mDeviceBeans = new ArrayList<>();
        for (int i = 0; i < MySelfInfo.getInstance().getDevice().size(); i++) {
            DeviceBean item = new DeviceBean();
            item.setIp(MySelfInfo.getInstance().getDevice().get(i).getIp());
            item.setPort(MySelfInfo.getInstance().getDevice().get(i).getPort());
            item.setType(0);
            mDeviceBeans.add(item);
        }


        //监听，如果断连了修改状态
        disposable = RxBus2.getInstance().toObservable(ConnectIpEvent.class, event -> {
            for (int i = 0; i < mDeviceBeans.size(); i++) {
                if (mDeviceBeans.get(i).getIpPort().equals(event.getIpPort())) {
                    if (event.getType() == -1) {
                        setDeviceType(2, event.getIp(),event.getPort());
                    }
                }
            }
        });
    }

    //添加设备，缓存和全局变量中添加
    public void addDevice(DeviceBean item) {
        mDeviceBeans.add(item);
        MySelfInfo.getInstance().addDevice(item.getIp(),item.getPort());
    }

    //删除设备，缓存和全局变量中删除
    public void removeDevice(DeviceBean item) {
        for (int i = 0; i < mDeviceBeans.size(); i++) {
            if (mDeviceBeans.get(i).getIpPort().equals(item.getIpPort())) {
                mDeviceBeans.remove(i);
                MySelfInfo.getInstance().removeDevice(item.getIp(),item.getPort());
            }

        }
    }

    //修改设备状态，0初始状态，1已连接，2断开连接
    public void setDeviceType(int type, String ip, int port) {//0正常，1连接，2断连
        String ipPort = ip + ":" + port;
        for (int i = 0; i < mDeviceBeans.size(); i++) {
            if (mDeviceBeans.get(i).getIpPort().equals(ipPort)) {
                mDeviceBeans.get(i).setType(type);

                if (type == 2) {
                    mDeviceBeans.get(i).getUdpClient().disconnect();
                } else if (type == 1) {
                    UdpClient mUdpClient = new UdpClient();
                    mUdpClient.connect(ip, port);
                    mDeviceBeans.get(i).setUdpClient(mUdpClient);
                }

                return;
            }
        }
    }

    //全局数据调用
    public List<DeviceBean> getDevices() {
        return mDeviceBeans;
    }

    //获取连接数
    public int getConnectionNum() {
        int num = 0;
        for (int i = 0; i < mDeviceBeans.size(); i++) {
            if (mDeviceBeans.get(i).getType() == 1) {
                num++;
            }
        }
        return num;
    }

    //获取单机操控对象
    public DeviceBean getSingleDevice() {
        for (int i = 0; i < mDeviceBeans.size(); i++) {
            if (mDeviceBeans.get(i).getType() == 1) {
                return mDeviceBeans.get(i);
            }
        }
        return null;
    }

}