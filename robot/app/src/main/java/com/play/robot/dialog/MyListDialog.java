package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.play.robot.R;
import com.play.robot.adapter.MyListAdapter;
import com.play.robot.bean.MyListBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyListDialog extends Dialog {

    Context mContext;

    RecyclerView recyclerView;

    MyListAdapter mAdapter;

    List<MyListBean> list;

    public MyListDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_list, null);
        this.setContentView(layout);

        initRecycler(layout);
    }

    //初始化recyclerview
    private void initRecycler(View layout) {
        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyListAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onClick(position);
                dismiss();
            }

        });

        if(list != null){
            mAdapter.setData(list);
        }
    }

    public MyListDialog setData(List<MyListBean> list){
        this.list = list;
        return this;
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public MyListDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

}
