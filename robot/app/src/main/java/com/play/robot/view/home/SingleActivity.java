package com.play.robot.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.play.robot.MyApplication;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.DeviceInfoBean;
import com.play.robot.bean.MarkerBean;
import com.play.robot.bean.SettingInfo;
import com.play.robot.constant.Constant;
import com.play.robot.dialog.DeviceInfoDialog;
import com.play.robot.dialog.DeviceModeDialog;
import com.play.robot.dialog.InstructDialog;
import com.play.robot.dialog.MarkerDialog;
import com.play.robot.dialog.MarkerModifyDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.AppUtils;
import com.play.robot.util.GsonUtil;
import com.play.robot.util.LogUtil;
import com.play.robot.util.SPUtils;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ChangeEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.ReceiveCarEvent;
import com.play.robot.util.rxbus.rxbusEvent.ReceiveStatusEvent;
import com.play.robot.util.rxbus.rxbusEvent.StopShowEvent;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.home.help.MyOnMarkerClickListener;
import com.play.robot.view.home.help.MyOnMarkerDragListener;
import com.play.robot.view.home.help.SendHelp;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.NumberView;
import com.play.robot.widget.SwitchButton;
import com.play.robot.widget.rocherView.MyRockerShakeListener;
import com.play.robot.widget.rocherView.MyRockerView;
import com.play.robot.widget.scale.ViewScale;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerView;
import io.reactivex.disposables.Disposable;

public class SingleActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_status, iv_sign, iv_flameout, iv_rocker, iv_stop;
    IvBattery iv_battery;
    IvSignal iv_signal;
    View small_view;
    LinearLayout ll_loc, ll_task;
    TextView tv_task_send, tv_task_read, tv_rocker_inside, tv_rocker_outside,tv_info;
    MyRockerView rockerViewLeft, rockerViewRight;
    ConstraintLayout cl_rocker;
    View view_stop;

    ViewScale view_scale;

    Disposable disposableAnim, disposableDevice, disposableStop, disposableChange, disposableCar,disposableStatus;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    NodePlayerView mSurfaceView;

    //链接的设备
    DeviceBean mDevice;

    //自己的经纬度
    double meLongitude;
    double meLatitude;

    int mode = 0;//0遥控模式，1智能遥控模式，2自主模式，3跟人，4跟车
    List<MarkerBean> markers;//地图marker点

    boolean isFlameout;//启动熄火

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
        mDevice.setRtsp(intent.getStringExtra("rtsp"));
        mDevice.setNumber(intent.getStringExtra("number"));
        meLongitude = intent.getDoubleExtra("meLongitude", 0);
        meLatitude = intent.getDoubleExtra("meLatitude", 0);

        small_view = $(R.id.small_view);
        tv_info = $(R.id.tv_info);
        cl_rocker = $(R.id.cl_rocker);
        iv_status = $(R.id.iv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        view_scale = $(R.id.view_scale);
        ll_loc = $(R.id.ll_loc);
        ll_task = $(R.id.ll_task);
        tv_task_send = $(R.id.tv_task_send);
        tv_task_read = $(R.id.tv_task_read);
        iv_sign = $(R.id.iv_sign);
        iv_flameout = $(R.id.iv_flameout);
        iv_rocker = $(R.id.iv_rocker);
        tv_rocker_inside = $(R.id.tv_rocker_inside);
        tv_rocker_outside = $(R.id.tv_rocker_outside);
        iv_stop = $(R.id.iv_stop);
        view_stop = $(R.id.view_stop);

        setOnClick(tv_rocker_inside, tv_rocker_outside, ll_loc, iv_more, iv_route, iv_camera, iv_battery, iv_signal, tv_task_send, tv_task_read, iv_sign, iv_flameout, iv_rocker);

        iv_sign.setOnLongClickListener(this);

        initBaiduMap();

        initSurfaceView();

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);

        initRockerView();
        initIvStop();
        initTrack();

    }

    boolean isDown = false;

    //急停按钮
    @SuppressLint("ClickableViewAccessibility")
    public void initIvStop() {
        view_stop.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 按下
                    isDown = true;
                    new Handler().postDelayed(() -> {
                        if (isDown) {
                            RxBus2.getInstance().post(new StopShowEvent());
                            SendHelp.sendJS(mDevice.getIpPort());
                        }
                    }, 500);
                    break;
                case MotionEvent.ACTION_MOVE:// 移动

                    break;
                case MotionEvent.ACTION_UP:// 抬起
                case MotionEvent.ACTION_CANCEL:// 移出区域
                    isDown = false;
                    iv_stop.setVisibility(View.GONE);
                    break;
            }
            return true;
        });
    }

    private NodePlayer nodePlayer;

    //初始化视频SurfaceView控件
    private void initSurfaceView() {
        mSurfaceView = $(R.id.surfaceView);
        mSurfaceView.setOnClickListener(this);

        //设置渲染器类型
        mSurfaceView.setRenderType(NodePlayerView.RenderType.SURFACEVIEW);
        //设置视频画面缩放模式
        mSurfaceView.setUIViewContentMode(NodePlayerView.UIViewContentMode.ScaleToFill);

        nodePlayer = new NodePlayer(this);
        //设置播放视图
        nodePlayer.setPlayerView(mSurfaceView);
        //设置RTSP流使用的传输协议,支持的模式有:
        nodePlayer.setRtspTransport(NodePlayer.RTSP_TRANSPORT_TCP);

    }

    public void setStart(String rtsp) {
        if(nodePlayer != null){
            nodePlayer.setInputUrl(rtsp);
            //设置视频是否开启
            nodePlayer.setVideoEnable(true);
            nodePlayer.start();
        }
    }

    public void setPause() {
        if (nodePlayer != null && nodePlayer.isPlaying()) {
            nodePlayer.setVideoEnable(false);
            nodePlayer.pause();
        }
    }

    public void setStop() {
        if (nodePlayer != null && nodePlayer.isPlaying()) {
            nodePlayer.setVideoEnable(false);
            nodePlayer.stop();
            nodePlayer = null;
        }
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
                    if (mode == 2) {//设置途径点
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
                                    mBean.setNum((type == 1 || type == 2) ? markers.size() : 0);
                                    markers.add(mBean);
                                    mBaiduHelper.onMapClick(new LatLng(latitude, longitude), mBean.getNumStr(),type, markers.size() - 1);

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
    public void setStatusView() {
        iv_status.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : R.mipmap.ic_ugv_un);
    }

    //设置mode显示
    public void setModeView() {
        iv_rocker.setImageResource(mode == 0 ? R.mipmap.ic_control_un
                : mode == 1 ? R.mipmap.ic_control_mind_un
                : mode == 2 ? R.mipmap.ic_mind_un
                : mode == 3 ? R.mipmap.ic_follow_people_un
                : mode == 4 ? R.mipmap.ic_follow_car_un
                : R.mipmap.ic_control_un);
    }

    //设置任务点
    boolean isSufCenter = true;

    public void setTaskView(boolean isSufCenter) {
        this.isSufCenter = isSufCenter;
        if (mode == 2) {
            ll_task.setVisibility(isSufCenter ? View.GONE : View.VISIBLE);
        } else {
            ll_task.setVisibility(View.GONE);
        }
    }

    //内外置摇杆
    public void setRockerView(int rockerType) {//0初始，1内置，2外置
        if (mode == 0) {
            cl_rocker.setVisibility(View.VISIBLE);
            tv_rocker_inside.setVisibility(View.GONE);
            tv_rocker_outside.setVisibility(View.GONE);
            rockerViewLeft.setVisibility(View.GONE);
            rockerViewRight.setVisibility(View.GONE);
            view_center.setVisibility(View.GONE);
            if (rockerType == 1) {
                rockerViewLeft.setVisibility(View.VISIBLE);
                rockerViewRight.setVisibility(View.VISIBLE);
            } else if (rockerType == 2) {
            } else {
                tv_rocker_inside.setVisibility(View.VISIBLE);
                tv_rocker_outside.setVisibility(View.VISIBLE);
            }
        } else if (mode == 1) {
            cl_rocker.setVisibility(View.GONE);
            tv_rocker_inside.setVisibility(View.GONE);
            tv_rocker_outside.setVisibility(View.GONE);
            rockerViewLeft.setVisibility(View.GONE);
            rockerViewRight.setVisibility(View.GONE);

            view_center.setVisibility(View.VISIBLE);
        } else {
            cl_rocker.setVisibility(View.GONE);

            view_center.setVisibility(View.GONE);
        }
    }

    //----------------------跟人模式 start----------------
    ConstraintLayout cl_track;
    ImageView iv_track_status;
    SwitchButton switch_track_gj, switch_track_qs;
    NumberView nv_track_speed, nv_track_space;

    int speedNum;
    int spaceNum;

    public void initTrack() {
        cl_track = $(R.id.cl_track);
        iv_track_status = $(R.id.iv_track_status);
        switch_track_gj = $(R.id.switch_track_gj);
        switch_track_qs = $(R.id.switch_track_qs);
        nv_track_speed = $(R.id.nv_track_speed);
        nv_track_space = $(R.id.nv_track_space);

        cl_track.setVisibility(View.GONE);

        iv_track_status.setOnClickListener(v -> {
            if (mode == 3) {
                SendHelp.sendTrackPeopleStart(mDevice.getIpPort());
            } else if (mode == 4) {
                SendHelp.sendTrackCarStart(mDevice.getIpPort());
            }
        });

        switch_track_gj.setChecked(false);
        switch_track_qs.setChecked(false);

        switch_track_gj.setOnCheckedChangeListener((view, isChecked) -> {
            SendHelp.sendTrackGj(mDevice.getIpPort(), isChecked);
        });
        switch_track_qs.setOnCheckedChangeListener((view, isChecked) -> {
            SendHelp.sendTrackQc(mDevice.getIpPort(), isChecked);
        });

        nv_track_speed.setNum(speedNum);
        nv_track_speed.setCallback(num -> {
            speedNum = num;
            SendHelp.sendTrackSpeed(mDevice.getIpPort(), speedNum);

        });
        nv_track_space.setNum(spaceNum);
        nv_track_space.setCallback(num -> {
            spaceNum = num;
            SendHelp.sendTrackSpace(mDevice.getIpPort(), speedNum);
        });
    }

    public void setTrack(boolean isShow) {
        if (isShow) {
            cl_track.setVisibility(View.VISIBLE);
            switch_track_gj.setChecked(false);
            switch_track_qs.setChecked(false);
            nv_track_speed.setNum(speedNum = 0);
            nv_track_space.setNum(spaceNum = 0);
        } else {
            cl_track.setVisibility(View.GONE);
        }
    }


    //----------------------跟人模式 end----------------
    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {

        setStatusView();
        setModeView();
        setRockerView(0);
        setTaskView(isSufCenter);

        setStart(mDevice.getRtsp());

        markers = new ArrayList<>();
        SettingInfo.initData();

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
                    mDevice.setType(event.getType() == 1 ? 1 : 2);
                    setStatusView();
                    setStop();
                }
            }
        });

        //急刹车
        disposableStop = RxBus2.getInstance().toObservable(StopShowEvent.class, event -> {
            iv_stop.setAlpha(0f);
            iv_stop.setVisibility(View.VISIBLE);
            iv_stop.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(null);
        });

        //更换无人车
        disposableChange = RxBus2.getInstance().toObservable(ChangeEvent.class, event -> {
            mDevice.setIp(event.getIp());
            mDevice.setPort(event.getPort());
            mDevice.setType(event.getType());
            mDevice.setRtsp(event.getRtsp());
            mDevice.setNumber(event.getNumber());

            setStatusView();
            SettingInfo.initData();

            markers.clear();
            mBaiduHelper.ClearMarkers();

            setStart(mDevice.getRtsp());

            tv_info.setText("");

        });

        //无人车信息
        disposableCar = RxBus2.getInstance().toObservable(ReceiveCarEvent.class, event -> {
            if (!TextUtils.equals(mDevice.getIpPort(), event.getIpPort())) return;
            mInfoBean.setSpeed(event.getN1());
            mInfoBean.setGear(event.getN2());
            mInfoBean.setLng(event.getN6());
            mInfoBean.setLat(event.getN7());

            //地图定位
            mBaiduHelper.setLocation(event.getN6Int(), event.getN7Int(), event.getN8Float());

            //电池
            iv_battery.setSelect(event.getN4Int() >= 5 ? Constant.BATTERY_5
                    : event.getN4Int() == 4 ? Constant.BATTERY_4
                    : event.getN4Int() == 3 ? Constant.BATTERY_3
                    : event.getN4Int() == 2 ? Constant.BATTERY_2
                    : Constant.BATTERY_1);

            //游标
            view_scale.setValues(event.getN8Int());

            event.getN1();//速度---------
            event.getN2();//档位-------

            event.getN3();//底层控制器错误码

            event.getN4();//电量
            event.getN5();//油量

            event.getN6();//车体当前经度----
            event.getN7();//车体当前纬度------
            event.getN8();//车体方向-------

            event.getN9();//车体横滚角
            event.getN10();//车体俯仰角
            event.getN11();//当前车辆模型名

            tv_info.setText("速度:"+event.getN1()
                    +"\n档位:"+event.getN2()
                    +"\n错误:"+event.getN3()
                    +"\n电量:"+event.getN4()
                    +"\n油量:"+event.getN5()
                    +"\n经度:"+event.getN6()
                    +"\n纬度:"+event.getN7()
                    +"\n方向:"+event.getN8()
                    +"\n横滚角:"+event.getN9()
                    +"\n俯仰角:"+event.getN10()
                    +"\n车辆:"+event.getN11()
            );
        });

        disposableStatus = RxBus2.getInstance().toObservable(ReceiveStatusEvent.class, event -> {
            if (!TextUtils.equals(mDevice.getIpPort(), event.getIpPort())) return;

            mInfoBean.setStatus(event.getN1());
            mInfoBean.setDistance(event.getN2());
            //n1:
            //0----有目标
            //1----目标暂时丢失但还保留历史信息
            //2----初始化失败
            //3----确认目标丢失，停车

            //n2: 目标距离


        });

        //------------------地图 start----------
        mBaiduHelper = new BaiduHelper(context, mMapView);
        MyOnMarkerClickListener markerClickListener = new MyOnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int pos = marker.getExtraInfo().getInt("markerPos");

                new MarkerModifyDialog(context)
                        .setLatLng(marker.getPosition().latitude, marker.getPosition().longitude,markers.get(pos).getType())
                        .setSubmitListener(new MarkerModifyDialog.OnItemClickListener() {
                            @Override
                            public void onClick(double latitude, double longitude, int type) {
                                markers.get(pos).setLatitude(latitude);
                                markers.get(pos).setLongitude(longitude);
                                markers.get(pos).setType(type);

                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }

                            @Override
                            public void onDel() {
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
                            }

                            @Override
                            public void onCancel() {
                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }
                        }).show();
                return false;
            }
        };
        MyOnMarkerDragListener markerDragListener = new MyOnMarkerDragListener() {
            @Override
            public void onMarkerDragEnd(Marker marker) {
                int pos = marker.getExtraInfo().getInt("markerPos");
//                //拖拽结束
//                if (!isMove()) {
//                    //删除
//                } else {
//                    //拖拽
//                }
                new MarkerModifyDialog(context)
                        .setLatLng(marker.getPosition().latitude, marker.getPosition().longitude,markers.get(pos).getType())
                        .setSubmitListener(new MarkerModifyDialog.OnItemClickListener() {
                            @Override
                            public void onClick(double latitude, double longitude, int type) {
                                markers.get(pos).setLatitude(latitude);
                                markers.get(pos).setLongitude(longitude);
                                markers.get(pos).setType(type);

                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }

                            @Override
                            public void onDel() {
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
                            }

                            @Override
                            public void onCancel() {
                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }
                        }).show();
            }
        };

        mBaiduHelper.initMap(meLongitude, meLatitude, markerClickListener, markerDragListener);

        //------------------地图 end----------


        //------------------动画 start----------
        mAnimatorHelp = new AnimatorHelp(mSurfaceView, mMapView, small_view, isSufCenter -> {
            ll_loc.setVisibility(isSufCenter ? View.GONE : View.VISIBLE);

            setTaskView(isSufCenter);
        });
        mAnimatorHelp.getAnimatorParam();

        //------------------动画 end----------

    }

    DeviceInfoBean mInfoBean = new DeviceInfoBean();


    //点击事件设置
    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra("ipPort", mDevice.getIpPort());
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
            case R.id.tv_rocker_inside:
                setRockerView(1);
                break;
            case R.id.tv_rocker_outside:
                setRockerView(2);
                break;
            case R.id.tv_task_send:
                if (!isMarkerUn()) {
                    showShortToast("请先设置途径点");
                    return;
                }
                SendHelp.sendMarker(mDevice.getIpPort(), markers);
                break;
            case R.id.tv_task_read:
                if(TextUtils.isEmpty(SPUtils.getInstance().getString("markers"))){
                    showShortToast("获取任务点失败");
                    return;
                }

                showShortToast("获取任务点成功");
                markers.clear();
                markers = GsonUtil.convertString2Collection(SPUtils.getInstance().getString("markers"),new TypeToken<List<MarkerBean>>(){});
                mBaiduHelper.showMarkerLine(markers);

                break;
            case R.id.iv_sign://信息，指令
                if (mInfoBean != null && !TextUtils.isEmpty(mInfoBean.toString()))
                    new DeviceInfoDialog(context).setTitle(mDevice.getIpPort()).setContent(mInfoBean.toString()).show();
                break;
            case R.id.iv_flameout://启动，熄火
                new TextDialog(context).setContent(isFlameout ? "是否确认熄火" : "是否确认启动").setSubmitListener(v1 -> {
                    if (isFlameout) {
                        SendHelp.sendXH(mDevice.getIpPort());
                    } else {
                        SendHelp.sendQD(mDevice.getIpPort());
                    }
                    isFlameout = !isFlameout;
                }).show();

                break;
            case R.id.iv_rocker://模式切换
                new DeviceModeDialog(context).setSubmitListener(mode -> {
                    this.mode = mode;
                    this.markers.clear();
                    mBaiduHelper.ClearMarkers();

                    setModeView();
                    setRockerView(0);

                    mInfoBean.setStatus("");
                    mInfoBean.setDistance("");

                    setTaskView(isSufCenter);

                    setTrack(false);

                    if (mode == 3) {
                        setTrack(true);
                        SendHelp.sendTrackPeopleInit(mDevice.getIpPort());
                    }
                    if (mode == 4) {
                        setTrack(true);
                        SendHelp.sendTrackCarInit(mDevice.getIpPort());
                    }

                }).show();
                break;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sign://信息，指令
                new InstructDialog(context).setTitle(mDevice.getIpPort()).setSubmitListener(content -> {
                    SendHelp.sendMsg(mDevice.getIpPort(), content);
                }).show();
                break;
        }
        return false;
    }

    //----------------------------------- 遥控 start-----------------
    int upLevel;
    int turnLevel;

    @SuppressLint("ClickableViewAccessibility")
    public void initRockerView() {
        w = AppUtils.getDisplayWidth(activity);
        h = AppUtils.getDisplayHeight(activity);

        view_center = findViewById(R.id.view_center);

        //前后
        rockerViewLeft = findViewById(R.id.rocker_view_left);
        if (rockerViewLeft != null) {
            rockerViewLeft.setCallBackMode(MyRockerView.CallBackMode.CALL_BACK_MODE_MOVE);
            rockerViewLeft.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_2_VERTICAL, new MyRockerShakeListener() {

                @Override
                public void directionLevel(int level) {
                    upLevel = level;
                    SendHelp.sendRocker(mDevice.getIpPort(), upLevel, turnLevel);
                }
            });
        }

        //左右
        rockerViewRight = findViewById(R.id.rocker_view_right);
        if (rockerViewRight != null) {
            rockerViewRight.setCallBackMode(MyRockerView.CallBackMode.CALL_BACK_MODE_MOVE);
            rockerViewRight.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_2_HORIZONTAL, new MyRockerShakeListener() {

                @Override
                public void directionLevel(int level) {
                    turnLevel = level;
                    SendHelp.sendRocker(mDevice.getIpPort(), upLevel, turnLevel);
                }

            });
        }

        view_center.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                    sx = (int) event.getRawX();
                    sy = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                    sx = (int) event.getRawX();
                    sy = (int) event.getRawY();

                    if(sy < AppUtils.dip2px(40)){
                        sy = AppUtils.dip2px(40);
                    }
                    if(sy > h - AppUtils.dip2px(10)){
                        sy = h - AppUtils.dip2px(10);
                    }
                    if(sx < AppUtils.dip2px(10)){
                        sx = AppUtils.dip2px(10);
                    }
                    if(sx > w - AppUtils.dip2px(120)){
                        sx = w - AppUtils.dip2px(120);
                    }

                    view_center.setY(sy-view_center.getHeight()/2);
                    view_center.setX(sx-view_center.getWidth()/2);

                    break;
                case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                    setCenter();
                    int msx = sx - AppUtils.dip2px(10);
                    int msy = sy - AppUtils.dip2px(40);

                    SendHelp.sendRockerAngle(mDevice.getIpPort(), msx, msy, mSurfaceView.getWidth(), mSurfaceView.getHeight());
                    break;
            }
            return true;
        });
    }

    public void setCenter() {
        LogUtil.e("mSurfaceView.getWidth():" + mSurfaceView.getWidth());
        LogUtil.e("mSurfaceView.getHeight():" + mSurfaceView.getHeight());

        centerW = AppUtils.dip2px(10) + mSurfaceView.getWidth()/2 - AppUtils.dip2px(centerSize) / 2;
        centerY = AppUtils.dip2px(40) + mSurfaceView.getHeight()/2 - AppUtils.dip2px(centerSize) / 2;
        view_center.setX(centerW);
        view_center.setY(centerY);
    }


    int sx,sy;//其实位置
    int w = 0;//屏幕宽度
    int h = 0;//屏幕高度
    ImageView view_center;
    int centerW = 0;
    int centerY = 0;
    int centerSize = 30;
    int centerMove = 5;
    //----------------------------------- 遥控 end-----------------

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        setStart(mDevice.getRtsp());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        setPause();
    }

    @Override
    protected void onDestroy() {
        if (disposableAnim != null && !disposableAnim.isDisposed())
            disposableAnim.dispose();
        if (disposableDevice != null && !disposableDevice.isDisposed())
            disposableDevice.dispose();
        if (disposableStop != null && !disposableStop.isDisposed())
            disposableStop.dispose();
        if (disposableChange != null && !disposableChange.isDisposed())
            disposableChange.dispose();
        if (disposableCar != null && !disposableCar.isDisposed())
            disposableCar.dispose();
        if (disposableStatus != null && !disposableStatus.isDisposed())
            disposableStatus.dispose();
        mMapView.onDestroy();

        MyApplication.getInstance().deviceClear();

        setStop();

        super.onDestroy();
    }


//    //发送marker
//    public void sendMarker(){
//        msg.setLength(0);
//        msg.append("$1,2,2");
//        for (int i=0;i<markers.size();i++){
//            msg.append("," + markers.get(i).getLatitude());
//            msg.append("," + markers.get(i).getLongitude());
//        }
//
//        MyApplication.getInstance().sendMsg(mDevice.getIpPort(),msg.toString());
//    }


    //----------------指令 end----------
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
