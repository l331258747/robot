package com.play.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.bean.MyListBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    Context mContext;
    List<MyListBean> datas;

    public MyListAdapter(Context context, List<MyListBean> datas) {
        mContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder == null) return;
        final MyListBean data = datas.get(position);
        if (data == null) return;

        holder.tv_content.setText(data.getContent());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(position));
        }



    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setData(List<MyListBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<MyListBean> getDatas(){
        return this.datas;
    }

    public MyListBean getData(int pos){
        return this.datas.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
