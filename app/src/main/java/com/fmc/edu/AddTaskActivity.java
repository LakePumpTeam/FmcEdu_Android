package com.fmc.edu;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.MultiSelectListControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.MultiCommonEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.builder.Builders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;


public class AddTaskActivity extends BaseActivity {

    private Button btnSubmit;
    private EditText editContent;
    private EditText editSubject;
    private TextView txtManager;
    private TextView txtFinishTime;
    private List<Integer> mSelectedStudentIds = new ArrayList<>();
    private List<MultiCommonEntity> mStudentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_add_task);
        initViews();
        initViewEvent();
    }

    private void initViews() {
        btnSubmit = (Button) findViewById(R.id.add_task_btn_submit);
        editContent = (EditText) findViewById(R.id.add_task_edit_content);
        editSubject = (EditText) findViewById(R.id.add_task_edit_subject);
        txtManager = (TextView) findViewById(R.id.add_task_txt_manager);
        txtFinishTime = (TextView) findViewById(R.id.add_task_txt_finish_time);
    }

    private void initViewEvent() {
        btnSubmit.setOnClickListener(btnSubmitOnClickListener);
        txtManager.setOnClickListener(txtManagerOnClickListener);
        txtFinishTime.setOnClickListener(txtFinishTimeOnClickListener);
    }

    private View.OnClickListener btnSubmitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mProgressControl.showWindow();
                Builders.Any.B withB = MyIon.with(AddTaskActivity.this).load(AppConfigUtils.getServiceHost() + "task/publishTask");
                withB.setMultipartParameter("userId", StringUtils.base64Encode(FmcApplication.getLoginUser().userId))
                        .setMultipartParameter("deadline", StringUtils.base64Encode(txtFinishTime.getText()))
                        .setMultipartParameter("title", StringUtils.base64Encode(editSubject.getText()))
                        .setMultipartParameter("task", StringUtils.base64Encode(editContent.getText()));
                for (int key : mSelectedStudentIds) {
                    withB.setMultipartParameter("students", StringUtils.base64Encode(key));
                }
                withB.asString(Charset.forName("utf8"))
                        .setCallback(new FMCMapFutureCallback(mProgressControl) {
                            @Override
                            public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                                mProgressControl.dismiss();
                                ToastToolUtils.showShort("添加成功");
                                setResult(RESULT_OK);
                                AddTaskActivity.this.finish();
                            }
                        });

            } catch (NetworkErrorException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener txtManagerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != mStudentList && mStudentList.size() > 0) {
                showStudentSelectList();
                return;
            }
            getStudentList();
        }
    };

    private View.OnClickListener txtFinishTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, dateDialogDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    };
    private DatePickerDialog.OnDateSetListener dateDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String birthDay = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
            txtFinishTime.setText(birthDay);
        }
    };

    private MultiSelectListControl.OnAfterSelectedListener selectStudentListener = new MultiSelectListControl.OnAfterSelectedListener() {
        @Override
        public void onAfterSelected(List<MultiCommonEntity> list) {
            txtManager.setText("");
            mSelectedStudentIds.clear();
            if (null == list || list.size() == 0) {
                return;
            }
            String studentNames = "";

            for (int i = 0; i < list.size(); i++) {
                MultiCommonEntity item = list.get(i);
                if (i <= 2 && !StringUtils.isEmptyOrNull(item.name)) {
                    studentNames += item.name + ",";
                }
                mSelectedStudentIds.add(item.id);
            }
            if (StringUtils.isEmptyOrNull(studentNames)) {
                return;
            }
            studentNames = studentNames.substring(0, studentNames.length() - 1);
            if (list.size() > 3) {
                studentNames += "...";
            }
            txtManager.setText(studentNames);
        }
    };

    private void getStudentList() {
        Map<String, Object> param = new HashMap<>();
        param.put("teacherId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(AddTaskActivity.this, "school/requestStudentList", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("studentList");
                if (null == list || list.size() == 0) {
                    return;
                }
                mStudentList = MultiCommonEntity.toMultiCommonList(list);
                showStudentSelectList();
            }
        });
    }

    private void showStudentSelectList() {
        handleSelectedStudent();
        MultiSelectListControl multiSelectListControl = new MultiSelectListControl(AddTaskActivity.this, mStudentList, "学生列表");
        multiSelectListControl.setOnAfterSelectedListener(selectStudentListener);
        multiSelectListControl.showAtLocation(txtManager, Gravity.CENTER, 0, 0);
    }

    private void handleSelectedStudent() {
        for (MultiCommonEntity multiCommonEntity : mStudentList) {
            multiCommonEntity.isCheck = mSelectedStudentIds.contains(multiCommonEntity.id);
        }
    }
}
