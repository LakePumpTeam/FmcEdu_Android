package com.fmc.edu.customcontrol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/6/6.
 */
public class TimePickerControl extends LinearLayout {
    private Context mContext;
    private PickerViewControl pickerViewHour;
    private PickerViewControl pickerViewMin;

    public TimePickerControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViewContent();
    }

    private void initViewContent() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_time_picker, null);
        pickerViewHour = (PickerViewControl) view.findViewById(R.id.control_time_picker_hour);
        pickerViewMin = (PickerViewControl) view.findViewById(R.id.control_time_picker_min);
        pickerViewHour.setData(getHourList());
        pickerViewMin.setData(getMinList());
        this.addView(view);
    }

    public void setTime(int hour, int min) {
        pickerViewHour.setSelected(hour);
        pickerViewMin.setSelected(min);
    }

    private List<String> getHourList() {
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hourList.add("0" + i);
            } else {
                hourList.add(ConvertUtils.getString(i));
            }
        }
        return hourList;
    }

    private List<String> getMinList() {
        List<String> minList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minList.add("0" + i);
            } else {
                minList.add(ConvertUtils.getString(i));
            }
        }
        return minList;
    }

    public String getSelectTime() {
        return pickerViewHour.getSelected() + ":" + pickerViewMin.getSelected();
    }
}
