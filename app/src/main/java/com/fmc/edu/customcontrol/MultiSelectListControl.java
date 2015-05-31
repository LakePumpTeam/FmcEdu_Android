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
import com.fmc.edu.adapter.MultiSelectListAdapter;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.entity.MultiCommonEntity;

import java.util.List;

/**
 * Created by Candy on 2015/5/30.
 */
public class MultiSelectListControl extends PopupWindow {
    private SlideListView listView;
    private TextView txtTitle;
    private LinearLayout llOk;

    private Context mContext;
    private List<MultiCommonEntity> mSourceList;
    private DisplayMetrics mDisplayMetrics;
    private String mTitle;
    private MultiSelectListAdapter mMultiSelectListAdapter;
    private OnAfterSelectedListener mOnAfterSelectedListener;

    public interface OnAfterSelectedListener {
        void onAfterSelected(List<MultiCommonEntity> list);
    }

    public MultiSelectListControl(Context context, List<MultiCommonEntity> sourceList, String title) {
        super(context, null);
        mContext = context;
        mSourceList = sourceList;
        mTitle = title;
        mDisplayMetrics = new DisplayMetrics();
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_multi_select_list, null);
        txtTitle = (TextView) view.findViewById(R.id.multi_select_list_txt_title);
        llOk = (LinearLayout) view.findViewById(R.id.multi_select_list_ll_ok);
        listView = (SlideListView) view.findViewById(R.id.multi_select_slide_list);

        txtTitle.setText(mTitle);
        linearLayout.addView(view);
        llOk.setOnClickListener(llOkOnClickListener);
        view.setMinimumHeight(mDisplayMetrics.heightPixels * 2 / 3);
        this.setContentView(linearLayout);
        bindListView();
    }

    private void bindListView() {
        if (null == mSourceList || 0 == mSourceList.size()) {
            return;
        }

        if (null == listView.getAdapter()) {
            mMultiSelectListAdapter = new MultiSelectListAdapter(mContext, mSourceList);
            listView.setAdapter(mMultiSelectListAdapter);
        }
    }

    private View.OnClickListener llOkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MultiSelectListControl.this.dismiss();
            if (null != mOnAfterSelectedListener) {
                mOnAfterSelectedListener.onAfterSelected(mMultiSelectListAdapter.getSelectedList());
            }
        }
    };

    public void setOnAfterSelectedListener(OnAfterSelectedListener onAfterSelectedListener) {
        this.mOnAfterSelectedListener = onAfterSelectedListener;
    }
}




