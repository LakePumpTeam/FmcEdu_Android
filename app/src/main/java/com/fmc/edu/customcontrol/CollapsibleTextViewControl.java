package com.fmc.edu.customcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.IInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;

import org.w3c.dom.ProcessingInstruction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Candy on 2015/5/26.
 */
public class CollapsibleTextViewControl extends LinearLayout {

    /**
     * default text show max lines
     */
    private static final int DEFAULT_MAX_LINE_COUNT = 5;

    private static final int COLLAPSIBLE_STATE_NONE = 0;
    private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;
    private Map<String, Integer> mDrawed = new HashMap<>();
    private int mCurrentPosition;
    private String mCurrentText;

    private TextView desc;
    private TextView descOp;

    private int mState;
    private boolean flag;
    private Context mContext;
    private String mPackUp = "收起";
    private String mSpread = "展开";
    private boolean mShowReadAll = false;


    public CollapsibleTextViewControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = inflate(context, R.layout.control_collapsible_textview, this);
        desc = (TextView) view.findViewById(R.id.desc_tv);
        descOp = (TextView) view.findViewById(R.id.desc_op_tv);
        view.setPadding(0, -1, 0, 0);
        descOp.setOnClickListener(readAllOnClickListener);
        initAttribute(attrs);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CollapsibleTextViewControl);
        mShowReadAll = typedArray.getBoolean(R.styleable.CollapsibleTextViewControl_isShowAll, false);
    }

    public void setTextContent(String charSequence, int position) {
        mCurrentText = charSequence;
        desc.setText(charSequence);
        mState = COLLAPSIBLE_STATE_SPREAD;
        mCurrentPosition = position;
        requestLayout();
    }

    private OnClickListener readAllOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            flag = false;

            // requestLayout();
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mShowReadAll) {
            return;
        }

        Object object = mDrawed.get(ConvertUtils.getString(mCurrentPosition));
        if (null == object) {
            if (desc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                descOp.setVisibility(View.GONE);
                mDrawed.put(ConvertUtils.getString(mCurrentPosition), 0);
                return;
            }
            descOp.setVisibility(View.VISIBLE);
            desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
            descOp.setText(mSpread);
            mDrawed.put(ConvertUtils.getString(mCurrentPosition), 1);
            return;
        }
        int obj = ConvertUtils.getInteger(object);
        desc.setText(mCurrentText);
        if (obj == 0) {
            descOp.setVisibility(View.GONE);
            return;
        }
        if (obj == 1) {
            descOp.setVisibility(View.VISIBLE);
            desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
            descOp.setText(mSpread);
        }

//
//        if (flag) {
//            return;
//        }
//        flag = true;
//        if (desc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
//            mState = COLLAPSIBLE_STATE_NONE;
//            descOp.setVisibility(View.GONE);
//            desc.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
//        } else {
//            post(new InnerRunnable());
//        }
    }

    class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mState == COLLAPSIBLE_STATE_SPREAD) {
                desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
                descOp.setVisibility(View.VISIBLE);
                descOp.setText(mSpread);
                mState = COLLAPSIBLE_STATE_SHRINKUP;
            } else if (mState == COLLAPSIBLE_STATE_SHRINKUP) {
                desc.setMaxLines(Integer.MAX_VALUE);
                descOp.setVisibility(View.VISIBLE);
                descOp.setText(mPackUp);
                mState = COLLAPSIBLE_STATE_SPREAD;
            }
        }
    }
}
