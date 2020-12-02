package com.play.robot.view.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.LoginBean;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.util.ToastUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_btn;
    EditText et_account, et_password;
    ImageView iv_back;

    String username, password;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        tv_btn = findViewById(R.id.tv_btn);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        iv_back = findViewById(R.id.iv_back);

        tv_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn:

                username = et_account.getText().toString();
                password = et_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showShortToast(context, "请输入帐号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showShortToast(context, "请输入密码");
                    return;
                }

                loginSuccess();

                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void loginSuccess() {
        LoginBean data = new LoginBean();
        data.setPassword(password);
        data.setUserId(1);
        data.setUserName(username);

        MySelfInfo.getInstance().setData(data);
        finish();

    }
}
