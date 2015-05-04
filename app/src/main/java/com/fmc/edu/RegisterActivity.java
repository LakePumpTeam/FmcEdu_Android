package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fmc.edu.enums.WatcherTypeEnum;
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
        MyTextWatcher validatePhoneTextWatcher = new MyTextWatcher(editCellphone, WatcherTypeEnum.ValidateCell);
        editCellphone.addTextChangedListener(validatePhoneTextWatcher);
    }

    private View.OnClickListener btnNextStepOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    class MyTextWatcher implements TextWatcher {

        EditText editText;
        WatcherTypeEnum checkCode;

        public MyTextWatcher(EditText editText, WatcherTypeEnum checkCode) {
            this.editText = editText;
            this.checkCode = checkCode;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isShowNextStep();
        }

        private void isShowNextStep() {
            switch (checkCode) {
                case ValidateCell:
                    SetValidatePhone();
//                case 1:
//                    SetInputInfo();
            }
        }

        private void SetValidatePhone() {
            String text = editText.getText().toString();
            if (editText.getId() == R.id.register_edit_cellphone) {
                boolean isEffectPhone = !StringUtils.isEmptyOrNull(text) && ValidationUtils.isMobilePhone(text);
                validateBtnGetAuthCode.setEnabled(isEffectPhone);
            }

            boolean isValidatePhone = !StringUtils.isEmptyOrNull(editCellphone.getText()) && ValidationUtils.isMobilePhone(editCellphone.getText().toString());
            boolean isValidateCode = !StringUtils.isEmptyOrNull(editAuthCode.getText());
            boolean isValidatePassword = editPassword.length() >= 6 && editPassword.length() <= 16;
            if (isValidatePhone && isValidateCode && ckReadAgreement.isChecked() && isValidatePassword) {
                btnNextStep.setEnabled(true);
                return;
            }
            btnNextStep.setEnabled(false);
        }

//        private void SetInputInfo() {
//            String fullname = et_register_fullname.getText().toString();
//            String drivingSchool = et_register_driving_school.getText().toString();
//            String carNum = et_register_car_num.getText().toString();
//            String cardNum = et_register_card_num.getText().toString();
//            String password = et_register_password.getText().toString();
//
//            if (StringUtils.isNotBlank(fullname) && StringUtils.isNotBlank(drivingSchool)
//                    && StringUtils.isNotBlank(carNum) && StringUtils.isNotBlank(cardNum) && StringUtils.isNotBlank(password)) {
//                btn_to_validate_identity.setEnabled(true);
//                return;
//            }
//            btn_to_validate_identity.setEnabled(false);
//        }
    }

}
