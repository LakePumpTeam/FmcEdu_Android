package com.fmc.edu.customcontrol;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/3.
 */
public class AlertWindowControl extends PopupWindow {
    private Context mContext;
    private TextView txtTitle;
    private TextView txtContent;
    private Button btnOk;

    public AlertWindowControl(Context context) {
        super(context, null);
        this.mContext = context;
        initPopWindow();
        initContentView();
    }

    private void initPopWindow() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.setTouchable(false);
        ColorDrawable dw = new ColorDrawable(-000000);
        this.setBackgroundDrawable(dw);
    }

    private void initContentView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_alert_window, null);
        txtTitle = (TextView) view.findViewById(R.id.alert_window_txt_title);
        txtContent = (TextView) view.findViewById(R.id.alert_window_txt_content);
        btnOk = (Button) view.findViewById(R.id.alert_window_btn_ok);
        btnOk.setOnClickListener(btnOkOnClickListener);
        this.setContentView(view);
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
        this.showAsDropDown(parentView);
    }
}
