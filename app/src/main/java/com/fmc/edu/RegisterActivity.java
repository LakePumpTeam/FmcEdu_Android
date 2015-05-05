package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fmc.edu.common.MyTextWatcher;
import com.fmc.edu.customcontrol.ValidateButtonControl;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ValidationUtils;


public class RegisterActivity extends Activity {

    private Button btnNextStep;
    private CheckBox ckReadAgreement;
    private EditText editCellphone;
    private EditText editAuthCode;
    private EditText editPassword;
    private ValidateButtonControl validateBtnGetAuthCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initViewEvents();
    }

    private void initViews() {
        btnNextStep = (Button) findViewById(R.id.register_btn_next_step);
        ckReadAgreement = (CheckBox) findViewById(R.id.register_ck_read_agreement);
        editCellphone = (EditText) findViewById(R.id.register_edit_cellphone);
        editAuthCode = (EditText) findViewById(R.id.register_edit_auth_code);
        editPassword = (EditText) findViewById(R.id.register_edit_password);
        validateBtnGetAuthCode = (ValidateButtonControl) findViewById(R.id.register_validate_btn_get_auth_code);
    }

    private void initViewEvents() {
        btnNextStep.setOnClickListener(btnNextStepOnClickListener);
        validateBtnGetAuthCode.setOnClickListener(btnGetAuthCodeOnClickListener);

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

    private View.OnClickListener btnNextStepOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RegisterActivity.this, RelatedInfoActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnGetAuthCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 获取验证码
        }
    };

    private View.OnClickListener ckReadAgreementOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckValidateInput();
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
}
