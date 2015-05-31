package com.fmc.edu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.MultiSelectListControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.MultiCommonEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddTaskActivity extends Activity {

    private Button btnSubmit;
    private EditText editContent;
    private EditText editSubject;
    private TextView txtManager;
    private TextView txtFinishTime;
    private List<Integer> mSelectedStudentIds = new ArrayList<>();
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private List<MultiCommonEntity> mStudentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_add_task);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
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
            Map<String, Object> param = new HashMap<>();
            param.put("userId", FmcApplication.getLoginUser().userId);
            param.put("students", mSelectedStudentIds);
            param.put("deadline", txtFinishTime.getText());
            param.put("title", editSubject.getText());
            param.put("task", editContent.getText());
            MyIon.httpPost(AddTaskActivity.this, mHostUrl + "task/publishTask", param, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ToastToolUtils.showShort("添加成功");
                    setResult(RESULT_OK);
                    AddTaskActivity.this.finish();
                }
            });
        }
    };

    private View.OnClickListener txtManagerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != mStudentList || mStudentList.size() > 0) {
                showStudentSelectList();
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
            String birthDay = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
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
        MyIon.httpPost(AddTaskActivity.this, mHostUrl + "school/requestStudentList", param, mProgressControl, new MyIon.AfterCallBack() {
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
