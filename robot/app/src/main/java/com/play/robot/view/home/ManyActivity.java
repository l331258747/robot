package com.play.robot.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.TextureView;
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
import com.play.robot.bean.MarkerBean;
import com.play.robot.bean.ReceiveCarBean;
import com.play.robot.bean.SettingInfo;
import com.play.robot.constant.Constant;
import com.play.robot.dialog.DeviceInfoDialog;
import com.play.robot.dialog.InstructDialog;
import com.play.robot.dialog.MarkerDialog;
import com.play.robot.dialog.MarkerModifyDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.GsonUtil;
import com.play.robot.util.LogUtil;
import com.play.robot.util.SPUtils;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.ReceiveCarEvent;
import com.play.robot.util.rxbus.rxbusEvent.StopShowEvent;
import com.play.robot.util.rxbus.rxbusEvent.ZkcEvent;
import com.play.robot.view.home.help.AnimatorHelp2;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.home.help.MyOnMarkerClickListener;
import com.play.robot.view.home.help.MyOnMarkerDragListener;
import com.play.robot.view.home.help.SendHelp;
import com.play.robot.view.setting.SettingManyActivity;
import com.play.robot.widget.ContentDeviceView;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvShape;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.TitleDeviceView;
import com.play.robot.widget.scale.ViewScale;

import org.easydarwin.video.EasyPlayerClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ManyActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_stop;
    IvBattery iv_battery;
    IvSignal iv_signal;
    IvShape iv_shape;
    View small_view, view_stop;
    TextView tv_task_send, tv_task_read,tv_info;
    LinearLayout ll_loc, ll_task;

    TitleDeviceView iv_status;
    ImageView iv_flameout;
    ContentDeviceView view_device;


    ViewScale view_scale;

    Disposable disposableAnim, disposableDevice, disposableStop,disposableZkc,disposableCar;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp2 mAnimatorHelp;

    TextureView ttV;

    List<DeviceBean> mDevices = new ArrayList<>();
    DeviceBean mDeviceZkc;//主控车
    int currentPos = 0;

    double meLongitude;
    double meLatitude;

    List<MarkerBean> markers;

    boolean isFlameout;//启动熄火

    @Override
    public int getLayoutId() {
        return R.layout.activity_many;
    }

    @Override
    public void initView() {
        meLongitude = intent.getDoubleExtra("meLongitude", 0);
        meLatitude = intent.getDoubleExtra("meLatitude", 0);

        for (int i = 0; i < MyApplication.getInstance().getDevices().size(); i++) {
            if(MyApplication.getInstance().getDevices().get(i).getType() == 1){
                DeviceBean item = new DeviceBean();
                item.setIp(MyApplication.getInstance().getDevices().get(i).getIp());
                item.setPort(MyApplication.getInstance().getDevices().get(i).getPort());
                item.setType(MyApplication.getInstance().getDevices().get(i).getType());
                item.setRtsp(MyApplication.getInstance().getDevices().get(i).getRtsp());
                item.setNumber(MyApplication.getInstance().getDevices().get(i).getNumber());
                mDevices.add(item);

                ReceiveCarBean bean = new ReceiveCarBean();
                bean.setIp(MyApplication.getInstance().getDevices().get(i).getIp());
                bean.setPort(MyApplication.getInstance().getDevices().get(i).getPort());
                infos.add(bean);
            }
        }

        tv_info = $(R.id.tv_info);
        tv_task_send = $(R.id.tv_task_send);
        tv_task_read = $(R.id.tv_task_read);
        ll_task = $(R.id.ll_task);
        iv_stop = $(R.id.iv_stop);
        view_stop = $(R.id.view_stop);
        iv_flameout = $(R.id.iv_flameout);
        view_device = $(R.id.view_device);
        small_view = $(R.id.small_view);
        iv_status = $(R.id.iv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        iv_shape = $(R.id.iv_shape);
        view_scale = $(R.id.view_scale);
        ll_loc = $(R.id.ll_loc);

        setOnClick(ll_loc, iv_more, iv_route, iv_camera, iv_battery, iv_signal, iv_flameout,tv_task_send,tv_task_read,iv_shape);

        initBaiduMap();
        initSurfaceView();

        ttV.setPivotX(0);
        ttV.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);


        initIvStop();
        initDeviceView();

        SettingInfo.initData();

        initDisposable();

    }

    @SuppressLint("SetTextI18n")
    private void initDisposable() {

        //动画 地图和视频视图加载完了之后  设置地图缩小，如果直接设置地图是小图的话，放大视图会变形。
        disposableAnim = RxBus2.getInstance().toObservable(AnimatorEvent.class, animatorEvent -> {
            if (animatorEvent.isBig() && animatorEvent.isSmall()) {
                mAnimatorHelp.setSmallAnimation();
            }
        });

        //链接状态
        disposableDevice = RxBus2.getInstance().toObservable(ConnectIpEvent.class, event -> {
            for (int i = 0; i < mDevices.size(); i++) {
                if (mDevices.get(i).getIpPort().equals(event.getIpPort())) {
                    if (event.getType() == -1) {
                        mDevices.get(i).setType(event.getType() == 1 ? 1 : 2);
                        setStatusView();
                    }
                }
            }

            if(mDeviceZkc != null && TextUtils.equals(event.getIpPort(),mDeviceZkc.getIpPort())){
                setStop();
            }
        });

        //急停
        disposableStop = RxBus2.getInstance().toObservable(StopShowEvent.class, event -> {
            iv_stop.setAlpha(0f);
            iv_stop.setVisibility(View.VISIBLE);
            iv_stop.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(null);
        });

        //主控车
        disposableZkc = RxBus2.getInstance().toObservable(ZkcEvent.class, event->{
            getDeviceZkc();
            if(isInitData){
                setZkcChange();
            }else{
                initData();
            }
        });

        //无人车信息
        disposableCar = RxBus2.getInstance().toObservable(ReceiveCarEvent.class, event -> {
            for (int i = 0;i<infos.size();i++){
                if(infos.get(i).getIpPort().equals(event.getIpPort())){
                    infos.get(i).setInfo("V:" + event.getN1() + "m/s\nD:" + event.getN2() + "m\n经度:" + event.getN6() + "\n维度:" + event.getN7());
                }
            }

            for (int i=0;i<mDevices.size();i++){
                if(mDevices.get(currentPos).getIpPort().equals(event.getIpPort())){
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
                }
            }

            if(mDeviceZkc == null) return;
            if(TextUtils.isEmpty(mDeviceZkc.getIpPort())) return;
            if(TextUtils.isEmpty(event.getIpPort())) return;
            if (!TextUtils.equals(mDeviceZkc.getIpPort(), event.getIpPort())) return;

            //定位
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

        });
    }

    List<ReceiveCarBean> infos = new ArrayList<>();

    private void initDeviceView() {
        view_device.setOnItemClickListener(new ContentDeviceView.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                for (int i=0;i<infos.size();i++){
                    if(mDevices.get(position).getIpPort().equals(infos.get(i).getIpPort())){
                        if(!TextUtils.isEmpty(infos.get(i).getInfo()))
                        new DeviceInfoDialog(context).setTitle(mDevices.get(position).getIpPort()).setContent(infos.get(i).getInfo()).show();
                    }
                }
            }

            @Override
            public void onLongClick(int position) {
                new InstructDialog(context).setTitle(mDevices.get(position).getIpPort()).setSubmitListener(content -> {
                    SendHelp.sendMsg(mDevices.get(position).getIpPort(), content);
                }).show();
            }
        });
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
                            SendHelp.sendJS(mDeviceZkc.getNumber(), mDeviceZkc.getIpPort());
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

//    private NodePlayer nodePlayer;
    private EasyPlayerClient mStreamRender;

    //初始化视频SurfaceView控件
    @SuppressLint("ClickableViewAccessibility")
    private void initSurfaceView() {
        ttV = findViewById(R.id.ttV);
        mStreamRender = new EasyPlayerClient(this, Constant.KEY, ttV, null, null);
        ttV.setOnClickListener(this);

        ttV.setOnTouchListener((v, event) -> {
            if(!mAnimatorHelp.getSurfaceViewCenter()) return false;
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                x2 = event.getX();
                y2 = event.getY();
                if(y1 - y2 > 100) {
                    LogUtil.e( "向上滑" );
                    if(currentPos < mDevices.size() - 1){
                        currentPos = currentPos + 1;
                        setStatusView();
                        tv_info.setText("");
                        mAnimatorHelp.moveUp();

                        setStart(mDevices.get(currentPos).getRtsp());

                    }
                } else if(y2 - y1 > 100) {
                    LogUtil.e("向下滑");
                    if(currentPos > 0){
                        currentPos = currentPos - 1;
                        tv_info.setText("");
                        setStatusView();
                        mAnimatorHelp.moveDown();

                        setStart(mDevices.get(currentPos).getRtsp());
                    }
                } else if(x1 - x2 > 100) {
                    LogUtil.e("向左滑");
                    if(leftCentreRight == 0){
                        //左边
                        leftCentreRight = 1;
                        mAnimatorHelp.moveLeft();

                        clearCamera();
                        SettingInfo.isCameraSide = true;

                        SendHelp.sendCamera(mDevices.get(currentPos).getNumber(), mDevices.get(currentPos).getIpPort(), 4);

                    }else if(leftCentreRight == 1){
                        //中间
                        leftCentreRight = 2;
                        mAnimatorHelp.moveLeft();

                        clearCamera();
                        SettingInfo.isCameraUp = true;

                        SendHelp.sendCamera(mDevices.get(currentPos).getNumber(), mDevices.get(currentPos).getIpPort(), 1);
                    }

                } else if(x2 - x1 > 100) {
                    LogUtil.e( "向右滑");
                    if(leftCentreRight == 1){
                        //右边
                        leftCentreRight = 0;
                        mAnimatorHelp.moveRight();

                        clearCamera();
                        SettingInfo.isCameraAfter = true;

                        SendHelp.sendCamera(mDevices.get(currentPos).getNumber(), mDevices.get(currentPos).getIpPort(), 5);

                    }else if(leftCentreRight == 2){
                        //中间
                        leftCentreRight = 1;
                        mAnimatorHelp.moveRight();

                        clearCamera();
                        SettingInfo.isCameraUp = true;

                        SendHelp.sendCamera(mDevices.get(currentPos).getNumber(), mDevices.get(currentPos).getIpPort(), 1);
                    }

                }
            }
            return true;
        });
    }


    public void clearCamera(){
        SettingInfo.isCameraUp = false;
        SettingInfo.isCameraSide = false;
        SettingInfo.isCameraAfter = false;
        SettingInfo.isCameraUpZt = false;
        SettingInfo.isCameraUpZtSb = false;
    }


    int leftCentreRight = 1;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    public void setStart(String rtsp){
        if(mStreamRender != null){
            mStreamRender.stop();
            mStreamRender.play(rtsp);
        }
    }

    public void setStop() {
        if (mStreamRender != null) {
            mStreamRender.stop();
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

    //设置任务点
    boolean isSufCenter = true;

    public void setTaskView(boolean isSufCenter) {
        this.isSufCenter = isSufCenter;
        ll_task.setVisibility(isSufCenter ? View.GONE : View.VISIBLE);
    }

    //设备状态
    private void setStatusView() {
        iv_status.setDevice(mDevices);
        view_device.setDevice(mDevices,currentPos);
    }

    public void getDeviceZkc(){
        for (int i=0;i<mDevices.size();i++){
            if(mDevices.get(i).getIpPort().equals(SettingInfo.shapeZkc)){
                mDeviceZkc = mDevices.get(i);
                currentPos = i;
            }
        }
    }


    public void setZkcChange(){
        setStatusView();
        if(mDeviceZkc != null)
            setStart(mDeviceZkc.getRtsp());
    }

    boolean isInitData = false;
    @Override
    public void initData() {
        if(TextUtils.isEmpty(SettingInfo.shapeZkc)){
            intent = new Intent(context, SettingManyActivity.class);
            intent.putExtra("position", Constant.SETTING_MANY_SHAPE);
            startActivity(intent);
            return;
        }

        isInitData = true;

        setStatusView();
        setTaskView(isSufCenter);

        if(mDeviceZkc != null)
            setStart(mDeviceZkc.getRtsp());

        markers = new ArrayList<>();

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
                                markers.get(pos).setLongitude(type);

                                if (isMarkerUn()) {
                                    mBaiduHelper.showMarkerLine(markers);
                                } else {
                                    mBaiduHelper.showMarkers(markers);
                                }
                            }

                            @Override
                            public void onDel() {
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
                //拖拽结束
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
        mAnimatorHelp = new AnimatorHelp2(ttV, mMapView, small_view, isSufCenter -> {
            ll_loc.setVisibility(isSufCenter ? View.GONE : View.VISIBLE);

            setTaskView(isSufCenter);
        });
        mAnimatorHelp.getAnimatorParam();

        //------------------动画 end----------

    }


    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(context, SettingManyActivity.class);
        switch (v.getId()) {
            case R.id.iv_more:
                intent.putExtra("position", Constant.SETTING_MANY_MORE);
                startActivity(intent);
                break;
            case R.id.iv_route:
                intent.putExtra("position", Constant.SETTING_MANY_ROUTE);
                startActivity(intent);
                break;
            case R.id.iv_camera:
                intent.putExtra("position", Constant.SETTING_MANY_CAMERA);
                startActivity(intent);
                break;
            case R.id.iv_battery:
                intent.putExtra("position", Constant.SETTING_MANY_BATTERY);
                startActivity(intent);
                break;
            case R.id.iv_signal:
                intent.putExtra("position", Constant.SETTING_MANY_SIGNAL);
                startActivity(intent);
                break;
            case R.id.iv_shape:
                intent.putExtra("position", Constant.SETTING_MANY_SHAPE);
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
            case R.id.tv_task_send:
                if (!isMarkerUn()) {
                    showShortToast("请先设置途径点");
                    return;
                }
                SendHelp.sendMarker(mDeviceZkc.getNumber(), mDeviceZkc.getIpPort(), markers);

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
            case R.id.iv_flameout://启动，熄火
                new TextDialog(context).setContent(isFlameout ? "是否确认熄火" : "是否确认启动").setSubmitListener(v1 -> {
                    if (isFlameout) {
                        SendHelp.sendXH(mDeviceZkc.getNumber(), mDeviceZkc.getIpPort());
                    } else {
                        SendHelp.sendQD(mDeviceZkc.getNumber(), mDeviceZkc.getIpPort());
                    }
                    isFlameout = !isFlameout;
                }).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        if (mStreamRender != null) {
            mStreamRender.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        if (mStreamRender != null) {
            mStreamRender.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (disposableAnim != null && !disposableAnim.isDisposed())
            disposableAnim.dispose();
        if (disposableDevice != null && !disposableDevice.isDisposed())
            disposableDevice.dispose();
        if (disposableStop != null && !disposableStop.isDisposed())
            disposableStop.dispose();
        if (disposableZkc != null && !disposableZkc.isDisposed())
            disposableZkc.dispose();
        if (disposableCar != null && !disposableCar.isDisposed())
            disposableCar.dispose();
        mMapView.onDestroy();

        MyApplication.getInstance().deviceClear();

        setStop();
        mStreamRender = null;

        super.onDestroy();
    }

}
