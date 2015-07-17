package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.MessageListEntity;

import java.util.List;

/**
 * Created by Candy on 2015/7/17.
 */
public class MessageListAdapter extends FmcBaseAdapter<MessageListEntity> {
    public MessageListAdapter(Context context, List<MessageListEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_time_work, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_message_list_date);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_message_list_content);

        MessageListEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtContent.setText(item.typeName + item.content);
        return convertView;
    }
}