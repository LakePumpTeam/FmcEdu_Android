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
import com.fmc.edu.utils.ConvertUtils;

import java.util.Map;

import static com.fmc.edu.R.id.parent_detail_info_txt_closed;

/**
 * Created by Candy on 2015/5/16.
 */
public class ParentDetailInfoControl extends PopupWindow {
    private TextView txtCellphone;
    private TextView txtParentName;
    private TextView txtStudentName;
    private TextView txtRelation;
    private TextView txtSex;
    private TextView txtBirth;
    private TextView txtDeviceCode;
    private TextView txtDeviceCardNum;
    private TextView txtAddress;
    private TextView txtClosed;


    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private Map<String, Object> mData;

    public
    ParentDetailInfoControl(Context context, Map<String, Object> data) {
        super(context, null);
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        this.mContext = context;
        this.mData = data;
        initPopWindow();
        initContentView();
        initPageData();
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_parent_detail_info, null);
        view.setMinimumWidth(mDisplayMetrics.widthPixels * 4 / 5);

        txtCellphone = (TextView) view.findViewById(R.id.parent_detail_info_txt_cellphone);
        txtParentName = (TextView) view.findViewById(R.id.parent_detail_info_txt_parent_name);
        txtStudentName = (TextView) view.findViewById(R.id.parent_detail_info_txt_student_name);
        txtRelation = (TextView) view.findViewById(R.id.parent_detail_info_txt_relation);
        txtSex = (TextView) view.findViewById(R.id.parent_detail_info_txt_sex);
        txtBirth = (TextView) view.findViewById(R.id.parent_detail_info_txt_birth);
        txtDeviceCode = (TextView) view.findViewById(R.id.parent_detail_info_txt_device_code);
        txtDeviceCardNum = (TextView) view.findViewById(R.id.parent_detail_info_txt_device_card_num);
        txtAddress = (TextView) view.findViewById(R.id.parent_detail_info_txt_address);
        txtClosed = (TextView) view.findViewById(parent_detail_info_txt_closed);
        txtClosed.setOnClickListener(txtCancelOnClickListener);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
    }

    private void initPageData() {
        txtCellphone.setText(ConvertUtils.getString(mData.get("cellPhone")));
        txtParentName.setText(ConvertUtils.getString(mData.get("parentName")));
        txtStudentName.setText(ConvertUtils.getString(mData.get("studentName")));
        txtBirth.setText(ConvertUtils.getString(mData.get("studentBirth")));
        txtRelation.setText(ConvertUtils.getString(mData.get("relation")));
        txtDeviceCode.setText(ConvertUtils.getString(mData.get("braceletNumber"), ""));
        txtDeviceCardNum.setText(ConvertUtils.getString(mData.get("braceletCardNumber"), ""));
        txtAddress.setText(ConvertUtils.getString(mData.get("relation")));
        if (ConvertUtils.getBoolean("studentSex")) {
            txtSex.setText("男");
        } else {
            txtSex.setText("女");
        }
    }

    private View.OnClickListener txtCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public void showWindow(View parentView) {
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
