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

public class InstructDialog extends Dialog {

    Context mContext;

    TextView tv_title,btn_submit;
    EditText et_content;



    public InstructDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_instruct, null);
        this.setContentView(layout);

        tv_title = layout.findViewById(R.id.tv_title);
        btn_submit = layout.findViewById(R.id.btn_submit);
        et_content = layout.findViewById(R.id.et_content);

        if(!TextUtils.isEmpty(title))
            tv_title.setText(title);

        btn_submit.setOnClickListener(view -> {
            if (submitListener != null) {
                if (TextUtils.isEmpty(et_content.getText().toString())) {
                    ToastUtil.showShortToast(mContext, et_content.getHint().toString());
                    return;
                }

                submitListener.onClick(et_content.getText().toString());
            }
            dismiss();
        });
    }

    String title;
    public InstructDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public interface OnItemClickListener {
        void onClick(String content);
    }

    OnItemClickListener submitListener;

    public InstructDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }
}