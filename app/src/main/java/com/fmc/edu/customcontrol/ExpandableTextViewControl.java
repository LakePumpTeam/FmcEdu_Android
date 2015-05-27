package com.fmc.edu.customcontrol;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.StringUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

/**
 * Created by Candy on 2015-05-27.
 */
public class ExpandableTextViewControl extends LinearLayout {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    private static final int DEFAULT_MAX_LINES = 3;
    private static final String DEFAULT_EXPAND_STR = "全文";
    private static final String DEFAULT_Collapse_STR = "收起";

    protected TextView mTxtContent;
    protected TextView mTxtOperate; // Button to expand/collapse

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private boolean mIsExpand = false;
    private int mMaxCollapsedLines;
    private String mExpandStr;
    private String mCollapseStr;


    private Context mContext;

    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public ExpandableTextViewControl(Context context) {
        super(context, null);

    }

    public ExpandableTextViewControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextViewControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }


    @Override
    protected void onFinishInflate() {
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE || mIsExpand) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mTxtOperate.setVisibility(View.GONE);
        mTxtContent.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTxtContent.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        if (mCollapsed) {
            mTxtContent.setMaxLines(mMaxCollapsedLines);
        }
        mTxtOperate.setVisibility(View.VISIBLE);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.ms.square.android.expandabletextview.R.styleable.ExpandableTextView);
        mIsExpand = typedArray.getBoolean(R.styleable.ExpandableTextViewControl_isExpand, false);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, DEFAULT_MAX_LINES);
        mExpandStr = typedArray.getString(R.styleable.ExpandableTextViewControl_expand);
        mCollapseStr = typedArray.getString(R.styleable.ExpandableTextViewControl_collapse);
        mExpandStr = StringUtils.isEmptyOrNull(mExpandStr) ? DEFAULT_EXPAND_STR : mExpandStr;
        mCollapseStr = StringUtils.isEmptyOrNull(mExpandStr) ? DEFAULT_Collapse_STR : mCollapseStr;
        typedArray.recycle();
    }

    private void findViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_expandable_textview, null);
        mTxtContent = (TextView) view.findViewById(R.id.desc_tv);
        mTxtOperate = (TextView) view.findViewById(R.id.desc_op_tv);
        mTxtOperate.setText(mCollapsed ? mExpandStr : mCollapseStr);
        mTxtOperate.setOnClickListener(mTxtExpandOnClickListener);
        this.addView(view);
    }

    private View.OnClickListener mTxtExpandOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mTxtOperate.getVisibility() != View.VISIBLE) {
                return;
            }

            mCollapsed = !mCollapsed;
            mTxtOperate.setText(mCollapsed ? mExpandStr : mCollapseStr);
            if (mCollapsedStatus != null) {
                mCollapsedStatus.put(mPosition, mCollapsed);
            }

            if (mCollapsed) {
                mTxtContent.setMaxLines(DEFAULT_MAX_LINES);
            } else {
                mTxtContent.setMaxLines(Integer.MAX_VALUE);
            }
        }
    };

    public void setText(CharSequence text) {
        mRelayout = true;
        mTxtContent.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(CharSequence text, SparseBooleanArray collapsedStatus, int position) {
        setText(text, collapsedStatus, position, true);
    }

    public void setText(CharSequence text, SparseBooleanArray collapsedStatus, int position, boolean isExpand) {
        mIsExpand = isExpand;
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        mTxtOperate.setText(mCollapsed ? mExpandStr : mCollapseStr);
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    public CharSequence getText() {
        if (mTxtContent == null) {
            return "";
        }
        return mTxtContent.getText();
    }
}
