package com.play.robot.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.play.robot.R;
import com.play.robot.bean.MarkerBean;
import com.play.robot.util.GsonUtil;
import com.play.robot.util.LngLonUtil;
import com.play.robot.util.SPUtils;
import com.play.robot.util.ToastUtil;

import java.util.List;

public class SetMarkerDialog extends Dialog {

    Context mContext;

    TextView tv_btn, tv_cancel, tv_tip;

    EditText et_content;

    public SetMarkerDialog(Context context) {
        super(context, R.style.mdialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_set_marker, null);
        this.setContentView(layout);

        et_content = layout.findViewById(R.id.et_content);
        tv_btn = layout.findViewById(R.id.tv_btn);
        tv_cancel = layout.findViewById(R.id.tv_cancel);
        tv_tip = layout.findViewById(R.id.tv_tip);

        tv_tip.setOnClickListener(view -> {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", mContext.getResources().getText(R.string.set_marker_tip2));
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);

            ToastUtil.showShortToast(mContext, "复制成功");
        });

        tv_cancel.setOnClickListener(View -> {
            dismiss();
        });

        if (!TextUtils.isEmpty(SPUtils.getInstance().getString("markers"))) {
            et_content.setText(SPUtils.getInstance().getString("markers"));
        }

        tv_btn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(et_content.getText().toString())) {
                ToastUtil.showShortToast(mContext, "请输入信息");
                return;
            }

            List<MarkerBean> list = GsonUtil.convertString2Collection(et_content.getText().toString(), new TypeToken<List<MarkerBean>>() {
            });
            if (list == null) {
                ToastUtil.showShortToast(mContext, "json错误");
                return;
            }

            if (list.size() < 2) {
                ToastUtil.showShortToast(mContext, "至少输入两个点");
                return;
            }

            if (list.get(0).getType() != 0) {
                ToastUtil.showShortToast(mContext, "第一个点必须为起点type=0");
                return;
            }
            if (list.get(list.size() - 1).getType() != -1) {
                ToastUtil.showShortToast(mContext, "最后一个点必须为终点type=-1");
                return;
            }

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setNum(i);
            }

            for (int i = 0; i < list.size(); i++) {
                double[] ds = LngLonUtil.gps84_To_bd09(list.get(i).getLatitude(), list.get(i).getLongitude());
                list.get(i).setLongitude(ds[1]);
                list.get(i).setLatitude(ds[0]);
            }

            SPUtils.getInstance().putString("markers", GsonUtil.convertVO2String(list));

            ToastUtil.showShortToast(mContext, "添加成功");

            dismiss();
        });

    }

}
