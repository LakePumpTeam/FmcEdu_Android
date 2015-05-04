package com.fmc.edu.customcontrol;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.adapter.SelectListControlAdapter;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.MapTokenTypeUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/3.
 */
public class SelectListControl extends DialogFragment {
    private Button btnLoadMore;
    private LinearLayout llTitle;
    private ListView listView;
    private TextView txtTitle;
    private View mListenerView;
    private View mView;
    private View mLoadMoreView;
    private ProgressControl progressControl;
    private Context mContext;
    private SelectListControlAdapter mSelectListControlAdapter;
    private OnItemSelectedListener mOnItemSelectedListener;
    private List<CommonEntity> mSourceList;
    private String mTitle;
    private String mMethodPath;
    private Map<String, List<String>> mParameter;
    private final static int MAX_PAGE_SIZE = 15;
    private int mPageCount;


    public interface OnItemSelectedListener {
        void onItemSelected(CommonEntity obj, View view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = android.support.v4.app.DialogFragment.STYLE_NO_TITLE;
        int theme = 0;
        setStyle(style, theme);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(mTitle);
        mView = inflater.inflate(R.layout.control_select_list, container, false);
        llTitle = (LinearLayout) mView.findViewById(R.id.select_list_ll_title);
        txtTitle = (TextView) mView.findViewById(R.id.select_list_txt_title);
        listView = (ListView) mView.findViewById(R.id.select_list_list);
        txtTitle.setText(mTitle);

        bindListView();
        listView.setOnItemClickListener(onItemClickListener);
        progressControl = new ProgressControl(mContext);
        return mView;
    }

    public void setSourceListener(OnItemSelectedListener sourceListener) {
        this.mOnItemSelectedListener = sourceListener;
    }

    public static SelectListControl newInstance(OnItemSelectedListener listener, Map<String, List<String>> parameter, String methodPath, View view, String title) {

        SelectListControl selectListControl = new SelectListControl();
        selectListControl.setSourceListener(listener);
        selectListControl.setListenerView(view);
        selectListControl.setDialogTitle(title);
        selectListControl.mMethodPath = methodPath;
        selectListControl.mParameter = parameter;
        selectListControl.mPageCount = 0;
        return selectListControl;
    }

    public void setAdapterDataSource(List<CommonEntity> dataSource) {
        this.mSourceList = dataSource;
    }

    public void setListenerView(View listenerView) {
        this.mListenerView = listenerView;
    }

    public void setDialogTitle(String dialogTitle) {
        this.mTitle = dialogTitle;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CommonEntity obj = (CommonEntity) parent.getAdapter().getItem(position);
            mOnItemSelectedListener.onItemSelected(obj, mListenerView);
            dismiss();
        }
    };

    private void bindListView() {
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
            mSelectListControlAdapter = new SelectListControlAdapter(mContext, null);
            listView.setAdapter(mSelectListControlAdapter);
        }
    }

    private void loadData() {
        try {
            getParameter();
            MyIon.with(mContext).load(mMethodPath)
                    .setBodyParameters(mParameter)
                    .as(new MapTokenTypeUtils())
                    .setCallback(new FutureCallback<Map<String, Object>>() {
                        @Override
                        public void onCompleted(Exception e, Map<String, Object> stringObjectMap) {
                            progressControl.dismiss();
                            if (!HttpTools.isRequestSuccessfully(e, stringObjectMap)) {
                                ToastToolUtils.showShort(HttpTools.getStatusMsg(e, stringObjectMap));
                            } else {
                                List<Map<String, Object>> data = HttpTools.getListMap(stringObjectMap);
                                handleData(data);
                            }
                        }
                    });
            progressControl.showAsDropDown(mView);
        } catch (NetWorkUnAvailableException e) {
            progressControl.dismiss();
            e.printStackTrace();
        }
    }

    private void getParameter() {
        List<String> filterKey = new ArrayList<String>();
        filterKey.add("");
        mParameter.put("filterkey", filterKey);
        List<String> pageCount = new ArrayList<String>();
        pageCount.add(String.valueOf(mPageCount));
        mParameter.put("pagecount", pageCount);
        List<String> filterSize = new ArrayList<String>();
        filterKey.add(String.valueOf(MAX_PAGE_SIZE));
        mParameter.put("pagesize", filterSize);
    }

    private void handleData(List<Map<String, Object>> data) {
        mSelectListControlAdapter.addAllItems(data, false);
        mSelectListControlAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener loadMoreOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPageCount++;
            loadData();
        }
    };
}
