package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.TaskEntity;

import java.util.List;

/**
 * Created by Candy on 2015-05-27.
 */
public class TaskListAdapter extends FmcBaseAdapter<TaskEntity> {
    public TaskListAdapter(Context context, List<TaskEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_list, null);
        }

        ImageView imgStatus = (ImageView) convertView.findViewById(R.id.item_task_list_img_status);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_task_list_txt_title);
        TextView txtManager = (TextView) convertView.findViewById(R.id.item_task_list_txt_manager);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_task_list_txt_date);

        TaskEntity taskEntity = mItems.get(position);
        imgStatus.setImageResource(taskEntity.status ? R.mipmap.ic_finish : R.mipmap.ic_un_finish);
        txtTitle.setText(taskEntity.subject);
        txtManager.setText("负责人:" + taskEntity.ManagerName);
        txtDate.setText("完成时间：" + taskEntity.date);
        return convertView;

    }
}
