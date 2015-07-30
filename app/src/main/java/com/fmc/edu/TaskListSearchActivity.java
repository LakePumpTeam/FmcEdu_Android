package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.adapter.TaskListAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskListSearchActivity extends BaseActivity {

    private EditText editKey;
    private LinearLayout llCancel;
    private SlideListView slideTaskList;
    private TaskListAdapter mAdapter;
    private boolean mIsLastPage;
    private int mPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_task_list_search);
        initViews();
        initViewEvent();
    }

    private void initViews() {
        editKey = (EditText) findViewById(R.id.task_list_search_edit_key);
        llCancel = (LinearLayout) findViewById(R.id.task_list_search_ll_cancel);
        slideTaskList = (SlideListView) findViewById(R.id.task_list_search_slide_task_list);
    }

    private void initViewEvent() {
        llCancel.setOnClickListener(llCancelOnClickListener);
        editKey.setOnEditorActionListener(editKeyOnEditorActionListener);
        slideTaskList.setOnItemClickListener(slideOnItemClickListener);
        slideTaskList.setOnLoadMoreListener(slideOnLoadMoreListener);
    }

    private View.OnClickListener llCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TaskListSearchActivity.this.finish();
        }
    };

    private TextView.OnEditorActionListener editKeyOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                mPageIndex = 1;
                loadTaskData();
                return true;
            }
            return false;
        }
    };

    private AdapterView.OnItemClickListener slideOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            gotoTaskDetail((TaskEntity) mAdapter.getItem(position));
        }
    };

    private SlideListView.OnLoadMoreListener slideOnLoadMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            slideTaskList.setFooterViewVisible(true);
            mPageIndex++;
            loadMoreData();
        }
    };


    private void loadTaskData() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", FmcApplication.getLoginUser().userId);
        param.put("pageIndex", mPageIndex);
        param.put("pageSize", Constant.PAGE_SIZE);
        param.put("filter", editKey.getText());
        MyIon.httpPost(this, "task/requestTaskList", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                slideTaskList.setFooterViewVisible(false);
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("taskList");
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                List<TaskEntity> taskList = TaskEntity.toTaskEntityList(list);
                if (mPageIndex == 1) {
                    mAdapter = new TaskListAdapter(TaskListSearchActivity.this, taskList);
                    slideTaskList.setAdapter(mAdapter);
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
                Intent intent = new Intent(TaskListSearchActivity.this, TaskDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                TaskListSearchActivity.this.finish();
            }
        });

    }


}
