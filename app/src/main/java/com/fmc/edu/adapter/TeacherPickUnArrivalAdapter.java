package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.BaseActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.TeacherInfoActivity;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.RequestCodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/17.
 */
public class TeacherPickUnArrivalAdapter extends FmcBaseAdapter<PickUpEntity> {
    public TeacherPickUnArrivalAdapter(Context context, List<PickUpEntity> items) {
        super(context, items);
    }

    public void addAllItems(List<PickUpEntity> list) {
        if (null != mItems) {
            mItems.clear();
        }
        mItems = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_teacher_pick_un_arrival, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_teacher_pick_un_arrival_txt_date);
        TextView txtStudent = (TextView) convertView.findViewById(R.id.item_teacher_pick_un_arrival_txt_student);
        TextView txtRemind = (TextView) convertView.findViewById(R.id.item_teacher_pick_un_arrival_txt_remind);

        PickUpEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtStudent.setText(item.studentName);
        txtRemind.setOnClickListener(txtRemindOnClickListener);
        return convertView;
    }

    private View.OnClickListener txtRemindOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final TextView txtView = (TextView) v;
            final BaseActivity baseActivity = (BaseActivity) mContext;
            baseActivity.mProgressControl.showWindow();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("studentId", FmcApplication.getLoginUser().studentId);
            MyIon.httpPost(mContext, "clock/in/notifyParentNorthDelta", params, baseActivity.mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    txtView.setText("已提醒");
                    txtView.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            txtView.setText("点击提醒");
                            txtView.setEnabled(true);
                        }
                    }, 180000);
                }
            });
        }
    };
}
