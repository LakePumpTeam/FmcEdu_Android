package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/16.
 */
public class CalendarControl extends PopupWindow {
    private DatePicker datePicker;
    private TextView txtCancel;
    private TextView txtOk;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private OnSelectedDateListener mOnSelectedDateListener;

    public interface OnSelectedDateListener {
        void onSelectedDate(String date);

    }

    public CalendarControl(Context context) {
        super(context, null);
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        this.mContext = context;
        initPopWindow();
        initContentView();
        initViewEvent();
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
        linearLayout.setPadding(40, 30, 40, 30);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_calerdar, null);
        view.setMinimumWidth(mDisplayMetrics.widthPixels * 4 / 5);
        datePicker = (DatePicker) view.findViewById(R.id.calendar_datePicker);
        txtCancel = (TextView) view.findViewById(R.id.calendar_txt_cancel);
        txtOk = (TextView) view.findViewById(R.id.calendar_txt_ok);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
    }

    private void initViewEvent() {
        txtCancel.setOnClickListener(txtCancelOnClickListener);
        txtOk.setOnClickListener(txtOkClickListener);
    }

    private View.OnClickListener txtOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnSelectedDateListener) {
                return;
            }
            //datePicker.getti
        }
    };

    private View.OnClickListener txtCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CalendarControl.this.dismiss();
        }
    };

    public void setOnSelectedDateListener(OnSelectedDateListener onSelectedDateListener) {
        this.mOnSelectedDateListener = onSelectedDateListener;
    }
}
