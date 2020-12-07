package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.util.AppUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DeviceInfoDialog extends Dialog {

    Context mContext;

    TextView tv_title,tv_content;


    public DeviceInfoDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_device_info, null);
        this.setContentView(layout);

        tv_content = layout.findViewById(R.id.tv_content);
        tv_title = layout.findViewById(R.id.tv_title);



        if(!TextUtils.isEmpty(title))
            tv_title.setText(title);

        if(!TextUtils.isEmpty(content)){
            tv_content.setText(content);
        }

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = AppUtils.dip2px(180);//宽高可设置具体大小
        lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    String title;
    public DeviceInfoDialog setTitle(String title){
        this.title = title;
        return this;
    }

    String content;
    public DeviceInfoDialog setContent(String content){
        this.content = content;
        return this;
    }

}