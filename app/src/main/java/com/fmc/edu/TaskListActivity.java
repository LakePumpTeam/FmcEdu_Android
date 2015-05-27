package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;

import com.fmc.edu.customcontrol.SlideListView;


public class TaskListActivity extends Activity {
    private Button btnAddTask;
    private RadioGroup rgTab;
    private SlideListView slideTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_task_list);
        initViews();
        initViewsEvent();
    }

    private void initViews() {
        btnAddTask = (Button) findViewById(R.id.task_list_btn_add_task);
        rgTab = (RadioGroup) findViewById(R.id.task_list_rg_tab);
        slideTaskList = (SlideListView) findViewById(R.id.task_list_slide_task_list);
    }

    private void initViewsEvent() {
        btnAddTask.setOnClickListener(addTaskOnClickListener);
        slideTaskList.setOnItemClickListener(slideOnItemClickListener);
    }

    private View.OnClickListener addTaskOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
    };


    private AdapterView.OnItemClickListener slideOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
            startActivity(intent);

        }
    };
}
