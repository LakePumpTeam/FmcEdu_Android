package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.TaskListAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskListActivity extends Activity {
    private Button btnAddTask;
    private RadioGroup rgTab;
    private SlideListView slideTaskList;
    private Bundle mBundle;
    private TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_task_list);
        mBundle = getIntent().getExtras();
        initViews();
        initViewsEvent();
        initPageData();
        ((RadioButton) rgTab.getChildAt(0)).setChecked(true);
    }

    private void initViews() {
        btnAddTask = (Button) findViewById(R.id.task_list_btn_add_task);
        rgTab = (RadioGroup) findViewById(R.id.task_list_rg_tab);
        slideTaskList = (SlideListView) findViewById(R.id.task_list_slide_task_list);
    }

    private void initViewsEvent() {
        btnAddTask.setOnClickListener(addTaskOnClickListener);
        slideTaskList.setOnItemClickListener(slideOnItemClickListener);
        rgTab.setOnCheckedChangeListener(tabOnCheckedChangeListener);
        slideTaskList.setOnLoadMoreListener(slideOnLoadMoreListener);
    }

    private void initPageData() {
        if (null == mBundle) {
            return;
        }
        List<TaskEntity> list = (List<TaskEntity>) mBundle.getSerializable("list");
        mAdapter = new TaskListAdapter(this, list);
        slideTaskList.setAdapter(mAdapter);
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

    private RadioGroup.OnCheckedChangeListener tabOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            boolean status = ConvertUtils.getBoolean(findViewById(checkedId).getTag(), false);
            getTaskList(status);
        }
    };

    private SlideListView.OnLoadMoreListener slideOnLoadMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            loadMoreData();
        }
    };


    private void getTaskList(boolean status) {
        mAdapter.addAllItems(TaskEntity.toTaskEntityList(getInitData(status)), true);
    }

    private List<Map<String, Object>> getInitData(boolean status) {
        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("taskId", i);
            item.put("subject", "大宝最理念的年龄");
            item.put("managerName", "李四");
            item.put("managerId", i);
            item.put("date", "2015-05-27");
            item.put("status", status);
            list.add(item);
        }
        return list;
    }

    private void loadMoreData() {
        boolean status = ConvertUtils.getBoolean(findViewById(rgTab.getCheckedRadioButtonId()).getTag(), false);
        List<Map<String, Object>> list = getInitData(status);

        mAdapter.addAllItems(TaskEntity.toTaskEntityList(getInitData(status)), false);
    }


}
