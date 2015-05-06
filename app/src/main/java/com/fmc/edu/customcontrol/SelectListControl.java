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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private Button btnLoadMore;
    private ListView listView;
    private TextView txtTitle;
    private View mClickView;
    private View mLoadMoreView;

    private Context mContext;
    private SelectListControlAdapter mSelectListControlAdapter;
    private OnItemSelectedListener mOnItemSelectedListener;
    private List<CommonEntity> mSourceList;
    private String mTitle;
    private final static int MAX_PAGE_SIZE = 15;
    private int mPageCount;


    public interface OnItemSelectedListener {
        void onItemSelected(CommonEntity obj, View view);
    }

    public SelectListControl(Context context, List<CommonEntity> sourceList, String title, View clickView) {
        super(context, null);
        mContext = context;
        mSourceList = sourceList;
        mTitle = title;
        mClickView = clickView;
        initPopWindow();
        initContentView();
    }

    private void initPopWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.setWidth(dm.widthPixels);
        this.setHeight( dm.heightPixels);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(-000000);
        this.setBackgroundDrawable(dw);
    }

    private void initContentView() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels);
        linearLayout.setPadding(40, 30, 40, 30);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));


        View view = LayoutInflater.from(mContext).inflate(R.layout.control_select_list, null);
        txtTitle = (TextView) view.findViewById(R.id.select_list_txt_title);
        listView = (ListView) view.findViewById(R.id.select_list_list);
        txtTitle.setText(mTitle);
        linearLayout.addView(view);


        view.setMinimumHeight(dm.heightPixels * 2 / 3);
        this.setContentView(linearLayout);

        bindListView();
        listView.setOnItemClickListener(onItemClickListener);
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

        mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.control_filter_load_more, null);
        btnLoadMore = (Button) mLoadMoreView.findViewById(R.id.filter_load_more_btn);
        if (null == btnLoadMore) {
            return;
        }
        btnLoadMore.setOnClickListener(loadMoreOnClickListener);
        boolean isAddFooter = mSourceList.size() >= MAX_PAGE_SIZE;
        if (isAddFooter) {
            listView.addFooterView(mLoadMoreView);
        }
        if (null == listView.getAdapter()) {
            mSelectListControlAdapter = new SelectListControlAdapter(mContext, mSourceList);
            listView.setAdapter(mSelectListControlAdapter);
        }
    }

    private void loadData() {
//        try {
//            MyIon.with(mContext).load(mMethodPath)
//                    .setBodyParameters(mParameter)
//                    .as(new MapTokenTypeUtils())
//                    .setCallback(new FutureCallback<Map<String, Object>>() {
//                        @Override
//                        public void onCompleted(Exception e, Map<String, Object> stringObjectMap) {
//                            progressControl.dismiss();
//                            if (!HttpTools.isRequestSuccessfully(e, stringObjectMap)) {
//                                ToastToolUtils.showShort(HttpTools.getStatusMsg(e, stringObjectMap));
//                            } else {
//                                List<Map<String, Object>> data = HttpTools.getListMap(stringObjectMap);
//                                handleData(data);
//                            }
//                        }
//                    });
//            progressControl.showAsDropDown(mView);
//        } catch (NetWorkUnAvailableException e) {
//            progressControl.dismiss();
//            e.printStackTrace();
//        }
    }

    private View.OnClickListener loadMoreOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPageCount++;
            loadData();
        }
    };

    public void setOnItemClickListener(OnItemSelectedListener sourceListener) {
        this.mOnItemSelectedListener = sourceListener;
    }

    public void setLoadMoreData(List<CommonEntity> data) {
        mSelectListControlAdapter.addAllItems(data, false);
        mSelectListControlAdapter.notifyDataSetChanged();
    }
}
