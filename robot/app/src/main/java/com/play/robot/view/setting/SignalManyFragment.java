package com.play.robot.view.setting;

import com.play.robot.MyApplication;
import com.play.robot.R;
import com.play.robot.adapter.SignalManyAdapter;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.DeviceBean;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ConnectIpEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class SignalManyFragment extends BaseFragment {

    RecyclerView recyclerView;
    SignalManyAdapter mAdapter;
    List<DeviceBean> list;

    Disposable disposable;

    public static Fragment newInstance() {
        SignalManyFragment fragment = new SignalManyFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signal_many;
    }

    @Override
    public void initView() {
        initRecycler();
    }

    //初始化recyclerview
    private void initRecycler() {
        recyclerView = $(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SignalManyAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

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
