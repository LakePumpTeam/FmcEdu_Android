package com.fmc.edu.customcontrol;

import android.content.Context;
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
    private Context mContext;
    private View mFooterView;

    public interface OnLoadMoreListener {
        void onLoadMore(View footerView);

    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnScrollListener(this);
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_footer_view, null);
        mFooterView.setVisibility(GONE);
        addFooterView(mFooterView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (getLastVisiblePosition() == getCount() - 1) {
                if (null == mOnLoadMoreListener) {
                    return;
                }
                mOnLoadMoreListener.onLoadMore(mFooterView);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }
}
