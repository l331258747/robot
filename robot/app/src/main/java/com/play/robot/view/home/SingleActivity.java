package com.play.robot.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MarkerBean;
import com.play.robot.bean.SettingInfo;
import com.play.robot.constant.Constant;
import com.play.robot.dialog.DeviceInfoDialog;
import com.play.robot.dialog.DeviceModeDialog;
import com.play.robot.dialog.InstructDialog;
import com.play.robot.dialog.MarkerDialog;
import com.play.robot.dialog.MarkerModifyDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ChangeEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.StopShowEvent;
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.home.help.MyOnMarkerClickListener;
import com.play.robot.view.home.help.MyOnMarkerDragListener;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.rocherView.MyRockerShakeListener;
import com.play.robot.widget.rocherView.MyRockerView;
import com.play.robot.widget.scale.ViewScale;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.reactivex.disposables.Disposable;

public class SingleActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_status, iv_sign, iv_flameout, iv_rocker, iv_stop;
    IvBattery iv_battery;
    IvSignal iv_signal;
    View small_view;
    LinearLayout ll_loc, ll_task;
    TextView tv_task_send, tv_task_read, tv_rocker_inside, tv_rocker_outside;
    MyRockerView rockerViewLeft, rockerViewRight;
    ConstraintLayout cl_rocker;
    View view_stop;

    ViewScale view_scale;

    Disposable disposableRuler, disposableAnim, disposableDevice, disposableStop, disposableChange;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    SurfaceView mSurfaceView;

    DeviceBean mDevice;

    double meLongitude;
    double meLatitude;

    int mode = 0;//0遥控模式，1智能遥控模式，2自主模式，3跟人，4跟车
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

        setStatusView();
        setModeView();
        setRockerView(0);
        setTaskView(isSufCenter);


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
                        }
                    }, 1200);
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
        if (mode == 1 || mode == 2) {
            ll_task.setVisibility(isSufCenter ? View.GONE : View.VISIBLE);
        } else {
            ll_task.setVisibility(View.GONE);
        }
    }

    //内外置摇杆
    public void setRockerView(int rockerType) {//0初始，1内置，2外置
        if (mode == 0 || mode == 1) {
            cl_rocker.setVisibility(View.VISIBLE);
            if (rockerType == 1) {
                tv_rocker_inside.setVisibility(View.GONE);
                tv_rocker_outside.setVisibility(View.GONE);
                rockerViewLeft.setVisibility(View.VISIBLE);
                rockerViewRight.setVisibility(View.VISIBLE);
            } else if (rockerType == 2) {
                tv_rocker_inside.setVisibility(View.GONE);
                tv_rocker_outside.setVisibility(View.GONE);
                rockerViewLeft.setVisibility(View.GONE);
                rockerViewRight.setVisibility(View.GONE);
            } else {
                tv_rocker_inside.setVisibility(View.VISIBLE);
                tv_rocker_outside.setVisibility(View.VISIBLE);
                rockerViewLeft.setVisibility(View.GONE);
                rockerViewRight.setVisibility(View.GONE);
            }
        } else {
            cl_rocker.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

        markers = new ArrayList<>();
        SettingInfo.initData();

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
                    setStatusView();
                }
            }
        });

        disposableStop = RxBus2.getInstance().toObservable(StopShowEvent.class, event -> {
            iv_stop.setAlpha(0f);
            iv_stop.setVisibility(View.VISIBLE);
            iv_stop.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(null);
        });

        disposableChange = RxBus2.getInstance().toObservable(ChangeEvent.class, event -> {
            mDevice.setIp(event.getIp());
            mDevice.setPort(event.getPort());
            mDevice.setType(event.getType());

            setStatusView();
            SettingInfo.initData();

            markers.clear();
            mBaiduHelper.ClearMarkers();
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
            @Override
            public void onMarkerDragEnd(Marker marker) {
                int pos = marker.getExtraInfo().getInt("markerPos");
                //拖拽结束
                if (!isMove()) {
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
                break;
            case R.id.tv_task_read:
                showShortToast("读取途径点");
                break;
            case R.id.iv_sign://信息，指令
                String str = "V:10m/s\nS:100m\nD:50m\n精度:12.325415\n维度:112.324567";
                new DeviceInfoDialog(context).setTitle(mDevice.getIpPort()).setContent(str).show();
                break;
            case R.id.iv_flameout://启动，熄火
                new TextDialog(context).setContent("是否确认熄火/启动").show();
                break;
            case R.id.iv_rocker://模式切换
                new DeviceModeDialog(context).setSubmitListener(mode -> {
                    this.mode = mode;
                    this.markers.clear();
                    mBaiduHelper.ClearMarkers();

                    setModeView();
                    setRockerView(0);

                    setTaskView(isSufCenter);


                }).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sign://信息，指令
                new InstructDialog(context).setTitle(mDevice.getIpPort()).setSubmitListener(content -> {
                    showShortToast("指令：" + content);
                }).show();
                break;
        }
        return false;
    }

    //----------------------------------- 遥控 start-----------------
    int upLevel;
    int turnLevel;

    public void initRockerView() {
        //前后
        rockerViewLeft = findViewById(R.id.rocker_view_left);
        if (rockerViewLeft != null) {
            rockerViewLeft.setCallBackMode(MyRockerView.CallBackMode.CALL_BACK_MODE_MOVE);
            rockerViewLeft.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_2_VERTICAL, new MyRockerShakeListener() {

                @Override
                public void directionLevel(int level) {
                    upLevel = level;
                    sendRocker();
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
                    sendRocker();
                }

            });
        }
    }

    public void sendRocker() {
        LogUtil.e("" + upLevel + "," + turnLevel);
    }

    //----------------------------------- 遥控 end-----------------

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableRuler != null && !disposableRuler.isDisposed())
            disposableRuler.dispose();
        if (disposableAnim != null && !disposableAnim.isDisposed())
            disposableAnim.dispose();
        if (disposableDevice != null && !disposableDevice.isDisposed())
            disposableDevice.dispose();
        if (disposableStop != null && !disposableStop.isDisposed())
            disposableStop.dispose();
        if (disposableChange != null && !disposableChange.isDisposed())
            disposableChange.dispose();
        mMapView.onDestroy();
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
