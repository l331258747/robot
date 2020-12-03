package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.play.robot.R;

/**
 * Created by LGQ
 * Time: 2018/7/25
 * Function: 请等待对话框
 */
public class LoadingDialog extends Dialog {

    private Context mContext;
    private LayoutInflater inflater;
    private LayoutParams lp;
    private TextView text;


    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_loading, null);
        text = (TextView) layout.findViewById(R.id.loading_text);
        setContentView(layout);

        //设置window属性
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        //去背景遮盖
        lp.dimAmount = 0;
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }


    public void showDialog(String message){
        text.setVisibility(View.VISIBLE);
        text.setText(message);
        super.show();
    }

    public void showNoText(){
        text.setVisibility(View.GONE);
        super.show();
    }

    @Override
    public void dismiss() {
        if(this!=null && this.isShowing())
            super.dismiss();
    }
}
