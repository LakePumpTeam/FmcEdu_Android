package com.fmc.edu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Candy on 2015/5/3.
 */
public class FmcBaseAdapter<T> extends BaseAdapter {
    public List<T> mItems;
    public Context mContext;

    public FmcBaseAdapter(Context context, List<T> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        if (null == mItems) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mItems) {
            return null;
        }
        return mItems.get(position);
    }

    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(T item) {
        this.mItems.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        this.mItems.add(position, item);
        notifyDataSetChanged();
    }

    public void addAllItems(List<T> items, boolean isClear) {
        if (null != this.mItems && isClear) {
            this.mItems.clear();
        }
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
