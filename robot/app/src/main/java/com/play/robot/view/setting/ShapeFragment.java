package com.play.robot.view.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.play.robot.MyApplication;
import com.play.robot.R;
import com.play.robot.adapter.MyShapeAdapter;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.MyListBean;
import com.play.robot.bean.SettingInfo;
import com.play.robot.dialog.MyListDialog;
import com.play.robot.dialog.TextDialog;
import com.play.robot.util.rxbus.RxBus2;
import com.play.robot.util.rxbus.rxbusEvent.ZkcEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShapeFragment extends BaseFragment implements View.OnClickListener {

    TextView btn_bdms,btn_zkcxz,btn_bgsc,btn_gsc,tv_add,tv_send;
    RecyclerView recyclerView;

    List<MyListBean> listBdms;
    List<MyListBean> listDevice;
    List<String> list;

    MyShapeAdapter mAdapter;

    public static Fragment newInstance() {
        ShapeFragment fragment = new ShapeFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shape;
    }

    @Override
    public void initView() {
        btn_bdms = $(R.id.btn_bdms);
        btn_zkcxz = $(R.id.btn_zkcxz);
        btn_bgsc = $(R.id.btn_bgsc);
        btn_gsc = $(R.id.btn_gsc);
        tv_add = $(R.id.tv_add);
        tv_send = $(R.id.tv_send);

        btn_bdms.setOnClickListener(this);
        btn_zkcxz.setOnClickListener(this);
        btn_bgsc.setOnClickListener(this);
        btn_gsc.setOnClickListener(this);
        tv_add.setOnClickListener(this);
        tv_send.setOnClickListener(this);


        initRecycler();

    }

    //初始化recyclerview
    private void initRecycler() {
        recyclerView = $(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyShapeAdapter(context, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            new TextDialog(context).setContent("是否删除").setSubmitListener(v -> {
                list.remove(position);
                mAdapter.setData(list);
            }).show();
        });
    }

    @Override
    public void initData() {

        btn_bdms.setText(SettingInfo.shapeMode);
        btn_zkcxz.setText(SettingInfo.shapeZkcBH);
        list = SettingInfo.shapeList;
        mAdapter.setData(list);

        listBdms = new ArrayList<>();
        listBdms.add(getMyListBean(0,"三角形编队"));
        listBdms.add(getMyListBean(1,"横向编队"));
        listBdms.add(getMyListBean(2,"纵向编队"));


        listDevice = new ArrayList<>();
        for (int i = 0; i < MyApplication.getInstance().getDevices().size(); i++) {
            if(MyApplication.getInstance().getDevices().get(i).getType() == 1){
                MyListBean item = new MyListBean();
                item.setId(MyApplication.getInstance().getDevices().get(i).getType());
                item.setContent(MyApplication.getInstance().getDevices().get(i).getNumber());
                item.setIp(MyApplication.getInstance().getDevices().get(i).getIp());
                item.setPort(MyApplication.getInstance().getDevices().get(i).getPort());
                listDevice.add(item);
            }
        }
    }

    public MyListBean getMyListBean(int id,String content){
        MyListBean item = new MyListBean();
        item.setId(id);
        item.setContent(content);
        return item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bdms:
                new MyListDialog(context).setData(listBdms).setOnItemClickListener(position -> {
                    btn_bdms.setText(SettingInfo.shapeMode = listBdms.get(position).getContent());

                    if(!TextUtils.isEmpty(SettingInfo.shapeMode) && !TextUtils.isEmpty(SettingInfo.shapeZkc)){
                        RxBus2.getInstance().post(new ZkcEvent());
                    }
                }).show();

                break;
            case R.id.btn_zkcxz:
                new MyListDialog(context).setData(listDevice).setOnItemClickListener(position -> {
                    SettingInfo.shapeZkc = listDevice.get(position).getIpPort();
                    SettingInfo.shapeZkcBH = listDevice.get(position).getContent();
                    btn_zkcxz.setText(SettingInfo.shapeZkcBH);
                    if(!TextUtils.isEmpty(SettingInfo.shapeMode) && !TextUtils.isEmpty(SettingInfo.shapeZkc)){
                        RxBus2.getInstance().post(new ZkcEvent());
                    }
                }).show();

                break;
            case R.id.btn_bgsc:
                new MyListDialog(context).setData(listDevice).setOnItemClickListener(position -> {
                    btn_bgsc.setText(listDevice.get(position).getContent());
                }).show();
                break;
            case R.id.btn_gsc:
                new MyListDialog(context).setData(listDevice).setOnItemClickListener(position -> {
                    btn_gsc.setText(listDevice.get(position).getContent());
                }).show();
                break;
            case R.id.tv_add:
                if(TextUtils.isEmpty(btn_bgsc.getText().toString())){
                    showShortToast("请选择被跟随车");
                    return;
                }
                if(TextUtils.isEmpty(btn_bgsc.getText().toString())){
                    showShortToast("请选择跟随车");
                    return;
                }
                String str = btn_bgsc.getText().toString() + " -> " + btn_gsc.getText().toString();
                list.add(str);
                mAdapter.setData(list);
                break;
            case R.id.tv_send:
                if(list.size() == 0){
                    showShortToast("请选择跟随关系");
                    return;
                }
                SettingInfo.shapeList = list;
                break;

        }
    }
}
