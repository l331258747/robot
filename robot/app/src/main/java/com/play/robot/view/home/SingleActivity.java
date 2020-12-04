package com.play.robot.view.home;

import android.content.Intent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.constant.Constant;
import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.util.udp.ConnectionDeviceHelp;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.scale.ViewScale;

import io.reactivex.disposables.Disposable;

public class SingleActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_status;
    IvBattery iv_battery;
    IvSignal iv_signal;
    View small_view;

    ViewScale view_scale;

    Disposable disposableRuler, disposableAnim, disposableDevice;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    SurfaceView mSurfaceView;

    String ip;
    int port;
    String ipPort;

    @Override
    public int getLayoutId() {
        return R.layout.activity_single;
    }

    @Override
    public void initView() {
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port", 0);
        ipPort = ip + ":" + port;

        small_view = $(R.id.small_view);
        iv_status = $(R.id.iv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        view_scale = $(R.id.view_scale);

        setOnClick(iv_more, iv_route, iv_camera, iv_battery, iv_signal);

        initBaiduMap();

        initSurfaceView();

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);

        setStatus(true);
    }

    private void initSurfaceView() {
        mSurfaceView = $(R.id.surfaceView);
        mSurfaceView.setOnClickListener(this);
    }

    private void initBaiduMap() {
        mMapView = $(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();

        //MAP_TYPE_SATELLITE电子地图 , MAP_TYPE_NORMAL普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!mAnimatorHelp.getSurfaceViewCenter()) return;
                LogUtil.e("mapView onClick");
                mAnimatorHelp.setAnimator();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
        mMapView.setVisibility(View.GONE);
    }


    public void setStatus(boolean isConnection){
        iv_status.setImageResource(isConnection?R.mipmap.ic_ugv_in :R.mipmap.ic_ugv_un);
    }

    @Override
    public void initData() {
//        SPUtils.getInstance().putBoolean(SPUtils.IS_LOGIN, true);

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
            if (event.getIpPort().equals(ipPort)){
                if (event.getType() == -1) {
                    ConnectionDeviceHelp.getInstance().removeDevice(event.getIp(), event.getPort());
                    setStatus(false);
                }
            }
        });

        mBaiduHelper = new BaiduHelper(context, mMapView);
//        mBaiduHelper.setMapCustomStyle();
        mBaiduHelper.initMap();
        mAnimatorHelp = new AnimatorHelp(mSurfaceView, mMapView, small_view);
        mAnimatorHelp.getAnimatorParam();

    }


    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
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
}
