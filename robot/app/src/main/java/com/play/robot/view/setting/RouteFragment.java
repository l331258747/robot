package com.play.robot.view.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;
import com.play.robot.dialog.TextDialog;
import com.play.robot.view.home.help.SendHelp;
import com.play.robot.widget.SwitchButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RouteFragment extends BaseFragment implements View.OnClickListener {

    SwitchButton switch_fh;
    TextView btn_ljd, btn_ksjl, btn_wcjl;
    String ipPort;

    public static Fragment newInstance(String ipPort) {
        RouteFragment fragment = new RouteFragment();
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
        return R.layout.fragment_route;
    }

    @Override
    public void initView() {
        switch_fh = $(R.id.switch_fh);
        btn_ljd = $(R.id.btn_ljd);
        btn_ksjl = $(R.id.btn_ksjl);
        btn_wcjl = $(R.id.btn_wcjl);

        switch_fh.setChecked(SettingInfo.isRouteFh);

        switch_fh.setOnCheckedChangeListener((view, isChecked) -> {
            switch_fh.setChecked(SettingInfo.isRouteFh = isChecked);
        });


        btn_ljd.setOnClickListener(this);
        btn_ksjl.setOnClickListener(this);
        btn_wcjl.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ljd:
                new TextDialog(context).setContent("是否发送路径点").setSubmitListener(v1 ->
                        SendHelp.sendFh(ipPort, 3, SettingInfo.isRouteFh ? 1 : 2)
                ).show();

                break;
            case R.id.btn_ksjl:
                new TextDialog(context).setContent("是否发送路径点").setSubmitListener(v1 ->
                        SendHelp.sendFh(ipPort, 1, SettingInfo.isRouteFh ? 1 : 2)
                ).show();
                break;
            case R.id.btn_wcjl:
                new TextDialog(context).setContent("是否发送路径点").setSubmitListener(v1 ->
                        SendHelp.sendFh(ipPort, 2, SettingInfo.isRouteFh ? 1 : 2)
                ).show();
                break;
        }
    }
}
