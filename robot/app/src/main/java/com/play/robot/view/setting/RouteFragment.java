package com.play.robot.view.setting;

import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;
import com.play.robot.widget.SwitchButton;

import androidx.fragment.app.Fragment;

public class RouteFragment extends BaseFragment {

    SwitchButton switch_jlljjd,switch_nxfh,switch_sxfh;
    TextView tv_fslj_v;

    public static Fragment newInstance() {
        RouteFragment fragment = new RouteFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_route;
    }

    @Override
    public void initView() {
        switch_jlljjd = $(R.id.switch_jlljjd);
        switch_nxfh = $(R.id.switch_nxfh);
        switch_sxfh = $(R.id.switch_sxfh);
        tv_fslj_v = $(R.id.tv_fslj_v);

        switch_jlljjd.setChecked(SettingInfo.isRouteJlljjd);
        switch_nxfh.setChecked(SettingInfo.isRouteNxfh);
        switch_sxfh.setChecked(SettingInfo.isRouteSxfh);

        switch_jlljjd.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_jlljjd.setChecked(SettingInfo.isRouteJlljjd =!SettingInfo.isRouteJlljjd);
        });
        switch_nxfh.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_nxfh.setChecked(SettingInfo.isRouteNxfh =!SettingInfo.isRouteNxfh);
        });
        switch_sxfh.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_sxfh.setChecked(SettingInfo.isRouteSxfh =!SettingInfo.isRouteSxfh);
        });

        tv_fslj_v.setOnClickListener(v -> {

        });
    }

    @Override
    public void initData() {

    }
}
