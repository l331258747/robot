package com.play.robot.view.setting;

import com.play.robot.MyApplication;
import com.play.robot.R;
import com.play.robot.adapter.SignalAdapter;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.DeviceBean;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ChangeEvent;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class SignalFragment extends BaseFragment {

    RecyclerView recyclerView;
    SignalAdapter mAdapter;
    List<DeviceBean> list;

    Disposable disposable;

    public static Fragment newInstance() {
        SignalFragment fragment = new SignalFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signal;
    }

    @Override
    public void initView() {

        initRecycler();
    }

    //初始化recyclerview
    private void initRecycler() {
        recyclerView = $(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SignalAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            new TextDialog(context).setContent("切换无人车后数据将重置，是否切换？").setSubmitListener(v -> {
                if (list.get(position).getType() == 2 || list.get(position).getType() == 0) {//连不上
                    setConnection(position);
                }
            }).show();
        });
    }

    public void setConnection(int pos){
        for (int i = 0;i<list.size();i++){
            if(i == pos){
                MyApplication.getInstance().setDeviceType(1, list.get(i).getIp(), list.get(i).getPort());
                list.get(i).setType(1);
            }else{
                MyApplication.getInstance().setDeviceType(0, list.get(i).getIp(), list.get(i).getPort());
                list.get(i).setType(0);
            }
            mAdapter.notifyDataSetChanged();
        }

        RxBus2.getInstance().post(new ChangeEvent(list.get(pos).getIp(),list.get(pos).getPort(),list.get(pos).getType(),list.get(pos).getNumber(),list.get(pos).getRtsp()));
    }

    @Override
    public void initData() {
        //一个个插入，是因为如果直接引用全局，会导致操作混乱，因为是引用对象
        //这样可以页面数据和控制对象分开控制。
        list = new ArrayList<>();
        for (int i = 0; i < MyApplication.getInstance().getDevices().size(); i++) {
            DeviceBean item = new DeviceBean();
            item.setIp(MyApplication.getInstance().getDevices().get(i).getIp());
            item.setPort(MyApplication.getInstance().getDevices().get(i).getPort());
            item.setType(MyApplication.getInstance().getDevices().get(i).getType());
            item.setRtsp(MyApplication.getInstance().getDevices().get(i).getRtsp());
            item.setNumber(MyApplication.getInstance().getDevices().get(i).getNumber());
            list.add(item);
        }
        mAdapter.setData(list);

        //如果断连，修改状态
        disposable = RxBus2.getInstance().toObservable(ConnectIpEvent.class, event -> {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getIpPort().equals(event.getIpPort())) {

                    list.get(i).setType(event.getType() == 1 ? 1 : 2);
                    mAdapter.notifyItemChanged(i);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
