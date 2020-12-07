package com.play.robot.view.home;

import android.content.Intent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.view.home.help.AnimatorHelp;
import com.play.robot.view.home.help.BaiduHelper;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvShape;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.scale.ViewScale;

import java.util.Random;

import io.reactivex.disposables.Disposable;

public class ManyActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera;
    IvBattery iv_battery;
    IvSignal iv_signal;
    IvShape iv_shape;
    TextView tv_status;
    View small_view;
    LinearLayout ll_loc;

    ViewScale view_scale;

    Disposable VoteDisposable,animatorDisposable;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;
    AnimatorHelp mAnimatorHelp;

    SurfaceView mSurfaceView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_many;
    }

    @Override
    public void initView() {
        small_view = $(R.id.small_view);
        tv_status = $(R.id.tv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        iv_shape = $(R.id.iv_shape);
        view_scale = $(R.id.view_scale);
        ll_loc = $(R.id.ll_loc);

        setOnClick(ll_loc,tv_status, iv_more, iv_route, iv_camera, iv_battery, iv_signal, iv_shape);

        initBaiduMap();

        initSurfaceView();

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);

    }

    private void initSurfaceView() {
        mSurfaceView = $(R.id.surfaceView);
        mSurfaceView.setOnClickListener(this);
    }

    private void initBaiduMap() {
        mMapView = $(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(!mAnimatorHelp.getSurfaceViewCenter()) return;
                LogUtil.e("mapView onClick");
                mAnimatorHelp.setAnimator();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
        mMapView.setVisibility(View.GONE);

    }

    @Override
    public void initData() {

        VoteDisposable = RxBus2.getInstance().toObservable(VoteEvent.class, voteEvent -> view_scale.setValues(voteEvent.getVote()));

        animatorDisposable = RxBus2.getInstance().toObservable(AnimatorEvent.class, animatorEvent -> {
            if(animatorEvent.isBig() && animatorEvent.isSmall()){
                mAnimatorHelp.setSmallAnimation();
            }

        });

        mBaiduHelper = new BaiduHelper(context, mMapView);
//        mBaiduHelper.initMap();
//        mBaiduHelper.setMapCustomStyle();
        mAnimatorHelp = new AnimatorHelp(mSurfaceView, mMapView, small_view, isCenter -> {
            ll_loc.setVisibility(isCenter?View.VISIBLE:View.GONE);
        });
        mAnimatorHelp.getAnimatorParam();

    }


    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, SettingActivity.class);
        switch (v.getId()) {
            case R.id.tv_status:

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


                break;

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
            case R.id.iv_shape:
                intent.putExtra("position", Constant.SETTING_SHAPE);
                startActivity(intent);
                break;

            case R.id.surfaceView:
                if(mAnimatorHelp.getSurfaceViewCenter()) return;
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
        if (VoteDisposable != null && !VoteDisposable.isDisposed())
            VoteDisposable.dispose();
        if (animatorDisposable != null && !animatorDisposable.isDisposed())
            animatorDisposable.dispose();
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
