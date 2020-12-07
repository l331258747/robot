package com.play.robot.view.home;

import android.content.Intent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MarkerBean;
import com.play.robot.constant.Constant;
import com.play.robot.dialog.MarkerDialog;
import com.play.robot.dialog.MarkerModifyDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.home.help.MyOnMarkerClickListener;
import com.play.robot.view.home.help.MyOnMarkerDragListener;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.scale.ViewScale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class SingleActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_status;
    IvBattery iv_battery;
    IvSignal iv_signal;
    View small_view;
    LinearLayout ll_loc;

    ViewScale view_scale;

    Disposable disposableRuler, disposableAnim, disposableDevice;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    SurfaceView mSurfaceView;

    DeviceBean mDevice;

    double meLongitude;
    double meLatitude;

    int mode = 1;//0遥控模式，1智能遥控模式，2自主模式，3跟人，4跟车
    List<MarkerBean> markers;

    @Override
    public int getLayoutId() {
        return R.layout.activity_single;
    }

    @Override
    public void initView() {
        mDevice = new DeviceBean();
        mDevice.setIp(intent.getStringExtra("ip"));
        mDevice.setPort(intent.getIntExtra("port", 0));
        mDevice.setType(intent.getIntExtra("type", 0));
        meLongitude = intent.getDoubleExtra("meLongitude", 0);
        meLatitude = intent.getDoubleExtra("meLatitude", 0);

        small_view = $(R.id.small_view);
        iv_status = $(R.id.iv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        view_scale = $(R.id.view_scale);
        ll_loc = $(R.id.ll_loc);

        setOnClick(ll_loc, iv_more, iv_route, iv_camera, iv_battery, iv_signal);

        initBaiduMap();

        initSurfaceView();

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);

        setStatus();
    }

    //初始化视频SurfaceView控件
    private void initSurfaceView() {
        mSurfaceView = $(R.id.surfaceView);
        mSurfaceView.setOnClickListener(this);
    }

    //初始化地图控件
    private void initBaiduMap() {
        mMapView = $(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();

        //MAP_TYPE_SATELLITE电子地图 , MAP_TYPE_NORMAL普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mAnimatorHelp.getSurfaceViewCenter()) {
                    //放大视图
                    LogUtil.e("mapView onClick");
                    mAnimatorHelp.setAnimator();
                } else {
                    if (mode == 1 || mode == 2) {//设置途径点
                        if (isMarkerUn()) {
                            showShortToast("已添加终点，不可再添加");
                            return;
                        }

                        int showType = markers.size() == 0 ? 0 : 1;
                        new MarkerDialog(context)
                                .setLatLng(latLng.latitude, latLng.longitude)
                                .setShowType(showType)
                                .setSubmitListener((latitude, longitude, type) -> {
                                    MarkerBean mBean = new MarkerBean();
                                    mBean.setLatitude(latitude);
                                    mBean.setLongitude(longitude);
                                    mBean.setType(type);
                                    mBean.setNum(type == 1 ? markers.size() : 0);
                                    markers.add(mBean);
                                    mBaiduHelper.onMapClick(new LatLng(latitude, longitude), mBean.getNumStr(), markers.size() - 1);

                                    if (type == -1) {
                                        mBaiduHelper.showMarkerLine(markers);
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
        mMapView.setVisibility(View.GONE);
    }

    public boolean isMarkerUn() {
        if (markers.size() >= 2) {
            boolean isStart = false;
            boolean isEnd = false;
            for (int i = 0; i < markers.size(); i++) {
                if (markers.get(i).getType() == 0) {
                    isStart = true;
                }
                if (markers.get(i).getType() == -1) {
                    isEnd = true;
                }
                if (isStart && isEnd)
                    return true;
            }
        }
        return false;
    }


    //设置状态
    public void setStatus() {
        iv_status.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : R.mipmap.ic_ugv_un);
    }

    @Override
    public void initData() {

        //游标
        disposableRuler = RxBus2.getInstance().toObservable(VoteEvent.class, voteEvent -> view_scale.setValues(voteEvent.getVote()));


        //动画 地图和视频视图加载完了之后  设置地图缩小，如果直接设置地图是小图的话，放大视图会变形。
        disposableAnim = RxBus2.getInstance().toObservable(AnimatorEvent.class, animatorEvent -> {
            if (animatorEvent.isBig() && animatorEvent.isSmall()) {
                mAnimatorHelp.setSmallAnimation();
            }
        });

        //链接状态
        disposableDevice = RxBus2.getInstance().toObservable(ConnectIpEvent.class, event -> {
            if (event.getIpPort().equals(mDevice.getIpPort())) {
                if (event.getType() == -1) {
                    setStatus();
                }
            }
        });

        //------------------地图 start----------
        mBaiduHelper = new BaiduHelper(context, mMapView);
        MyOnMarkerClickListener markerClickListener = new MyOnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                new MarkerModifyDialog(context)
                        .setLatLng(marker.getPosition().latitude, marker.getPosition().longitude)
                        .setSubmitListener((latitude, longitude) -> {
                            int pos = marker.getExtraInfo().getInt("markerPos");
                            markers.get(pos).setLatitude(latitude);
                            markers.get(pos).setLongitude(longitude);

                            if (isMarkerUn()) {
                                mBaiduHelper.showMarkerLine(markers);
                            } else {
                                mBaiduHelper.showMarkers(markers);
                            }

                        }).show();
                return false;
            }
        };
        MyOnMarkerDragListener markerDragListener = new MyOnMarkerDragListener() {
            boolean isMove;

            @Override
            public void onMarkerDrag(Marker marker) {
                isMove = true;
                LogUtil.e("拖拽中");
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                int pos = marker.getExtraInfo().getInt("markerPos");
                //拖拽结束
                if (!isMove) {
                    //删除
                    new TextDialog(context)
                            .setTitle("删除")
                            .setContent(pos == 0 ? "如果删除起点，将清除所有点。" : "是否删除途径点")
                            .setSubmitListener(v -> {
                                if (pos == 0) {
                                    markers.clear();
                                    mBaiduHelper.ClearMarkers();
                                } else {
                                    markers.remove(pos);
                                    if (isMarkerUn()) {
                                        mBaiduHelper.showMarkerLine(markers);
                                    } else {
                                        mBaiduHelper.showMarkers(markers);
                                    }
                                }
                            })
                            .setCancelListener(v -> {
                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }).show();

                } else {
                    //拖拽
                    LogUtil.e("拖拽");
                    new MarkerModifyDialog(context)
                            .setLatLng(marker.getPosition().latitude, marker.getPosition().longitude)
                            .setSubmitListener((latitude, longitude) -> {
                                markers.get(pos).setLatitude(latitude);
                                markers.get(pos).setLongitude(longitude);

                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }

                            }).show();
                }
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
                isMove = false;
                LogUtil.e("开始拖拽");

            }
        };

        mBaiduHelper.initMap(meLongitude, meLatitude, markerClickListener, markerDragListener);

        //------------------地图 end----------


        //------------------动画 start----------
        mAnimatorHelp = new AnimatorHelp(mSurfaceView, mMapView, small_view, ll_loc);
        mAnimatorHelp.getAnimatorParam();

        //------------------动画 end----------

        markers = new ArrayList<>();
    }


    //点击事件设置
    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, SettingActivity.class);
        switch (v.getId()) {
            case R.id.iv_more:
                intent.putExtra("position", Constant.SETTING_MORE);
                startActivity(intent);
                break;
            case R.id.iv_route:
                intent.putExtra("position", Constant.SETTING_ROUTE);
                startActivity(intent);
                break;
            case R.id.iv_camera:
                intent.putExtra("position", Constant.SETTING_CAMERA);
                startActivity(intent);
                break;
            case R.id.iv_battery:
                intent.putExtra("position", Constant.SETTING_BATTERY);
                startActivity(intent);
                break;
            case R.id.iv_signal:
                intent.putExtra("position", Constant.SETTING_SIGNAL);
                startActivity(intent);
                break;
            case R.id.surfaceView:
                if (mAnimatorHelp.getSurfaceViewCenter()) return;
                LogUtil.e("surfaceView onClick");
                mAnimatorHelp.setAnimator();
                break;
            case R.id.ll_loc:
                mBaiduHelper.setLoc();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableRuler != null && !disposableRuler.isDisposed())
            disposableRuler.dispose();
        if (disposableAnim != null && !disposableAnim.isDisposed())
            disposableAnim.dispose();
        if (disposableDevice != null && !disposableDevice.isDisposed())
            disposableDevice.dispose();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



    /*
    移动尺标
    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Random r = new Random();
                        for (int i = 0; i < 10; i++) {
                            int i1 = r.nextInt(360);
                            RxBus2.getInstance().post(new VoteEvent(i1));
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
     */

    /*
    百度地图无人车定位
    List<Double> a = new ArrayList<>();
                a.add(112.929229);
                a.add(112.929839);
                a.add(112.933828);
                a.add(112.936451);
                a.add(112.939793);
                a.add(112.944176);
                List<Double> b = new ArrayList<>();
                b.add(28.231485);
                b.add(28.231389);
                b.add(28.231517);
                b.add(28.231548);
                b.add(28.231548);
                b.add(28.231676);

                new Thread(() -> {
                    for (int i = 0; i < a.size(); i++) {
                        mBaiduHelper.setLocation(a.get(i),b.get(i));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
     */
}
