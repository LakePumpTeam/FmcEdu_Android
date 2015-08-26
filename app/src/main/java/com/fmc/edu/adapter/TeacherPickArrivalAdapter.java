package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/7/17.
 */
public class TeacherPickArrivalAdapter extends FmcBaseAdapter<PickUpEntity> {
    public TeacherPickArrivalAdapter(Context context, List<PickUpEntity> items) {
        super(context, items);
    }

    public void addAllItems(List<PickUpEntity> list, boolean isClear) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_pick_arrival, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_teacher_pick_arrival_txt_date);
        TextView txtTime = (TextView) convertView.findViewById(R.id.item_teacher_pick_arrival_txt_time);
        TextView txtStudent = (TextView) convertView.findViewById(R.id.item_teacher_pick_arrival_txt_student);
        TextView txtParent = (TextView) convertView.findViewById(R.id.item_teacher_pick_arrival_txt_parent);

        PickUpEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtParent.setText(item.parentName);
        txtTime.setText(item.time);
        txtStudent.setText(item.studentName);
        return convertView;
    }
}
