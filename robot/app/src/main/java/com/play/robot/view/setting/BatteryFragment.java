package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.warkiz.widget.IndicatorSeekBar;

import androidx.fragment.app.Fragment;

public class BatteryFragment extends BaseFragment {

    IndicatorSeekBar seekbar_ddlbj;

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
        seekbar_ddlbj = $(R.id.seekbar_ddlbj);
        seekbar_ddlbj.setIndicatorTextFormat("${PROGRESS} %");
    }

    @Override
    public void initData() {

    }
}
