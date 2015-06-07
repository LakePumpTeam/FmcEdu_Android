package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.CourseTimeSelectControl;
import com.fmc.edu.customcontrol.EditWindowControl;
import com.fmc.edu.entity.SyllabusEntity;
import com.fmc.edu.utils.ConvertUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Candy on 2015/6/6.
 */
public class SyllabusAdapter extends FmcBaseAdapter<SyllabusEntity> {


    public SyllabusAdapter(Context context, List<SyllabusEntity> items) {
        super(context, items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_syllabus, null);
        }
        SyllabusEntity syllabusEntity = mItems.get(position);
        TextView txtCourseNum = (TextView) convertView.findViewById(R.id.item_syllabus_txt_course_num);
        TextView txtCourseName = (TextView) convertView.findViewById(R.id.item_syllabus_txt_course_name);
        TextView txtCourseTime = (TextView) convertView.findViewById(R.id.item_syllabus_txt_course_time);

        txtCourseNum.setText(syllabusEntity.orderName);
        txtCourseName.setText(syllabusEntity.courseName);
        txtCourseName.setTag(position);
        txtCourseName.setOnClickListener(txtCourseNameOnClickListener);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");


        String time = simpleDateFormat.format(syllabusEntity.startTime) + "-" + simpleDateFormat.format(syllabusEntity.endTime);
        txtCourseTime.setText(time);
        txtCourseTime.setTag(position);
        txtCourseTime.setOnClickListener(txtCourseTimeOnClickListener);
        return convertView;
    }

    private View.OnClickListener txtCourseNameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ConvertUtils.getInteger(v.getTag(), 0);
            SyllabusEntity syllabusEntity = mItems.get(position);
            EditWindowControl editWindowControl = new EditWindowControl(mContext);
            editWindowControl.showWindow(v, "课程名称", syllabusEntity.courseName);
            editWindowControl.setOnOperateOnClickListener(editWindowOperateOnClickListener);
        }
    };

    private EditWindowControl.OnOperateOnClickListener editWindowOperateOnClickListener = new EditWindowControl.OnOperateOnClickListener() {
        @Override
        public void onOperateOnClick(View view, String content) {
            int position = ConvertUtils.getInteger(view.getTag(), 0);
            ((TextView) view).setText(content);
            SyllabusEntity syllabusEntity = mItems.get(position);
            syllabusEntity.courseName = content;
            mItems.set(position, syllabusEntity);
        }
    };

    private View.OnClickListener txtCourseTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ConvertUtils.getInteger(v.getTag(), 0);
            SyllabusEntity syllabusEntity = mItems.get(position);
            CourseTimeSelectControl courseTimeSelectControl = new CourseTimeSelectControl(mContext);
            courseTimeSelectControl.showWindow(v, syllabusEntity.startTime, syllabusEntity.endTime);
            courseTimeSelectControl.setOnOperateOnClickListener(editTimeOperateOnClickListener);
        }
    };


    private CourseTimeSelectControl.OnOperateOnClickListener editTimeOperateOnClickListener = new CourseTimeSelectControl.OnOperateOnClickListener() {
        @Override
        public void onOperateOnClick(View view, String startTime, String endTime) {
            int position = ConvertUtils.getInteger(view.getTag(), 0);
            String timeStr = startTime + "-" + endTime;
            ((TextView) view).setText(timeStr);
            SyllabusEntity syllabusEntity = mItems.get(position);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            try {
                syllabusEntity.startTime = simpleDateFormat.parse(startTime);
                syllabusEntity.endTime = simpleDateFormat.parse(endTime);
            } catch (ParseException e) {
                syllabusEntity.startTime = calendar.getTime();
                syllabusEntity.endTime = calendar.getTime();
                e.printStackTrace();
            }
            mItems.set(position, syllabusEntity);
        }
    };
}
