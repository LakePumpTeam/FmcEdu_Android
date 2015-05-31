package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.adapter.SelectListControlAdapter;
import com.fmc.edu.entity.CommonEntity;

import java.util.List;

/**
 * Created by Candy on 2015/5/3.
 */
public class SelectListControl extends PopupWindow {
    private SlideListView listView;
    private TextView txtTitle;
    private View mClickView;

    private Context mContext;
    private SelectListControlAdapter mSelectListControlAdapter;
    private OnItemSelectedListener mOnItemSelectedListener;
    private List<CommonEntity> mSourceList;
    private DisplayMetrics mDisplayMetrics;
    private String mTitle;
    private OnLoadMoreListener mOnLoadMoreListener;
    private View mFooterView;
    private boolean mIsLastPage;

    public interface OnItemSelectedListener {
        void onItemSelected(CommonEntity obj, View view);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public SelectListControl(Context context, List<CommonEntity> sourceList, boolean isLastPage, String title, View clickView) {
        super(context, null);
        mContext = context;
        mSourceList = sourceList;
        mTitle = title;
        mClickView = clickView;
        mIsLastPage = isLastPage;
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

        View view = LayoutInflater.from(mContext).inflate(R.layout.control_select_list, null);
        txtTitle = (TextView) view.findViewById(R.id.select_list_txt_title);
        listView = (SlideListView) view.findViewById(R.id.select_slide_list);
        txtTitle.setText(mTitle);
        linearLayout.addView(view);

        view.setMinimumHeight(mDisplayMetrics.heightPixels * 2 / 3);
        this.setContentView(linearLayout);

        bindListView();
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnLoadMoreListener(onLoadMoreListener);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (null == mOnItemSelectedListener) {
                return;
            }
            CommonEntity obj = (CommonEntity) parent.getAdapter().getItem(position);
            mOnItemSelectedListener.onItemSelected(obj, mClickView);
            dismiss();
        }
    };

    private void bindListView() {
        if (null == mSourceList || 0 == mSourceList.size()) {
            return;
        }

        if (null == listView.getAdapter()) {
            mSelectListControlAdapter = new SelectListControlAdapter(mContext, mSourceList);
            listView.setAdapter(mSelectListControlAdapter);
        }
    }

    private SlideListView.OnLoadMoreListener onLoadMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (null == mOnLoadMoreListener || mIsLastPage) {
                return;
            }
//            footerView.setVisibility(View.VISIBLE);
//            mFooterView = footerView;
            listView.setFooterViewVisible(true);
            mOnLoadMoreListener.onLoadMore();
        }
    };

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener sourceListener) {
        this.mOnItemSelectedListener = sourceListener;
    }

    public void setLoadMoreData(List<CommonEntity> data, boolean isLastPage) {
        mIsLastPage = isLastPage;
        mFooterView.setVisibility(View.GONE);
        mSelectListControlAdapter.addAllItems(data, false);
        mSelectListControlAdapter.notifyDataSetChanged();
    }

    public void setFooterViewFalse() {
        listView.setFooterViewVisible(true);
//        mFooterView.setVisibility(View.GONE);
    }
}
