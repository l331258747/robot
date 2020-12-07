package com.play.robot.view.home.help;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.play.robot.util.LogUtil;

public class MyOnMarkerDragListener implements BaiduMap.OnMarkerDragListener {
    boolean isMove;

    //在Marker拖拽过程中回调此方法，这个Marker的位置可以通过getPosition()方法获取
    //marker 被拖动的Marker对象
    @Override
    public void onMarkerDrag(Marker marker) {
        //对marker处理拖拽逻辑 //拖拽中
        isMove = true;
        LogUtil.e("拖拽中");
    }

    //在Marker拖动完成后回调此方法， 这个Marker的位可以通过getPosition()方法获取
    //marker 被拖拽的Marker对象
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //拖拽结束
        if (!isMove) {
            //删除
            LogUtil.e("删除");
        } else {
            //拖拽
            LogUtil.e("拖拽");
        }
    }

    //在Marker开始被拖拽时回调此方法， 这个Marker的位可以通过getPosition()方法获取
    //marker 被拖拽的Marker对象
    @Override
    public void onMarkerDragStart(Marker marker) {
        //开始拖拽
        isMove = false;
        LogUtil.e("开始拖拽");

    }
}
