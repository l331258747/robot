package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.util.ToastUtil;

public class EditDialog extends Dialog {

    Context mContext;

    TextView tv_btn;

    EditText et_account, et_password, et_rtsp, et_password2;

    public EditDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_edit, null);
        this.setContentView(layout);

        tv_btn = layout.findViewById(R.id.tv_btn);
        et_account = layout.findViewById(R.id.et_account);
        et_password = layout.findViewById(R.id.et_password);
        et_rtsp = layout.findViewById(R.id.et_rtsp);
        et_password2 = layout.findViewById(R.id.et_password2);

        tv_btn.setOnClickListener(view -> {
            if (submitListener != null) {
                if (TextUtils.isEmpty(et_account.getText().toString())) {
                    ToastUtil.showShortToast(mContext, et_account.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    ToastUtil.showShortToast(mContext, et_password.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(et_password2.getText().toString())) {
                    ToastUtil.showShortToast(mContext, et_password2.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(et_rtsp.getText().toString())) {
                    ToastUtil.showShortToast(mContext, et_rtsp.getHint().toString());
                    return;
                }

                submitListener.onClick(et_account.getText().toString(), Integer.parseInt(et_password.getText().toString()), et_password2.getText().toString(), et_rtsp.getText().toString());
            }
            dismiss();
        });

        setDebug();
    }

    private void setDebug() {
        et_account.setText("192.168.1.254");
        et_password.setText("8585");
        et_password2.setText("3");
        et_rtsp.setText("rtmp://58.200.131.2:1935/livetv/hunantv");
    }


    public interface OnItemClickListener {
        void onClick(String ip, int port, String number, String rtsp);
    }

    OnItemClickListener submitListener;

    public EditDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }

}
