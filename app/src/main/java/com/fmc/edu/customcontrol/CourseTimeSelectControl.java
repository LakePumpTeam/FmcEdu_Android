package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fmc.edu.R;
import com.fmc.edu.utils.ToastToolUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Candy on 2015/6/6.
 */
public class CourseTimeSelectControl extends PopupWindow {

    private ImageView imgClose;
    private Button btnOk;
    private View mParentView;
    private TimePickerControl pickerStartTime;
    private TimePickerControl pickerEndTime;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private OnOperateOnClickListener mOnOperateOnClickListener;

    public interface OnOperateOnClickListener {
        void onOperateOnClick(View view, String startTime, String endTime);
    }

    public CourseTimeSelectControl(Context context) {
        super(context, null);
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        this.mContext = context;
        initPopWindow();
        initContentView();
    }

    private void initPopWindow() {
        this.setWidth(mDisplayMetrics.widthPixels);
        this.setHeight(mDisplayMetrics.heightPixels);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(-000000);
        this.setBackgroundDrawable(dw);
    }

    private void initContentView() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        linearLayout.setPadding(20, 30, 20, 30);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_course_time_select, null);
        view.setMinimumWidth(mDisplayMetrics.widthPixels * 9 / 10);

        btnOk = (Button) view.findViewById(R.id.control_edit_window_btn_ok);
        imgClose = (ImageView) view.findViewById(R.id.control_course_time_select_img_close);
        pickerStartTime = (TimePickerControl) view.findViewById(R.id.control_course_time_select_picker_start_time);
        pickerEndTime = (TimePickerControl) view.findViewById(R.id.control_course_time_select_picker_end_time);

        imgClose.setOnClickListener(txtCancelOnClickListener);
        btnOk.setOnClickListener(txtOperateClickListener);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
    }

    private View.OnClickListener txtCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    private View.OnClickListener txtOperateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnOperateOnClickListener) {
                return;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            try {
                Date beginDate = simpleDateFormat.parse(pickerStartTime.getSelectTime());
                Date endDate = simpleDateFormat.parse(pickerEndTime.getSelectTime());
                if (beginDate.getTime() > endDate.getTime()) {
                    ToastToolUtils.showLong("上课时间不能大于下课时间");
                    return;
                }
                mOnOperateOnClickListener.onOperateOnClick(mParentView, pickerStartTime.getSelectTime(), pickerEndTime.getSelectTime());
                dismiss();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    public void setOnOperateOnClickListener(OnOperateOnClickListener mOnOperateOnClickListener) {
        this.mOnOperateOnClickListener = mOnOperateOnClickListener;
    }

    public void showWindow(View parentView, Date startDate, Date endDate) {
        mParentView = parentView;
        pickerStartTime.setTime(startDate.getHours(), startDate.getMinutes());
        pickerEndTime.setTime(endDate.getHours(), endDate.getMinutes());
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
