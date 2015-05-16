package com.fmc.edu;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fmc.edu.adapter.SelectListControlAdapter;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.customcontrol.SlideImageControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.CommonEntity;

import java.util.ArrayList;
import java.util.List;


public class TestActivity extends Activity {

    private SlideListView list;
    private SelectListControlAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        list = (SlideListView) findViewById(R.id.list);
        list.setOnLoadMoreListener(loadMoreListener);
        mAdapter = new SelectListControlAdapter(this, getList());
        list.setAdapter(mAdapter);
    }

    private SlideListView.OnLoadMoreListener loadMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            footerView.setVisibility(View.VISIBLE);
            int count = mAdapter.getCount();
            List<CommonEntity> lists = new ArrayList<CommonEntity>();
            for (int i = 0; i < 10; i++) {
                count++;
                CommonEntity item = new CommonEntity(String.valueOf(count), "name" + count);
                lists.add(item);
            }
            mAdapter.addAllItems(lists, false);
            footerView.setVisibility(View.GONE);
        }
    };

    private List<CommonEntity> getList() {
        List<CommonEntity> list = new ArrayList<CommonEntity>();
        for (int i = 0; i <= 20; i++) {
            CommonEntity item = new CommonEntity(String.valueOf(i), "name" + i);
            list.add(item);
        }
        return list;
    }
}
