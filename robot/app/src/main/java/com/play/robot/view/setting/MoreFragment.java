package com.play.robot.view.setting;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;

import androidx.fragment.app.Fragment;

public class MoreFragment extends BaseFragment {

    public static Fragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
