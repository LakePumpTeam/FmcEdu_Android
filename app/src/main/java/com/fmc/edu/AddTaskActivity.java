package com.fmc.edu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.TopBarControl;

import java.util.Calendar;


public class AddTaskActivity extends Activity {

    private Button btnSubmit;
    private EditText editContent;
    private EditText editSubject;
    private TextView txtManager;
    private TextView txtFinishTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_add_task);
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
//TODO 提交新任务
        }
    };

    private View.OnClickListener txtManagerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//选择责任人
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

}
