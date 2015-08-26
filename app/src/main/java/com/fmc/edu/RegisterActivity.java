package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.common.MyTextWatcher;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.ValidateButtonControl;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity {
    private Button btnNextStep;
    private CheckBox ckReadAgreement;
    private EditText editCellphone;
    private EditText editAuthCode;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private TextView txtService;
    private ValidateButtonControl validateBtnGetAuthCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_register);
        initViews();
        initViewEvents();
        if (AppConfigUtils.isDevelopment()) {
            initTestData();
        }
    }

    private void initViews() {
        btnNextStep = (Button) findViewById(R.id.register_btn_next_step);
        ckReadAgreement = (CheckBox) findViewById(R.id.register_ck_read_agreement);
        editCellphone = (EditText) findViewById(R.id.register_edit_cellphone);
        editAuthCode = (EditText) findViewById(R.id.register_edit_auth_code);
        editPassword = (EditText) findViewById(R.id.register_edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.register_edit_confirm_password);
        txtService = (TextView) findViewById(R.id.register_txt_service);
        validateBtnGetAuthCode = (ValidateButtonControl) findViewById(R.id.register_validate_btn_get_auth_code);
    }

    private void initViewEvents() {
        btnNextStep.setOnClickListener(btnNextStepOnClickListener);
        validateBtnGetAuthCode.setOnClickListener(btnGetAuthCodeOnClickListener);
        txtService.setOnClickListener(txtServiceOnClickListener);

        MyTextWatcher validatePhoneTextWatcher = new MyTextWatcher();
        editCellphone.addTextChangedListener(validatePhoneTextWatcher);
        validatePhoneTextWatcher.setOnTextChangedListener(validateCellphoneTextChangeListener);

        MyTextWatcher validateInputTextWatcher = new MyTextWatcher();
        editAuthCode.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

        editPassword.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

        ckReadAgreement.setOnClickListener(ckReadAgreementOnClickListener);
    }

    private View.OnClickListener btnGetAuthCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAuthCode(v);
        }
    };

    private View.OnClickListener ckReadAgreementOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckValidateInput();
        }
    };

    private View.OnClickListener txtServiceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RegisterActivity.this, ServiceStatementActivity.class);
            startActivity(intent);
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

    private View.OnClickListener btnNextStepOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();
            if (!password.equals(confirmPassword)) {
                ToastToolUtils.showLong("两次密码输入不一致");
                return;
            }
            doNextStepOnClick(v);
        }
    };

    private void getAuthCode(View view) {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", editCellphone.getText().toString());
        MyIon.httpPost(this, "profile/requestPhoneIdentify", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
//                if (AppConfigUtils.isDevelopment()) {
//                    editAuthCode.setText(data.get("identifyCode").toString());
//                }
                //TODO 短信验证开启后，要关闭此处
                // editAuthCode.setText(data.get("identifyCode").toString());
                validateBtnGetAuthCode.startCountdown();
            }
        });
    }

    private void doNextStepOnClick(View view) {
        mProgressControl.showWindow();
        Map<String, Object> params = getNextStepParams();

        MyIon.httpPost(this, "profile/requestRegisterConfirm", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterNextStep();
            }
        });
    }

    private Map<String, Object> getNextStepParams() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("cellPhone", editCellphone.getText());
        data.put("authCode", editAuthCode.getText());

        long salt = System.currentTimeMillis();
        String md5Password = StringUtils.MD5(String.valueOf(salt), editPassword.getText().toString());
        data.put("password", md5Password);
        data.put("salt", salt);
        return data;
    }

    private void afterNextStep() {
        this.finish();
        Intent intent = new Intent(RegisterActivity.this, RelatedInfoActivity.class);
        intent.putExtra("cellPhone", editCellphone.getText().toString());
        intent.putExtra("isRegister", true);
        startActivity(intent);
    }

    private void CheckValidateInput() {
        boolean isValidatePhone = !StringUtils.isEmptyOrNull(editCellphone.getText().toString()) && ValidationUtils.isMobilePhone(editCellphone.getText().toString());
        boolean isValidateCode = !StringUtils.isEmptyOrNull(editAuthCode.getText());
        boolean isValidatePassword = editPassword.length() >= 6 && editPassword.length() <= 16;
        if (isValidatePhone && isValidateCode && ckReadAgreement.isChecked() && isValidatePassword) {
            btnNextStep.setEnabled(true);
            return;
        }
        btnNextStep.setEnabled(false);
    }

    private void initTestData() {
        ckReadAgreement.setChecked(true);
        editCellphone.setText("13880454117");
        editAuthCode.setText("123456");
        editPassword.setText("123456");
        editConfirmPassword.setText("123456");
    }
}
