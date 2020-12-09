package com.play.robot.view.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseFragment;
import com.play.robot.bean.SettingInfo;

import androidx.fragment.app.Fragment;

public class MoreFragment extends BaseFragment implements View.OnClickListener {

    EditText et_jd,et_wd,et_bjjl;
    TextView tv_read,tv_send;


    public static Fragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void initView() {
        et_jd = $(R.id.et_jd);
        et_wd = $(R.id.et_wd);
        et_bjjl = $(R.id.et_bjjl);

        tv_read = $(R.id.tv_read);
        tv_send = $(R.id.tv_send);

        et_jd.setText(SettingInfo.moreJd);
        et_wd.setText(SettingInfo.moreWd);
        et_bjjl.setText(SettingInfo.moreJl);

        tv_read.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_read:

                break;
            case R.id.tv_send:
                if(TextUtils.isEmpty(et_jd.getText().toString())){
                    showShortToast("请输入经度");
                    return;
                }
                if(TextUtils.isEmpty(et_wd.getText().toString())){
                    showShortToast("请输入纬度");
                    return;
                }
                if(TextUtils.isEmpty(et_bjjl.getText().toString())){
                    showShortToast("请输入报警距离");
                    return;
                }

                SettingInfo.moreJd = et_jd.getText().toString();
                SettingInfo.moreWd = et_wd.getText().toString();
                SettingInfo.moreJl = et_bjjl.getText().toString();

                break;

        }
    }
}
