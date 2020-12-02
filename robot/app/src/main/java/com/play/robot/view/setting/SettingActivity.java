package com.play.robot.view.setting;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.base.BaseFragmentAdapter;
import com.play.robot.constant.Constant;
import com.play.robot.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_more, iv_route, iv_camera,iv_battery,iv_signal,iv_shape,iv_close;

    CustomViewPager mViewPager;
    TextView tv_title;

    int pos;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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
        mFragments.add(CameraFragment.newInstance());
        mFragments.add(RouteFragment.newInstance());
        mFragments.add(ShapeFragment.newInstance());
        mFragments.add(SignalFragment.newInstance());
        mFragments.add(BatteryFragment.newInstance());
        mFragments.add(MoreFragment.newInstance());

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1);

        pos = intent.getIntExtra("position",0);

        setSelectView(pos);
    }

    @Override
    public void initData() {

    }

    public void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_camera:
                setSelectView(Constant.SETTING_CAMERA);
                break;
            case R.id.iv_route:
                setSelectView(Constant.SETTING_ROUTE);
                break;
            case R.id.iv_shape:
                setSelectView(Constant.SETTING_SHAPE);
                break;
            case R.id.iv_signal:
                setSelectView(Constant.SETTING_SIGNAL);
                break;
            case R.id.iv_battery:
                setSelectView(Constant.SETTING_BATTERY);
                break;
            case R.id.iv_more:
                setSelectView(Constant.SETTING_MORE);
                break;
        }
    }

    public void setSelectView(int pos){
        mViewPager.setCurrentItem(pos);
        clearView();
        switch (pos){
            case Constant.SETTING_CAMERA:
                tv_title.setText("摄像头设置");
                iv_camera.setImageResource(R.mipmap.setting_camera);
                break;
            case Constant.SETTING_ROUTE:
                tv_title.setText("轨迹记录");
                iv_route.setImageResource(R.mipmap.setting_route);
                break;
            case Constant.SETTING_SHAPE:
                tv_title.setText("无人机编队");
                iv_shape.setImageResource(R.mipmap.setting_shape);
                break;
            case Constant.SETTING_SIGNAL:
                tv_title.setText("通信状态");
                iv_signal.setImageResource(R.mipmap.setting_signal);
                break;
            case Constant.SETTING_BATTERY:
                tv_title.setText("电量");
                iv_battery.setImageResource(R.mipmap.setting_battery);
                break;
            case Constant.SETTING_MORE:
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
}
