package com.play.robot.view.home.help;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.play.robot.util.LogUtil;

public class MyOnMarkerClickListener implements BaiduMap.OnMarkerClickListener {
    @Override
    public boolean onMarkerClick(Marker marker) {
        LogUtil.e("点击编辑");

        return false;
    }
}
