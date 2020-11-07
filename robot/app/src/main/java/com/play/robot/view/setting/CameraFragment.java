package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.widget.SwitchButton;

import androidx.fragment.app.Fragment;

public class CameraFragment extends BaseFragment {

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

        SwitchButton switchButton = $(R.id.switch_qzsxt);

//        switchButton.setChecked(true);
//        switchButton.isChecked();
//        switchButton.toggle();     //switch state                      //开关状态
//        switchButton.toggle(false);//switch without animation  //无动画切换
//        switchButton.setShadowEffect(true);//disable shadow effect     //禁用阴影效果
//        switchButton.setEnabled(false);//disable button                //禁用按钮
//        switchButton.setEnableEffect(false);//disable the switch animation     //禁用开关动画
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // do your job
            }
        });

    }

    @Override
    public void initData() {

    }
}
