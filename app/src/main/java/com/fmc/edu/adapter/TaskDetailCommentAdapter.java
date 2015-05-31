package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.CommentItemEntity;

import java.util.List;

/**
 * Created by Candy on 2015/5/31.
 */
public class TaskDetailCommentAdapter extends FmcBaseAdapter<CommentItemEntity> {
    public TaskDetailCommentAdapter(Context context, List<CommentItemEntity> items) {
        super(context, items);
    }

    public void removeItem(CommentItemEntity item) {
        this.mItems.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_detail_comment, null);
        }
        ImageView imgHeadPhoto = (ImageView) convertView.findViewById(R.id.task_detail_comment_img_head_photo);
        TextView txtCommentName = (TextView) convertView.findViewById(R.id.task_detail_comment_txt_name);
        TextView txtDate = (TextView) convertView.findViewById(R.id.task_detail_comment_txt_date);
        TextView txtCommentContent = (TextView) convertView.findViewById(R.id.task_detail_comment_txt_content);
        CommentItemEntity item = mItems.get(position);

        imgHeadPhoto.setImageResource(item.sex ? R.mipmap.head_photo_boy : R.mipmap.head_photo_girl);
        txtCommentName.setText(item.userName + "(" + item.relationShip + ")");
        txtDate.setText(item.date);
        txtCommentContent.setText(item.comment);
        return convertView;
    }
}
