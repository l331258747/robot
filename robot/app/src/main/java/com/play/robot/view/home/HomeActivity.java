package com.play.robot.view.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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

    SurfaceView mSurfaceView;

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

        initSurfaceView();
    }

    private void initSurfaceView() {
        mSurfaceView = $(R.id.surfaceView);
        mSurfaceView.setOnClickListener(this);
    }

    private void initBaiduMap() {
        mMapView = $(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(!isSurfaceViewCenter) return;
                LogUtil.e("mapView onClick");
                setAnimator();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });

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

        mBaiduHelper = new BaiduHelper(context, mMapView);
        mBaiduHelper.setMapCustomStyle();

        getAnimatorParam();

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
                if(isSurfaceViewCenter) return;
                LogUtil.e("surfaceView onClick");
                setAnimator();
                break;
        }
    }


    float bigViewWidth;
    float bigViewHeight;
    float smallViewWidth;
    float smallViewHeight;
    int bigViewX, bigViewY, smallViewX, smallViewY;
    boolean isSurfaceViewCenter = true;

    //获取动画需要的参数
    public void getAnimatorParam() {
        ViewTreeObserver vto2 = mSurfaceView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bigViewWidth = mSurfaceView.getWidth();
                bigViewHeight = mSurfaceView.getHeight();
                LogUtil.e("bigViewWidth：" + bigViewWidth);
                LogUtil.e("bigViewHeight：" + bigViewHeight);

                int[] location = new int[2];
                mSurfaceView.getLocationOnScreen(location);
                bigViewX = location[0];
                bigViewY = location[1];
                LogUtil.e("bigViewX:" + bigViewX + " bigViewY:" + bigViewY);
            }
        });

        ViewTreeObserver vto1 = mMapView.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                smallViewWidth = mMapView.getWidth();
                smallViewHeight = mMapView.getHeight();
                LogUtil.e("smallViewWidth：" + smallViewWidth);
                LogUtil.e("smallViewHeight：" + smallViewHeight);

                int[] location2 = new int[2];
                mMapView.getLocationOnScreen(location2);
                smallViewX = location2[0];
                smallViewY = location2[1];
                LogUtil.e("smallViewX:" + smallViewX + " smallViewY:" + smallViewY);
            }
        });
    }

    //设置动画
    public void setAnimator() {

        float scaleXToSmall = smallViewWidth / bigViewWidth;
        float scaleYToSmall = smallViewHeight / bigViewHeight;

        float scaleXToBig = bigViewWidth / smallViewWidth;
        float scaleYToBig = bigViewHeight / smallViewHeight;

        float translationXToSamll = smallViewX - bigViewX;
        float translationYToSamll = smallViewY - bigViewY;
        float translationXToBig = bigViewX - smallViewX;
        float translationYToBig = bigViewY - smallViewY;

        mSurfaceView.setPivotX(0);
        mSurfaceView.setPivotY(0);

        mMapView.setPivotX(0);
        mMapView.setPivotY(0);

        if (isSurfaceViewCenter) {

            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mSurfaceView, "scaleX", 1, scaleXToSmall);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mSurfaceView, "scaleY", 1, scaleYToSmall);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mSurfaceView, "translationX", 0, translationXToSamll);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mSurfaceView, "translationY", 0, translationYToSamll);


            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(mMapView, "scaleX", 1, scaleXToBig);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(mMapView, "scaleY", 1, scaleYToBig);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(mMapView, "translationX", 0, translationXToBig);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(mMapView, "translationY", 0, translationYToBig);

            //组合
            AnimatorSet set = new AnimatorSet();
            /**
             * 动画执行
             */
            set.play(objectAnimator1)
                    .with(objectAnimator2)
                    .with(objectAnimator3)
                    .with(objectAnimator4)
                    .with(objectAnimator5)
                    .with(objectAnimator6)
                    .with(objectAnimator7)
                    .with(objectAnimator8)
            ;
            set.setDuration(500);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int[] location = new int[2];
                    mSurfaceView.getLocationOnScreen(location);
                    LogUtil.e("bigViewX:" + location[0] + " bigViewY:" + location[1]);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        } else {
            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mSurfaceView, "scaleX", scaleXToSmall, 1);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mSurfaceView, "scaleY", scaleYToSmall, 1);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mSurfaceView, "translationX", translationXToSamll, 0);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mSurfaceView, "translationY", translationYToSamll, 0);


            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(mMapView, "scaleX", scaleXToBig, 1);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(mMapView, "scaleY", scaleYToBig, 1);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(mMapView, "translationX", translationXToBig, 0);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(mMapView, "translationY", translationYToBig, 0);

            //组合
            AnimatorSet set = new AnimatorSet();
            /**
             * 动画执行
             */
            set.play(objectAnimator1)
                    .with(objectAnimator2)
                    .with(objectAnimator3)
                    .with(objectAnimator4)
                    .with(objectAnimator5)
                    .with(objectAnimator6)
                    .with(objectAnimator7)
                    .with(objectAnimator8)
            ;
            set.setDuration(500);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int[] location = new int[2];
                    mSurfaceView.getLocationOnScreen(location);
                    LogUtil.e("bigViewX:" + location[0] + " bigViewY:" + location[1]);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }

        isSurfaceViewCenter = !isSurfaceViewCenter;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VoteDisposable != null && !VoteDisposable.isDisposed())
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
