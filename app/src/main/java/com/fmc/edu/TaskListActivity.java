package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.TaskListAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.RequestCodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskListActivity extends BaseActivity {
    private Button btnAddTask;
    private RadioGroup rgTab;
    private RadioButton rbUnFinish;
    private RadioButton rbFinish;
    private SlideListView slideTaskList;
    private TopBarControl topBar;
    private Bundle mBundle;
    private TaskListAdapter mAdapter;
    private boolean mIsLastPage;
    private int mPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_task_list);
        mBundle = getIntent().getExtras();
        mIsLastPage = mBundle.getBoolean("isLastPage", false);
        initViews();
        rbUnFinish.setChecked(true);
        initViewsEvent();
        initPageData();
    }

    private void initViews() {
        btnAddTask = (Button) findViewById(R.id.task_list_btn_add_task);
        rgTab = (RadioGroup) findViewById(R.id.task_list_rg_tab);
        rbUnFinish = (RadioButton) findViewById(R.id.task_list_un_finish);
        rbFinish = (RadioButton) findViewById(R.id.task_list_finish);
        slideTaskList = (SlideListView) findViewById(R.id.task_list_slide_task_list);
        topBar = (TopBarControl) findViewById(R.id.task_list_top_bar);
    }

    private void initViewsEvent() {
        btnAddTask.setOnClickListener(addTaskOnClickListener);
        rgTab.setOnCheckedChangeListener(tabOnCheckedChangeListener);
        slideTaskList.setOnItemClickListener(slideOnItemClickListener);
        slideTaskList.setOnLoadMoreListener(slideOnLoadMoreListener);
        topBar.setOnOperateOnClickListener(searchOperateOnClickListener);
    }

    private void initPageData() {
        mPageIndex = 1;
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        if (loginUserEntity.userRole == UserRoleEnum.Teacher) {
            btnAddTask.setVisibility(View.VISIBLE);
        }

        if (null == mBundle) {
            return;
        }
        List<TaskEntity> list = (List<TaskEntity>) mBundle.getSerializable("list");
        mAdapter = new TaskListAdapter(this, list);
        slideTaskList.setAdapter(mAdapter);
        if (null == list || 0 == list.size()) {
            rbFinish.setChecked(true);
        }
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
            gotoTaskDetail((TaskEntity) mAdapter.getItem(position));
        }
    };

    private RadioGroup.OnCheckedChangeListener tabOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mPageIndex = 1;
            loadTaskData();
        }
    };

    private SlideListView.OnLoadMoreListener slideOnLoadMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            loadMoreData();
        }
    };

    private TopBarControl.OnOperateOnClickListener searchOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            Intent intent = new Intent(TaskListActivity.this, TaskListSearchActivity.class);
            startActivity(intent);
        }
    };

    private void loadTaskData() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", FmcApplication.getLoginUser().userId);
        param.put("pageIndex", mPageIndex);
        param.put("pageSize", Constant.PAGE_SIZE);
        param.put("filter", "");
        param.put("status", ConvertUtils.getInteger(findViewById(rgTab.getCheckedRadioButtonId()).getTag(), 0));
        MyIon.httpPost(this, "task/requestTaskList", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("taskList");
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                List<TaskEntity> taskList = TaskEntity.toTaskEntityList(list);
                if (mPageIndex == 1) {
                    mAdapter.addAllItems(taskList, true);
                } else {
                    mAdapter.addAllItems(taskList, false);
                    slideTaskList.setFooterViewVisible(false);
                }
            }

        });
    }

    private void loadMoreData() {
        mPageIndex++;
        slideTaskList.setFooterViewVisible(true);
        loadTaskData();
    }

    private void gotoTaskDetail(TaskEntity taskEntity) {
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", taskEntity.studentId);
        param.put("taskId", taskEntity.taskId);
        MyIon.httpPost(this, "task/requestTaskDetail", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskDetail", TaskEntity.toTaskEntity(data));
                Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCodeUtils.TASK_DETAIL);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RequestCodeUtils.ADD_TASK) {
            rbUnFinish.setChecked(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
