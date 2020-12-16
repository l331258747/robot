package com.play.robot.view.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.base.BaseFragmentAdapter;
import com.play.robot.bean.SettingInfo;
import com.play.robot.constant.Constant;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ZkcEvent;
import com.play.robot.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;

public class SettingManyActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera,iv_battery,iv_signal,iv_shape,iv_close;

    CustomViewPager mViewPager;
    TextView tv_title;

    int pos;

    CameraFragment mCameraFragment;
    RouteFragment mRouteFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_many;
    }

    @Override
    public void initView() {
        tv_title = $(R.id.tv_title);
        iv_close = $(R.id.iv_close);
        mViewPager = $(R.id.viewpager);
        iv_more = $(R.id.iv_more);
        iv_route = $(R.id.iv_route);
        iv_camera = $(R.id.iv_camera);
        iv_battery = $(R.id.iv_battery);
        iv_signal = $(R.id.iv_signal);
        iv_shape = $(R.id.iv_shape);

        setOnClick(iv_close, iv_more, iv_route, iv_camera, iv_battery, iv_signal, iv_shape);


        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(mCameraFragment = (CameraFragment) CameraFragment.newInstance(SettingInfo.shapeZkc));
        mFragments.add(mRouteFragment = (RouteFragment) RouteFragment.newInstance(SettingInfo.shapeZkc));
        mFragments.add(ShapeFragment.newInstance());
        mFragments.add(SignalManyFragment.newInstance());
        mFragments.add(BatteryFragment.newInstance());
        mFragments.add(MoreFragment.newInstance());

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1);

        pos = intent.getIntExtra("position",0);

        setSelectView(pos);
    }

    Disposable disposableZkc;

    @Override
    public void initData() {
        disposableZkc = RxBus2.getInstance().toObservable(ZkcEvent.class, event->{
            mCameraFragment.setIpPort(SettingInfo.shapeZkc);
            mRouteFragment.setIpPort(SettingInfo.shapeZkc);

        });
    }

    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(SettingInfo.shapeZkc) || TextUtils.isEmpty(SettingInfo.shapeMode)){
            showShortToast("请选择主控车和编队模式");
            return;
        }

        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_camera:
                setSelectView(Constant.SETTING_MANY_CAMERA);
                break;
            case R.id.iv_route:
                setSelectView(Constant.SETTING_MANY_ROUTE);
                break;
            case R.id.iv_shape:
                setSelectView(Constant.SETTING_MANY_SHAPE);
                break;
            case R.id.iv_signal:
                setSelectView(Constant.SETTING_MANY_SIGNAL);
                break;
            case R.id.iv_battery:
                setSelectView(Constant.SETTING_MANY_BATTERY);
                break;
            case R.id.iv_more:
                setSelectView(Constant.SETTING_MANY_MORE);
                break;
        }
    }

    public void setSelectView(int pos){
        mViewPager.setCurrentItem(pos);
        clearView();
        switch (pos){
            case Constant.SETTING_MANY_CAMERA:
                tv_title.setText("摄像头设置");
                iv_camera.setImageResource(R.mipmap.setting_camera);
                break;
            case Constant.SETTING_MANY_ROUTE:
                tv_title.setText("轨迹记录");
                iv_route.setImageResource(R.mipmap.setting_route);
                break;
            case Constant.SETTING_MANY_SHAPE:
                tv_title.setText("无人机编队");
                iv_shape.setImageResource(R.mipmap.setting_shape);
                break;
            case Constant.SETTING_MANY_SIGNAL:
                tv_title.setText("通信状态");
                iv_signal.setImageResource(R.mipmap.setting_signal);
                break;
            case Constant.SETTING_MANY_BATTERY:
                tv_title.setText("电量");
                iv_battery.setImageResource(R.mipmap.setting_battery);
                break;
            case Constant.SETTING_MANY_MORE:
                tv_title.setText("通用设置");
                iv_more.setImageResource(R.mipmap.setting_more);
                break;
        }
    }

    public void clearView(){
        iv_camera.setImageResource(R.mipmap.setting_camera_un);
        iv_route.setImageResource(R.mipmap.setting_route_un);
        iv_shape.setImageResource(R.mipmap.setting_shape_un);
        iv_signal.setImageResource(R.mipmap.setting_signal_un);
        iv_battery.setImageResource(R.mipmap.setting_battery_un);
        iv_more.setImageResource(R.mipmap.setting_more_un);
    }

    @Override
    public void onBackPressed() {
        if(TextUtils.isEmpty(SettingInfo.shapeZkc) || TextUtils.isEmpty(SettingInfo.shapeMode)){
            showShortToast("请选择主控车和编队模式");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableZkc != null && !disposableZkc.isDisposed())
            disposableZkc.dispose();
    }
}
