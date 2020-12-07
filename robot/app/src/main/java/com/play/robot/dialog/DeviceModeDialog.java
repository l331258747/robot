package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.play.robot.R;

public class DeviceModeDialog extends Dialog implements View.OnClickListener {

    Context mContext;

    LinearLayout ll_control,ll_control_mind,ll_mind,ll_follow_people,ll_follow_car;


    public DeviceModeDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_device_mode, null);
        this.setContentView(layout);

        ll_control = layout.findViewById(R.id.ll_control);
        ll_control_mind = layout.findViewById(R.id.ll_control_mind);
        ll_mind = layout.findViewById(R.id.ll_mind);
        ll_follow_people = layout.findViewById(R.id.ll_follow_people);
        ll_follow_car = layout.findViewById(R.id.ll_follow_car);


        ll_control.setOnClickListener(this);
        ll_control_mind.setOnClickListener(this);
        ll_mind.setOnClickListener(this);
        ll_follow_people.setOnClickListener(this);
        ll_follow_car.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()){
            case R.id.ll_control:
                submitListener.onClick(0);
                break;
            case R.id.ll_control_mind:
                submitListener.onClick(1);
                break;
            case R.id.ll_mind:
                submitListener.onClick(2);
                break;
            case R.id.ll_follow_people:
                submitListener.onClick(3);
                break;
            case R.id.ll_follow_car:
                submitListener.onClick(4);
                break;
        }
    }

    public interface OnItemClickListener {
        void onClick(int mode);
    }

    OnItemClickListener submitListener;

    public DeviceModeDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }
}