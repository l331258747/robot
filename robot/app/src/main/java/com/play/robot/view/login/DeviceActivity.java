package com.play.robot.view.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.adapter.DeviceAdapter;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.DeviceBean;
import com.play.robot.bean.MyDeviceList;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.dialog.EditDialog;
import com.play.robot.dialog.TextDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_back;
    RecyclerView recyclerView;
    TextView tv_btn;

    DeviceAdapter mAdapter;
    List<DeviceBean> list;

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

        for (int i = 0;i<list.size();i++){
            for (int j=0;j<MyDeviceList.getInstance().getDeviceList().size();j++){
                if(list.get(i).getIp().equals(MyDeviceList.getInstance().getDeviceList().get(j))){
                    list.get(i).setType(1);
                    continue;
                }
            }
        }

        mAdapter.setData(list);
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
                if(list.get(position).getType() == 2){//连不上
                    //TODO 重连
                    MyDeviceList.getInstance().addDevice(list.get(position).getIp());

                    list.get(position).setType(1);
                    mAdapter.notifyItemChanged(position);
                }else if(list.get(position).getType() == 1){//连上
                    //TODO 断开链接
                    MyDeviceList.getInstance().removeDevice(list.get(position).getIp());

                    list.get(position).setType(0);
                    mAdapter.notifyItemChanged(position);
                }else {//未连
                    //TODO 建立连接

                    list.get(position).setType(2);
                    mAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongClick(int position) {
                if(list.get(position).getType() == 1){
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
                if(MySelfInfo.getInstance().getDevice().size() >= 5){
                    showShortToast("最多添加5条");
                    return;
                }

                new EditDialog(context).setSubmitListener((ip, port) -> {
                    DeviceBean item = new DeviceBean();
                    item.setIp(ip + ":" + port);
                    item.setType(0);

                    List<DeviceBean> lists = MySelfInfo.getInstance().getDevice();
                    for (DeviceBean bean : lists) {
                        if (TextUtils.equals(item.getIp(), bean.getIp())) {
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
}
