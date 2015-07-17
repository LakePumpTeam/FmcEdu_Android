package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.PickUpEntity;

import java.util.List;

/**
 * Created by Candy on 2015/7/17.
 */
public class TeacherPickUnArrivalAdapter extends FmcBaseAdapter<PickUpEntity> {
    public TeacherPickUnArrivalAdapter(Context context, List<PickUpEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_pick_un_arrival, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_teacher_pick_un_arrival_txt_date);
        TextView txtStudent = (TextView) convertView.findViewById(R.id.item_teacher_pick_un_arrival_txt_student);
        Button btnParent = (Button) convertView.findViewById(R.id.item_teacher_pick_un_arrival_btn_remind);

        PickUpEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtStudent.setText(item.studentName);
        return convertView;
    }
}
