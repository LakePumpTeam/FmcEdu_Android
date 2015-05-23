package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.CommentItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/23.
 */
public class DynamicCommentDetailAdapter extends FmcBaseAdapter<CommentItemEntity> {
    public DynamicCommentDetailAdapter(Context context, List<CommentItemEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_comment_detail, null);
        }
        TextView txtCommentName = (TextView) convertView.findViewById(R.id.dynamic_comment_detail_txt_comment_name);
        TextView txtCommentContent = (TextView) convertView.findViewById(R.id.dynamic_comment_detail_txt_comment_content);
        CommentItemEntity item = mItems.get(position);
        txtCommentName.setText(item.userName);
        txtCommentContent.setText(item.comment);
        return convertView;
    }
}
