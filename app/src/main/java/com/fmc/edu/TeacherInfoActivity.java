package com.fmc.edu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TeacherInfoActivity extends BaseActivity {
    private Button btnSubmit;
    private EditText editCourse;
    private EditText editName;
    private EditText editRecord;
    private EditText editCellphone;
    private RadioGroup rgSex;
    private TextView txtBirth;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_teacher_info);
        initViews();
        initViewEvent();
        mBundle = getIntent().getExtras();
        bindEnable();
        initPageData();
    }

    private void initViews() {
        btnSubmit = (Button) findViewById(R.id.teacher_info_btn_submit);
        editCourse = (EditText) findViewById(R.id.teacher_info_edit_course);
        editCellphone = (EditText) findViewById(R.id.teacher_info_edit_cellphone);
        editName = (EditText) findViewById(R.id.teacher_info_edit_name);
        editRecord = (EditText) findViewById(R.id.teacher_info_edit_record);
        rgSex = (RadioGroup) findViewById(R.id.teacher_info_rg_sex);
        txtBirth = (TextView) findViewById(R.id.teacher_info_txt_birth);
    }

    private void initViewEvent() {
        btnSubmit.setOnClickListener(btnSubmitListener);
        txtBirth.setOnClickListener(txtBirthListener);
    }

    private void initPageData() {
        editCourse.setText(mBundle.getString("course"));
        editCellphone.setText(mBundle.getString("cellPhone"));
        editName.setText(mBundle.getString("teacherName"));
        editRecord.setText(mBundle.getString("resume"));
        txtBirth.setText(mBundle.getString("teacherBirth"));
        if (mBundle.getBoolean("teacherSex")) {
            ((RadioButton) rgSex.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        }
    }

    private void bindEnable() {
        if (null == mBundle || !mBundle.getBoolean("isModify")) {
            return;
        }
        editCourse.setEnabled(true);
        editCellphone.setEnabled(true);
        editName.setEnabled(true);
        editRecord.setEnabled(true);
        rgSex.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
        txtBirth.setEnabled(true);
        rgSex.getChildAt(0).setEnabled(true);
        rgSex.getChildAt(1).setEnabled(true);
    }


    private View.OnClickListener btnSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("teacherId", mBundle.getInt("teacherId"));
            params.put("teacherName", editName.getText());
            params.put("teacherBirth", txtBirth.getText());
            params.put("course", editCourse.getText());
            params.put("cellPhone", editCellphone.getText());
            params.put("resume", editRecord.getText());
            params.put("teacherSex", (findViewById(rgSex.getCheckedRadioButtonId()).getTag()));

            MyIon.httpPost(TeacherInfoActivity.this, "school/requestModifyTeacherInfo", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ToastToolUtils.showLong("修改成功");
                }
            });
        }
    };

    private View.OnClickListener txtBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherInfoActivity.this, dateDialogDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    };

    private DatePickerDialog.OnDateSetListener dateDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String birthDay = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
            txtBirth.setText(birthDay);
        }
    };
}
