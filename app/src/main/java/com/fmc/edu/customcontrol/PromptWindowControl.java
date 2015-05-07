package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/7.
 */
public class PromptWindowControl extends PopupWindow {
    private TextView txtCancel;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtOperator;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private OnOperateOnClickListener mOnOperateOnClickListener;

    public interface OnOperateOnClickListener {
        void onOperateOnClick();
    }

    public PromptWindowControl(Context context) {
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

        txtTitle = (TextView) view.findViewById(R.id.prompt_window_txt_title);
        txtContent = (TextView) view.findViewById(R.id.prompt_window_txt_content);
        txtCancel = (TextView) view.findViewById(R.id.prompt_window_txt_cancel);
        txtOperator = (TextView) view.findViewById(R.id.prompt_window_txt_operator);

        txtCancel.setOnClickListener(txtCancelOnClickListener);
        txtOperator.setOnClickListener(txtOperateClickListener);
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
            mOnOperateOnClickListener.onOperateOnClick();
        }
    };

    public void setOnOperateOnClickListener(OnOperateOnClickListener mOnOperateOnClickListener) {
        this.mOnOperateOnClickListener = mOnOperateOnClickListener;
    }

    public void showWindow(View parentView, String title, String message, String operaterName) {
        txtTitle.setText(title);
        txtContent.setText(message);
        txtOperator.setText(operaterName);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
