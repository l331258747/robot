package com.play.robot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.bean.DeviceBean;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TitleDeviceView extends ConstraintLayout {

    ImageView iv_status_0, iv_status_1, iv_status_2, iv_status_3, iv_status_4;
    TextView tv_status_car_0,tv_status_car_1,tv_status_car_2,tv_status_car_3,tv_status_car_4;


    public TitleDeviceView(Context context) {
        this(context, null);
    }

    public TitleDeviceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleDeviceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.view_title_device, this, true);

        iv_status_0 = findViewById(R.id.iv_status_0);
        iv_status_1 = findViewById(R.id.iv_status_1);
        iv_status_2 = findViewById(R.id.iv_status_2);
        iv_status_3 = findViewById(R.id.iv_status_3);
        iv_status_4 = findViewById(R.id.iv_status_4);

        tv_status_car_0 = findViewById(R.id.tv_status_car_0);
        tv_status_car_1 = findViewById(R.id.tv_status_car_1);
        tv_status_car_2 = findViewById(R.id.tv_status_car_2);
        tv_status_car_3 = findViewById(R.id.tv_status_car_3);
        tv_status_car_4 = findViewById(R.id.tv_status_car_4);

        iv_status_0.setVisibility(GONE);
        iv_status_1.setVisibility(GONE);
        iv_status_2.setVisibility(GONE);
        iv_status_3.setVisibility(GONE);
        iv_status_4.setVisibility(GONE);
        tv_status_car_0.setVisibility(GONE);
        tv_status_car_1.setVisibility(GONE);
        tv_status_car_2.setVisibility(GONE);
        tv_status_car_3.setVisibility(GONE);
        tv_status_car_4.setVisibility(GONE);
    }

    public void setDevice(List<DeviceBean> list) {
        for (int i = 0; i < list.size(); i++) {
            DeviceBean mDevice = list.get(i);
            if (i == 0) {
                iv_status_0.setVisibility(VISIBLE);
                tv_status_car_0.setVisibility(VISIBLE);
                iv_status_0.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : mDevice.getType() == 2 ? R.mipmap.ic_ugv_un : R.mipmap.ic_ugv);
                tv_status_car_0.setText(mDevice.getNumber());
            }else if(i == 1){
                iv_status_1.setVisibility(VISIBLE);
                tv_status_car_1.setVisibility(VISIBLE);
                iv_status_1.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : mDevice.getType() == 2 ? R.mipmap.ic_ugv_un : R.mipmap.ic_ugv);
                tv_status_car_1.setText(mDevice.getNumber());
            }else if(i == 2){
                iv_status_2.setVisibility(VISIBLE);
                tv_status_car_2.setVisibility(VISIBLE);
                iv_status_2.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : mDevice.getType() == 2 ? R.mipmap.ic_ugv_un : R.mipmap.ic_ugv);
                tv_status_car_2.setText(mDevice.getNumber());
            }else if(i == 3){
                iv_status_3.setVisibility(VISIBLE);
                tv_status_car_3.setVisibility(VISIBLE);
                iv_status_3.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : mDevice.getType() == 2 ? R.mipmap.ic_ugv_un : R.mipmap.ic_ugv);
                tv_status_car_3.setText(mDevice.getNumber());
            }else if(i == 4){
                iv_status_4.setVisibility(VISIBLE);
                tv_status_car_4.setVisibility(VISIBLE);
                iv_status_4.setImageResource(mDevice.getType() == 1 ? R.mipmap.ic_ugv_in : mDevice.getType() == 2 ? R.mipmap.ic_ugv_un : R.mipmap.ic_ugv);
                tv_status_car_4.setText(mDevice.getNumber());
            }
        }

    }
}
