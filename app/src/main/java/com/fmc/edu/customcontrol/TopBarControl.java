package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.R;

public class TopBarControl extends LinearLayout {
    private Context mContext;
    private boolean backVisible;
    private String topTitle;
    private String operatorText;
    private int operatorImageSrc;

    private LinearLayout llBack;
    private LinearLayout llOperate;
    private LinearLayout llOperateText;
    private LinearLayout llOperateImage;
    private ImageView imgOperate;
    private TextView txtOperate;
    private TextView txtTitle;
    private OnOperateOnClickListener mOnOperateOnClickListener;

    public interface OnOperateOnClickListener {
        void onBackClick(View view);

        void onOperateClick(View v);
    }

    public TopBarControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttribute(attrs);
        initContentView();
        initData();
        bindControlEvent();
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray arrayList = mContext.obtainStyledAttributes(attrs, R.styleable.TopBarControl);
        backVisible = arrayList.getBoolean(R.styleable.TopBarControl_backVisible, false);
        topTitle = arrayList.getString(R.styleable.TopBarControl_topTitle);
        operatorText = arrayList.getString(R.styleable.TopBarControl_operatorText);
        operatorImageSrc = arrayList.getResourceId(R.styleable.TopBarControl_operatorImageSrc, 0);
    }

    private void initContentView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_top_bar, null);
        llBack = (LinearLayout) view.findViewById(R.id.top_bar_ll_back);
        llOperate = (LinearLayout) view.findViewById(R.id.top_bar_ll_operate);
        llOperateText = (LinearLayout) view.findViewById(R.id.top_bar_ll_operate_text);
        llOperateImage = (LinearLayout) view.findViewById(R.id.top_bar_ll_operate_image);
        imgOperate = (ImageView) view.findViewById(R.id.top_bar_img_operate);
        txtOperate = (TextView) view.findViewById(R.id.top_bar_txt_operate);
        txtTitle = (TextView) view.findViewById(R.id.top_bar_txt_title);
        this.addView(view);
        this.setBackgroundColor(getResources().getColor(R.color.activity_top_bar_color));
    }

    private void initData() {
        txtTitle.setText(topTitle);
        llBack.setVisibility(backVisible ? VISIBLE : GONE);
        if ((null == operatorText || "".equals(operatorText)) && operatorImageSrc == 0) {
            llOperate.setVisibility(GONE);
            return;
        }
        llOperate.setVisibility(VISIBLE);
        if (operatorImageSrc != 0) {
            llOperateImage.setVisibility(VISIBLE);
            llOperateText.setVisibility(GONE);
            imgOperate.setImageDrawable(getResources().getDrawable(operatorImageSrc));
            return;
        }
        llOperateImage.setVisibility(GONE);
        llOperateText.setVisibility(VISIBLE);
        txtOperate.setText(operatorText);
    }

    public void setTopBarText(String topTitle) {
        txtTitle.setText(topTitle);
    }

    public void setTopOperateTextVisible(boolean isVisible) {
        llOperateText.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setOperatorText(String operatorText) {
        txtOperate.setText(operatorText);
        llOperateText.setVisibility(VISIBLE);
        llOperateImage.setVisibility(GONE);
    }

    public void setTopOperateImgVisible(boolean isVisible) {
        llOperateImage.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void setOperateEnable(boolean isEnable) {
        llOperateImage.setEnabled(isEnable);
        llOperateText.setEnabled(isEnable);
    }

    public void setTopBarOperateImg(int imgId) {
        imgOperate.setImageDrawable(getResources().getDrawable(imgId));
        llOperateImage.setVisibility(VISIBLE);
    }

    private void bindControlEvent() {
        if (llBack.getVisibility() == VISIBLE) {
            llBack.setOnClickListener(llBackOnClickListener);
        }
        if (llOperate.getVisibility() == VISIBLE) {
            llOperate.setOnClickListener(operateOnClickListener);
        }
    }

    private OnClickListener llBackOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (llBack.getVisibility() != VISIBLE) {
                return;
            }
            if (null != mOnOperateOnClickListener) {
                mOnOperateOnClickListener.onBackClick(v);
            }
            ((Activity) mContext).finish();
        }
    };

    private OnClickListener operateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnOperateOnClickListener) {
                return;
            }
            mOnOperateOnClickListener.onOperateClick(v);
        }
    };

    public void setOnOperateOnClickListener(OnOperateOnClickListener mOnOperateOnClickListener) {
        this.mOnOperateOnClickListener = mOnOperateOnClickListener;
    }
}
