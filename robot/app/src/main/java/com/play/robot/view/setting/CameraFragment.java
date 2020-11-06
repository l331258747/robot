package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;

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

    }

    @Override
    public void initData() {

    }
}
