package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;

import androidx.fragment.app.Fragment;

public class BatteryFragment extends BaseFragment {

    public static Fragment newInstance() {
        BatteryFragment fragment = new BatteryFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_battery;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
