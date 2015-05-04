package com.fmc.edu.customcontrol;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/3.
 */
public class ProgressControl extends PopupWindow {
    private Context mContext;
    public ProgressControl(Context context) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_progress, null);
        this.setContentView(view);
    }
}
