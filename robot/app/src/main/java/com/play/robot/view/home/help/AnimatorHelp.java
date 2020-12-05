package com.play.robot.view.home.help;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.MapView;
import com.play.robot.util.LogUtil;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.AnimatorEvent;

public class AnimatorHelp {

    SurfaceView mSurfaceView;
    MapView mMapView;
    View small_view;
    LinearLayout ll_loc;

    public AnimatorHelp(SurfaceView surfaceView, MapView mMapView, View small_view, LinearLayout ll_loc) {
        mSurfaceView = surfaceView;
        this.mMapView = mMapView;
        this.small_view = small_view;
        this.ll_loc = ll_loc;
    }

    float bigViewWidth;
    float bigViewHeight;
    float smallViewWidth;
    float smallViewHeight;
    int bigViewX, bigViewY, smallViewX, smallViewY;
    boolean isSurfaceViewCenter = true;
    AnimatorEvent mAnimatorEvent;

    //获取动画需要的参数
    public void getAnimatorParam() {
        mAnimatorEvent = new AnimatorEvent();
        ViewTreeObserver vto2 = mSurfaceView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bigViewWidth = mSurfaceView.getWidth();
                bigViewHeight = mSurfaceView.getHeight();

                int[] location = new int[2];
                mSurfaceView.getLocationOnScreen(location);
                bigViewX = location[0];
                bigViewY = location[1];

                mAnimatorEvent.setBig(true);
                RxBus2.getInstance().post(mAnimatorEvent);
            }
        });

        ViewTreeObserver vto1 = small_view.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                small_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                smallViewWidth = small_view.getWidth();
                smallViewHeight = small_view.getHeight();

                int[] location2 = new int[2];
                small_view.getLocationOnScreen(location2);
                smallViewX = location2[0];
                smallViewY = location2[1];

                mAnimatorEvent.setSmall(true);
                RxBus2.getInstance().post(mAnimatorEvent);
            }
        });
    }

    float scaleXToSmall, scaleYToSmall, translationXToSmall, translationYToSmall;

    public void setSmallAnimation() {//地图变小
        scaleXToSmall = smallViewWidth / bigViewWidth;
        scaleYToSmall = smallViewHeight / bigViewHeight;

//        float scaleXToBig = bigViewWidth / smallViewWidth;
//        float scaleYToBig = bigViewHeight / smallViewHeight;

        translationXToSmall = smallViewX - bigViewX;
        translationYToSmall = smallViewY - bigViewY;
//        float translationXToBig = bigViewX - smallViewX;
//        float translationYToBig = bigViewY - smallViewY;


        //缩放---ofFloat用4个参数的ofFloat
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mMapView, "scaleX", 1, scaleXToSmall);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mMapView, "scaleY", 1, scaleYToSmall);

        // 获得当前按钮的位置
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mMapView, "translationX", 0, translationXToSmall);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mMapView, "translationY", 0, translationYToSmall);

        //组合
        AnimatorSet set = new AnimatorSet();
        /**
         * 动画执行
         */
        set.play(objectAnimator1)
                .with(objectAnimator2)
                .with(objectAnimator3)
                .with(objectAnimator4)
        ;
        set.setDuration(500);
        set.start();

        set.addListener(new MyAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMapView.setVisibility(View.VISIBLE);
            }
        });
    }

    //设置动画
    public void setAnimator() {

        if (isSurfaceViewCenter) {//地图变大，视频变小

            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mSurfaceView, "scaleX", 1, scaleXToSmall);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mSurfaceView, "scaleY", 1, scaleYToSmall);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mSurfaceView, "translationX", 0, translationXToSmall);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mSurfaceView, "translationY", 0, translationYToSmall);


            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(mMapView, "scaleX", scaleXToSmall, 1);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(mMapView, "scaleY", scaleYToSmall, 1);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(mMapView, "translationX", translationXToSmall, 0);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(mMapView, "translationY", translationYToSmall, 0);

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

            set.addListener(new MyAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    int[] location = new int[2];
                    mSurfaceView.getLocationOnScreen(location);
                    LogUtil.e("bigViewX:" + location[0] + " bigViewY:" + location[1]);
                    ll_loc.setVisibility(View.VISIBLE);

                }
            });

        } else {//地图变小，视频变大
            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mSurfaceView, "scaleX", scaleXToSmall, 1);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mSurfaceView, "scaleY", scaleYToSmall, 1);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mSurfaceView, "translationX", translationXToSmall, 0);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mSurfaceView, "translationY", translationYToSmall, 0);


            //缩放---ofFloat用4个参数的ofFloat
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(mMapView, "scaleX", 1, scaleXToSmall);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(mMapView, "scaleY", 1, scaleYToSmall);

            // 获得当前按钮的位置
            ObjectAnimator objectAnimator7 = ObjectAnimator.ofFloat(mMapView, "translationX", 0, translationXToSmall);
            ObjectAnimator objectAnimator8 = ObjectAnimator.ofFloat(mMapView, "translationY", 0, translationYToSmall);

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

            set.addListener(new MyAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    int[] location = new int[2];
                    mSurfaceView.getLocationOnScreen(location);
                    LogUtil.e("bigViewX:" + location[0] + " bigViewY:" + location[1]);
                    ll_loc.setVisibility(View.GONE);
                }
            });

        }

        isSurfaceViewCenter = !isSurfaceViewCenter;

    }

    public boolean getSurfaceViewCenter() {
        return isSurfaceViewCenter;
    }
}
