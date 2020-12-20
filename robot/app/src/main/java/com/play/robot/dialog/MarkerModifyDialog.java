package com.play.robot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.util.ToastUtil;

public class MarkerModifyDialog extends Dialog {

    Context mContext;
    TextView tv_btn,btn_road,btn_road2,tv_del,tv_cancel;
    EditText et_account,et_password;
    LinearLayout view_btn;

    public MarkerModifyDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_marker_modify, null);
        this.setContentView(layout);

        setCancelable(false);

        tv_btn = layout.findViewById(R.id.tv_btn);
        tv_del = layout.findViewById(R.id.tv_del);
        tv_cancel = layout.findViewById(R.id.tv_cancel);
        et_account = layout.findViewById(R.id.et_account);
        et_password = layout.findViewById(R.id.et_password);

        view_btn = layout.findViewById(R.id.view_btn);
        btn_road = layout.findViewById(R.id.btn_road);
        btn_road2 = layout.findViewById(R.id.btn_road2);

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

                submitListener.onClick(Double.parseDouble(et_account.getText().toString()), Double.parseDouble(et_password.getText().toString()),type);
            }
            dismiss();
        });

        tv_cancel.setOnClickListener(view -> {
            if (submitListener != null) {
                submitListener.onCancel();
            }
            dismiss();
        });

        tv_del.setOnClickListener(view -> {
            if (submitListener != null) {
                submitListener.onDel();
            }
            dismiss();
        });

        if(type == 1 || type == 2){
            view_btn.setVisibility(View.VISIBLE);
        }else{
            view_btn.setVisibility(View.GONE);
        }

        setBtn(type);

        btn_road.setOnClickListener(v -> {
            setBtn(1);
        });
        btn_road2.setOnClickListener(v -> {
            setBtn(2);
        });

        if(latitude != 0){
            et_account.setText(latitude + "");
            et_password.setText(longitude + "");
        }
    }

    private void setBtn(int type){
        this.type = type;

        btn_road.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_road2.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);

        if(type == 1){
            btn_road.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }else if(type == 2){
            btn_road2.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }
    }

    double latitude;
    double longitude;
    int type;
    public MarkerModifyDialog setLatLng(double latitude, double longitude, int type){
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        return this;
    }

    public interface OnItemClickListener {
        void onClick(double latitude, double longitude, int type);
        void onDel();
        void onCancel();
    }

    OnItemClickListener submitListener;

    public MarkerModifyDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }
}
