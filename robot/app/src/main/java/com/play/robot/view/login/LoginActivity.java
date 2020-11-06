package com.play.robot.view.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.play.robot.R;
import com.play.robot.base.BaseActivity;
import com.play.robot.bean.LoginBean;
import com.play.robot.bean.MySelfInfo;
import com.play.robot.util.SPUtils;
import com.play.robot.util.ToastUtil;
import com.play.robot.view.home.HomeActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_btn;
    EditText et_account, et_password;

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
        tv_btn.setOnClickListener(this);

    }

    @Override
    public void initData() {
        goHome();
    }

    public void goHome() {
        if (SPUtils.getInstance().getBoolean(SPUtils.IS_LOGIN, false)) {
            startActivity(new Intent(context, HomeActivity.class));
            finish();
        }
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
                    ToastUtil.showShortToast(context, "请输入帐号");
                    return;
                }

                loginSuccess();

                break;
        }
    }

    public void loginSuccess() {
        LoginBean data = new LoginBean();
        data.setDeptId(1);
        data.setLoginName("驾驶员");
        data.setPassword("123456");
        data.setUserId(1);
        data.setUserName("admin");

        MySelfInfo.getInstance().setData(data);
        startActivity(new Intent(context, HomeActivity.class));
        finish();

    }
}
