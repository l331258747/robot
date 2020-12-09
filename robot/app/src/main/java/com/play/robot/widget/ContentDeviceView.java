package com.play.robot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.play.robot.R;
import com.play.robot.bean.DeviceBean;

import java.util.List;

import androidx.annotation.Nullable;

public class ContentDeviceView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    ImageView iv_status_0, iv_status_1, iv_status_2, iv_status_3, iv_status_4;


    public ContentDeviceView(Context context) {
        this(context, null);
    }

    public ContentDeviceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentDeviceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.view_content_device, this, true);

        iv_status_0 = findViewById(R.id.iv_status_0);
        iv_status_1 = findViewById(R.id.iv_status_1);
        iv_status_2 = findViewById(R.id.iv_status_2);
        iv_status_3 = findViewById(R.id.iv_status_3);
        iv_status_4 = findViewById(R.id.iv_status_4);

        iv_status_0.setVisibility(GONE);
        iv_status_1.setVisibility(GONE);
        iv_status_2.setVisibility(GONE);
        iv_status_3.setVisibility(GONE);
        iv_status_4.setVisibility(GONE);

        iv_status_0.setOnClickListener(this);
        iv_status_1.setOnClickListener(this);
        iv_status_2.setOnClickListener(this);
        iv_status_3.setOnClickListener(this);
        iv_status_4.setOnClickListener(this);

        iv_status_0.setOnLongClickListener(this);
        iv_status_1.setOnLongClickListener(this);
        iv_status_2.setOnLongClickListener(this);
        iv_status_3.setOnLongClickListener(this);
        iv_status_4.setOnLongClickListener(this);
    }

    public void setDevice(List<DeviceBean> list) {
        for (int i = 0; i < list.size(); i++) {
            DeviceBean mDevice = list.get(i);
            if (i == 0) {
                iv_status_0.setVisibility(VISIBLE);
                iv_status_0.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_device_in : mDevice.getType() == 2 ? R.mipmap.ic_device_un : R.mipmap.ic_device);
            }else if(i == 1){
                iv_status_1.setVisibility(VISIBLE);
                iv_status_1.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_device_in : mDevice.getType() == 2 ? R.mipmap.ic_device_un : R.mipmap.ic_device);
            }else if(i == 2){
                iv_status_2.setVisibility(VISIBLE);
                iv_status_2.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_device_in : mDevice.getType() == 2 ? R.mipmap.ic_device_un : R.mipmap.ic_device);
            }else if(i == 3){
                iv_status_3.setVisibility(VISIBLE);
                iv_status_3.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_device_in : mDevice.getType() == 2 ? R.mipmap.ic_device_un : R.mipmap.ic_device);
            }else if(i == 4){
                iv_status_4.setVisibility(VISIBLE);
                iv_status_4.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_device_in : mDevice.getType() == 2 ? R.mipmap.ic_device_un : R.mipmap.ic_device);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_status_0:
                mOnItemClickListener.onClick(0);
                break;
            case R.id.iv_status_1:
                mOnItemClickListener.onClick(1);
                break;
            case R.id.iv_status_2:
                mOnItemClickListener.onClick(2);
                break;
            case R.id.iv_status_3:
                mOnItemClickListener.onClick(3);
                break;
            case R.id.iv_status_4:
                mOnItemClickListener.onClick(4);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.iv_status_0:
                mOnItemClickListener.onLongClick(0);
                break;
            case R.id.iv_status_1:
                mOnItemClickListener.onLongClick(1);
                break;
            case R.id.iv_status_2:
                mOnItemClickListener.onLongClick(2);
                break;
            case R.id.iv_status_3:
                mOnItemClickListener.onLongClick(3);
                break;
            case R.id.iv_status_4:
                mOnItemClickListener.onLongClick(4);
                break;

        }
        return false;
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
