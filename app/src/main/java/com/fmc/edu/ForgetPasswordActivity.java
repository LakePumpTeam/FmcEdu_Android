package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fmc.edu.common.MyTextWatcher;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.ValidateButtonControl;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;


public class ForgetPasswordActivity extends BaseActivity {
    private Button btnReset;
    private EditText editCellphone;
    private EditText editAuthCode;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private ValidateButtonControl validateBtnGetAuthCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_forget_password);
        initViews();
        initViewEvents();
        editCellphone.setText(ConvertUtils.getString(getIntent().getExtras().get("cellPhone"), ""));
        if (AppConfigUtils.isDevelopment()) {
            initTestData();
        }
    }

    private void initViews() {
        btnReset = (Button) findViewById(R.id.forget_password_btn_reset);
        editCellphone = (EditText) findViewById(R.id.forget_password_edit_cellphone);
        editAuthCode = (EditText) findViewById(R.id.forget_password_edit_auth_code);
        editPassword = (EditText) findViewById(R.id.forget_password_edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.forget_password_edit_confirm);
        validateBtnGetAuthCode = (ValidateButtonControl) findViewById(R.id.forget_password_btn_get_auth_code);
    }

    private void initViewEvents() {
        btnReset.setOnClickListener(btnResetOnClickListener);
        validateBtnGetAuthCode.setOnClickListener(btnGetAuthCodeOnClickListener);

        MyTextWatcher validatePhoneTextWatcher = new MyTextWatcher();
        editCellphone.addTextChangedListener(validatePhoneTextWatcher);
        validatePhoneTextWatcher.setOnTextChangedListener(validateCellphoneTextChangeListener);

        MyTextWatcher validateInputTextWatcher = new MyTextWatcher();
        editAuthCode.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

        editPassword.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

    }

    private View.OnClickListener btnGetAuthCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validateBtnGetAuthCode.startCountdown();
            getAuthCode();
        }
    };

    private MyTextWatcher.OnTextChangedListener validateCellphoneTextChangeListener = new MyTextWatcher.OnTextChangedListener() {
        @Override
        public void onTextChanged() {
            String cellphoneNum = editCellphone.getText().toString();
            boolean isEffectPhone = !StringUtils.isEmptyOrNull(cellphoneNum) && ValidationUtils.isMobilePhone(cellphoneNum);
            validateBtnGetAuthCode.setEnabled(isEffectPhone);
            CheckValidateInput();
        }
    };

    private MyTextWatcher.OnTextChangedListener validateInputTextChangeListener = new MyTextWatcher.OnTextChangedListener() {
        @Override
        public void onTextChanged() {
            CheckValidateInput();
        }
    };

    private View.OnClickListener btnResetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();
            if (!password.equals(confirmPassword)) {
                ToastToolUtils.showLong("两次密码输入不一致");
                return;
            }
            getLoginSalt();
        }
    };

    private void getAuthCode() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", editCellphone.getText().toString());
        MyIon.httpPost(this, "profile/requestPhoneIdentify", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                //TODO 短信验证开启后，要关闭此处
                editAuthCode.setText(data.get("identifyCode").toString());
            }
        });
    }

    private void getLoginSalt() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", editCellphone.getText());
        MyIon.httpPost(this, "profile/requestSalt", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                doResetPasswordOnClick(ConvertUtils.getString(data.get("salt"), ""));
            }
        });
    }

    private void doResetPasswordOnClick(String salt) {
        mProgressControl.showWindow();
        Map<String, Object> params = getResetPasswordParams(salt);
        MyIon.httpPost(this, "profile/requestForgetPwd", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterResetPassword();
            }
        });
    }

    private Map<String, Object> getResetPasswordParams(String salt) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("cellPhone", editCellphone.getText());
        data.put("authCode", editAuthCode.getText());
        String md5Password = StringUtils.MD5(salt, editPassword.getText().toString());
        data.put("password", md5Password);
        return data;
    }

    private void afterResetPassword() {
        ServicePreferenceUtils.clearPasswordPreference(this);
        this.finish();
        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void CheckValidateInput() {
        boolean isValidatePhone = !StringUtils.isEmptyOrNull(editCellphone.getText().toString()) && ValidationUtils.isMobilePhone(editCellphone.getText().toString());
        boolean isValidateCode = !StringUtils.isEmptyOrNull(editAuthCode.getText());
        boolean isValidatePassword = editPassword.length() >= 6 && editPassword.length() <= 16;
        if (isValidatePhone && isValidateCode && isValidatePassword) {
            btnReset.setEnabled(true);
            return;
        }
        btnReset.setEnabled(false);
    }

    private void initTestData() {
        editCellphone.setText("13880454117");
        editAuthCode.setText("123456");
        editPassword.setText("123456");
        editConfirmPassword.setText("123456");
    }
}
