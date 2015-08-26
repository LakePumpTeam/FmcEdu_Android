package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.entity.TimeWorkEntity;
import com.fmc.edu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/7/14.
 */
public class TimeWorkAdapter extends FmcBaseAdapter<TimeWorkEntity> {
    public TimeWorkAdapter(Context context, List<TimeWorkEntity> items) {
        super(context, items);
    }


    public void addAllItems(List<TimeWorkEntity> list, boolean isClear) {
        if (null == mItems) {
            mItems = new ArrayList<>();
        }
        if (isClear) {
            mItems.clear();
        }
        if (!isClear && mItems.size() > 0) {
            if (StringUtils.isEmptyOrNull(mItems.get(0).date)) {
                mItems.clear();
            }
        }
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_time_work, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_time_work_txt_date);
        TextView txtWeek = (TextView) convertView.findViewById(R.id.item_time_work_txt_week);
        TextView txtTime = (TextView) convertView.findViewById(R.id.item_time_work_txt_time);
        TextView txtSign = (TextView) convertView.findViewById(R.id.item_time_work_txt_sign);

        TimeWorkEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtWeek.setText(item.week);
        txtTime.setText(item.time);
        txtSign.setText(item.attendanceName);
        return convertView;
    }
}
