package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;
import com.play.robot.widget.SwitchButton;

import androidx.fragment.app.Fragment;

public class CameraFragment extends BaseFragment {

    SwitchButton switch_qzsxt,switch_cssxt,switch_qztz,switch_hssxt,switch_qztzmbsb;

    public static Fragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
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

//        switchButton.setChecked(true);
//        switchButton.isChecked();
//        switchButton.toggle();     //switch state                      //开关状态
//        switchButton.toggle(false);//switch without animation  //无动画切换
//        switchButton.setShadowEffect(true);//disable shadow effect     //禁用阴影效果
//        switchButton.setEnabled(false);//disable button                //禁用按钮
//        switchButton.setEnableEffect(false);//disable the switch animation     //禁用开关动画
        switch_qzsxt.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_qzsxt.setChecked(SettingInfo.isCameraUp =!SettingInfo.isCameraUp);
        });
        switch_cssxt.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_cssxt.setChecked(SettingInfo.isCameraSide =!SettingInfo.isCameraSide);
        });
        switch_hssxt.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_hssxt.setChecked(SettingInfo.isCameraAfter =!SettingInfo.isCameraAfter);
        });
        switch_qztz.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_qztz.setChecked(SettingInfo.isCameraUpZt =!SettingInfo.isCameraUpZt);
        });
        switch_qztzmbsb.setOnCheckedChangeListener((view, isChecked) -> {
            // do your job
            switch_qztzmbsb.setChecked(SettingInfo.isCameraUpZtSb =!SettingInfo.isCameraUpZtSb);
        });

    }

    @Override
    public void initData() {

    }
}
