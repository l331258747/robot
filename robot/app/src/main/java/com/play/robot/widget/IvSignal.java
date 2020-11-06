package com.play.robot.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.play.robot.R;
import com.play.robot.constant.Constant;

import androidx.annotation.Nullable;

/**
 * Created by LGQ
 * Time: 2019/1/17
 * Function:
 */

public class IvSignal extends LinearLayout {

    Context context;
    ImageView iv_img;

    public IvSignal(Context context) {
        this(context, null);
    }

    public IvSignal(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IvSignal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_signal, this, true);

        iv_img = view.findViewById(R.id.iv_img);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.iv_battery);

        if (attributes != null) {
            int imgId = attributes.getResourceId(R.styleable.iv_battery_iv_drawable, -1);
            if (imgId != -1) {
                iv_img.setImageDrawable(context.getResources().getDrawable(imgId));
            }

            attributes.recycle();
        }
    }


    public void setSelect(int imgId) {
        switch (imgId) {
            case Constant.SIGNAL_0:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_signal_0));
                break;
            case Constant.SIGNAL_1:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_signal_1));
                break;
            case Constant.SIGNAL_2:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_signal_2));
                break;
            case Constant.SIGNAL_3:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_signal_3));
                break;
        }
    }
}
