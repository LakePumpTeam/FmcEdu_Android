package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015-05-27.
 */
public class TaskListAdapter extends FmcBaseAdapter<TaskEntity> {
    private LoginUserEntity loginUserEntity;

    public TaskListAdapter(Context context, List<TaskEntity> items) {
        super(context, items);
        loginUserEntity = FmcApplication.getLoginUser();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems || 0 == mItems.size()) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_list, null);
        }

        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.item_task_list_img_status);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_task_list_txt_title);
        TextView txtManager = (TextView) convertView.findViewById(R.id.item_task_list_txt_manager);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_task_list_txt_date);

        TaskEntity taskEntity = mItems.get(position);
        if (taskEntity.status == 1) {
            imgStatus.setEnabled(false);
            imgStatus.setImageResource(R.mipmap.ic_finish);
        } else {
            if (loginUserEntity.userRole == UserRoleEnum.Parent) {
                imgStatus.setVisibility(View.GONE);
            } else {
                imgStatus.setImageResource(R.mipmap.ic_un_finish);
                imgStatus.setEnabled(true);
            }
        }

        txtTitle.setText(taskEntity.title);
        txtManager.setText("学生:" + taskEntity.studentName);
        txtDate.setText("完成日期:" + taskEntity.deadline);
        imgStatus.setTag(position);
        imgStatus.setOnClickListener(imgDeleteOnClickListener);
        return convertView;

    }

    private View.OnClickListener imgDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ProgressControl progressControl = new ProgressControl(mContext, v);
            progressControl.showWindow();
            final int position = (int) v.getTag();
            TaskEntity taskEntity = mItems.get(position);
            Map<String, Object> param = new HashMap<>();
            param.put("taskId", taskEntity.taskId);
            param.put("studentId", taskEntity.studentId);
            param.put("userId", FmcApplication.getLoginUser().userId);
            MyIon.httpPost(mContext, "task/deleteTask", param, progressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ToastToolUtils.showLong("删除成功！");
                    removeItem(position);
                }
            });
        }
    };
}
