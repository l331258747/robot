package com.play.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.bean.DeviceBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    Context mContext;
    List<DeviceBean> datas;

    public DeviceAdapter(Context context, List<DeviceBean> datas) {
        mContext = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder == null) return;
        final DeviceBean data = datas.get(position);
        if (data == null) return;

        holder.tv_ip.setText(data.getIp());
        holder.iv_check.setImageResource(R.mipmap.ic_check);
        holder.iv_head.setImageResource(R.mipmap.ic_device);
        holder.tv_status.setText("未连接");
        holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.color_text));
        holder.tv_ip.setTextColor(ContextCompat.getColor(mContext,R.color.color_text));

        if(data.getType() == 1){
            holder.iv_check.setImageResource(R.mipmap.ic_check_in);
            holder.iv_head.setImageResource(R.mipmap.ic_device_in);
            holder.tv_status.setText("已连接");
            holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.color_green));
            holder.tv_ip.setTextColor(ContextCompat.getColor(mContext,R.color.color_green));
        }else if(data.getType() == 2){
            holder.iv_check.setImageResource(R.mipmap.ic_check_un);
            holder.iv_head.setImageResource(R.mipmap.ic_device_un);
            holder.tv_status.setText("未连接");
            holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.color_c3392d));
            holder.tv_ip.setTextColor(ContextCompat.getColor(mContext,R.color.color_c3392d));
        }

        if (mOnItemClickListener != null) {
            holder.iv_check.setOnClickListener(v -> mOnItemClickListener.onClick(position));
            holder.itemView.setOnLongClickListener(v -> {
                mOnItemClickListener.onLongClick(position);
                return false;
            });
        }



    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setData(List<DeviceBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<DeviceBean> getDatas(){
        return this.datas;
    }

    public DeviceBean getData(int pos){
        return this.datas.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_head,iv_check;
        TextView tv_ip,tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_ip = itemView.findViewById(R.id.tv_ip);
            tv_status = itemView.findViewById(R.id.tv_status);
            iv_head = itemView.findViewById(R.id.iv_head);
            iv_check = itemView.findViewById(R.id.iv_check);
        }
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
