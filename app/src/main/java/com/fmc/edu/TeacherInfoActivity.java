package com.fmc.edu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TeacherInfoActivity extends Activity {
    private Button btnSubmit;
    private EditText editCourse;
    private EditText editName;
    private EditText editRecord;
    private EditText editCellphone;
    private RadioGroup rgSex;
    private TextView txtBirth;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private boolean mIsModify;
    private String mTeacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initViews();
        initViewEvent();
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        mIsModify = ConvertUtils.getBoolean(getIntent().getExtras().getBoolean("isModify"));
        mTeacherId = ConvertUtils.getString(getIntent().getExtras().get("teacherId"));
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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", mTeacherId);
        MyIon.httpPost(this, mHostUrl + "school/requestTeacherInfo", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                bindViewData(data);
            }
        });
    }

    private void bindViewData(Map<String, Object> data) {
        editCourse.setText(ConvertUtils.getString(data.get("course")));
        editCellphone.setText(ConvertUtils.getString(data.get("cellPhone")));
        editName.setText(ConvertUtils.getString(data.get("teacherName")));
        editRecord.setText(ConvertUtils.getString(data.get("resume")));
        txtBirth.setText(ConvertUtils.getString(data.get("teacherBirth")));
        if (ConvertUtils.getBoolean(data.get("teacherSex"))) {
            ((RadioButton) rgSex.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        }
    }

    private void bindEnable() {
        if (!mIsModify) {
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
            params.put("teacherId", mTeacherId);
            params.put("teacherName", editName.getText());
            params.put("teacherBirth", txtBirth.getText());
            params.put("course", editCourse.getText());
            params.put("cellPhone", editCellphone.getText());
            params.put("resume", editRecord.getText());
            params.put("teacherSex", (findViewById(rgSex.getCheckedRadioButtonId()).getTag()));

            MyIon.httpPost(TeacherInfoActivity.this, mHostUrl + "school/requestModifyTeacherInfo", params, mProgressControl, new MyIon.AfterCallBack() {
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
