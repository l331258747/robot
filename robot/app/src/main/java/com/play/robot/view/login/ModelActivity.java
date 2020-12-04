package com.play.robot.view.login;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.util.udp.ConnectionDeviceHelp;
import com.play.robot.util.udp.UdpClient;
import com.play.robot.view.home.SingleActivity;

import androidx.core.content.ContextCompat;

public class ModelActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_login, iv_status;
    TextView tv_single, tv_many, tv_device;

    @Override
    public int getLayoutId() {
        return R.layout.activity_model;
    }

    @Override
    public void initView() {
        iv_login = $(R.id.iv_login);
        iv_status = $(R.id.iv_status);
        tv_single = $(R.id.tv_single);
        tv_many = $(R.id.tv_many);
        tv_device = $(R.id.tv_device);

        tv_single.setOnClickListener(this);
        tv_many.setOnClickListener(this);
        tv_device.setOnClickListener(this);
        iv_login.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusView();
    }

    public void setStatusView() {
        iv_login.setImageResource(MySelfInfo.getInstance().isLogin() ? R.mipmap.ic_login : R.mipmap.ic_login_un);

        if (ConnectionDeviceHelp.getInstance().getDeviceList() == null || ConnectionDeviceHelp.getInstance().getDeviceList().size() == 0) {
            tv_device.setTextColor(ContextCompat.getColor(context, R.color.color_text));
            iv_status.setImageResource(R.mipmap.ic_link_un);
        } else {
            tv_device.setTextColor(ContextCompat.getColor(context, R.color.color_green));
            iv_status.setImageResource(R.mipmap.ic_link);
        }
    }

    UdpClient mUdpClient;
    UdpClient mUdpClient2;
    @Override
    public void initData() {
        mUdpClient = new UdpClient();
        mUdpClient2 = new UdpClient();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                startActivity(new Intent(context, LoginActivity.class));
                break;
            case R.id.tv_single:
                if (!MySelfInfo.getInstance().isLogin()) {
                    showShortToast("请先登录");
                    return;
                }
                if (ConnectionDeviceHelp.getInstance().getDeviceList() == null || ConnectionDeviceHelp.getInstance().getDeviceList().size() == 0) {
                    showShortToast("请选择设备");
                    return;
                }
                intent = new Intent(context, SingleActivity.class);
                intent.putExtra("ip",ConnectionDeviceHelp.getInstance().getSingleBean().getIp());
                intent.putExtra("port",ConnectionDeviceHelp.getInstance().getSingleBean().getPort());
                startActivity(intent);

                break;
            case R.id.tv_many:

                break;
            case R.id.tv_device:
                startActivity(new Intent(context, DeviceActivity.class));
                break;
        }
    }

}
