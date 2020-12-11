package com.play.robot.view.setting;

import android.os.Bundle;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;
import com.play.robot.view.home.help.SendHelp;
import com.play.robot.widget.SwitchButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends BaseFragment {

    SwitchButton switch_qzsxt, switch_cssxt, switch_qztz, switch_hssxt, switch_qztzmbsb;
    String ipPort;

    public static Fragment newInstance(String ipPort) {
        CameraFragment fragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ipPort", ipPort);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ipPort = bundle.getString("ipPort");
        }
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    public void initView() {

        switch_qzsxt = $(R.id.switch_qzsxt);
        switch_cssxt = $(R.id.switch_cssxt);
        switch_hssxt = $(R.id.switch_hssxt);
        switch_qztz = $(R.id.switch_qztz);
        switch_qztzmbsb = $(R.id.switch_qztzmbsb);

        switch_qzsxt.setChecked(SettingInfo.isCameraUp);
        switch_cssxt.setChecked(SettingInfo.isCameraSide);
        switch_hssxt.setChecked(SettingInfo.isCameraAfter);
        switch_qztz.setChecked(SettingInfo.isCameraUpZt);
        switch_qztzmbsb.setChecked(SettingInfo.isCameraUpZtSb);

        switch_qzsxt.setOnCheckedChangeListener((view, isChecked) -> {
            clear();
            switch_qzsxt.setChecked(SettingInfo.isCameraUp = isChecked);

            if (isChecked)
                SendHelp.sendCamera(ipPort, 1);

        });
        switch_cssxt.setOnCheckedChangeListener((view, isChecked) -> {
            clear();
            switch_cssxt.setChecked(SettingInfo.isCameraSide = isChecked);

            SendHelp.sendCamera(ipPort, 4);
        });
        switch_hssxt.setOnCheckedChangeListener((view, isChecked) -> {
            clear();
            switch_hssxt.setChecked(SettingInfo.isCameraAfter = isChecked);


            if (isChecked)
                SendHelp.sendCamera(ipPort, 5);
        });
        switch_qztz.setOnCheckedChangeListener((view, isChecked) -> {
            clear();
            switch_qztz.setChecked(SettingInfo.isCameraUpZt = isChecked);

            if (isChecked)
                SendHelp.sendCamera(ipPort, 2);
        });
        switch_qztzmbsb.setOnCheckedChangeListener((view, isChecked) -> {
            clear();
            switch_qztzmbsb.setChecked(SettingInfo.isCameraUpZtSb = isChecked);

            if (isChecked)
                SendHelp.sendCamera(ipPort, 3);
        });
    }

    public void clear(){
        switch_qzsxt.setChecked(SettingInfo.isCameraUp = false);
        switch_cssxt.setChecked(SettingInfo.isCameraSide = false);
        switch_hssxt.setChecked(SettingInfo.isCameraAfter = false);
        switch_qztz.setChecked(SettingInfo.isCameraUpZt = false);
        switch_qztzmbsb.setChecked(SettingInfo.isCameraUpZtSb = false);
    }

    @Override
    public void initData() {

    }
}
