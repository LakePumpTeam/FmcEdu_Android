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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/3.
 */
public class AlertWindowControl extends PopupWindow {
    private TextView txtOk;
    private TextView txtTitle;
    private TextView txtContent;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;

    public AlertWindowControl(Context context) {
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
        linearLayout.setPadding(40, 30, 40, 30);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_alert_window, null);
        view.setMinimumWidth(mDisplayMetrics.widthPixels * 4 / 5);

        txtTitle = (TextView) view.findViewById(R.id.alert_window_txt_title);
        txtContent = (TextView) view.findViewById(R.id.alert_window_txt_content);
        txtOk = (TextView) view.findViewById(R.id.alert_window_txt_ok);

        txtOk.setOnClickListener(btnOkOnClickListener);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
    }

    private View.OnClickListener btnOkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public void showWindow(View parentView, String title, String message) {
        txtTitle.setText(title);
        txtContent.setText(message);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
