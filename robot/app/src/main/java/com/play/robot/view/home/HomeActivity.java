package com.play.robot.view.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.constant.Constant;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.VoteEvent;
import com.play.robot.view.setting.SettingActivity;
import com.play.robot.widget.IvBattery;
import com.play.robot.widget.IvShape;
import com.play.robot.widget.IvSignal;
import com.play.robot.widget.baidu.BaiduHelper;
import com.play.robot.widget.scale.ViewScale;

import java.util.Random;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera;
    IvBattery iv_battery;
    IvSignal iv_signal;
    IvShape iv_shape;
    TextView tv_status;

    ViewScale view_scale;

    Disposable VoteDisposable;

    private MapView mMapView = null;
    // 用于设置个性化地图的样式文件
    BaiduHelper mBaiduHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        tv_status = $(R.id.tv_status);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        iv_shape = $(R.id.iv_shape);
        view_scale = $(R.id.view_scale);

        setOnClick(tv_status, iv_more, iv_route, iv_camera, iv_battery, iv_signal, iv_shape);

        initBaiduMap();
    }

    private void initBaiduMap() {
        mMapView = $(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void initData() {
//        SPUtils.getInstance().putBoolean(SPUtils.IS_LOGIN, true);

        VoteDisposable = RxBus2.getInstance().toObservable(VoteEvent.class, new Consumer<VoteEvent>() {
            @Override
            public void accept(VoteEvent voteEvent) throws Exception {
                view_scale.setValues(voteEvent.getVote());
            }
        });

        mBaiduHelper = new BaiduHelper(context,mMapView);
        mBaiduHelper.setMapCustomStyle();

    }

    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context,SettingActivity.class);
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(VoteDisposable !=null && !VoteDisposable.isDisposed())
            VoteDisposable.dispose();
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
