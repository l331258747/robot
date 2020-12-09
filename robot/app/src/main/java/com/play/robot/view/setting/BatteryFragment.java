package com.play.robot.view.setting;

import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;
import com.play.robot.widget.SwitchButton;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import androidx.fragment.app.Fragment;

public class BatteryFragment extends BaseFragment {

    SwitchButton switch_ddlznfh;
    TextView tv_yxsj;

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
        seekbar_ddlbj.setProgress(SettingInfo.batteryBj);
        seekbar_ddlbj.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                SettingInfo.batteryBj = seekBar.getProgress();
            }
        });


        tv_yxsj = $(R.id.tv_yxsj);
        tv_yxsj.setText("00:00");

        switch_ddlznfh = $(R.id.switch_ddlznfh);
        switch_ddlznfh.setChecked(SettingInfo.isBatteryFh);
        switch_ddlznfh.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_ddlznfh.setChecked(SettingInfo.isBatteryFh =!SettingInfo.isBatteryFh);
        });


    }

    @Override
    public void initData() {

    }
}
