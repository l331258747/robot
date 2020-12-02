package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.play.robot.R;

public class TextDialog extends Dialog {

    Context mContext;

    TextView btn_cancel,btn_submit,tv_title,tv_content;


    public TextDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_text, null);
        this.setContentView(layout);

        btn_cancel = layout.findViewById(R.id.btn_cancel);
        btn_submit = layout.findViewById(R.id.btn_submit);
        tv_content = layout.findViewById(R.id.tv_content);
        tv_title = layout.findViewById(R.id.tv_title);



        if(!TextUtils.isEmpty(title))
            tv_title.setText(title);

        if(!TextUtils.isEmpty(content)){
            tv_content.setText(content);
        }


        btn_submit.setOnClickListener(view -> {
            if(submitListener != null){
                submitListener.onClick(view);
            }
            dismiss();
        });


        btn_cancel.setOnClickListener(view -> {
            if(cancelListener != null){
                cancelListener.onClick(view);
            }
            dismiss();
        });
    }

    public String getEditContent(){
        return tv_content.getText().toString();
    }

    String title;
    public TextDialog setTitle(String title){
        this.title = title;
        return this;
    }

    String content;
    public TextDialog setContent(String content){
        this.content = content;
        return this;
    }

    View.OnClickListener cancelListener;
    public TextDialog setCancelListener(View.OnClickListener cancelListener){
        this.cancelListener = cancelListener;
        return this;
    }

    View.OnClickListener submitListener;
    public TextDialog setSubmitListener(View.OnClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }

}