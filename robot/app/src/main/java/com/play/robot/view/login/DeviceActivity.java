package com.play.robot.view.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.adapter.DeviceAdapter;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.dialog.EditDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;
import com.play.robot.util.udp.ConnectionDeviceHelp;
import com.play.robot.util.udp.UdpClient;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class DeviceActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_back;
    RecyclerView recyclerView;
    TextView tv_btn;

    DeviceAdapter mAdapter;
    List<DeviceBean> list;

    Disposable disposable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_device;
    }

    @Override
    public void initView() {
        iv_back = $(R.id.iv_back);
        recyclerView = $(R.id.recycler_view);
        tv_btn = $(R.id.tv_btn);

        iv_back.setOnClickListener(this);
        tv_btn.setOnClickListener(this);

        initRecycler();
    }

    @Override
    public void initData() {
        list = MySelfInfo.getInstance().getDevice();

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < ConnectionDeviceHelp.getInstance().getDeviceList().size(); j++) {
                if (list.get(i).getIpPort().equals(ConnectionDeviceHelp.getInstance().getDeviceList().get(j).getIpPort())) {
                    list.get(i).setType(1);
                    continue;
                }
            }
        }

        mAdapter.setData(list);

        disposable = RxBus2.getInstance().toObservable(ConnectIpEvent.class, event -> {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getIpPort().equals(event.getIpPort())) {

                    list.get(i).setType(event.getType() == 1 ? 1 : 2);
                    mAdapter.notifyItemChanged(i);

                    if(event.getType() == -1){
                        ConnectionDeviceHelp.getInstance().removeDevice(event.getIp(), event.getPort());
                    }

                    list.get(i).setType(event.getType() == 1 ? 1 : 2);
                    mAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });
    }

    //初始化recyclerview
    private void initRecycler() {
        recyclerView = $(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DeviceAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (list.get(position).getType() == 2) {//连不上
                    UdpClient mUdpClient = new UdpClient();
                    mUdpClient.connect(list.get(position).getIp(), list.get(position).getPort());

                    ConnectionDeviceHelp.getInstance().addDevice(mUdpClient, list.get(position).getIp(), list.get(position).getPort());

                    list.get(position).setType(1);
                    mAdapter.notifyItemChanged(position);
                } else if (list.get(position).getType() == 1) {//连上
                    ConnectionDeviceHelp.getInstance().removeDevice(list.get(position).getIp(), list.get(position).getPort());

                    list.get(position).setType(0);
                    mAdapter.notifyItemChanged(position);
                } else {//未连:连接，加入全局，显示变化
                    UdpClient mUdpClient = new UdpClient();
                    mUdpClient.connect(list.get(position).getIp(), list.get(position).getPort());

                    ConnectionDeviceHelp.getInstance().addDevice(mUdpClient, list.get(position).getIp(), list.get(position).getPort());

                    list.get(position).setType(1);
                    mAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongClick(int position) {
                if (list.get(position).getType() == 1) {
                    showShortToast("请在断开链接的时候删除");
                    return;
                }

                new TextDialog(context).setContent("确认删除？").setSubmitListener(v -> {
                    MySelfInfo.getInstance().removeDevice(mAdapter.getData(position));
                    list.remove(position);
                    mAdapter.setData(list);
                }).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_btn:
                if (MySelfInfo.getInstance().getDevice().size() >= 5) {
                    showShortToast("最多添加5条");
                    return;
                }

                new EditDialog(context).setSubmitListener((ip, port) -> {
                    DeviceBean item = new DeviceBean();
                    item.setIp(ip);
                    item.setPort(port);
                    item.setType(0);

                    List<DeviceBean> lists = MySelfInfo.getInstance().getDevice();
                    for (DeviceBean bean : lists) {
                        if (TextUtils.equals(item.getIpPort(), bean.getIpPort())) {
                            showShortToast("添加的ip相同");
                            return;
                        }
                    }

                    MySelfInfo.getInstance().addDevice(item);
                    list.add(item);
                    mAdapter.setData(list);
                }).show();
                break;
        }
    }


//    public void test(){
//        //发送
//        if (mUdpClient.isConnect()) {
//            String wSend = "hhhhhhhhhh";
//            byte[] data = wSend.getBytes();
//            send(mUdpClient,data);
//        } else {
//            showShortToast("尚未连接，请连接Socket");
//        }
//
//        //取消链接
//        disconnect(mUdpClient);
//        disconnect(mUdpClient2);
//
//
//        //链接
//        connect(mUdpClient, ip, Integer.parseInt(port));
//        connect(mUdpClient2, ip, Integer.parseInt(port2));
//
//    }
//    /**
//     * socket send
//     */
//    private void send(UdpClient mUdpClient,byte[] data) {
//        mUdpClient.sendByteCmd(data, 1001);
//    }
//
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
