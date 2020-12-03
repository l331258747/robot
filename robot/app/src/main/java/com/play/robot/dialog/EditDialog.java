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

    EditText et_account, et_password;

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

                submitListener.onClick(et_account.getText().toString(), Integer.parseInt(et_password.getText().toString()));
            }
            dismiss();
        });

    }

    public interface OnItemClickListener {
        void onClick(String ip, int port);
    }

    OnItemClickListener submitListener;

    public EditDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }

}
