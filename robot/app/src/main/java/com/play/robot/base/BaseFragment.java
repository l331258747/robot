package com.play.robot.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.play.robot.util.ToastUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Created by Administrator on 2017\10\12 0012.
 */

public abstract  class BaseFragment extends Fragment {

    public BaseActivity activity;
    public Context context;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    //---------------init start--------------------
    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public <T extends View> T $(int id) {
        return (T) view.findViewById(id);
    }
    public <T extends View> T $(View view, int id) {
        return (T) view.findViewById(id);
    }

    public void showShortToast(String msg) {
        ToastUtil.showShortToast(context, msg);
    }

    public void showLongToast(String msg) {
        ToastUtil.showLongToast(context, msg);
    }

    public int getResColor(int resId) {
        return getResources().getColor(resId);
    }

    //----------------------init end--------------------------------------
}
