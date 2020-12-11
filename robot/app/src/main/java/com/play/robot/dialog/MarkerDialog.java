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

public class MarkerDialog  extends Dialog {

    Context mContext;
    TextView btn_start,btn_road,btn_end,tv_btn,btn_road2;
    EditText et_account,et_password;

    int type = -2;//0起点，-1终点，1途径点

    public MarkerDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_marker, null);
        this.setContentView(layout);

        btn_start = layout.findViewById(R.id.btn_start);
        btn_road = layout.findViewById(R.id.btn_road);
        btn_road2 = layout.findViewById(R.id.btn_road2);
        btn_end = layout.findViewById(R.id.btn_end);
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

                if(type == -2){
                    ToastUtil.showShortToast(mContext, "请选择属性");
                    return;
                }

                submitListener.onClick(Double.parseDouble(et_account.getText().toString()), Double.parseDouble(et_password.getText().toString()), type);
            }
            dismiss();
        });

        btn_start.setOnClickListener(v -> {
            setBtn(0);

        });
        btn_road.setOnClickListener(v -> {
            setBtn(1);

        });
        btn_road2.setOnClickListener(v -> {
            setBtn(2);
        });
        btn_end.setOnClickListener(v -> {
            setBtn(-1);

        });

        if(latitude != 0){
            et_account.setText(latitude + "");
            et_password.setText(longitude + "");
        }


        initBtn(Showtype);
    }

    private void initBtn(int showtype) {
        btn_start.setVisibility(View.GONE);
        btn_road.setVisibility(View.GONE);
        btn_road2.setVisibility(View.GONE);
        btn_end.setVisibility(View.GONE);

        btn_start.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_road.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_road2.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_end.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);

        if(showtype == 0){
            this.type = 0;
            btn_start.setVisibility(View.VISIBLE);
            btn_start.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }else{
            this.type = 1;
            btn_road.setVisibility(View.VISIBLE);
            btn_road2.setVisibility(View.VISIBLE);
            btn_end.setVisibility(View.VISIBLE);
            btn_road.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
            btn_road2.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
            btn_end.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        }
    }

    private void setBtn(int type){
        this.type = type;

        btn_start.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_road.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_road2.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);
        btn_end.setBackgroundResource(R.drawable.btn_0_1e1e1e_r40);

        if(type == 0){
            btn_start.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }else if(type == 1){
            btn_road.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }else if(type == 2){
            btn_road2.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }else if(type == -1){
            btn_end.setBackgroundResource(R.drawable.btn_0_c3392d_r40);
        }
    }

    double latitude;
    double longitude;
    public MarkerDialog setLatLng(double latitude,double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    int Showtype = -1;//0起点，1终点,途径点
    public MarkerDialog setShowType(int Showtype){
        this.Showtype = Showtype;
        return this;
    }


    public interface OnItemClickListener {
        void onClick(double latitude, double longitude, int type);
    }

    OnItemClickListener submitListener;

    public MarkerDialog setSubmitListener(OnItemClickListener submitListener) {
        this.submitListener = submitListener;
        return this;
    }
}
