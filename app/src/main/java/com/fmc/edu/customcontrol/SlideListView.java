package com.fmc.edu.customcontrol;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/15.
 */
public class SlideListView extends ListView implements AbsListView.OnScrollListener {
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnScrollPrepListener mOnScrollPrepListener;
    private Context mContext;
    private View mFooterView;

    public interface OnLoadMoreListener {
        void onLoadMore(View footerView);
    }

    public interface OnScrollPrepListener {
        void onScrollPrep();
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnScrollListener(this);
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_footer_view, null);
        addFooterView(mFooterView);
        setFooterViewVisible(false);
        this.setDivider(new ColorDrawable(getResources().getColor(R.color.divider_bg_color)));
        this.setDividerHeight(0);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (null != mOnScrollPrepListener) {
            mOnScrollPrepListener.onScrollPrep();
        }
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (getLastVisiblePosition() == getCount() - 1) {
                if (null != mOnLoadMoreListener) {
                    mOnLoadMoreListener.onLoadMore(mFooterView);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }


    public void setFooterViewVisible(boolean isVisible) {
        if (null == mFooterView) {
            return;
        }
        if (isVisible) {
            mFooterView.setVisibility(View.VISIBLE);

        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }
}
