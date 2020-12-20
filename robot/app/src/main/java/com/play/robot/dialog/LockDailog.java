package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.util.ToastUtil;

public class LockDailog extends Dialog {
    Context mContext;

    TextView tv_title,btn_submit,tv_content;

    public LockDailog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_lock, null);
        this.setContentView(layout);

        setCancelable(false);

        btn_submit = layout.findViewById(R.id.btn_submit);
        tv_content = layout.findViewById(R.id.tv_content);
        tv_title = layout.findViewById(R.id.tv_title);

        btn_submit.setOnClickListener(view -> {
            ToastUtil.showShortToast(mContext,"请联系管理员");
        });
    }


}