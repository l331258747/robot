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

public class IvShape extends LinearLayout {

    Context context;
    ImageView iv_img;

    public IvShape(Context context) {
        this(context, null);
    }

    public IvShape(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IvShape(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_shape, this, true);

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


    public void setSelect(String imgId) {
        switch (imgId) {
            case Constant.SHAPE_CIRCULAR:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_shape_circular));
                break;
            case Constant.SHAPE_SQUARE:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_shape_square));
                break;
            case Constant.SHAPE_TRIANGLE:
                iv_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_shape_triangle));
                break;
        }
    }
}
