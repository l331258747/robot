package com.play.robot.view.home.help;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.play.robot.R;
import com.play.robot.bean.MarkerBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BaiduHelper {

    Context context;
    MapView mMapView;
    BaiduMap mBaiduMap;

    private static final String CUSTOM_FILE_NAME_CX = "22935e6550b6085cda2643a668d11e8b.sty";

    public BaiduHelper(Context context, MapView mapView) {
        this.context = context;
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
    }


    public void initMap(double longitude, double latitude) {
        LatLng cenpt = new LatLng(latitude, longitude);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        //百度地图
        mBaiduMap = mMapView.getMap();
        // 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
        mBaiduMap.setMyLocationEnabled(true);
    }

//    public void setMark(){
//        // 设置marker图标
//        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mark);
//    }

    public void onMapClick(List<MarkerBean> markers) {
        List<LatLng> points = new ArrayList<>();
        for (int i=0;i<markers.size();i++){
            //构建折线点坐标
            LatLng p1 = new LatLng(markers.get(i).getLatitude(), markers.get(i).getLongitude());
            points.add(p1);
        }

        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(5)
                .color(0xAAFF0000)
                .points(points);
        //在地图上绘制折线
        //mPloyline 折线对象
        Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
    }

    public void onMapClick(LatLng latLng, String str) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_mark, null, true);
        TextView tv_num = view.findViewById(R.id.tv_text);//获取自定义布局中的textview
        tv_num.setText(str);//设置要显示的文本
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(view);
        //获取经纬度
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        //先清除图层
//        mBaiduMap.clear();
        // 定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions options = new MarkerOptions()
                .position(point)
                .draggable(true)  //设置手势拖拽
                .icon(bitmap);
        // 在地图上添加Marker，并显示
        Overlay mPolyline = mBaiduMap.addOverlay(options);


//        //Marker点击事件
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            //marker被点击时回调的方法
//            //若响应点击事件，返回true，否则返回false
//            //默认返回false
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                return false;
//            }
//        });
//
//        //Marker拖拽事件
//        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
//
//            //在Marker拖拽过程中回调此方法，这个Marker的位置可以通过getPosition()方法获取
//            //marker 被拖动的Marker对象
//            @Override
//            public void onMarkerDrag(Marker marker) {
//                //对marker处理拖拽逻辑 //拖拽中
//            }
//
//            //在Marker拖动完成后回调此方法， 这个Marker的位可以通过getPosition()方法获取
//            //marker 被拖拽的Marker对象
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                    //拖拽结束
//            }
//
//            //在Marker开始被拖拽时回调此方法， 这个Marker的位可以通过getPosition()方法获取
//            //marker 被拖拽的Marker对象
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                    //开始拖拽
//            }
//        });
    }

    double longitude;
    double latitude;

    public void setLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(100)//getRadius 获取定位精度,默认值0.0f
                .direction(100)//方向 // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(latitude)
                .longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);
    }

    // 1，获取无人车位置后，设置定位点
    // 2，或者通过传感器最后一条数据设置定位点
    public void setLoc() {
        if (latitude == 0) return;
        LatLng cenpt = new LatLng(latitude, longitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(cenpt);
        mBaiduMap.animateMapStatus(msu);
    }

    //---------------start 地图样式------------------
    //设置已定义地图样式
    public void setMapCustomStyle() {
        // 获取.sty文件路径 22935e6550b6085cda2643a668d11e8b.sty
        String customStyleFilePath = getCustomStyleFilePath(context, CUSTOM_FILE_NAME_CX);
        // 设置个性化地图样式文件的路径和加载方式
        mMapView.setMapCustomStylePath(customStyleFilePath);
        // 动态设置个性化地图样式是否生效
        mMapView.setMapCustomStyleEnable(true);
    }

    /**
     * 读取json路径
     */
    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;
        try {
            inputStream = context.getAssets().open(customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("CustomMapDemo", "Close stream failed", e);
                return null;
            }
        }
        return parentPath + "/" + customStyleFileName;
    }

    //---------------end 地图样式------------------


}
