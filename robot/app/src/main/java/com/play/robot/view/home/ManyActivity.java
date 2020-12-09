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
import com.play.robot.MyApplication;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MarkerBean;
import com.play.robot.bean.SettingInfo;
import com.play.robot.constant.Constant;
import com.play.robot.dialog.DeviceInfoDialog;
import com.play.robot.dialog.InstructDialog;
import com.play.robot.dialog.MarkerDialog;
import com.play.robot.dialog.MarkerModifyDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.rxbus.rxbusEvent.StopShowEvent;
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.home.help.MyOnMarkerClickListener;
import com.play.robot.view.home.help.MyOnMarkerDragListener;
import com.play.robot.view.setting.SettingManyActivity;
import com.play.robot.widget.ContentDeviceView;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvShape;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.TitleDeviceView;
import com.play.robot.widget.scale.ViewScale;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ManyActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera, iv_stop;
    IvBattery iv_battery;
    IvSignal iv_signal;
    IvShape iv_shape;
    View small_view, view_stop;
    TextView tv_task_send, tv_task_read;
    LinearLayout ll_loc, ll_task;

    TitleDeviceView iv_status;
    ImageView iv_flameout;
    ContentDeviceView view_device;


    ViewScale view_scale;

    Disposable disposableRuler, disposableAnim, disposableDevice, disposableStop;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    SurfaceView mSurfaceView;

    List<DeviceBean> mDevices = new ArrayList<>();

    double meLongitude;
    double meLatitude;

    List<MarkerBean> markers;

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
                mDevices.add(item);
            }
        }

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

        setOnClick(ll_loc, iv_more, iv_route, iv_camera, iv_battery, iv_signal, iv_flameout,tv_task_send,tv_task_read);

        initBaiduMap();
        initSurfaceView();

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);


        initIvStop();
        initDeviceView();

        setStatusView();
        setTaskView(isSufCenter);

    }

    private void initDeviceView() {
        view_device.setOnItemClickListener(new ContentDeviceView.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String str = "V:10m/s\nS:100m\nD:50m\n精度:12.325415\n维度:112.324567";
                new DeviceInfoDialog(context).setTitle(mDevices.get(position).getIpPort()).setContent(str).show();
            }

            @Override
            public void onLongClick(int position) {
                new InstructDialog(context).setTitle(mDevices.get(position).getIpPort()).setSubmitListener(content -> {
                    showShortToast("指令：" + content);
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
        view_device.setDevice(mDevices);
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
            for (int i = 0; i < mDevices.size(); i++) {
                if (mDevices.get(i).getIpPort().equals(event.getIpPort())) {
                    if (event.getType() == -1) {
                        mDevices.get(i).setType(event.getType() == 1 ? 1 : 2);
                        setStatusView();
                    }
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


    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, SettingManyActivity.class);
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
                break;
            case R.id.tv_task_read:
                showShortToast("读取途径点");
                break;
//            case R.id.iv_sign://信息，指令
//                String str = "V:10m/s\nS:100m\nD:50m\n精度:12.325415\n维度:112.324567";
//                new DeviceInfoDialog(context).setTitle(mDevice.getIpPort()).setContent(str).show();
//                break;
            case R.id.iv_flameout://启动，熄火
                new TextDialog(context).setContent("是否确认熄火/启动").show();
                break;
        }
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
        mMapView.onDestroy();
    }

}
