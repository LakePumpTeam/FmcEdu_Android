package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ValidationUtils;


public class LoginActivity extends Activity {
    private Button btnLogin;
    private EditText editCellphone;
    private EditText editPassword;
    private TextView txtForgetPassword;
    private TextView txtRegister;

    private int REGIEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        bindViewEvents();
    }

    private void initViews() {
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        editCellphone = (EditText) findViewById(R.id.login_edit_cellphone);
        editPassword = (EditText) findViewById(R.id.login_edit_password);
        txtForgetPassword = (TextView) findViewById(R.id.login_txt_forget_password);
        txtRegister = (TextView) findViewById(R.id.login_txt_register);
    }

    private void bindViewEvents() {
        btnLogin.setOnClickListener(btnLoginOnClickListener);
        txtForgetPassword.setOnClickListener(txtForgetPasswordOnClickListener);
        txtRegister.setOnClickListener(txtRegisterOnClickListener);
    }

    private View.OnClickListener btnLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLogin(v);
        }
    };

    private View.OnClickListener txtForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 忘记密码操作
        }
    };

    private View.OnClickListener txtRegisterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REGIEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REGIEST_CODE) {
            this.finish();
        }

    }

    private void doLogin(View view) {
        String cellphone = editCellphone.getText().toString();
        String password = editPassword.getText().toString();
        AlertWindowControl alertWindowControl = new AlertWindowControl(LoginActivity.this);
        if (StringUtils.isEmptyOrNull(cellphone) && ValidationUtils.isMobilePhone(cellphone)) {
            alertWindowControl.showWindow(view, "登录失败", "请输入有效的电话号码");
            return;
        }
        if (StringUtils.isEmptyOrNull(password)) {
            alertWindowControl.showWindow(view, "登录失败", "请输入密码");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            alertWindowControl.showWindow(view, "登录失败", "有效的密码是6-16位的数字或者字符");
            return;
        }
        //TODO 调用登录接口进行登录并进入主页面
    }
}

