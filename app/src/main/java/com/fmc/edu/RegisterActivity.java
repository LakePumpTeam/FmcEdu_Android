package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fmc.edu.common.MyTextWatcher;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.ValidateButtonControl;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends Activity {

    private Button btnNextStep;
    private CheckBox ckReadAgreement;
    private EditText editCellphone;
    private EditText editAuthCode;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private ValidateButtonControl validateBtnGetAuthCode;
    private ProgressControl progressControl;
    private String mHostUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initViewEvents();
        progressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        if (AppConfigUtils.IsDevelopment()) {
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
            doNextStep(v);
        }
    };

    private View.OnClickListener btnGetAuthCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validateBtnGetAuthCode.startCountdown();
            getAuthCode(v);
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

    private void getAuthCode(View view) {
        progressControl.showWindow(view);
        String url = mHostUrl + "profile/requestPhoneIdentify";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", editCellphone.getText().toString());

        MyIon.setUrlAndBodyParams(this, url, params, progressControl)
                .setCallback(new FMCMapFutureCallback() {
                    @Override
                    public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                        progressControl.dismiss();
                        if (!HttpTools.isRequestSuccessfully(e, result)) {
                            ToastToolUtils.showLong(result.get("msg").toString());
                            return;
                        }
                        if (AppConfigUtils.IsDevelopment()) {
                            Map<String, Object> data = (Map<String, Object>) result.get("data");
                            editAuthCode.setText(data.get("identifyCode").toString());
                        }
                    }
                });
    }

    private void doNextStep(View view) {
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            ToastToolUtils.showLong("两次密码输入不一致");
            return;
        }

        progressControl.showWindow(view);
        String url = mHostUrl + "profile/requestRegisterConfirm";
        Map<String, Object> params = getNextStepParams();
        MyIon.setUrlAndBodyParams(this, url, params, progressControl)
                .setCallback(new FMCMapFutureCallback() {
                    @Override
                    public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                        progressControl.dismiss();
                        if (!HttpTools.isRequestSuccessfully(e, result)) {
                            ToastToolUtils.showLong(result.get("msg").toString());
                            return;
                        }
                        afterNextStep();
                    }
                });
    }

    private Map<String, Object> getNextStepParams() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("cellPhone", editCellphone.getText());
        data.put("authCode", editAuthCode.getText());
        String md5Password = StringUtils.MD5(editCellphone.getText().toString(), editPassword.getText().toString());
        String md5ConfirmPassword = StringUtils.MD5(editCellphone.getText().toString(), editConfirmPassword.getText().toString());
        data.put("password", md5Password);
        data.put("confirmPassword", md5ConfirmPassword);
        return data;
    }

    private void afterNextStep() {

        Intent intent = new Intent(RegisterActivity.this, RelatedInfoActivity.class);
        intent.putExtra("cellphone", editCellphone.getText().toString());
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
