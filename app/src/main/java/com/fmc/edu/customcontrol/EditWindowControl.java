package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;

/**
 * Created by Candy on 2015/6/6.
 */
public class EditWindowControl extends PopupWindow {

    private ImageView imgClose;
    private TextView txtLabel;
    private EditText txtContent;
    private Button btnOk;
    private View mParentView;

    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private OnOperateOnClickListener mOnOperateOnClickListener;

    public interface OnOperateOnClickListener {
        void onOperateOnClick(View view, String content);
    }

    public EditWindowControl(Context context) {
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_edit_window, null);
        view.setMinimumWidth(mDisplayMetrics.widthPixels * 9 / 10);

        txtLabel = (TextView) view.findViewById(R.id.control_edit_window_txt_label);
        txtContent = (EditText) view.findViewById(R.id.control_edit_window_edit_content);
        imgClose = (ImageView) view.findViewById(R.id.control_edit_window_img_close);
        btnOk = (Button) view.findViewById(R.id.control_edit_window_btn_ok);

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
            mOnOperateOnClickListener.onOperateOnClick(mParentView, ConvertUtils.getString(txtContent.getText()));
            dismiss();
        }
    };

    public void setOnOperateOnClickListener(OnOperateOnClickListener mOnOperateOnClickListener) {
        this.mOnOperateOnClickListener = mOnOperateOnClickListener;
    }

    public void showWindow(View parentView, String label, String content) {
        mParentView = parentView;
        txtLabel.setText(label);
        if (!StringUtils.isEmptyOrNull(content)) {
            txtContent.setText(content);
            txtContent.setSelection(content.length());
        }
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}