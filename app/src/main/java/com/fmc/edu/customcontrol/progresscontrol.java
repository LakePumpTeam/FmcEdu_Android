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

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/3.
 */
public class ProgressControl extends PopupWindow {
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private View mParentView;

    public ProgressControl(Context context, View parentView) {
        super(context, null);
        this.mContext = context;
        mDisplayMetrics = new DisplayMetrics();
        this.mParentView = parentView;
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_progress, null);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
    }

    public void showWindow() {
        if (this.isShowing()) {
            return;
        }
        this.showAtLocation(mParentView, Gravity.CENTER, 0, 0);
    }
}
