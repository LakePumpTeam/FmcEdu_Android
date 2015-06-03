package com.fmc.edu;

import android.app.Activity;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmc.edu.adapter.TaskDetailCommentAdapter;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.Map;


public class TaskDetailActivity extends BaseActivity {
    private Button btnComment;
    private EditText editComment;
    private EditText editContent;
    private ListView listComment;
    private RelativeLayout rlComment;
    private TopBarControl topBar;
    private TextView txtSubject;
    private TextView txtStudent;
    private TextView txtDate;
    private TaskEntity mTaskEntity;
    private TaskDetailCommentAdapter mCommentAdapter;
    private TextView popupDelete;
    private TextView popupCopy;
    private PopupWindow commentPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_task_detail);
        mTaskEntity = (TaskEntity) getIntent().getExtras().getSerializable("taskDetail");
        initViews();
        initViewEvent();
        initPageData();
        initPopupWindow();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.task_detail_top_bar);
        txtSubject = (TextView) findViewById(R.id.task_detail_txt_subject);
        txtStudent = (TextView) findViewById(R.id.task_detail_txt_student);
        txtDate = (TextView) findViewById(R.id.task_detail_txt_date);
        editContent = (EditText) findViewById(R.id.task_detail_edit_content);
        listComment = (ListView) findViewById(R.id.task_detail_list_comment);
        rlComment = (RelativeLayout) findViewById(R.id.task_detail_rl_comment);
        editComment = (EditText) findViewById(R.id.task_detail_edit_comment);
        btnComment = (Button) findViewById(R.id.task_detail_btn_comment);
    }

    private void initViewEvent() {
        topBar.setOnOperateOnClickListener(topBarOnOperateOnClickListener);
        btnComment.setOnClickListener(btnCommentOnClickListener);
        listComment.setOnItemLongClickListener(listCommentOnItemClickListener);
    }

    private void initPageData() {
        if (null == mTaskEntity) {
            ToastToolUtils.showShort("加载失败");
            this.finish();
            return;
        }
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        if (loginUserEntity.userRole == UserRoleEnum.Parent) {
            editContent.setEnabled(false);
            topBar.setTopBarOperateImg(R.mipmap.btn_finish);
            topBar.setOperateEnable(mTaskEntity.status == 0);

        } else if (loginUserEntity.userRole == UserRoleEnum.Teacher) {
            topBar.setTopBarOperateImg(R.mipmap.btn_save);
            topBar.setEnabled(true);
            editContent.setEnabled(true);
        }
        String title = mTaskEntity.title.length() > 6 ? mTaskEntity.title.substring(0, 6) : mTaskEntity.title;
        topBar.setTopBarText(title);
        txtSubject.setText(mTaskEntity.title);
        txtStudent.setText(mTaskEntity.studentName);
        txtDate.setText(mTaskEntity.deadline);
        editContent.setText(mTaskEntity.content);
        bindCommentList();
    }

    private void initPopupWindow() {
        DisplayMetrics mDisplayMetrics;
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        commentPopupWindow = new PopupWindow();
        commentPopupWindow.setWidth(mDisplayMetrics.widthPixels);
        commentPopupWindow.setHeight(mDisplayMetrics.heightPixels);
        commentPopupWindow.setFocusable(true);
        commentPopupWindow.setOutsideTouchable(true);
        commentPopupWindow.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(-000000);
        commentPopupWindow.setBackgroundDrawable(dw);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        linearLayout.setPadding(40, 30, 40, 30);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));

        View view = LayoutInflater.from(this).inflate(R.layout.popup_task_detail, null);
        popupDelete = (TextView) view.findViewById(R.id.popup_task_detail_delete);
        popupCopy = (TextView) view.findViewById(R.id.popup_task_detail_copy);
        popupDelete.setOnClickListener(deleteCommentOnClickListener);
        popupCopy.setOnClickListener(copyCommentOnClickListener);
        linearLayout.addView(view);
        commentPopupWindow.setContentView(linearLayout);
    }

    private void bindCommentList() {
        mCommentAdapter = new TaskDetailCommentAdapter(this, mTaskEntity.commentList);
        listComment.setAdapter(mCommentAdapter);
    }

    private TopBarControl.OnOperateOnClickListener topBarOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
            if (loginUserEntity.userRole == UserRoleEnum.Parent) {
                finishTask();
            } else {
                modifyTaskDetail();
            }
        }
    };

    private AdapterView.OnItemLongClickListener listCommentOnItemClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            CommentItemEntity item = (CommentItemEntity) mCommentAdapter.getItem(position);
            LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
            if (loginUserEntity.userRole == UserRoleEnum.Teacher || item.userId == loginUserEntity.userId) {
                popupDelete.setEnabled(true);
            }
            popupDelete.setTag(item);
            popupCopy.setTag(item.comment);
            commentPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            return false;
        }
    };

    private View.OnClickListener deleteCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CommentItemEntity commentItem = (CommentItemEntity) v.getTag();
            deleteCommentItem(commentItem);
        }
    };


    private View.OnClickListener copyCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentPopupWindow.dismiss();
            Object comment = v.getTag();
            if (StringUtils.isEmptyOrNull(comment)) {
                return;
            }
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setText(ConvertUtils.getString(comment, ""));

        }
    };

    private View.OnClickListener btnCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (StringUtils.isEmptyOrNull(editComment.getText())) {
                ToastToolUtils.showLong("请输入评论内容！");
                return;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("userId", FmcApplication.getLoginUser().userId);
            param.put("taskId", mTaskEntity.taskId);
            param.put("comment", editComment.getText());
            MyIon.httpPost(TaskDetailActivity.this, "task/addComment", param, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    editComment.setText("");
                    CommentItemEntity commentItemEntity = CommentItemEntity.toCommentEntity(data);
                    mCommentAdapter.addItem(0, commentItemEntity);
                }
            });
        }
    };

    private void deleteCommentItem(final CommentItemEntity item) {
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        if (loginUserEntity.userRole == UserRoleEnum.Parent && item.userId != loginUserEntity.userId) {
            ToastToolUtils.showLong("不能删除他人评论");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("commentId", item.commentId);
        param.put("userId", loginUserEntity.userId);
        MyIon.httpPost(TaskDetailActivity.this, "task/deleteComment", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                commentPopupWindow.dismiss();
                mCommentAdapter.removeItem(item);
            }
        });
    }

    private void modifyTaskDetail() {
        if (StringUtils.isEmptyOrNull(editContent.getText())) {
            ToastToolUtils.showLong("请输入描述内容！");
            return;
        }
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginUserEntity.userId);
        param.put("taskId", mTaskEntity.taskId);
        param.put("task", editContent.getText());
        MyIon.httpPost(TaskDetailActivity.this, "task/editTask", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showLong("保存成功！");
            }
        });
    }

    private void finishTask() {
        if (StringUtils.isEmptyOrNull(editContent.getText())) {
            ToastToolUtils.showLong("请输入描述内容！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("studentId", mTaskEntity.studentId);
        param.put("taskId", mTaskEntity.taskId);
        MyIon.httpPost(TaskDetailActivity.this, "task/submitTask", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showLong("已完成！");
            }
        });
    }

}
