package com.play.robot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.play.robot.R;

import androidx.annotation.Nullable;

/**
 * Created by LGQ
 * Time: 2018/8/15
 * Function:
 */

public class NumberView extends LinearLayout implements View.OnClickListener {

    TextView tv_num;
    TextView tv_minus,tv_plus;
    int num;

    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.view_number, this, true);

        tv_minus = findViewById(R.id.tv_minus);
        tv_num = findViewById(R.id.tv_num);
        tv_plus = findViewById(R.id.tv_plus);

        tv_minus.setOnClickListener(this);
        tv_plus.setOnClickListener(this);

        setNum(0);

    }

    public void setNum(int num) {
        this.num = num;
        tv_num.setText(num + "");
        if(onItemClickListener == null)
            return;
        onItemClickListener.onClick(num);
    }

    public int getNum(){
        return this.num;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_minus:
                if (num <= -3)
                    return;
                num--;
                setNum(num);

                break;
            case R.id.tv_plus:
                if (num >= 3)
                    return;
                num++;
                setNum(num);
                break;
        }
    }

    OnItemClickListener onItemClickListener;

    public void setCallback(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int num);
    }
}
