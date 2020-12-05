package com.play.robot.view.home.help;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.play.robot.bean.LocationModel;
import com.play.robot.util.AppUtils;
import com.play.robot.util.LogUtil;

/**
 * Created by LGQ
 * Time: 2018/7/25
 * Function: 定位工具类
 */

public class LocationUtil {

    private LocationClient mLocationClient;
    private LocationClientOption mLocationOption;

    public void initLocationClient(final LocationListener locationListener) {
        if (mLocationClient != null) return;

        // 1，实例化LocationClient类
        mLocationClient = new LocationClient(AppUtils.getContext());

        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    LogUtil.e("获取当前定位结果来源，如网络定位结果，详见定位类型表:" + bdLocation.getLocType());
                    LogUtil.e("获取纬度" + bdLocation.getLatitude());
                    LogUtil.e("获取经度" + bdLocation.getLongitude());
                    LogUtil.e("国家信息" + bdLocation.getCountry());
                    LogUtil.e("省信息" + bdLocation.getProvince());
                    LogUtil.e("城市信息" + bdLocation.getCity());
                    LogUtil.e("城区信息" + bdLocation.getDistrict());
                    LogUtil.e("街道信息" + bdLocation.getStreet());
                    LogUtil.e("街道门牌号信息" + bdLocation.getStreetNumber());
                    LogUtil.e("城市编码" + bdLocation.getCityCode());
                    LogUtil.e("地区编码" + bdLocation.getAdCode());
                    String city = bdLocation.getCity();
                    if (city.endsWith("市"))
                        city = city.substring(0, city.length() - 1);
                    locationListener.getAdress(new LocationModel(
                             bdLocation.getLongitude()
                            , bdLocation.getLatitude()));
                }
            }
        });
    }

    private void initLocation() {
        if (mLocationOption != null) return;
        mLocationOption = new LocationClientOption();
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mLocationOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        mLocationOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationOption.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationOption.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mLocationOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationOption.setIgnoreKillProcess(false);
        mLocationOption.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(mLocationOption);
    }


    public void startLocation(LocationListener locationListener) {
        initLocationClient(locationListener);
        initLocation();
        //启动定位
        mLocationClient.start();
    }

    public void stopLocation() {
        if (mLocationClient == null) return;
        mLocationClient.stop();
        mLocationClient = null;
        mLocationOption = null;
    }

    public interface LocationListener {
        void getAdress(LocationModel adress);
    }
}
